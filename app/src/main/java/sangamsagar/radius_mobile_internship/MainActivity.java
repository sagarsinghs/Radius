package sangamsagar.radius_mobile_internship;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sangamsagar.radius_mobile_internship.Adapter.DetailsAdapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Model> movieList;
    public RecyclerView recyclerView;
    public DetailsAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new DetailsAdapter(movieList,MainActivity.this);

        //method to extract the details of the user
        getDetailsName_age_Image();
    }

    public  void getDetailsName_age_Image() {

        String JsonURL = "https://raw.githubusercontent.com/iranjith4/radius-intern-mobile/master/users.json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JsonURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.d(TAG, "Register response :" + response.toString());

                try {

                    JSONObject jsonObject = new JSONObject(response.trim());
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject j1 = jsonArray.getJSONObject(i);
                        JSONObject j2 = j1.getJSONObject("name");
                        String name = j2.getString("title");
                        String title = j2.getString("first");
                        String last = j2.getString("last");

                        Log.d("PrintingValues", name + title + last);


                        JSONObject j3 = j1.getJSONObject("dob");
                        String date = j3.getString("age");


                        JSONObject j4 = j1.getJSONObject("picture");
                        String picture_image = j4.getString("large");

                        Model model = new Model(name + " " + last + " " + title, date, picture_image);
                        movieList.add(model);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);

                        Log.d("AgeAndPicture", date + " " + picture_image);

                    }


                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                }
            }
        },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                })
        {

            //This method is for caching the details that is once the data is loaded the data will remain there even when the device is offline.you
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));

                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        // Adds the JSON object request "obreq" to the request queue
        AppSingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest, "cancel");
    }
}
