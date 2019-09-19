package com.zenvia.api.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.zenvia.api.sdk.JsonMapper;
import com.zenvia.api.sdk.client.exceptions.JsonException;
import com.zenvia.api.sdk.client.messages.MessageResponse;
import com.zenvia.api.sdk.client.messages.TMessageDirection;
import com.zenvia.api.sdk.contents.ContentType;
import com.zenvia.api.sdk.contents.TextContent;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class MessageResponseJsonTest {
	private JsonMapper jsonMapper = new JsonMapper();


	@Test
	public void deserializationMissingContents() throws JsonException, IOException {
		MessageResponse messageResponse = jsonMapper.deserialize(
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
	public void deserialization() throws JsonException, IOException {
		MessageResponse messageResponse = jsonMapper.deserialize(
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
	public void deserializationWithUnsupportedAttributes() throws JsonException, IOException {
		MessageResponse messageResponse = jsonMapper.deserialize(
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
	public void deserializationWithUnsupportedChannel() throws IOException {
		try {
			jsonMapper.deserialize(
				"{\"id\":\"12345\",\"from\":\"123\",\"to\":\"456\",\"direction\":\"OUT\",\"channel\":\"new\",\"contents\":[]}".getBytes( StandardCharsets.UTF_8 ),
				MessageResponse.class );
			fail();
		} catch( JsonException exception ) {
			assertEquals( "Exception deserializing", exception.getMessage() );
		}
	}
}
