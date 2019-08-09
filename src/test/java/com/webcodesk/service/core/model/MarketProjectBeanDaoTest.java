package com.webcodesk.service.core.model;

import com.webcodesk.service.web.WebcodeskServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebcodeskServiceApplication.class)
@Transactional(propagation = Propagation.REQUIRED)
public class MarketProjectBeanDaoTest {

    @Autowired
    private MarketProjectBeanDao marketProjectBeanDao;

    @Test
    public void insert() {
        String projectName = "test_project";
        String groupName = "test_group";
        String componentName = "test_component";
        String repoUrl = "https://test_repo";
        String demoUrl = "https://test_demo";
        String description = "This is test component in test report";
        String tags = "Test, Test, Test";
        Long userId = 60L;

        MarketProjectBean marketProjectBean = new MarketProjectBean();
        marketProjectBean.setName(projectName);
        marketProjectBean.setRepoUrl(repoUrl);
        marketProjectBean.setDemoUrl(demoUrl);
        marketProjectBean.setAccountId(userId);

        marketProjectBeanDao.insert(marketProjectBean);

    }

    @Test
    public void update() {
    }

    @Test
    public void getById() {
    }

    @Test
    public void getByNameAndAccountId() {
    }
}