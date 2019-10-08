# Zenvia CPaaS SDK for Java

This SDK for [Java](https://www.java.com/) was created based on the [Zenvia](https://www.zenvia.com/) [API](https://zenvia.github.io/zenvia-openapi-spec/) and
[Zenvia CPaaS SDK for Node.js](https://github.com/zenvia/zenvia-sdk-node).

[![License](https://img.shields.io/github/license/zenvia/zenvia-sdk-java.svg)](LICENSE.md)
[![Build Status](https://travis-ci.com/zenvia/zenvia-sdk-java.svg?branch=master)](https://travis-ci.com/zenvia/zenvia-sdk-java)
[![Coverage Status](https://coveralls.io/repos/github/zenvia/zenvia-sdk-java/badge.svg?branch=master)](https://coveralls.io/github/zenvia/zenvia-sdk-java?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.zenvia/zenvia-api-sdk/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.zenvia/zenvia-api-sdk/)

[![Twitter Follow](https://img.shields.io/twitter/follow/ZenviaMobile.svg?style=social)](https://twitter.com/intent/follow?screen_name=ZenviaMobile)



## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Basic Usage](#basic-usage)
- [Getting Started](#getting-started)
- [Contributing](#contributing)
- [License](#license)



## Features

- [x] Text message content
- [x] File message content
- [x] Template message content
- [x] Subscription handling
- [x] Logging support



## Prerequisites

* [Sign up](https://www.zenvia.com/) for a Zenvia Account
* [Java](https://www.java.com/)



#### Obtain an API Token

You need to create an API token in the Zenvia [API console](https://app.zenvia.com/home/api).



## Installation
If using maven, add to your pom [this SDK](https://search.maven.org/search?q=g:com.zenvia+AND+a:zenvia-api-sdk-client-apache):

```xml
<dependency>
	<groupId>com.zenvia</groupId>
	<artifactId>zenvia-api-sdk-client-apache</artifactId>
	<version>1.0.0</version>
</dependency>
```


or [the one](https://search.maven.org/search?q=g:com.zenvia+AND+a:zenvia-api-sdk-client-spring) that includes [Spring Boot](https://spring.io/projects/spring-boot):

```xml
<dependency>
	<groupId>com.zenvia</groupId>
	<artifactId>zenvia-api-sdk-client-spring</artifactId>
	<version>1.0.0</version>
</dependency>
```



## Basic Usage

```Java
import com.zenvia.api.sdk.contents.*;
import com.zenvia.api.sdk.messages.Message;
import com.zenvia.api.sdk.client.Channel;
import com.zenvia.api.sdk.client.errors.ErrorResponse;
import com.zenvia.api.sdk.client.exception.ApiException;
import com.zenvia.api.sdk.client.exception.UnsuccessfulRequestException;

// When using zenvia-api-sdk-client-apache
import com.zenvia.api.sdk.client.apache.Client;
// When using zenvia-api-sdk-client-spring
import com.zenvia.api.sdk.client.spring.Client;


// Initialization with your API token (x-api-token)
Client client = new Client("YOUR_API_TOKEN");

// Choosing the channel
Channel whatsapp = client.getChannel("whatsapp");

// Creating a text content
Content content = new TextContent("some text message here");

try {
  Message response = whatsapp.sendMessage("sender-identifier", "recipient-identifier", content);
  // do something here
} catch(UnsuccessfulRequestException exception) {
  ErrorResponse response = exception.body;
  // handle error here
} catch(ApiException exception) {
  // handle error here
}
```


## Getting Started


### Sending your first message

Use the `sendMessage` method to send text (`TextContent`), file (`FileContent`) or template (`TemplateContent`) messages to your contacts.

```java
Client client = new Client("YOUR_API_TOKEN");
Channel sms = client.getChannel("sms");
TextContent content = new TextContent("some text message");
try {
  Message response = sms.sendMessage("sender-identifier", "recipient-identifier", content);
} catch(UnsuccessfulRequestException exception) {
  ErrorResponse response = exception.body;
}
```

The response will be an `Message` object when successful, or an `ErrorResponse` object will be
available on the exception.

The content types can be:

| Name            | Description |
|-----------------|-------------|
| TextContent     | Used to send text messages to your customer.
| FileContent     | Used to send file messages to your customer.
| TemplateContent | Used to send template messages to your customer.

The content support by channel is described below.

| Channel  | TextContent | FileContent | TemplateContent |
|----------|    :---:    |    :---:    |      :---:      |
| SMS      | X           |             |                 |
| WhatsApp | X           | X           | X               |
| Facebook | X           | X           |                 |



## Contributing

Pull requests are always welcome!

Please see the [Contributors' Guide](CONTRIBUTING.md) for more information on contributing.



## License

[MIT](LICENSE.md)
