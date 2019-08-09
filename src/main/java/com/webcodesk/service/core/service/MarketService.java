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

package com.webcodesk.service.core.service;

import com.webcodesk.service.core.entity.MarketDao;
import com.webcodesk.service.core.entity.MarketProject;
import com.webcodesk.service.core.model.MarketComponentProjection;
import com.webcodesk.service.core.model.MarketComponentTagProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = EmptyResultDataAccessException.class)
public class MarketService {

    @Autowired
    private MarketDao marketDao;

    public void updateProjectWithComponent(
            String projectName,
            String groupName,
            String componentName,
            String repoUrl,
            String demoUrl,
            String description,
            String tags,
            String lang,
            String type,
            String license,
            Long userId
    ) {
        MarketProject marketProject =
                marketDao.findProjectByUserAndName(projectName, userId);

        if (marketProject != null) {
            MarketProject marketProjectWithComponent = marketDao.findComponentByProject(
                    groupName, componentName, marketProject.getMarketProject()
            );
            if (marketProjectWithComponent != null) {
                marketDao.updateProject(
                        marketProjectWithComponent.getMarketProject().getId(),
                        repoUrl,
                        demoUrl,
                        license
                );
                // there is such a component, it should be updated then
                marketDao.updateComponent(
                        marketProjectWithComponent.getFoundMarketComponent().getId(),
                        description,
                        tags,
                        lang,
                        type
                );
            } else {
                marketDao.updateProject(
                        marketProject.getMarketProject().getId(),
                        repoUrl,
                        demoUrl,
                        license
                );
                // this is a new component in the project
                marketDao.createNewComponentInExistingProject(
                        marketProject.getMarketProject().getId(),
                        groupName,
                        componentName,
                        description,
                        tags,
                        lang,
                        type
                );
            }
        } else {
            // this is a new component in the new project
            marketDao.createNewComponentInNewProject(
                    projectName,
                    groupName,
                    componentName,
                    description,
                    tags,
                    lang,
                    type,
                    userId,
                    repoUrl,
                    demoUrl,
                    license
            );
        }
    }

    public List<MarketComponentTagProjection> getAllComponentsTags() {
        return marketDao.getAllComponentsTags();
    }

    public List<MarketComponentProjection> getComponentsTop(String lang) {
        return marketDao.getComponentsTop(lang);
    }

    public List<MarketComponentProjection> findComponents(String searchText, String lang) {
        return marketDao.findComponents(searchText, lang);
    }

    public MarketProject getProjectById(Long projectId) {
        return marketDao.getProjectById(projectId);
    }

    public MarketComponentProjection getComponentById(Long componentId) {
        return marketDao.getComponentById(componentId);
    }

    public void incrementComponentDownloadCount(Long componentId) {
        marketDao.incrementComponentDownloadCount(componentId);
    }

    public List<MarketProject> getUserProjects(Long userId) {
        return marketDao.getProjectByAccountId(userId);
    }

    public MarketProject deleteProjectByIdAndAccountId(Long projectId, Long userId) {
        MarketProject foundProject = marketDao.findProjectByIdAndAccountId(projectId, userId);
        if (foundProject != null) {
            marketDao.deleteProjectById(foundProject.getMarketProject().getId());
        }
        return foundProject;
    }
}
