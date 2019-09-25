package com.zenvia.api.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import com.zenvia.api.sdk.client.exceptions.HttpRequestException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.exceptions.UnsupportedContentException;
import com.zenvia.api.sdk.client.messages.MessageRequest;
import com.zenvia.api.sdk.contents.Content;
import com.zenvia.api.sdk.contents.TextContent;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public abstract class AbstractChannelTest {
	protected final AbstractClient client = Mockito.mock( AbstractClient.class );

	protected final Channel channel;


	public AbstractChannelTest( ChannelType channelType ) {
		this.channel = new Channel( channelType, client );
	}


	@Before
	public void before() {
		when( client.getApiUrl() ).thenReturn( "URL" );
	}


	@Test
	public void textContentSupported() {
		contentSupportedTest( new TextContent( "This is a test!" ) );
	}


	@Test
	public void multipleTextContentSupported() {
		contentSupportedTest(
			new TextContent( "This is the first test!" ),
			new TextContent( "This is the second test!" ),
			new TextContent( "This is the third test!" )
		);
	}


	protected void contentSupportedTest( Content... contents ) {
		try {
			MessageRequest messageRequest = new MessageRequest( "from", "to", contents );
			channel.sendMessage( messageRequest );
			verify( client ).sendMessage( channel, messageRequest );
		} catch( UnsuccessfulRequestException | HttpRequestException exception ) {
			fail();
		}
	}


	protected void contentNotSupportedTest( int expectedFailIndex, Content... contents ) {
		try {
			try {
				channel.sendMessage( new MessageRequest( "from", "to", contents ) );
				fail();
			} catch( UnsupportedContentException exception ) {
				assertEquals( contents[expectedFailIndex].type.name(), exception.getContentType() );
				assertEquals( channel.type.name(), exception.getChannelType() );
				assertEquals( "Content type " + exception.getContentType() + " is not supported by " + exception.getChannelType() + " channel", exception.getMessage() );
				verify( client, never() ).sendMessage( any( Channel.class ), any( MessageRequest.class ) );
			} catch( UnsuccessfulRequestException exception ) {
				fail();
			}
		} catch( UnsuccessfulRequestException | HttpRequestException exception ) {
			fail();
		}
	}
}
