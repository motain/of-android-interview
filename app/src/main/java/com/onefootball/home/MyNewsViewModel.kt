package com.onefootball.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.ads.AdSize
import com.google.gson.Gson
import com.onefootball.adtech.GoogleAdsLoader
import com.onefootball.model.Ad
import com.onefootball.model.AdState
import com.onefootball.model.ListViewType
import com.onefootball.model.News
import com.onefootball.model.NewsItem
import com.onefootball.model.NewsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.nio.charset.Charset

class MyNewsViewModel(
    private val application: Application,
    private val googleAdsLoader: GoogleAdsLoader
) : AndroidViewModel(application) {

    private val _news = MutableStateFlow<List<NewsItem>>(emptyList())
    val news = _news.asStateFlow()

    private var jsonString: String? = null

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun getNewsItems() {
        coroutineScope.launch(Dispatchers.IO) {
            val inputStream = application.assets.open("news.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            jsonString = buffer.toString(Charset.defaultCharset())

            val items = parseJsonString(jsonString!!)
            _news.value = buildList {
                items.forEach { it.type = ListViewType.NEWS }
                addAll(items)
                add(
                    2, Ad(
                        "/38577695/parent_test_code/child_test_code",
                        listOf(AdSize(300, 250)),
                        "ad1"
                    ).also {
                        it.type = ListViewType.AD
                    }
                )
            }
        }
    }

    private fun parseJsonString(jsonString: String): List<News> {
        return Gson().fromJson(jsonString, NewsResponse::class.java).news
    }

    fun loadAd(ad: Ad) {
        coroutineScope.launch {
            val view = googleAdsLoader.loadAd(ad)
            _news.value = _news.value.map {
                if (it is Ad) {
                    it.copy(adView = view, adState = AdState.LOADED)
                } else {
                    it
                }
            }
        }
    }
}