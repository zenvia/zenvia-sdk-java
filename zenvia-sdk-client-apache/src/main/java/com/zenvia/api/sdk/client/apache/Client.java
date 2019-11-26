package com.zenvia.api.sdk.client.apache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


/** API Client that is backed by <a href="http://hc.apache.org/" target="_blank">Apache HTTP Client</a>.
 *  This class also relies on <a href="https://github.com/FasterXML/jackson" target="_blank">Jackson</a> for JSON handling.
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
	
	private final ObjectMapper jsonMapper = new ObjectMapper();


	/** Initializes the connection pool using default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Maximum connections:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTIONS}</li>
	 *    <li><b>Connection timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_TIMEOUT} ms</li>
	 *    <li><b>Socket timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_SOCKET_TIMEOUT} ms</li>
	 *    <li><b>Maximum connection retries:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTION_RETRIES}</li>
	 *    <li><b>Connection pool timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @since 0.9.0 */
	public Client( String apiToken ) {
		super( apiToken );
	}


	/** Initializes the connection pool using a mix of the given configurations
	 * and below default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Connection timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_TIMEOUT} ms</li>
	 *    <li><b>Socket timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_SOCKET_TIMEOUT} ms</li>
	 *    <li><b>Maximum connection retries:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTION_RETRIES}</li>
	 *    <li><b>Connection pool timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections in the pool.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @since 0.9.0 */
	public Client( String apiToken, Integer maxConnections ) {
		super( apiToken, maxConnections );
	}


	/** Initializes the connection pool using a mix of the given configurations
	 * and below default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Maximum connections:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTIONS}</li>
	 *    <li><b>Connection pool timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param connectionTimeout
	 *  The amount of time in milliseconds for a connection attempt to timeout.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_TIMEOUT} will be used instead.
	 *
	 *  @param socketTimeout
	 *  The amount of time in milliseconds for a server reply to timeout.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_SOCKET_TIMEOUT} will be used instead.
	 *
	 *  @param maxConnectionRetries
	 *  The maximum amount of connection retries automatically made by the HTTP client in
	 *  case of connection failure.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTION_RETRIES} will be used instead.
	 *
	 *  @since 0.9.0 */
	public Client(
		String apiToken,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxConnectionRetries
	) {
		super( apiToken, connectionTimeout, socketTimeout, maxConnectionRetries );
	}


	/** Initializes the connection pool using a mix of the given configurations
	 * and below default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Connection pool timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections in the pool.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @param connectionTimeout
	 *  The amount of time in milliseconds for a connection attempt to timeout.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_TIMEOUT} will be used instead.
	 *
	 *  @param socketTimeout
	 *  The amount of time in milliseconds for a server reply to timeout.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_SOCKET_TIMEOUT} will be used instead.
	 *
	 *  @param maxConnectionRetries
	 *  The maximum amount of connection retries automatically made by the HTTP client in
	 *  case of connection failure.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTION_RETRIES} will be used instead.
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
	}


	/** Initializes the connection pool using the given configurations.
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections in the pool.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @param connectionTimeout
	 *  The amount of time in milliseconds for a connection attempt to timeout.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_TIMEOUT} will be used instead.
	 *
	 *  @param socketTimeout
	 *  The amount of time in milliseconds for a server reply to timeout.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_SOCKET_TIMEOUT} will be used instead.
	 *
	 *  @param maxConnectionRetries
	 *  The maximum amount of connection retries automatically made by the HTTP client in
	 *  case of connection failure.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTION_RETRIES} will be used instead.
	 *
	 *  @param connectionPoolTimeout
	 *  The amount of time in milliseconds for a request to timeout when wait for a free connection
	 *  from the pool. When zero, it means it will wait indefinitely for a connection.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_POOL_TIMEOUT} will be used instead.
	 *
	 *  @param inactivityTimeBeforeStaleCheck
	 *  The amount of time in milliseconds of inactivity necessary to trigger a stale check on
	 *  idle pool connections.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} will be used instead.
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
	}


	/** Initializes the connection pool using default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Maximum connections:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTIONS}</li>
	 *    <li><b>Connection timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_TIMEOUT} ms</li>
	 *    <li><b>Socket timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_SOCKET_TIMEOUT} ms</li>
	 *    <li><b>Maximum connection retries:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTION_RETRIES}</li>
	 *    <li><b>Connection pool timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Normally the value {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_URL} is used, but can be
	 *  useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_URL} will be used instead.
	 *
	 *  @since 0.9.0 */
	public Client( String apiToken, String apiUrl ) {
		super( apiToken, apiUrl );
	}


	/** Initializes the connection pool using a mix of the given configurations
	 * and below default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Connection timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_TIMEOUT} ms</li>
	 *    <li><b>Socket timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_SOCKET_TIMEOUT} ms</li>
	 *    <li><b>Maximum connection retries:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTION_RETRIES}</li>
	 *    <li><b>Connection pool timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Normally the value {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_URL} is used, but can be
	 *  useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_URL} will be used instead.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections in the pool.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @since 0.9.0 */
	public Client( String apiToken, String apiUrl, Integer maxConnections ) {
		super( apiToken, apiUrl, maxConnections );
	}


	/** Initializes the connection pool using a mix of the given configurations
	 * and below default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Maximum connections:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTIONS}</li>
	 *    <li><b>Connection pool timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Normally the value {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_URL} is used, but can be
	 *  useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_URL} will be used instead.
	 *
	 *  @param connectionTimeout
	 *  The amount of time in milliseconds for a connection attempt to timeout.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_TIMEOUT} will be used instead.
	 *
	 *  @param socketTimeout
	 *  The amount of time in milliseconds for a server reply to timeout.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_SOCKET_TIMEOUT} will be used instead.
	 *
	 *  @param maxConnectionRetries
	 *  The maximum amount of connection retries automatically made by the HTTP client in
	 *  case of connection failure.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTION_RETRIES} will be used instead.
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
	}


	/** Initializes the connection pool using a mix of the given configurations
	 * and below default configurations.
	 * <br>
	 * <ul>
	 *    <li><b>Connection pool timeout:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
	 *    <li><b>Inactivity time before stale checking:</b> {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
	 *  </ul>
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Normally the value {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_URL} is used, but can be
	 *  useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_URL} will be used instead.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections in the pool.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @param connectionTimeout
	 *  The amount of time in milliseconds for a connection attempt to timeout.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_TIMEOUT} will be used instead.
	 *
	 *  @param socketTimeout
	 *  The amount of time in milliseconds for a server reply to timeout.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_SOCKET_TIMEOUT} will be used instead.
	 *
	 *  @param maxConnectionRetries
	 *  The maximum amount of connection retries automatically made by the HTTP client in
	 *  case of connection failure.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTION_RETRIES} will be used instead.
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
	}


	/** Initializes the connection pool using the given configurations.
	 *
	 *  @param apiToken
	 *  An API token generated on <a href="https://app.zenvia.com/home/api" target="_blank">Zenvia API console</a>.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Normally the value {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_URL} is used, but can be
	 *  useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_URL} will be used instead.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections in the pool.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @param connectionTimeout
	 *  The amount of time in milliseconds for a connection attempt to timeout.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_TIMEOUT} will be used instead.
	 *
	 *  @param socketTimeout
	 *  The amount of time in milliseconds for a server reply to timeout.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_SOCKET_TIMEOUT} will be used instead.
	 *
	 *  @param maxConnectionRetries
	 *  The maximum amount of connection retries automatically made by the HTTP client in
	 *  case of connection failure.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_MAX_CONNECTION_RETRIES} will be used instead.
	 *
	 *  @param connectionPoolTimeout
	 *  The amount of time in milliseconds for a request to timeout when wait for a free connection
	 *  from the pool. When zero, it means it will wait indefinitely for a connection.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_CONNECTION_POOL_TIMEOUT} will be used instead.
	 *
	 *  @param inactivityTimeBeforeStaleCheck
	 *  The amount of time in milliseconds of inactivity necessary to trigger a stale check on
	 *  idle pool connections.
	 *  When null, the default {@value com.zenvia.api.sdk.client.AbstractClient#DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} will be used instead.
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
		return executeRequest( new HttpGet( url ), null, responseBodyType );
	}


	@Override
	protected <RESPONSE> RESPONSE get( String url, String id, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return executeRequest( new HttpGet( url + "/" + id ), null, responseBodyType );
	}


	@Override
	protected <REQUEST,RESPONSE> RESPONSE post( String url, REQUEST requestBody, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return executeRequest( new HttpPost( url ), requestBody, responseBodyType );
	}


	@Override
	protected <REQUEST,RESPONSE> RESPONSE patch( String url, String id, REQUEST requestBody, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return executeRequest( new HttpPatch( url + "/" + id ), requestBody, responseBodyType );
	}


	@Override
	protected void delete( String url, String id )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		executeRequest( new HttpDelete( url + "/" + id ), null, null );
	}


	private <RESPONSE> RESPONSE executeRequest( HttpUriRequest httpMethod, Object requestBody, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		HttpResponse httpResponse = executeRequest( httpMethod, requestBody );
		int httpStatus = httpResponse.getStatusLine().getStatusCode();
		if ( httpStatus < 200 || httpStatus >= 300 ) {
			throw logException( new UnsuccessfulRequestException(
				httpMethod.getURI().toString(),
				httpStatus,
				deserialize( httpResponse.getEntity(), ErrorResponse.class,  httpMethod.getURI().toString(), httpStatus )
			) );
		}
		return deserialize( httpResponse.getEntity(), responseBodyType,  httpMethod.getURI().toString(), httpStatus );
	}
	


	private HttpResponse executeRequest( HttpUriRequest httpMethod, Object requestBody )
		throws HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {

		HttpClientContext httpContext = new HttpClientContext();

		httpMethod.addHeader( "X-API-Token", apiToken );
		if( requestBody != null && httpMethod instanceof HttpEntityEnclosingRequest ) {
			( (HttpEntityEnclosingRequest) httpMethod ).setEntity(
				new ByteArrayEntity( serialize( requestBody ), ContentType.APPLICATION_JSON )
			);
		}

		String url = httpMethod.getURI().toString();
		try {
			return httpClient.execute( httpMethod, httpContext );
		} catch( SocketTimeoutException cause ) {
			throw logException( new HttpSocketTimeoutException( url, cause ) );
		} catch( ConnectTimeoutException cause ) {
			throw logException( new HttpConnectionTimeoutException( url, cause ) );
		} catch( ConnectException cause ) {
			throw logException( new HttpConnectionFailException( url, cause ) );
		} catch( IOException cause ) {
			throw logException( new HttpIOException( url, cause ) );
		}
	}


	private <TYPE> TYPE deserialize( HttpEntity entity, Class<TYPE>type, String url, int httpStatus ) throws UnsuccessfulRequestException, HttpIOException {
		if( entity == null || entity.getContentLength() == 0 ) {
			return null;
		}
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			entity.writeTo( buffer );
			byte[] data = buffer.toByteArray();
			if( LOG.isTraceEnabled() ) {
				LOG.trace( "Response body: {}", new String( data, StandardCharsets.UTF_8 ) );
			}
			return jsonMapper.readValue( data, type );
		} catch( JsonMappingException | JsonParseException exception ) {
			throw logException( new UnsuccessfulRequestException( url, httpStatus, exception ) );
		} catch( IOException exception ) {
			throw logException( new HttpIOException( url, exception ) );
		}
	}


	private byte[] serialize( Object data ) throws IllegalArgumentException {
		try {
			byte[] serialized = jsonMapper.writeValueAsBytes( data );
			if( LOG.isTraceEnabled() ) {
				LOG.trace( "Request body: {}", new String( serialized, StandardCharsets.UTF_8 ) );
			}
			return serialized;
		}
		catch( JsonProcessingException exception ) {
			LOG.error( "Exception serializing request body", exception );
			throw new IllegalArgumentException( "Exception serializing request body", exception );
		}
	}


	private <EXCEPTION extends Exception> EXCEPTION logException( EXCEPTION exception ) {
		LOG.warn( exception.getMessage() );
		LOG.debug( "Request error", exception );
		return exception;
	}
}
