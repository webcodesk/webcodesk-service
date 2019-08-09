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
public class UserAccountBeanDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static class BeanMapper implements RowMapper<UserAccountBean> {
        @Override
        public UserAccountBean mapRow(ResultSet rs, int i) throws SQLException {
            return new UserAccountBean(
                    rs.getLong("ID"),
                    rs.getString("EMAIL"),
                    rs.getString("PASSWORD"),
                    rs.getString("AUTHORITIES"),
                    rs.getTimestamp("CREATE_DATE"),
                    rs.getString("FIRST_NAME"),
                    rs.getString("LAST_NAME")
            );
        }
    }

    private static final BeanMapper mapperBean = new BeanMapper();

    public UserAccountBean insert(UserAccountBean bean) {
        String query =
                "INSERT INTO user_account (email, password, authorities, first_name, last_name) " +
                "VALUES (?, ?, ?, ?, ?)";
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, bean.getEmail());
                statement.setString(2, bean.getPassword());
                statement.setString(3, bean.getAuthorities());
                statement.setString(4, bean.getFirsName());
                statement.setString(5, bean.getLastName());
                return statement;
            }
        }, holder);
        Long primaryKey = (Long) holder.getKeys().get("id");
        bean.setId(primaryKey);
        return bean;
    }

    public void update(UserAccountBean bean) {
        String query = "UPDATE user_account " +
                "SET email = ?, password = ?, authorities = ?, first_name = ?, last_name = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(
                query,
                bean.getEmail(),
                bean.getPassword(),
                bean.getAuthorities(),
                bean.getFirsName(),
                bean.getLastName(),
                bean.getId()
        );
    }

    public UserAccountBean getById(Long id) {
        String query = "SELECT * FROM user_account WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, mapperBean, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public int countByEmail(String email) {
        String query = "SELECT count(*) FROM user_account WHERE email = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, email);
    }

    public Long countAll() {
        String query = "SELECT count(*) FROM user_account";
        return jdbcTemplate.queryForObject(query, Long.class);
    }

    public UserAccountBean getByEmail(String email) {
        String query = "SELECT * FROM user_account WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(query, mapperBean, email);
        } catch (DataAccessException e) {
            return null;
        }
    }

}
