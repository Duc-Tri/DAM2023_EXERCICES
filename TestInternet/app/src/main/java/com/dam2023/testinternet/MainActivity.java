package com.dam2023.testinternet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //new RequestTask().execute("https://yahoo.com");
        //volleyHttp();
        requestQueue = Volley.newRequestQueue(this);

        parseJSON();
    }


    private void parseJSON() {
        String search = "moto";

        String pixabayKey = "33662212-c2c62294c592b2fedfeac70ad"; // TRI
        //String pixabayKey = "33669268-565fad73ad079dae3dee1fc28"; // INAN

        String urlJSONPixabay = "https://pixabay.com/api/?key=" + pixabayKey +
                "&q=" + search +
                "&image_type=photo" +
                //"&orientation=horizontal" +
                "&pretty=true";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlJSONPixabay, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // On récupère le tableau de données JSON à partir de notre objet JsonObjectRequest
                            // dans un try and catch ajouter en auto en corrigeant l'erreur
                            JSONArray jsonArray = response.getJSONArray("hits");

                            // On récupère dans un premier temps toutes les données présentent dans le Array avec une boucle for
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                // Puis on sélectionne celles dn on à besoin soit user - likes - webformatURL
                                String creator = hit.getString("user");
                                int likes = hit.getInt("likes");
                                String imageUrl = hit.getString("webformatURL");

                                Log.i("TAG", "imageUrl■ " + imageUrl);
                                // On ajoute les données à notre tableau en utilisant son model
                                ////////////itemArrayList.add(new ModelItem(imageUrl, creator, likes));
                            }

                            /////adapterRecycler = new AdapterRecycler(MainActivity.this, itemArrayList); // Noter MainActivity.this car nous sommes dans une classe interne
                            // Puis on lie l'adapter au Recycler
                            ////recyclerView.setAdapter(adapterRecycler);
                            // #10.3 On peut alors ajouter le listener
                            ////adapterRecycler.setOnItemClickListener(MainActivity.this);

                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // #9.1 On rempli la request avec les données récupérées
        requestQueue.add(request);

    }


    public void volleyHttp() {

        RequestQueue requestQueue;

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

// Start the queue
        requestQueue.start();

        String url = "http://www.example.com";

        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        Log.i("TAG", "onResponse■ " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("TAG", "onErrorResponse■ " + error.getMessage());
                    }
                });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);

    }

/*

    private void parseJSON() {
        String search = "moto";

        String pixabayKey = "33662212-c2c62294c592b2fedfeac70ad"; // TRI
        //String pixabayKey = "33669268-565fad73ad079dae3dee1fc28"; // INAN

        String urlJSONPixabay = "https://pixabay.com/api/?key=" + pixabayKey +
                "&q=" + search +
                "&image_type=photo" +
                //"&orientation=horizontal" +
                "&pretty=true";

        urlJSONPixabay="https://yahoo.com";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlJSONPixabay, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // On récupère le tableau de données JSON à partir de notre objet JsonObjectRequest
                            // dans un try and catch ajouter en auto en corrigeant l'erreur
                            JSONArray jsonArray = response.getJSONArray("hits");

                            // On récupère dans un premier temps toutes les données présentent dans le Array avec une boucle for
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                // Puis on sélectionne celles dn on à besoin soit user - likes - webformatURL
                                String creator = hit.getString("user");
                                int likes = hit.getInt("likes");
                                String imageUrl = hit.getString("webformatURL");

                                // On ajoute les données à notre tableau en utilisant son model
                                itemArrayList.add(new ModelItem(imageUrl, creator, likes));
                            }

                            ///// adapterRecycler = new AdapterRecycler(MainActivity.this, itemArrayList); // Noter MainActivity.this car nous sommes dans une classe interne

                            // Puis on lie l'adapter au Recycler
                            /////////recyclerView.setAdapter(adapterRecycler);

                            // #10.3 On peut alors ajouter le listener
                            ////adapterRecycler.setOnItemClickListener(MainActivity.this);

                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
// #9.1 On rempli la request avec les données récupérées
        requestQueue.add(request);

    }
    */


}