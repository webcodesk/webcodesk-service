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
public class LicenseBeanDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static class BeanMapper implements RowMapper<LicenseBean> {
        @Override
        public LicenseBean mapRow(ResultSet rs, int i) throws SQLException {
            return new LicenseBean(
                    rs.getLong("ID"),
                    rs.getString("NAME"),
                    rs.getInt("DURATION"),
                    rs.getInt("PRICE")
            );
        }
    }

    private static final BeanMapper mapperBean = new BeanMapper();

    public LicenseBean insert(LicenseBean bean) {
        String query =
                "INSERT INTO license (name, duration, price) " +
                "VALUES (?, ?, ?)";
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, bean.getName());
                statement.setInt(2, bean.getDuration());
                statement.setInt(3, bean.getPrice());
                return statement;
            }
        }, holder);
        Long primaryKey = (Long) holder.getKeys().get("id");
        bean.setId(primaryKey);
        return bean;
    }

    public void update(LicenseBean bean) {
        String query = "UPDATE license " +
                "SET name = ?, duration = ?, price = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(
                query,
                bean.getName(),
                bean.getDuration(),
                bean.getPrice(),
                bean.getId()
        );
    }

    public LicenseBean getById(Long id) {
        String query = "SELECT * FROM license WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, mapperBean, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

}
