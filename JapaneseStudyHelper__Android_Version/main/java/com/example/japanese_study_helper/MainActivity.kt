package com.example.japanese_study_helper

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // "!!" is like --> if(actionBar != null)...
        supportActionBar!!.hide()

        val HiraganaButtonMainCopy: Button = findViewById(R.id.HiraganaButtonMain)
        HiraganaButtonMainCopy.setOnClickListener {
            val intent = Intent(this, Hiragana_Activity::class.java)
            startActivity(intent)
        } // end HiraganaButtonMain setListener() method

        val KatakanaButtonMainCopy: Button = findViewById(R.id.KatakanaButtonMain)
        KatakanaButtonMainCopy.setOnClickListener {
            val intent = Intent(this, Katakana_Activity::class.java)
            startActivity(intent)
        } // end KatakanaButtonMain setListener() method

        val HiraganaAndKatakanaButtonMainCopy: Button = findViewById(R.id.HiraganaAndKatakanaButtonMain)
        HiraganaAndKatakanaButtonMainCopy.setOnClickListener {
            val intent = Intent(this, HiraganaAndKatakana_Activity::class.java)
            startActivity(intent)
        } // end KatakanaButtonMain setListener() method

        val KanjiButtonCopy: Button = findViewById(R.id.KanjiButtonMain)
        KanjiButtonCopy.setOnClickListener {
            val intent = Intent(this, Kanji_Activity::class.java)
            startActivity(intent)
        } // end KanjiButtonMain setListener() method
    } // end onCreate() function
} // end MainActivity class