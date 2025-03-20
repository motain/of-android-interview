package com.onefootball.home

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onefootball.R
import com.onefootball.adtech.GoogleAdsLoader
import kotlinx.coroutines.launch

class MyNewsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: MyNewsViewModel = ViewModelProvider(
            this,
            ViewModelFactory(application, GoogleAdsLoader(this))
        )[MyNewsViewModel::class.java]

        recyclerView = findViewById(R.id.newsRecyclerView)
        myAdapter = NewsAdapter {
            viewModel.loadAd(it)
        }
        with(recyclerView) {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(this@MyNewsActivity)
        }

        lifecycleScope.launch {
            viewModel.news.collect {
                myAdapter.setNewsItems(it)
            }
        }

        viewModel.getNewsItems()
    }
}

class ViewModelFactory(
    private val application: Application,
    private val googleAdsLoader: GoogleAdsLoader
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyNewsViewModel(
            application,
            googleAdsLoader
        ) as T
    }
}
