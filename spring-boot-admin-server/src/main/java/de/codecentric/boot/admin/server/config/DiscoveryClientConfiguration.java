/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.codecentric.boot.admin.server.config;

import de.codecentric.boot.admin.server.discovery.ApplicationDiscoveryListener;
import de.codecentric.boot.admin.server.discovery.DefaultServiceInstanceConverter;
import de.codecentric.boot.admin.server.discovery.EurekaServiceInstanceConverter;
import de.codecentric.boot.admin.server.discovery.ServiceInstanceConverter;
import de.codecentric.boot.admin.server.domain.entities.ApplicationRepository;
import de.codecentric.boot.admin.server.services.ApplicationRegistry;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.netflix.discovery.EurekaClient;

@Configuration
@ConditionalOnSingleCandidate(DiscoveryClient.class)
@ConditionalOnProperty(prefix = "spring.boot.admin.discovery", name = "enabled", matchIfMissing = true)
@AutoConfigureAfter({SimpleDiscoveryClientAutoConfiguration.class})
public class DiscoveryClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "spring.boot.admin.discovery")
    public ApplicationDiscoveryListener applicationDiscoveryListener(ServiceInstanceConverter serviceInstanceConverter,
                                                                     DiscoveryClient discoveryClient,
                                                                     ApplicationRegistry registry,
                                                                     ApplicationRepository repository) {
        ApplicationDiscoveryListener listener = new ApplicationDiscoveryListener(discoveryClient, registry, repository);
        listener.setConverter(serviceInstanceConverter);
        return listener;
    }

    @Configuration
    @ConditionalOnBean(EurekaClient.class)
    public static class EurekaConverterConfiguration {
        @Bean
        @ConditionalOnMissingBean({ServiceInstanceConverter.class})
        @ConfigurationProperties(prefix = "spring.boot.admin.discovery.converter")
        public EurekaServiceInstanceConverter serviceInstanceConverter() {
            return new EurekaServiceInstanceConverter();
        }
    }

    @Bean
    @ConditionalOnMissingBean({ServiceInstanceConverter.class})
    @ConfigurationProperties(prefix = "spring.boot.admin.discovery.converter")
    public DefaultServiceInstanceConverter serviceInstanceConverter() {
        return new DefaultServiceInstanceConverter();
    }
}
