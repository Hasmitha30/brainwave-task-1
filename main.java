package com.example.newsreaderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NewsAdapter adapter;
    ArrayList<NewsModel> newsList;
    String url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=YOUR_API_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsList = new ArrayList<>();
        adapter = new NewsAdapter(this, newsList);
        recyclerView.setAdapter(adapter);

        fetchNews();
    }

    private void fetchNews() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray articles = response.getJSONArray("articles");
                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject obj = articles.getJSONObject(i);
                            String title = obj.getString("title");
                            String desc = obj.getString("description");
                            String imageUrl = obj.getString("urlToImage");
                            String newsUrl = obj.getString("url");

                            newsList.add(new NewsModel(title, desc, imageUrl, newsUrl));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Error fetching news!", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }
}
