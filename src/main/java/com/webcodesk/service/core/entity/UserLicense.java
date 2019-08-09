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

import com.webcodesk.service.core.model.AccountLicenseActivationBean;
import com.webcodesk.service.core.model.AccountLicenseBean;
import com.webcodesk.service.core.model.LicenseBean;

import java.util.ArrayList;
import java.util.List;

public class UserLicense {

//    private static int DAY_IN_MILLISECONDS = 60 * 60 * 24;

    private LicenseBean license;
    private AccountLicenseBean accountLicense;
    private List<AccountLicenseActivationBean> licenseActivations;

    private UserLicense(Builder builder) {
        setLicense(builder.license);
        setAccountLicense(builder.accountLicense);
        setLicenseActivations(builder.licenseActivations);
    }

    private void setLicense(LicenseBean license) {
        this.license = license;
    }

    private void setAccountLicense(AccountLicenseBean accountLicense) {
        this.accountLicense = accountLicense;
    }

    private void setLicenseActivations(List<AccountLicenseActivationBean> licenseActivations) {
        this.licenseActivations = licenseActivations;
    }

    public Long getAccountLicenseId() {
        return this.accountLicense.getId();
    }

    public String getLicenseName() {
        return this.license.getName();
    }

    public Long getStartDate() {
        return this.accountLicense.getStartDate().getTime();
    }

    public Long getEndDate() {
        return this.accountLicense.getEndDate().getTime();
    }

    public List<AccountLicenseActivationBean> getLicenseActivations() {
        return licenseActivations;
    }

    public boolean isExpired() {
        if(accountLicense != null) {
            long currentTimestamp = System.currentTimeMillis();
//            long startDate = accountLicense.getStartDate().getTime();
            long endDate = accountLicense.getEndDate().getTime();
            return endDate - currentTimestamp < 0;
        }
        return true;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private LicenseBean license;
        private AccountLicenseBean accountLicense;
        private List<AccountLicenseActivationBean> licenseActivations;

        private Builder() {
            this.license = new LicenseBean();
            this.accountLicense = new AccountLicenseBean();
            this.licenseActivations = new ArrayList<>();
        }

        public Builder withLicense(LicenseBean license) {
            this.license = license;
            return this;
        }

        public Builder withAccountLicense(AccountLicenseBean accountLicense) {
            this.accountLicense = accountLicense;
            return this;
        }

        public Builder withLicenseActivations(List<AccountLicenseActivationBean> licenseActivations) {
            this.licenseActivations = licenseActivations;
            return this;
        }

        public UserLicense build() {
            return new UserLicense(this);
        }
    }

}
