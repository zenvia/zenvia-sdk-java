package com.zenvia.api.sdk.client.subscriptions;


/** Indicates the {@link Subscription subscription} status.
*
*  @since 0.9.0 */
public enum SubscriptionStatus {
	/** Callbacks will be triggered when the criteria is matched.
	*
	*  @since 0.9.0 */
	ACTIVE,

	/** Callbacks will not be triggered.
	*
	*  @since 0.9.0 */
	INACTIVE;
}
