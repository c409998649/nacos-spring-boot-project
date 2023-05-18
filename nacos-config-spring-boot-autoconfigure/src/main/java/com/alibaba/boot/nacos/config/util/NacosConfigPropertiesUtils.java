/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.boot.nacos.config.util;

import com.alibaba.boot.nacos.config.NacosConfigConstants;
import com.alibaba.boot.nacos.config.properties.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.env.ConfigurableEnvironment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Springboot used to own property binding configured binding
 *
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since 0.2.3
 */
public class NacosConfigPropertiesUtils {

    private static final Pattern PATTERN = Pattern.compile("-(\\w)");

    private static final Logger logger = LoggerFactory
            .getLogger(NacosConfigPropertiesUtils.class);

    public static NacosConfigProperties buildNacosConfigProperties(
            ConfigurableEnvironment environment) {
        NacosConfigProperties nacosConfigProperties = setData(environment);
//        NacosConfigProperties nacosConfigProperties = new NacosConfigProperties();
//        Binder binder = Binder.get(environment);
//        ResolvableType type = ResolvableType.forClass(NacosConfigProperties.class);
//        Bindable<?> target = Bindable.of(type).withExistingValue(nacosConfigProperties);
//        binder.bind(NacosConfigConstants.PREFIX, target);
        logger.info("nacosConfigProperties : {}", nacosConfigProperties);
        return nacosConfigProperties;
    }

    public static NacosConfigProperties setData(ConfigurableEnvironment environment) {
        NacosConfigProperties nacosConfigProperties = new NacosConfigProperties();
        String serverAddr = environment.resolvePlaceholders("${" + NacosConfigConstants.PREFIX + ".server-addr:}");
        if (StringUtils.isNotBlank(serverAddr)) {
            nacosConfigProperties.setServerAddr(serverAddr);
        }
        String namespace = environment.resolvePlaceholders("${" + NacosConfigConstants.PREFIX + ".namespace:}");
        if (StringUtils.isNotBlank(namespace)) {
            nacosConfigProperties.setNamespace(namespace);
        }
        String username = environment.resolvePlaceholders("${" + NacosConfigConstants.PREFIX + ".username:}");
        if (StringUtils.isNotBlank(username)) {
            nacosConfigProperties.setUsername(username);
        }
        String password = environment.resolvePlaceholders("${" + NacosConfigConstants.PREFIX + ".password:}");
        if (StringUtils.isNotBlank(password)) {
            nacosConfigProperties.setPassword(password);
        }
        String autoRefresh = environment.resolvePlaceholders("${" + NacosConfigConstants.PREFIX + ".auto-refresh:}");
        if (StringUtils.isNotBlank(autoRefresh)) {
            nacosConfigProperties.setAutoRefresh(Boolean.parseBoolean(autoRefresh));
        }
        String remoteFirst = environment.resolvePlaceholders("${" + NacosConfigConstants.PREFIX + ".remote-first:}");
        if (StringUtils.isNotBlank(remoteFirst)) {
            nacosConfigProperties.setRemoteFirst(Boolean.parseBoolean(remoteFirst));
        }
        String dataId = environment.resolvePlaceholders("${" + NacosConfigConstants.PREFIX + ".data-id:}");
        if (StringUtils.isNotBlank(dataId)) {
            nacosConfigProperties.setDataId(dataId);
        }
        String type = environment.resolvePlaceholders("${" + NacosConfigConstants.PREFIX + ".type:}");
        if (StringUtils.isNotBlank(type)) {
            ConfigType[] values = ConfigType.values();
            for (ConfigType configType : values) {
                if (type.equals(configType.getType())) {
                    nacosConfigProperties.setType(configType);
                }
            }
        }
        String enable = environment.resolvePlaceholders("${" + NacosConfigConstants.PREFIX + ".bootstrap.enable:}");
        if (StringUtils.isNotBlank(type)) {
            NacosConfigProperties.Bootstrap bootstrap = new NacosConfigProperties.Bootstrap();
            bootstrap.setEnable(Boolean.parseBoolean(enable));
            nacosConfigProperties.setBootstrap(bootstrap);
        }
        return nacosConfigProperties;
    }

    private static String resolveKey(String key) {
        Matcher matcher = PATTERN.matcher(key);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
