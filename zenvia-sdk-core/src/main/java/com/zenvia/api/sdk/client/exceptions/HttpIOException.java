package com.zenvia.api.sdk.client.exceptions;


/** Exception generated when a unmapped communication problem happened while executing the request.
 *
 *  It is possible that the API server successfully received the request, and therefore will follow
 *  through with the execution of the requested action, so just the server reply was lost, or the
 *  request was not executed at all.
 *
 *  @since 0.9.0 */
@SuppressWarnings( "serial" )
public class HttpIOException extends HttpRequestException {
	/** @param url URL where the request was attempted/executed.
	 *
	 *  @param cause Exception that triggered this exception.
	 *
	 *  @since 0.9.0 */
	public HttpIOException( String url, Exception cause ) {
		super( "IO error: " + url, cause );
	}
}
