Projectile
==========
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

## Configurable parameters
| Key               | Description                                            |
|:------------------|:-------------------------------------------------------|
| method            | http method                                            |
| priority          | priority for volley to set on the request              |
| network timeout   | time in milliseconds for a request to timeout          |
| retry count       | number of times to retry the request                   |
| headers           | optional headers to send with request                  |
| params            | parameters to add to request (query or payload depending on method type |
