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

package com.webcodesk.service.web.controller;

import com.webcodesk.service.core.ServiceException;
import com.webcodesk.service.core.entity.Application;
import com.webcodesk.service.core.entity.MarketProject;
import com.webcodesk.service.core.model.MarketComponentProjection;
import com.webcodesk.service.core.model.MarketComponentTagProjection;
import com.webcodesk.service.core.service.ApplicationService;
import com.webcodesk.service.core.service.MarketService;
import com.webcodesk.service.core.service.UserService;
import com.webcodesk.service.core.utils.SystemSets;
import com.webcodesk.service.web.view.ApplicationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserService userService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private SystemSets systemSets;

    @Value("${storage.directory}")
    private String storageDirectory;

    @RequestMapping(path = "/application-info", method = RequestMethod.GET)
    public ResponseEntity<ApplicationInfo> getApplicationInfo(HttpServletRequest request){
        ApplicationInfo data = new ApplicationInfo();
        try {
            Application application = applicationService.getApplication();
            if (application != null) {
                data.setApplicationVersion(application.getVersion());
            }
        } catch (Exception e) {
            // do nothing
        }
        return ResponseEntity.ok(data);
    }

    @RequestMapping(path = "/user-amount", method = RequestMethod.GET)
    public ResponseEntity<ApplicationInfo> getUserAmount(HttpServletRequest request){
        ApplicationInfo data = new ApplicationInfo();
        try {
            Long userAmount = userService.getUserAmount();
            if (userAmount != null) {
                data.setUserAmount(userAmount);
            }
        } catch (Exception e) {
            // do nothing
        }
        return ResponseEntity.ok(data);
    }

    @RequestMapping(path = "/components/top", method = RequestMethod.GET)
    public ResponseEntity<List<MarketComponentProjection>> getComponentsTop(
            @RequestParam("lang") String lang
    ){
        List<MarketComponentProjection> data = new ArrayList<>();
        try {
            data = marketService.getComponentsTop(lang);
        } catch (Exception e) {
            // do nothing
        }
        return ResponseEntity.ok(data);
    }

    @RequestMapping(path = "/components/tags", method = RequestMethod.GET)
    public ResponseEntity<List<MarketComponentTagProjection>> getComponentsTags(){
        List<MarketComponentTagProjection> data = new ArrayList<>();
        try {
            data = marketService.getAllComponentsTags();
        } catch (Exception e) {
            // do nothing
        }
        return ResponseEntity.ok(data);
    }

    @RequestMapping(path = "/components/find", method = RequestMethod.GET)
    public ResponseEntity<List<MarketComponentProjection>> findComponents(
            @RequestParam("searchText") String searchText,
            @RequestParam("lang") String lang
    ){
        List<MarketComponentProjection> data = new ArrayList<>();
        try {
            data = marketService.findComponents(searchText, lang);
        } catch (Exception e) {
            // do nothing
        }
        return ResponseEntity.ok(data);
    }

    @RequestMapping(path = "/project", method = RequestMethod.GET)
    public ResponseEntity<MarketProject> getProjectById(
            @RequestParam("projectId") Long projectId
    ){
        MarketProject data = MarketProject.newBuilder().build();
        try {
            data = marketService.getProjectById(projectId);
        } catch (Exception e) {
            // do nothing
        }
        return ResponseEntity.ok(data);
    }

    @RequestMapping(value = "/component/package", method = RequestMethod.GET)
    public void getComponentPicture(
            @RequestParam("userId") Long userId,
            @RequestParam("projectName") String projectName,
            @RequestParam("groupName") String groupName,
            @RequestParam("componentName") String componentName,
            @RequestParam("componentId") Long componentId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        InputStream is = null;
        OutputStream os = null;
        try {
                String packagePath = storageDirectory +
                        "/" +
                        userId +
                        "/" +
                        projectName +
                        "/" +
                        groupName +
                        "/" +
                        componentName + ".tar.gz";
                is = new FileInputStream(packagePath);
                os = response.getOutputStream();
                response.setHeader(
                        "Content-Disposition",
                        String.format("attachment; filename=\"%s.tar.gz\"", componentName)
                        );
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                StreamUtils.copy(is, response.getOutputStream());
                marketService.incrementComponentDownloadCount(componentId);
        } catch (IOException e) {
            // do nothing
            // throw new ServiceException(systemSets.getError("SYS"));
        } finally {
            if(os != null){
                try{
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is != null){
                try{
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
