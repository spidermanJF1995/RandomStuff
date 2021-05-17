package com.example.japanese_study_helper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HiraganaAndKatakana_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiragana_and_katakana)

        // "!!" is like --> if(actionBar != null)...
        supportActionBar!!.hide()

        val GoBackButtonCopy: Button = findViewById(R.id.BackOneMenuButton)
        GoBackButtonCopy.setOnClickListener {
            finish()
        } // end setOnClickListener() method

        val HiraganaAndKatakanaCharactersButtonCopy: Button = findViewById(R.id.HiraganaAndKatakanaCharactersButton)
        HiraganaAndKatakanaCharactersButtonCopy.setOnClickListener {
            val intent = Intent(this, HiraganaAndKatakana_Characters_Activity::class.java)
            startActivity(intent)
        } // end setOnClickListener() method
    } // end onCreate() function
} // end HiraganaAndKatakana_Activity class