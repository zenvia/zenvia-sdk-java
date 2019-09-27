package com.zenvia.api.sdk.client.exceptions;


@SuppressWarnings( "serial" )
public class HttpConnectionTimeoutException extends HttpRequestException {
	public HttpConnectionTimeoutException( String url, Exception cause ) {
		super( "Connection timeout on: " + url, cause );
	}
}
