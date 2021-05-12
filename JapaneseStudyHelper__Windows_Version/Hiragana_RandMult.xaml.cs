using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;

namespace Japanese_Study_Helper
{
   public partial class Hiragana_RandMult : Window
    {
        // Used to generate Random numbers
        Random rand;

        // Used to read the respective files
        private System.IO.StreamReader Hfile, Rfile;

        // Used to track the missed characters to help display them later...
        public static List<String> missedHiraganaCharacters, missedRanji;

        // Used to track the specific Japanese character missed to help display it at the end of the program...
        private String missedHiraganaCharacter = "";
        private String missedRanjiCharacter = "";

        // Used to store the Japanese characters...
        public static List<String> allHiraganaCharacters, allRanji;

        // Used to read the respective files
        // C# is dumb and there is no way to simply read a file String by String
        // C# also reads the characters as the Ascii value and not the readable version that everybody would assume would be standard
        // these values are what the file reader returns
        private int hiraganaChar, ranjiChar;

        // Used to get the complete ranji pronounciation of a Japanese character ("Chi" and "Tsu" and everything else that is >= 2 characters)
        // it turns out that many Hiragana characters are more than one character long, so the same logic applies for them
        private String hira, ranji;

        // Used to keep track of which character is the correct character...
        int currentCharacter = 0;

        // Used to check if all of the characters have been used...
        int countToEndOfGame = 0;

        public Hiragana_RandMult()
        {
            InitializeComponent();

            // Initializes some variables that might not ever get initialized otherwise...
            missedHiraganaCharacters = new List<string>();
            missedRanji = new List<string>();
            rand = new Random();

            // postitions the content onto the screen
            setContent();

            // Read the files and get their contents
            readFiles();

            // Gets the total number of Japanese characters to help with determining when at the end of the game...
            countToEndOfGame = allHiraganaCharacters.Count;

            // Helps with debugging...
            //            printContent();

            // Assing the answer choices to the buttons
            assignAnswers();

        } // end RandMult() method

        // Debug stuff...
        void printContent()
        {
            Test_Things_Only window = new Test_Things_Only();
            window.Show();
            this.Hide();
            System.Windows.Threading.Dispatcher.Run();
        } // end printContent() method

        private void Continue_Button_Click(object sender, RoutedEventArgs e)
        {
            // if at the end of the game...
            if (countToEndOfGame == 0)
            {
                MessageBox.Show("You have made it to the end of the game.\nNice Job!");

                // if the user got everything correct...
                if (missedHiraganaCharacters.Count == 0)
                {
                    EndWindowAllCorrect window = new EndWindowAllCorrect();
                    window.Show();
                    this.Hide();
                    System.Windows.Threading.Dispatcher.Run();
                }
                // if the user missed 1 or more characters...
                else
                {
                    EndWindow_Hiragana_RandMult_NotPerfect window = new EndWindow_Hiragana_RandMult_NotPerfect();
                    window.Show();
                    this.Hide();
                    System.Windows.Threading.Dispatcher.Run();
                } // end if/else
            } // end if

            // hide these to get ready for the next character
            CorrectLabel.Visibility = Visibility.Hidden;
            ContinueButton.Visibility = Visibility.Hidden;

            // reset the buttons
            resetButtons();

            // assign the answer choices
            assignAnswers();
        } // end Continue_Button_Click() click

        private void Check_Answer_Button_Click(object sender, RoutedEventArgs e)
        {
            // the button that was clicked...
            Button button = (Button)sender;

            // used to help keep track of what buttons have been given a value
            List<Button> answerButtons = new List<Button>();
            answerButtons.Add(TopLeftButton);
            answerButtons.Add(TopRightButton);
            answerButtons.Add(BottomLeftButton);
            answerButtons.Add(BottomRightButton);

            // prevent the user from changing answers...
            foreach (Button b in answerButtons)
            {
                b.IsEnabled = false;
            } // end foreach()

            // if correct...
            if (button.Content.Equals(allRanji[currentCharacter]))
            {
                CorrectLabel.Content = "Correct!";
                CorrectLabel.Background = new SolidColorBrush(Colors.Transparent);
                CorrectLabel.Foreground = new SolidColorBrush(Colors.Green);
                CorrectLabel.Visibility = Visibility.Visible;
                ContinueButton.Visibility = Visibility.Visible;

                button.Background = new SolidColorBrush(Colors.Green);

                // set the correct answer button to have a Green background
                BrushConverter bc = new BrushConverter();
                foreach (Button b in answerButtons)
                {
                    if (!b.Equals(button))
                    {
                        b.Background = (Brush)bc.ConvertFrom("#FF27253A");
                    } // end if

                } // end foreach()

                // increase the total score
                int score = Convert.ToInt32(CorrectCounterLabel.Content);
                CorrectCounterLabel.Content = (++score).ToString();
            }
            // if wrong...
            else
            {
                CorrectLabel.Content = "Wrong!";
                CorrectLabel.Background = new SolidColorBrush(Colors.Transparent);
                CorrectLabel.Foreground = new SolidColorBrush(Colors.Red);
                CorrectLabel.Visibility = Visibility.Visible;
                ContinueButton.Visibility = Visibility.Visible;

                // set the selected button background to be Red to show that it was wrong
                button.Background = new SolidColorBrush(Colors.Red);

                // disable all buttons to prevent the user from entering multiple answers
                // sets the background of the correct button to be Green
                // sets any buttons not involved to have a Grayish background
                BrushConverter bc = new BrushConverter();
                foreach (Button b in answerButtons)
                {
                    if (b.Content.Equals(allRanji[currentCharacter]))
                    {
                        b.Background = new SolidColorBrush(Colors.Green);
                    }
                    else if (!b.Equals(button))
                    {
                        b.Background = (Brush)bc.ConvertFrom("#FF27253A");
                    } // end if/else
                } // end foreach()

                // increase the score
                int score = Convert.ToInt32(WrongCounterLabel.Content);
                WrongCounterLabel.Content = (++score).ToString();

                // keep track of the missed characters to help display them later
                missedHiraganaCharacters.Add(missedHiraganaCharacter);
                missedRanji.Add(missedRanjiCharacter);
            } // end if/else
        } // end Check_Answer_Button_Click() method

        void readFiles()
        {
            // Actually create the lists to use them
            allHiraganaCharacters = new List<String>();
            allRanji = new List<String>();

            // Helps make sure anybody can use this project without having to change any code (ie change directory strings)
            // just be sure to have the txt files in the parent folder
            String projectDirectory = Directory.GetParent(Environment.CurrentDirectory).Parent.FullName;
            String hiraganaPath = projectDirectory + @"\hiragana.txt";
            String ranjiPath = projectDirectory + @"\ranji.txt";

            // if the file exists... create reader...
            if (File.Exists(hiraganaPath))
            {
                Hfile = new System.IO.StreamReader(hiraganaPath);
            }
            // if no file exists... quit life...
            else
            {
                MessageBox.Show("\"hiragana.txt\" File Does Not Exist. Terminating Program...");
                System.Windows.Application.Current.Shutdown();
            } // end if/else

            // if the file exists... create reader...
            if (File.Exists(ranjiPath))
            {
                Rfile = new System.IO.StreamReader(ranjiPath);
            }
            // if no file exists... quit life...
            else
            {
                MessageBox.Show("\"ranji.txt\" File Does Not Exist. Terminating Program...");
                System.Windows.Application.Current.Shutdown();
            } // end if/else

            // Gets the first character of the Hiragana File...
            hiraganaChar = Hfile.Read();

            // while not at the end of the file...
            while (hiraganaChar != -1)
            {
                // used to start the String version of the ranji characters
                // must be done because some ranji characters are 1 character and others are 3 and most are 2
                hira += (((char)hiraganaChar).ToString());

                // reads the file until a space or newline is encountered, then assign the character...
                // Trim() is needed because C# is dumb
                // Note: apparently "\n" and "\r\n" are the same (newline) but are different in terms of how the computer reads them
                // C# is too dumb so both were included to be safe
                // Note: Checked to see if the next character exists because if it doesn't, the reader is on the last character currently, and
                // the way the code works, at the end of it all, the reader will read a new character, but that character wouldn't exist
                // that would break the while loop and not put the last character "N" into the ranjiChars list
                if (hira.Contains(" ") || hira.Contains("\n") || hira.Contains("\r\n") && Hfile.Peek() != -1)
                {
                    hira.Trim();
                    allHiraganaCharacters.Add(hira);
                    hira = "";
                } // end if

                if (Hfile.Peek() == -1)
                {
                    hira.Trim();
                    allHiraganaCharacters.Add(hira);
                    hira = "";
                } // end if

                hiraganaChar = Hfile.Read();
            } // end while()
            Hfile.Close();

            // Similar to above, a few changes though...
            ranjiChar = Rfile.Read();
            while (ranjiChar != -1)
            {
                // used to start the String version of the ranji characters
                // must be done because some ranji characters are 1 character and others are 3 and most are 2
                ranji += (((char)ranjiChar).ToString());

                // what I had to do was read until a space or new line was read from the file
                // when one was read, it can be assumed that the ranji character has been completed
                // so trim the fat to be safe and add to the ranjiChars list and reset ranji to start the next character
                // Note: apparently "\n" and "\r\n" are the same (newline) but are different in terms of how the computer reads them
                // C# is too dumb so both were included to be safe
                // Note: Checked to see if the next character exists because if it doesn't, the reader is on the last character currently
                // the way the code works, at the end of it all, the reader will read a new character, but that character wouldn't exist
                // that would break the while loop and not put the last character "N" into the ranjiChars list
                if (ranji.Contains(" ") || ranji.Contains("\n") || ranji.Contains("\r\n") && Rfile.Peek() != -1)
                {
                    ranji.Trim();
                    allRanji.Add(ranji);
                    ranji = "";
                } // end if

                if (Rfile.Peek() == -1)
                {
                    ranji.Trim();
                    allRanji.Add(ranji);
                    ranji = "";
                } // end if

                ranjiChar = Rfile.Read();
            } // end while()
            Rfile.Close();
        } // end readFiles() method

        void assignAnswers()
        {
            // used to randomly assign the answer buttons with answers
            List<Button> answerButtons = new List<Button>();
            answerButtons.Add(TopLeftButton);
            answerButtons.Add(TopRightButton);
            answerButtons.Add(BottomLeftButton);
            answerButtons.Add(BottomRightButton);

            // chooses a random character as the current character to display as long as that character hasn't been displayed before...
            currentCharacter = 0;
            do
            {
                currentCharacter = rand.Next(0, allHiraganaCharacters.Count);
            }
            while (allHiraganaCharacters[currentCharacter].Equals("*"));

            // the randomly generated button spot to put the correct answer
            int correctSpot = rand.Next(0, answerButtons.Count);

            // sets the content of the Label that displays the current character...
            CharacterLabel.Content = allHiraganaCharacters[currentCharacter];

            // sets the content of the correct answerButton to be the correct answer choice...
            answerButtons[correctSpot].Content = allRanji[currentCharacter];
            answerButtons.RemoveAt(correctSpot);

            // Makes the value in "allKatakanaCharacters" be "*" to indicate that it was already used...
            // Makes the value in "allRanji" be "*" to indicate that it was already used...
            // Gets a backup first for use if it gets missed...
            missedHiraganaCharacter = allHiraganaCharacters[currentCharacter];
            missedRanjiCharacter = allRanji[currentCharacter];
            allHiraganaCharacters[currentCharacter] = "*";

            // a backup of "allRanji" to help make sure that all Ranji choices can be a choice regardless if they showed up before
            // the choice just can't show up twice in the same window...
            List<String> allRanjiHolder = allRanji.ToList();

            allRanjiHolder[currentCharacter] = "*";

            while (answerButtons.Count != 0)
            {
                // randomly selects a wrong answer choice as long as the choice hasn't been used yet...
                int wrongAnswerChoice = 0;
                do
                {
                    wrongAnswerChoice = rand.Next(0, allRanjiHolder.Count);
                }
                while (allRanjiHolder[wrongAnswerChoice].Equals("*"));

                answerButtons[0].Content = allRanjiHolder[wrongAnswerChoice];

                allRanjiHolder[wrongAnswerChoice] = "*";

                answerButtons.RemoveAt(0);
            } // end while

            countToEndOfGame--;
        } // end assignAnswers() method

        void setContent()
        {
            // Hide the label and button until needed
            CorrectLabel.Visibility = Visibility.Hidden;
            ContinueButton.Visibility = Visibility.Hidden;

            double l = Correct.Width / 6;
            double d = Correct.Height;
            Correct.Margin = new Thickness(l, d, 0, 0);
            Wrong.Margin = new Thickness(l, Correct.Height * 1.8, 0, 0);

            CorrectCounterLabel.Height = Correct.Height;
            CorrectCounterLabel.Margin = new Thickness(l + Correct.Width, d, 0, 0);

            WrongCounterLabel.Height = Wrong.Height;
            WrongCounterLabel.Margin = new Thickness(l + Wrong.Width, Correct.Height * 1.8, 0, 0);

            double width = SystemParameters.PrimaryScreenWidth;
            double height = SystemParameters.PrimaryScreenHeight;
            CharacterLabel.Margin = new Thickness(width / 2 - CharacterLabel.Width / 2, height / 12, 0, 0);

            TopLeftButton.Margin = new Thickness(CharacterLabel.Margin.Left - TopLeftButton.Width / 2,
                                                CharacterLabel.Margin.Top + CharacterLabel.Height + TopLeftButton.Height / 2, 0, 0);

            TopRightButton.Margin = new Thickness(CharacterLabel.Margin.Left + CharacterLabel.Width - TopRightButton.Width / 2,
                                                 CharacterLabel.Margin.Top + CharacterLabel.Height + TopRightButton.Height / 2, 0, 0);

            BottomLeftButton.Margin = new Thickness(CharacterLabel.Margin.Left - BottomLeftButton.Width / 2,
                                                   CharacterLabel.Margin.Top + CharacterLabel.Height + BottomLeftButton.Height * 2, 0, 0);

            BottomRightButton.Margin = new Thickness(CharacterLabel.Margin.Left + CharacterLabel.Width - BottomRightButton.Width / 2,
                                                     CharacterLabel.Margin.Top + CharacterLabel.Height + BottomRightButton.Height * 2, 0, 0);

            CorrectLabel.Margin = new Thickness(CharacterLabel.Margin.Left + CorrectLabel.Width * 0.91 - CorrectLabel.Width * 0.5,
                                                CharacterLabel.Margin.Top + CharacterLabel.Height + CorrectLabel.Height * 2.1, 0, 0);

            ContinueButton.Margin = new Thickness(CharacterLabel.Margin.Left + ContinueButton.Width - ContinueButton.Width * 0.5,
                                                CharacterLabel.Margin.Top + CharacterLabel.Height + ContinueButton.Height * 2, 0, 0);

            BackToMainWindow.Margin = new Thickness(1082, 949, 0, 0);

        } // end setContent() method

        void resetButtons()
        {
            // used to randomly assign the answer buttons with answers
            List<Button> answerButtons = new List<Button>();
            answerButtons.Add(TopLeftButton);
            answerButtons.Add(TopRightButton);
            answerButtons.Add(BottomLeftButton);
            answerButtons.Add(BottomRightButton);

            BrushConverter bc = new BrushConverter();
            foreach (Button b in answerButtons)
            {
                b.Background = (Brush)bc.ConvertFrom("#FF090157");
                b.IsEnabled = true;
            } // end foreach()

        } // end resetButtons() method

        private void Go_Back_Button_Click(object sender, RoutedEventArgs e)
        {
            MainWindow window = new MainWindow();
            window.Show();
            this.Hide();
            System.Windows.Threading.Dispatcher.Run();
        } // end Go_Back_Button_Click() method

        private void Exit_Enviornment(object sender, System.ComponentModel.CancelEventArgs e)
        {
            Environment.Exit(0);
        } // end Exit_Enviornment() method
    } // end class
} // end namespace
