package com.zenvia.api.sdk.contents;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


/** Generic content used for multiple purposes. Usually used to receive metadata from the sender. 
*
*  @since 0.9.0 */
public class JsonContent extends Content {
	/** @since 0.9.0 */
	public static final String TYPE = "json";


	/** The payload object as a map.
	 *
	 *  @since 0.9.0 */
	public final Map<String,Object> payload;


	/** @param payload The payload object as a map.
	 *
	 *  @since 0.9.0 */
	@JsonCreator
	public JsonContent( @JsonProperty( "payload" ) Map<String,Object> payload ) {
		super( ContentType.json );
		this.payload = payload;
	}
}
