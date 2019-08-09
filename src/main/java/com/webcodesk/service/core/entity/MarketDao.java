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

package com.webcodesk.service.core.entity;

import com.webcodesk.service.core.ServiceException;
import com.webcodesk.service.core.model.*;
import com.webcodesk.service.core.utils.CacheItem;
import com.webcodesk.service.core.utils.SystemSets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class MarketDao {

    @Autowired
    SystemSets systemSets;

    @Autowired
    private MarketProjectBeanDao marketProjectBeanDao;

    @Autowired
    private MarketComponentBeanDao marketComponentBeanDao;

    @Autowired
    private UserAccountBeanDao userAccountBeanDao;

    @Autowired
    private MarketComponentProjectionDao marketComponentProjectionDao;

    @Autowired
    private MarketComponentTagProjectionDao marketComponentTagProjectionDao;

    // 10 minutes cache
    private CacheItem<List<MarketProject>> allMarketProjectsCacheItem = new CacheItem<>(1000 * 60 * 10);

    /**
     *
     * @param groupName
     * @param componentName
     * @return
     */
    public MarketProject findComponentByProject(
            String groupName, String componentName, MarketProjectBean projectBean
    ) {
        MarketComponentBean marketComponentBean = marketComponentBeanDao.getByProjectIdAndGroupAndName(
                projectBean.getId(), groupName, componentName
        );
        if (marketComponentBean != null) {
            return MarketProject.newBuilder()
                    .withMarketProject(projectBean)
                    .withFoundMarketComponent(marketComponentBean)
                    .build();
        }
        return null;
    }

    /**
     *
     * @param projectName
     * @param userId
     * @return
     */
    public MarketProject findProjectByUserAndName(
            String projectName, Long userId
    ) {
        MarketProjectBean marketProjectBean = marketProjectBeanDao.getByNameAndAccountId(projectName, userId);
        if (marketProjectBean != null) {
            return MarketProject.newBuilder()
                    .withMarketProject(marketProjectBean)
                    .build();
        }
        return null;
    }

    /**
     *
     * @param projectName
     * @param groupName
     * @param componentName
     * @param description
     * @param tags
     * @param userId
     * @param repoUrl
     * @param demoUrl
     */
    public void createNewComponentInNewProject(
            String projectName,
            String groupName,
            String componentName,
            String description,
            String tags,
            String lang,
            String type,
            Long userId,
            String repoUrl,
            String demoUrl,
            String license
    ) {
        MarketProjectBean marketProjectBean = new MarketProjectBean();
        marketProjectBean.setName(projectName);
        marketProjectBean.setAccountId(userId);
        marketProjectBean.setRepoUrl(repoUrl);
        marketProjectBean.setDemoUrl(demoUrl);
        marketProjectBean.setLicense(license);
        try {
            marketProjectBean = marketProjectBeanDao.insert(marketProjectBean);

            MarketComponentBean marketComponentBean = new MarketComponentBean();
            marketComponentBean.setProjectId(marketProjectBean.getId());
            marketComponentBean.setGroup(groupName);
            marketComponentBean.setDescription(description);
            marketComponentBean.setTags(tags);
            marketComponentBean.setLang(lang);
            marketComponentBean.setType(type);
            marketComponentBean.setName(componentName);
            marketComponentBeanDao.insert(marketComponentBean);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(systemSets.getError("SYS"));
        }
    }

    /**
     *
     * @param projectId
     * @param groupName
     * @param componentName
     * @param description
     * @param tags
     * @param lang
     */
    public void createNewComponentInExistingProject(
            Long projectId,
            String groupName,
            String componentName,
            String description,
            String tags,
            String lang,
            String type
    ) {
        try {
            MarketComponentBean marketComponentBean = new MarketComponentBean();
            marketComponentBean.setProjectId(projectId);
            marketComponentBean.setGroup(groupName);
            marketComponentBean.setDescription(description);
            marketComponentBean.setTags(tags);
            marketComponentBean.setLang(lang);
            marketComponentBean.setType(type);
            marketComponentBean.setName(componentName);
            marketComponentBeanDao.insert(marketComponentBean);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(systemSets.getError("SYS"));
        }
    }

    /**
     *
     * @param projectId
     * @param repoUrl
     * @param demoUrl
     */
    public void updateProject(
            Long projectId,
            String repoUrl,
            String demoUrl,
            String license
    ) {
        try {
            MarketProjectBean marketProjectBean = new MarketProjectBean();
            marketProjectBean.setId(projectId);
            marketProjectBean.setRepoUrl(repoUrl);
            marketProjectBean.setDemoUrl(demoUrl);
            marketProjectBean.setLicense(license);
            marketProjectBeanDao.update(marketProjectBean);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(systemSets.getError("SYS"));
        }
    }

    /**
     *
     * @param description
     * @param tags
     * @param lang
     */
    public void updateComponent(
            Long componentId,
            String description,
            String tags,
            String lang,
            String type
    ) {
        try {
            MarketComponentBean marketComponentBean = new MarketComponentBean();
            marketComponentBean.setId(componentId);
            marketComponentBean.setDescription(description);
            marketComponentBean.setTags(tags);
            marketComponentBean.setLang(lang);
            marketComponentBean.setType(type);
            marketComponentBeanDao.update(marketComponentBean);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(systemSets.getError("SYS"));
        }
    }

    /**
     *
     * @return
     */
    public List<MarketComponentTagProjection> getAllComponentsTags() {
        return marketComponentTagProjectionDao.getAllComponentsTags();
    }

    /**
     *
     * @return
     */
    public List<MarketComponentProjection> getComponentsTop(String lang) {
        return marketComponentProjectionDao.getComponentsTop(lang);
    }

    /**
     *
     * @param searchText
     * @return
     */
    public List<MarketComponentProjection> findComponents(String searchText, String lang) {
        return marketComponentProjectionDao.findComponents(searchText, lang);
    }

    /**
     *
     * @param componentId
     * @return
     */
    public MarketComponentProjection getComponentById(Long componentId) {
        return marketComponentProjectionDao.getByComponentId(componentId);
    }

    /**
     *
     * @param projectId
     * @return
     */
    public MarketProject getProjectById(Long projectId) {
        MarketProject result = null;
        MarketProjectBean marketProjectBean = marketProjectBeanDao.getById(projectId);
        if (marketProjectBean != null) {
            UserAccountBean userAccountBean = userAccountBeanDao.getById(marketProjectBean.getAccountId());
            List<MarketComponentBean> marketComponentBeans = marketComponentBeanDao.getByProjectId(projectId);
            result = MarketProject.newBuilder()
                    .withMarketProject(marketProjectBean)
                    .withMarketComponents(marketComponentBeans)
                    .withUserAccount(userAccountBean)
                    .build();
        }
        return result;
    }

    /**
     *
     * @param componentId
     */
    public void incrementComponentDownloadCount(Long componentId) {
        marketComponentBeanDao.incrementDownloadCount(componentId);
    }

    /**
     *
     * @param projectId
     * @param accountId
     * @return
     */
    public MarketProject findProjectByIdAndAccountId(Long projectId, Long accountId) {
        MarketProject result = null;
        MarketProjectBean marketProjectBean = marketProjectBeanDao.getByIdAndAccountId(projectId, accountId);
        if (marketProjectBean != null) {
            result = MarketProject.newBuilder()
                    .withMarketProject(marketProjectBean)
                    .build();
        }
        return result;
    }

    /**
     *
     * @param accountId
     * @return
     */
    public List<MarketProject> getProjectByAccountId(Long accountId) {
        List<MarketProject> result = new ArrayList<>();
        List<MarketProjectBean> beans = marketProjectBeanDao.getByAccountId(accountId);
        if (beans != null && beans.size() > 0) {
            for(MarketProjectBean bean : beans) {
                result.add(MarketProject.newBuilder().withMarketProject(bean).build());
            }
        } else {

        }
        return result;
    }

    /**
     *
     * @param projectId
     */
    public void deleteProjectById(Long projectId) {
        marketProjectBeanDao.delete(projectId);
    }
}
