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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class UserRegistrationBeanDao {


    private static final Logger log = LoggerFactory.getLogger(UserRegistrationBeanDao.class);


    @Autowired
    JdbcTemplate jdbcTemplate;

    private static class UserRegistrationBeanMapper implements RowMapper<UserRegistrationBean> {

        @Override
        public UserRegistrationBean mapRow(ResultSet rs, int i) throws SQLException {
            return new UserRegistrationBean(
                    rs.getLong("ID"),
                    rs.getString("EMAIL"),
                    rs.getString("RECORD_ID"),
                    rs.getTimestamp("CREATE_DATE"),
                    rs.getString("TYPE")
            );
        }
    }

    private final static UserRegistrationBeanMapper mapperBean =
            new UserRegistrationBeanMapper();


    private void deleteOldRecords() {
        String queryDelete = "DELETE FROM user_registration WHERE create_date <= now() - interval '1 day'";
        jdbcTemplate.update(queryDelete);
    }

    public UserRegistrationBean insert(UserRegistrationBean bean){
        this.deleteOldRecords();
        String query = "INSERT INTO user_registration (email, record_id, type) VALUES (?, ?, ?)";
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, bean.getEmail());
                statement.setString(2, bean.getRecordId());
                statement.setString(3, bean.getType());
                return statement;
            }
        }, holder);
        Long primaryKey = (Long) holder.getKeys().get("id");
        bean.setId(primaryKey);
        return bean;
    }

    public UserRegistrationBean getByRecordIdAndType(String recordId, String type){
        this.deleteOldRecords();
        String query =
                "SELECT * FROM user_registration " +
                        "WHERE type = ? " +
                        "and record_id = ? " +
                        "and create_date >= now() - interval '1 day'";
        try {
            return jdbcTemplate.queryForObject(query, mapperBean, type, recordId);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void delete(Long id){
        String queryDelete = "DELETE FROM user_registration WHERE id = ?";
        jdbcTemplate.update(queryDelete, id);
    }

    public int countByEmail(String email) {
        this.deleteOldRecords();
        String query = "SELECT count(*) FROM user_registration WHERE email = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, email);
    }

}
