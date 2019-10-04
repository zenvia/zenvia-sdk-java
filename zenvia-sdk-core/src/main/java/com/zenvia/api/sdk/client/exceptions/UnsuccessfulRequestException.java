package com.zenvia.api.sdk.client.exceptions;

import com.zenvia.api.sdk.client.errors.ErrorResponse;


/** Exception generated when the client receives an HTTP response with a status code
 *  either less than 200 or greater or equal than 300.
 *
 *  Most likely, this is due a bad request. But it is also possible that is a temporary problem
 *  on the API server, or a local infrastructure problem.
 *
 *  @since 0.9.0 */
@SuppressWarnings( "serial" )
public class UnsuccessfulRequestException extends ApiException {
	/** HTTP status code received from the URL request. Same as {@link #getHttpStatusCode}.
	 *
	 *  @since 0.9.0 */
	public final int httpStatusCode;
	
	/** Error response from the URL request. Same as {@link #getBody}.
	 *
	 *  @since 0.9.0 */
	public final ErrorResponse body;


	/** @param url URL where the request was made.
	 *
	 *  @param httpStatusCode HTTP status code received from the URL request.
	 *
	 *  @param body Error response from the URL request.
	 *
	 *  @since 0.9.0 */
	public UnsuccessfulRequestException( String url, int httpStatusCode, ErrorResponse body ) {
		this( url, httpStatusCode, body, null );
	}


	/** @param url URL where the request was made.
	 *
	 *  @param httpStatusCode HTTP status code received from the URL request.
	 *
	 *  @param cause Exception while while handling the response body.
	 *
	 *  @since 0.9.0 */
	public UnsuccessfulRequestException( String url, int httpStatusCode, Exception cause ) {
		this( url, httpStatusCode, null, cause );
	}


	/** @param url URL where the connection was attempted.
	 *
	 *  @param cause Exception that triggered this exception.
	 *
	 *  @since 0.9.0 */
	public UnsuccessfulRequestException( String url, int httpStatusCode, ErrorResponse body, Exception cause ) {
		super( "Unsuccessful request on: " + url, cause );
		this.httpStatusCode = httpStatusCode;
		this.body = body;
	}


	/** HTTP status code received from the URL request. Same as {@link #httpStatusCode}.
	 *
	 *  @since 0.9.0 */
	public int getHttpStatusCode() {
		return httpStatusCode;
	}


	/** Error response from the URL request. Same as {@link #body}.
	 *
	 *  @since 0.9.0 */
	public ErrorResponse getBody() {
		return body;
	}
}
