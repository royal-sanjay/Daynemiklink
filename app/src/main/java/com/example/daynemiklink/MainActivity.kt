package com.example.daynemiklink

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamiclinks.androidParameters
import com.google.firebase.dynamiclinks.iosParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var bnt: Button
    var shortlink = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bnt = findViewById<Button>(R.id.share)

        val intentFilter = IntentFilter(Intent.ACTION_VIEW)
        intentFilter.addDataScheme("https")
        intentFilter.addDataAuthority("google.com", null)
        registerReceiver(dynamicLinkReceiver, intentFilter)

        Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://google.com/userName?name=sonu")
            domainUriPrefix = "https://daynemiklink.page.link"
            androidParameters {

            }
            iosParameters("com.example.ios") {}
        }
            .addOnSuccessListener {
                shortlink = it.shortLink.toString()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed" + it.message, Toast.LENGTH_SHORT).show()
            }

        bnt.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            val link = shortlink
            Toast.makeText(this, "" + link, Toast.LENGTH_SHORT).show()
            intent.putExtra(Intent.EXTRA_TEXT, link)
            startActivity(Intent.createChooser(intent, "Share Link"))
        }

         dynamicLinkRecevider() // Commented out as it's not being used in the provided code
    }

    private fun dynamicLinkRecevider() {
        Firebase.dynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamickLinkData ->
                var deepLinkUri: Uri? = null
                if (pendingDynamickLinkData != null) {
                    deepLinkUri = pendingDynamickLinkData.link
                }
                if (deepLinkUri != null) {
                    val name = deepLinkUri.getQueryParameter("name")
                    bnt.text = name.toString()
                }
            }
    }

    private val dynamicLinkReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            // handle dynamic click here
        }
    }
}
