package com.zenvia.api.sdk.webhook;

/** Indicates the status of a sent message.
 *
 *  @since 1.1.0 */
public enum MessageStatusCode {
	/** Message was rejected either by Zenvia API or by channel's provider. 
	 *
	 *  @since 1.1.0 */
	REJECTED,

	/** Message was sent successfully to channel's provider. 
	 *
	 *  @since 1.1.0 */
	SENT,
	
	/** Message was accepted by channel's provider and delivered to recipient. 
	 *
	 *  @since 1.1.0 */
	DELIVERED,
	
	/** Message was accepted by channel's provider but cannot be delivered to recipient.  
	 *
	 *  @since 1.1.0 */
	NOT_DELIVERED,
	
	/** Message was delivered and read by recipient. 
	 *
	 *  @since 1.1.0 */
	READ;
}
