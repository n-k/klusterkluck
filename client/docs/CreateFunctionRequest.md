
# CreateFunctionRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**name** | **String** |  |  [optional]
**template** | **String** |  |  [optional]
**serviceType** | [**ServiceTypeEnum**](#ServiceTypeEnum) |  |  [optional]
**ingress** | **Boolean** |  |  [optional]
**host** | **String** |  |  [optional]
**path** | **String** |  |  [optional]


<a name="ServiceTypeEnum"></a>
## Enum: ServiceTypeEnum
Name | Value
---- | -----
CLUSTERIP | &quot;ClusterIP&quot;
NODEPORT | &quot;NodePort&quot;



