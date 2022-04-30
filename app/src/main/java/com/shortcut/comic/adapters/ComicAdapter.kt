package com.shortcut.comic.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shortcut.comic.R
import com.shortcut.comic.fragments.DetailFragment
import com.shortcut.comic.fragments.dataSet_AllComics
import com.shortcut.comic.objects.ComicObject


class ComicAdapter(private var dataSet: MutableList<ComicObject>, private val context: Context?):
    RecyclerView.Adapter<ComicAdapter.ViewHolder>()
{


    class ViewHolder(view:View): RecyclerView.ViewHolder(view)
    {
        // Initialize Item's XML Variables Here
        var layout_item: ConstraintLayout
        var constraintLayout: ConstraintLayout
        var imageView_thumbnail: ImageView
        var textView_title: TextView
        var textView_date: TextView
        var textView_favourite: TextView


        init {
            // Assign Item's XML Variables Here
            layout_item = view.findViewById(R.id.layout_item)
            constraintLayout = view.findViewById(R.id.constraintLayout)
            imageView_thumbnail = view.findViewById(R.id.imageView_thumbnail)
            textView_title = view.findViewById(R.id.textView_title)
            textView_date = view.findViewById(R.id.textView_date)
            textView_favourite = view.findViewById(R.id.textView_favourite)
        }
    }


    // When Item Get Rendered
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ComicAdapter.ViewHolder
    {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_comic, viewGroup, false)

        return ViewHolder(view)
    }

    // When Item Get Data-Binding
    override fun onBindViewHolder(holder: ComicAdapter.ViewHolder, position: Int) {
        holder.textView_title.text = dataSet[position].get_safe_title()
        if (context != null) {
            Glide.with(context).load(dataSet[position].get_img()).into(holder.imageView_thumbnail)
        }
        val date: String =
            dataSet[position].get_day() + "/" + dataSet[position].get_month() + "/" + dataSet[position].get_year()
        holder.textView_date.text = date



        //  Details For Comic
        holder.layout_item.setOnClickListener(View.OnClickListener { v ->
        val data = Bundle()
        data.putInt("index", position)
        data.putInt("num", dataSet[position].get_number())

        //  Opens Details Fragment
        val activity = v.context as AppCompatActivity
        val detailFragment = DetailFragment()
        detailFragment.arguments = data
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, detailFragment).addToBackStack(null).commit()
        })
    }


    // Count Items in RecyclerView
    override fun getItemCount() = dataSet.size
}
