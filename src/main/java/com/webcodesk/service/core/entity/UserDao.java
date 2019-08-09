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

package com.webcodesk.service.core.entity;

import com.webcodesk.service.core.ServiceException;
import com.webcodesk.service.core.model.*;
import com.webcodesk.service.core.utils.SystemSets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.UUID;
import org.apache.commons.validator.routines.EmailValidator;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class UserDao {

    private static int DAY_IN_MILLISECONDS = 60 * 60 * 24 * 1000; // 86400000

    // 1549957185905
    // 1548254218609

    @Autowired
    SystemSets systemSets;

    @Autowired
    UserAccountBeanDao userAccountBeanDao;

    @Autowired
    UserRegistrationBeanDao userRegistrationBeanDao;

    @Autowired
    LicenseBeanDao licenseBeanDao;

    @Autowired
    AccountLicenseBeanDao accountLicenseBeanDao;

    /**
     *
     * @param email
     */
    public void validateEmail(String email) {
        if(!EmailValidator.getInstance().isValid(email)){
            throw new ServiceException(systemSets.getError("invalidEmail"));
        }
        if(userAccountBeanDao.countByEmail(email) > 0){
            throw new ServiceException(systemSets.getError("emailExists"));
        }
    }

    /**
     *
     * @param email
     */
    public void validateNewUserRegistration(String email) {
        int recordsCount = userRegistrationBeanDao.countByEmail(email);
        if (recordsCount > 0) {
            throw new ServiceException(systemSets.getError("pendingRegistration"));
        }
    }

    /**
     *
     * @param email
     */
    public void validateRestoringRegistration(String email) {
        UserAccountBean userAccountBean = null;
        try {
            userAccountBean = userAccountBeanDao.getByEmail(email);
        } catch (Exception e) {
            throw new ServiceException(systemSets.getError("accountNotFound"));
        }
        if (userAccountBean != null) {
            int recordsCount = userRegistrationBeanDao.countByEmail(email);
            if (recordsCount > 0) {
                throw new ServiceException(systemSets.getError("pendingRegistration"));
            }
        } else {
            throw new ServiceException(systemSets.getError("accountNotFound"));
        }
    }

    /**
     *
     * @param email
     * @return User
     */
    public User addNewUserRegistration(String email) {
        UserRegistrationBean bean = new UserRegistrationBean();
        bean.setEmail(email);
        bean.setRecordId(UUID.randomUUID().toString());
        bean.setType(UserRegistrationBean.REGISTRATION_TYPE_NEW_ACCOUNT);
        UserRegistrationBean newBean = userRegistrationBeanDao.insert(bean);
        return User.newBuilder().userRegistration(newBean).build();
    }

    /**
     *
     * @param recordId
     * @return
     */
    public User getNewUserRegistration(String recordId) {
        UserRegistrationBean userRegistration =
                null;
        try {
            userRegistration = userRegistrationBeanDao.getByRecordIdAndType(recordId, UserRegistrationBean.REGISTRATION_TYPE_NEW_ACCOUNT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userRegistration != null) {
            UserAccountBean userAccount = new UserAccountBean();
            userAccount.setEmail(userRegistration.getEmail());
            return User.newBuilder().userAccount(userAccount).userRegistration(userRegistration).build();
        }
        return null;
    }

    /**
     *
     * @param email
     * @return User
     */
    public User addRestoringUserRegistration(String email) {
        UserRegistrationBean bean = new UserRegistrationBean();
        bean.setEmail(email);
        bean.setRecordId(UUID.randomUUID().toString());
        bean.setType(UserRegistrationBean.REGISTRATION_TYPE_NEW_PASSWORD);
        UserRegistrationBean newBean = userRegistrationBeanDao.insert(bean);
        return User.newBuilder().userRegistration(newBean).build();
    }

    /**
     *
     * @param recordId
     * @return
     */
    public User getRestoringUserRegistration(String recordId) {
        UserRegistrationBean userRegistration =
                null;
        try {
            userRegistration = userRegistrationBeanDao.getByRecordIdAndType(recordId, UserRegistrationBean.REGISTRATION_TYPE_NEW_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userRegistration != null) {
            UserAccountBean userAccount = new UserAccountBean();
            userAccount.setEmail(userRegistration.getEmail());
            return User.newBuilder().userAccount(userAccount).userRegistration(userRegistration).build();
        }
        return null;
    }

    /**
     *
     * @param pass
     * @param firstName
     * @param lastName
     * @param registrationRecordId
     * @return
     */
    public User createUser(String pass,
                           String firstName,
                           String lastName,
                           String registrationRecordId) {
        User userRegistration = this.getNewUserRegistration(registrationRecordId);
        if (userRegistration != null) {
            userRegistrationBeanDao.delete(userRegistration.getRegistrationId());
            // Create new user account
            UserAccountBean userAccountBean = new UserAccountBean();
            userAccountBean.setEmail(userRegistration.getRegistrationEmail());
            userAccountBean.setPassword(pass);
            userAccountBean.setFirsName(firstName);
            userAccountBean.setLastName(lastName);
            userAccountBean.setAuthorities("USER");
            UserAccountBean newUserAccountBean = userAccountBeanDao.insert(userAccountBean);

            // Add default trial license
            try {
                LicenseBean defaultLicenseBean = licenseBeanDao.getById(1L);

                AccountLicenseBean accountLicenseBean = new AccountLicenseBean();
                accountLicenseBean.setAccountId(userAccountBean.getId());
                accountLicenseBean.setLicenseId(defaultLicenseBean.getId());
                long currentTimestamp = System.currentTimeMillis();
                long duration = (long)defaultLicenseBean.getDuration();
                long endTimestamp = currentTimestamp + (duration * DAY_IN_MILLISECONDS);
                accountLicenseBean.setStartDate(new Timestamp(currentTimestamp));
                accountLicenseBean.setEndDate(new Timestamp(endTimestamp));
                accountLicenseBeanDao.insert(accountLicenseBean);

            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(systemSets.getError("missingDefaultLicense"));
            }

            return User.newBuilder().userAccount(newUserAccountBean).build();
        }
        throw new ServiceException(systemSets.getError("invalidRegistration"));
    }

    /**
     *
     * @param pass
     * @param registrationRecordId
     * @return
     */
    public User updateUser(String pass, String registrationRecordId) {
        User userRegistration = this.getRestoringUserRegistration(registrationRecordId);
        if (userRegistration != null) {
            userRegistrationBeanDao.delete(userRegistration.getRegistrationId());
            UserAccountBean userAccountBean = userAccountBeanDao.getByEmail(userRegistration.getEmail());
            if (userAccountBean != null) {
                userAccountBean.setPassword(pass);
                userAccountBeanDao.update(userAccountBean);
                return User.newBuilder().userAccount(userAccountBean).build();
            } else {
                throw new ServiceException(systemSets.getError("accountNotFound"));
            }
        } else {
            throw new ServiceException(systemSets.getError("invalidRegistration"));
        }
    }

    /**
     *
     * @param email
     * @return
     */
    public User findUserByEmail(String email) {
        User result = null;
        UserAccountBean userAccount = null;
        try {
            userAccount = userAccountBeanDao.getByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userAccount != null) {
            result = User.newBuilder().userAccount(userAccount).build();
        }
        return result;
    }

    /**
     *
     * @param id
     * @return
     */
    public User getUserById(Long id) {
        UserAccountBean userAccount = null;
        try {
            userAccount = userAccountBeanDao.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userAccount != null) {
            return User.newBuilder().userAccount(userAccount).build();
        }
        throw new ServiceException(systemSets.getError("noAccountById", String.valueOf(id)));
    }

    /**
     *
     * @return
     */
    public Long getUserAmount() {
        return this.userAccountBeanDao.countAll();
    }

}
