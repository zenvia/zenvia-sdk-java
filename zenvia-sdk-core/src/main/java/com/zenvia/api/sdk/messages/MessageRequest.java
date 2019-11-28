package com.zenvia.api.sdk.messages;


import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zenvia.api.sdk.Json;
import com.zenvia.api.sdk.client.AbstractClient;
import com.zenvia.api.sdk.contents.Content;


/** Internally used by all implementations of {@link AbstractClient} to send a message.
 *
 *  @since 0.9.0 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class MessageRequest {
	/** Id of the sender of the message. The format of the value varies from channel to channel.
	 *  When the message originates from the contact, it is the id of the contact. When it is the
	 *  other way around, it represents an integration on <a href="https://app.zenvia.com" target="_blank">https://app.zenvia.com</a>.
	 *
	 *  @since 0.9.0 */
	public final String from;

	/** Id of the receiver of the message. The format of the value varies from channel to channel.
	 *  When the contact is the receiver of the message, it is the id of the contact. When it is the
	 *  other way around, it represents an integration on <a href="https://app.zenvia.com" target="_blank">https://app.zenvia.com</a>.
	 *
	 *  @since 0.9.0 */
	public final String to;

	/** List of contents. When sending a message, it is the list of contents to be sent to the contact.
	 *  When handling callbacks from subscriptions, it is the list of contents sent either by the
	 *  contact, or on its behalf. 
	 *
	 *  @since 0.9.0 */
	public final List<Content> contents;


	/** @param from Id of the sender of the message.
	 *
	 *  @param to Id of the receiver of the message.
	 *
	 *  @param contents List of contents.
	 *
	 *  @since 0.9.0 */
	@JsonCreator
	public MessageRequest(
		@JsonProperty( "from" ) String from,
		@JsonProperty( "to" ) String to,
		@JsonProperty( "contents" ) List<Content> contents
	) {
		this.from = from;
		this.to = to;
		this.contents = contents == null ? Collections.emptyList() : Collections.unmodifiableList( contents );
	}


	/** String containg the object as an indented JSON.
	 *
	 *  @since 0.9.0 */
	@Override
	public String toString() {
		return Json.pretty( this );
	}
}
