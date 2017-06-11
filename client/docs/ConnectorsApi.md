# ConnectorsApi

All URIs are relative to *http://localhost/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**get**](ConnectorsApi.md#get) | **GET** /api/v1/connectors/{id} | get
[**list**](ConnectorsApi.md#list) | **GET** /api/v1/connectors | list


<a name="get"></a>
# **get**
> Connector get(id)

get



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.ConnectorsApi;


ConnectorsApi apiInstance = new ConnectorsApi();
String id = "id_example"; // String | 
try {
    Connector result = apiInstance.get(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConnectorsApi#get");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |

### Return type

[**Connector**](Connector.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="list"></a>
# **list**
> List&lt;Connector&gt; list()

list



### Example
```java
// Import classes:
//import com.github.nk.klusterfuck.ApiException;
//import com.github.nk.klusterfuck.client.ConnectorsApi;


ConnectorsApi apiInstance = new ConnectorsApi();
try {
    List<Connector> result = apiInstance.list();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConnectorsApi#list");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;Connector&gt;**](Connector.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

