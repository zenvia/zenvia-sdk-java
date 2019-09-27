package com.zenvia.api.sdk.client.exceptions;


@SuppressWarnings( "serial" )
public abstract class ApiException extends RuntimeException {
	public final String message;

	public final Exception causedBy;


	public ApiException( String message ) {
		this( message, null );
	}


	public ApiException( String message, Exception cause ) {
		super( message, cause );
		this.message = message;
		this.causedBy = cause;
	}
}
