package com.zenvia.api.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import com.zenvia.api.sdk.client.exceptions.UnsupportedChannelException;


@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class ClientTest {
	@Test
	public void getUnsupportedChannel() {
		try {
			client().getChannel( "new" );
			fail();
		} catch( UnsupportedChannelException exception ) {
			assertEquals( "new" , exception.getChannelType() );
			assertEquals( "Unsupported channel: new" , exception.getMessage() );
		}
	}


	@Test
	public void getWhatsappChannelByString() {
		Channel channel = client().getChannel( "whatsapp" );
		assertNotNull( channel );
		assertEquals( channel.type, ChannelType.whatsapp );
	}


	@Test
	public void getSmsChannelByString() {
		Channel channel = client().getChannel( "sms" );
		assertNotNull( channel );
		assertEquals( channel.type, ChannelType.sms );
	}


	@Test
	public void getFacebookChannelByString() {
		Channel channel = client().getChannel( "facebook" );
		assertNotNull( channel );
		assertEquals( channel.type, ChannelType.facebook );
	}


	@Test
	public void getWhatsappChannelByType() {
		Channel channel = client().getChannel( ChannelType.whatsapp );
		assertNotNull( channel );
		assertEquals( channel.type, ChannelType.whatsapp );
	}


	@Test
	public void getSmsChannelByType() {
		Channel channel = client().getChannel( ChannelType.sms );
		assertNotNull( channel );
		assertEquals( channel.type, ChannelType.sms );
	}


	@Test
	public void getFacebookChannelByType() {
		Channel channel = client().getChannel( ChannelType.facebook );
		assertNotNull( channel );
		assertEquals( channel.type, ChannelType.facebook );
	}


	private AbstractClient client() {
		return Mockito.mock( AbstractClient.class, Mockito.CALLS_REAL_METHODS );
	}
}
