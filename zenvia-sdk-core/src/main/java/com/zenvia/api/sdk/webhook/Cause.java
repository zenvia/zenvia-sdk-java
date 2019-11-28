package com.zenvia.api.sdk.webhook;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


/** Cause of status on {@link MessageStatus}.
 * 
 * @since 1.1.0 */
public class Cause {
	/** The error code provided by channel.
	 *
	 *  @since 1.1.0 */
	public final String channelErrorCode;

	/** Reason of this error.
	 *
	 *  @since 1.1.0 */
	public final String reason;

	/** More details about this error.
	 *
	 *  @since 1.1.0 */
	public final String details;

	/** @param channelErrorCode Error code provided by channel.
	 *
	 *  @param reason Reason of this error.
	 *
	 *  @param details More details about this error.
	 *
	 *  @since 1.1.0 */
	@JsonCreator
	public Cause(
		@JsonProperty( "channelErrorCode" ) String channelErrorCode,
		@JsonProperty( "reason" ) String reason,
		@JsonProperty( "details" ) String details
	) {
		this.channelErrorCode = channelErrorCode;
		this.reason = reason;
		this.details = details;
	}
}
