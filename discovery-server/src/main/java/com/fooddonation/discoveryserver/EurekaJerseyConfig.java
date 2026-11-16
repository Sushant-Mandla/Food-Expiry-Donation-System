package com.fooddonation.discoveryserver;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.Jersey3DiscoveryClientOptionalArgs;
import com.netflix.discovery.shared.transport.jersey.TransportClientFactories;
import com.netflix.discovery.shared.transport.jersey3.Jersey3TransportClientFactories;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EurekaJerseyConfig {

    @Bean
    public Jersey3DiscoveryClientOptionalArgs jersey3DiscoveryClientOptionalArgs() {
        return new Jersey3DiscoveryClientOptionalArgs();
    }

    @Bean
    public Jersey3TransportClientFactories jersey3TransportClientFactories() {
        return Jersey3TransportClientFactories.getInstance();
    }
}
