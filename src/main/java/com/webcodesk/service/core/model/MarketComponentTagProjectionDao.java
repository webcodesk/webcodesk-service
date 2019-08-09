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
public class MarketComponentTagProjectionDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static class BeanMapper implements RowMapper<MarketComponentTagProjection> {
        @Override
        public MarketComponentTagProjection mapRow(ResultSet rs, int i) throws SQLException {
            return new MarketComponentTagProjection(
                    rs.getString("VALUE"),
                    rs.getInt("COMPONENTS_COUNT")
            );
        }
    }

    private static final BeanMapper mapperBean = new BeanMapper();

    /**
     *
     * @return
     */
    public List<MarketComponentTagProjection> getAllComponentsTags() {
        try {
            String query = "select token_list.value as value,\n" +
                    "       (\n" +
                    "         select count(*)\n" +
                    "         from market_component mc\n" +
                    "         where mc.tags_tokens @@ to_tsquery(token_list.value)\n" +
                    "       )                as components_count\n" +
                    "from (select distinct token_value as value\n" +
                    "      from (select unnest(array [token1 , token2, token3, token4, token5]) as token_value\n" +
                    "            from (select split_part(mc.tags, ' ', 1) as token1,\n" +
                    "                         split_part(mc.tags, ' ', 2) as token2,\n" +
                    "                         split_part(mc.tags, ' ', 3) as token3,\n" +
                    "                         split_part(mc.tags, ' ', 4) as token4,\n" +
                    "                         split_part(mc.tags, ' ', 5) as token5\n" +
                    "                  from market_component mc\n" +
                    "                 ) as token_table\n" +
                    "           ) as tag_list\n" +
                    "      where token_value <> '') as token_list";
            return jdbcTemplate.query(query, mapperBean);
        } catch (DataAccessException e) {
            return new ArrayList<>(0);
        }
    }

}
