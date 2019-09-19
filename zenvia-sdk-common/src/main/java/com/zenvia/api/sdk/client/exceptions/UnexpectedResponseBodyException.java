package com.zenvia.api.sdk.client.exceptions;


@SuppressWarnings( "serial" )
public class UnexpectedResponseBodyException extends HttpRequestException {
	private final JsonException cause;


	public UnexpectedResponseBodyException( String url, JsonException cause ) {
		super( "Unexpected response body on: " + url, cause );
		this.cause = cause;
	}


	public String getBody() {
		return cause.getJsonAsString();
	}
}
