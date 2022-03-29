package com.example.goldenequatorassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.goldenequatorassignment.R
import com.example.goldenequatorassignment.adapter.FragmentAdapter
import com.example.goldenequatorassignment.ui.home_page.NowPlayingFragment
import com.example.goldenequatorassignment.ui.home_page.PopularFragment
import com.example.goldenequatorassignment.ui.home_page.TopRatedFragment
import com.example.goldenequatorassignment.ui.home_page.UpcomingFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var viewPager = findViewById<ViewPager>(R.id.viewPager)
        var tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(NowPlayingFragment(),"Now Playing")
        fragmentAdapter.addFragment(PopularFragment(),"Popular")
        fragmentAdapter.addFragment(TopRatedFragment(),"Top Rated")
        fragmentAdapter.addFragment(UpcomingFragment(),"Upcoming")

        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.action_bar_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.action_bar_search ->{
                Toast.makeText(this, "Search Page", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.action_bar_favorite ->{
                Toast.makeText(this, "Favorite Page", Toast.LENGTH_LONG).show()
                return true
            }else -> super.onOptionsItemSelected(item)
        }
    }

}