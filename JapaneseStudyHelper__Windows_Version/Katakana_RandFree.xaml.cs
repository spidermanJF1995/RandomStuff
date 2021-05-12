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
    public partial class Katakana_RandFree : Window
    {
        // generates a random number...
        Random rand;

        // Used to read the respective files
        private System.IO.StreamReader Kfile, Rfile;

        // Used to track the missed characters to help display them later...
        public static List<String> missedKatakanaCharacters, missedRanji;

        // Used to track the specific Japanese character missed to help display it at the end of the program...
        private String missedKatakanaCharacter = "";
        private String missedRanjiCharacter = "";

        // Used to store the Japanese characters...
        public static List<String> allKatakanaCharacters, allRanji;
        private String currentKatakanaCharacter = "";
        private String currentRanjiCharacter = "";

        // Used to read the respective files
        // C# is dumb and there is no way to simply read a file String by String
        // C# also reads the characters as the Ascii value and not the readable version that everybody would assume would be standard
        // these values are what the file reader returns
        private int katakanaChar, ranjiChar;

        // Used to get the complete ranji pronounciation of a Japanese character ("Chi" and "Tsu" and everything else that is >= 2 characters)
        // it turns out that many Hiragana characters are more than one character long, so the same logic applies for them
        private String kata, ranji;

        // Used to keep track of which character is the correct character...
        int currentCharacter = 0;

        // Used to check if all of the characters have been used...
        int countToEndOfGame = 0;

        // Used to get the User's answer...
        private String txtboxDefault = "Enter Your Answer Here (In Ranji)";

        public Katakana_RandFree()
        {
            InitializeComponent();

            // Initializes some variables that might not ever get initialized otherwise...
            missedKatakanaCharacters = new List<string>();
            missedRanji = new List<string>();
            rand = new Random();

            // postitions the content onto the screen
            setContent();

            // Read the files and get their contents
            readFiles();

            // Gets the total number of Japanese characters to help with determining when at the end of the game...
            countToEndOfGame = allKatakanaCharacters.Count;

            // Helps with debugging...
            //            printContent();

            // Assing the answer choices to the buttons
            assignAnswers();

        } // end RandFree() method

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
                if (missedKatakanaCharacters.Count == 0)
                {
                    EndWindowAllCorrect window = new EndWindowAllCorrect();
                    window.Show();
                    this.Hide();
                    System.Windows.Threading.Dispatcher.Run();
                }
                // if the user missed 1 or more characters...
                else
                {
                    EndWindow_Katakana_RandFree_Not_Perfect window = new EndWindow_Katakana_RandFree_Not_Perfect();
                    window.Show();
                    this.Hide();
                    System.Windows.Threading.Dispatcher.Run();
                } // end if/else
            } // end if

            CorrectLabel.Visibility = Visibility.Hidden;
            ContinueButton.Visibility = Visibility.Hidden;

            resetButtons();

            assignAnswers();
        } // end Continue_Button_Click() click

        private void Check_Answer_Button_Click(object sender, RoutedEventArgs e)
        {
            Button button = (Button)sender;

            EnterButton.IsEnabled = false;
            AnswerChoiceTextBox.IsHitTestVisible = false;

            // if correct...
            // for some reason, "Trim()" has to be used to compare "TxtBox.Text" to another String
            if ((AnswerChoiceTextBox.Text.ToString().ToUpper().Trim()).Equals(currentRanjiCharacter.ToUpper().Trim()))
            {
                CorrectLabel.Content = "Correct!";
                CorrectLabel.Background = new SolidColorBrush(Colors.Transparent);
                CorrectLabel.Foreground = new SolidColorBrush(Colors.Green);
                EnterButton.Background = new SolidColorBrush(Colors.Green);

                CorrectLabel.Visibility = Visibility.Visible;
                ContinueButton.Visibility = Visibility.Visible;

                int score = Convert.ToInt32(CorrectCounterLabel.Content);
                CorrectCounterLabel.Content = (++score).ToString();
            }
            else
            {
                CorrectLabel.Content = "Wrong! The answer was " + allRanji[currentCharacter].ToString().Trim();
                CorrectLabel.Background = new SolidColorBrush(Colors.Transparent);
                CorrectLabel.Foreground = new SolidColorBrush(Colors.Red);
                EnterButton.Background = new SolidColorBrush(Colors.Red);

                CorrectLabel.Visibility = Visibility.Visible;
                ContinueButton.Visibility = Visibility.Visible;

                button.Background = new SolidColorBrush(Colors.Red);

                int score = Convert.ToInt32(WrongCounterLabel.Content);
                WrongCounterLabel.Content = (++score).ToString();

                missedKatakanaCharacters.Add(currentKatakanaCharacter);
                missedRanji.Add(currentRanjiCharacter);
            } // end if/else

        } // end Check_Answer_Button_Click() method
        void readFiles()
        {
            // Actually create the lists to use them
            allKatakanaCharacters = new List<String>();
            allRanji = new List<String>();

            // Helps make sure anybody can use this project without having to change any code (ie change directory strings)
            // just be sure to have the txt files in the parent folder
            String projectDirectory = Directory.GetParent(Environment.CurrentDirectory).Parent.FullName;
            String katakanaPath = projectDirectory + @"\katakana.txt";
            String ranjiPath = projectDirectory + @"\ranji.txt";

            // if the file exists... create reader...
            if (File.Exists(katakanaPath))
            {
                Kfile = new System.IO.StreamReader(katakanaPath);
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
            katakanaChar = Kfile.Read();

            // while not at the end of the file...
            while (katakanaChar != -1)
            {
                // used to start the String version of the ranji characters
                // must be done because some ranji characters are 1 character and others are 3 and most are 2
                kata += (((char)katakanaChar).ToString());

                // reads the file until a space or newline is encountered, then assign the character...
                // Trim() is needed because C# is dumb
                // Note: apparently "\n" and "\r\n" are the same (newline) but are different in terms of how the computer reads them
                // C# is too dumb so both were included to be safe
                // Note: Checked to see if the next character exists because if it doesn't, the reader is on the last character currently, and
                // the way the code works, at the end of it all, the reader will read a new character, but that character wouldn't exist
                // that would break the while loop and not put the last character "N" into the ranjiChars list
                if (kata.Contains(" ") || kata.Contains("\n") || kata.Contains("\r\n") && Kfile.Peek() != -1)
                {
                    kata.Trim();
                    allKatakanaCharacters.Add(kata);
                    kata = "";
                } // end if

                if (Kfile.Peek() == -1)
                {
                    kata.Trim();
                    allKatakanaCharacters.Add(kata);
                    kata = "";
                } // end if

                katakanaChar = Kfile.Read();
            } // end while()
            Kfile.Close();

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

            // Gets 2 copies of the Ranji file to account for the Hiragana AND Katakana characters
            allRanji.AddRange(allRanji);
        } // end readFiles() method

        void assignAnswers()
        {
            // chooses a random character as the current character to display as long as that character hasn't been displayed before...
            currentCharacter = 0;
            do
            {
                currentCharacter = rand.Next(0, allKatakanaCharacters.Count);
            }
            while (allKatakanaCharacters[currentCharacter].Equals("*"));

            // sets the content of the Label that displays the current character...
            CharacterLabel.Content = allKatakanaCharacters[currentCharacter];

            // keeps track of what character the game is currently on just in case the User misses it...
            currentKatakanaCharacter = allKatakanaCharacters[currentCharacter];
            currentRanjiCharacter = allRanji[currentCharacter];

            // Makes the value in "allKatakanaCharacters" be "*" to indicate that it was already used...
            // Makes the value in "allRanji" be "*" to indicate that it was already used...
            // Gets a backup first for use if it gets missed...
            missedKatakanaCharacter = allKatakanaCharacters[currentCharacter];
            missedRanjiCharacter = allRanji[currentCharacter];
            allKatakanaCharacters[currentCharacter] = "*";

            countToEndOfGame--;
        } // end assignAnswers() method

        // Focuses the textbox and clears the information in it 
        // only clears the information once
        private void AnswerTextBox_Clicked(object sender, KeyboardFocusChangedEventArgs e)
        {
            TextBox txtBox = (TextBox)sender;

            txtBox.Text = String.Empty;
            txtBox.GotKeyboardFocus -= AnswerTextBox_Clicked;
        } // end AnswerTextBox_Clicked() method

        // makes nothing be focused if the window is clicked on
        private void Window_MouseDown(object sender, MouseButtonEventArgs e)
        {
            AnswerChoiceTextBox.Text = txtboxDefault;
            Keyboard.ClearFocus();
            AnswerChoiceTextBox.GotKeyboardFocus += AnswerTextBox_Clicked;
        } // end Window_MouseDown() method

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

            EnterButton.Margin = new Thickness(CharacterLabel.Margin.Left + CharacterLabel.Width - EnterButton.Width / 2,
                                               CharacterLabel.Margin.Top + CharacterLabel.Height + EnterButton.Height,
                                               0, 0);

            CorrectLabel.Margin = new Thickness(CharacterLabel.Margin.Left - CorrectLabel.Width * 0.25,
                                                CharacterLabel.Margin.Top + CharacterLabel.Height + CorrectLabel.Height * 2.1,
                                                0, 0);

            ContinueButton.Margin = new Thickness(CharacterLabel.Margin.Left + ContinueButton.Width - ContinueButton.Width * 0.5,
                                                 Height * 0.83, 0, 0);

            AnswerChoiceTextBox.Margin = new Thickness(CharacterLabel.Margin.Left - EnterButton.Width / 2,
                                                       CharacterLabel.Margin.Top + CharacterLabel.Height + AnswerChoiceTextBox.Height / 2,
                                                       0, 0);

            BackToMainWindow.Margin = new Thickness(1082, 949, 0, 0);

        } // end setContent() method

        void resetButtons()
        {
            EnterButton.IsEnabled = true;

            BrushConverter bc = new BrushConverter();
            EnterButton.Background = (Brush)bc.ConvertFrom("#FF090157");

            AnswerChoiceTextBox.Text = "";
            AnswerChoiceTextBox.GotKeyboardFocus += AnswerTextBox_Clicked;
            AnswerChoiceTextBox.IsHitTestVisible = true;
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
