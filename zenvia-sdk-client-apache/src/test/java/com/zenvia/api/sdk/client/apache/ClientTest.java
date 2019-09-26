package com.zenvia.api.sdk.client.apache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.zenvia.api.sdk.client.AbstractClient;
import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.client.errors.ErrorResponse;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionFailException;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionTimeoutException;
import com.zenvia.api.sdk.client.exceptions.HttpSocketTimeoutException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.messages.MessageDirection;
import com.zenvia.api.sdk.client.messages.MessageRequest;
import com.zenvia.api.sdk.client.messages.MessageResponse;
import com.zenvia.api.sdk.client.subscriptions.Criteria;
import com.zenvia.api.sdk.client.subscriptions.EventType;
import com.zenvia.api.sdk.client.subscriptions.MessageCriteria;
import com.zenvia.api.sdk.client.subscriptions.MessageStatusSubscription;
import com.zenvia.api.sdk.client.subscriptions.MessageSubscription;
import com.zenvia.api.sdk.client.subscriptions.PartialSubscription;
import com.zenvia.api.sdk.client.subscriptions.Subscription;
import com.zenvia.api.sdk.client.subscriptions.SubscriptionStatus;
import com.zenvia.api.sdk.client.subscriptions.Webhook;
import com.zenvia.api.sdk.contents.ContentType;
import com.zenvia.api.sdk.contents.TextContent;


@RunWith( SpringRunner.class )
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT, properties = "spring.main.banner-mode=off" )
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class ClientTest {
	@LocalServerPort
	private Integer serverPort;

	@Inject
	private TestServer server;
	
	private final Field apiTokenField;


	public ClientTest() throws Exception {
		apiTokenField = AbstractClient.class.getDeclaredField( "apiToken" );
		apiTokenField.setAccessible( true );
	}


	@Test
	public void constructor1() throws Exception {
		Client client = new Client( "API_TOKEN" );
		assertEquals( "https://api.zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 25000, client.getConnectionTimeout() );
		assertEquals( 60000, client.getSocketTimeout() );
		assertEquals( 4, client.getMaxAutoRetries() );
		assertEquals( 100, client.getMaxTotalConnections() );
		assertEquals( 100, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructor2() throws Exception {
		Client client = new Client( "API_TOKEN", 10 );
		assertEquals( "https://api.zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 25000, client.getConnectionTimeout() );
		assertEquals( 60000, client.getSocketTimeout() );
		assertEquals( 4, client.getMaxAutoRetries() );
		assertEquals( 10, client.getMaxTotalConnections() );
		assertEquals( 10, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructor4() throws Exception {
		Client client = new Client( "API_TOKEN", 11, 12, 13 );
		assertEquals( "https://api.zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 11, client.getConnectionTimeout() );
		assertEquals( 12, client.getSocketTimeout() );
		assertEquals( 13, client.getMaxAutoRetries() );
		assertEquals( 100, client.getMaxTotalConnections() );
		assertEquals( 100, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructor5() throws Exception {
		Client client = new Client( "API_TOKEN", 14, 11, 12, 13 );
		assertEquals( "https://api.zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 11, client.getConnectionTimeout() );
		assertEquals( 12, client.getSocketTimeout() );
		assertEquals( 13, client.getMaxAutoRetries() );
		assertEquals( 14, client.getMaxTotalConnections() );
		assertEquals( 14, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructor7() throws Exception {
		Client client = new Client( "API_TOKEN", 14, 11, 12, 13, 15, 16 );
		assertEquals( "https://api.zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 11, client.getConnectionTimeout() );
		assertEquals( 12, client.getSocketTimeout() );
		assertEquals( 13, client.getMaxAutoRetries() );
		assertEquals( 14, client.getMaxTotalConnections() );
		assertEquals( 14, client.getMaxPerHostConnections() );
		assertEquals( 15, client.getConnectionPoolTimeout() );
		assertEquals( 16, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructorWithUrl1() throws Exception {
		Client client = new Client( "API_TOKEN", "https://zenvia.com" );
		assertEquals( "https://zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 25000, client.getConnectionTimeout() );
		assertEquals( 60000, client.getSocketTimeout() );
		assertEquals( 4, client.getMaxAutoRetries() );
		assertEquals( 100, client.getMaxTotalConnections() );
		assertEquals( 100, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructorWithUrl2() throws Exception {
		Client client = new Client( "API_TOKEN", "https://zenvia.com", 10 );
		assertEquals( "https://zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 25000, client.getConnectionTimeout() );
		assertEquals( 60000, client.getSocketTimeout() );
		assertEquals( 4, client.getMaxAutoRetries() );
		assertEquals( 10, client.getMaxTotalConnections() );
		assertEquals( 10, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructorWithUrl4() throws Exception {
		Client client = new Client( "API_TOKEN", "https://zenvia.com", 11, 12, 13 );
		assertEquals( "https://zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 11, client.getConnectionTimeout() );
		assertEquals( 12, client.getSocketTimeout() );
		assertEquals( 13, client.getMaxAutoRetries() );
		assertEquals( 100, client.getMaxTotalConnections() );
		assertEquals( 100, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructorWithUrl5() throws Exception {
		Client client = new Client( "API_TOKEN", "https://zenvia.com", 14, 11, 12, 13 );
		assertEquals( "https://zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 11, client.getConnectionTimeout() );
		assertEquals( 12, client.getSocketTimeout() );
		assertEquals( 13, client.getMaxAutoRetries() );
		assertEquals( 14, client.getMaxTotalConnections() );
		assertEquals( 14, client.getMaxPerHostConnections() );
		assertEquals( 0, client.getConnectionPoolTimeout() );
		assertEquals( 5000, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void constructorWithUrl7() throws Exception {
		Client client = new Client( "API_TOKEN", "https://zenvia.com", 14, 11, 12, 13, 15, 16 );
		assertEquals( "https://zenvia.com", client.getApiUrl() );
		assertEquals( "API_TOKEN", apiTokenField.get( client ) );
		assertEquals( 11, client.getConnectionTimeout() );
		assertEquals( 12, client.getSocketTimeout() );
		assertEquals( 13, client.getMaxAutoRetries() );
		assertEquals( 14, client.getMaxTotalConnections() );
		assertEquals( 14, client.getMaxPerHostConnections() );
		assertEquals( 15, client.getConnectionPoolTimeout() );
		assertEquals( 16, client.getCheckStaleConnectionAfterInactivityTime() );
		client.close();
	}


	@Test
	public void messageRequestUnsuccessful() {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		try {
			client.getChannel( "sms" ).sendMessage( messageRequest() );
			fail();
		} catch( UnsuccessfulRequestException exception ) {
			assertNotNull( server.lastMessagePost );
			assertEquals( "from", server.lastMessagePost.from );
			assertEquals( "to", server.lastMessagePost.to );
			assertNotNull( server.lastMessagePost.contents );
			assertEquals( 1, server.lastMessagePost.contents.size() );
			assertEquals( ContentType.text, server.lastMessagePost.contents.get( 0 ).type );
			assertEquals( "This is a test!", ( (TextContent) server.lastMessagePost.contents.get( 0 ) ).text );

			ErrorResponse errorResponse = exception.getBody();
			assertNotNull( errorResponse );
			assertEquals( "TEST", errorResponse.code );
			assertEquals( "This is a test!", errorResponse.message );
			assertNotNull( errorResponse.details );
			assertEquals( 1, errorResponse.details.size() );
			assertEquals( "INVALID", errorResponse.details.get( 0 ).code );
			assertEquals( "id", errorResponse.details.get( 0 ).path );
			assertEquals( "Invalid id!", errorResponse.details.get( 0 ).message );
		} catch( Exception exception ) {
			fail();
		}
		client.close();
	}


	@Test
	public void messageRequestWithWrongToken() {
		Client client = new Client( "INCORRECT_TOKEN", "http://127.0.0.1:" + serverPort );
		try {
			client.getChannel( "whatsapp" ).sendMessage( messageRequest() );
			fail();
		} catch( UnsuccessfulRequestException exception ) {
			assertNotNull( server.lastMessagePost );
			assertEquals( "from", server.lastMessagePost.from );
			assertEquals( "to", server.lastMessagePost.to );
			assertNotNull( server.lastMessagePost.contents );
			assertEquals( 1, server.lastMessagePost.contents.size() );
			assertEquals( ContentType.text, server.lastMessagePost.contents.get( 0 ).type );
			assertEquals( "This is a test!", ( (TextContent) server.lastMessagePost.contents.get( 0 ) ).text );

			ErrorResponse errorResponse = exception.getBody();
			assertNotNull( errorResponse );
			assertEquals( "AUTHENTICATION_ERROR", errorResponse.code );
			assertEquals( "No authorization token was found", errorResponse.message );
			assertNotNull( errorResponse.details );
			assertEquals( 0, errorResponse.details.size() );
		} catch( Exception exception ) {
			fail();
		}
		client.close();
	}


	@Test
	public void messageRequestSuccessful() throws Exception {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		MessageResponse messageResponse = client.getChannel( "whatsapp" ).sendMessage( messageRequest() );

		assertNotNull( server.lastMessagePost );
		assertEquals( "from", server.lastMessagePost.from );
		assertEquals( "to", server.lastMessagePost.to );
		assertNotNull( server.lastMessagePost.contents );
		assertEquals( 1, server.lastMessagePost.contents.size() );
		assertEquals( ContentType.text, server.lastMessagePost.contents.get( 0 ).type );
		assertEquals( "This is a test!", ( (TextContent) server.lastMessagePost.contents.get( 0 ) ).text );

		assertNotNull( messageResponse );
		assertEquals( "12345", messageResponse.id );
		assertEquals( "123", messageResponse.from );
		assertEquals( "456", messageResponse.to );
		assertEquals( MessageDirection.OUT, messageResponse.direction );
		assertEquals( ChannelType.whatsapp, messageResponse.channel );
		assertNotNull( messageResponse.contents );
		assertEquals( 1, messageResponse.contents.size() );
		assertEquals( ContentType.text, messageResponse.contents.get( 0 ).type );
		assertEquals( "This is a test!", ( (TextContent) messageResponse.contents.get( 0 ) ).text );

		client.close();
	}


	@Test
	public void messageRequestWithEmptyReply() {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		try {
			client.getChannel( "facebook" ).sendMessage( messageRequest() );
			fail();
		} catch( UnsuccessfulRequestException exception ) {
			assertNotNull( server.lastMessagePost );
			assertEquals( "from", server.lastMessagePost.from );
			assertEquals( "to", server.lastMessagePost.to );
			assertNotNull( server.lastMessagePost.contents );
			assertEquals( 1, server.lastMessagePost.contents.size() );
			assertEquals( ContentType.text, server.lastMessagePost.contents.get( 0 ).type );
			assertEquals( "This is a test!", ( (TextContent) server.lastMessagePost.contents.get( 0 ) ).text );

			assertNull( exception.getBody() );
			assertNull( exception.getCause() );
		} catch( Exception exception ) {
			fail();
		}
		client.close();
	}


	@Test
	public void messageRequestWithNonJsonReply() {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort + "/invalid" );
		try {
			client.getChannel( "facebook" ).sendMessage( messageRequest() );
			fail();
		} catch( UnsuccessfulRequestException exception ) {
			assertNotNull( server.lastMessagePost );
			assertEquals( "from", server.lastMessagePost.from );
			assertEquals( "to", server.lastMessagePost.to );
			assertNotNull( server.lastMessagePost.contents );
			assertEquals( 1, server.lastMessagePost.contents.size() );
			assertEquals( ContentType.text, server.lastMessagePost.contents.get( 0 ).type );
			assertEquals( "This is a test!", ( (TextContent) server.lastMessagePost.contents.get( 0 ) ).text );

			assertNull( exception.getBody() );
			assertEquals( "Unrecognized token 'invalid': was expecting ('true', 'false' or 'null')\n at [Source: (byte[])\"invalid\"; line: 1, column: 15]", exception.getCause().getMessage() );
		} catch( Exception exception ) {
			fail();
		}
		client.close();
	}


	@Test
	public void messageRequestWithConnectionRefused() {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:8" );
		try {
			client.getChannel( "whatsapp" ).sendMessage( messageRequest() );
			fail();
		} catch( HttpConnectionFailException exception ) {
			assertNull( server.lastMessagePost );
		} catch( Exception exception ) {
			fail();
		}
		client.close();
	}


	@Test
	public void messageRequestWithConnectionTimeout() {
		Client client = new Client( "API_TOKEN", "http://192.168.255.253:8", 1000, 1000, null );
		try {
			client.getChannel( "whatsapp" ).sendMessage( messageRequest() );
			fail();
		} catch( HttpConnectionTimeoutException exception ) {
			assertNull( server.lastMessagePost );
		} catch( Exception exception ) {
			fail();
		}
		client.close();
	}


	@Test
	public void messageRequestWithSocketTimeout() {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort + "/timeout", 1000, 1000, null );
		try {
			client.getChannel( "whatsapp" ).sendMessage( messageRequest() );
			fail();
		} catch( HttpSocketTimeoutException exception ) {
			assertNotNull( server.lastMessagePost );
			assertEquals( "from", server.lastMessagePost.from );
			assertEquals( "to", server.lastMessagePost.to );
			assertNotNull( server.lastMessagePost.contents );
			assertEquals( 1, server.lastMessagePost.contents.size() );
			assertEquals( ContentType.text, server.lastMessagePost.contents.get( 0 ).type );
			assertEquals( "This is a test!", ( (TextContent) server.lastMessagePost.contents.get( 0 ) ).text );
		} catch( Exception exception ) {
			fail();
		}
		client.close();
	}


	@Test
	public void messageSubscriptionSuccessfulCreation() throws Exception {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		MessageSubscription subscription = client.createSubscription( messageSubscription() );

		assertNotNull( server.lastMessageSubscriptionPost );
		assertEquals( EventType.MESSAGE, server.lastMessageSubscriptionPost.eventType );
		assertEquals( "123", server.lastMessageSubscriptionPost.id );
		assertNotNull( server.lastMessageSubscriptionPost.criteria );
		assertEquals( ChannelType.whatsapp, server.lastMessageSubscriptionPost.criteria.channel );
		assertEquals( MessageDirection.IN, server.lastMessageSubscriptionPost.criteria.direction );
		assertNotNull( server.lastMessageSubscriptionPost.webhook );
		assertEquals( "http://localhost", server.lastMessageSubscriptionPost.webhook.url );
		assertNotNull( server.lastMessageSubscriptionPost.webhook.headers );
		assertEquals( 1, server.lastMessageSubscriptionPost.webhook.headers.size() );
		assertEquals( "value", server.lastMessageSubscriptionPost.webhook.headers.get( "name" ) );

		assertNotNull( subscription );
		assertEquals( EventType.MESSAGE, subscription.eventType );
		assertEquals( MessageSubscription.class, subscription.getClass() );
		assertEquals( "123", subscription.id );
		assertNotNull( subscription.criteria );
		assertEquals( ChannelType.whatsapp, subscription.criteria.channel );
		assertEquals( MessageDirection.IN, subscription.criteria.direction );
		assertNotNull( subscription.webhook );
		assertEquals( "http://localhost", subscription.webhook.url );
		assertNotNull( subscription.webhook.headers );
		assertEquals( 1, subscription.webhook.headers.size() );
		assertEquals( "value", subscription.webhook.headers.get( "name" ) );

		client.close();
	}


	@Test
	public void messageStatusSubscriptionSuccessfulCreation() throws Exception {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		MessageStatusSubscription subscription = client.createSubscription( messageStatusSubscription() );

		assertNotNull( server.lastMessageStatusSubscriptionPost );
		assertEquals( EventType.MESSAGE_STATUS, server.lastMessageStatusSubscriptionPost.eventType );
		assertEquals( "456", server.lastMessageStatusSubscriptionPost.id );
		assertNotNull( server.lastMessageStatusSubscriptionPost.criteria );
		assertEquals( ChannelType.whatsapp, server.lastMessageStatusSubscriptionPost.criteria.channel );
		assertNotNull( server.lastMessageStatusSubscriptionPost.webhook );
		assertEquals( "http://localhost", server.lastMessageStatusSubscriptionPost.webhook.url );
		assertNotNull( server.lastMessageStatusSubscriptionPost.webhook.headers );
		assertEquals( 1, server.lastMessageStatusSubscriptionPost.webhook.headers.size() );
		assertEquals( "value", server.lastMessageStatusSubscriptionPost.webhook.headers.get( "name" ) );

		assertNotNull( subscription );
		assertEquals( EventType.MESSAGE_STATUS, subscription.eventType );
		assertEquals( "456", subscription.id );
		assertNotNull( subscription.criteria );
		assertEquals( ChannelType.whatsapp, subscription.criteria.channel );
		assertNotNull( subscription.webhook );
		assertEquals( "http://localhost", subscription.webhook.url );
		assertNotNull( subscription.webhook.headers );
		assertEquals( 1, subscription.webhook.headers.size() );
		assertEquals( "value", subscription.webhook.headers.get( "name" ) );

		client.close();
	}


	@Test
	public void messageSubscriptionSuccessfulUpdate() throws Exception {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		MessageSubscription input = messageSubscription()
			.apply( new Webhook( "https://127.0.0.1", null ), SubscriptionStatus.INACTIVE );
		MessageSubscription output = client.updateSubscription( input );

		assertNotNull( server.lastMessageSubscriptionPatch );
		assertEquals( EventType.MESSAGE, server.lastMessageSubscriptionPatch.eventType );
		assertEquals( "123", server.lastMessageSubscriptionPatch.id );
		assertNotNull( server.lastMessageSubscriptionPatch.criteria );
		assertEquals( ChannelType.whatsapp, server.lastMessageSubscriptionPatch.criteria.channel );
		assertEquals( MessageDirection.IN, server.lastMessageSubscriptionPatch.criteria.direction );
		assertNotNull( server.lastMessageSubscriptionPatch.webhook );
		assertEquals( "https://127.0.0.1", server.lastMessageSubscriptionPatch.webhook.url );
		assertNotNull( server.lastMessageSubscriptionPatch.webhook.headers );
		assertEquals( 0, server.lastMessageSubscriptionPatch.webhook.headers.size() );

		assertNotNull( output );
		assertEquals( EventType.MESSAGE, output.eventType );
		assertEquals( MessageSubscription.class, output.getClass() );
		assertEquals( "123", output.id );
		assertNotNull( output.criteria );
		assertEquals( ChannelType.whatsapp, output.criteria.channel );
		assertEquals( MessageDirection.IN, output.criteria.direction );
		assertNotNull( output.webhook );
		assertEquals( "https://127.0.0.1", output.webhook.url );
		assertNotNull( output.webhook.headers );
		assertEquals( 0, output.webhook.headers.size() );

		client.close();
	}


	@Test
	public void messageStatusSubscriptionSuccessfulUpdate() throws Exception {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		MessageStatusSubscription input = messageStatusSubscription()
			.apply( new Webhook( "https://127.0.0.1", null ), SubscriptionStatus.INACTIVE );
		MessageStatusSubscription output = client.updateSubscription( input );

		assertNotSame( input, output );

		assertNotNull( server.lastMessageStatusSubscriptionPatch );
		assertEquals( EventType.MESSAGE_STATUS, server.lastMessageStatusSubscriptionPatch.eventType );
		assertEquals( "456", server.lastMessageStatusSubscriptionPatch.id );
		assertNotNull( server.lastMessageStatusSubscriptionPatch.criteria );
		assertEquals( ChannelType.whatsapp, server.lastMessageStatusSubscriptionPatch.criteria.channel );
		assertNotNull( server.lastMessageStatusSubscriptionPatch.webhook );
		assertEquals( "https://127.0.0.1", server.lastMessageStatusSubscriptionPatch.webhook.url );
		assertNotNull( server.lastMessageStatusSubscriptionPatch.webhook.headers );
		assertEquals( 0, server.lastMessageStatusSubscriptionPatch.webhook.headers.size() );

		assertNotNull( output );
		assertEquals( EventType.MESSAGE_STATUS, output.eventType );
		assertEquals( "456", output.id );
		assertNotNull( output.criteria );
		assertEquals( ChannelType.whatsapp, output.criteria.channel );
		assertNotNull( output.webhook );
		assertEquals( "https://127.0.0.1", output.webhook.url );
		assertNotNull( output.webhook.headers );
		assertEquals( 0, output.webhook.headers.size() );

		client.close();
	}


	@Test
	public void messageSubscriptionSuccessfulGet() throws Exception {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		MessageSubscription subscription = client.getSubscription( "123" );

		assertNotNull( subscription );
		assertEquals( EventType.MESSAGE, subscription.eventType );
		assertEquals( MessageSubscription.class, subscription.getClass() );
		assertEquals( "123", subscription.id );
		assertNotNull( subscription.criteria );
		assertEquals( ChannelType.whatsapp, subscription.criteria.channel );
		assertEquals( MessageDirection.IN, subscription.criteria.direction );
		assertNotNull( subscription.webhook );
		assertEquals( "http://localhost", subscription.webhook.url );
		assertNotNull( subscription.webhook.headers );
		assertEquals( 1, subscription.webhook.headers.size() );
		assertEquals( "value", subscription.webhook.headers.get( "name" ) );

		client.close();
	}


	@Test
	public void messageStatusSubscriptionSuccessfulGet() throws Exception {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		MessageStatusSubscription subscription = client.getSubscription( "456" );

		assertNotNull( subscription );
		assertEquals( EventType.MESSAGE_STATUS, subscription.eventType );
		assertEquals( "456", subscription.id );
		assertNotNull( subscription.criteria );
		assertEquals( ChannelType.whatsapp, subscription.criteria.channel );
		assertNotNull( subscription.webhook );
		assertEquals( "http://localhost", subscription.webhook.url );
		assertNotNull( subscription.webhook.headers );
		assertEquals( 1, subscription.webhook.headers.size() );
		assertEquals( "value", subscription.webhook.headers.get( "name" ) );

		client.close();
	}


	@Test
	public void subscriptionSuccessfulDelete() throws Exception {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		client.deleteSubscription( "123" );
		assertEquals( "123", server.lastSubscriptionDelete );
		client.close();
	}


	@Test
	public void subscriptionSuccessfulList() throws Exception {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		List<Subscription> subscriptions = client.listSubscriptions();

		assertNotNull( subscriptions );
		assertEquals( 2, subscriptions.size() );
		
		assertNotNull( subscriptions.get( 0 ) );
		assertEquals( EventType.MESSAGE, subscriptions.get( 0 ).eventType );
		assertEquals( MessageSubscription.class, subscriptions.get( 0 ).getClass() );
		assertEquals( "123", subscriptions.get( 0 ).id );
		assertNotNull( subscriptions.get( 0 ).criteria );
		assertEquals( ChannelType.whatsapp, subscriptions.get( 0 ).criteria.channel );
		assertEquals(
			MessageDirection.IN,
			( (MessageSubscription) subscriptions.get( 0 ) ).criteria.direction
		);
		assertNotNull( subscriptions.get( 0 ).webhook );
		assertEquals( "http://localhost", subscriptions.get( 0 ).webhook.url );
		assertNotNull( subscriptions.get( 0 ).webhook.headers );
		assertEquals( 1, subscriptions.get( 0 ).webhook.headers.size() );
		assertEquals( "value", subscriptions.get( 0 ).webhook.headers.get( "name" ) );
		
		assertNotNull( subscriptions.get( 1 ) );
		assertEquals( EventType.MESSAGE_STATUS, subscriptions.get( 1 ).eventType );
		assertEquals( "456", subscriptions.get( 1 ).id );
		assertNotNull( subscriptions.get( 1 ).criteria );
		assertEquals( ChannelType.whatsapp, subscriptions.get( 1 ).criteria.channel );
		assertNotNull( subscriptions.get( 1 ).webhook );
		assertEquals( "http://localhost", subscriptions.get( 1 ).webhook.url );
		assertNotNull( subscriptions.get( 1 ).webhook.headers );
		assertEquals( 1, subscriptions.get( 1 ).webhook.headers.size() );
		assertEquals( "value", subscriptions.get( 1 ).webhook.headers.get( "name" ) );

		client.close();
	}


	@Test
	public void subscriptionSuccessfulSingleValueList() throws Exception {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort + "/single" );
		List<Subscription> subscriptions = client.listSubscriptions();

		assertNotNull( subscriptions );
		assertEquals( 1, subscriptions.size() );
		
		assertNotNull( subscriptions.get( 0 ) );
		assertEquals( EventType.MESSAGE, subscriptions.get( 0 ).eventType );
		assertEquals( MessageSubscription.class, subscriptions.get( 0 ).getClass() );
		assertEquals( "123", subscriptions.get( 0 ).id );
		assertNotNull( subscriptions.get( 0 ).criteria );
		assertEquals( ChannelType.whatsapp, subscriptions.get( 0 ).criteria.channel );
		assertEquals(
			MessageDirection.IN,
			( (MessageSubscription) subscriptions.get( 0 ) ).criteria.direction
		);
		assertNotNull( subscriptions.get( 0 ).webhook );
		assertEquals( "http://localhost", subscriptions.get( 0 ).webhook.url );
		assertNotNull( subscriptions.get( 0 ).webhook.headers );
		assertEquals( 1, subscriptions.get( 0 ).webhook.headers.size() );
		assertEquals( "value", subscriptions.get( 0 ).webhook.headers.get( "name" ) );

		client.close();
	}


	@Test
	public void subscriptionSuccessfulEmptyList() throws Exception {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort + "/empty" );
		List<Subscription> subscriptions = client.listSubscriptions();

		assertNotNull( subscriptions );
		assertEquals( 0, subscriptions.size() );

		client.close();
	}


	@Test
	public void messageStatusSubscriptionNotFoundGet() throws Exception {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		try {
			client.getSubscription( "789" );
			fail();
		} catch( UnsuccessfulRequestException exception ) {
			assertEquals( "NOT_FOUND", exception.getBody().code );
			assertEquals( "Subscription with id [789] not found", exception.getBody().message );
			assertNotNull( exception.getBody().details );
			assertEquals( 0, exception.getBody().details.size() );
		}
		client.close();
	}


	private MessageRequest messageRequest() {
		return new MessageRequest( "from", "to", new TextContent( "This is a test!" ) ); 
	}


	private static MessageSubscription messageSubscription() {
		Map<String,String> headers = new HashMap<>();
		headers.put( "name", "value" );
		return new MessageSubscription(
			"123",
			new Webhook( "http://localhost", headers ),
			new MessageCriteria( ChannelType.whatsapp, MessageDirection.IN ),
			SubscriptionStatus.ACTIVE,
			ZonedDateTime.of( 2019, 9, 24, 21, 1, 30, 500000000, ZoneId.of( "America/Sao_Paulo" ) ),
			ZonedDateTime.of( 2019, 9, 24, 21, 8, 0, 100000000, ZoneId.of( "America/Sao_Paulo" ) )
		);
	}


	private static MessageStatusSubscription messageStatusSubscription() {
		Map<String,String> headers = new HashMap<>();
		headers.put( "name", "value" );
		return new MessageStatusSubscription(
			"456",
			new Webhook( "http://localhost", headers ),
			new Criteria( ChannelType.whatsapp ),
			SubscriptionStatus.ACTIVE,
			ZonedDateTime.of( 2019, 9, 24, 21, 1, 30, 500000000, ZoneId.of( "America/Sao_Paulo" ) ),
			ZonedDateTime.of( 2019, 9, 24, 21, 8, 0, 100000000, ZoneId.of( "America/Sao_Paulo" ) )
		);
	}


	@Before
	public void reset() {
		server.lastMessagePost = null;
		server.lastMessageSubscriptionPost = null;
		server.lastMessageSubscriptionPatch = null;
		server.lastMessageStatusSubscriptionPost = null;
		server.lastMessageStatusSubscriptionPatch = null;
		server.lastSubscriptionDelete = null;
	}


	@SpringBootApplication
	@Path( "" )
	public static class TestServer extends ResourceConfig {
		private MessageRequest lastMessagePost;

		private MessageSubscription lastMessageSubscriptionPost;

		private MessageSubscription lastMessageSubscriptionPatch;

		private MessageStatusSubscription lastMessageStatusSubscriptionPost;

		private MessageStatusSubscription lastMessageStatusSubscriptionPatch;

		private String lastSubscriptionDelete;


		public TestServer() {
			register( this );
		}


		@POST
		@Path( "/v1/subscriptions" )
		@Consumes( MediaType.APPLICATION_JSON )
		@Produces( MediaType.APPLICATION_JSON )
		public Response createMessageSubscriptionResource(
			Subscription subscription,
			@HeaderParam( "x-api-token" ) String token
		) {
			if ( subscription instanceof MessageSubscription ) {
				this.lastMessageSubscriptionPost = (MessageSubscription) subscription;
			}
			if ( subscription instanceof MessageStatusSubscription ) {
				this.lastMessageStatusSubscriptionPost = (MessageStatusSubscription) subscription;
			}
			if( token != null && token.equals( "API_TOKEN" ) ) {
				return Response
					.ok( subscription )
					.build();
			} else {
				return Response
					.status( 401 )
					.entity( "{\"code\":\"AUTHENTICATION_ERROR\",\"message\":\"No authorization token was found\"}" )
					.build();
			}
		}


		@PATCH
		@Path( "/v1/subscriptions/{id}" )
		@Consumes( MediaType.APPLICATION_JSON )
		@Produces( MediaType.APPLICATION_JSON )
		public Response updateMessageSubscriptionResource(
			PartialSubscription partialSubscription,
			@HeaderParam( "x-api-token" ) String token,
			@PathParam( "id" ) String id
		) {
			if( token == null || !token.equals( "API_TOKEN" ) ) {
				return Response
					.status( 401 )
					.entity( "{\"code\":\"AUTHENTICATION_ERROR\",\"message\":\"No authorization token was found\"}" )
					.build();
			}
			if ( id.equals( "123" ) ) {
				MessageSubscription subscription = messageSubscription().apply( partialSubscription );
				this.lastMessageSubscriptionPatch = subscription;
				return Response
					.ok( subscription )
					.build();
			}
			if ( id.equals( "456" ) ) {
				MessageStatusSubscription subscription = messageStatusSubscription().apply( partialSubscription );
				this.lastMessageStatusSubscriptionPatch = subscription;
				return Response
					.ok( subscription )
					.build();
			}
			return Response
				.status( 404 )
				.entity( new ErrorResponse( "NOT_FOUND", "Subscription with id [" +id + "] not found" ) )
				.build();
		}


		@DELETE
		@Path( "/v1/subscriptions/{id}" )
		@Produces( MediaType.APPLICATION_JSON )
		public Response deleteMessageSubscriptionResource(
			PartialSubscription partialSubscription,
			@HeaderParam( "x-api-token" ) String token,
			@PathParam( "id" ) String id
		) {
			if( token == null || !token.equals( "API_TOKEN" ) ) {
				return Response
					.status( 401 )
					.entity( "{\"code\":\"AUTHENTICATION_ERROR\",\"message\":\"No authorization token was found\"}" )
					.build();
			}
			if ( id.equals( "123" ) || id.equals( "456" ) ) {
				this.lastSubscriptionDelete = id;
				return Response.status( 204 ).build();
			}
			return Response
				.status( 404 )
				.entity( new ErrorResponse( "NOT_FOUND", "Subscription with id [" +id + "] not found" ) )
				.build();
		}


		@GET
		@Path( "/v1/subscriptions/{id}" )
		@Produces( MediaType.APPLICATION_JSON )
		public Response getSubscriptionResource(
			@HeaderParam( "x-api-token" ) String token,
			@PathParam( "id" ) String id
		) {
			if( token == null || !token.equals( "API_TOKEN" ) ) {
				return Response
					.status( 401 )
					.entity( "{\"code\":\"AUTHENTICATION_ERROR\",\"message\":\"No authorization token was found\"}" )
					.build();
			}
			if ( id.equals( "123" ) ) {
				return Response.ok( messageSubscription() ).build();
			}
			if ( id.equals( "456" ) ) {
				return Response.ok( messageStatusSubscription() ).build();
			}
			return Response
				.status( 404 )
				.entity( new ErrorResponse( "NOT_FOUND", "Subscription with id [" +id + "] not found" ) )
				.build();
		}


		@GET
		@Path( "/v1/subscriptions" )
		@Produces( MediaType.APPLICATION_JSON )
		public Response listSubscriptionsResource( @HeaderParam( "x-api-token" ) String token ) {
			if( token != null && token.equals( "API_TOKEN" ) ) {
				return Response
					.ok( new Subscription[] { messageSubscription(), messageStatusSubscription() } )
					.build();
			} else {
				return Response
					.status( 401 )
					.entity( "{\"code\":\"AUTHENTICATION_ERROR\",\"message\":\"No authorization token was found\"}" )
					.build();
			}
		}


		@GET
		@Path( "/single/v1/subscriptions" )
		@Produces( MediaType.APPLICATION_JSON )
		public Response singleValueListSubscriptionsResource( @HeaderParam( "x-api-token" ) String token ) {
			if( token != null && token.equals( "API_TOKEN" ) ) {
				return Response
					.ok( new Subscription[] { messageSubscription() } )
					.build();
			} else {
				return Response
					.status( 401 )
					.entity( "{\"code\":\"AUTHENTICATION_ERROR\",\"message\":\"No authorization token was found\"}" )
					.build();
			}
		}


		@GET
		@Path( "/empty/v1/subscriptions" )
		@Produces( MediaType.APPLICATION_JSON )
		public Response emptyListSubscriptionsResource( @HeaderParam( "x-api-token" ) String token ) {
			if( token != null && token.equals( "API_TOKEN" ) ) {
				return Response
					.ok( new Subscription[] {} )
					.build();
			} else {
				return Response
					.status( 401 )
					.entity( "{\"code\":\"AUTHENTICATION_ERROR\",\"message\":\"No authorization token was found\"}" )
					.build();
			}
		}


		@POST
		@Path( "/v1/channels/whatsapp/messages" )
		@Consumes( MediaType.APPLICATION_JSON )
		@Produces( MediaType.APPLICATION_JSON )
		public Response messageResource(
			MessageRequest messageRequest,
			@HeaderParam( "x-api-token" ) String token
		) {
			this.lastMessagePost = messageRequest;
			if( token != null && token.equals( "API_TOKEN" ) ) {
				return Response
					.ok( "{\"id\":\"12345\",\"from\":\"123\",\"to\":\"456\",\"direction\":\"OUT\",\"channel\":\"whatsapp\",\"contents\":[{\"type\":\"text\",\"text\":\"This is a test!\"}]}" )
					.build();
			} else {
				return Response
					.status( 401 )
					.entity( "{\"code\":\"AUTHENTICATION_ERROR\",\"message\":\"No authorization token was found\"}" )
					.build();
			}
		}


		@POST
		@Path( "/timeout/v1/channels/whatsapp/messages" )
		@Consumes( MediaType.APPLICATION_JSON )
		@Produces( MediaType.APPLICATION_JSON )
		public Response timeoutResource( MessageRequest messageRequest ) {
			this.lastMessagePost = messageRequest;
			try {
				Thread.sleep( 5000 );
			}
			catch( InterruptedException exception ) {
				throw new IllegalStateException( exception );
			}
			return null;
		}


		@POST
		@Path( "/v1/channels/sms/messages" )
		@Consumes( MediaType.APPLICATION_JSON )
		@Produces( MediaType.APPLICATION_JSON )
		public Response errorResource( MessageRequest messageRequest ) {
			this.lastMessagePost = messageRequest;
			return Response
				.status( 500 )
				.entity( "{\"code\":\"TEST\",\"message\":\"This is a test!\",\"details\":[{\"code\":\"INVALID\",\"path\":\"id\",\"message\":\"Invalid id!\"}]}" )
				.build();
		}


		@POST
		@Path( "/v1/channels/facebook/messages" )
		@Consumes( MediaType.APPLICATION_JSON )
		@Produces( MediaType.APPLICATION_JSON )
		public Response emptyResource( MessageRequest messageRequest ) {
			this.lastMessagePost = messageRequest;
			return Response
				.status( 500 )
				.entity( "" )
				.build();
		}


		@POST
		@Path( "/invalid/v1/channels/facebook/messages" )
		@Consumes( MediaType.APPLICATION_JSON )
		@Produces( MediaType.APPLICATION_JSON )
		public Response invalidResource( MessageRequest messageRequest ) {
			this.lastMessagePost = messageRequest;
			return Response
				.status( 500 )
				.entity( "invalid" )
				.build();
		}
	}
}
