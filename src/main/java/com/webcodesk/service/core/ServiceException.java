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

package com.webcodesk.service.core;

import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {


    private HttpStatus httpStatus;

    public ServiceException() {
        httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ServiceException(String message) {
        super(message);
        httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ServiceException(Throwable cause) {
        super(cause);
        httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ServiceException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ServiceException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public ServiceException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
