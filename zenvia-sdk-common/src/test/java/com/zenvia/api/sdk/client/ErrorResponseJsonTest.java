package com.zenvia.api.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.zenvia.api.sdk.JsonMapper;
import com.zenvia.api.sdk.client.errors.ErrorResponse;
import com.zenvia.api.sdk.client.exceptions.JsonException;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class ErrorResponseJsonTest {
	private JsonMapper jsonMapper = new JsonMapper();


	@Test
	public void deserializationWithoutDetails() throws JsonException, IOException {
		ErrorResponse error = jsonMapper.deserialize(
			"{\"code\":\"TEST\",\"message\":\"This is a test!\"}".getBytes( StandardCharsets.UTF_8 ),
			ErrorResponse.class );
		
		assertNotNull( error );
		assertEquals( "TEST", error.code );
		assertEquals( "This is a test!", error.message );
		assertNotNull( error.details );
		assertEquals( 0, error.details.size() );
	}


	@Test
	public void deserialization() throws JsonException, IOException {
		ErrorResponse error = jsonMapper.deserialize(
			"{\"code\":\"TEST\",\"message\":\"This is a test!\",\"details\":[{\"code\":\"INVALID\",\"path\":\"id\",\"message\":\"Invalid id!\"}]}".getBytes( StandardCharsets.UTF_8 ),
			ErrorResponse.class );
		
		assertNotNull( error );
		assertEquals( "TEST", error.code );
		assertEquals( "This is a test!", error.message );
		assertNotNull( error.details );
		assertEquals( 1, error.details.size() );
		assertEquals( "INVALID", error.details.get( 0 ).code );
		assertEquals( "id", error.details.get( 0 ).path );
		assertEquals( "Invalid id!", error.details.get( 0 ).message );
	}


	@Test
	public void deserializationWithUnsupportedAttributes() throws JsonException, IOException {
		ErrorResponse error = jsonMapper.deserialize(
			"{\"code\":\"TEST\",\"message\":\"This is a test!\",\"details\":[],\"new\":[{\"whatever\":\"!\"}]}".getBytes( StandardCharsets.UTF_8 ),
			ErrorResponse.class );
		
		assertNotNull( error );
		assertEquals( "TEST", error.code );
		assertEquals( "This is a test!", error.message );
	}


	@Test
	public void deserializationWithUnsupportedAttributesOnADetail() throws JsonException, IOException {
		ErrorResponse error = jsonMapper.deserialize(
			"{\"code\":\"TEST\",\"message\":\"This is a test!\",\"details\":[{\"code\":\"INVALID\",\"path\":\"id\",\"message\":\"Invalid id!\",\"new\":[{\"whatever\":\"!\"}]}]}".getBytes( StandardCharsets.UTF_8 ),
			ErrorResponse.class );
		
		assertNotNull( error );
		assertEquals( "TEST", error.code );
		assertEquals( "This is a test!", error.message );
	}


	@Test
	public void deserializationWithoutKnownAttributes() throws JsonException, IOException {
		ErrorResponse error = jsonMapper.deserialize(
			"{}".getBytes( StandardCharsets.UTF_8 ),
			ErrorResponse.class );
		
		assertNotNull( error );
		assertNull( error.code );
		assertNull( error.message );
	}
}
