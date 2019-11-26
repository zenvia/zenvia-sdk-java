package com.zenvia.api.sdk.client.spring;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
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
import com.zenvia.api.sdk.client.Channel;
import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.client.errors.ErrorResponse;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionFailException;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionTimeoutException;
import com.zenvia.api.sdk.client.exceptions.HttpIOException;
import com.zenvia.api.sdk.client.exceptions.HttpSocketTimeoutException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.exceptions.UnsupportedChannelException;
import com.zenvia.api.sdk.client.subscriptions.PartialSubscription;
import com.zenvia.api.sdk.client.subscriptions.Subscription;


/** API Client that is backed by <a href="https://github.com/spring-projects/spring-framework/blob/master/spring-web/src/main/java/org/springframework/web/client/RestTemplate.java" target="_blank">Spring Boot Rest Template</a>.
 *  The Rest Template is backed by <a href="http://hc.apache.org/" target="_blank">Apache HTTP Client</a>
 *  and <a href="https://github.com/FasterXML/jackson" target="_blank">Jackson</a>.
 *  <br><br>
 *  Its is meant to not have too many dependencies.
 *  <br><br>
 *  Sending WhatsApp text message example:
 *  <br>
 *  <pre>{@code
 *    Client client = new Client( "API TOKEN" );
 *    client.getChannel( "whatsapp" ).sendMessage(
 *      "WhatsApp-business-id", "receiver-phone-number", new TextContent( "Text to be sent!" )
 *    );
 *  }</pre>
 *
 *  @since 0.9.0 */
public class Client extends AbstractClient {
	private static final Logger LOG = LoggerFactory.getLogger( Client.class );
	
	private final RestTemplate restTemplate;


	/** Initializes the connection pool using default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Maximum connections:</b> {@value #DEFAULT_MAX_CONNECTIONS}</li>
	 *    <li><b>Connection timeout:</b> {@value #DEFAULT_CONNECTION_TIMEOUT} ms</li>
	 *    <li><b>Socket timeout:</b> {@value #DEFAULT_SOCKET_TIMEOUT} ms</li>
	 *    <li><b>Maximum connection retries:</b> {@value #DEFAULT_MAX_CONNECTION_RETRIES}</li>
	 *    <li><b>Connection pool timeout:</b> {@value #DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value #DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @since 0.9.0 */
	public Client( String apiToken ) {
		super( apiToken );
		restTemplate = buildRestTemplate();
	}


	/** Initializes the connection pool using a mix of the given configurations
	 * and below default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Connection timeout:</b> {@value #DEFAULT_CONNECTION_TIMEOUT} ms</li>
	 *    <li><b>Socket timeout:</b> {@value #DEFAULT_SOCKET_TIMEOUT} ms</li>
	 *    <li><b>Maximum connection retries:</b> {@value #DEFAULT_MAX_CONNECTION_RETRIES}</li>
	 *    <li><b>Connection pool timeout:</b> {@value #DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value #DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections in the pool.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @since 0.9.0 */
	public Client( String apiToken, Integer maxConnections ) {
		super( apiToken, maxConnections );
		restTemplate = buildRestTemplate();
	}


	/** Initializes the connection pool using a mix of the given configurations
	 * and below default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Maximum connections:</b> {@value #DEFAULT_MAX_CONNECTIONS}</li>
	 *    <li><b>Connection pool timeout:</b> {@value #DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value #DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param connectionTimeout
	 *  The amount of time in milliseconds for a connection attempt to timeout.
	 *  When null, the default {@value #DEFAULT_CONNECTION_TIMEOUT} will be used instead.
	 *
	 *  @param socketTimeout
	 *  The amount of time in milliseconds for a server reply to timeout.
	 *  When null, the default {@value #DEFAULT_SOCKET_TIMEOUT} will be used instead.
	 *
	 *  @param maxConnectionRetries
	 *  The maximum amount of connection retries automatically made by the HTTP client in
	 *  case of connection failure.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTION_RETRIES} will be used instead.
	 *
	 *  @since 0.9.0 */
	public Client(
		String apiToken,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxConnectionRetries
	) {
		super( apiToken, connectionTimeout, socketTimeout, maxConnectionRetries );
		restTemplate = buildRestTemplate();
	}


	/** Initializes the connection pool using a mix of the given configurations
	 * and below default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Connection pool timeout:</b> {@value #DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value #DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections in the pool.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @param connectionTimeout
	 *  The amount of time in milliseconds for a connection attempt to timeout.
	 *  When null, the default {@value #DEFAULT_CONNECTION_TIMEOUT} will be used instead.
	 *
	 *  @param socketTimeout
	 *  The amount of time in milliseconds for a server reply to timeout.
	 *  When null, the default {@value #DEFAULT_SOCKET_TIMEOUT} will be used instead.
	 *
	 *  @param maxConnectionRetries
	 *  The maximum amount of connection retries automatically made by the HTTP client in
	 *  case of connection failure.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTION_RETRIES} will be used instead.
	 *
	 *  @since 0.9.0 */
	public Client(
		String apiToken,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxConnectionRetries
	) {
		super( apiToken, maxConnections, connectionTimeout, socketTimeout, maxConnectionRetries );
		restTemplate = buildRestTemplate();
	}


	/** Initializes the connection pool using the given configurations.
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections in the pool.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @param connectionTimeout
	 *  The amount of time in milliseconds for a connection attempt to timeout.
	 *  When null, the default {@value #DEFAULT_CONNECTION_TIMEOUT} will be used instead.
	 *
	 *  @param socketTimeout
	 *  The amount of time in milliseconds for a server reply to timeout.
	 *  When null, the default {@value #DEFAULT_SOCKET_TIMEOUT} will be used instead.
	 *
	 *  @param maxConnectionRetries
	 *  The maximum amount of connection retries automatically made by the HTTP client in
	 *  case of connection failure.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTION_RETRIES} will be used instead.
	 *
	 *  @param connectionPoolTimeout
	 *  The amount of time in milliseconds for a request to timeout when wait for a free connection
	 *  from the pool. When zero, it means it will wait indefinitely for a connection.
	 *  When null, the default {@value #DEFAULT_CONNECTION_POOL_TIMEOUT} will be used instead.
	 *
	 *  @param inactivityTimeBeforeStaleCheck
	 *  The amount of time in milliseconds of inactivity necessary to trigger a stale check on
	 *  idle pool connections.
	 *  When null, the default {@value #DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} will be used instead.
	 *
	 *  @since 0.9.0 */
	public Client(
		String apiToken,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxConnectionRetries,
		Integer connectionPoolTimeout,
		Integer inactivityTimeBeforeStaleCheck
	) {
		super( apiToken, maxConnections, connectionTimeout, socketTimeout, maxConnectionRetries, connectionPoolTimeout, inactivityTimeBeforeStaleCheck );
		restTemplate = buildRestTemplate();
	}


	/** Initializes the connection pool using default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Maximum connections:</b> {@value #DEFAULT_MAX_CONNECTIONS}</li>
	 *    <li><b>Connection timeout:</b> {@value #DEFAULT_CONNECTION_TIMEOUT} ms</li>
	 *    <li><b>Socket timeout:</b> {@value #DEFAULT_SOCKET_TIMEOUT} ms</li>
	 *    <li><b>Maximum connection retries:</b> {@value #DEFAULT_MAX_CONNECTION_RETRIES}</li>
	 *    <li><b>Connection pool timeout:</b> {@value #DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value #DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Normally the value {@value #DEFAULT_URL} is used, but can be
	 *  useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value #DEFAULT_URL} will be used instead.
	 *
	 *  @since 0.9.0 */
	public Client( String apiToken, String apiUrl ) {
		super( apiToken, apiUrl );
		restTemplate = buildRestTemplate();
	}


	/** Initializes the connection pool using a mix of the given configurations
	 * and below default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Connection timeout:</b> {@value #DEFAULT_CONNECTION_TIMEOUT} ms</li>
	 *    <li><b>Socket timeout:</b> {@value #DEFAULT_SOCKET_TIMEOUT} ms</li>
	 *    <li><b>Maximum connection retries:</b> {@value #DEFAULT_MAX_CONNECTION_RETRIES}</li>
	 *    <li><b>Connection pool timeout:</b> {@value #DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value #DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Normally the value {@value #DEFAULT_URL} is used, but can be
	 *  useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value #DEFAULT_URL} will be used instead.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections in the pool.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @since 0.9.0 */
	public Client( String apiToken, String apiUrl, Integer maxConnections ) {
		super( apiToken, apiUrl, maxConnections );
		restTemplate = buildRestTemplate();
	}


	/** Initializes the connection pool using a mix of the given configurations
	 * and below default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Maximum connections:</b> {@value #DEFAULT_MAX_CONNECTIONS}</li>
	 *    <li><b>Connection pool timeout:</b> {@value #DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value #DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Normally the value {@value #DEFAULT_URL} is used, but can be
	 *  useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value #DEFAULT_URL} will be used instead.
	 *
	 *  @param connectionTimeout
	 *  The amount of time in milliseconds for a connection attempt to timeout.
	 *  When null, the default {@value #DEFAULT_CONNECTION_TIMEOUT} will be used instead.
	 *
	 *  @param socketTimeout
	 *  The amount of time in milliseconds for a server reply to timeout.
	 *  When null, the default {@value #DEFAULT_SOCKET_TIMEOUT} will be used instead.
	 *
	 *  @param maxConnectionRetries
	 *  The maximum amount of connection retries automatically made by the HTTP client in
	 *  case of connection failure.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTION_RETRIES} will be used instead.
	 *
	 *  @since 0.9.0 */
	public Client(
		String apiToken,
		String apiUrl,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxConnectionRetries
	) {
		super( apiToken, apiUrl, connectionTimeout, socketTimeout, maxConnectionRetries );
		restTemplate = buildRestTemplate();
	}


	/** Initializes the connection pool using a mix of the given configurations
	 * and below default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Connection pool timeout:</b> {@value #DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value #DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Normally the value {@value #DEFAULT_URL} is used, but can be
	 *  useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value #DEFAULT_URL} will be used instead.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections in the pool.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @param connectionTimeout
	 *  The amount of time in milliseconds for a connection attempt to timeout.
	 *  When null, the default {@value #DEFAULT_CONNECTION_TIMEOUT} will be used instead.
	 *
	 *  @param socketTimeout
	 *  The amount of time in milliseconds for a server reply to timeout.
	 *  When null, the default {@value #DEFAULT_SOCKET_TIMEOUT} will be used instead.
	 *
	 *  @param maxConnectionRetries
	 *  The maximum amount of connection retries automatically made by the HTTP client in
	 *  case of connection failure.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTION_RETRIES} will be used instead.
	 *
	 *  @since 0.9.0 */
	public Client(
		String apiToken,
		String apiUrl,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxConnectionRetries
	) {
		super( apiToken, apiUrl, maxConnections, connectionTimeout, socketTimeout, maxConnectionRetries );
		restTemplate = buildRestTemplate();
	}


	/** Initializes the connection pool using the given configurations.
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Normally the value {@value #DEFAULT_URL} is used, but can be
	 *  useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value #DEFAULT_URL} will be used instead.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections in the pool.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @param connectionTimeout
	 *  The amount of time in milliseconds for a connection attempt to timeout.
	 *  When null, the default {@value #DEFAULT_CONNECTION_TIMEOUT} will be used instead.
	 *
	 *  @param socketTimeout
	 *  The amount of time in milliseconds for a server reply to timeout.
	 *  When null, the default {@value #DEFAULT_SOCKET_TIMEOUT} will be used instead.
	 *
	 *  @param maxConnectionRetries
	 *  The maximum amount of connection retries automatically made by the HTTP client in
	 *  case of connection failure.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTION_RETRIES} will be used instead.
	 *
	 *  @param connectionPoolTimeout
	 *  The amount of time in milliseconds for a request to timeout when wait for a free connection
	 *  from the pool. When zero, it means it will wait indefinitely for a connection.
	 *  When null, the default {@value #DEFAULT_CONNECTION_POOL_TIMEOUT} will be used instead.
	 *
	 *  @param inactivityTimeBeforeStaleCheck
	 *  The amount of time in milliseconds of inactivity necessary to trigger a stale check on
	 *  idle pool connections.
	 *  When null, the default {@value #DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} will be used instead.
	 *
	 *  @since 0.9.0 */
	public Client(
		String apiToken,
		String apiUrl,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxConnectionRetries,
		Integer connectionPoolTimeout,
		Integer inactivityTimeBeforeStaleCheck
	) {
		super( apiToken, apiUrl, maxConnections, connectionTimeout, socketTimeout, maxConnectionRetries, connectionPoolTimeout, inactivityTimeBeforeStaleCheck );
		restTemplate = buildRestTemplate();
	}


	// Overriding so it also appears on the Client documentation
	@Override
	public Channel getChannel( String channelType ) throws UnsupportedChannelException {
		return super.getChannel( channelType );
	}


	// Overriding so it also appears on the Client documentation
	@Override
	public Channel getChannel( ChannelType channelType ) throws UnsupportedChannelException {
		return super.getChannel( channelType );
	}


	// Overriding so it also appears on the Client documentation
	@Override
	public List<Subscription> listSubscriptions()
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return super.listSubscriptions();
	}


	// Overriding so it also appears on the Client documentation
	@Override
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION createSubscription( SUBSCRIPTION subscription )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return super.createSubscription( subscription );
	}


	// Overriding so it also appears on the Client documentation
	@Override
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION getSubscription( String id )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return super.getSubscription( id );
	}


	// Overriding so it also appears on the Client documentation
	@Override
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION updateSubscription( SUBSCRIPTION subscription )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return super.updateSubscription( subscription );
	}


	// Overriding so it also appears on the Client documentation
	@Override
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION updateSubscription( String id, PartialSubscription partialSubscription )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return super.updateSubscription( id, partialSubscription );
	}


	// Overriding so it also appears on the Client documentation
	@Override
	public void deleteSubscription( String id )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		super.deleteSubscription( id );
	}


	@Override
	protected <RESPONSE> RESPONSE list( String url, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return executeRequest( url, HttpMethod.GET, null, responseBodyType );
	}


	@Override
	protected <RESPONSE> RESPONSE get( String url, String id, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return executeRequest( url + "/" + id, HttpMethod.GET, null, responseBodyType );
	}


	@Override
	protected <REQUEST,RESPONSE> RESPONSE post( String url, REQUEST requestBody, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return executeRequest( url, HttpMethod.POST, requestBody, responseBodyType );
	}


	@Override
	protected <REQUEST,RESPONSE> RESPONSE patch( String url, String id, REQUEST requestBody, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return executeRequest( url + "/" + id, HttpMethod.PATCH, requestBody, responseBodyType );
	}


	@Override
	protected void delete( String url, String id )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		executeRequest( url + "/" + id, HttpMethod.DELETE, null, null );
	}


	private <RESPONSE> RESPONSE executeRequest( String url, HttpMethod httpMethod, Object requestBody, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException{
		
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
