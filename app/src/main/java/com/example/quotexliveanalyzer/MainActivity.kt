package com.example.quotexliveanalyzer

import android.app.Activity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = WebView(this)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        webView.loadUrl("https://quotex.io/")

        setContentView(webView)
    }

    override fun onBackPressed() {
        val view = findViewById<WebView>(android.R.id.content)
        if (view?.canGoBack() == true) {
            view.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
