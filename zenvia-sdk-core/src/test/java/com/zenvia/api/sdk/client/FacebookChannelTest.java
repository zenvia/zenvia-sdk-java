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
public class FacebookChannelTest extends ChannelTest {
	public FacebookChannelTest() {
		super( ChannelType.facebook ); 
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
	public void templateContentNotSupported() {
		Map<String,String> fields = new HashMap<String,String>();
		fields.put( "name", "value" );
		contentNotSupportedTest( 0, new TemplateContent( "12345", fields ) );
	}


	@Test
	public void templateContentNotSupportedMixedWithSupportedContent() {
		contentNotSupportedTest( 0,
			new TemplateContent( "12345", null ),
			new TextContent( "This is the second test!" ),
			new TextContent( "This is the third test!" ),
			new TextContent( "This is the fourth test!" )
		);
	}


	@Test
	public void mixedSuportedContent() {
		contentSupportedTest(
			new FileContent(
				"https://zenvia.com/favicon.ico",
				"image/x-icon",
				"This is the first test!" 
			),
			new TextContent( "This is the second test!" ),
			new TextContent( "This is the third test!" ),
			new TextContent( "This is the fourth test!" )
		);
	}
}
