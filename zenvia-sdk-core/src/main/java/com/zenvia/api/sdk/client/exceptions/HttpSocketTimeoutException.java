package com.zenvia.api.sdk.client.exceptions;


@SuppressWarnings( "serial" )
public class HttpSocketTimeoutException extends HttpRequestException {
	public HttpSocketTimeoutException( String url, Exception cause ) {
		super( "Response timeout on: " + url, cause );
	}
}
