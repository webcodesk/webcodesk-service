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
import com.webcodesk.service.core.entity.UserLicense;
import com.webcodesk.service.core.service.MarketService;
import com.webcodesk.service.core.service.UserService;
import com.webcodesk.service.core.utils.SystemSets;
import com.webcodesk.service.web.form.ActivateLicenseForm;
import com.webcodesk.service.web.security.LicenseTokenUtils;
import com.webcodesk.service.web.view.ActivateLicense;
import com.webcodesk.service.web.view.ActivationLicense;
import com.webcodesk.service.web.view.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SystemSets systemSets;

    @Autowired
    private UserService userService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private LicenseTokenUtils licenseTokenUtils;

    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<UserProfile> getUserProfile(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            User user = userService.findUserByEmail(principal.getName());
            if (user != null) {
                UserProfile profile = new UserProfile();
                profile.setUser(user);
                List<UserLicense> userLicenses = userService.getUserLicensesById(user.getId());
                profile.setUserLicenses(userLicenses);
                return ResponseEntity.ok(profile);
            } else {
                throw new ServiceException(systemSets.getError("accountNotFound"));
            }
        }
        throw new ServiceException(systemSets.getError("notAuthenticated"));
    }

    @RequestMapping(path = "/projects", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<MarketProject>> getUserProjects(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            User user = userService.findUserByEmail(principal.getName());
            if (user != null) {
                return ResponseEntity.ok(marketService.getUserProjects(user.getId()));
            } else {
                throw new ServiceException(systemSets.getError("accountNotFound"));
            }
        }
        throw new ServiceException(systemSets.getError("notAuthenticated"));
    }

    @RequestMapping(path = "/activation-license", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ActivationLicense> getActivationLicense(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            User user = userService.findUserByEmail(principal.getName());
            if (user != null) {
                UserLicense userLicense = userService.getAvailableLicense(user.getId());
                if (userLicense != null) {
                    String token = licenseTokenUtils.generateToken(user, userLicense);
                    return ResponseEntity.ok(new ActivationLicense(token));
                } else {
                    return ResponseEntity.ok(new ActivationLicense(""));
                }
            } else {
                throw new ServiceException(systemSets.getError("accountNotFound"));
            }
        }
        throw new ServiceException(systemSets.getError("notAuthenticated"));
    }

    @RequestMapping(path = "/activate-license", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ActivateLicense> activateLicense(HttpServletRequest request,
                                                           @RequestBody ActivateLicenseForm form) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            try {
                Long activationId = userService.activateLicenseByAccountLicenseId(form.getAccountLicenseId());
                if (activationId != null) {
                    return ResponseEntity.ok(new ActivateLicense(activationId));
                } else {
                    throw new ServiceException(systemSets.getError("invalidLicenseActivation"));
                }
            } catch (ServiceException e) {
                throw new ServiceException(systemSets.getError("invalidLicenseActivation"));
            }
        }
        throw new ServiceException(systemSets.getError("notAuthenticated"));
    }

    @RequestMapping(path = "/deactivate-license", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> deactivateLicense(HttpServletRequest request,
                                                    @RequestBody ActivateLicenseForm form) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            try {
                userService.deactivateLicenseByActivationId(form.getActivationId());
                return ResponseEntity.ok("OK");
            } catch (ServiceException e) {
                e.printStackTrace();
                throw new ServiceException(systemSets.getError("invalidLicenseDeactivation"));
            }
        }
        throw new ServiceException(systemSets.getError("notAuthenticated"));
    }

}
