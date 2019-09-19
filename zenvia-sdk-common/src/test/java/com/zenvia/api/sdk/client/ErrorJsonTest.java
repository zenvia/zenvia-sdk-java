package com.zenvia.api.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.zenvia.api.sdk.JsonMapper;
import com.zenvia.api.sdk.client.errors.Error;


public class ErrorJsonTest {
	@Test
	public void deserializationWithoutDetails() {
		Error error = JsonMapper.deserialize(
			"{\"code\":\"TEST\",\"message\":\"This is a test!\"}".getBytes( StandardCharsets.UTF_8 ),
			Error.class );
		
		assertNotNull( error );
		assertEquals( "TEST", error.code );
		assertEquals( "This is a test!", error.message );
		assertNotNull( error.details );
		assertEquals( 0, error.details.size() );
	}


	@Test
	public void deserialization() {
		Error error = JsonMapper.deserialize(
			"{\"code\":\"TEST\",\"message\":\"This is a test!\",\"details\":[{\"code\":\"INVALID\",\"path\":\"id\",\"message\":\"Invalid id!\"}]}".getBytes( StandardCharsets.UTF_8 ),
			Error.class );
		
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
	public void deserializationWithUnsupportedAttributes() {
		Error error = JsonMapper.deserialize(
			"{\"code\":\"TEST\",\"message\":\"This is a test!\",\"details\":[],\"new\":[{\"whatever\":\"!\"}]}".getBytes( StandardCharsets.UTF_8 ),
			Error.class );
		
		assertNotNull( error );
		assertEquals( "TEST", error.code );
		assertEquals( "This is a test!", error.message );
	}


	@Test
	public void deserializationWithUnsupportedAttributesOnADetail() {
		Error error = JsonMapper.deserialize(
			"{\"code\":\"TEST\",\"message\":\"This is a test!\",\"details\":[{\"code\":\"INVALID\",\"path\":\"id\",\"message\":\"Invalid id!\",\"new\":[{\"whatever\":\"!\"}]}]}".getBytes( StandardCharsets.UTF_8 ),
			Error.class );
		
		assertNotNull( error );
		assertEquals( "TEST", error.code );
		assertEquals( "This is a test!", error.message );
	}
}
