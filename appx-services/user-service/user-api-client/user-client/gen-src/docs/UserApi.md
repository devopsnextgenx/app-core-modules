# UserApi

All URIs are relative to *http://localhost:8080/api*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteUserById**](UserApi.md#deleteUserById) | **DELETE** /user/{id} | Delete User By Id |
| [**getUserById**](UserApi.md#getUserById) | **GET** /user/{id} | Get User By Id |
| [**saveUser**](UserApi.md#saveUser) | **POST** /user | Save User |



## deleteUserById

> deleteUserById(id)

Delete User By Id

### Example

```java
// Import classes:
import io.devopsnextgenx.microservices.modules.user.client.ApiClient;
import io.devopsnextgenx.microservices.modules.user.client.ApiException;
import io.devopsnextgenx.microservices.modules.user.client.Configuration;
import io.devopsnextgenx.microservices.modules.user.client.models.*;
import io.devopsnextgenx.microservices.modules.user.client.api.UserApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api");

        UserApi apiInstance = new UserApi(defaultClient);
        UUID id = UUID.randomUUID(); // UUID | Unique Id of an User
        try {
            apiInstance.deleteUserById(id);
        } catch (ApiException e) {
            System.err.println("Exception when calling UserApi#deleteUserById");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **UUID**| Unique Id of an User | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | OK |  -  |


## getUserById

> User getUserById(id)

Get User By Id

### Example

```java
// Import classes:
import io.devopsnextgenx.microservices.modules.user.client.ApiClient;
import io.devopsnextgenx.microservices.modules.user.client.ApiException;
import io.devopsnextgenx.microservices.modules.user.client.Configuration;
import io.devopsnextgenx.microservices.modules.user.client.models.*;
import io.devopsnextgenx.microservices.modules.user.client.api.UserApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api");

        UserApi apiInstance = new UserApi(defaultClient);
        UUID id = UUID.randomUUID(); // UUID | Unique Id of an User
        try {
            User result = apiInstance.getUserById(id);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling UserApi#getUserById");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **UUID**| Unique Id of an User | |

### Return type

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |


## saveUser

> User saveUser(user)

Save User

### Example

```java
// Import classes:
import io.devopsnextgenx.microservices.modules.user.client.ApiClient;
import io.devopsnextgenx.microservices.modules.user.client.ApiException;
import io.devopsnextgenx.microservices.modules.user.client.Configuration;
import io.devopsnextgenx.microservices.modules.user.client.models.*;
import io.devopsnextgenx.microservices.modules.user.client.api.UserApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api");

        UserApi apiInstance = new UserApi(defaultClient);
        User user = new User(); // User | 
        try {
            User result = apiInstance.saveUser(user);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling UserApi#saveUser");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **user** | [**User**](User.md)|  | [optional] |

### Return type

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

