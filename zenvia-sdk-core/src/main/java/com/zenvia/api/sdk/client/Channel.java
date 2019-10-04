package com.zenvia.api.sdk.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.zenvia.api.sdk.client.exceptions.HttpConnectionFailException;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionTimeoutException;
import com.zenvia.api.sdk.client.exceptions.HttpIOException;
import com.zenvia.api.sdk.client.exceptions.HttpSocketTimeoutException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.exceptions.UnsupportedContentException;
import com.zenvia.api.sdk.client.messages.MessageRequest;
import com.zenvia.api.sdk.client.subscriptions.MessageStatusSubscription;
import com.zenvia.api.sdk.client.messages.Message;
import com.zenvia.api.sdk.contents.Content;


/** Encapsulates the API calls under channel resource
 *  ({@link "https://zenvia.github.io/zenvia-openapi-spec/#section/API-Design/Resources-path"}).
 *
 *  @since 0.9.0 */
public class Channel {
	/** @since 0.9.0 */
	public final ChannelType type;

	/** @since 0.9.0 */
	public final String url;

	protected final AbstractClient client;


	protected Channel( ChannelType type, AbstractClient client ) {
		this.type = type;
		this.client = client;
		this.url = client.getApiUrl() + type.messagePath;
	}


	/** Send one or more contents to a contact. On most channels, each content will be a message
	 *  delivered to the receiver. The call is asynchronous. So, in order to know if it has been
	 *  delivered successfully to the receiver, it is necessary to create a
	 *  {@link MessageStatusSubscription message status subscription} for this channel, using
	 *  {@link AbstractClient#createSubscription createSubscription method} from one of the
	 *  available {@link AbstractClient client} implementations.
	 * 
	 *  @param from Id of the sender of the message. Represents an integration on
	 *  {@link "https://app.zenvia.com"}. The format of the value varies from channel to channel.
	 *
	 *  @param to Id of the receiver of the message. The format of the value varies from channel
	 *  to channel. On channels which handles with phone numbers, it is the contact's phone number.
	 *
	 *  @param contents List of contents to be sent to the receiver.
	 *
	 *  @throws UnsupportedContentException
	 *  If this channel does not support any of contents passed.
	 *
	 *  @throws UnsuccessfulRequestException
	 *  If the deletion failed, indicated by the
	 *  {@link UnsuccessfulRequestException#httpStatusCode} from the server response.
	 *  The request will also fail if no subscription with the given id exists.
	 *
	 *  @throws HttpSocketTimeoutException
	 *  If the server response timed out. 
	 *
	 *  @throws HttpConnectionTimeoutException
	 *  If the connection attempt timed out.
	 *
	 *  @throws HttpConnectionFailException
	 *  If the connection attempt failed.
	 *
	 *  @throws HttpIOException
	 *  If any other communication problem happens.
	 *
	 *  @since 0.9.0 */
	public Message sendMessage( String from, String to, Content... contents )
		throws UnsupportedContentException, UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return sendMessage( new MessageRequest( from, to, contents == null ? null : Arrays.asList( contents ) ) );
	}


	/** Send one or more content to a contact. On most channels, each content will be a message
	 *  delivered to the receiver. The call is asynchronous. So, in order to know if it has been
	 *  delivered successfully to the receiver, it is necessary to create a
	 *  {@link MessageStatusSubscription message status subscription} for this channel, using
	 *  {@link AbstractClient#createSubscription createSubscription method} from one of the
	 *  available {@link AbstractClient client} implementations.
	 * 
	 *  @param from Id of the sender of the message. Represents an integration on
	 *  {@link "https://app.zenvia.com"}. The format of the value varies from channel to channel.
	 *
	 *  @param to Id of the receiver of the message. The format of the value varies from channel
	 *  to channel. On channels which handles with phone numbers, it is the contact's phone number.
	 *
	 *  @param contents List of contents to be sent to the receiver.
	 *
	 *  @throws UnsupportedContentException
	 *  If this channel does not support any of contents passed.
	 *
	 *  @throws UnsuccessfulRequestException
	 *  If the deletion failed, indicated by the
	 *  {@link UnsuccessfulRequestException#httpStatusCode} from the server response.
	 *  The request will also fail if no subscription with the given id exists.
	 *
	 *  @throws HttpSocketTimeoutException
	 *  If the server response timed out. 
	 *
	 *  @throws HttpConnectionTimeoutException
	 *  If the connection attempt timed out.
	 *
	 *  @throws HttpConnectionFailException
	 *  If the connection attempt failed.
	 *
	 *  @throws HttpIOException
	 *  If any other communication problem happens.
	 *
	 *  @since 0.9.0 */
	public Message sendMessage( String from, String to, Collection<Content> contents )
		throws UnsupportedContentException, UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return sendMessage( new MessageRequest( from, to, contents == null ? null : new ArrayList<>( contents ) ) );
	}


	/** Send one or more content to a contact. On most channels, each content will be a message
	 *  delivered to the receiver. The call is asynchronous. So, in order to know if it has been
	 *  delivered successfully to the receiver, it is necessary to create a
	 *  {@link MessageStatusSubscription message status subscription} for this channel, using
	 *  {@link AbstractClient#createSubscription createSubscription method} from one of the
	 *  available {@link AbstractClient client} implementations.
	 * 
	 *  @param from Id of the sender of the message. Represents an integration on
	 *  {@link "https://app.zenvia.com"}. The format of the value varies from channel to channel.
	 *
	 *  @param to Id of the receiver of the message. The format of the value varies from channel
	 *  to channel. On channels which handles with phone numbers, it is the contact's phone number.
	 *
	 *  @param contents List of contents to be sent to the receiver.
	 *
	 *  @throws UnsupportedContentException
	 *  If this channel does not support any of contents passed.
	 *
	 *  @throws UnsuccessfulRequestException
	 *  If the deletion failed, indicated by the
	 *  {@link UnsuccessfulRequestException#httpStatusCode} from the server response.
	 *  The request will also fail if no subscription with the given id exists.
	 *
	 *  @throws HttpSocketTimeoutException
	 *  If the server response timed out. 
	 *
	 *  @throws HttpConnectionTimeoutException
	 *  If the connection attempt timed out.
	 *
	 *  @throws HttpConnectionFailException
	 *  If the connection attempt failed.
	 *
	 *  @throws HttpIOException
	 *  If any other communication problem happens.
	 *
	 *  @since 0.9.0 */
	public Message sendMessage( String from, String to, List<Content> contents )
		throws UnsupportedContentException, UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		return sendMessage( new MessageRequest( from, to, contents ) );
	}


	protected Message sendMessage( MessageRequest messageRequest )
		throws UnsupportedContentException, UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpIOException {
		messageRequest.contents.forEach( ( content ) -> contentSupportValidation( content ) );
		return client.sendMessage( this, messageRequest );
	}


	private void contentSupportValidation( Content content ) throws UnsupportedContentException {
		if( !type.supportsContent( content.type ) ) {
			throw new UnsupportedContentException( content.type, type );
		}
	}


	/** String containg the channel name.
	 *
	 *  @since 0.9.0 */
	@Override
	public String toString() {
		return type.name();
	}
}
