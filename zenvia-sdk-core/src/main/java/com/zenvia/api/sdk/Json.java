package com.zenvia.api.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


/** Class used on toString methods of DTO beans.
 *
 *  @since 0.9.0 */
public abstract class Json {
	private static final ObjectMapper jsonMapper = new ObjectMapper()
		.enable( SerializationFeature.INDENT_OUTPUT );


	/** Serializes an object as a indented JSON.
	 * 
	 *  @param object The object to be serialized.
	 *
	 *  @return String containg the indented JSON.
	 *
	 *  @since 0.9.0 */
	public static final String pretty( Object object ) {
		try {
			return jsonMapper.writeValueAsString( object );
		} catch( JsonProcessingException exception ) {
			throw new IllegalArgumentException(
				"Failed to generate JSON for " + object.getClass().getName(),
				exception
			);
		}
	}


	private Json() {
		super();
	}
}
