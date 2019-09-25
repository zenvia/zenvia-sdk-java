package com.zenvia.api.sdk.contents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class TextContent extends Content {
	public static final String TYPE = "text";
	
	public final String text;


	@JsonCreator
	public TextContent(
		@JsonProperty( "text" ) String text
	) {
		super( ContentType.text );
		this.text = text;
	}
}
