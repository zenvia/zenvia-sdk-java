package com.zenvia.api.sdk.client.exceptions;


@SuppressWarnings( "serial" )
public class HttpIOException extends HttpRequestException {
	public HttpIOException( String url, Exception cause ) {
		super( "IO error: " + url, cause );
	}
}
