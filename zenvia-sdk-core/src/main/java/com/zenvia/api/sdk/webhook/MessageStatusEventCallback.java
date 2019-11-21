package com.zenvia.api.sdk.webhook;

/** The <code>MessageStatusEventCallback</code> interface should be implemented by any
 * class whose instances are intended to handle {@link EventType.MESSAGE_STATUS} events.
 * 
 * @since 1.1.0 */
@FunctionalInterface
public interface MessageStatusEventCallback {
	public void onMessageStatusEvent(MessageStatusEvent status);
}
