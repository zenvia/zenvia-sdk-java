package com.zenvia.api.sdk.client.exceptions;


/** Exception generated when a connection to the API server was attempted, but it failed due,
 *  most likely, a connection refused.
 *
 *  This might happen due a server temporary unavailability, or some local infrastructure problem
 *  where the application was deployed, like a firewall blocking access or an offline proxy.
 *
 *  @since 0.9.0 */
@SuppressWarnings( "serial" )
public class HttpConnectionFailException extends HttpRequestException {
	/** @param url URL where the connection was attempted.
	 *
	 *  @param cause Exception that triggered this exception.
	 *
	 *  @since 0.9.0 */
	public HttpConnectionFailException( String url, Exception cause ) {
		super( "Connection fail on: " + url, cause );
	}
}
