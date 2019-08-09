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

package com.webcodesk.service.web.view;

public class ApplicationInfo {

    private String applicationVersion;
    private Long userAmount;

    public ApplicationInfo() {
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public Long getUserAmount() {
        return userAmount;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public void setUserAmount(Long userAmount) {
        this.userAmount = userAmount;
    }
}
