package com.zenvia.api.sdk.autoconfigure.webhook.conditions;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import com.zenvia.api.sdk.autoconfigure.webhook.WebhookControllerJerseyAutoConfiguration;
import com.zenvia.api.sdk.webhook.MessageEventCallback;
import com.zenvia.api.sdk.webhook.MessageStatusEventCallback;

@AutoConfigureBefore(WebhookControllerJerseyAutoConfiguration.class)
public class OnEventCallbacksCondition extends AnyNestedCondition {

	public OnEventCallbacksCondition() {
		super(ConfigurationPhase.REGISTER_BEAN);
	}

	@ConditionalOnBean(MessageEventCallback.class)
	static class OnMessageEventCallback {}

	@ConditionalOnBean(MessageStatusEventCallback.class)
	static class OnMessageStatusEventCallback {}

}
