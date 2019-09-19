package com.zenvia.api.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.zenvia.api.sdk.JsonMapper;
import com.zenvia.api.sdk.client.errors.ErrorResponse;
import com.zenvia.api.sdk.client.exceptions.JsonException;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class JsonMapperTest {
	private JsonMapper jsonMapper = new JsonMapper();


	@Test
	public void nonJsonDeserialization() {
		try {
			jsonMapper.deserialize(
				"whatever".getBytes( StandardCharsets.UTF_8 ),
				ErrorResponse.class );
			fail();
		} catch( JsonException exception ) {
			assertEquals( "Exception deserializing", exception.getMessage() );
		} catch( IOException exception ) {
			fail();
		}
	}


	@Test
	public void listDeserializationAsObject() {
		try {
			jsonMapper.deserialize(
				"[]".getBytes( StandardCharsets.UTF_8 ),
				ErrorResponse.class );
			fail();
		} catch( JsonException exception ) {
			assertEquals( "Exception deserializing", exception.getMessage() );
		} catch( IOException exception ) {
			fail();
		}
	}
}
