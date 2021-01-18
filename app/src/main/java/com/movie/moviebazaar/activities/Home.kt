package com.movie.moviebazaar.activities

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
import com.movie.moviebazaar.navigation.EarnF
import com.movie.moviebazaar.navigation.HomeF
import com.movie.moviebazaar.navigation.MoviesF
import com.shankar.customtoast.StatusBar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_tabs.*


class Home : AppCompatActivity() {

    private val firstFragment: Fragment = HomeF()
    private val secondFragment: Fragment = EarnF()
    private val thirdFragment: Fragment = MoviesF()

    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = firstFragment

    private var parent_view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        parent_view = findViewById(android.R.id.content)
        initNavigationMenu()

        logoutLayout.setOnClickListener { logout() }

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
                        .into(avatar);

                userName.text = profile.displayName
            }
        }

    }

    @SuppressLint("NonConstantResourceId")
    private fun bottomNavigation() {
        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        fm.beginTransaction().add(R.id.frameLayout, thirdFragment, "3").hide(thirdFragment).commit()
        fm.beginTransaction().add(R.id.frameLayout, secondFragment, "2").hide(secondFragment).commit()
        fm.beginTransaction().add(R.id.frameLayout, firstFragment, "1").commit()
        navigation.selectedItemId = R.id.home
        navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> {
                    fm.beginTransaction().hide(active).show(firstFragment).commit()
                    active = firstFragment
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.earn -> {
                    fm.beginTransaction().hide(active).show(secondFragment).commit()
                    active = secondFragment
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.movies -> {
                    fm.beginTransaction().hide(active).show(thirdFragment).commit()
                    active = thirdFragment
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    private fun initNavigationMenu() {
        val nav_view = findViewById<View>(R.id.nav_view) as NavigationView
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer,
            toolbar as Toolbar?,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener { item: MenuItem ->
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