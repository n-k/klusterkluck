# FunctionsApi

All URIs are relative to *http://localhost/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create**](FunctionsApi.md#create) | **POST** /api/v1/functions | create
[**delete**](FunctionsApi.md#delete) | **DELETE** /api/v1/functions/{id} | delete
[**get**](FunctionsApi.md#get) | **GET** /api/v1/functions/{id} | get
[**getService**](FunctionsApi.md#getService) | **GET** /api/v1/functions/{id}/service | getAddress
[**getVersion**](FunctionsApi.md#getVersion) | **GET** /api/v1/functions/{id}/versions/{versionId} | getVersions
[**getVersions**](FunctionsApi.md#getVersions) | **GET** /api/v1/functions/{id}/versions | getVersions
[**list**](FunctionsApi.md#list) | **GET** /api/v1/functions | list
[**proxy**](FunctionsApi.md#proxy) | **POST** /api/v1/functions/{id}/proxy | proxy
[**setVersion**](FunctionsApi.md#setVersion) | **PUT** /api/v1/functions/{id}/versions/{versionId} | setVersion


<a firstName="create"></a>
# **create**
> KFFunction create(body)

create



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FunctionsApi;


FunctionsApi apiInstance = new FunctionsApi();
CreateFunctionRequest body = new CreateFunctionRequest(); // CreateFunctionRequest | 
try {
    KFFunction result = apiInstance.create(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FunctionsApi#create");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateFunctionRequest**](CreateFunctionRequest.md)|  | [optional]

### Return type

[**KFFunction**](KFFunction.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a firstName="delete"></a>
# **delete**
> delete(id)

delete



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FunctionsApi;


FunctionsApi apiInstance = new FunctionsApi();
String id = "id_example"; // String | 
try {
    apiInstance.delete(id);
} catch (ApiException e) {
    System.err.println("Exception when calling FunctionsApi#delete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a firstName="get"></a>
# **get**
> KFFunction get(id)

get



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FunctionsApi;


FunctionsApi apiInstance = new FunctionsApi();
String id = "id_example"; // String | 
try {
    KFFunction result = apiInstance.get(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FunctionsApi#get");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |

### Return type

[**KFFunction**](KFFunction.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a firstName="getService"></a>
# **getService**
> Service getService(id)

getAddress



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FunctionsApi;


FunctionsApi apiInstance = new FunctionsApi();
String id = "id_example"; // String | 
try {
    Service result = apiInstance.getService(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FunctionsApi#getService");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |

### Return type

[**Service**](Service.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a firstName="getVersion"></a>
# **getVersion**
> Version getVersion(id, versionId)

getVersions



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FunctionsApi;


FunctionsApi apiInstance = new FunctionsApi();
String id = "id_example"; // String | 
String versionId = "versionId_example"; // String | 
try {
    Version result = apiInstance.getVersion(id, versionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FunctionsApi#getVersion");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |
 **versionId** | **String**|  |

### Return type

[**Version**](Version.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a firstName="getVersions"></a>
# **getVersions**
> List&lt;Version&gt; getVersions(id)

getVersions



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FunctionsApi;


FunctionsApi apiInstance = new FunctionsApi();
String id = "id_example"; // String | 
try {
    List<Version> result = apiInstance.getVersions(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FunctionsApi#getVersions");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |

### Return type

[**List&lt;Version&gt;**](Version.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a firstName="list"></a>
# **list**
> List&lt;KFFunction&gt; list()

list



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FunctionsApi;


FunctionsApi apiInstance = new FunctionsApi();
try {
    List<KFFunction> result = apiInstance.list();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FunctionsApi#list");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;KFFunction&gt;**](KFFunction.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a firstName="proxy"></a>
# **proxy**
> ProxyResponse proxy(id, body)

proxy



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FunctionsApi;


FunctionsApi apiInstance = new FunctionsApi();
String id = "id_example"; // String | 
String body = "body_example"; // String | 
try {
    ProxyResponse result = apiInstance.proxy(id, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FunctionsApi#proxy");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |
 **body** | **String**|  | [optional]

### Return type

[**ProxyResponse**](ProxyResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a firstName="setVersion"></a>
# **setVersion**
> Map&lt;String, String&gt; setVersion(id, versionId)

setVersion



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FunctionsApi;


FunctionsApi apiInstance = new FunctionsApi();
String id = "id_example"; // String | 
String versionId = "versionId_example"; // String | 
try {
    Map<String, String> result = apiInstance.setVersion(id, versionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FunctionsApi#setVersion");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |
 **versionId** | **String**|  |

### Return type

[**Map&lt;String, String&gt;**](Map.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

