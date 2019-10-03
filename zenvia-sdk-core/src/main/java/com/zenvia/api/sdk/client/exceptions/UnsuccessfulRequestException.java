package com.zenvia.api.sdk.client.exceptions;

import com.zenvia.api.sdk.client.errors.ErrorResponse;

@SuppressWarnings( "serial" )
public class UnsuccessfulRequestException extends ApiException {
	public final int httpStatusCode;
	
	public final ErrorResponse body;


	public UnsuccessfulRequestException( String url, int httpStatusCode, ErrorResponse body ) {
		this( url, httpStatusCode, body, null );
	}


	public UnsuccessfulRequestException( String url, int httpStatusCode, Exception cause ) {
		this( url, httpStatusCode, null, cause );
	}


	public UnsuccessfulRequestException( String url, int httpStatusCode, ErrorResponse body, Exception cause ) {
		super( "Unsuccessful request on: " + url, cause );
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
