# swagger-java-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-java-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-java-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-java-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import io.swagger.client.*;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import io.swagger.client.api.FunctionsApi;

import java.io.File;
import java.util.*;

public class FunctionsApiExample {

    public static void main(String[] args) {
        
        FunctionsApi apiInstance = new FunctionsApi();
        CreateFunctionRequest body = new CreateFunctionRequest(); // CreateFunctionRequest | 
        try {
            KFFunction result = apiInstance.create(body);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling FunctionsApi#create");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://localhost/*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*FunctionsApi* | [**create**](docs/FunctionsApi.md#create) | **POST** /api/v1/functions | create
*FunctionsApi* | [**delete**](docs/FunctionsApi.md#delete) | **DELETE** /api/v1/functions/{id} | delete
*FunctionsApi* | [**get**](docs/FunctionsApi.md#get) | **GET** /api/v1/functions/{id} | get
*FunctionsApi* | [**getService**](docs/FunctionsApi.md#getService) | **GET** /api/v1/functions/{id}/service | getAddress
*FunctionsApi* | [**getVersion**](docs/FunctionsApi.md#getVersion) | **GET** /api/v1/functions/{id}/versions/{versionId} | getVersions
*FunctionsApi* | [**getVersions**](docs/FunctionsApi.md#getVersions) | **GET** /api/v1/functions/{id}/versions | getVersions
*FunctionsApi* | [**list**](docs/FunctionsApi.md#list) | **GET** /api/v1/functions | list
*FunctionsApi* | [**proxy**](docs/FunctionsApi.md#proxy) | **POST** /api/v1/functions/{id}/proxy | proxy
*FunctionsApi* | [**setVersion**](docs/FunctionsApi.md#setVersion) | **PUT** /api/v1/functions/{id}/versions/{versionId} | setVersion


## Documentation for Models

 - [CreateFunctionRequest](docs/CreateFunctionRequest.md)
 - [IntOrString](docs/IntOrString.md)
 - [KFFunction](docs/KFFunction.md)
 - [LoadBalancerIngress](docs/LoadBalancerIngress.md)
 - [LoadBalancerStatus](docs/LoadBalancerStatus.md)
 - [ObjectMeta](docs/ObjectMeta.md)
 - [OwnerReference](docs/OwnerReference.md)
 - [ProxyResponse](docs/ProxyResponse.md)
 - [Service](docs/Service.md)
 - [ServicePort](docs/ServicePort.md)
 - [ServiceSpec](docs/ServiceSpec.md)
 - [ServiceStatus](docs/ServiceStatus.md)
 - [Version](docs/Version.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author



