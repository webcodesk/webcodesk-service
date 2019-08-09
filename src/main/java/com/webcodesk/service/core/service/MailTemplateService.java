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

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

@Service
public class MailTemplateService {

    private Configuration freemarkerCfg;

    public MailTemplateService() {
        freemarkerCfg = new Configuration(Configuration.VERSION_2_3_23);
        freemarkerCfg.setObjectWrapper(new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_22).build());
        freemarkerCfg.setDefaultEncoding("UTF-8");
        freemarkerCfg.setLocale(Locale.US);
        freemarkerCfg.setTemplateLoader(new ClassTemplateLoader(MailTemplateService.class, "/mail_templates"));
    }

    public String getText(Map<String, ?> variables, String templateName) throws IOException, TemplateException {
        StringBuilder fileBuffer = new StringBuilder();
        Template tmpl = freemarkerCfg.getTemplate(templateName + ".ftl");
        StringWriter writer = new StringWriter();
        tmpl.process(variables, writer);
        fileBuffer.append(writer.toString());
        return fileBuffer.toString();
    }

}
