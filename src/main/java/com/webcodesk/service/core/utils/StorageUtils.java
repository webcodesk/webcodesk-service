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

import org.springframework.util.FileSystemUtils;

import java.io.*;

public class StorageUtils {

    public static void writeBinaryFile(byte[] fileBody, String filePath) throws IOException {
        String directoryPath = null;
        if (filePath != null && filePath.lastIndexOf("/") >= 0) {
            int pos = filePath.lastIndexOf("/");
            directoryPath = filePath.substring(0, pos);
            File checkDir = new File(directoryPath);
            if (checkDir.exists() || checkDir.mkdirs()) {
                OutputStream writer = null;
                try {
                    writer = new BufferedOutputStream(new FileOutputStream(filePath));
                    writer.write(fileBody);
                } finally {
                    if (writer != null) {
                        try {
                            writer.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                throw new IOException("Can not make dirs for file: " + filePath);
            }
        } else {
            throw new IOException("Path is not file");
        }
    }

    public static boolean deleteDirectory(String dirPath) {
        File dirToDelete = new File(dirPath);
        return FileSystemUtils.deleteRecursively(dirToDelete);
    }

}
