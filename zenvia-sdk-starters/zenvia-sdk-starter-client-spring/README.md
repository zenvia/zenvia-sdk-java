# Spring Boot Starter for Zenvia Client with Spring

[![License](https://img.shields.io/github/license/zenvia/zenvia-sdk-java.svg)](LICENSE.md)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.zenvia/zenvia-api-sdk-starter-client-spring/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.zenvia/zenvia-api-sdk-starter-client-spring/)

Spring Boot Starter module will enable your Spring Boot application to work with Zenvia CPaaS SDK Client with Spring.

## What you need

- An [Zenvia account](https://www.zenvia.com/)
- An [api token](https://app.zenvia.com/home/api)

## Include the dependency

For Apache Maven:

```xml
<dependency>
	<groupId>com.zenvia</groupId>
	<artifactId>zenvia-api-sdk-starter-client-spring</artifactId>
	<version>1.1.0</version>
</dependency>
```

For Gradle:

```groovy
dependencies {
    implementation group: 'com.zenvia', name: 'zenvia-api-sdk-starter-client-spring', version: '1.1.0'
}
```

### Configure your properties

You can configure your applications properties with environment variables, system properties, or configuration files. Take a look at the [Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) for more details.

The property `apiToken` is required to the Client be auto-created.

| Property                                             | Default                | Details                                                                                                                                                                   |
| ---------------------------------------------------- | ---------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| zenvia.api.sdk.client.apiToken                       | N/A                    | Your [api token](https://app.zenvia.com/home/api)                                                                                                                         |
| zenvia.api.sdk.client.apiUrl                         | https://api.zenvia.com | The URL for the API service. Usually the default value is used, but change it can be useful for testing                                                                   |
| zenvia.api.sdk.client.maxConnections                 | 100                    | The maximum number of connections the pool can have                                                                                                                       |
| zenvia.api.sdk.client.connectionTimeout              | 25000                  | The amount of time in milliseconds for a connection attempt to timeout                                                                                                    |
| zenvia.api.sdk.client.socketTimeout                  | 60000                  | The amount of time in milliseconds for a server reply to timeout                                                                                                          |
| zenvia.api.sdk.client.maxConnectionRetries           | 4                      | The maximum amount of connection retries automatically made by the HTTP client in case of connection failure                                                              |
| zenvia.api.sdk.client.connectionPoolTimeout          | 0                      | The amount of time in milliseconds for a request to timeout when wait for a free connection from the pool. When zero, it means it will wait indefinitely for a connection |
| zenvia.api.sdk.client.inactivityTimeBeforeStaleCheck | 5000                   | The amount of time in milliseconds of inactivity necessary to trigger a stale check on idle pool connections                                                              |
