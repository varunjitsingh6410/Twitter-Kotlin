package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.github.scribejava.apis.TwitterApi
import okhttp3.Headers
import org.json.JSONException

class TimelineActivity : AppCompatActivity() {

    lateinit var client: TwitterClient
    private var TAG = "TimelineActivity"

    lateinit var rvTweets: RecyclerView
    lateinit var adapter: TweetsAdapter
    val tweets =  ArrayList<Tweet>()

    lateinit var swipeToRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client = TwitterApplication.getRestClient(this)

        swipeToRefresh = findViewById(R.id.swipeContainer)

        swipeToRefresh.setOnRefreshListener {
            Log.i(TAG, "Refreshing timeline")
            populateHomeTimeline()
        }

        // Configure the refreshing colors
        swipeToRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(tweets)
        rvTweets.layoutManager = LinearLayoutManager(this)
        rvTweets.adapter = adapter

        populateHomeTimeline()
    }

    fun populateHomeTimeline()
    {
        client.populateHomeTimeline(object : JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "Populate timeline on failure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Populate timeline on success $json")
                val jsonArray = json.jsonArray

                try {
                    adapter.clear()
                    val newTweets = Tweet.fromJsonArray(jsonArray)
                    tweets.addAll(newTweets)
                    adapter.notifyDataSetChanged()
                    swipeToRefresh.setRefreshing(false)
                } catch (e: JSONException)
                {
                    Log.e(TAG, "JSON Exception: $e")
                }
            }

        })
    }
}