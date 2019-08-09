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

package com.webcodesk.service.core.service;

import com.webcodesk.service.core.utils.StorageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class StorageService {

    @Value("${storage.directory}")
    private String storageDirectory;

    public void writeComponentPackage(
            Long userId,
            String projectName,
            String groupName,
            String fileName,
            byte[] fileBody
    ) throws IOException {
        String filePath = storageDirectory + "/" + userId + "/" + projectName + "/" + groupName + "/" + fileName;
        StorageUtils.writeBinaryFile(fileBody, filePath);
    }

    public void deleteProjectDir(
            Long userId,
            String projectName
    ) {
        String filePath = storageDirectory + "/" + userId + "/" + projectName;
        StorageUtils.deleteDirectory(filePath);
    }

}
