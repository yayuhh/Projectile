package com.nicktate;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.nicktate.projectile.Projectile;
import com.nicktate.projectile.ResponseListener;
import com.nicktate.projectile.StringListener;

import java.io.UnsupportedEncodingException;

public class Sample extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        Projectile.draw(this).aim("http://www.google.com")
                .timeout(5000)
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
