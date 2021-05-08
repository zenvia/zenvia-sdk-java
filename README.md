# Zenvia CPaaS SDK for Java

This SDK for [Java](https://www.java.com/) was created based on the [Zenvia](https://www.zenvia.com/) [API](https://zenvia.github.io/zenvia-openapi-spec/) and
[Zenvia CPaaS SDK for Node.js](https://github.com/zenvia/zenvia-sdk-node).

[![License](https://img.shields.io/github/license/zenvia/zenvia-sdk-java.svg)](LICENSE.md)
[![Build Status](https://travis-ci.com/zenvia/zenvia-sdk-java.svg?branch=master)](https://travis-ci.com/zenvia/zenvia-sdk-java)
[![Coverage Status](https://coveralls.io/repos/github/zenvia/zenvia-sdk-java/badge.svg?branch=master)](https://coveralls.io/github/zenvia/zenvia-sdk-java?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.zenvia/zenvia-api-sdk/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.zenvia/zenvia-api-sdk/)

[![Twitter Follow](https://img.shields.io/twitter/follow/ZENVIA_.svg?style=social)](https://twitter.com/intent/follow?screen_name=ZENVIA_)

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

- [Sign up](https://www.zenvia.com/) for a Zenvia Account
- [Java](https://www.java.com/)

#### Obtain an API Token

You need to create an API token in the Zenvia [API console](https://app.zenvia.com/home/api).

## Installation

### Client

If using maven, add to your pom [this SDK](https://search.maven.org/search?q=g:com.zenvia+AND+a:zenvia-api-sdk-client-apache):

```xml
<dependency>
	<groupId>com.zenvia</groupId>
	<artifactId>zenvia-api-sdk-client-apache</artifactId>
	<version>1.1.0</version>
</dependency>
```

If you are using Gradle instead, add to your `build.gradle` [this SDK](https://search.maven.org/search?q=g:com.zenvia+AND+a:zenvia-api-sdk-client-apache):

```groovy
dependencies {
    implementation group: 'com.zenvia', name: 'zenvia-api-sdk-client-apache', version: '1.1.0'
}
```

You can also use [the one](https://search.maven.org/search?q=g:com.zenvia+AND+a:zenvia-api-sdk-client-spring) that includes [Spring Boot](https://spring.io/projects/spring-boot):

```xml
<dependency>
	<groupId>com.zenvia</groupId>
	<artifactId>zenvia-api-sdk-client-spring</artifactId>
	<version>1.1.0</version>
</dependency>
```

or in `build.gradle`:

```groovy
dependencies {
    implementation group: 'com.zenvia', name: 'zenvia-api-sdk-client-spring', version: '1.1.0'
}
```

### Webhook Controller

Add to your pom [this SDK](https://search.maven.org/search?q=g:com.zenvia+AND+a:zenvia-api-sdk-webhook-jersey) for the WebhookController over `Jersey` framework:

```xml
<dependency>
	<groupId>com.zenvia</groupId>
	<artifactId>zenvia-api-sdk-webhook-jersey</artifactId>
	<version>1.1.0</version>
</dependency>
```

or in `build.gradle`:

```groovy
dependencies {
    implementation group: 'com.zenvia', name: 'zenvia-api-sdk-webhook-jersey', version: '1.1.0'
}
```

or [the one](https://search.maven.org/search?q=g:com.zenvia+AND+a:zenvia-api-sdk-webhook-webmvc) for the WebhookController over `Spring Web MVC` framework:

```xml
<dependency>
	<groupId>com.zenvia</groupId>
	<artifactId>zenvia-api-sdk-webhook-webmvc</artifactId>
	<version>1.1.0</version>
</dependency>
```

or in `build.gradle`

```groovy
dependencies {
    implementation group: 'com.zenvia', name: 'zenvia-api-sdk-webhook-webmvc', version: '1.1.0'
}
```

### Starters

For those using Spring Boot, consider ours [Starters](./zenvia-sdk-starters/README.md) to easily configure the Zenvia SDK in your project.

## Basic Usage

```Java
import com.zenvia.api.sdk.contents.*;
import com.zenvia.api.sdk.messages.Message;
import com.zenvia.api.sdk.client.Channel;
import com.zenvia.api.sdk.client.errors.ErrorResponse;
import com.zenvia.api.sdk.client.exceptions.ApiException;
import com.zenvia.api.sdk.client.exceptions.UnsuccessfulRequestException;

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

| Name            | Description                                      |
| --------------- | ------------------------------------------------ |
| TextContent     | Used to send text messages to your customer.     |
| FileContent     | Used to send file messages to your customer.     |
| TemplateContent | Used to send template messages to your customer. |

The content support by channel is described below.

| Channel  | TextContent | FileContent | TemplateContent |
| -------- | :---------: | :---------: | :-------------: |
| SMS      |      X      |             |                 |
| WhatsApp |      X      |      X      |        X        |
| Facebook |      X      |      X      |                 |

### Receiving message and message status events

Use the `WebhookController` class to create your webhook to receive message and message status events. The default port is `8080`.

If you inform the `client`, `url`, and `channel` fields, a subscription will be created if it does not exist for these configurations.

In the `messageEventHandler` field you will receive the message events and in the `messageStatusEventHandler` field you will receive the message status events.

```java
Client client = new Client("YOUR_API_TOKEN");
WebhookController webhook = new WebhookController(
  new ResourceConfig(), //or an instance of RequestMappingHandlerMapping when using Spring Wev MVC version
  (MessageEvent messageEvent) -> {
    System.out.println("Message event:" + messageEvent);
  },
  (MessageStatusEvent messageStatusEvent) -> {
    System.out.println("Message status event:" + messageStatusEvent);
  },
  client,
  "https://my-webhook.company.com",
  ChannelType.whatsapp
);
webhook.init();
```

## Contributing

Pull requests are always welcome!

Please see the [Contributors' Guide](CONTRIBUTING.md) for more information on contributing.

## License

[MIT](LICENSE.md)
