package org.springframework.samples.petclinic.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.ContextKind;
import com.launchdarkly.sdk.server.LDClient;
import com.launchdarkly.sdk.server.LDConfig;
import com.launchdarkly.sdk.LDValue;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class FeatureFlagService {

	@Value("${launchdarkly.sdk-key:disabled}")
	private String sdkKey;

	private LDClient ldClient;

	@PostConstruct
	public void initialize() {
		if (!"disabled".equals(sdkKey)) {
			this.ldClient = new LDClient(sdkKey, new LDConfig.Builder().build());
			System.out.println("LaunchDarkly ready!");
		}
		else {
			System.out.println("LaunchDarkly disabled");
		}
	}

	@PreDestroy
	public void shutdown() {
		if (ldClient != null) {
			try {
				ldClient.close();
				System.out.println("LaunchDarkly client closed successfully");
			}
			catch (IOException e) {
				System.err.println("⚠️ Error closing LaunchDarkly client: " + e.getMessage());
			}
		}
	}

	public boolean isFeatureEnabled(String flagKey, LDContext context) {
		return ldClient != null ? ldClient.boolVariation(flagKey, context, false) : false;
	}

	public String getStringVariation(String flagKey, LDContext context, String defaultValue) {
		return ldClient != null ? ldClient.stringVariation(flagKey, context, defaultValue) : defaultValue;
	}

	public LDContext createUserContext(String userId, String email, String name) {
		return LDContext.builder(userId).name(name).set("email", email).build();
	}

	public LDContext createContextFromRequest(HttpServletRequest request) {
		LDContext context = LDContext.builder("context-key-123abc")
			.kind(ContextKind.of("Request"))
			.set("firstName", "Sandy")
			.set("lastName", "Smith")
			.set("email", "sandy@example.com")
			.set("groups", LDValue.buildArray().add("Google").add("Microsoft").build())
			.build();
		return context;
	}

	public LDContext createContextFromRequestAndUser(HttpServletRequest request, String userId, String email,
			String name) {
		LDContext c1 = LDContext.builder(userId).name(name).set("email", email).build();
		LDContext c2 = LDContext.create(ContextKind.of("kind2"), "key2");
		LDContext c3 = LDContext.create(ContextKind.of("kind3"), "key3");

		LDContext multi1 = LDContext.createMulti(c1, c2, c3);
		return multi1;
	}

}