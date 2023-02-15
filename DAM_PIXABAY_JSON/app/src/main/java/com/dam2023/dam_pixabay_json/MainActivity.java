package com.dam2023.dam_pixabay_json;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*

    1] POUR AVOIR UNE CLEF pixabay, FAIRE SIGN UP

        https://pixabay.com/api/docs/

        EXEMPLE DE JSON :

         "hits":[
            {
                "id":2295434,
                "pageURL":"https://pixabay.com/photos/spring-bird-bird-tit-spring-blue-2295434/",
                "type":"photo",
                "webformatURL":"https://pixabay.com/get/g4c...ea9_640.jpg",
                "webformatWidth":640,
                ...
                "imageSize":2938651,
                "likes":1988,
            },

    2] ITEM -> add layout / root element CARDVIEW

    3] librairies GRADLE

        ...
        implementation 'androidx.recyclerview:recyclerview:1.2.1'
        implementation 'androidx.cardview:cardview:1.0.0'
        ...

    4] GESTION IMAGES     https://github.com/bumptech/glide

    5] faire holder, model et dapter

    6] remplir Mainactivity

    */

    private RecyclerView recyclerView;
    private AdapterRecycler adapterRecycler;
    private ArrayList<ModelItem> itemArrayList;
    private RequestQueue requestQueue;
    private String search;

    public void initUI() {
        // lien designer
        recyclerView = findViewById(R.id.rvPixaBayRecyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

//        testHttp();
        parseJSON();
        //new RequestTask().execute("http://google.com");
    }


    private void parseJSON() {
        search = "moto";

        String pixabayKey = "33662212-c2c62294c592b2fedfeac70ad"; // TRI
        //String pixabayKey = "33669268-565fad73ad079dae3dee1fc28"; // INAN

        String urlJSONPixabay = "https://pixabay.com/api/?key=" + pixabayKey +
                "&q=" + search +
                "&image_type=photo" +
                "&orientation=horizontal" +
                "&pretty=true";

        //urlJSONPixabay = "https://yahoo.com";

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

                            adapterRecycler = new AdapterRecycler(MainActivity.this, itemArrayList); // Noter MainActivity.this car nous sommes dans une classe interne

                            // Puis on lie l'adapter au Recycler
                            recyclerView.setAdapter(adapterRecycler);

                            /** #10.3 On peut alors ajouter le listener **/
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
/** #9.1 On rempli la request avec les données récupérées **/
        requestQueue.add(request);

    }


    private void testHttp() {
        HttpURLConnection con = null;
        URL url = null;
        try {

            url = new URL("https://yahoo.com");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            Log.i("TAG", content.toString());
            in.close();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);

        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }

    }

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpURLConnection con = null;
            URL url = null;
            StringBuffer content = new StringBuffer();
            try {
                url = new URL("https://yahoo.com");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                Log.i("TAG", content.toString());
                in.close();
            } catch (MalformedURLException e) {
                Log.e("TAG", e.getMessage());

            } catch (IOException e) {
                Log.e("TAG", e.getMessage());
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
        }
    }
}