package com.epam.esm.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@EntityScan( basePackages = {"com.epam.esm"} )
@Configuration
public class CoreSpringConfig {

}
