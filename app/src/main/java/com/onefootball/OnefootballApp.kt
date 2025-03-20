package com.onefootball

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.google.android.gms.ads.MobileAds

class OnefootballApp : Application(), SingletonImageLoader.Factory {

    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this) {
            // Initialization done
        }
    }
}