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

import com.webcodesk.service.core.model.MarketComponentBean;
import com.webcodesk.service.core.model.MarketProjectBean;
import com.webcodesk.service.core.model.UserAccountBean;

import java.util.ArrayList;
import java.util.List;

public class MarketProject {

    private MarketProjectBean marketProject;
    private List<MarketComponentBean> marketComponents;
    private MarketComponentBean foundMarketComponent;
    private UserAccountBean userAccount;
    private long totalDownloadCount;

    public MarketProjectBean getMarketProject() {
        return marketProject;
    }

    private void setMarketProject(MarketProjectBean marketProject) {
        this.marketProject = marketProject;
    }

    public List<MarketComponentBean> getMarketComponents() {
        return marketComponents;
    }

    private void setMarketComponents(List<MarketComponentBean> marketComponents) {
        this.marketComponents = marketComponents;
    }

    public MarketComponentBean getFoundMarketComponent() {
        return foundMarketComponent;
    }

    private void setFoundMarketComponent(MarketComponentBean foundMarketComponent) {
        this.foundMarketComponent = foundMarketComponent;
    }

    public UserAccountBean getUserAccount() {
        return userAccount;
    }

    private void setUserAccount(UserAccountBean userAccount) {
        this.userAccount = userAccount;
    }

    public long getTotalDownloadCount() {
        return totalDownloadCount;
    }

    private void setTotalDownloadCount(long totalDownloadCount) {
        this.totalDownloadCount = totalDownloadCount;
    }

    private MarketProject(Builder builder) {
        this.setMarketComponents(builder.marketComponents);
        this.setMarketProject(builder.marketProject);
        this.setFoundMarketComponent(builder.foundMarketComponent);
        this.setUserAccount(builder.userAccount);
        this.setTotalDownloadCount(builder.totalDownloadCount);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private MarketProjectBean marketProject;
        private List<MarketComponentBean> marketComponents;
        private MarketComponentBean foundMarketComponent;
        private UserAccountBean userAccount;
        private long totalDownloadCount;

        private Builder() {
            this.marketProject = new MarketProjectBean();
            this.marketComponents = new ArrayList<>();
            this.userAccount = new UserAccountBean();
            this.foundMarketComponent = null;
            this.totalDownloadCount = 0L;
        }

        public Builder withMarketProject(MarketProjectBean val) {
            this.marketProject = val;
            return this;
        }

        public Builder withMarketComponents(List<MarketComponentBean> val) {
            this.marketComponents = val;
            return this;
        }

        public Builder withFoundMarketComponent(MarketComponentBean val) {
            this.foundMarketComponent = val;
            return this;
        }

        public Builder withUserAccount(UserAccountBean val) {
            this.userAccount = val;
            return this;
        }

        public Builder withTotalDownloadCount(long value) {
            this.totalDownloadCount = value;
            return this;
        }

        public MarketProject build() {
            return new MarketProject(this);
        }
    }

}
