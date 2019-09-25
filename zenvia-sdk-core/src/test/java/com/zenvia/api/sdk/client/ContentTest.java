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

import com.zenvia.api.sdk.contents.Content;
import com.zenvia.api.sdk.contents.FileContent;
import com.zenvia.api.sdk.contents.ContentType;
import com.zenvia.api.sdk.contents.TemplateContent;
import com.zenvia.api.sdk.contents.TextContent;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class ContentJsonTest {
	private ObjectMapper jsonMapper = new ObjectMapper();


	@Test
	public void textDeserialization() throws IOException {
		Content content = jsonMapper.readValue(
			"{\"type\":\"text\",\"text\":\"This is a test!\"}".getBytes( StandardCharsets.UTF_8 ),
			Content.class
		);
		
		assertNotNull( content );
		assertEquals( ContentType.text, content.type );
		assertEquals( TextContent.class, content.getClass() );
		TextContent textContent = (TextContent) content;
		assertEquals( "This is a test!", textContent.text );
	}


	@Test
	public void fileDeserialization() throws IOException {
		Content content = jsonMapper.readValue(
			"{\"type\":\"file\",\"fileUrl\":\"https://zenvia.com/favicon.ico\",\"fileMimeType\":\"image/x-icon\",\"fileCaption\":\"This is a test!\"}".getBytes( StandardCharsets.UTF_8 ),
			Content.class
		);
		
		assertNotNull( content );
		assertEquals( ContentType.file, content.type );
		assertEquals( FileContent.class, content.getClass() );
		FileContent fileContent = (FileContent) content;
		assertEquals( "https://zenvia.com/favicon.ico", fileContent.fileUrl );
		assertEquals( "image/x-icon", fileContent.fileMimeType );
		assertEquals( "This is a test!", fileContent.fileCaption );
	}


	@Test
	public void templateDeserialization() throws IOException {
		Content content = jsonMapper.readValue(
			"{\"type\":\"template\",\"templateId\":\"123\",\"fields\":{\"name\":\"value\"}}".getBytes( StandardCharsets.UTF_8 ),
			Content.class
		);
		
		assertNotNull( content );
		assertEquals( ContentType.template, content.type );
		assertEquals( TemplateContent.class, content.getClass() );
		TemplateContent templateContent = (TemplateContent) content;
		assertEquals( "123", templateContent.templateId );
		assertNotNull( templateContent.fields );
		assertEquals( 1, templateContent.fields.size() );
		assertEquals( "value", templateContent.fields.get( "name" ) );
	}


	@Test
	public void unknowDeserialization() throws IOException {
		try {
			jsonMapper.readValue(
				"{\"type\":\"new\"}" .getBytes( StandardCharsets.UTF_8 ),
				Content.class );
			fail();
		} catch( JsonMappingException exception ) {
			assertEquals(
				"Could not resolve type id 'new' as a subtype of [simple type, class com.zenvia.api.sdk.contents.Content]: known type ids = [file, template, text]\n at [Source: (byte[])\"{\"type\":\"new\"}\"; line: 1, column: 9]",
				exception.getMessage()
			);
		}
	}
}
