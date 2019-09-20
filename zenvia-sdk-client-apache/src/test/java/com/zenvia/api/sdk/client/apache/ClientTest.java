package com.zenvia.api.sdk.client.apache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
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

import com.zenvia.api.sdk.client.ChannelType;
import com.zenvia.api.sdk.client.errors.ErrorResponse;
import com.zenvia.api.sdk.client.exceptions.UnexpectedResponseBodyException;
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


	@Before
	public void reset() {
		server.lastBody = null;
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
		} catch( UnexpectedResponseBodyException exception ) {
			assertNotNull( server.lastBody );
			assertEquals( "from", server.lastBody.from );
			assertEquals( "to", server.lastBody.to );
			assertNotNull( server.lastBody.contents );
			assertEquals( 1, server.lastBody.contents.size() );
			assertEquals( ContentType.text, server.lastBody.contents.get( 0 ).type );
			assertEquals( "This is a test!", ( (TextContent) server.lastBody.contents.get( 0 ) ).text );

			assertEquals( "", exception.getBody() );
		} catch( Exception exception ) {
			exception.printStackTrace();
			fail();
		}
		client.close();
	}


	@Test
	public void unsuccessfulMessageRequestExecution() {
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
		@Path( "/v1/channels/whatsapp/messages" )
		@Consumes( MediaType.APPLICATION_JSON )
		@Produces( MediaType.APPLICATION_JSON )
		public String messageResource( MessageRequest messageRequest ) {
			this.lastBody = messageRequest;
			return "{\"id\":\"12345\",\"from\":\"123\",\"to\":\"456\",\"direction\":\"OUT\",\"channel\":\"whatsapp\",\"contents\":[{\"type\":\"text\",\"text\":\"This is a test!\"}]}";
		}
	}
}
