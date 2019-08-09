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
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class AccountLicenseActivationBeanDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static class BeanMapper implements RowMapper<AccountLicenseActivationBean> {
        @Override
        public AccountLicenseActivationBean mapRow(ResultSet rs, int i) throws SQLException {
            return new AccountLicenseActivationBean(
                    rs.getLong("ID"),
                    rs.getLong("ACCOUNT_LICENSE_ID"),
                    rs.getString("STATUS"),
                    rs.getTimestamp("ACTIVATE_DATE"),
                    rs.getTimestamp("DEACTIVATE_DATE")
            );
        }
    }

    private static final BeanMapper mapperBean = new BeanMapper();

    public AccountLicenseActivationBean insert(AccountLicenseActivationBean bean) {
        String query =
                "INSERT INTO account_license_activation (account_license_id, status, activate_date, deactivate_date) " +
                "VALUES (?, ?, ?, ?)";
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setLong(1, bean.getAccountLicenseId());
                statement.setString(2, bean.getStatus());
                statement.setTimestamp(3, bean.getActivateDate());
                statement.setTimestamp(4, bean.getDeactivateDate());
                return statement;
            }
        }, holder);
        Long primaryKey = (Long) holder.getKeys().get("id");
        bean.setId(primaryKey);
        return bean;
    }

    public void update(AccountLicenseActivationBean bean) {
        String query = "UPDATE account_license_activation " +
                "SET status = ?, deactivate_date = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(
                query,
                bean.getStatus(),
                bean.getDeactivateDate(),
                bean.getId()
        );
    }

    public AccountLicenseActivationBean getById(Long id) {
        String query = "SELECT * FROM account_license_activation WHERE id = ?";
        return jdbcTemplate.queryForObject(query, mapperBean, id);
    }

    public List<AccountLicenseActivationBean> getByAccountLicenseId(Long accountLicenseId) {
        String query = "SELECT * FROM account_license_activation WHERE account_license_id = ?";
        try {
            return jdbcTemplate.query(query, mapperBean, accountLicenseId);
        } catch (DataAccessException e) {
            return new ArrayList<>(0);
        }
    }

}
