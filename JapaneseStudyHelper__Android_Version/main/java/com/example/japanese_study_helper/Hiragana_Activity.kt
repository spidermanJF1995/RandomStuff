package com.example.japanese_study_helper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Hiragana_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiragana)

        // "!!" is like --> if(actionBar != null)...
        supportActionBar!!.hide()

        val GoBackButtonCopy: Button = findViewById(R.id.BackOneMenuButton)
        GoBackButtonCopy.setOnClickListener {
            finish()
        } // end setOnClickListener() method

        val HiraganaCharactersButtonCopy: Button = findViewById(R.id.HiraganaCharactersButton)
        HiraganaCharactersButtonCopy.setOnClickListener {
            val intent = Intent(this, Hiragana_Characters_Activity::class.java)
            startActivity(intent)
        } // end setOnClickListener() method
    } // end onCreate() function
} // end Hiragana_Activity class