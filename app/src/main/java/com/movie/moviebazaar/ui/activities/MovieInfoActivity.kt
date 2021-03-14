package com.movie.moviebazaar.ui.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.movie.moviebazaar.R
import com.movie.moviebazaar.ui.player.Player
import kotlinx.android.synthetic.main.activity_movie_info.*


class MovieInfoActivity : AppCompatActivity() {


    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_info)



        val bundle = intent.extras!!
        val imageUrlL = bundle.getString("imageUrl")
        val movieName = bundle.getString("movieName")
        val movieYear = bundle.getString("movieYear")
        val videoUrl = bundle.getString("videoUrl")

        movieInfoName.text = movieName
        movieInfoYear.text = movieYear


        if(imageUrlL != null)

        {
            Glide.with(this) //1
                .load(imageUrlL)
                .error(R.drawable.placeholder_image)
                .placeholder(R.drawable.placeholder_image)
                .skipMemoryCache(false) //2
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .addListener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                            e: GlideException?,
                            model: Any,
                            target: Target<Drawable?>,
                            isFirstResource: Boolean
                    ): Boolean {
                        imageProgressBar.visibility = View.GONE

                        return false
                    }

                    override fun onResourceReady(
                            resource: Drawable?,
                            model: Any,
                            target: Target<Drawable?>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                    ): Boolean {
                        imageProgressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(movieInfoImage)
        }


        if(videoUrl != null)
        {
            watchNow.setOnClickListener {
                val i = Intent(this, Player::class.java)
                val b = Bundle()
                i.putExtra("videoID", videoUrl)
                i.putExtras(b)
                this.startActivity(i)
            }
//            playInYoutube.setOnClickListener { openInYoutube(videoUrl) }
            playInYoutube.setOnClickListener { openInYoutube(videoUrl) }

        }

        bannerAds()


    }



    private fun bannerAds() {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

    }

    private fun openInYoutube(videoUrl: String)
    {
        val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=$videoUrl")
        )
        try {
            this.startActivity(webIntent)
        } catch (ex: ActivityNotFoundException) {
        }
    }
}