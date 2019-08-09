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

import com.webcodesk.service.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserAccountDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserAccountDetailsService.class);

    private UserService userService;

    private UserDetails getUserDetails(String email, com.webcodesk.service.core.entity.User user) {
        if (user != null) {
            UserDetails userDetails =
                    new User(
                            user.getEmail(),
                            user.getPassword(),
                            AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities())
                    );
            return userDetails;
        }
        return new User(email, "", new ArrayList<>(0));
    }

    @Autowired
    public UserAccountDetailsService(UserService userAccountService) {
        this.userService = userAccountService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return getUserDetails(s, userService.findUserByEmail(s));
    }
}
