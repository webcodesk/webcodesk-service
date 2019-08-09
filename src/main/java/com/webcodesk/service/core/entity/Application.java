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

import com.webcodesk.service.core.model.ApplicationVersionBean;

public class Application {

    private ApplicationVersionBean applicationVersion;

    private Application(Builder builder) {
        this.applicationVersion = builder.applicationVersion;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getVersion() {
        return this.applicationVersion.getVersion();
    }

    public static final class Builder {
        private ApplicationVersionBean applicationVersion;

        private Builder() {
            this.applicationVersion = new ApplicationVersionBean();
        }

        public Builder withApplicationVersion(ApplicationVersionBean val) {
            applicationVersion = val;
            return this;
        }

        public Application build() {
            return new Application(this);
        }
    }

}
