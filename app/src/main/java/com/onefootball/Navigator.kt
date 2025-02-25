package com.onefootball

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.onefootball.model.News

class Navigator(private val context: Context) {
    fun openNews(news: News) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(news.news_link))
        )
    }
}
