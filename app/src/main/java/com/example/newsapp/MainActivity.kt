package com.example.newsapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import androidx.browser.customtabs.CustomTabColorSchemeParams

import android.graphics.Color

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = NewsRVAdapter(this)
        recyclerView.adapter = mAdapter
        fetchData()
    }

    private fun fetchData(){

//        val url = "https://api.mediastack.com/v1/news?access_key=893543f1ca723b1f542387b82060e65c"
        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=c4b8366a7ac944e3bb8978d2c75f488b"
        val jsonObjectRequest = object: JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val newsArray = ArrayList<News>()
                val newsJsonArray = response.getJSONArray("articles")
//                val newsJsonArray = response.getJSONArray("data")

                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val newsObject = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("description"),
                        newsJsonObject.getString("url"),
//                        newsJsonObject.getString("image")
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(newsObject)
                }
                mAdapter.updateNews(newsArray)
            },
            { error ->
                Toast.makeText(applicationContext, "Unable to Load news.", Toast.LENGTH_LONG).show()
                Log.d("error", "" + error)
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"]="Mozilla/5.0"
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()

        val colorInt = Color.parseColor("#FF7C4DFF")
        val defaultColors = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(colorInt)
            .build()
        builder.setDefaultColorSchemeParams(defaultColors)

        builder.setStartAnimations(this, R.anim.fade, R.anim.empty);

        val customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }
}