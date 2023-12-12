package com.hoxton.crawler.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties("spring.twse")
@EnableConfigurationProperties(TwseProperties.class)
@Setter
@Getter
public class TwseProperties {

    Integer yearInterval;
}
