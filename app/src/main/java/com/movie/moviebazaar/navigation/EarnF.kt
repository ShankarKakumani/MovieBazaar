package com.movie.moviebazaar.navigation

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.movie.moviebazaar.R
import com.shankar.customtoast.Toasty
import kotlinx.android.synthetic.main.fragment_earn.*


class EarnF : Fragment(), RewardedVideoAdListener {


    private var mView: View? = null
    lateinit var mAdView : AdView
    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var mRewardedVideoAd: RewardedVideoAd


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_earn, container, false)

        MobileAds.initialize(context) {}
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context)
        mRewardedVideoAd.rewardedVideoAdListener = this

        bannerAds()
        interstitialAds()
        loadVideoAd()
        return mView
    }

    private fun loadVideoAd() {


        //Sample Video ad ID  ca-app-pub-3940256099942544/5224354917
        //Real Video ad ID  ca-app-pub-5248287644539273/1549424190
        mRewardedVideoAd.loadAd("ca-app-pub-5248287644539273/1549424190",
            AdRequest.Builder().build())

    }

    private fun interstitialAds() {

        //Test InterstitialAd ID  ca-app-pub-3940256099942544/1033173712
        //Real InterstitialAd ID  ca-app-pub-5248287644539273/1984716078
        mInterstitialAd = InterstitialAd(context)
        mInterstitialAd.adUnitId = "ca-app-pub-5248287644539273/1984716078"
        mInterstitialAd.loadAd(AdRequest.Builder().build())


        mInterstitialAd.adListener = object: AdListener() {
            override fun onAdLoaded() {
                interstitialAdText.text = " InterstitialAd Loaded Ready to display"
                interstitialAdText.setOnClickListener { mInterstitialAd.show() }
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                interstitialAdText.text = "onAdFailedToLoad ${adError.message}"

            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(AdRequest.Builder().build())
                interstitialAdText.text = " Reloading InterstitialAd...."
            }
        }
    }

    private fun bannerAds() {


        mAdView = mView!!.findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    override fun onRewardedVideoAdLoaded() {

        videoAdText.text = "Video Add Loaded"
        videoAdText.setOnClickListener {
            mRewardedVideoAd.show()
        }

    }

    override fun onRewardedVideoAdOpened() {
    }

    override fun onRewardedVideoStarted() {
    }

    override fun onRewardedVideoAdClosed() {
        loadVideoAd()
        videoAdText.text = "Reloading Video Ad ...."


    }

    override fun onRewarded(p0: RewardItem?) {
    }

    override fun onRewardedVideoAdLeftApplication() {
    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        videoAdText.text = "onRewardedVideoAdFailedToLoad , Error code : $p0"

    }

    override fun onRewardedVideoCompleted() {
    }

    override fun onPause() {
        super.onPause()
        mRewardedVideoAd.pause(context)
    }

    override fun onResume() {
        super.onResume()
        mRewardedVideoAd.resume(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        mRewardedVideoAd.destroy(context)
    }

}