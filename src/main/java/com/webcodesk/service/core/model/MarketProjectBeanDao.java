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
public class MarketProjectBeanDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static class BeanMapper implements RowMapper<MarketProjectBean> {
        @Override
        public MarketProjectBean mapRow(ResultSet rs, int i) throws SQLException {
            return new MarketProjectBean(
                    rs.getLong("ID"),
                    rs.getString("NAME"),
                    rs.getLong("ACCOUNT_ID"),
                    rs.getString("REPO_URL"),
                    rs.getString("DEMO_URL"),
                    rs.getTimestamp("CREATE_DATE"),
                    rs.getString("LICENSE"),
                    rs.getInt("STATUS")
            );
        }
    }

    private static final BeanMapper mapperBean = new BeanMapper();

    public MarketProjectBean insert(MarketProjectBean bean) {
        String query =
                "INSERT INTO market_project (name, account_id, repo_url, demo_url, license) " +
                        "VALUES (?, ?, ?, ?, ?)";
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, bean.getName());
                statement.setLong(2, bean.getAccountId());
                statement.setString(3, bean.getRepoUrl());
                statement.setString(4, bean.getDemoUrl());
                statement.setString(5, bean.getLicense());
                return statement;
            }
        }, holder);
        Long primaryKey = (Long) holder.getKeys().get("id");
        bean.setId(primaryKey);
        return bean;
    }

    public void update(MarketProjectBean bean) {
        String query = "UPDATE market_project " +
                "SET repo_url = ?, demo_url = ?, license = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(
                query,
                bean.getRepoUrl(),
                bean.getDemoUrl(),
                bean.getLicense(),
                bean.getId()
        );
    }

    public void delete(Long id) {
        String query = "DELETE FROM market_project " +
                "WHERE id = ?";
        jdbcTemplate.update(
                query,
                id
        );
    }

    public MarketProjectBean getById(Long id) {
        String query = "SELECT * FROM market_project WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, mapperBean, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public MarketProjectBean getByIdAndAccountId(Long projectId, Long accountId) {
        String query = "SELECT * FROM market_project WHERE id = ? AND account_id = ?";
        try {
            return jdbcTemplate.queryForObject(query, mapperBean, projectId, accountId);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public MarketProjectBean getByNameAndAccountId(String name, Long accountId) {
        String query = "SELECT * FROM market_project WHERE name = ? AND account_id = ?";
        try {
            return jdbcTemplate.queryForObject(query, mapperBean, name, accountId);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public int countByNameAndAccountId(String name, Long accountId) {
        String query = "SELECT count(*) FROM market_project WHERE name = ? AND account_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, name, accountId);
    }

    public List<MarketProjectBean> getByAccountId(Long accountId) {
        try {
            String query = "select * " +
                    "from market_project " +
                    "where account_id = ? and status = 0";
            return jdbcTemplate.query(query, mapperBean, accountId);
        } catch (DataAccessException e) {
            return new ArrayList<>(0);
        }
    }

}
