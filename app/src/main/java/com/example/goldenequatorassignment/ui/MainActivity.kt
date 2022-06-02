package com.example.goldenequatorassignment.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.ui.home_page.NowPlayingFragment
import com.example.goldenequatorassignment.ui.home_page.top_rated.TopRatedFragment
import com.example.goldenequatorassignment.ui.home_page.upcoming.UpcomingFragment
import com.example.goldenequatorassignment.ui.home_page.popular.PopularFragment
import com.example.goldenequatorassignment.ui.search_page.SearchMovieActivity
import com.example.goldenequatorassignment.ui.shared_favorite_page.SharedFavoriteMovieActivity
import com.example.goldenequatorassignment.ui.single_fragment.SingleFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        val fragmentAdapter = MainActivityFragmentAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(SingleFragment(1), "Now Playing")
        fragmentAdapter.addFragment(SingleFragment(2),"Popular")
        fragmentAdapter.addFragment(SingleFragment(3),"Top Rated")
        fragmentAdapter.addFragment(SingleFragment(4),"Upcoming")

        /*fragmentAdapter.addFragment(NowPlayingFragment(),"Now Playing")
        fragmentAdapter.addFragment(PopularFragment(),"Popular")
        fragmentAdapter.addFragment(TopRatedFragment(),"Top Rated")
        fragmentAdapter.addFragment(UpcomingFragment(),"Upcoming")*/

        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.action_bar_search ->{
                startActivity(Intent(this, SearchMovieActivity::class.java))
                Toast.makeText(this, "Search Page", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.action_bar_favorite ->{
                //startActivity(Intent(this, FavoriteMovieActivity::class.java))
                startActivity(Intent(this, SharedFavoriteMovieActivity::class.java))
                Toast.makeText(this, "Favorite Page", Toast.LENGTH_LONG).show()
                return true
            }else -> super.onOptionsItemSelected(item)
        }
    }
}