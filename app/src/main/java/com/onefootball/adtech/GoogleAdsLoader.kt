package com.onefootball.adtech

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.gms.ads.formats.AdManagerAdViewOptions
import com.onefootball.model.Ad
import kotlinx.coroutines.suspendCancellableCoroutine

class GoogleAdsLoader(private val context: Context) {

    suspend fun loadAd(ads: Ad): View? = suspendCancellableCoroutine { coroutine ->

        val googleAdRequest = AdManagerAdRequest.Builder().build()

        AdLoader.Builder(context, ads.adId)
            .withAdManagerAdViewOptions(AdManagerAdViewOptions.Builder().build())
            .forAdManagerAdView(
                { ad: AdManagerAdView ->
                    val bannerAd = FrameLayout(context)
                    bannerAd.addView(ad)
                    coroutine.resume(bannerAd) {}
                },
                *ads.adSizes.toTypedArray(),
            )
            .withAdListener(
                object : AdListener() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        coroutine.resume(null) {}
                    }
                }
            )
            .build()
            .loadAd(googleAdRequest)
    }
}