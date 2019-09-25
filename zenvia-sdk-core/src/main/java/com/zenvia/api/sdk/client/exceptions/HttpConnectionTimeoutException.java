package com.zenvia.api.sdk.client.exceptions;


@SuppressWarnings( "serial" )
public class HttpConnectionTimeoutException extends HttpRequestException {
	public HttpConnectionTimeoutException( String url, Throwable cause ) {
		super( "Connection timeout on: " + url, cause );
	}
}
