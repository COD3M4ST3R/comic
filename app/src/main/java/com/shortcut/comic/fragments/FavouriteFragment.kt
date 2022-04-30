package com.shortcut.comic.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shortcut.comic.R
import com.shortcut.comic.adapters.ComicAdapter
import com.shortcut.comic.objects.ComicObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

// DataSet: Favourite Comics
val dataSet_FavouriteComics = mutableListOf<ComicObject>()

/**
 * A simple [Fragment] subclass.
 * Use the [FavouriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class FavouriteFragment : Fragment()
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
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        // Initialize - Assign XML Varibles
        val rv_FavouriteFragment = view.findViewById<RecyclerView>(R.id.rv_FavouriteFragment)
        val textView_hint = view.findViewById<TextView>(R.id.textView_hint)
        val searchView = view.findViewById<SearchView>(R.id.searchView)

        if(dataSet_FavouriteComics.isEmpty() == true)
        {
            textView_hint.visibility = View.VISIBLE
        }

        // Recycler View - Adapter Initialize
        val adapter = ComicAdapter(dataSet_FavouriteComics, context)
        rv_FavouriteFragment.adapter = adapter
        rv_FavouriteFragment.layoutManager = LinearLayoutManager(context)
        rv_FavouriteFragment.setHasFixedSize(true)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment delete.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}