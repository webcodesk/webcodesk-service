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

import com.webcodesk.service.core.ServiceException;
import com.webcodesk.service.core.entity.User;
import com.webcodesk.service.core.entity.UserDao;
import com.webcodesk.service.core.entity.UserLicense;
import com.webcodesk.service.core.entity.UserLicenseDao;
import com.webcodesk.service.core.utils.SystemSets;
import freemarker.template.TemplateException;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = EmptyResultDataAccessException.class)
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final Pattern pattern = Pattern.compile("^[a-zA-Z]*$");

    @Value("${service.context.url}")
    private String contextUrl;

    @Autowired
    private SystemSets systemSets;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserLicenseDao userLicenseDao;

    @Autowired
    private MailTemplateService mailTemplateService;

    @Autowired
    private MailService mailService;

    /**
     * @param email
     * @return
     */
    public User findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    /**
     *
     * @param userId
     * @return
     */
    public List<UserLicense> getUserLicensesById(Long userId) {
        try {
            return userLicenseDao.getAccountLicenses(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param userId
     * @return
     */
    public UserLicense getAvailableLicense(Long userId) {
        try{
            return userLicenseDao.getAvailableLicense(userId);
        } catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param accountLicenseId
     * @return
     */
    public Long activateLicenseByAccountLicenseId(Long accountLicenseId) {
        return userLicenseDao.activateLicenceByLicenseAccountId(accountLicenseId);
    }

    /**
     *
     * @param activationLicenseId
     */
    public void deactivateLicenseByActivationId(Long activationLicenseId) {
        userLicenseDao.deactivateLicenseByActivationId(activationLicenseId);
    }

    /**
     *
     * @param email
     * @param doSendMail
     * @return
     */
    public String addNewRegistration(String email, boolean doSendMail) {
        userDao.validateEmail(email);
        userDao.validateNewUserRegistration(email);
        User userRegistration = null;
        try {
            userRegistration = userDao.addNewUserRegistration(email);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(systemSets.getError("SYS"));
        }
        //
        if (doSendMail) {
            try {
                Map<String, String> mailObjs = new HashMap<>(0);
                mailObjs.put("url", contextUrl + "/registration/confirm/" + userRegistration.getRegistrationRecordId());
                mailObjs.put("serviceContextUrl", contextUrl);
                String mailText = mailTemplateService.getText(mailObjs, "new-registration");
                mailService.sendMail(
                        "webcodedesk@gmail.com",
                        email,
                        "Webcodesk new account registration",
                        mailText);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ServiceException(systemSets.getError("SYS"));
            } catch (TemplateException e) {
                log.error(e.getMessage());
                throw new ServiceException(systemSets.getError("SYS"));
            } catch (MessagingException e) {
                log.error(e.getMessage());
                throw new ServiceException(systemSets.getError("SYS"));
            }
        }
        return userRegistration.getRegistrationRecordId();
    }

    /**
     *
     * @param registrationRecordId
     * @param password
     * @param firstName
     * @param lastName
     * @return
     */
    public User confirmNewRegistration(String registrationRecordId,
                                       String password,
                                       String firstName,
                                       String lastName) {
        if (firstName == null || firstName.length() <= 0) {
            throw new ServiceException(systemSets.getError("emptyFirstName"));
        }
        if (!pattern.matcher(firstName).matches()) {
            throw new ServiceException(systemSets.getError("invalidFirsName"));
        }
        if (firstName.length() > 500) {
            throw new ServiceException(systemSets.getError("longFirstName", "500"));
        }
        if (lastName == null || lastName.length() <= 0) {
            throw new ServiceException(systemSets.getError("emptyLastName"));
        }
        if (!pattern.matcher(lastName).matches()) {
            throw new ServiceException(systemSets.getError("invalidLastName"));
        }
        if (lastName.length() > 500) {
            throw new ServiceException(systemSets.getError("longLastName", "500"));
        }
        User userRegistration = null;
        try {
            userRegistration = userDao.getNewUserRegistration(registrationRecordId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(systemSets.getError("SYS"));
        }
        if (userRegistration != null) {
            userDao.validateEmail(userRegistration.getRegistrationEmail());
        } else {
            throw new ServiceException(systemSets.getError("invalidRegistration"));
        }
        // Insert new user
        return userDao.createUser(
                password,
                firstName,
                lastName,
                registrationRecordId
        );
    }
    /**
     *
     * @param email
     * @param doSendMail
     * @return
     */
    public String addRestoreRegistration(String email, boolean doSendMail) {
        if(!EmailValidator.getInstance().isValid(email)){
            throw new ServiceException(systemSets.getError("invalidEmail"));
        }
        userDao.validateRestoringRegistration(email);
        User userRegistration = null;
        try {
            userRegistration = userDao.addRestoringUserRegistration(email);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(systemSets.getError("SYS"));
        }
        //
        if (doSendMail) {
            try {
                Map<String, String> mailObjs = new HashMap<>(0);
                mailObjs.put("url", contextUrl + "/restoring/confirm/" + userRegistration.getRegistrationRecordId());
                mailObjs.put("serviceContextUrl", contextUrl);
                String mailText = mailTemplateService.getText(mailObjs, "password-recovery-request");
                mailService.sendMail(
                        "webcodedesk@gmail.com",
                        email,
                        "Webcodesk account recovery",
                        mailText);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ServiceException(systemSets.getError("SYS"));
            } catch (TemplateException e) {
                log.error(e.getMessage());
                throw new ServiceException(systemSets.getError("SYS"));
            } catch (MessagingException e) {
                log.error(e.getMessage());
                throw new ServiceException(systemSets.getError("SYS"));
            }
        }
        return userRegistration.getRegistrationRecordId();
    }

    /**
     *
     * @param registrationRecordId
     * @param password
     * @return
     */
    public User confirmRestoringRegistration(String registrationRecordId,
                                            String password) {
        try {
            User userRegistration = userDao.getRestoringUserRegistration(registrationRecordId);
            if (userRegistration == null) {
                throw new ServiceException(systemSets.getError("invalidRegistration"));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(systemSets.getError("invalidRegistration"));
        }
        // Insert new user
        return userDao.updateUser(
                password,
                registrationRecordId
        );
    }

    /**
     *
     * @return
     */
    public Long getUserAmount() {
        try {
            return userDao.getUserAmount();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(systemSets.getError("SYS"));
        }
    }
}
