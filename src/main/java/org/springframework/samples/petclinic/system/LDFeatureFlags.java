package org.springframework.samples.petclinic.system;

import java.time.Duration;

import com.launchdarkly.sdk.*;
import com.launchdarkly.sdk.server.*;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * LaunchDarkly configuration and service classes for feature flag management.
 *
 * @author Your Name
 */
@Configuration
@EnableConfigurationProperties(LDFeatureFlags.LaunchDarklyProperties.class)
public class LDFeatureFlags {

	@Bean
	@ConditionalOnProperty(name = "launchdarkly.sdk-key")
	public LDClient ldClient(LaunchDarklyProperties properties) {
		LDConfig config = new LDConfig.Builder().build();
		return new LDClient(properties.getSdkKey(), config);
	}

	@ConfigurationProperties(prefix = "launchdarkly")
	public static class LaunchDarklyProperties {

		private String sdkKey;

		private boolean offline = false;

		private String baseUri = "https://sdk.launchdarkly.com";

		private Duration startWait = Duration.ofSeconds(5);

		// Add these getter and setter methods manually:
		public String getSdkKey() {
			return sdkKey;
		}

		public void setSdkKey(String sdkKey) {
			this.sdkKey = sdkKey;
		}

		public boolean isOffline() {
			return offline;
		}

		public void setOffline(boolean offline) {
			this.offline = offline;
		}

		public String getBaseUri() {
			return baseUri;
		}

		public void setBaseUri(String baseUri) {
			this.baseUri = baseUri;
		}

		public Duration getStartWait() {
			return startWait;
		}

		public void setStartWait(Duration startWait) {
			this.startWait = startWait;
		}

	}

	@Service
	public class FeatureFlagService {

		private final LDClient ldClient;

		public FeatureFlagService(LDClient ldClient) {
			this.ldClient = ldClient;
		}

		// Method to evaluate boolean flags
		public boolean isFeatureEnabled(String flagKey, LDContext context) {
			return ldClient.boolVariation(flagKey, context, false);
		}

		// Method for string variations (A/B testing)
		public String getStringVariation(String flagKey, LDContext context, String defaultValue) {
			return ldClient.stringVariation(flagKey, context, defaultValue);
		}

		// Create user context helper
		public LDContext createUser(String userId, String email, String name) {
			return LDContext.builder(userId).name(name).set("email", email).build();
		}

	}

}