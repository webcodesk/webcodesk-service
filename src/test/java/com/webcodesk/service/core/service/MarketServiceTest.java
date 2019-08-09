package com.webcodesk.service.core.service;

import com.webcodesk.service.core.entity.MarketDao;
import com.webcodesk.service.core.model.MarketComponentProjection;
import com.webcodesk.service.core.model.MarketComponentTagProjection;
import com.webcodesk.service.web.WebcodeskServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebcodeskServiceApplication.class)
@Transactional
public class MarketServiceTest {

    @Autowired
    private MarketService marketService;

//    @Test
//    @Commit
//    public void updateProjectWithComponent() {
//        String projectName = "test_project";
//        String groupName = "test_group";
//        String componentName = "test_component";
//        String repoUrl = "https://test_repo";
//        String demoUrl = "https://test_demo";
//        String description = "This is test component in test report";
//        String tags = "Test, Test, Test";
//        Long userId = 60L;
//
//        marketService.updateProjectWithComponent(
//                projectName,
//                groupName,
//                componentName,
//                repoUrl,
//                demoUrl,
//                description,
//                tags,
//                userId
//        );
//
//    }


    @Test
    public void initialRequest() {
        List<MarketComponentProjection> componentsTop = marketService.getComponentsTop("javascript");
        List<MarketComponentTagProjection> allTags = marketService.getAllComponentsTags();
        MarketComponentProjection singleComponent = marketService.getComponentById(32L);

        System.out.println(componentsTop);
        System.out.println(allTags);

    }
}