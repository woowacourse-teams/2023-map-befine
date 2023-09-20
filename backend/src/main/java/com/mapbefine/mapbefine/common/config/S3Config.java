package com.mapbefine.mapbefine.common.config;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    private static final Logger log = LoggerFactory.getLogger(S3Config.class);
    @Value("${server.tomcat.accept-count}")
    private int acceptCount;
    @Value("${server.tomcat.max-connections}")
    private int maxConnections;
    @Value("${server.tomcat.threads.max}")
    private int threadsMax;

    @Bean
    public InstanceProfileCredentialsProvider instanceProfileCredentialsProvider() {
        return InstanceProfileCredentialsProvider.getInstance();
    }

    @Bean
    public AmazonS3 amazonS3() {
        log.debug("tomcat acceptCount : {}", acceptCount);
        log.debug("tomcat maxConnections : {}", maxConnections);
        log.debug("tomcat threadsMax : {}", threadsMax);
        
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(instanceProfileCredentialsProvider())
                .build();
    }

}
