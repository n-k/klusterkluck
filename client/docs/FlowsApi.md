# FlowsApi

All URIs are relative to *http://localhost/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create**](FlowsApi.md#create) | **POST** /api/v1/flows | create
[**delete**](FlowsApi.md#delete) | **DELETE** /api/v1/flows/{id} | delete
[**deploy**](FlowsApi.md#deploy) | **POST** /api/v1/flows/{id}/deploy | deploy
[**get**](FlowsApi.md#get) | **GET** /api/v1/flows/{id} | get
[**getModel**](FlowsApi.md#getModel) | **GET** /api/v1/flows/{id}/model | getModel
[**list**](FlowsApi.md#list) | **GET** /api/v1/flows | list
[**saveModel**](FlowsApi.md#saveModel) | **POST** /api/v1/flows/{id}/model | saveModel
[**validate**](FlowsApi.md#validate) | **POST** /api/v1/flows/validate | validate


<a name="create"></a>
# **create**
> Flow create(body)

create



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FlowsApi;


FlowsApi apiInstance = new FlowsApi();
CreateFlowRequest body = new CreateFlowRequest(); // CreateFlowRequest | 
try {
    Flow result = apiInstance.create(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FlowsApi#create");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateFlowRequest**](CreateFlowRequest.md)|  | [optional]

### Return type

[**Flow**](Flow.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="delete"></a>
# **delete**
> String delete(id)

delete



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FlowsApi;


FlowsApi apiInstance = new FlowsApi();
String id = "id_example"; // String | 
try {
    String result = apiInstance.delete(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FlowsApi#delete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="deploy"></a>
# **deploy**
> Flow deploy(id)

deploy



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FlowsApi;


FlowsApi apiInstance = new FlowsApi();
String id = "id_example"; // String | 
try {
    Flow result = apiInstance.deploy(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FlowsApi#deploy");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |

### Return type

[**Flow**](Flow.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="get"></a>
# **get**
> Flow get(id)

get



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FlowsApi;


FlowsApi apiInstance = new FlowsApi();
String id = "id_example"; // String | 
try {
    Flow result = apiInstance.get(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FlowsApi#get");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |

### Return type

[**Flow**](Flow.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="getModel"></a>
# **getModel**
> DAGStepRef getModel(id)

getModel



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FlowsApi;


FlowsApi apiInstance = new FlowsApi();
String id = "id_example"; // String | 
try {
    DAGStepRef result = apiInstance.getModel(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FlowsApi#getModel");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |

### Return type

[**DAGStepRef**](DAGStepRef.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="list"></a>
# **list**
> List&lt;Flow&gt; list()

list



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FlowsApi;


FlowsApi apiInstance = new FlowsApi();
try {
    List<Flow> result = apiInstance.list();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FlowsApi#list");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;Flow&gt;**](Flow.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="saveModel"></a>
# **saveModel**
> String saveModel(id, body)

saveModel



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FlowsApi;


FlowsApi apiInstance = new FlowsApi();
String id = "id_example"; // String | 
Object body = null; // Object | 
try {
    String result = apiInstance.saveModel(id, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FlowsApi#saveModel");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |
 **body** | **Object**|  | [optional]

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="validate"></a>
# **validate**
> String validate(body)

validate



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.FlowsApi;


FlowsApi apiInstance = new FlowsApi();
Object body = null; // Object | 
try {
    String result = apiInstance.validate(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FlowsApi#validate");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | **Object**|  | [optional]

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

