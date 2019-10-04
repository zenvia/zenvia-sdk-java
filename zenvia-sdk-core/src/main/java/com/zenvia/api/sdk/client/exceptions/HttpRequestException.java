package com.zenvia.api.sdk.client.exceptions;


/** Base exception for HTTP SDK generated exceptions.
*
*  @since 0.9.0 */
@SuppressWarnings( "serial" )
public abstract class HttpRequestException extends ApiException {
	/** Creates an exception with another exception as cause.
	 *
	 *  @param message Exception message.
	 *
	 *  @param cause Exception that triggered this exception.
	 *
	 *  @since 0.9.0 */
	public HttpRequestException( String message, Exception cause ) {
		super( message, cause );
	}
}
