package com.zenvia.api.sdk.client.exceptions;

import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.contents.ContentType;


/** Exception generated when a unsupported content is attempted to be used on a channel that does
 *  not support it.
 *
 *  @since 0.9.0 */
@SuppressWarnings( "serial" )
public class UnsupportedContentException extends ApiException {
	/** Content type that is not supported by the channel. Same as {@link #getContentType}.
	 *
	 *  @since 1.0.0 */
	public final String contentType;

	/** Channel that does support the content. Same as {@link #getChannel}.
	 *
	 *  @since 1.0.0 */
	public final ChannelType channel;


	/** @param contentType Content type that is not supported by the channel.
	 *
	 *  @param channel Channel that does support the content.
	 *
	 *  @since 0.9.0 */
	public UnsupportedContentException( ContentType contentType, ChannelType channel ) {
		super( "Content type " + contentType + " is not supported by " + channel + " channel" );
		this.contentType = contentType == null ? null : contentType.name();
		this.channel = channel;
	}


	/** Content type that is not supported by the channel. Same as {@link #contentType}.
	 *
	 *  @since 0.9.0 */
	public String getContentType() {
		return contentType;
	}


	/** Channel that does support the content. Same as {@link #channel}.
	 *
	 *  @since 1.0.0 */
	public ChannelType getChannel() {
		return channel;
	}
}
