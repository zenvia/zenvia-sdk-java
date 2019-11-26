package com.zenvia.api.sdk.webhook;

import com.zenvia.api.sdk.client.subscriptions.EventType;

/** The <code>MessageEventCallback</code> interface should be implemented by any
 * class whose instances are intended to handle {@link EventType#MESSAGE} events.
 * 
 * @since 1.1.0 */
@FunctionalInterface
public interface MessageEventCallback {
	public void onMessageEvent(MessageEvent message);
}
