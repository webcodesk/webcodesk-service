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
public class MarketComponentBeanDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static class BeanMapper implements RowMapper<MarketComponentBean> {
        @Override
        public MarketComponentBean mapRow(ResultSet rs, int i) throws SQLException {
            return new MarketComponentBean(
                    rs.getLong("ID"),
                    rs.getLong("PROJECT_ID"),
                    rs.getString("NAME"),
                    rs.getString("GROUP"),
                    rs.getString("DESCRIPTION"),
                    rs.getString("TAGS"),
                    rs.getTimestamp("CREATE_DATE"),
                    rs.getTimestamp("UPDATE_DATE"),
                    rs.getInt("DOWNLOAD_COUNT"),
                    rs.getString("LANG"),
                    rs.getString("TYPE")
            );
        }
    }

    private static final BeanMapper mapperBean = new BeanMapper();

    public MarketComponentBean insert(MarketComponentBean bean) {
        String query =
                "INSERT INTO market_component (project_id, name, \"group\", description, tags, lang, \"type\", tags_tokens) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, to_tsvector(?))";
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setLong(1, bean.getProjectId());
                statement.setString(2, bean.getName());
                statement.setString(3, bean.getGroup());
                statement.setString(4, bean.getDescription());
                statement.setString(5, bean.getTags());
                statement.setString(6, bean.getLang());
                statement.setString(7, bean.getType());
                statement.setString(8, bean.getTags());
                return statement;
            }
        }, holder);
        Long primaryKey = (Long) holder.getKeys().get("id");
        bean.setId(primaryKey);
        return bean;
    }

    public void update(MarketComponentBean bean) {
        String query = "UPDATE market_component " +
                "SET description = ?, tags = ?, lang = ?, \"type\" = ?, update_date = now(), tags_tokens = to_tsvector(?) " +
                "WHERE id = ?";
        jdbcTemplate.update(
                query,
                bean.getDescription(),
                bean.getTags(),
                bean.getLang(),
                bean.getType(),
                bean.getTags(),
                bean.getId()
        );
    }

    public void incrementDownloadCount(Long id) {
        String query = "update market_component set download_count = (download_count + 1) " +
                "where id = ?";
        jdbcTemplate.update(query, id);
    }

    public MarketComponentBean getById(Long id) {
        String query = "SELECT * FROM market_component WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, mapperBean, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public MarketComponentBean getByProjectIdAndGroupAndName(Long projectId, String group, String name) {
        String query = "SELECT * FROM market_component WHERE project_id = ? AND \"group\" = ? AND name = ?";
        try {
            return jdbcTemplate.queryForObject(query, mapperBean, projectId, group, name);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public int countByProjectIdAndGroupAndName(Long projectId, String group, String name) {
        String query = "SELECT count(*) FROM market_component WHERE project_id = ? AND \"group\" = ? AND name = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, projectId, group, name);
    }

    public List<MarketComponentBean> getByProjectId(Long projectId) {
        try {
            String query = "SELECT * FROM market_component WHERE project_id = ?";
            return jdbcTemplate.query(query, mapperBean, projectId);
        } catch (DataAccessException e) {
            return new ArrayList<>(0);
        }
    }

}
