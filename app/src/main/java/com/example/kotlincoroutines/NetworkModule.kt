package com.example.kotlincoroutines

import com.simplymadeapps.preferencehelper.PreferenceHelper
import com.squareup.moshi.Moshi
import dagger.Module

/**
 * @Module : This is used on the classes that create the objects of dependency classes.
 * **/
@Module
class NetworkModule {

    fun provideOkHttpClient(preferenceHelper: PreferenceHelper, moshi: Moshi){

    }
}