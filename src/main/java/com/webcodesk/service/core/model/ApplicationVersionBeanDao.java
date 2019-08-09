/*
 *
 *  * Copyright 2019 Oleksandr (Alex) Pustovalov
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.webcodesk.service.core.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class ApplicationVersionBeanDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static class BeanMapper implements RowMapper<ApplicationVersionBean> {
        @Override
        public ApplicationVersionBean mapRow(ResultSet rs, int i) throws SQLException {
            return new ApplicationVersionBean(
                    rs.getLong("ID"),
                    rs.getString("VERSION"),
                    rs.getTimestamp("CREATE_DATE")
            );
        }
    }

    private static final BeanMapper mapperBean = new BeanMapper();

    public ApplicationVersionBean insert(ApplicationVersionBean bean) {
        String query =
                "INSERT INTO application_version (name) " +
                "VALUES (?)";
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, bean.getVersion());
                return statement;
            }
        }, holder);
        Long primaryKey = (Long) holder.getKeys().get("id");
        bean.setId(primaryKey);
        return bean;
    }

    public ApplicationVersionBean getLastVersion() {
        String query =
                "select * from application_version " +
                        "where create_date = " +
                        "(select max(create_date) from application_version)";
        try {
            return jdbcTemplate.queryForObject(query, mapperBean);
        } catch (DataAccessException e) {
            return null;
        }
    }

}
