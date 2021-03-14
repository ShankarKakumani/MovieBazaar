package com.movie.moviebazaar.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.movie.moviebazaar.R
import com.movie.moviebazaar.ui.fragments.AddMoviesFragment
import com.movie.moviebazaar.ui.fragments.HomeFragment
import com.movie.moviebazaar.ui.fragments.MoviesFragment
import com.shankar.customtoast.StatusBar
import kotlinx.android.synthetic.main.activity_home.toolbar
import kotlinx.android.synthetic.main.layout_tabs.*


class HomeActivity : AppCompatActivity() {

    private val homeFragment: Fragment = HomeFragment()
    private val addMoviesFragment: Fragment = AddMoviesFragment()
    private val moviesFragment: Fragment = MoviesFragment()

    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = homeFragment

    private var parentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        Tools.loadDataUsingVolley(this);


        parentView = findViewById(android.R.id.content)
        initNavigationMenu()

        logoutLayout.setOnClickListener { logout() }
        aboutLayout.setOnClickListener { startActivity(Intent(this,AboutUsActivity::class.java)) }

        bottomNavigation()
        loadData()
        StatusBar.setStatusBarColorCustom(this, R.color.black)
    }

    private fun loadData() {
        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                Glide.with(this)
                        .load(profile.photoUrl)
                        .error(R.drawable.ph_rect_vertical)
                        .placeholder(R.drawable.ph_rect_vertical)
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(avatar)

                userName.text = profile.displayName
            }
        }

    }

    @SuppressLint("NonConstantResourceId")
    private fun bottomNavigation() {
        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        fm.beginTransaction().add(R.id.frameLayout, moviesFragment, "3").hide(moviesFragment).commit()
        fm.beginTransaction().add(R.id.frameLayout, addMoviesFragment, "2").hide(addMoviesFragment).commit()
        fm.beginTransaction().add(R.id.frameLayout, homeFragment, "1").commit()
        navigation.selectedItemId = R.id.home
        navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> {
                    fm.beginTransaction().hide(active).show(homeFragment).commit()
                    active = homeFragment
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.earn -> {
                    fm.beginTransaction().hide(active).show(addMoviesFragment).commit()
                    active = addMoviesFragment
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.movies -> {
                    fm.beginTransaction().hide(active).show(moviesFragment).commit()
                    active = moviesFragment
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false
        }

    }

    private fun initNavigationMenu() {
        val navView = findViewById<View>(R.id.nav_view) as NavigationView
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer,
            toolbar as Toolbar?,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
        }
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener { item: MenuItem ->
            Toast.makeText(
                applicationContext,
                item.title.toString() + " Selected",
                Toast.LENGTH_SHORT
            ).show()
            drawer.closeDrawers()
            true
        }

        // open drawer at start
        //drawer.openDrawer(GravityCompat.START);
    }


    private fun logout() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to logout ?")
                .setPositiveButton("Yes") { dialog, id ->
                    run {
                        Firebase.auth.signOut()
                        startActivity(Intent(this, Authentication::class.java))
                        finish()
                    }
                }
                .setNegativeButton("No", null)
                .show()

    }

}