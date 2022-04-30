package com.shortcut.comic.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.SearchView
import android.widget.TextView

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shortcut.comic.R
import com.shortcut.comic.activities.SplashActivity
import com.shortcut.comic.adapters.ComicAdapter
import com.shortcut.comic.objects.ComicObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

// DataSet: All Comics
val dataSet_AllComics = mutableListOf<ComicObject>()

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment()
{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate View
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize - Assign XML Variables
        val rv_HomeFragment = view.findViewById<RecyclerView>(R.id.rv_HomeFragment)
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        val searchView = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)

        // RecyclerView - Adapter Initialize
        val adapter = ComicAdapter(dataSet_AllComics, context)
        rv_HomeFragment.adapter = adapter
        rv_HomeFragment.layoutManager = LinearLayoutManager(context)
        rv_HomeFragment.setHasFixedSize(true)

        // Refresh
        swipeRefresh.setOnRefreshListener {
            // TODO

            dataSet_AllComics.clear()
            fetchData()
            Handler().postDelayed(
                {
                    adapter!!.notifyDataSetChanged()
                    val toast = Toast.makeText(context, "Updated", Toast.LENGTH_SHORT)
                    toast.show()
                    swipeRefresh.isRefreshing = false
                },
                5000 // value in milliseconds
            )
        }

        // Search
        searchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(p0: String): Boolean
            {
                filterItems(p0, adapter)
                return true
            }

            override fun onQueryTextChange(p0: String): Boolean
            {
                filterItems(p0, adapter)
                return true
            }
        })
        return view
    }

    // Filter amongus Items
    fun filterItems(parameter: String, adapter: ComicAdapter)
    {
        var text = parameter
        var copy_dataSet = mutableListOf<ComicObject>()
        copy_dataSet = dataSet_AllComics

        if(text.isEmpty())
        {

        }else{
            dataSet_AllComics.clear()
            text = text.lowercase()

            for(comic in copy_dataSet)
            {
                if(comic.get_safe_title().toLowerCase().contains(text) || comic.get_number().toString().contains(text))
                {
                    dataSet_AllComics.clear()
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    // Gets All Comics from Internet
    private fun fetchData(){
        val url: String = "https://xkcd.com/info.0.json"
        var total: String
        var totalcomics: Int
        val queue = Volley.newRequestQueue(context)

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                // TODO: Deal With JSON Object Here --> response is in JSON format!

                total = response.getString("num")
                totalcomics = total.toInt()

                var index: Int = totalcomics
                while (index > 0)
                {
                    val url2 = "https://xkcd.com/$index/info.0.json"

                    val request2 = JsonObjectRequest(
                        Request.Method.GET, url2, null,
                        { response2 ->

                            // TODO: Deal With JSON Object Here --> response is in JSON format!

                            val comic = ComicObject()
                            comic.set_safe_title(response2.getString("safe_title"))
                            comic.set_img(response2.getString("img"))
                            comic.set_day(response2.getString("day"))
                            comic.set_month(response2.getString("month"))
                            comic.set_year(response2.getString("year"))
                            comic.set_number(response2.getInt("num"))
                            comic.set_news(response2.getString("news"))
                            comic.set_transcript(response2.getString("transcript"))
                            comic.set_alt(response2.getString("alt"))
                            comic.set_day(response2.getString("day"))
                            comic.set_link("https://www.explainxkcd.com/wiki/index.php/" + response2.getString("num") + ":_" + response2.getString("safe_title").replace(' ', '_'))

                            dataSet_AllComics.add(comic)
                        },
                        { Response.ErrorListener {  }  })
                    queue.add(request2)
                    index--
                }
            },
            { Response.ErrorListener {  }  })

        queue.add(request)
    }
}