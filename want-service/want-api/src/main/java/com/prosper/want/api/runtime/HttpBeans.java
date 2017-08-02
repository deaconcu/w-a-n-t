package com.prosper.want.api.runtime;

import com.prosper.want.common.boot.RuntimeSpringBeans;
import com.prosper.want.api.util.Config;
import com.prosper.want.common.validation.Validation;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.Jedis;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.SQLException;

@Configuration
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableScheduling
@PropertySources({
    @PropertySource("classpath:app.properties"),
    @PropertySource(value = "file:etc/want-api.properties", ignoreResourceNotFound=true)
})
@ComponentScan(basePackages = {
        "com.prosper.want.api.controller",
        "com.prosper.want.api.service",
        "com.prosper.want.api.util",
        "com.prosper.want.common.util",
        "com.prosper.want.common.exception",
        "com.prosper.want.common.validation",
})
@RuntimeSpringBeans(mode = "api-server", withWeb = true)
public class HttpBeans {

    @Bean(name="dataSource")
    public DataSource dataSource(Config config) throws SQLException {
        DriverManagerDataSource instance = new DriverManagerDataSource();
        instance.setDriverClassName("com.mysql.jdbc.Driver");
        instance.setUrl("jdbc:mysql://" + config.dbIp + ":" + config.dbPort + "/" + config.dbName
                +"?useUnicode=true&characterEncoding=utf8");
        System.out.println(instance.getUrl());
        instance.setUsername(config.dbUserName);                                  
        instance.setPassword(config.dbPassword);
        instance.getConnection();
        return instance;
    }

    @Bean(name="sqlSessionFactory")	
    @DependsOn("dataSource")
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) 
            throws PropertyVetoException, SQLException {
        SqlSessionFactoryBean instance = new SqlSessionFactoryBean();
        instance.setDataSource(dataSource);
        instance.setConfigLocation(new ClassPathResource("mybatis.xml"));
        return instance;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        configurer.setBasePackage("com.prosper.want.api.mapper");
        return configurer;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource)
            throws PropertyVetoException, SQLException {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public Jedis jedis(@Value("${redis.ip}") String ip, @Value("${redis.port}") int port) {
        return new Jedis(ip, port);
//        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
//        jedisClusterNodes.add(new HostAndPort(ip, port));
//        JedisCluster jc = new JedisCluster(jedisClusterNodes);
//        return jc;
    }
    
    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory(
            @Value("${server.port:8080}") final String port,
            @Value("${jetty.threadPool.maxThreads:100}") final String maxThreads,
            @Value("${jetty.threadPool.minThreads:10}") final String minThreads,
            @Value("${jetty.threadPool.idleTimeout:60000}") final String idleTimeout) {
        final JettyEmbeddedServletContainerFactory factory =  
                new JettyEmbeddedServletContainerFactory(Integer.valueOf(port));
        factory.addServerCustomizers(new JettyServerCustomizer() {
            @Override
            public void customize(final Server server) {
                // Tweak the connection pool used by Jetty to handle incoming HTTP connections
                final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
                threadPool.setMaxThreads(Integer.valueOf(maxThreads));
                threadPool.setMinThreads(Integer.valueOf(minThreads));
                threadPool.setIdleTimeout(Integer.valueOf(idleTimeout));
            }
        });
        return factory;
    }
    
    @Bean
    public PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer source = new PropertySourcesPlaceholderConfigurer();
        return source;
    }

}
