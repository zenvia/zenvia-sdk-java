package com.zenvia.api.sdk.client.exceptions;


/** Exception generated when a connection to the API server was attempted, but it failed due
 *  a connection timeout.
 *
 *  This might happen due a server temporary unavailability, or some local infrastructure problem
 *  where the application was deployed, like a firewall blocking access or an offline proxy.
 *
 *  Another possibility is that the default connection timeout has been overriden on the client
 *  construction and set too low.
 *
 *  @since 0.9.0 */
@SuppressWarnings( "serial" )
public class HttpConnectionTimeoutException extends HttpRequestException {
	/** @param url URL where the connection was attempted.
	 *
	 *  @param cause Exception that triggered this exception.
	 *
	 *  @since 0.9.0 */
	public HttpConnectionTimeoutException( String url, Exception cause ) {
		super( "Connection timeout on: " + url, cause );
	}
}
