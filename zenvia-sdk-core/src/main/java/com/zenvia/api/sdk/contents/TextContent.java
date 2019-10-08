package com.zenvia.api.sdk.contents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


/** Content used for sending and receiving text messages.
 *
 *  @since 0.9.0 */
public class TextContent extends Content {
	/** @since 0.9.0 */
	public static final String TYPE = "text";

	/** Text of the message
	 *
	 *  @since 0.9.0 */
	public final String text;


	/** @param text Text of the message.
	 *
	 *  @since 0.9.0 */
	@JsonCreator
	public TextContent(
		@JsonProperty( "text" ) String text
	) {
		super( ContentType.text );
		this.text = text;
	}
}
