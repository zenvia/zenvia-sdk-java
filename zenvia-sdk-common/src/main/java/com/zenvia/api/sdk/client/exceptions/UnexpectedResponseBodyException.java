package com.zenvia.api.sdk.client.exceptions;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;


@SuppressWarnings( "serial" )
public class UnexpectedResponseBodyException extends HttpRequestException {
	private final byte[] json;


	public UnexpectedResponseBodyException( String url, byte[] json, JsonProcessingException cause ) {
		super( "Unexpected response body on: " + url, cause );
		this.json = json;
	}


	public String getBody() {
		return new String( json, StandardCharsets.UTF_8);
	}
}
