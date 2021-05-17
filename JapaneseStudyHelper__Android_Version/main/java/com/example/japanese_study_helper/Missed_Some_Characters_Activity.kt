package com.example.japanese_study_helper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Missed_Some_Characters_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_missed_some_characters)

        // "!!" is like --> if(actionBar != null)...
        supportActionBar!!.hide()

        val GoBackButtonCopy: Button = findViewById(R.id.BackOneMenuButton)
        GoBackButtonCopy.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent)
        } // end setOnClickListener() method

        // Get the data to be displayed...
        var intent = intent
        val missedJapaneseCharacters : MutableList<String> = intent.getStringArrayListExtra("MissedJapaneseCharacterList") as MutableList<String>
        val missedRanjiCharacters : MutableList<String> = intent.getStringArrayListExtra("MissedRanjiCharacterList") as MutableList<String>

        // Display the data...
        val missedCharacters : TextView = findViewById(R.id.DisplayMissedCharactersTextView)
        var i = 0
        missedCharacters.text = ""
        var stringHolderMissedCharacters = ""

        missedJapaneseCharacters.forEach {
            stringHolderMissedCharacters += (it + " --------> " + missedRanjiCharacters[i] + "\n" + "************************\n")
            i++
        } // end forEach()

        missedCharacters.text = stringHolderMissedCharacters
    } // end onCreate() function
} // end Missed_Some_Characters class