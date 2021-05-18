package com.ball.gateway;

import com.netflix.config.AggregatedConfiguration;
import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicConfiguration;

import org.apache.commons.configuration.AbstractConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import java.util.List;

import javax.annotation.PreDestroy;

@EnableZuulProxy
@EnableCircuitBreaker
@SpringBootApplication
@EnableWebSocketMessageBroker
public class BallComGateway {

	public static void main(String[] args) {
		SpringApplication.run(BallComGateway.class, args);
	}

	@PreDestroy
	public void cleanup() {
		try {
			if (ConfigurationManager.getConfigInstance() instanceof DynamicConfiguration) {
				DynamicConfiguration config = (DynamicConfiguration) ConfigurationManager.getConfigInstance();
				config.stopLoading();
			} else if (ConfigurationManager.getConfigInstance() instanceof ConcurrentCompositeConfiguration) {
				AggregatedConfiguration configInst = (ConcurrentCompositeConfiguration) ConfigurationManager
						.getConfigInstance();
				List<AbstractConfiguration> configs = configInst.getConfigurations();
				if (configs != null) {
					for (AbstractConfiguration config : configs) {
						if (config instanceof DynamicConfiguration) {
							((DynamicConfiguration) config).stopLoading();
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
