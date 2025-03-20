package com.onefootball.model

import android.view.View
import com.google.android.gms.ads.AdSize


open class NewsItem(open var type: ListViewType?)

data class News(
    val title: String,
    val image_url: String,
    val resource_name: String,
    val resource_url: String,
    val news_link: String,
    val id: String,
) : NewsItem(ListViewType.NEWS)

data class Ad(
    val adId: String,
    val adSizes: List<AdSize>,
    val id: String,
    val adView: View? = null,
    val adState: AdState? = null,
) : NewsItem(ListViewType.AD)

enum class ListViewType {
    NEWS,
    AD
}
enum class AdState {
    LOADING,
    LOADED
}


data class NewsResponse(
    val news: List<News>
)


