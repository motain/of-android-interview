package com.onefootball.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.onefootball.R
import com.onefootball.model.Ad
import com.onefootball.model.AdState
import com.onefootball.model.ListViewType
import com.onefootball.model.News
import com.onefootball.model.NewsItem
import com.onefootball.utils.Navigator

class NewsAdapter(
    private val loadAds: (Ad) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val newsItems = mutableListOf<NewsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ListViewType.AD.ordinal -> AdViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.ad_item, parent, false)
            )

            else -> NewsViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (newsItems.isNotEmpty()) {
            newsItems[position].type?.ordinal ?: RecyclerView.INVALID_TYPE
        } else {
            RecyclerView.INVALID_TYPE
        }
    }

    override fun getItemCount() = newsItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val newsItem = newsItems[position]

        when (getItemViewType(position)) {
            ListViewType.NEWS.ordinal -> {
                val newsViewHolder = holder as NewsViewHolder
                (newsItem as? News)?.let {
                    newsViewHolder.titleView.text = newsItem.title
                    newsViewHolder.newsView.load(newsItem.image_url)
                    newsViewHolder.resourceImage.load(newsItem.resource_url)
                    newsViewHolder.resourceName.text = newsItem.resource_name
                    newsViewHolder.itemView.setOnClickListener {
                        Navigator(it.context).openNews(newsItem)
                    }
                }
            }

            ListViewType.AD.ordinal -> {
                val adViewHolder = holder as AdViewHolder
                (newsItem as? Ad)?.let {
                    when (it.adState) {
                        AdState.LOADED -> {
                            if (it.adView != null) {
                                adViewHolder.adContainer.removeAllViews()
                                adViewHolder.adContainer.addView(it.adView)
                                adViewHolder.placeholder.visibility = View.GONE
                                adViewHolder.adContainer.visibility = View.VISIBLE
                            } else {
                                adViewHolder.adContainer.visibility = View.GONE
                                adViewHolder.placeholder.visibility = View.GONE
                            }
                        }

                        AdState.LOADING, null -> {
                            adViewHolder.adContainer.visibility = View.GONE
                            adViewHolder.placeholder.visibility = View.VISIBLE
                            loadAds.invoke(it)
                        }
                    }

                }
            }
        }
    }

    fun setNewsItems(newsItems: List<NewsItem>) {
        this.newsItems.clear()
        this.newsItems.addAll(newsItems)
        notifyDataSetChanged()
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var titleView: TextView = itemView.findViewById(R.id.news_title)
        var newsView: ImageView = itemView.findViewById(R.id.news_view)
        var resourceImage: ImageView = itemView.findViewById(R.id.resource_icon)
        var resourceName: TextView = itemView.findViewById(R.id.resource_name)
    }

    class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var adContainer: FrameLayout = itemView.findViewById(R.id.ad_container)
        var placeholder: FrameLayout = itemView.findViewById(R.id.placeholder)
    }
}
