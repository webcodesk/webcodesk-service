package com.webcodesk.service.core.service;

import com.webcodesk.service.core.entity.Application;
import com.webcodesk.service.web.WebcodeskServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebcodeskServiceApplication.class)
@Transactional
public class ApplicationServiceTest {

    @Autowired
    ApplicationService applicationService;

    @Test
    public void getApplication() {
        Application test = applicationService.getApplication();
        assertEquals(test.getVersion(), "1.0.0-beta.1");
    }
}