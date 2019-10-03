package com.zenvia.api.sdk.client.exceptions;


@SuppressWarnings( "serial" )
public abstract class HttpRequestException extends ApiException {
	public HttpRequestException( String message, Exception cause ) {
		super( message, cause );
	}
}
