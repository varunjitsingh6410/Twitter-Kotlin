package com.codepath.apps.restclienttemplate

import android.app.Application
import android.content.Context
import com.codepath.oauth.OAuthBaseClient

/*
* This is the Android application itself and is used to configure various settings
* including the image cache in memory and on disk. This also adds a singleton
* for accessing the relevant rest client.
*
*     RestClient client = RestApplication.getRestClient(Context context);
*     // use client to send requests to API
*
*/
class TwitterApplication : Application() {

    var myDatabase: MyDatabase? = null

    override fun onCreate() {
        super.onCreate()
        // when upgrading versions, kill the original tables by using
        // fallbackToDestructiveMigration()
//        myDatabase = Room.databaseBuilder(
//            this, MyDatabase::class.java,
//            MyDatabase.NAME
//        ).fallbackToDestructiveMigration().build()
//
//        // use chrome://inspect to inspect your SQL database
//        Stetho.initializeWithDefaults(this)
    }

    companion object {
        fun getRestClient(context: Context): TwitterClient {
            return OAuthBaseClient.getInstance(TwitterClient::class.java, context) as TwitterClient
        }
    }
}