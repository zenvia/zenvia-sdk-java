package com.zenvia.api.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.zenvia.api.sdk.client.errors.ErrorResponse;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class JsonMapperTest {
	private ObjectMapper jsonMapper = new ObjectMapper();


	@Test
	public void emptyDeserialization() {
		try {
			jsonMapper.readValue(
				"".getBytes( StandardCharsets.UTF_8 ),
				ErrorResponse.class
			);
			fail();
		} catch( JsonMappingException exception ) {
			assertEquals(
				"No content to map due to end-of-input\n at [Source: (byte[])\"\"; line: 1, column: 0]",
				exception.getMessage()
			);
		} catch( IOException exception ) {
			fail();
		}
	}


	@Test
	public void nonJsonDeserialization() {
		try {
			jsonMapper.readValue(
				"whatever".getBytes( StandardCharsets.UTF_8 ),
				ErrorResponse.class
			);
			fail();
		} catch( JsonParseException exception ) {
			assertEquals( "Unrecognized token 'whatever': was expecting ('true', 'false' or 'null')\n at [Source: (byte[])\"whatever\"; line: 1, column: 17]", exception.getMessage() );
		} catch( IOException exception ) {
			fail();
		}
	}


	@Test
	public void listDeserializationAsObject() {
		try {
			jsonMapper.readValue(
				"[]".getBytes( StandardCharsets.UTF_8 ),
				ErrorResponse.class
			);
			fail();
		} catch( JsonMappingException exception ) {
			assertEquals(
				"Cannot deserialize instance of `com.zenvia.api.sdk.client.errors.ErrorResponse` out of START_ARRAY token\n at [Source: (byte[])\"[]\"; line: 1, column: 1]",
				exception.getMessage()
			);
		} catch( IOException exception ) {
			fail();
		}
	}
}
