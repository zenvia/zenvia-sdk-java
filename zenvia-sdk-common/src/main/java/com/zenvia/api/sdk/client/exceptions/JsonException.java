package com.zenvia.api.sdk.client.exceptions;

import java.nio.charset.StandardCharsets;


@SuppressWarnings( "serial" )
public class JsonException extends Exception {
	private final byte[] json;


	public JsonException( byte[] json, Throwable cause ) {
		super( "Exception deserializing", cause );
		this.json = json;
	}


	public byte[] getJson() {
		return json;
	}


	public String getJsonAsString() {
		return new String( json, StandardCharsets.UTF_8 );
	}
}
