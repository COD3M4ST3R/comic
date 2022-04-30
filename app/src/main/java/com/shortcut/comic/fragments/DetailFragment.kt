package com.shortcut.comic.fragments

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.shortcut.comic.R
import com.shortcut.comic.objects.ComicObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment(), TextToSpeech.OnInitListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var tts: TextToSpeech? = null
    lateinit var imageView5: ImageView
    lateinit var imageView6: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
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
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        // Variables
        tts = TextToSpeech(context, this, "com.google.android.tts")

        // Assign - Initialize XML Variables
        val imageView_thumbnail = view.findViewById<ImageView>(R.id.imageView_thumbnail)
        val textView_title = view.findViewById<TextView>(R.id.textView_title)
        val imageView_share = view.findViewById<ImageView>(R.id.imageView_share)
        val textView_alt = view.findViewById<TextView>(R.id.textView_alt)
        val textView_explanation = view.findViewById<TextView>(R.id.textView_explanation)
        imageView5 = view.findViewById(R.id.imageView5)
        imageView6 = view.findViewById(R.id.imageView6)

        // Pragmatic Changes
        imageView5.visibility = View.VISIBLE
        imageView6.visibility = View.INVISIBLE

        //  Bundle From Adapter To Distinguish Data Amogus DataSet
        var index: Int = 0
        var num: Int = 0
        val bundle = this.arguments
        if (bundle != null) {
            index = bundle.getInt("index") // Get Right Comic For Detail With Index(Key) for dataSet_AllComics
            num = bundle.getInt("num")
        }


        // Cloning Object With Target Data
        var walk = 0
        var size = dataSet_AllComics.size
        var comic = ComicObject()
        while(walk < size)
        {
            if(dataSet_AllComics[walk].get_number() == num)
            {
                comic = dataSet_AllComics[walk]
                break
            }
            walk++
        }

        // Data Binding into XML Variables
        context?.let { Glide.with(it).load(comic.get_img()).into(imageView_thumbnail) }
        textView_title.text = comic.get_safe_title() + ": " + comic.get_number()
        textView_alt.text = comic.get_alt()

        if(comic.get_transcript().isEmpty())
        {
            textView_explanation.setText("No Transcript Available")
        }else
        {
            textView_explanation.text = comic.get_transcript()
        }

        // TTS
        imageView5.setOnClickListener {
            speak(tts!!, comic)
            imageView5.visibility = View.INVISIBLE
            imageView6.visibility = View.VISIBLE
        }

        imageView6.setOnClickListener {
            tts!!.stop()
            imageView5.visibility = View.VISIBLE
            imageView6.visibility = View.INVISIBLE
        }

        // Share Function: Link
        imageView_share.setOnClickListener {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, comic.get_link())
                type = "text/*"
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }


        // Double-Click for Favourite - UnFavourite
        imageView_thumbnail.setOnClickListener(object : DoubleClickListener()
        {
            override fun onDoubleClick(v: View?)
            {
                // Check if dataSet is empty: true --> add inside
                if (size == 0){
                    var comic = ComicObject()
                    comic = dataSet_AllComics[index]
                    dataSet_FavouriteComics.add(comic)
                    val toast = Toast.makeText(context, "Added To Favourites", Toast.LENGTH_SHORT)
                    toast.show()

                }else{ // dataSet is not empty
                    var walk: Int = 0
                    var flag:Boolean = false
                    val size = dataSet_FavouriteComics.size
                    while(walk < size)
                    {
                        // exists in target, so user wants to unfavourite it
                        if(num == dataSet_FavouriteComics[walk].get_number())
                        {
                            flag = true
                            break
                        }
                        walk++
                    }

                    if(flag == true)
                    {
                        // delete from target dataset
                        dataSet_FavouriteComics.removeAt(walk)
                        val toast = Toast.makeText(context, "Deleted From Favourites", Toast.LENGTH_SHORT)
                        toast.show()
                    }else{
                        // else, add it to target dataset
                        dataSet_FavouriteComics.add(comic)
                        val toast = Toast.makeText(context, "Added To Favourites", Toast.LENGTH_SHORT)
                        toast.show()
                    }

                }
            }
        })

        return view
    }

    // Double-Click for Favourite-UnFavourite
    abstract class DoubleClickListener : View.OnClickListener {
        var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(v)
            }
            lastClickTime = clickTime
        }

        abstract fun onDoubleClick(v: View?)

        companion object {
            private const val DOUBLE_CLICK_TIME_DELTA: Long = 300 //milliseconds
        }
    }

    override fun onInit(p0: Int) {
        if (p0 == TextToSpeech.SUCCESS)
        {
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onDone(utteranceId: String)
                {
                    val toast = Toast.makeText(context, "TEST", Toast.LENGTH_SHORT)
                    toast.show()

                    imageView5.visibility = View.VISIBLE
                    imageView6.visibility = View.INVISIBLE
                }

                override fun onError(utteranceId: String) {}
                override fun onStart(utteranceId: String) {}
            })
        } else {

        }

    }

    fun speak(tts: TextToSpeech, comic: ComicObject)
    {
        tts!!.speak(comic.get_safe_title() + comic.get_number() + comic.get_alt() + comic.get_transcript(), TextToSpeech.QUEUE_FLUSH, null, "")
    }



}
