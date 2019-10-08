package com.zenvia.api.sdk.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.zenvia.api.sdk.client.exceptions.UnsupportedChannelException;
import com.zenvia.api.sdk.contents.ContentType;

import static com.zenvia.api.sdk.contents.ContentType.*;


/** Lists channels supported by the API/SDK.
 *
 *  @since 0.9.0 */
public enum ChannelType {
	/** Supports
	 *  {@link com.zenvia.api.sdk.contents.TextContent},
	 *  {@link com.zenvia.api.sdk.contents.FileContent} and
	 *  {@link com.zenvia.api.sdk.contents.TemplateContent}.
	 *
	 *  @since 0.9.0 */
	whatsapp( text, file, template ),
	/** Supports only {@link com.zenvia.api.sdk.contents.TextContent}.
	 *
	 *  @since 0.9.0 */

	sms( text ),

	/** Supports
	 *  {@link com.zenvia.api.sdk.contents.TextContent} and
	 *  {@link com.zenvia.api.sdk.contents.FileContent}.
	 *
	 *  @since 0.9.0 */
	facebook( text, file );

	/** Path for the message resource.
	 *
	 *  @since 1.0.0 */
	public final String messagePath;

	private final Set<ContentType> supportedContentTypes;


	private ChannelType( ContentType... contentTypes ) {
		this.messagePath = "/v1/channels/" + name() + "/messages";
		this.supportedContentTypes = Collections.unmodifiableSet( new HashSet<>( Arrays.asList( contentTypes ) ) );
	}


	/** Indicates if content type is supported by the channel.
	 *
	 * @return True when the content type is supported by the channel.
	 *
	 *  @since 0.9.0 */
	public boolean supportsContent( ContentType contentType ) {
		return supportedContentTypes.contains( contentType );
	}


	/** Exception normalization for {@link #valueOf(String)}.
	 *
	 *  @param channel String to be mapped to a ChannelType.
	 *
	 *  @return The ChannelType if the channel is supported.
	 *
	 *  @throws UnsupportedChannelException If the channel is not supported.
	 *
	 *  @since 0.9.0 */
	public static ChannelType parse( String channel ) throws UnsupportedChannelException {
		try {
			return ChannelType.valueOf( channel  );
		} catch ( RuntimeException exception ) {
			throw new UnsupportedChannelException( channel );
		}
	}
}
