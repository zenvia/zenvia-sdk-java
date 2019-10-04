package com.zenvia.api.sdk.client;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.zenvia.api.sdk.client.exceptions.HttpConnectionFailException;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionTimeoutException;
import com.zenvia.api.sdk.client.exceptions.HttpIOException;
import com.zenvia.api.sdk.client.exceptions.HttpSocketTimeoutException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.exceptions.UnsupportedChannelException;
import com.zenvia.api.sdk.client.messages.MessageRequest;
import com.zenvia.api.sdk.client.messages.Message;
import com.zenvia.api.sdk.client.subscriptions.MessageStatusSubscription;
import com.zenvia.api.sdk.client.subscriptions.MessageSubscription;
import com.zenvia.api.sdk.client.subscriptions.PartialSubscription;
import com.zenvia.api.sdk.client.subscriptions.Subscription;


/** Core class for the client side of this SDK.
 *  <br><br>
 *  The instances of this class have a internal connection pool, so all instances need to be closed
 *  once they are no longer needed.
 *  <br><br>
 *  Also, instances should be shared as much as possible. Ideally,
 *  only one instance should be created and shared across the entire application.
 *  <br><br>
 *  By default, the internal connection pool is created using the following configuration:
 *  <br>
 *  <ul>
 *    <li><b>Maximum connections:</b> {@value #DEFAULT_MAX_CONNECTIONS}</li>
 *    <li><b>Connection timeout:</b> {@value #DEFAULT_CONNECTION_TIMEOUT} ms</li>
 *    <li><b>Socket timeout:</b> {@value #DEFAULT_SOCKET_TIMEOUT} ms</li>
 *    <li><b>Maximum connection retries:</b> {@value #DEFAULT_MAX_CONNECTION_RETRIES}</li>
 *    <li><b>Connection pool timeout:</b> {@value #DEFAULT_CONNECTION_POOL_TIMEOUT} ms</li>
 *    <li><b>Inactivity time before stale checking:</b> {@value #DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK} ms</li>
 *  </ul>
 *  <br>
 *  See concrete implementations for examples of use.
 *
 *  @since 0.9.0 */
public abstract class AbstractClient implements Closeable {
	/** {@value} */
	public static final String DEFAULT_URL = "https://api.zenvia.com";

	/** {@value} */
	public static final int DEFAULT_MAX_CONNECTIONS = 100;

	/** {@value} */
	public static final int DEFAULT_CONNECTION_TIMEOUT = 25000;

	/** {@value} */
	public static final int DEFAULT_SOCKET_TIMEOUT = 60000;

	/** {@value} */
	public static final int DEFAULT_CONNECTION_POOL_TIMEOUT = 0;

	/** {@value} */
	public static final int DEFAULT_MAX_CONNECTION_RETRIES = 4;

	/** {@value} */
	public static final int DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK = 5000;

	protected final String apiToken;

	protected final String apiUrl;

	protected final String subscriptionApiUrl;

	protected final HttpClient httpClient;

	private final ConnectionConfig connectionConfig;

	private final RequestConfig requestConfig;

	private final ConnectionReuseStrategy connectionReuseStrategy;

	private final DefaultHttpRequestRetryHandler requestRetryHandler;

	private final PoolingHttpClientConnectionManager connectionPool;


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
	 *  An API token generated on Zenvia API console: {@link "https://app.zenvia.com/home/api"}.
	 *
	 *  @since 0.9.0 */
	public AbstractClient( String apiToken ) {
		this( apiToken, (String) null );
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
	 *  An API token generated on Zenvia API console: {@link "https://app.zenvia.com/home/api"}.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections the pool can have.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @since 0.9.0 */
	public AbstractClient(
		String apiToken,
		Integer maxConnections
	) {
		this( apiToken, maxConnections, null, null, null );
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
	 *  An API token generated on Zenvia API console: {@link "https://app.zenvia.com/home/api"}.
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
	public AbstractClient(
		String apiToken,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxConnectionRetries
	) {
		this( apiToken, (Integer) null, connectionTimeout, socketTimeout, maxConnectionRetries );
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
	 *  An API token generated on Zenvia API console: {@link "https://app.zenvia.com/home/api"}.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections the pool can have.
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
	public AbstractClient(
		String apiToken,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxConnectionRetries
	) {
		this( apiToken, maxConnections, connectionTimeout, socketTimeout, maxConnectionRetries, null, null );
	}


	/** Initializes the connection pool using the given configurations.
	 *
	 *  @param apiToken
	 *  An API token generated on Zenvia API console: {@link "https://app.zenvia.com/home/api"}.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections the pool can have.
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
	public AbstractClient(
		String apiToken,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxConnectionRetries,
		Integer connectionPoolTimeout,
		Integer inactivityTimeBeforeStaleCheck
	) {
		this( apiToken, null, maxConnections, connectionTimeout, socketTimeout, maxConnectionRetries, connectionPoolTimeout, inactivityTimeBeforeStaleCheck );
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
	 *  An API token generated on Zenvia API console: {@link "https://app.zenvia.com/home/api"}.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Usually the value {@value #DEFAULT_URL} is used, but change it
	 *  can be useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value #DEFAULT_URL} will be used instead.
	 *
	 *  @since 0.9.0 */
	public AbstractClient( String apiToken, String apiUrl ) {
		this( apiToken, apiUrl, null, null, null );
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
	 *  An API token generated on Zenvia API console: {@link "https://app.zenvia.com/home/api"}.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Usually the value {@value #DEFAULT_URL} is used, but change it
	 *  can be useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value #DEFAULT_URL} will be used instead.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections the pool can have.
	 *  When null, the default {@value #DEFAULT_MAX_CONNECTIONS} will be used instead.
	 *
	 *  @since 0.9.0 */
	public AbstractClient(
		String apiToken,
		String apiUrl,
		Integer maxConnections
	) {
		this( apiToken, apiUrl, maxConnections, null, null, null );
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
	 *  An API token generated on Zenvia API console: {@link "https://app.zenvia.com/home/api"}.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Usually the value {@value #DEFAULT_URL} is used, but change it
	 *  can be useful for testing, and in rare cases, for custom integrations.
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
	public AbstractClient(
		String apiToken,
		String apiUrl,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxConnectionRetries
	) {
		this( apiToken, apiUrl, null, connectionTimeout, socketTimeout, maxConnectionRetries );
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
	 *  An API token generated on Zenvia API console: {@link "https://app.zenvia.com/home/api"}.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Usually the value {@value #DEFAULT_URL} is used, but change it
	 *  can be useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value #DEFAULT_URL} will be used instead.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections the pool can have.
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
	public AbstractClient(
		String apiToken,
		String apiUrl,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxConnectionRetries
	) {
		this( apiToken, apiUrl, maxConnections, connectionTimeout, socketTimeout, maxConnectionRetries, null, null );
	}


	/** Initializes the connection pool using the given configurations.
	 *
	 *  @param apiToken
	 *  An API token generated on Zenvia API console: {@link "https://app.zenvia.com/home/api"}.
	 *
	 *  @param apiUrl
	 *  The URL for the API service. Usually the value {@value #DEFAULT_URL} is used, but change it
	 *  can be useful for testing, and in rare cases, for custom integrations.
	 *  When null, the default {@value #DEFAULT_URL} will be used instead.
	 *
	 *  @param maxConnections
	 *  The maximum number of connections the pool can have.
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
	public AbstractClient(
		String apiToken,
		String apiUrl,
		Integer maxConnections,
		Integer connectionTimeout,
		Integer socketTimeout,
		Integer maxConnectionRetries,
		Integer connectionPoolTimeout,
		Integer inactivityTimeBeforeStaleCheck
	) {
		this(
			apiToken,
			apiUrl,
			buildConnectionPool(
				valueOrDefault( maxConnections, DEFAULT_MAX_CONNECTIONS ),
				valueOrDefault( inactivityTimeBeforeStaleCheck, DEFAULT_INACTIVITY_TIME_BEFORE_STALE_CHECK )
			),
			buildRequestConfig(
				valueOrDefault( connectionTimeout, DEFAULT_CONNECTION_TIMEOUT ),
				valueOrDefault( socketTimeout, DEFAULT_SOCKET_TIMEOUT ),
				valueOrDefault( connectionPoolTimeout, DEFAULT_CONNECTION_POOL_TIMEOUT )
			),
			new DefaultHttpRequestRetryHandler(
				valueOrDefault( maxConnectionRetries, DEFAULT_MAX_CONNECTION_RETRIES ),
				false
			),
			ConnectionConfig.custom().setCharset( StandardCharsets.UTF_8 ).build(),
			DefaultConnectionReuseStrategy.INSTANCE
		);
	}


	private AbstractClient(
		String apiToken,
		String apiUrl,
		PoolingHttpClientConnectionManager connectionPool,
		RequestConfig defaultRequestConfig,
		DefaultHttpRequestRetryHandler requestRetryHandler,
		ConnectionConfig defaultConnectionConfig,
		ConnectionReuseStrategy connectionReuseStrategy
	) {
		this.apiToken = apiToken;
		this.apiUrl = valueOrDefault( apiUrl, DEFAULT_URL );
		this.subscriptionApiUrl = this.apiUrl + "/v1/subscriptions";
		
		this.connectionPool = connectionPool;
		this.connectionConfig = defaultConnectionConfig;
		this.requestConfig = defaultRequestConfig;
		this.connectionReuseStrategy = connectionReuseStrategy;
		this.requestRetryHandler = requestRetryHandler;
		
		httpClient = HttpClientBuilder.create()
			.setConnectionManager( this.connectionPool )
			.setDefaultConnectionConfig( this.connectionConfig )
			.setDefaultRequestConfig( this.requestConfig )
			.setConnectionReuseStrategy( this.connectionReuseStrategy )
			.setRetryHandler( this.requestRetryHandler )
			.disableCookieManagement()
			.build();
	}


	/** Selects the channel for message API calls.
	 *
	 *  @param channelType Channel being selected.
	 *
	 *  @return Selected channel when it is supported.
	 *
	 *  @throws UnsupportedChannelException If the channel is not supported.
	 *
	 *  @since 0.9.0 */
	public Channel getChannel( String channelType ) throws UnsupportedChannelException {
		return getChannel( ChannelType.parse( channelType ) );
	}


	/** Selects the channel for message API calls.
	 *
	 *  @param channelType Channel being selected.
	 *
	 *  @return Selected channel when it is supported.
	 *
	 *  @throws UnsupportedChannelException If the channel is not supported.
	 *
	 *  @since 0.9.0 */
	public Channel getChannel( ChannelType channelType ) throws UnsupportedChannelException {
		return new Channel( channelType, this );
	}


	/** Gets all subscriptions, of all types and all {@link Channel channels}. So the list may contain
	 * {@link MessageSubscription} and {@link MessageStatusSubscription} mixed together.
	 *
	 *  @return Existing subscriptions.
	 *
	 *  @throws UnsuccessfulRequestException
	 *  If the request failed, indicated by the
	 *  {@link UnsuccessfulRequestException#httpStatusCode} from the server response.
	 *
	 *  @throws HttpSocketTimeoutException
	 *  If the server response timed out. 
	 *
	 *  @throws HttpConnectionTimeoutException
	 *  If the connection attempt timed out.
	 *
	 *  @throws HttpConnectionFailException
	 *  If the connection attempt failed.
	 *
	 *  @throws HttpIOException
	 *  If any other communication problem happens.
	 *
	 *  @since 0.9.0 */
	public List<Subscription> listSubscriptions()
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return Arrays.asList( list( subscriptionApiUrl, Subscription[].class ) );
	}


	/** Creates a subscription. Subscription must be either for
	 *  {@link MessageSubscription messages} or for {@link MessageStatusSubscription message status},
	 *  and also must be tied to a {@link ChannelType channel}.
	 *
	 *  @param subscription Subscription to be created.
	 *
	 *  @return Subscription created if successful.
	 *
	 *  @throws UnsuccessfulRequestException
	 *  If the creation failed, indicated by the
	 *  {@link UnsuccessfulRequestException#httpStatusCode} from the server response.
	 *
	 *  @throws HttpSocketTimeoutException
	 *  If the server response timed out. 
	 *
	 *  @throws HttpConnectionTimeoutException
	 *  If the connection attempt timed out.
	 *
	 *  @throws HttpConnectionFailException
	 *  If the connection attempt failed.
	 *
	 *  @throws HttpIOException
	 *  If any other communication problem happens.
	 *
	 *  @since 0.9.0 */
	@SuppressWarnings( "unchecked" )
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION createSubscription( SUBSCRIPTION subscription )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return (SUBSCRIPTION) post( subscriptionApiUrl, subscription, Subscription.class );
	}


	/** Gets the subscription with given id.
	 *
	 *  @param id Id of the subscription to be fetched.
	 *
	 *  @return The subscription with the given id, if it exists.
	 *
	 *  @throws UnsuccessfulRequestException
	 *  If the request failed, indicated by the
	 *  {@link UnsuccessfulRequestException#httpStatusCode} from the server response.
	 *  The request will also fail if no subscription with the given id exists.
	 *
	 *  @throws HttpSocketTimeoutException
	 *  If the server response timed out. 
	 *
	 *  @throws HttpConnectionTimeoutException
	 *  If the connection attempt timed out.
	 *
	 *  @throws HttpConnectionFailException
	 *  If the connection attempt failed.
	 *
	 *  @throws HttpIOException
	 *  If any other communication problem happens.
	 *
	 *  @since 0.9.0 */
	@SuppressWarnings( "unchecked" )
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION getSubscription( String id )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return (SUBSCRIPTION) get( subscriptionApiUrl, id, Subscription.class );
	}


	/** Updates subscription {@link com.zenvia.api.sdk.client.subscriptions.Webhook webhook}
	 *  and {@link com.zenvia.api.sdk.client.subscriptions.SubscriptionStatus status}.
	 *  Other attributes from subscription are not updatable.
	 *
	 *  @param subscription Subscription to be updated.
	 *
	 *  @return The subscription updated, if the update is successful.
	 *
	 *  @throws UnsuccessfulRequestException
	 *  If the update failed, indicated by the
	 *  {@link UnsuccessfulRequestException#httpStatusCode} from the server response.
	 *  The request will also if the subscription does not exist.
	 *
	 *  @throws HttpSocketTimeoutException
	 *  If the server response timed out. 
	 *
	 *  @throws HttpConnectionTimeoutException
	 *  If the connection attempt timed out.
	 *
	 *  @throws HttpConnectionFailException
	 *  If the connection attempt failed.
	 *
	 *  @throws HttpIOException
	 *  If any other communication problem happens.
	 *
	 *  @since 0.9.0 */
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION updateSubscription( SUBSCRIPTION subscription )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return updateSubscription( subscription.id, new PartialSubscription( subscription ) );
	}


	/** Updates subscription {@link com.zenvia.api.sdk.client.subscriptions.Webhook webhook}
	 *  and {@link com.zenvia.api.sdk.client.subscriptions.SubscriptionStatus status}.
	 *  Other attributes from subscription are not updatable.
	 *
	 *  @param id Id of the subscription to be updated.
	 *
	 *  @param partialSubscription Object containing the new
	 *  {@link com.zenvia.api.sdk.client.subscriptions.Webhook webhook}
	 *  and {@link com.zenvia.api.sdk.client.subscriptions.SubscriptionStatus status}
	 *  of the subscription.
	 *
	 *  @return The subscription updated, if the update is successful.
	 *
	 *  @throws UnsuccessfulRequestException
	 *  If the update failed, indicated by the
	 *  {@link UnsuccessfulRequestException#httpStatusCode} from the server response.
	 *  The request will also fail if no subscription with the given id exists.
	 *
	 *  @throws HttpSocketTimeoutException
	 *  If the server response timed out. 
	 *
	 *  @throws HttpConnectionTimeoutException
	 *  If the connection attempt timed out.
	 *
	 *  @throws HttpConnectionFailException
	 *  If the connection attempt failed.
	 *
	 *  @throws HttpIOException
	 *  If any other communication problem happens.
	 *
	 *  @since 0.9.0 */
	@SuppressWarnings( "unchecked" )
	public <SUBSCRIPTION extends Subscription> SUBSCRIPTION updateSubscription( String id, PartialSubscription partialSubscription )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return (SUBSCRIPTION) patch( subscriptionApiUrl, id, partialSubscription, Subscription.class );
	}


	/** Deletes the subscription with the given id.
	 *
	 *  @param id Id of the subscription to be deleted.
	 *
	 *  @throws UnsuccessfulRequestException
	 *  If the deletion failed, indicated by the
	 *  {@link UnsuccessfulRequestException#httpStatusCode} from the server response.
	 *  The request will also fail if no subscription with the given id exists.
	 *
	 *  @throws HttpSocketTimeoutException
	 *  If the server response timed out. 
	 *
	 *  @throws HttpConnectionTimeoutException
	 *  If the connection attempt timed out.
	 *
	 *  @throws HttpConnectionFailException
	 *  If the connection attempt failed.
	 *
	 *  @throws HttpIOException
	 *  If any other communication problem happens.
	 *
	 *  @since 0.9.0 */
	public void deleteSubscription( String id )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		delete( subscriptionApiUrl, id );
	}


	protected Message sendMessage( Channel channel, MessageRequest messageRequest )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return post( channel.url, messageRequest, Message.class );
	}


	protected abstract <RESPONSE> RESPONSE list( String url, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException;


	protected abstract <RESPONSE> RESPONSE get( String url, String id, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException;


	protected abstract <REQUEST,RESPONSE> RESPONSE post( String url, REQUEST requestBody, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException;


	protected abstract <REQUEST,RESPONSE> RESPONSE patch( String url, String id, REQUEST requestBody, Class<RESPONSE> responseBodyType )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException;


	protected abstract void delete( String url, String id )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException;


	/** Returns the configured service URL. In most cases it will be {@value #DEFAULT_URL}.
	 *
	 *  @since 0.9.0 */
	public String getApiUrl() {
		return apiUrl;
	}


	/** Shuts down the client and connection pool.
	 *
	 *  @since 0.9.0 */
	@Override
	public void close() {
		if( this.connectionPool != null ) {
			this.connectionPool.shutdown();
		}
	}


	private static final PoolingHttpClientConnectionManager buildConnectionPool(
		int maxConnections, int validateAfterInactivity
	) {
		PoolingHttpClientConnectionManager connectionPool = new PoolingHttpClientConnectionManager();
		connectionPool.setMaxTotal( maxConnections );
		connectionPool.setDefaultMaxPerRoute( maxConnections );
		connectionPool.setValidateAfterInactivity( validateAfterInactivity );
		
		return connectionPool;
	}


	private static final RequestConfig buildRequestConfig( int connectionTimeout, int socketTimeout, int poolTimeout ) {
		return RequestConfig.custom()
			.setConnectionRequestTimeout( poolTimeout )
			.setConnectTimeout( connectionTimeout )
			.setSocketTimeout( socketTimeout )
			.build();
	}


	private static final <TYPE> TYPE valueOrDefault( TYPE value, TYPE defaultValue ) {
		return value == null ? defaultValue : value;
	}


	/** The maximum connections the pool can have.
	 *  <br><br>
	 *  This is set by the client constructor.
	 *
	 *  @since 1.0.0 */
	public int getMaxConnections() {
		return connectionPool.getMaxTotal();
	}


	/** The amount of time in milliseconds of inactivity necessary to trigger a stale check on
	 *  idle pool connections.
	 *  <br><br>
	 *  This is set by the client constructor.
	 *
	 *  @since 0.9.0 */
	public int getInactivityTimeBeforeStaleCheck() {
		return connectionPool.getValidateAfterInactivity();
	}


	/** The amount of time in milliseconds for a request to timeout when wait for a free connection
	 *  from the pool.
	 *  <br><br>
	 *  This is set by the client constructor.
	 *
	 *  @since 0.9.0 */
	public int getConnectionPoolTimeout() {
		return requestConfig.getConnectionRequestTimeout();
	}


	/** The amount of time in milliseconds for a connection attempt to timeout.
	 *  <br><br>
	 *  This is set by the client constructor.
	 *
	 *  @since 0.9.0 */
	public int getConnectionTimeout() {
		return requestConfig.getConnectTimeout();
	}


	/** The amount of time in milliseconds for a server reply to timeout.
	 *  <br><br>
	 *  This is set by the client constructor.
	 *
	 *  @since 0.9.0 */
	public int getSocketTimeout() {
		return requestConfig.getSocketTimeout();
	}


	/** The maximum amount of connection retries automatically made by the HTTP client in
	 *  case of connection failure.
	 *  <br><br>
	 *  This is set by the client constructor.
	 *
	 *  @since 0.9.0 */
	public int getMaxConnectionRetries() {
		return requestRetryHandler.getRetryCount();
	}


	/** Returns the configuration of this client, except for the token.
	 *
	 *  @since 0.9.0 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "{"
			+ "\n  apiUrl = [" + getApiUrl() + "]"
			+ "\n  maxConnections = [" + getMaxConnections() + "]"
			+ "\n  connectionTimeout = [" + getConnectionTimeout() + "]"
			+ "\n  socketTimeout = [" + getSocketTimeout() + "]"
			+ "\n  connectionPoolTimeout = [" + getConnectionPoolTimeout() + "]"
			+ "\n  maxConnectionRetries = [" + getMaxConnectionRetries() + "]"
			+ "\n  inactivityTimeBeforeStaleCheck = [" + getInactivityTimeBeforeStaleCheck() + "]"
			+ "\n}";
	}
}
