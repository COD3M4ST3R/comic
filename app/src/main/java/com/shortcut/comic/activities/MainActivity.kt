@file:Suppress("DEPRECATION")

package com.shortcut.comic.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shortcut.comic.R
import com.shortcut.comic.fragments.FavouriteFragment
import com.shortcut.comic.fragments.HomeFragment
import com.shortcut.comic.fragments.dataSet_AllComics
import com.shortcut.comic.fragments.dataSet_FavouriteComics
import com.shortcut.comic.objects.ComicObject


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Fragment Variables
        val fragment_home = HomeFragment()
        val fragment_favourite = FavouriteFragment()

        // Initialize - Assign XML Variables
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        currentFragment(fragment_home) // Current fragment

        // Attach Fragments Into Frame
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.home -> currentFragment(fragment_home)
                R.id.favourites -> currentFragment(fragment_favourite)
            }
            true
        }


        // Load Fav Comics
        val sharedPreferences2 = getSharedPreferences("TEST", Context.MODE_PRIVATE)
        var favs: String

        if(sharedPreferences2.getString("favs", null) != null)
        {
            favs = sharedPreferences2.getString("favs", null)!!
            val keys = favs.split("+").toMutableList()
            keys.removeAt(0)

            fetchData(keys)

        }else{
            println("\n\n\n NULL: ")
        }
    }





    // FUNCTIONS

    // TODO
    // Save Favourite DataSet on Destroy
    override fun onDestroy()
    {
        if(dataSet_FavouriteComics.size != 0) // There are at least 1 data inside
        {
            val size = dataSet_FavouriteComics.size
            var index = 0
            var favs = ""

            while(index < size)
            {
                val number = dataSet_FavouriteComics[index].get_number()
                favs = favs + "+" + number.toString()
                index++
            }

            println("\n\n\n\n\n SAVED DATA" + favs)
            val sharedPreferences = getSharedPreferences("TEST", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("favs", favs)
            editor.commit()
        }

        else // Clear the cache to make cache same with fav dataSet
        {
            val sharedPreferences = getSharedPreferences("TEST", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear().commit()
        }
        super.onDestroy()
    }


    // Current Fragment
    private fun currentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            commit()
        }

    // Fetch Data for Fav Comics
    private fun fetchData(favs: MutableList<String>)
    {
        val size = favs.size
        var index: Int = 0

        while(index < size)
        {
            val key_string = favs[index]
            val key = key_string.toInt()

            var walk: Int = 0
            while(walk < dataSet_AllComics.size)
            {
                val comic: ComicObject
                comic = dataSet_AllComics[walk]
                if(comic.get_number() == key)
                {
                    dataSet_FavouriteComics.add(comic)
                }
                walk++
            }
            index++
        }
    }
}