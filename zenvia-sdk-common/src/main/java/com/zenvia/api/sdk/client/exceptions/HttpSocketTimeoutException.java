package com.zenvia.api.sdk.client.exceptions;


@SuppressWarnings( "serial" )
public class HttpSocketTimeoutException extends HttpRequestException {
	public HttpSocketTimeoutException( String url, Throwable cause ) {
		super( "Response timeout on: " + url, cause );
	}
}