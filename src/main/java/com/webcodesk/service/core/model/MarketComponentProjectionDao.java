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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class MarketComponentProjectionDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static class BeanMapper implements RowMapper<MarketComponentProjection> {
        @Override
        public MarketComponentProjection mapRow(ResultSet rs, int i) throws SQLException {
            return new MarketComponentProjection(
                    rs.getLong("COMPONENT_ID"),
                    rs.getLong("PROJECT_ID"),
                    rs.getString("COMPONENT_GROUP"),
                    rs.getString("TAGS"),
                    rs.getString("DESCRIPTION"),
                    rs.getString("COMPONENT_NAME"),
                    rs.getTimestamp("LAST_UPDATE"),
                    rs.getString("LANG"),
                    rs.getString("TYPE"),
                    rs.getInt("DOWNLOAD_COUNT"),
                    rs.getString("PROJECT_NAME"),
                    rs.getString("REPO_URL"),
                    rs.getString("DEMO_URL"),
                    rs.getString("LICENSE"),
                    rs.getLong("USER_ID"),
                    rs.getString("USER_FIRST_NAME"),
                    rs.getString("USER_LAST_NAME"),
                    rs.getString("USER_EMAIL")
            );
        }
    }

    private static final BeanMapper mapperBean = new BeanMapper();

    /**
     *
     * @return
     */
    public List<MarketComponentProjection> getAllComponents() {
        try {
            String query = "select mc.id as component_id, " +
                    "mc.project_id as project_id, " +
                    "mc.group as component_group, " +
                    "mc.tags, " +
                    "mc.description, " +
                    "mc.name as component_name, " +
                    "mc.update_date as last_update, " +
                    "mc.lang, " +
                    "mc.type, " +
                    "mc.download_count, " +
                    "mp.name as project_name, " +
                    "mp.repo_url, " +
                    "mp.demo_url, " +
                    "mp.license, " +
                    "ua.id as user_id, " +
                    "ua.first_name as user_first_name, " +
                    "ua.last_name as user_last_name, " +
                    "ua.email as user_email " +
                    "from market_component mc, market_project mp, user_account ua " +
                    "where mc.project_id = mp.id and mp.account_id = ua.id and mp.status = 0";
            return jdbcTemplate.query(query, mapperBean);
        } catch (DataAccessException e) {
            return new ArrayList<>(0);
        }
    }

    public List<MarketComponentProjection> getComponentsTop(String lang) {
        try {
            String query = "select mc.id as component_id, " +
                    "mc.project_id as project_id, " +
                    "mc.group as component_group, " +
                    "mc.tags, " +
                    "mc.description, " +
                    "mc.name as component_name, " +
                    "mc.update_date as last_update, " +
                    "mc.lang, " +
                    "mc.type, " +
                    "mc.download_count, " +
                    "mp.name as project_name, " +
                    "mp.repo_url, " +
                    "mp.demo_url, " +
                    "mp.license, " +
                    "ua.id as user_id, " +
                    "ua.first_name as user_first_name, " +
                    "ua.last_name as user_last_name, " +
                    "ua.email as user_email " +
                    "from market_component mc, market_project mp, user_account ua " +
                    "where mc.project_id = mp.id and mp.account_id = ua.id and mp.status = 0 and mc.lang = ? " +
                    "order by mc.download_count " +
                    "limit 20";
            return jdbcTemplate.query(query, mapperBean, lang);
        } catch (DataAccessException e) {
            return new ArrayList<>(0);
        }
    }

    public List<MarketComponentProjection> findComponents(String searchText, String lang) {
        try {
            String query = "select mc.id as component_id, " +
                    "mc.project_id as project_id, " +
                    "mc.group as component_group, " +
                    "mc.tags, " +
                    "mc.description, " +
                    "mc.name as component_name, " +
                    "mc.update_date as last_update, " +
                    "mc.lang, " +
                    "mc.type, " +
                    "mc.download_count, " +
                    "mp.name as project_name, " +
                    "mp.repo_url, " +
                    "mp.demo_url, " +
                    "mp.license, " +
                    "ua.id as user_id, " +
                    "ua.first_name as user_first_name, " +
                    "ua.last_name as user_last_name, " +
                    "ua.email as user_email " +
                    "from market_component mc, market_project mp, user_account ua " +
                    "where mc.project_id = mp.id and mp.account_id = ua.id and mp.status = 0 and mc.lang = ? and mc.tags_tokens @@ to_tsquery('english', ?) " +
                    "order by mc.download_count";
            String[] tags = searchText != null && searchText.length() > 0 ? searchText.split(" ") : null;
            if (tags != null) {
                return jdbcTemplate.query(query, mapperBean, lang, String.join(" | ", tags));
            }
            return new ArrayList<>(0);
        } catch (DataAccessException e) {
            return new ArrayList<>(0);
        }
    }

    public MarketComponentProjection getByComponentId(Long componentId) {
        try {
            String query = "select mc.id as component_id, " +
                    "mc.project_id as project_id, " +
                    "mc.group as component_group, " +
                    "mc.tags, " +
                    "mc.description, " +
                    "mc.name as component_name, " +
                    "mc.update_date as last_update, " +
                    "mc.lang, " +
                    "mc.type, " +
                    "mc.download_count, " +
                    "mp.name as project_name, " +
                    "mp.repo_url, " +
                    "mp.demo_url, " +
                    "mp.license, " +
                    "ua.id as user_id, " +
                    "ua.first_name as user_first_name, " +
                    "ua.last_name as user_last_name, " +
                    "ua.email as user_email " +
                    "from market_component mc, market_project mp, user_account ua " +
                    "where mc.project_id = mp.id and mp.account_id = ua.id and mp.status = 0 and mc.id = ?";
            return jdbcTemplate.queryForObject(query, mapperBean, componentId);
        } catch (DataAccessException e) {
            return null;
        }
    }

}
