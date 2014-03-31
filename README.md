<img src="https://raw.github.com/nicktate/projectile/master/images/projectile.png" />

A convenience utility for interfacing with volley and submitting requests. **Projectile** is a simple to use request builder with a focus on easy configuration. Take a look below for some quick examples on how to get started.

Current version: **1.0.x**

## Getting started

```java
public class SampleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // boilerplate...

        Projectile.draw(this).aim("http://www.google.com")
                .fire(new ResponseListener<String>() {
                    @Override
                    public String responseParser(NetworkResponse networkResponse) {
                        String response;
                        try {
                            response = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
                        } catch (UnsupportedEncodingException e) {
                            response = new String(networkResponse.data);
                        }
                        return response;
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(Sample.class.getName(), response);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.d(Sample.class.getName(), "Error");
                    }
                });
    }
}
```

It's configurable to choose any response type you like, you just have to handle parsing the network response you receive back. The library comes built in with three standard response types:
* StringListener - returns a string
* JsonElementListener - returns a `com.google.gson.JsonElement`
* NetworkResponseListener - returns a `com.volley.NetworkResponse`

Thanks to the builder paradigm, you can also configure optional parameters for every request.

```java
public class SampleConfigureActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // boilerplate...

        Projectile.draw(this).aim("http://www.myapiurl/users")
                .method(Method.POST)
                .addParam("user", "nick")
                .addParam("password", "mypassword")
                .timeout(5000)
                .priority(Priority.MEDIUM)
                .fire(new StringListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Sample.class.getName(), response);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.d(Sample.class.getName(), "Error");
                    }
                });
    }
}
```

## Configurable Builder Parameters
| Method          						  	 | Description                    |
|:------------------|:-------------------------------------------------------|
| `method(Method method)`     | http method to be used for the request    |
| `priority(Priority priority)`   | priority for volley to set on the request              |
| `addHeader(String key, String value)`     | add header value to request    |
| `addHeaders(Map<String, String> headers)` | add map of header values to request |   
| `addParam(String key, String value)`     | add request parameter (type depends on HTTP method)    |
| `addParams(Map<String, String> params)` | add map of parameters to request (type depends on HTTP method)|  
| `retryPolicy(RetryPolicy policy)`  | retry policy to use for the request; if set, backoffMultiplier, timeout, and retryCount configureables are ignored           |                                       
| `timeout(int timeout)`   | time in milliseconds for a request to timeout   |
| `retryCount(int count)`       | number of times to retry the request                   |
| `backoffMultiplier(float multiplier)` | backoff multiplier to apply to socket timeout per retry|
| `tag(Object tag)` | tag to set on the request for use in cancelling |
| `shouldCache(boolean cacheRequest)` | determines whether or not the request should be cached |

Download
--------
Maven:
```xml
<dependency>
  <groupId>org.nicktate</groupId>
  <artifactId>projectile</artifactId>
  <version>(insert latest version)</version>
</dependency>
```
or Gradle:
```groovy
compile 'org.nicktate:projectile:(insert latest version)'
```
