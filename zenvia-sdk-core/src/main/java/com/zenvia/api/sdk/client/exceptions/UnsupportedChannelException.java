package com.zenvia.api.sdk.client.exceptions;


/** Exception generated when a unsupported channel is attempted to be used.
 *
 *  @since 0.9.0 */
@SuppressWarnings( "serial" )
public class UnsupportedChannelException extends ApiException {
	/** Channel that is unsupported by the API/SDK. Same as {@link #getChannel}.
	 *
	 *  @since 1.0.0 */
	public final String channel;


	/** @param channel Channel that is unsupported by the API/SDK.
	 *
	 *  @since 0.9.0 */
	public UnsupportedChannelException( String channel ) {
		super( "Unsupported channel: " + channel );
		this.channel = channel;
	}


	/** Channel that is unsupported by the API/SDK. Same as {@link #channel}.
	 *
	 *  @since 1.0.0 */
	public String getChannel() {
		return channel;
	}
}
