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
import com.webcodesk.service.core.service.UserService;
import com.webcodesk.service.core.utils.EmailValidator;
import com.webcodesk.service.core.utils.SystemSets;
import com.webcodesk.service.web.form.UserRegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class RegistrationController {

    private static final EmailValidator emailValidator = new EmailValidator();

    @Autowired
    private SystemSets systemSets;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    public ResponseEntity<String> newRegistration(@RequestBody UserRegistrationForm form){
        if(form.getEmail() == null || form.getEmail().length() <= 0){
            throw new ServiceException(systemSets.getError("emptyEmail"));
        }
        if(form.getEmail().length() > 500){
            throw new ServiceException(systemSets.getError("longEmail", "500"));
        }
        if(!emailValidator.validate(form.getEmail())){
            throw new ServiceException(systemSets.getError("invalidEmail"));
        }
        userService.addNewRegistration(form.getEmail(), true);
        return ResponseEntity.ok("OK");
    }

    @RequestMapping(path = "/confirm-registration", method = RequestMethod.POST)
    public ResponseEntity<User> confirmNewRegistration(@RequestBody UserRegistrationForm form){
        User data = userService.confirmNewRegistration(
                form.getRecordId(),
                form.getPassword(),
                form.getFirstName(),
                form.getLastName()
        );
        return ResponseEntity.ok(data);
    }

    @RequestMapping(path = "/restoring", method = RequestMethod.POST)
    public ResponseEntity<String> newRestoring(@RequestBody UserRegistrationForm form){
        if(form.getEmail() == null || form.getEmail().length() <= 0){
            throw new ServiceException(systemSets.getError("emptyEmail"));
        }
        if(form.getEmail().length() > 500){
            throw new ServiceException(systemSets.getError("longEmail", "500"));
        }
        if(!emailValidator.validate(form.getEmail())){
            throw new ServiceException(systemSets.getError("invalidEmail"));
        }
        userService.addRestoreRegistration(form.getEmail(), true);
        return ResponseEntity.ok("OK");
    }

    @RequestMapping(path = "/restore-account", method = RequestMethod.POST)
    public ResponseEntity<User> confirmRestoring(@RequestBody UserRegistrationForm form){
        User data = userService.confirmRestoringRegistration(
                form.getRecordId(),
                form.getPassword()
        );
        return ResponseEntity.ok(data);
    }

}
