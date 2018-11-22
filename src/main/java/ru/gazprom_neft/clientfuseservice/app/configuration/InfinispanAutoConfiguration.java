package ru.gazprom_neft.clientfuseservice.app.configuration;

import org.infinispan.client.hotrod.ProtocolVersion;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.commons.api.BasicCacheContainer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Настройка DataGrid.
 */
@Configuration
@ConfigurationProperties(prefix = "infinispan")
public class InfinispanAutoConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public BasicCacheContainer cacheContainer(Environment environment) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.version(ProtocolVersion.PROTOCOL_VERSION_26)
                .addServers(environment.getProperty("datagrid.host") + ":"
                        + environment.getProperty("datagrid.port"))
                .security().ssl()
                .enabled(environment.getProperty("datagrid.ssl.enabled", Boolean.class))
                .trustStoreFileName(environment.getProperty("datagrid.ssl.trust-store"))
                .trustStorePassword(environment.getProperty("datagrid.ssl.trust-store-password").toCharArray());


        return new RemoteCacheManager(builder.create(), false);
    }
}
