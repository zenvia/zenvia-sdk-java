package com.zenvia.api.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.zenvia.api.sdk.JsonMapper;
import com.zenvia.api.sdk.client.messages.MessageResponse;
import com.zenvia.api.sdk.client.messages.TMessageDirection;
import com.zenvia.api.sdk.contents.ContentType;
import com.zenvia.api.sdk.contents.TextContent;


public class MessageResponseJsonTest {
	@Test
	public void deserializationMissingContents() {
		MessageResponse messageResponse = JsonMapper.deserialize(
			"{\"id\":\"12345\",\"from\":\"123\",\"to\":\"456\",\"direction\":\"OUT\",\"channel\":\"whatsapp\"}".getBytes( StandardCharsets.UTF_8 ),
			MessageResponse.class );
		
		assertNotNull( messageResponse );
		assertEquals( "12345", messageResponse.id );
		assertEquals( "123", messageResponse.from );
		assertEquals( "456", messageResponse.to );
		assertEquals( TMessageDirection.OUT, messageResponse.direction );
		assertEquals( ChannelType.whatsapp, messageResponse.channel );
	}


	@Test
	public void deserialization() {
		MessageResponse messageResponse = JsonMapper.deserialize(
			"{\"id\":\"12345\",\"from\":\"123\",\"to\":\"456\",\"direction\":\"OUT\",\"channel\":\"whatsapp\",\"contents\":[{\"type\":\"text\",\"text\":\"This is a test!\"}]}".getBytes( StandardCharsets.UTF_8 ),
			MessageResponse.class );
		
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
	}


	@Test
	public void deserializationWithUnsupportedAttributes() {
		MessageResponse messageResponse = JsonMapper.deserialize(
			"{\"id\":\"12345\",\"from\":\"123\",\"to\":\"456\",\"direction\":\"OUT\",\"channel\":\"whatsapp\",\"contents\":[]}".getBytes( StandardCharsets.UTF_8 ),
			MessageResponse.class );
		
		assertNotNull( messageResponse );
		assertEquals( "12345", messageResponse.id );
		assertEquals( "123", messageResponse.from );
		assertEquals( "456", messageResponse.to );
		assertEquals( TMessageDirection.OUT, messageResponse.direction );
		assertEquals( ChannelType.whatsapp, messageResponse.channel );
	}


	@Test
	public void deserializationWithUnsupportedChannel() {
		try {
			JsonMapper.deserialize(
				"{\"id\":\"12345\",\"from\":\"123\",\"to\":\"456\",\"direction\":\"OUT\",\"channel\":\"new\",\"contents\":[]}".getBytes( StandardCharsets.UTF_8 ),
				MessageResponse.class );
			fail();
		} catch( RuntimeException exception ) {
			assertEquals( "Exception deserializing", exception.getMessage() );
		}
	}
}
