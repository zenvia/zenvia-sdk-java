package com.zenvia.api.sdk.client.exceptions;


@SuppressWarnings( "serial" )
public class UnsuccessfulRequestException extends Exception {
	private final int httpStatusCode;
	
	private final Error body;


	public UnsuccessfulRequestException( String url, int httpStatusCode, Error body ) {
		super( "Unsuccessful request on: " + url );
		this.httpStatusCode = httpStatusCode;
		this.body = body;
	}


	public int getHttpStatusCode() {
		return httpStatusCode;
	}


	public Error getBody() {
		return body;
	}
}
