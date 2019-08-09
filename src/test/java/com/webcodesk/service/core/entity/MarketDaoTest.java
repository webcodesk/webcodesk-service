package com.webcodesk.service.core.entity;

import com.webcodesk.service.web.WebcodeskServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebcodeskServiceApplication.class)
@Transactional
public class MarketDaoTest {

    @Autowired
    private MarketDao marketDao;

//    @Test
//    public void findProjectByUserAndComponent() {
//        String projectName = "test_project";
//        String groupName = "test_group";
//        String componentName = "test_component";
//        String repoUrl = "https://test_repo";
//        String demoUrl = "https://test_demo";
//        String description = "This is test component in test report";
//        String tags = "Test, Test, Test";
//        Long userId = 60L;
//
//        MarketProject marketProject =
//                marketDao.findProjectByUserAndComponent(projectName, groupName, componentName, userId);
//        assertNull(marketProject);
//    }
//
//    @Test
//    @Commit
//    public void createNewComponent() {
//        String projectName = "test_project";
//        String groupName = "test_group";
//        String componentName = "test_component";
//        String repoUrl = "https://test_repo";
//        String demoUrl = "https://test_demo";
//        String description = "This is test component in test report";
//        String tags = "Test, Test, Test";
//        Long userId = 60L;
//
//        MarketProject marketProject =
//                marketDao.findProjectByUserAndComponent(projectName, groupName, componentName, userId);
//        assertNull(marketProject);
//        marketDao.createNewComponent(
//                projectName, groupName, componentName, description, tags, userId, repoUrl, demoUrl
//        );
//        marketProject =
//                marketDao.findProjectByUserAndComponent(projectName, groupName, componentName, userId);
//        assertNotNull(marketProject);
//    }
//
//    @Test
//    public void updateComponent() {
//        String projectName = "test_project";
//        String groupName = "test_group";
//        String componentName = "test_component";
//        String repoUrl = "https://test_repo";
//        String demoUrl = "https://test_demo";
//        String description = "This is test component in test report";
//        String tags = "Test, Test, Test";
//        Long userId = 60L;
//
//        String newRepoUrl = "https://test_repo";
//        String newDemoUrl = "https://test_demo";
//        String newDescription = "This is test component in test report";
//        String newTags = "Test, Test, Test";
//
//        MarketProject marketProject =
//                marketDao.findProjectByUserAndComponent(projectName, groupName, componentName, userId);
//        assertNull(marketProject);
//        marketDao.createNewComponent(
//                projectName, groupName, componentName, description, tags, userId, repoUrl, demoUrl
//        );
//        marketProject =
//                marketDao.findProjectByUserAndComponent(projectName, groupName, componentName, userId);
//        assertNotNull(marketProject);
//        marketDao.updateComponent(
//                marketProject, newDescription, newTags, newRepoUrl, newDemoUrl
//        );
//        marketProject =
//                marketDao.findProjectByUserAndComponent(projectName, groupName, componentName, userId);
//        assertNotNull(marketProject);
//        assertEquals(marketProject.getMarketProject().getRepoUrl(), newRepoUrl);
//        assertEquals(marketProject.getMarketProject().getDemoUrl(), newDemoUrl);
//        assertEquals(marketProject.getFoundMarketComponent().getDescription(), newDescription);
//        assertEquals(marketProject.getFoundMarketComponent().getTags(), newTags);
//    }
}