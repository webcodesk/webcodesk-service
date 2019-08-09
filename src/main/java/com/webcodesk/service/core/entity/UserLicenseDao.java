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

import com.webcodesk.service.core.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class UserLicenseDao {

    @Autowired
    private AccountLicenseBeanDao accountLicenseBeanDao;

    @Autowired
    private LicenseBeanDao licenseBeanDao;

    @Autowired
    private AccountLicenseActivationBeanDao accountLicenseActivationBeanDao;

    /**
     *
     * @param accountId
     * @return
     */
    public List<UserLicense> getAccountLicenses(Long accountId) {
        List<UserLicense> result = new ArrayList<>();
        List<AccountLicenseBean> accountLicenseBeans = accountLicenseBeanDao.getByAccountId(accountId);
        for(AccountLicenseBean accountLicenseBean : accountLicenseBeans) {
            LicenseBean licenseBean =
                    licenseBeanDao.getById(accountLicenseBean.getLicenseId());
            List<AccountLicenseActivationBean> activations =
                    accountLicenseActivationBeanDao.getByAccountLicenseId(accountLicenseBean.getId());
            result.add(UserLicense
                    .newBuilder()
                    .withLicense(licenseBean)
                    .withAccountLicense(accountLicenseBean)
                    .withLicenseActivations(activations)
                    .build());
        }
        return result;
    }

    /**
     *
     * @param accountId
     * @return
     */
    public UserLicense getAvailableLicense(Long accountId) {
        AccountLicenseBean accountLicenseBean = null;
        try {
            accountLicenseBean = accountLicenseBeanDao.getMostFarByAccountId(accountId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (accountLicenseBean != null) {
            LicenseBean licenseBean =
                    licenseBeanDao.getById(accountLicenseBean.getLicenseId());
            return UserLicense
                    .newBuilder()
                    .withAccountLicense(accountLicenseBean)
                    .withLicense(licenseBean)
                    .build();
        }
        return null;
    }

    /**
     *
     * @param licenseAccountId
     * @return
     */
    public Long activateLicenceByLicenseAccountId(Long licenseAccountId) {
        Long activationId = null;
        AccountLicenseBean accountLicenseBean = null;
        try {
            accountLicenseBean = accountLicenseBeanDao.getById(licenseAccountId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (accountLicenseBean != null) {
            AccountLicenseActivationBean accountLicenseActivationBean = new AccountLicenseActivationBean();
            accountLicenseActivationBean.setAccountLicenseId(licenseAccountId);
            accountLicenseActivationBean.setActivateDate(new Timestamp(System.currentTimeMillis()));
            accountLicenseActivationBean.setStatus("activated");
            AccountLicenseActivationBean newActivation =
                    accountLicenseActivationBeanDao.insert(accountLicenseActivationBean);
            if (newActivation != null) {
                activationId = newActivation.getId();
            }
        }
        return activationId;
    }

    /**
     *
     * @param activationId
     */
    public void deactivateLicenseByActivationId(Long activationId) {
        try {
            AccountLicenseActivationBean accountLicenseActivationBean =
                    accountLicenseActivationBeanDao.getById(activationId);
            if (accountLicenseActivationBean != null) {
                accountLicenseActivationBean.setStatus("deactivated");
                accountLicenseActivationBean.setDeactivateDate(new Timestamp(System.currentTimeMillis()));
                accountLicenseActivationBeanDao.update(accountLicenseActivationBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
