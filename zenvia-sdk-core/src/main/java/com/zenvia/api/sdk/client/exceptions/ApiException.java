package com.zenvia.api.sdk.client.exceptions;


/** Base exception for all SDK generated exceptions.
 *
 *  @since 0.9.0 */
@SuppressWarnings( "serial" )
public abstract class ApiException extends RuntimeException {
	/** Exception message. The same from {@link #getMessage}.
	 * 
	 *  @since 0.9.0 */
	public final String message;

	/** Exception that triggered this exception. The same from {@link #getCause}.
	 * 
	 *  @since 0.9.0 */
	public final Exception causedBy;


	/** Creates an exception without a cause.
	 *
	 *  @param message Exception message.
	 *
	 *  @since 0.9.0 */
	public ApiException( String message ) {
		this( message, null );
	}


	/** Creates an exception with another exception as cause.
	 *
	 *  @param message Exception message.
	 *
	 *  @param cause Exception that triggered this exception.
	 *
	 *  @since 0.9.0 */
	public ApiException( String message, Exception cause ) {
		super( message, cause );
		this.message = message;
		this.causedBy = cause;
	}
}
