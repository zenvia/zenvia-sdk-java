# Spring Boot Starter for Zenvia Webhook with Jersey
[![License](https://img.shields.io/github/license/zenvia/zenvia-sdk-java.svg)](LICENSE.md)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.zenvia/zenvia-api-sdk-starter-webhook-jersey/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.zenvia/zenvia-api-sdk-starter-webhook-jersey/)

Spring Boot Starter module will enable your Spring Boot application to work with Zenvia CPaaS SDK Webhook with Jersey.

## What you need

* An [Zenvia account](https://www.zenvia.com/)
* An [api token](https://app.zenvia.com/home/api)
* An implementation of `MessageEventCallback` and/or `MessageStatusEventCallback` to handle the callback events

## Include the dependency

For Apache Maven:
```xml
<dependency>
	<groupId>com.zenvia</groupId>
	<artifactId>zenvia-api-sdk-starter-webhook-jersey</artifactId>
	<version>1.1.0</version>
</dependency>
```

### Configure your properties

You can configure your applications properties with environment variables, system properties, or configuration files. Take a look at the [Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) for more details.

For webhook configuration, all properties is an optional configuration.

| Property | Default | Details |
|----------|---------|---------|
| zenvia.api.sdk.webhook.path    | /    | The URI path to serve requests for webhook call
| zenvia.api.sdk.webhook.url     | null | URL to be used in subscription creation
| zenvia.api.sdk.webhook.channel | null | Channel to trigger the callbacks for subscription to be created
