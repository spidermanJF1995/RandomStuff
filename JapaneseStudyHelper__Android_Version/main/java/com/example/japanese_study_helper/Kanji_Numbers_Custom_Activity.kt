package com.example.japanese_study_helper

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView

class Kanji_Numbers_Custom_Activity : AppCompatActivity() {
    // The "EditText" that the User will enter the number with...
    lateinit var userNumberEditText: EditText

    // The "TextView"s that will be used to display the translations...
    lateinit var japaneseVersionDisplay: TextView
    lateinit var kanjiDisplay: TextView
    lateinit var hiraganaDisplay: TextView
    lateinit var romanjiDisplay: TextView

    // Keeps track of which base is currently needed...
    var currentBase = 0

    // Where to store the respective readings when finally converted...
    var kanjiReading = ""
    var hiraganaReading = ""
    var romanjiReading = ""

    // Used to help make sure a proper number was given...
    val regexPattern = "[^0-9,]+"
    val regex = Regex(regexPattern)

    // Contains the bases under 10,000...
    val kanjiBaseNumbers = mapOf(Pair(0, "零"), Pair(1, "一"), Pair(2, "二"), Pair(3, "三"), Pair(4, "四"), Pair(5, "五"),
        Pair(6, "六"), Pair(7, "七"), Pair(8, "八"), Pair(9, "九"), Pair(10, "十"), Pair(100, "百"), Pair(300, "三百"), Pair(600, "六百"),
        Pair(800, "八百"), Pair(1000, "千"), Pair(3000, "三千"), Pair(8000, "八千"))

    val hiraganaBaseNumbers = mapOf(Pair(0, "ゼロ"), Pair(1, "いち"), Pair(2, "に"), Pair(3, "さん"), Pair(4, "よん"), Pair(5, "ご"),
        Pair(6, "ろく"), Pair(7, "なな"), Pair(8, "はち"), Pair(9, "きゅう"), Pair(10, "じゅう"), Pair(100, "ひゃく"), Pair(300, "さんびゃく"), Pair(600, "ろっぴゃく"),
        Pair(800, "はっぴゃく"), Pair(1000, "せん"), Pair(3000, "さんぜん"), Pair(8000, "はっせん"))

    val romanjiBaseNumbers = mapOf(Pair(0, "zero"), Pair(1, "ichi"), Pair(2, "ni"), Pair(3, "san"), Pair(4, "yon"), Pair(5, "go"),
        Pair(6, "roku"), Pair(7, "nana"), Pair(8, "hachi"), Pair(9, "kyuu"), Pair(10, "juu"), Pair(100, "hyaku"), Pair(300, "sanbyaku"), Pair(600, "roppyaku"),
        Pair(800, "happyaku"), Pair(1000, "sen"), Pair(3000, "sanzen"), Pair(8000, "hassen"))

    // Contains the bases 10,000 and above minus 京 (kei)... --> 万 (man), 億 (oku), 兆 (cho)
    // The numbers represent how many commas there are...
    val kanjiBaseNumbersAbove = mapOf(Pair(1, "万"), Pair(2, "億"), Pair(3, "兆"))

    val hiraganaBaseNumbersAbove = mapOf(Pair(1, "まん"), Pair(2, "おく"), Pair(3, "ちょう"))

    val romanjiBaseNumbersAbove = mapOf(Pair(1, "man"), Pair(2, "oku"), Pair(3, "chou"))

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanji_numbers_custom)

        // "!!" is like --> if(actionBar != null)...
        supportActionBar!!.hide()

        // Makes the "Enter" button work to submit the answer...
        userNumberEditText = findViewById(R.id.Kanji_Numbers_Custom_Number_EditText)
        userNumberEditText.setOnKeyListener { v, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
            {
                userNumberEditText.clearFocus()

                var submitNumber : Button = findViewById(R.id.convertButton)
                submitNumber.performClick()
            } // end if
            return@setOnKeyListener false
        } // end setOnKeyListener{}

        japaneseVersionDisplay = findViewById(R.id.japaneseVersionDisplayTextView)
        kanjiDisplay = findViewById(R.id.kanjiDisplayTextView)
        hiraganaDisplay = findViewById(R.id.hiraganaDisplayTextView)
        romanjiDisplay = findViewById(R.id.romanjiDisplayTextView)
    } // end onCreate() function

    fun convertButtonClick(view: View)
    {
        // Makes the keyboard go away...
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.getWindowToken(), 0)

        // The number that was entered by the User...
        var inputNumber : String = userNumberEditText.text.toString()

        // Makes sure no non-numbers are in the User input...
        if(regex.containsMatchIn(inputNumber))
        {
            val alert = AlertDialog.Builder(this)
            alert.setMessage("The entered number was Not in a proper format\nTherefore, the default value of \"100,000\" will be used")
                .setCancelable(false)
                .setPositiveButton("OK") { _, _ -> }

            val displayAlert = alert.create()
            displayAlert.setTitle("Improper Number Format")
            displayAlert.show()
            inputNumber = "100000"
        } // end if

        // if the number is contains enough digits to create a number over 1 quadrillion...
        // 1 quadrillion has 16 digits...
        if(inputNumber.contains(","))
        {
            inputNumber = inputNumber.replace(",", "")
        } // end if

        if(inputNumber.length > 16)
        {
            val alert = AlertDialog.Builder(this)
            alert.setMessage("The entered number was too big.\nTherefore, the default value of \"100,000\" will be used")
                .setCancelable(false)
                .setPositiveButton("OK") { _, _ -> }

            val displayAlert = alert.create()
            displayAlert.setTitle("Improper Number Format")
            displayAlert.show()
            inputNumber = "100000"
        } // end if

        // Might seem a bit redundant to remove the commas and then add them back,
        // but this process helps capture cases like "12,35,54564654,213516,456456456489,45494,"
        // Remove those commas and then insert them into the proper places...
        userNumberEditText.setText(convertToEnglishVersionWithCommas(inputNumber))

        // if the number is just "0"...
        if(inputNumber.toLong() == 0L)
        {
            displayZero()
        }
        else
        {
            readChunks(getJapaneseChunks(inputNumber))
        } // end if/else

        // Resets the "Reading"s...
        kanjiReading = ""
        hiraganaReading = ""
        romanjiReading = ""
    } // end convertButtonClick() function

    private fun convertToEnglishVersionWithCommas(userNumber : String) : String
    {
        // How many digits are in the chunk...
        var countDigits = 0

        // Start at the end of the userNumber...
        var startingCharacter = userNumber.length - 1

        // The value in each chunk...
        var chunkValue = mutableListOf<Char>()
        var chunkValueAsString = ""

        // All of the chunks put together...
        var chunks = mutableListOf<String>()

        // The English version of the number...
        var englishVersion = ""

        // Read the "userNumber" from Right To Left
        while(startingCharacter >= 0)
        {
            chunkValue.add(0, userNumber[startingCharacter])

            countDigits++
            startingCharacter--

            // if an entire chunk has been obtained or if the last digit has been obtained...
            if(countDigits == 3 || startingCharacter < 0)
            {
                chunkValue.forEach{
                    chunkValueAsString += it
                } // end forEach()

                chunks.add(0, chunkValueAsString)

                // Reset for the next chunk...
                chunkValueAsString = ""
                chunkValue.clear()
                countDigits = 0
            } // end if
        } // end while

        // Insert the commas to the number...
        var i = 0
        while(i < chunks.size)
        {
            // if on final "chunkValue" in "chunks"...
            if(i == chunks.size - 1)
            {
                englishVersion += chunks[i]
            }
            else
            {
                englishVersion += chunks[i]
                englishVersion += ","
            } // end if/else

            i++
        } // end while()

        return englishVersion
    } // end convertToEnglishVersionWithCommas() method

    private fun displayZero()
    {
        japaneseVersionDisplay.text = "0"
        kanjiDisplay.text = kanjiBaseNumbers[0]
        hiraganaDisplay.text = hiraganaBaseNumbers[0]
        romanjiDisplay.text = romanjiBaseNumbers[0]
    } // end displayZero() function

    private fun getJapaneseChunks(userNumber : String): MutableList<String>
    {
        // How many digits are in the chunk...
        var countDigits = 0

        // Start at the end of the userNumber...
        var startingCharacter = userNumber.length - 1

        // The value in each chunk...
        var chunkValue = mutableListOf<Char>()
        var chunkValueAsString = ""

        // All of the chunks put together...
        var chunks = mutableListOf<String>()

        // Read the "userNumber" from Right To Left
        while(startingCharacter >= 0)
        {
            chunkValue.add(0, userNumber[startingCharacter])

            countDigits++
            startingCharacter--

            // if an entire chunk has been obtained or if the last digit has been obtained...
            if(countDigits == 4 || startingCharacter < 0)
            {
                chunkValue.forEach{
                    chunkValueAsString += it
                } // end forEach()

                chunks.add(0, chunkValueAsString)

                // Reset for the next chunk...
                chunkValueAsString = ""
                chunkValue.clear()
                countDigits = 0
            } // end if
        } // end while

        currentBase = chunks.size - 1

        // Display the Japanese Version of the number...
        // That simply means, take the number and separate it into chunks of 4...
        var c = ""

        chunks.forEach{
            c += it
            c+= ","
        } // end forEach{}

        // Remove the "," that will be at the end of the String...
        c = c.substring(0, c.length - 1)

        // Display the value...
        japaneseVersionDisplay.text = c

        return chunks
    } // end getJapaneseChunks() function

    private fun readChunks(chunkList : MutableList<String>)
    {
        var i = 0
        chunkList.forEach{
            // if chunk is a single digit...
            if(it.length < 2)
            {
                readSingleDigit(it)
            }
            // if chunk is 2 digits...
            else if(it.length < 3)
            {
                readTwoDigits(it)
            }
            // if chunk is 3 digits...
            else if(it.length < 4)
            {
                readThreeDigits(it)
            }
            else if(it.length < 5)
            // if the number is 4 digits...
            {
                readFourDigits(it)
            } // end the string of ifs...

            // Add the bigger base to the number...
            if(currentBase != 0 && it.toInt() != 0)
            {
                // Add a tab before adding the base...
                kanjiReading += "\t"
                hiraganaReading += "\t"
                romanjiReading += "\t"

                kanjiReading += kanjiBaseNumbersAbove[currentBase]
                hiraganaReading += hiraganaBaseNumbersAbove[currentBase]
                romanjiReading += romanjiBaseNumbersAbove[currentBase]

                // Add 2 tabs after adding the base...
                // One space isn't enough to really separate the chunks...
                kanjiReading += "\t\t"
                hiraganaReading += "\t\t"
                romanjiReading += "\t\t"
            } // end if
            currentBase--
        } // end forEach{}

        // Remove Trailing Spaces...
        kanjiReading = kanjiReading.trim()
        hiraganaReading = hiraganaReading.trim()
        romanjiReading = romanjiReading.trim()

        kanjiDisplay.text = kanjiReading
        hiraganaDisplay.text = hiraganaReading
        romanjiDisplay.text = romanjiReading
    } // end readChunks() function

    private fun readSingleDigit(digit : String)
    {
        // Don't read the "0" values...
        // If the number was "0", then it was already read...
        if(digit.toInt() > 0) {
            kanjiReading += kanjiBaseNumbers[digit.toInt()]
            hiraganaReading += hiraganaBaseNumbers[digit.toInt()]
            romanjiReading += romanjiBaseNumbers[digit.toInt()]
        } // end if
    } // end readSingleDigit() function

    private fun readTwoDigits(digit : String)
    {
        when(digit.toInt()) {
            10 -> {
                kanjiReading += kanjiBaseNumbers[10]
                hiraganaReading += hiraganaBaseNumbers[10]
                romanjiReading += romanjiBaseNumbers[10]
            } // end 10
            else -> {
                // if the first digit is Not a "0" or a "1", add the base
                // "0" is un-necessary to say
                // "1" would imply just saying "Ten" or "Ju"
                if((digit[0].digitToInt() != 0) && (digit[0].digitToInt() != 1)) {
                    // Read first digit...
                    readSingleDigit(digit[0].toString())

                    // Add the base...
                    kanjiReading += kanjiBaseNumbers[10]
                    hiraganaReading += hiraganaBaseNumbers[10]
                    romanjiReading += romanjiBaseNumbers[10]

                    // Add a space...
                    kanjiReading += " "
                    hiraganaReading += " "
                    romanjiReading += " "
                }
                else if(digit[0].digitToInt() == 1)
                {
                    // Simply say "Ten"...
                    kanjiReading += kanjiBaseNumbers[10]
                    hiraganaReading += hiraganaBaseNumbers[10]
                    romanjiReading += romanjiBaseNumbers[10]

                    // Add a space...
                    kanjiReading += " "
                    hiraganaReading += " "
                    romanjiReading += " "
                } // end if/else if

                // Read second digit...
                readSingleDigit(digit[1].toString())
            } // end when else...
        } // end when
    } // end readTwoDigits() function

    private fun readThreeDigits(digit : String)
    {
        // if the number does not start with a "0"...
        if(digit[0].digitToInt() != 0) {
            when (digit.toInt())
            {
                100 -> {
                    kanjiReading += kanjiBaseNumbers[100]
                    hiraganaReading += hiraganaBaseNumbers[100]
                    romanjiReading += romanjiBaseNumbers[100]

                    // Add a space...
                    kanjiReading += " "
                    hiraganaReading += " "
                    romanjiReading += " "
                } // end 100
                300 -> {
                    kanjiReading += kanjiBaseNumbers[300]
                    hiraganaReading += hiraganaBaseNumbers[300]
                    romanjiReading += romanjiBaseNumbers[300]

                    // Add a space...
                    kanjiReading += " "
                    hiraganaReading += " "
                    romanjiReading += " "
                } // end 300
                600 -> {
                    kanjiReading += kanjiBaseNumbers[600]
                    hiraganaReading += hiraganaBaseNumbers[600]
                    romanjiReading += romanjiBaseNumbers[600]

                    // Add a space...
                    kanjiReading += " "
                    hiraganaReading += " "
                    romanjiReading += " "
                } // end 600
                800 -> {
                    kanjiReading += kanjiBaseNumbers[800]
                    hiraganaReading += hiraganaBaseNumbers[800]
                    romanjiReading += romanjiBaseNumbers[800]

                    // Add a space...
                    kanjiReading += " "
                    hiraganaReading += " "
                    romanjiReading += " "
                } // end 800
                else -> {
                    // if the first digit is a "1"...
                    if(digit[0].digitToInt() == 1) {
                        // Simply say "Hundred"...
                        kanjiReading += kanjiBaseNumbers[100]
                        hiraganaReading += hiraganaBaseNumbers[100]
                        romanjiReading += romanjiBaseNumbers[100]

                        // Add a space...
                        kanjiReading += " "
                        hiraganaReading += " "
                        romanjiReading += " "

                        // Read the other digits...
                        readTwoDigits(digit.substring(1))
                    }
                    // if the first digit is a "3"...
                    else if(digit[0].digitToInt() == 3) {
                        // Simply say "300"...
                        kanjiReading += kanjiBaseNumbers[300]
                        hiraganaReading += hiraganaBaseNumbers[300]
                        romanjiReading += romanjiBaseNumbers[300]

                        // Add a space...
                        kanjiReading += " "
                        hiraganaReading += " "
                        romanjiReading += " "

                        // Read the other digits...
                        readTwoDigits(digit.substring(1))
                    }
                    // if the first digit is a "6"...
                    else if(digit[0].digitToInt() == 6) {
                        // Simply say "600"...
                        kanjiReading += kanjiBaseNumbers[600]
                        hiraganaReading += hiraganaBaseNumbers[600]
                        romanjiReading += romanjiBaseNumbers[600]

                        // Add a space...
                        kanjiReading += " "
                        hiraganaReading += " "
                        romanjiReading += " "

                        // Read the other digits...
                        readTwoDigits(digit.substring(1))
                    }
                    // if the first digit is an "8"...
                    else if(digit[0].digitToInt() == 8) {
                        // Simply say "800"...
                        kanjiReading += kanjiBaseNumbers[800]
                        hiraganaReading += hiraganaBaseNumbers[800]
                        romanjiReading += romanjiBaseNumbers[800]

                        // Add a space...
                        kanjiReading += " "
                        hiraganaReading += " "
                        romanjiReading += " "

                        // Read the other digits...
                        readTwoDigits(digit.substring(1))
                    }
                    else
                    {
                        // Read first digit...
                        readSingleDigit(digit[0].toString())

                        // Add the base...
                        kanjiReading += kanjiBaseNumbers[100]
                        hiraganaReading += hiraganaBaseNumbers[100]
                        romanjiReading += romanjiBaseNumbers[100]

                        // Add a space...
                        kanjiReading += " "
                        hiraganaReading += " "
                        romanjiReading += " "

                        // Read the other digits...
                        readTwoDigits(digit.substring(1))
                    } // end string of if/else
                } // end when else...
            } // end when
        }
        // if the number started with a "0"...
        else
        {
            readTwoDigits(digit.substring(1))
        } // end if/else
    } // end readThreeDigits() function
    private fun readFourDigits(digit : String)
    {
        // if the number does not start with a "0"...
        if(digit[0].digitToInt() != 0) {
            when (digit.toInt())
            {
                1000 -> {
                    kanjiReading += kanjiBaseNumbers[1000]
                    hiraganaReading += hiraganaBaseNumbers[1000]
                    romanjiReading += romanjiBaseNumbers[1000]

                    // Add a space...
                    kanjiReading += " "
                    hiraganaReading += " "
                    romanjiReading += " "
                } // end 1000
                3000 -> {
                    kanjiReading += kanjiBaseNumbers[3000]
                    hiraganaReading += hiraganaBaseNumbers[3000]
                    romanjiReading += romanjiBaseNumbers[3000]

                    // Add a space...
                    kanjiReading += " "
                    hiraganaReading += " "
                    romanjiReading += " "
                } // end 3000
                8000 -> {
                    kanjiReading += kanjiBaseNumbers[8000]
                    hiraganaReading += hiraganaBaseNumbers[8000]
                    romanjiReading += romanjiBaseNumbers[8000]

                    // Add a space...
                    kanjiReading += " "
                    hiraganaReading += " "
                    romanjiReading += " "
                } // end 8000
                else -> {
                    // if the first digit is a "1"...
                    if(digit[0].digitToInt() == 1) {
                        // Simply say "Thousand"...
                        kanjiReading += kanjiBaseNumbers[1000]
                        hiraganaReading += hiraganaBaseNumbers[1000]
                        romanjiReading += romanjiBaseNumbers[1000]

                        // Add a space...
                        kanjiReading += " "
                        hiraganaReading += " "
                        romanjiReading += " "

                        // Read the other digits...
                        readThreeDigits(digit.substring(1))
                    }
                    // if the first digit is a "3"...
                    else if(digit[0].digitToInt() == 3) {
                        // Simply say "3,000"...
                        kanjiReading += kanjiBaseNumbers[3000]
                        hiraganaReading += hiraganaBaseNumbers[3000]
                        romanjiReading += romanjiBaseNumbers[3000]

                        // Add a space...
                        kanjiReading += " "
                        hiraganaReading += " "
                        romanjiReading += " "

                        // Read the other digits...
                        readThreeDigits(digit.substring(1))
                    }
                    // if the first digit is a "8"...
                    else if(digit[0].digitToInt() == 8) {
                        // Simply say "8,000"...
                        kanjiReading += kanjiBaseNumbers[8000]
                        hiraganaReading += hiraganaBaseNumbers[8000]
                        romanjiReading += romanjiBaseNumbers[8000]

                        // Add a space...
                        kanjiReading += " "
                        hiraganaReading += " "
                        romanjiReading += " "

                        // Read the other digits...
                        readThreeDigits(digit.substring(1))
                    }
                    else
                    {
                        // Read first digit...
                        readSingleDigit(digit[0].toString())

                        // Add the base...
                        kanjiReading += kanjiBaseNumbers[1000]
                        hiraganaReading += hiraganaBaseNumbers[1000]
                        romanjiReading += romanjiBaseNumbers[1000]

                        // Add a space...
                        kanjiReading += " "
                        hiraganaReading += " "
                        romanjiReading += " "

                        // Read the other digits...
                        readThreeDigits(digit.substring(1))
                    } // end string of if/else
                } // end when else...
            } // end when
        }
        // if the number started with a "0"...
        else
        {
            // Ignore that "0" and just read the other digits...
            readThreeDigits(digit.substring(1))
        } // end if/else
    } // end readFourDigits() function
} // end Kanji_Numbers_Custom_Activity class
