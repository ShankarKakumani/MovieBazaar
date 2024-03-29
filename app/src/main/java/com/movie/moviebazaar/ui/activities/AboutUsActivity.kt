package com.movie.moviebazaar.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.movie.moviebazaar.R
import kotlinx.android.synthetic.main.layout_about_members_contributors.*
import kotlinx.android.synthetic.main.toolbar_default.*

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        githubLayout.setOnClickListener { goToUrl("https://github.com/ShankarKakumani/") }


        Glide.with(this)
                .load("https://avatars0.githubusercontent.com/u/35686278?s=400&u=0d0a25b989c9329d0d27e10b900ffb436a6c096d&v=4")
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(avatarAboutUs)




        initToolbar()

    }

    private fun goToUrl(url: String) {
        val uriUrl = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    private fun initToolbar() {

        setSupportActionBar(toolbarDefault)
        supportActionBar!!.title= "About Us"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}