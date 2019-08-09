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
import java.util.*;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class AccountLicenseBeanDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static class BeanMapper implements RowMapper<AccountLicenseBean> {
        @Override
        public AccountLicenseBean mapRow(ResultSet rs, int i) throws SQLException {
            return new AccountLicenseBean(
                    rs.getLong("ID"),
                    rs.getLong("ACCOUNT_ID"),
                    rs.getTimestamp("START_DATE"),
                    rs.getTimestamp("END_DATE"),
                    rs.getLong("LICENSE_ID")
            );
        }
    }

    private static final BeanMapper mapperBean = new BeanMapper();

    public AccountLicenseBean insert(AccountLicenseBean bean) {
        String query =
                "INSERT INTO account_license (account_id, start_date, end_date, license_id) " +
                "VALUES (?, ?, ?, ?)";
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setLong(1, bean.getAccountId());
                statement.setTimestamp(2, bean.getStartDate());
                statement.setTimestamp(3, bean.getEndDate());
                statement.setLong(4, bean.getLicenseId());
                return statement;
            }
        }, holder);
        Long primaryKey = (Long) holder.getKeys().get("id");
        bean.setId(primaryKey);
        return bean;
    }

//    public void update(AccountLicenseBean bean) {
//        String query = "UPDATE account_license " +
//                "SET name = ?, duration = ?, price = ? " +
//                "WHERE id = ?";
//        jdbcTemplate.update(
//                query,
//                bean.getName(),
//                bean.getDuration(),
//                bean.getPrice(),
//                bean.getId()
//        );
//    }

    public AccountLicenseBean getById(Long id) {
        String query = "SELECT * FROM account_license WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, mapperBean, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public List<AccountLicenseBean> getByAccountId(Long accountId) {
        try {
            String query = "SELECT * FROM account_license WHERE account_id = ?";
            return jdbcTemplate.query(query, mapperBean, accountId);
        } catch (DataAccessException e) {
            return new ArrayList<>(0);
        }
    }

    public AccountLicenseBean getMostFarByAccountId(Long accountId) {
        String query = "SELECT * FROM account_license where end_date >= now() and account_id = ? order by end_date desc";
        List<AccountLicenseBean> result = jdbcTemplate.query(query, mapperBean, accountId);
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

}
