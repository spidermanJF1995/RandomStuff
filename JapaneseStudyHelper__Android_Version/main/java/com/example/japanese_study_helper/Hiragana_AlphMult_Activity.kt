package com.example.japanese_study_helper

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import java.io.IOException
import kotlin.collections.ArrayList
import kotlin.random.Random

class Hiragana_AlphMult_Activity : AppCompatActivity() {

    // Used to store the Characters...
    private var allHiraganaCharacters = mutableListOf<String>()
    private var allRanjiCharacters = mutableListOf<String>()

    // Used to track the missed characters to help display them later...
    private var allMissedHiraganaCharacters = mutableListOf<String>()
    private var allMissedRanjiCharacters = mutableListOf<String>()
    private var hiraganaCharacter = ""
    private var ranjiCharacter = ""

    // Used to keep track of the correct character...
    private var currentCharacter: Int = 0
    private var correctAnswerButtonLocation: Int = 0

    // Used to check if all of the characters have been used...
    private var countToEndOfGame: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiragana_alph_mult)

        // "!!" is like --> if(actionBar != null)...
        supportActionBar!!.hide()

        val GoBackButtonCopy: Button = findViewById(R.id.BackOneMenuButton)
        GoBackButtonCopy.setOnClickListener {
            finish()
        } // end setOnClickListener() method

        // Set the Score values to 0
        val correctScore: TextView = findViewById(R.id.HiraganaAlphMultDisplayCorrectNumberValue)
        val wrongScore: TextView = findViewById(R.id.HiraganaAlphMultDisplayWrongNumberValue)
        correctScore.text = "0"
        wrongScore.text = "0"

        readFiles()

        countToEndOfGame = allHiraganaCharacters.size

        assignAnswers()
    } // end onCreate() function

    private fun readFiles()
    {
        // Used to store the file's contents...
        var Hiragana_Chars: String? = ""
        var Ranji_Chars: String? = ""

        try {
            // Reads the "hiragana.txt" file...
            Hiragana_Chars = assets.open("hiragana.txt").bufferedReader().use{it.readText()}
            Hiragana_Chars = Hiragana_Chars.replace("\n", " ")
            Hiragana_Chars = Hiragana_Chars.trim()
            allHiraganaCharacters = sendToList(Hiragana_Chars)

            // Reads the "Ranji.txt" file...
            Ranji_Chars = assets.open("Ranji.txt").bufferedReader().use{it.readText()}
            Ranji_Chars = Ranji_Chars.replace("\n", " ")
            Ranji_Chars = Ranji_Chars.trim()
            allRanjiCharacters = sendToList(Ranji_Chars)

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
        var usedChoices = mutableListOf<String>()

        // Get the answer choice buttons...
        var buttons = mutableListOf<Button>()
        var buttonCopy: Button = findViewById(R.id.HiraganaAlphMultButtonTopLeft)
        buttons.add(buttonCopy)

        buttonCopy = findViewById(R.id.HiraganaAlphMultButtonTopRight)
        buttons.add(buttonCopy)

        buttonCopy = findViewById(R.id.HiraganaAlphMultButtonBottomLeft)
        buttons.add(buttonCopy)

        buttonCopy = findViewById(R.id.HiraganaAlphMultButtonBottomRight)
        buttons.add(buttonCopy)

        // Store the current character and answer...
        hiraganaCharacter = allHiraganaCharacters[currentCharacter]
        ranjiCharacter = allRanjiCharacters[currentCharacter]

        // Keeps track of the "ranjiCharacter"s that have already been displayed as choices...
        usedChoices.add(ranjiCharacter)

        // Set the TextView's text to be the current character...
        var labelCopy: TextView = findViewById(R.id.HiraganaAlphMultDisplayChar)
        labelCopy.text = hiraganaCharacter

        // Set a random button to have the correct answer and remove that button so it won't be edited again...
        var correctAnswerSpot = Random.nextInt(0, buttons.size)
        buttons[correctAnswerSpot].text = ranjiCharacter
        buttons.removeAt(correctAnswerSpot)
        correctAnswerButtonLocation = correctAnswerSpot

        // Set the other buttons to have a random answer choice...
        var randomCharacter = 0
        buttons.forEach{
            // select a random answer choice while that choice has not been used yet...
            do
            {
                randomCharacter = Random.nextInt(0, allHiraganaCharacters.size)
            }
            while(usedChoices.contains(allRanjiCharacters[randomCharacter]))

            // keep track of another used character choice...
            usedChoices.add(allRanjiCharacters[randomCharacter])

            // make the random choice be the text of a button...
            it.text = allRanjiCharacters[randomCharacter]
        } // end forEach{}

        countToEndOfGame--
    } // end assignAnswers() function

    fun checkAnswerOnClick(view: View)
    {
        // Button that was clicked...
        val button: Button = view as Button

        // Get the answer choice buttons...
        var buttons = mutableListOf<Button>()
        var buttonCopy: Button = findViewById(R.id.HiraganaAlphMultButtonTopLeft)
        buttons.add(buttonCopy)

        buttonCopy = findViewById(R.id.HiraganaAlphMultButtonTopRight)
        buttons.add(buttonCopy)

        buttonCopy = findViewById(R.id.HiraganaAlphMultButtonBottomLeft)
        buttons.add(buttonCopy)

        buttonCopy = findViewById(R.id.HiraganaAlphMultButtonBottomRight)
        buttons.add(buttonCopy)

        // Disable all of the buttons to prevent spam clicking...
        buttons.forEach{
            it.isEnabled = false
            it.setBackgroundColor(ContextCompat.getColor(this, R.color.ButtonDisabledColor))
        } // end forEach{}

        // if correct answer selected...
        if(button.text == ranjiCharacter)
        {
            // Edit the "CorrectOrWrong" label and make it visible...
            var CorrectOrWrongLabel: TextView = findViewById(R.id.CorrectOrWrongText)
            CorrectOrWrongLabel.text = "Correct!"
            CorrectOrWrongLabel.setTextColor(ContextCompat.getColor(this, R.color.CorrectAnswerColor))
            CorrectOrWrongLabel.isVisible = true

            // Edit the selected button...
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.CorrectAnswerColor))

            // Increase the Correct Score and then Display the new Score...
            val correctScore: TextView = findViewById(R.id.HiraganaAlphMultDisplayCorrectNumberValue)
            correctScore.text = (correctScore.text.toString().toInt() + 1).toString()
        }
        // if wrong answer selected...
        else
        {
            // Store the missed character to be displayed later...
            allMissedHiraganaCharacters.add(hiraganaCharacter)
            allMissedRanjiCharacters.add(ranjiCharacter)

            // Edit the "CorrectOrWrong" label and make it visible...
            var CorrectOrWrongLabel: TextView = findViewById(R.id.CorrectOrWrongText)
            CorrectOrWrongLabel.text = "Wrong!"
            CorrectOrWrongLabel.setTextColor(ContextCompat.getColor(this, R.color.WrongAnswerColor));
            CorrectOrWrongLabel.isVisible = true

            // Edit the selected button...
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.WrongAnswerColor))

            // Increase the Correct Score and then Display the new Score...
            val wrongScore: TextView = findViewById(R.id.HiraganaAlphMultDisplayWrongNumberValue)
            wrongScore.text = (wrongScore.text.toString().toInt() + 1).toString()

            // Edit the background color of the Correct Answer Button...
            buttons[correctAnswerButtonLocation].setBackgroundColor(ContextCompat.getColor(this, R.color.CorrectAnswerColor))
        } // end if/else

        // Make the continueButton visible...
        var continueButton: Button = findViewById(R.id.ContinueButton)
        continueButton.isVisible = true
    } // end checkAnswerOnClick() function

    fun continueButtonClick(view: View)
    {
        // if at the end of the characters list...
        if(countToEndOfGame == 0)
        {
            // if the User got all of the answers correct...
            if(allMissedHiraganaCharacters.size == 0)
            {
                val intent = Intent(this, All_Correct_Activity::class.java)
                startActivity(intent)
            }
            else
            {
                val intent = Intent(this, Missed_Some_Characters_Activity::class.java)
                intent.putStringArrayListExtra("MissedJapaneseCharacterList", ArrayList(allMissedHiraganaCharacters))
                intent.putStringArrayListExtra("MissedRanjiCharacterList", ArrayList(allMissedRanjiCharacters))
                startActivity(intent)
            } // end if/else
        }
        // if there are more characters to show...
        else
        {
            // Move to the next character...
            currentCharacter++

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

        // Reset the Button backgrounds...
        var buttons = mutableListOf<Button>()
        var buttonCopy: Button = findViewById(R.id.HiraganaAlphMultButtonTopLeft)
        buttons.add(buttonCopy)

        buttonCopy = findViewById(R.id.HiraganaAlphMultButtonTopRight)
        buttons.add(buttonCopy)

        buttonCopy = findViewById(R.id.HiraganaAlphMultButtonBottomLeft)
        buttons.add(buttonCopy)

        buttonCopy = findViewById(R.id.HiraganaAlphMultButtonBottomRight)
        buttons.add(buttonCopy)

        buttons.forEach{
            it.isEnabled = true
            it.setBackgroundColor(ContextCompat.getColor(this, R.color.ButtonBackgroundColor))
        } // end forEach{}
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
} // end Hiragana_AlphMult_Activity