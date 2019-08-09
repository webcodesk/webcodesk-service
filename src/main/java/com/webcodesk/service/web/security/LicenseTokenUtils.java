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

package com.webcodesk.service.web.security;

import com.webcodesk.service.core.entity.User;
import com.webcodesk.service.core.entity.UserLicense;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class LicenseTokenUtils {

    @Value("${license.token.secret}")
    private String secret;
    @Value("${service.token.expiration}")
    private long expiration;

    public String generateToken(User user, UserLicense userLicense) {
        String subject = userLicense.getAccountLicenseId() + ";";
        subject += userLicense.getStartDate() + ";" + userLicense.getEndDate() + ";";
        subject += user.getFirsName() + ";" + user.getLastName() + ";" + user.getEmail() + ";";
        subject += System.currentTimeMillis();
        try {
            return Jwts.builder()
                    .setSubject(subject)
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                    .signWith(SignatureAlgorithm.HS512, secret.getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}
