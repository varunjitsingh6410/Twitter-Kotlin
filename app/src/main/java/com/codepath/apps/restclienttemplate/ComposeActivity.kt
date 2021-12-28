package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button

    private val TAG = "ComposeActivity"

    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        client = TwitterApplication.getRestClient(this)

        etCompose = findViewById(R.id.etComposeTweet)
        btnTweet = findViewById(R.id.btnTweet)

        btnTweet.setOnClickListener {
            val tweetContent = etCompose.text.toString()

            if (tweetContent.isEmpty())
            {
                Toast.makeText(this, "Empty tweets not allowed", Toast.LENGTH_SHORT).show()
            }
            else if (tweetContent.length > 140)
            {
                Toast.makeText(this, "Tweet can't exceed 140 characters", Toast.LENGTH_SHORT).show()
            }
            else
            {

                //Toast.makeText(this, "Tweet is: $tweetContent", Toast.LENGTH_SHORT).show()
                client.publishTweet(tweetContent, object: JsonHttpResponseHandler() {
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish tweet")
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        Log.i(TAG, "Tweet was published")

                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                })
            }

        }
    }
}