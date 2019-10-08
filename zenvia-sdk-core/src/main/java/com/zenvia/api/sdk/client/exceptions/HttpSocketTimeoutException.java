package com.zenvia.api.sdk.client.exceptions;


/** Exception generated when the request was executed and sent to the API server, but it timed out
 *  while wait for a response from the server.
 *
 *  Most likely, the API server successfully received the request, and therefore will follow
 *  through with the execution of the requested action, so just the server reply was lost, but it is
 *  also possible that the request was not executed at all.
 *
 *  Another possibility is that the default socket timeout has been overriden on the client
 *  construction and set too low.
 *
 *  @since 0.9.0 */
@SuppressWarnings( "serial" )
public class HttpSocketTimeoutException extends HttpRequestException {
	/** @param url URL where the request was executed.
	 *
	 *  @param cause Exception that triggered this exception.
	 *
	 *  @since 0.9.0 */
	public HttpSocketTimeoutException( String url, Exception cause ) {
		super( "Response timeout on: " + url, cause );
	}
}
