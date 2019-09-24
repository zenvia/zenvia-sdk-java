package com.zenvia.api.sdk.client.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import com.zenvia.api.sdk.client.messages.MessageRequest;
import com.zenvia.api.sdk.client.messages.MessageResponse;
import com.zenvia.api.sdk.client.messages.TMessageDirection;
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


	@Before
	public void reset() {
		server.lastBody = null;
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
		Client client = new Client( "API_TOKEN", 11, 12, 13, 14 );
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
		Client client = new Client( "API_TOKEN", 11, 12, 13, 14, 15, 16 );
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
		Client client = new Client( "API_TOKEN", "https://zenvia.com", 11, 12, 13, 14 );
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
		Client client = new Client( "API_TOKEN", "https://zenvia.com", 11, 12, 13, 14, 15, 16 );
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
	public void unsuccessfulMessageRequest() {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		try {
			client.sendMessage( client.getChannel( "sms" ), messageRequest() );
			fail();
		} catch( UnsuccessfulRequestException exception ) {
			assertNotNull( server.lastBody );
			assertEquals( "from", server.lastBody.from );
			assertEquals( "to", server.lastBody.to );
			assertNotNull( server.lastBody.contents );
			assertEquals( 1, server.lastBody.contents.size() );
			assertEquals( ContentType.text, server.lastBody.contents.get( 0 ).type );
			assertEquals( "This is a test!", ( (TextContent) server.lastBody.contents.get( 0 ) ).text );

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
	public void wrongTokenMessageRequest() {
		Client client = new Client( "INCORRECT_TOKEN", "http://127.0.0.1:" + serverPort );
		try {
			client.sendMessage( client.getChannel( "whatsapp" ), messageRequest() );
			fail();
		} catch( UnsuccessfulRequestException exception ) {
			assertNotNull( server.lastBody );
			assertEquals( "from", server.lastBody.from );
			assertEquals( "to", server.lastBody.to );
			assertNotNull( server.lastBody.contents );
			assertEquals( 1, server.lastBody.contents.size() );
			assertEquals( ContentType.text, server.lastBody.contents.get( 0 ).type );
			assertEquals( "This is a test!", ( (TextContent) server.lastBody.contents.get( 0 ) ).text );

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
	public void successfulJsonRequest() throws Exception {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		MessageResponse messageResponse = client.sendMessage( client.getChannel( "whatsapp" ), messageRequest() );

		assertNotNull( server.lastBody );
		assertEquals( "from", server.lastBody.from );
		assertEquals( "to", server.lastBody.to );
		assertNotNull( server.lastBody.contents );
		assertEquals( 1, server.lastBody.contents.size() );
		assertEquals( ContentType.text, server.lastBody.contents.get( 0 ).type );
		assertEquals( "This is a test!", ( (TextContent) server.lastBody.contents.get( 0 ) ).text );

		assertNotNull( messageResponse );
		assertEquals( "12345", messageResponse.id );
		assertEquals( "123", messageResponse.from );
		assertEquals( "456", messageResponse.to );
		assertEquals( TMessageDirection.OUT, messageResponse.direction );
		assertEquals( ChannelType.whatsapp, messageResponse.channel );
		assertNotNull( messageResponse.contents );
		assertEquals( 1, messageResponse.contents.size() );
		assertEquals( ContentType.text, messageResponse.contents.get( 0 ).type );
		assertEquals( "This is a test!", ( (TextContent) messageResponse.contents.get( 0 ) ).text );

		client.close();
	}


	@Test
	public void emptyReply() {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort );
		try {
			client.sendMessage( client.getChannel( "facebook" ), messageRequest() );
			fail();
		} catch( UnsuccessfulRequestException exception ) {
			assertNotNull( server.lastBody );
			assertEquals( "from", server.lastBody.from );
			assertEquals( "to", server.lastBody.to );
			assertNotNull( server.lastBody.contents );
			assertEquals( 1, server.lastBody.contents.size() );
			assertEquals( ContentType.text, server.lastBody.contents.get( 0 ).type );
			assertEquals( "This is a test!", ( (TextContent) server.lastBody.contents.get( 0 ) ).text );

			assertNull( exception.getBody() );
			assertNull( exception.getCause() );
		} catch( Exception exception ) {
			fail();
		}
		client.close();
	}


	@Test
	public void nonJsonReply() {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort + "/invalid" );
		try {
			client.sendMessage( client.getChannel( "facebook" ), messageRequest() );
			fail();
		} catch( UnsuccessfulRequestException exception ) {
			assertNotNull( server.lastBody );
			assertEquals( "from", server.lastBody.from );
			assertEquals( "to", server.lastBody.to );
			assertNotNull( server.lastBody.contents );
			assertEquals( 1, server.lastBody.contents.size() );
			assertEquals( ContentType.text, server.lastBody.contents.get( 0 ).type );
			assertEquals( "This is a test!", ( (TextContent) server.lastBody.contents.get( 0 ) ).text );

			assertNull( exception.getBody() );
			assertEquals( "Error while extracting response for type [class com.zenvia.api.sdk.client.errors.ErrorResponse] and content type [application/json]; nested exception is org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Unrecognized token 'invalid': was expecting ('true', 'false' or 'null'); nested exception is com.fasterxml.jackson.core.JsonParseException: Unrecognized token 'invalid': was expecting ('true', 'false' or 'null')\n at [Source: (PushbackInputStream); line: 1, column: 15]", exception.getCause().getMessage() );
		} catch( Exception exception ) {
			fail();
		}
		client.close();
	}


	@Test
	public void connectionRefused() {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:8" );
		try {
			client.sendMessage( client.getChannel( "whatsapp" ), messageRequest() );
			fail();
		} catch( HttpConnectionFailException exception ) {
			assertNull( server.lastBody );
		} catch( Exception exception ) {
			fail();
		}
		client.close();
	}


	@Test
	public void connectionTimeout() {
		Client client = new Client( "API_TOKEN", "http://192.168.255.253:8", 1000, 1000, null );
		try {
			client.sendMessage( client.getChannel( "whatsapp" ), messageRequest() );
			fail();
		} catch( HttpConnectionTimeoutException exception ) {
			assertNull( server.lastBody );
		} catch( Exception exception ) {
			fail();
		}
		client.close();
	}


	@Test
	public void socketTimeout() {
		Client client = new Client( "API_TOKEN", "http://127.0.0.1:" + serverPort + "/timeout", 1000, 1000, null );
		try {
			client.sendMessage( client.getChannel( "whatsapp" ), messageRequest() );
			fail();
		} catch( HttpSocketTimeoutException exception ) {
			assertNotNull( server.lastBody );
			assertEquals( "from", server.lastBody.from );
			assertEquals( "to", server.lastBody.to );
			assertNotNull( server.lastBody.contents );
			assertEquals( 1, server.lastBody.contents.size() );
			assertEquals( ContentType.text, server.lastBody.contents.get( 0 ).type );
			assertEquals( "This is a test!", ( (TextContent) server.lastBody.contents.get( 0 ) ).text );
		} catch( Exception exception ) {
			fail();
		}
		client.close();
	}


	private MessageRequest messageRequest() {
		return new MessageRequest( "from", "to", new TextContent( "This is a test!" ) ); 
	}


	@SpringBootApplication
	@Path( "" )
	public static class TestServer extends ResourceConfig {
		private MessageRequest lastBody;
		
		
		public TestServer() {
			register( this );
		}


		@POST
		@Path( "/timeout/v1/channels/whatsapp/messages" )
		@Consumes( MediaType.APPLICATION_JSON )
		@Produces( MediaType.APPLICATION_JSON )
		public Response timeoutResource( MessageRequest messageRequest ) {
			this.lastBody = messageRequest;
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
			this.lastBody = messageRequest;
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
			this.lastBody = messageRequest;
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
			this.lastBody = messageRequest;
			return Response
				.status( 500 )
				.entity( "invalid" )
				.build();
		}


		@POST
		@Path( "/v1/channels/whatsapp/messages" )
		@Consumes( MediaType.APPLICATION_JSON )
		@Produces( MediaType.APPLICATION_JSON )
		public Response messageResource(
			MessageRequest messageRequest,
			@HeaderParam( "x-api-token" ) String token
		) {
			this.lastBody = messageRequest;
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
	}
}
