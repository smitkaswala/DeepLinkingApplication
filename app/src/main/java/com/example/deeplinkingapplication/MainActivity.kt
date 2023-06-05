package com.example.deeplinkingapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.net.toUri
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sendButton = findViewById<Button>(R.id.mSend)

        sendButton.setOnClickListener {

            val url = "https://slackpack.page.link/shareNote?noteText=hello_smit_kaswala_"

            generateURL(url.toUri()) {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
                i.putExtra(Intent.EXTRA_TEXT, it)
                startActivity(Intent.createChooser(i, "Share URL"))
            }

        }

        Firebase.dynamicLinks.getDynamicLink(intent).addOnSuccessListener {
            if (it != null){
                val myName = it.link?.getQueryParameter("noteText").toString()
                Toast.makeText(this, myName, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun generateURL(
        generateURI : Uri,
        getShareLink : (String) -> Unit = {}
    ){
        val shareLink = FirebaseDynamicLinks.getInstance().createDynamicLink().run {
            link = generateURI
            domainUriPrefix = "https://slackpack.page.link"

            androidParameters {
                build()
            }

            buildDynamicLink()

        }

        getShareLink.invoke(shareLink.uri.toString())

    }

}