package com.devaanshingh.dankport

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var currentimageurl : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadmeme()
    }
    private fun loadmeme() {
        // Instantiate the RequestQueue.
        progress_bar.visibility = View.VISIBLE

        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->
               currentimageurl = response.getString("url")
                Glide.with(this).load(currentimageurl).listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?,
                                              model: Any?,
                                              target: Target<Drawable>?,
                                              isFirstResource: Boolean): Boolean
                    {
                        progress_bar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?,
                                                 model: Any?,
                                                 target: Target<Drawable>?,
                                                 dataSource: DataSource?,
                                                 isFirstResource: Boolean): Boolean
                    {
                        progress_bar.visibility = View.GONE
                        return false
                    }
                }).into(meme_image)
            },
            Response.ErrorListener {
               Toast.makeText(this , "OOOPS something went wrong!" , Toast.LENGTH_LONG).show()
            })

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun SwitchColor(view: View) {
        if(switchcolor.text == "OFF") {
            main_view.setBackgroundColor(Color.BLACK)
            dark_mode.setTextColor(Color.WHITE)
            switchcolor.setTextColor(Color.WHITE)
            switchcolor.text = "ON"
            next_button.setTextColor(Color.WHITE)
            share_button.setTextColor(Color.WHITE)
            next_button.setBackgroundColor(Color.GRAY)
            share_button.setBackgroundColor(Color.GRAY)
        }
        else if(switchcolor.text == "ON") {
            main_view.setBackgroundColor(Color.WHITE)
            dark_mode.setTextColor(Color.BLACK)
            switchcolor.setTextColor(Color.BLACK)
            switchcolor.text = "OFF"
            next_button.setBackgroundColor(Color.CYAN)
            share_button.setBackgroundColor(Color.CYAN)
            next_button.setTextColor(Color.WHITE)
            share_button.setTextColor(Color.WHITE)
        }
    }

    fun nextmeme(view: View) {
        loadmeme()
    }

    fun sharememe(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT , "Hey Bro, check out this cool meme ! $currentimageurl")
        val chooser = Intent.createChooser(intent, "Share via : ")
        startActivity(chooser)
    }
}