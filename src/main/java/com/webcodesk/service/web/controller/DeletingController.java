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
import com.webcodesk.service.core.entity.MarketProject;
import com.webcodesk.service.core.entity.User;
import com.webcodesk.service.core.service.MarketService;
import com.webcodesk.service.core.service.StorageService;
import com.webcodesk.service.core.service.UserService;
import com.webcodesk.service.core.utils.SystemSets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/delete")
public class DeletingController {

    @Autowired
    private SystemSets systemSets;

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserService userService;

    @Autowired
    private MarketService marketService;

    @RequestMapping(path = "/project", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> deleteProject(@RequestParam("projectId") Long projectId,
                                                HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            User user = userService.findUserByEmail(principal.getName());
            if (user != null) {
                MarketProject marketProject = marketService.deleteProjectByIdAndAccountId(
                        projectId,
                        user.getId()
                );
                if (marketProject != null) {
                    storageService.deleteProjectDir(user.getId(), marketProject.getMarketProject().getName());
                }
                return ResponseEntity.ok("OK");
            } else {
                throw new ServiceException(systemSets.getError("accountNotFound"));
            }
        }
        throw new ServiceException(systemSets.getError("notAuthenticated"));
    }

}
