package com.zenvia.api.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.zenvia.api.sdk.client.messages.Message;
import com.zenvia.api.sdk.client.messages.MessageDirection;
import com.zenvia.api.sdk.contents.ContentType;
import com.zenvia.api.sdk.contents.TextContent;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class MessageTest {
	private ObjectMapper jsonMapper = new ObjectMapper();


	@Test
	public void deserializationMissingContents() throws IOException {
		Message message = jsonMapper.readValue(
			"{\"id\":\"12345\",\"from\":\"123\",\"to\":\"456\",\"direction\":\"OUT\",\"channel\":\"whatsapp\"}".getBytes( StandardCharsets.UTF_8 ),
			Message.class
		);

		assertNotNull( message );
		assertEquals( "12345", message.id );
		assertEquals( "123", message.from );
		assertEquals( "456", message.to );
		assertEquals( MessageDirection.OUT, message.direction );
		assertEquals( ChannelType.whatsapp, message.channel );
	}


	@Test
	public void deserialization() throws IOException {
		Message message = jsonMapper.readValue(
			"{\"id\":\"12345\",\"from\":\"123\",\"to\":\"456\",\"direction\":\"OUT\",\"channel\":\"whatsapp\",\"contents\":[{\"type\":\"text\",\"text\":\"This is a test!\"}]}".getBytes( StandardCharsets.UTF_8 ),
			Message.class
		);

		assertNotNull( message );
		assertEquals( "12345", message.id );
		assertEquals( "123", message.from );
		assertEquals( "456", message.to );
		assertEquals( MessageDirection.OUT, message.direction );
		assertEquals( ChannelType.whatsapp, message.channel );
		assertNotNull( message.contents );
		assertEquals( 1, message.contents.size() );
		assertEquals( ContentType.text, message.contents.get( 0 ).type );
		assertEquals( "This is a test!", ( (TextContent) message.contents.get( 0 ) ).text );
	}


	@Test
	public void deserializationWithUnsupportedAttributes() throws IOException {
		Message message = jsonMapper.readValue(
			"{\"id\":\"12345\",\"from\":\"123\",\"to\":\"456\",\"direction\":\"OUT\",\"channel\":\"whatsapp\",\"contents\":[]}".getBytes( StandardCharsets.UTF_8 ),
			Message.class
		);

		assertNotNull( message );
		assertEquals( "12345", message.id );
		assertEquals( "123", message.from );
		assertEquals( "456", message.to );
		assertEquals( MessageDirection.OUT, message.direction );
		assertEquals( ChannelType.whatsapp, message.channel );
	}


	@Test
	public void deserializationWithUnsupportedChannel() throws IOException {
		try {
			jsonMapper.readValue(
				"{\"id\":\"12345\",\"from\":\"123\",\"to\":\"456\",\"direction\":\"OUT\",\"channel\":\"new\",\"contents\":[]}".getBytes( StandardCharsets.UTF_8 ),
				Message.class
			);
			fail();
		} catch( JsonMappingException exception ) {
			assertEquals(
				"Cannot deserialize value of type `com.zenvia.api.sdk.client.ChannelType` from String \"new\": value not one of declared Enum instance names: [sms, whatsapp, facebook]\n at [Source: (byte[])\"{\"id\":\"12345\",\"from\":\"123\",\"to\":\"456\",\"direction\":\"OUT\",\"channel\":\"new\",\"contents\":[]}\"; line: 1, column: 67] (through reference chain: com.zenvia.api.sdk.client.messages.MessageResponse[\"channel\"])",
				exception.getMessage()
			);
		}
	}
}
