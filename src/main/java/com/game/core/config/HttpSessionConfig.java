package com.game.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import redis.clients.jedis.JedisShardInfo;

@Configuration
@EnableRedisHttpSession
@PropertySource("classpath:game.properties")
public class HttpSessionConfig {

	@Value("${redis.ip}")
	private String redisIp;
	
	@Value("${redis.port}")
	private int redisPort;
	
	@Bean
	public HttpSessionStrategy httpSessionStrategy() {
		return new HeaderHttpSessionStrategy();
	}
	
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisShardInfo info = new JedisShardInfo(redisIp, redisPort);
		return new JedisConnectionFactory(info);
	}
}