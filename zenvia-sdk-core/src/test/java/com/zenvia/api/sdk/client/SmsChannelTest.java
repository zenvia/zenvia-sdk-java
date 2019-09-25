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
public class SmsChannelTest extends ChannelTest {
	public SmsChannelTest() {
		super( ChannelType.sms ); 
	}


	@Test
	public void fileContentNotSupported() {
		contentNotSupportedTest( 0, new FileContent(
			"https://zenvia.com/favicon.ico",
			"image/x-icon",
			"This is a test!" 
		) );
	}


	@Test
	public void fileContentNotSupportedMixedWithSupportedContent() {
		contentNotSupportedTest( 2,
			new TextContent( "This is the first test!" ),
			new TextContent( "This is the second test!" ),
			new FileContent(
				"https://zenvia.com/favicon.ico",
				"image/x-icon",
				"This is the third test!" 
			),
			new TextContent( "This is the fourth test!" )
		);
	}


	@Test
	public void templateContentNotSupported() {
		Map<String,String> fields = new HashMap<String,String>();
		fields.put( "name", "value" );
		contentNotSupportedTest( 0, new TemplateContent( "12345", fields ) );
	}


	@Test
	public void templateContentNotSupportedMixedWithSupportedContent() {
		contentNotSupportedTest( 3,
			new TextContent( "This is the first test!" ),
			new TextContent( "This is the second test!" ),
			new TextContent( "This is the third test!" ),
			new FileContent(
				"https://zenvia.com/favicon.ico",
				"image/x-icon",
				"This is the fourth test!" 
			)
		);
	}
}
