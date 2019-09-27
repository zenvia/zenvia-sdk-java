package com.zenvia.api.sdk.client.exceptions;

import com.zenvia.api.sdk.client.errors.ErrorResponse;

@SuppressWarnings( "serial" )
public class UnsuccessfulRequestException extends RuntimeException {
	private final int httpStatusCode;
	
	private final ErrorResponse body;


	public UnsuccessfulRequestException( String url, int httpStatusCode, ErrorResponse body ) {
		super( "Unsuccessful request on: " + url );
		this.httpStatusCode = httpStatusCode;
		this.body = body;
	}


	public UnsuccessfulRequestException( String url, int httpStatusCode, Throwable cause ) {
		super( "Unsuccessful request on: " + url, cause );
		this.httpStatusCode = httpStatusCode;
		this.body = null;
	}


	public int getHttpStatusCode() {
		return httpStatusCode;
	}


	public ErrorResponse getBody() {
		return body;
	}
}
