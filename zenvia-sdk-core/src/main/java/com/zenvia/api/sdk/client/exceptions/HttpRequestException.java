package com.zenvia.api.sdk.client.exceptions;


@SuppressWarnings( "serial" )
public abstract class HttpRequestException extends Exception {
	public HttpRequestException( String message, Throwable cause ) {
		super( message, cause );
	}
}
