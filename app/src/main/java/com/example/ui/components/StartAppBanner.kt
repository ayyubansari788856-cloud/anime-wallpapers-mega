package com.example.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.startapp.sdk.ads.banner.Banner
import com.startapp.sdk.ads.banner.BannerListener
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.startapp.sdk.adsbase.adlisteners.AdEventListener

object StartAppConfig {
    private const val PREFS_NAME = "startapp_prefs"
    private const val KEY_APP_ID = "startapp_app_id"
    const val DEFAULT_APP_ID = "208180681" // Anime Wallpapers Start.io App ID
    const val AURA_APP_ID = "209978529"    // Aura Quest Start.io App ID

    fun getAppId(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_APP_ID, DEFAULT_APP_ID) ?: DEFAULT_APP_ID
    }

    fun saveAppId(context: Context, newId: String) {
        val cleanId = newId.trim().ifEmpty { DEFAULT_APP_ID }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_APP_ID, cleanId).apply()
        initialize(context, cleanId)
    }

    fun initialize(context: Context, customId: String? = null) {
        try {
            val appId = customId ?: getAppId(context)
            StartAppSDK.init(context, appId, false)
            StartAppSDK.enableReturnAds(false)
            Log.d("StartAppConfig", "StartApp SDK initialized successfully with App ID: $appId")
        } catch (e: Exception) {
            Log.e("StartAppConfig", "StartApp initialization failed: ${e.message}")
        }
    }
}

@Composable
fun StartAppBannerView(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { ctx ->
                Banner(ctx).apply {
                    setBannerListener(object : BannerListener {
                        override fun onReceiveAd(bannerView: View?) {
                            Log.d("StartAppBanner", "Banner ad loaded successfully")
                        }

                        override fun onFailedToReceiveAd(bannerView: View?) {
                            Log.w("StartAppBanner", "Failed to receive banner ad")
                        }

                        override fun onClick(bannerView: View?) {
                            Log.d("StartAppBanner", "Banner ad clicked")
                        }

                        override fun onImpression(bannerView: View?) {
                            Log.d("StartAppBanner", "Banner ad impression recorded")
                        }
                    })
                    loadAd()
                }
            }
        )
    }
}

object InterstitialAdHelper {
    private var interstitialAd: StartAppAd? = null
    private var actionCount = 0

    fun loadAd(context: Context) {
        try {
            if (interstitialAd == null) {
                interstitialAd = StartAppAd(context)
            }
            interstitialAd?.loadAd(StartAppAd.AdMode.AUTOMATIC, object : AdEventListener {
                override fun onReceiveAd(ad: Ad) {
                    Log.d("StartAppInterstitial", "Interstitial ad loaded")
                }

                override fun onFailedToReceiveAd(ad: Ad?) {
                    Log.w("StartAppInterstitial", "Failed to load interstitial ad")
                }
            })
        } catch (e: Exception) {
            Log.e("StartAppInterstitial", "Error loading interstitial: ${e.message}")
        }
    }

    fun showAd(context: Context, onDismiss: () -> Unit = {}) {
        val activity = context.findActivity()
        if (activity != null) {
            val ad = interstitialAd ?: StartAppAd(activity).also { interstitialAd = it }
            ad.showAd(object : AdDisplayListener {
                override fun adHidden(ad: Ad) {
                    onDismiss()
                    loadAd(context)
                }

                override fun adDisplayed(ad: Ad) {
                    Log.d("StartAppInterstitial", "Interstitial ad displayed")
                }

                override fun adClicked(ad: Ad) {
                    Log.d("StartAppInterstitial", "Interstitial ad clicked")
                }

                override fun adNotDisplayed(ad: Ad) {
                    onDismiss()
                    loadAd(context)
                }
            })
        } else {
            onDismiss()
        }
    }

    fun showAdEveryXActions(context: Context, interval: Int = 2, onDismiss: () -> Unit = {}) {
        actionCount++
        if (actionCount % interval == 0) {
            showAd(context, onDismiss)
        } else {
            onDismiss()
        }
    }

    private fun Context.findActivity(): Activity? {
        var current = this
        while (current is ContextWrapper) {
            if (current is Activity) return current
            current = current.baseContext
        }
        return null
    }
}
