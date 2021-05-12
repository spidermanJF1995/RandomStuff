package com.example.japanese_study_helper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Hiragana_Characters_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiragana_characters)

        // "!!" is like --> if(actionBar != null)...
        supportActionBar!!.hide()

        val GoBackButtonCopy: Button = findViewById(R.id.BackOneMenuButton)
        GoBackButtonCopy.setOnClickListener {
            finish()
        } // end setOnClickListener() method

        val AlphMultButtonCopy: Button = findViewById(R.id.HiraganaCharactersAlphMultButton)
        AlphMultButtonCopy.setOnClickListener {
            val intent = Intent(this, Hiragana_AlphMult_Activity::class.java)
            startActivity(intent)
        } // end setOnClickListener() method

        val AlphFreeButtonCopy: Button = findViewById(R.id.HiraganaCharactersAlphFreeButton)
        AlphFreeButtonCopy.setOnClickListener {
            val intent = Intent(this, Hiragana_AlphFree_Activity::class.java)
            startActivity(intent)
        } // end setOnClickListener() method

        val RandMultButtonCopy: Button = findViewById(R.id.HiraganaCharactersRandMultButton)
        RandMultButtonCopy.setOnClickListener {
            val intent = Intent(this, Hiragana_RandMult_Activity::class.java)
            startActivity(intent)
        } // end setOnClickListener() method

        val RandFreeButtonCopy: Button = findViewById(R.id.HiraganaCharactersRandFreeButton)
        RandFreeButtonCopy.setOnClickListener {
            val intent = Intent(this, Hiragana_RandFree_Activity::class.java)
            startActivity(intent)
        } // end setOnClickListener() method
    } // end onCreate() function
} // end Hiragana_Characters_Activity