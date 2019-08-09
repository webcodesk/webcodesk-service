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

package com.webcodesk.service.core.utils;

public class CacheItem<T> {

    private long timestamp = 0L;
    private T item = null;
    private long timeToLive;

    public CacheItem(long cacheTimeToLive) {
        this.timeToLive = cacheTimeToLive;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isExpired() {
        if (item != null) {
            long currentTimestamp = System.currentTimeMillis();
            return currentTimestamp - this.timestamp > this.timeToLive;
        }
        return true;
    }
}
