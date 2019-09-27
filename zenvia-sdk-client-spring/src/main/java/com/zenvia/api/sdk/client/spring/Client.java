package com.zenvia.api.sdk.client.spring;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.zenvia.api.sdk.client.AbstractClient;
import com.zenvia.api.sdk.client.errors.ErrorResponse;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionFailException;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionTimeoutException;
import com.zenvia.api.sdk.client.exceptions.HttpIOException;
import com.zenvia.api.sdk.client.exceptions.HttpProtocolException;
import com.zenvia.api.sdk.client.exceptions.HttpSocketTimeoutException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;


public class Client extends AbstractClient {
	private static final Logger LOG = LoggerFactory.getLogger( Client.class );
	
	private final RestTemplate restTemplate;


	public Client( String apiToken ) {
		super( apiToken );
		restTemplate = buildRestTemplate();
	}


	public Client( String apiToken, Integer maxConnections ) {
		super( apiToken, maxConnections );
		restTemplate = buildRestTemplate();
	}


	public Client(
		String apiToken,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries
	) {
		super( apiToken, connectionTimeout, socketTimeout, maxAutoRetries );
		restTemplate = buildRestTemplate();
	}


	public Client(
		String apiToken,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries
	) {
		super( apiToken, maxConnections, connectionTimeout, socketTimeout, maxAutoRetries );
		restTemplate = buildRestTemplate();
	}


	public Client(
		String apiToken,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries,
		Integer connectionPoolTimeout,
		Integer checkStaleConnectionAfterInactivityTime
	) {
		super( apiToken, maxConnections, connectionTimeout, socketTimeout, maxAutoRetries, connectionPoolTimeout, checkStaleConnectionAfterInactivityTime );
		restTemplate = buildRestTemplate();
	}


	public Client( String apiToken, String apiUrl ) {
		super( apiToken, apiUrl );
		restTemplate = buildRestTemplate();
	}


	public Client( String apiToken, String apiUrl, Integer maxConnections ) {
		super( apiToken, apiUrl, maxConnections );
		restTemplate = buildRestTemplate();
	}


	public Client(
		String apiToken,
		String apiUrl,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries
	) {
		super( apiToken, apiUrl, connectionTimeout, socketTimeout, maxAutoRetries );
		restTemplate = buildRestTemplate();
	}


	public Client(
		String apiToken,
		String apiUrl,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries
	) {
		super( apiToken, apiUrl, maxConnections, connectionTimeout, socketTimeout, maxAutoRetries );
		restTemplate = buildRestTemplate();
	}


	public Client(
		String apiToken,
		String apiUrl,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxAutoRetries,
		Integer connectionPoolTimeout,
		Integer checkStaleConnectionAfterInactivityTime
	) {
		super( apiToken, apiUrl, maxConnections, connectionTimeout, socketTimeout, maxAutoRetries, connectionPoolTimeout, checkStaleConnectionAfterInactivityTime );
		restTemplate = buildRestTemplate();
	}


	public Client(
		String apiToken,
		String apiUrl,
		PoolingHttpClientConnectionManager connectionPool,
		RequestConfig defaultRequestConfig,
		DefaultHttpRequestRetryHandler requestRetryHandler,
		ConnectionConfig defaultConnectionConfig,
		ConnectionReuseStrategy connectionReuseStrategy
	) {
		super( apiToken, apiUrl, connectionPool, defaultRequestConfig, requestRetryHandler, defaultConnectionConfig, connectionReuseStrategy );
		restTemplate = buildRestTemplate();
	}


	@Override
	protected <RESPONSE> RESPONSE list( String url, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		return executeRequest( url, HttpMethod.GET, null, responseBodyType );
	}


	@Override
	protected <RESPONSE> RESPONSE get( String url, String id, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		return executeRequest( url + "/" + id, HttpMethod.GET, null, responseBodyType );
	}


	@Override
	protected <REQUEST,RESPONSE> RESPONSE post( String url, REQUEST requestBody, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		return executeRequest( url, HttpMethod.POST, requestBody, responseBodyType );
	}


	@Override
	protected <REQUEST,RESPONSE> RESPONSE patch( String url, String id, REQUEST requestBody, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		return executeRequest( url + "/" + id, HttpMethod.PATCH, requestBody, responseBodyType );
	}


	@Override
	protected void delete( String url, String id )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException {
		executeRequest( url + "/" + id, HttpMethod.DELETE, null, null );
	}


	private <RESPONSE> RESPONSE executeRequest( String url, HttpMethod httpMethod, Object requestBody, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException{
		
		try {
			return restTemplate.exchange( url, httpMethod, new HttpEntity<>( requestBody ), responseBodyType ).getBody();
		} catch( ResourceAccessException exception ) {
			Throwable cause = exception.getCause();
			if( cause instanceof ErrorResponseException ) {
				ErrorResponseException errorResponseException = (ErrorResponseException) cause;
				throw logException( new UnsuccessfulRequestException( url, errorResponseException.httpStatusCode, errorResponseException.body, errorResponseException.causedBy ) );
			}
			if( cause instanceof SocketTimeoutException ) {
				throw logException( new HttpSocketTimeoutException( url, exception ) );
			}
			if( cause instanceof ConnectTimeoutException ) {
				throw logException( new HttpConnectionTimeoutException( url, exception ) );
			}
			if( cause instanceof ConnectException ) {
				throw logException( new HttpConnectionFailException( url, exception ) );
			}
			if( cause instanceof ClientProtocolException ) {
				throw logException( new HttpProtocolException( url, exception ) );
			}
			throw logException( new HttpIOException( url, exception ) );
		}
	}


	private <EXCEPTION extends Exception> EXCEPTION logException( EXCEPTION exception ) {
		LOG.warn( exception.getMessage() );
		LOG.debug( "Request error", exception );
		return exception;
	}
	
	
	private RestTemplate buildRestTemplate() {
		RestTemplate restTemplate = new RestTemplate( new HttpComponentsClientHttpRequestFactory( httpClient ) );
		restTemplate.getInterceptors().add( new ClientHttpRequestInterceptor() {
			@Override
			public ClientHttpResponse intercept( HttpRequest request, byte[] body, ClientHttpRequestExecution execution ) throws IOException
			{
				request.getHeaders().add( "X-API-Token", apiToken );
				return execution.execute( request, body );
			}
		});
		restTemplate.setErrorHandler( new DefaultResponseErrorHandler() {
			@Override
			public boolean hasError( ClientHttpResponse response ) throws IOException {
				int httpStatus = response.getRawStatusCode();
				return httpStatus < 200 || httpStatus >= 300;
			}


			@Override
			protected void handleError( ClientHttpResponse response, HttpStatus statusCode ) throws IOException {
				ErrorResponse errorResponse = null;
				try {
					errorResponse = (ErrorResponse) restTemplate.responseEntityExtractor( ErrorResponse.class ).extractData( response ).getBody();
					throw new ErrorResponseException( statusCode.value(), errorResponse );
				} catch( RestClientException exception ) {
					throw new ErrorResponseException( statusCode.value(), exception );
				}
				
			}
		});
		return restTemplate;
	}


	@SuppressWarnings( "serial" )
	private static class ErrorResponseException extends IOException {
		private final int httpStatusCode;

		private final ErrorResponse body;

		private final Exception causedBy;


		private ErrorResponseException( int httpStatus, ErrorResponse errorResponse ) {
			this.httpStatusCode = httpStatus;
			this.body = errorResponse;
			this.causedBy = null;
		}


		private ErrorResponseException( int httpStatus, Exception cause ) {
			super( cause );
			this.httpStatusCode = httpStatus;
			this.body = null;
			this.causedBy = cause;
		}
	}
}
