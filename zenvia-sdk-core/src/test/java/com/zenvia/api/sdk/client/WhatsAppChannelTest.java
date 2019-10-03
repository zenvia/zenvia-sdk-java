package com.zenvia.api.sdk.client;

import java.util.HashMap;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.zenvia.api.sdk.contents.FileContent;
import com.zenvia.api.sdk.contents.TemplateContent;
import com.zenvia.api.sdk.contents.TextContent;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class WhatsAppChannelTest extends ChannelTest {
	public WhatsAppChannelTest() {
		super( ChannelType.whatsapp ); 
	}


	@Test
	public void fileContentSupported() {
		contentSupportedTest( new FileContent(
			"https://zenvia.com/favicon.ico",
			"image/x-icon",
			"This is a test!" 
		) );
	}


	@Test
	public void templateContentSupported() {
		Map<String,String> fields = new HashMap<String,String>();
		fields.put( "name", "value" );
		contentSupportedTest( new TemplateContent( "12345", fields ) );
	}


	@Test
	public void mixedSuportedContent() {
		Map<String,String> fields = new HashMap<String,String>();
		fields.put( "name", "value" );
		contentSupportedTest(
			new TemplateContent( "12345", fields ),
			new TextContent( "This is the second test!" ),
			new FileContent(
				"https://zenvia.com/favicon.ico",
				"image/x-icon",
				"This is the third test!" 
			),
			new TextContent( "This is the fourth test!" ),
			new TextContent( "This is the fifty test!" )
		);
	}
}
