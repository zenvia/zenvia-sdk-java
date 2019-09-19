package com.zenvia.api.sdk.client;

import com.zenvia.api.sdk.client.exceptions.HttpConnectionFailException;
import com.zenvia.api.sdk.client.exceptions.HttpConnectionTimeoutException;
import com.zenvia.api.sdk.client.exceptions.HttpIOException;
import com.zenvia.api.sdk.client.exceptions.HttpProtocolException;
import com.zenvia.api.sdk.client.exceptions.HttpSocketTimeoutException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;
import com.zenvia.api.sdk.client.messages.MessageRequest;
import com.zenvia.api.sdk.client.messages.MessageResponse;


public interface IClient {
	public MessageResponse sendMessage( Channel channel, MessageRequest messageRequest )
		throws UnsuccessfulRequestException, HttpSocketTimeoutException, HttpConnectionTimeoutException, HttpConnectionFailException, HttpProtocolException, HttpIOException;


	public String getApiUrl();
}
