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

import com.webcodesk.service.core.model.UserAccountBean;
import com.webcodesk.service.core.model.UserRegistrationBean;

import java.beans.Transient;
import java.sql.Timestamp;

/**
 * User's composition should stay mutable only in the package-private scope
 */
public class User {

    private UserAccountBean userAccount;
    private UserRegistrationBean userRegistration;

    private User(Builder builder) {
        setUserAccount(builder.userAccount);
        setUserRegistration(builder.userRegistration);
    }

    private void setUserAccount(UserAccountBean userAccount) {
        this.userAccount = userAccount;
    }

    private void setUserRegistration(UserRegistrationBean userRegistration) {
        this.userRegistration = userRegistration;
    }

    public Long getId() {
        return userAccount.getId();
    }

    public void setId(Long id) {
        userAccount.setId(id);
    }

    public String getEmail() {
        return userAccount.getEmail();
    }

    public void setEmail(String email) {
        userAccount.setEmail(email);
    }

    @Transient
    public String getPassword() {
        return userAccount.getPassword();
    }

    public void setPassword(String password) {
        userAccount.setPassword(password);
    }

    public String getAuthorities() {
        return userAccount.getAuthorities();
    }

    public void setAuthorities(String authorities) {
        userAccount.setAuthorities(authorities);
    }

    public Timestamp getCreateDate() {
        return userAccount.getCreateDate();
    }

    public void setCreateDate(Timestamp createDate) {
        userAccount.setCreateDate(createDate);
    }

    public String getFirsName() {
        return userAccount.getFirsName();
    }

    public void setFirsName(String firsName) {
        userAccount.setFirsName(firsName);
    }

    public String getLastName() {
        return userAccount.getLastName();
    }

    public void setLastName(String lastName) {
        userAccount.setLastName(lastName);
    }

    public Long getRegistrationId() {
        return this.userRegistration.getId();
    }

    public String getRegistrationRecordId() {
        return this.userRegistration.getRecordId();
    }

    public String getRegistrationEmail() {
        return this.userRegistration.getEmail();
    }


    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private UserAccountBean userAccount;
        private UserRegistrationBean userRegistration;

        private Builder() {
            this.userAccount = new UserAccountBean();
            this.userRegistration = new UserRegistrationBean();
        }

        public Builder userAccount(UserAccountBean val) {
            userAccount = val;
            return this;
        }

        public Builder userRegistration(UserRegistrationBean val) {
            userRegistration = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
