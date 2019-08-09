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
import com.webcodesk.service.core.entity.User;
import com.webcodesk.service.core.entity.UserLicense;
import com.webcodesk.service.core.service.MarketService;
import com.webcodesk.service.core.service.StorageService;
import com.webcodesk.service.core.service.UserService;
import com.webcodesk.service.core.utils.SystemSets;
import com.webcodesk.service.web.controller.util.PublishForm;
import com.webcodesk.service.web.form.ActivateLicenseForm;
import com.webcodesk.service.web.view.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/publish")
public class PublishingController {

    @Autowired
    private SystemSets systemSets;

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserService userService;

    @Autowired
    private MarketService marketService;

    @RequestMapping(path = "/component", method = RequestMethod.POST, consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> publishComponent(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("readme") MultipartFile readme,
                                                   @RequestParam(value = "picture", required = false) MultipartFile picture,
                                                   @RequestParam("projectName") String projectName,
                                                   @RequestParam("groupName") String groupName,
                                                   @RequestParam("componentName") String componentName,
                                                   @RequestParam("repoUrl") String repoUrl,
                                                   @RequestParam("demoUrl") String demoUrl,
                                                   @RequestParam("description") String description,
                                                   @RequestParam("tags") String tags,
                                                   @RequestParam("lang") String lang,
                                                   @RequestParam("type") String type,
                                                   @RequestParam("license") String license,
                                                   HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            User user = userService.findUserByEmail(principal.getName());
            if (user != null) {
                marketService.updateProjectWithComponent(
                        projectName,
                        groupName,
                        componentName,
                        repoUrl,
                        demoUrl,
                        description,
                        tags,
                        lang,
                        type,
                        license,
                        user.getId()
                );
            } else {
                throw new ServiceException(systemSets.getError("accountNotFound"));
            }
            try {
                if (file != null) {
                    storageService.writeComponentPackage(
                            user.getId(),
                            projectName,
                            groupName,
                            file.getOriginalFilename(),
                            file.getBytes()
                    );
                }
                if (readme != null) {
                    storageService.writeComponentPackage(
                            user.getId(),
                            projectName,
                            groupName,
                            readme.getOriginalFilename(),
                            readme.getBytes()
                    );
                }
                if (picture != null) {
                    storageService.writeComponentPackage(
                            user.getId(),
                            projectName,
                            groupName,
                            picture.getOriginalFilename(),
                            picture.getBytes()
                    );
                }
                return ResponseEntity.ok("OK");
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(systemSets.getError("SYS"));
            }
        }
        throw new ServiceException(systemSets.getError("notAuthenticated"));
    }

}
