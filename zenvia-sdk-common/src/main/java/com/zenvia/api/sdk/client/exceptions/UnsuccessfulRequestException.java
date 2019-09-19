package com.zenvia.api.sdk.client.exceptions;

import com.zenvia.api.sdk.client.errors.ErrorResponse;

@SuppressWarnings( "serial" )
public class UnsuccessfulRequestException extends Exception {
	private final int httpStatusCode;
	
	private final ErrorResponse body;


	public UnsuccessfulRequestException( String url, int httpStatusCode, ErrorResponse body ) {
		super( "Unsuccessful request on: " + url );
		this.httpStatusCode = httpStatusCode;
		this.body = body;
	}


	public int getHttpStatusCode() {
		return httpStatusCode;
	}


	public ErrorResponse getBody() {
		return body;
	}
}
