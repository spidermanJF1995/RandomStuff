package com.example.japanese_study_helper

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import java.io.IOException
import kotlin.collections.ArrayList
import kotlin.random.Random

class HiraganaAndKatakana_RandFree_Activity : AppCompatActivity()
{
    // Used to store the Characters...
    private var allHiraganaCharacters = mutableListOf<String>()
    private var allKatakanaCharacters = mutableListOf<String>()
    private var bothCharacterSets = mutableListOf<String>()
    private var allRanjiCharacters = mutableListOf<String>()
    private var allRanjiCharactersDuplicated = mutableListOf<String>()

    // Used to track the missed characters to help display them later...
    private var allMissedJapaneseCharacters = mutableListOf<String>()
    private var allMissedRanjiCharacters = mutableListOf<String>()
    private var japaneseCharacter = ""
    private var ranjiCharacter = ""

    // Used to keep track of the correct character...
    private var currentCharacter: Int = 0

    // Used to check if all of the characters have been used...
    private var countToEndOfGame: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiragana_and_katakana_rand_free)

        // "!!" is like --> if(actionBar != null)...
        supportActionBar!!.hide()

        val GoBackButtonCopy: Button = findViewById(R.id.BackOneMenuButton)
        GoBackButtonCopy.setOnClickListener {
            finish()
        } // end setOnClickListener() method

        // Makes the "Enter" button work to submit the answer...
        val EditTextCopy: EditText = findViewById(R.id.HiraganaAndKatakanaRandFreeEnterAnswer)
        EditTextCopy.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER)
            {
                var submitAnswer : Button = findViewById(R.id.SubmitAnswerButton)
                submitAnswer.performClick()
            } // end if
            return@setOnKeyListener false
        } // end setOnKeyListener{}

        // Set the Score values to 0
        val correctScore: TextView = findViewById(R.id.HiraganaAndKatakanaRandFreeDisplayCorrectNumberValue)
        val wrongScore: TextView = findViewById(R.id.HiraganaAndKatakanaRandFreeDisplayWrongNumberValue)
        correctScore.text = "0"
        wrongScore.text = "0"

        readFiles()

        countToEndOfGame = bothCharacterSets.size

        assignAnswers()
    } // end onCreate() function

    private fun readFiles()
    {
        // Used to store the file's contents...
        var Hiragana_Chars: String? = ""
        var Katakana_Chars: String? = ""
        var Ranji_Chars: String? = ""

        try {
            // Reads the "hiragana.txt" file...
            Hiragana_Chars = assets.open("hiragana.txt").bufferedReader().use{it.readText()}
            Hiragana_Chars = Hiragana_Chars.replace("\n", " ")
            Hiragana_Chars = Hiragana_Chars.trim()
            allHiraganaCharacters = sendToList(Hiragana_Chars)

            // Reads the "katakana.txt" file...
            Katakana_Chars = assets.open("katakana.txt").bufferedReader().use{it.readText()}
            Katakana_Chars = Katakana_Chars.replace("\n", " ")
            Katakana_Chars = Katakana_Chars.trim()
            allKatakanaCharacters = sendToList(Katakana_Chars)

            // Reads the "Ranji.txt" file...
            Ranji_Chars = assets.open("Ranji.txt").bufferedReader().use{it.readText()}
            Ranji_Chars = Ranji_Chars.replace("\n", " ")
            Ranji_Chars = Ranji_Chars.trim()
            allRanjiCharacters = sendToList(Ranji_Chars)

            // Combines the lists together...
            bothCharacterSets.addAll(allHiraganaCharacters)
            bothCharacterSets.addAll(allKatakanaCharacters)

            allRanjiCharactersDuplicated.addAll(allRanjiCharacters)
            allRanjiCharactersDuplicated.addAll(allRanjiCharacters)

            // Opens up some space...
            allHiraganaCharacters.clear()
            allKatakanaCharacters.clear()
            allRanjiCharacters.clear()
        } catch (e: IOException){
            var dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("File Not Found")
            dialogBuilder.setCancelable(true)
            dialogBuilder.show()
        } // end try/catch
    } // end readFiles() function

    private fun sendToList(str: String): MutableList<String>
    {
        var L = mutableListOf<String>()
        var i = 0
        var char = ""

        // Go through whole String...
        while(i < str.length)
        {
            // Obtained whole character...
            if(str[i] == ' ')
            {
                char = char.trim()
                L.add(char)
                char = ""
            }
            else
            {
                char += str[i]
            } // end if/else

            i++
        } // end while()

        // This Must be done due to ending the while() loop before adding the final character...
        char = char.trim()
        L.add(char)

        return L
    } // end sendToList() function

    private fun assignAnswers()
    {
        // Store which characters are already being displayed by the buttons...
        var userAnswer : EditText = findViewById(R.id.HiraganaAndKatakanaRandFreeEnterAnswer)
        userAnswer.setText("")

        // Gets a random character...
        currentCharacter = Random.nextInt(0, bothCharacterSets.size)

        // Store the current character and answer...
        japaneseCharacter = bothCharacterSets[currentCharacter]
        ranjiCharacter = allRanjiCharactersDuplicated[currentCharacter]

        // Set the TextView's text to be the randomly selected current character...
        var labelCopy: TextView = findViewById(R.id.HiraganaAndKatakanaRandFreeDisplayChar)
        labelCopy.text = japaneseCharacter

        // Makes sure no characters show up twice...
        bothCharacterSets.removeAt(currentCharacter)
        allRanjiCharactersDuplicated.removeAt(currentCharacter)

        countToEndOfGame--
    } // end assignAnswers() function

    fun checkAnswerOnClick(view: View)
    {
        var userAnswer : EditText = findViewById(R.id.HiraganaAndKatakanaRandFreeEnterAnswer)
        userAnswer.isEnabled = false

        var submitAnswer : Button = findViewById(R.id.SubmitAnswerButton)
        submitAnswer.isEnabled = false
        submitAnswer.isVisible = false

        // if correct answer selected...
        if(userAnswer.text.toString().equals(ranjiCharacter, ignoreCase = true))
        {
            // Edit the "CorrectOrWrong" label and make it visible...
            var CorrectOrWrongLabel: TextView = findViewById(R.id.CorrectOrWrongText)
            CorrectOrWrongLabel.textSize = 25F
            CorrectOrWrongLabel.text = "Correct!"
            CorrectOrWrongLabel.setTextColor(ContextCompat.getColor(this, R.color.CorrectAnswerColor))
            CorrectOrWrongLabel.isVisible = true

            // Edit the selected button...
            userAnswer.setBackgroundColor(ContextCompat.getColor(this, R.color.CorrectAnswerColor))

            // Increase the Correct Score and then Display the new Score...
            val correctScore: TextView = findViewById(R.id.HiraganaAndKatakanaRandFreeDisplayCorrectNumberValue)
            correctScore.text = (correctScore.text.toString().toInt() + 1).toString()
        }
        // if wrong answer selected...
        else
        {
            allMissedJapaneseCharacters.add(japaneseCharacter)
            allMissedRanjiCharacters.add(ranjiCharacter)

            // Edit the "CorrectOrWrong" label and make it visible...
            var CorrectOrWrongLabel: TextView = findViewById(R.id.CorrectOrWrongText)
            CorrectOrWrongLabel.textSize = 17F

            // Needed this variable because ".text" won't accept concating Strings
            var wrongText = "Wrong!" + " The correct Answer was --> " + ranjiCharacter
            CorrectOrWrongLabel.text = wrongText
            CorrectOrWrongLabel.setTextColor(ContextCompat.getColor(this, R.color.WrongAnswerColor))
            CorrectOrWrongLabel.isVisible = true

            // Edit the User's answer EditText...
            userAnswer.setBackgroundColor(ContextCompat.getColor(this, R.color.WrongAnswerColor))

            // Increase the Correct Score and then Display the new Score...
            val wrongScore: TextView = findViewById(R.id.HiraganaAndKatakanaRandFreeDisplayWrongNumberValue)
            wrongScore.text = (wrongScore.text.toString().toInt() + 1).toString()
        } // end if/else

        var continueButton: Button = findViewById(R.id.ContinueButton)
        continueButton.isVisible = true
    } // end checkAnswerOnClick() function

    fun continueButtonClick(view: View)
    {
        // if at the end of the characters list...
        if(countToEndOfGame == 0)
        {
            // if the User got all of the answers correct...
            if(allMissedJapaneseCharacters.size == 0)
            {
                val intent = Intent(this, All_Correct_Activity::class.java)
                startActivity(intent)
            }
            else
            {
                val intent = Intent(this, Missed_Some_Characters_Activity::class.java)
                intent.putStringArrayListExtra("MissedJapaneseCharacterList", ArrayList(allMissedJapaneseCharacters))
                intent.putStringArrayListExtra("MissedRanjiCharacterList", ArrayList(allMissedRanjiCharacters))
                startActivity(intent)
            } // end if/else
        }
        // if there are more characters to show...
        else
        {
            // Update and Reset the View to set up for the next character...
            updateView()

            // Reassign the answers...
            assignAnswers()
        } // end if/else
    } // end continueButtonClick() method

    private fun updateView()
    {
        // Reset the Continue button...
        var continueButton: Button = findViewById(R.id.ContinueButton)
        continueButton.isVisible = false

        // Reset the CorrectOrWrong button...
        var CorrectOrWrongLabel: TextView = findViewById(R.id.CorrectOrWrongText)
        CorrectOrWrongLabel.isVisible = false

        // Reset the SubmitAnswer Button...
        var buttonCopy: Button = findViewById(R.id.SubmitAnswerButton)
        buttonCopy.setTextColor(ContextCompat.getColor(this, R.color.ButtonTextColor))
        buttonCopy.setBackgroundColor(ContextCompat.getColor(this, R.color.ButtonBackgroundColor))
        buttonCopy.isEnabled = true
        buttonCopy.isVisible = true

        // Reset the User EditText...
        var userAnswer : EditText = findViewById(R.id.HiraganaAndKatakanaRandFreeEnterAnswer)
        userAnswer.isEnabled = true
        userAnswer.setBackgroundColor(ContextCompat.getColor(this, R.color.EnterAnswerTextBackGroundColor))
        userAnswer.setTextColor(ContextCompat.getColor(this, R.color.purple_200))
    } // end updateView() function

    // Debug Purposes...
    private fun debug(L:MutableList<String>)
    {
        var contents: String = ""

        L.forEach{
            contents += it.toString()
        } // end forEach()

        var dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("File Contents")
        dialogBuilder.setMessage(contents + "Checks Space Characters Or Enter Characters")
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    } // end debug() function

    // Debug Purposes...
    private fun debug(S : String)
    {
        var dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(S)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    } // end debug() function
} // end Hiragana_RandFree_Activity