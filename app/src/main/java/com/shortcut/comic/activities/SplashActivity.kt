
// PACKAGE
package com.shortcut.comic.activities

// IMPORTS
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.shortcut.comic.R
import android.view.WindowManager
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shortcut.comic.fragments.dataSet_AllComics
import com.shortcut.comic.objects.ComicObject


// MAIN DRIVE
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Pragmatic Changes
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) // hide top bar for better UX

        // Initialize-Assign XML Variables
        val textView = findViewById<TextView>(R.id.textView)
        val textView2 = findViewById<TextView>(R.id.textView2)

        fetchData(textView, textView2)
        Handler().postDelayed({
           open_MainActivity()
            finish()
        }, 10000)
    }

    // Opens Main Activity with Intent
    fun open_MainActivity()
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // Gets All Comics from Internet
    private fun fetchData(textView: TextView, textView2: TextView){
        val url: String = "https://xkcd.com/info.0.json"
        var total: String
        var totalcomics: Int
        val queue = Volley.newRequestQueue(this)

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
                            textView.setText("Fetching Comic: " + comic.get_number().toString())
                            textView2.setText(comic.get_safe_title())
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