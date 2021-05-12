package com.example.japanese_study_helper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HiraganaAndKatakana_Characters_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiragana_and_katakana_characters)

        // "!!" is like --> if(actionBar != null)...
        supportActionBar!!.hide()

        val GoBackButtonCopy: Button = findViewById(R.id.BackOneMenuButton)
        GoBackButtonCopy.setOnClickListener {
            finish()
        } // end setOnClickListener() method

        val RandMultButtonCopy: Button = findViewById(R.id.HiraganaAndKatakanaCharactersRandMultButton)
        RandMultButtonCopy.setOnClickListener {
            val intent = Intent(this, HiraganaAndKatakana_RandMult_Activity::class.java)
            startActivity(intent)
        } // end setOnClickListener() method

        val RandFreeButtonCopy: Button = findViewById(R.id.HiraganaAndKatakanaCharactersRandFreeButton)
        RandFreeButtonCopy.setOnClickListener {
            val intent = Intent(this, HiraganaAndKatakana_RandFree_Activity::class.java)
            startActivity(intent)
        } // end setOnClickListener() method
    } // end onCreate() function
} // end HiraganaAndKatakana_Characters_Activity