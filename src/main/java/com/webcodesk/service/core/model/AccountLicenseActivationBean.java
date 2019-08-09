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

package com.webcodesk.service.core.model;

import java.sql.Timestamp;

public class AccountLicenseActivationBean {

    private Long id;
    private Long accountLicenseId;
    private String status;
    private Timestamp activateDate;
    private Timestamp deactivateDate;

    public AccountLicenseActivationBean() {
    }

    public AccountLicenseActivationBean(Long id, Long accountLicenseId, String status, Timestamp activateDate, Timestamp deactivateDate) {
        this.id = id;
        this.accountLicenseId = accountLicenseId;
        this.status = status;
        this.activateDate = activateDate;
        this.deactivateDate = deactivateDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountLicenseId() {
        return accountLicenseId;
    }

    public void setAccountLicenseId(Long accountLicenseId) {
        this.accountLicenseId = accountLicenseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getActivateDate() {
        return activateDate;
    }

    public void setActivateDate(Timestamp activateDate) {
        this.activateDate = activateDate;
    }

    public Timestamp getDeactivateDate() {
        return deactivateDate;
    }

    public void setDeactivateDate(Timestamp deactivateDate) {
        this.deactivateDate = deactivateDate;
    }
}
