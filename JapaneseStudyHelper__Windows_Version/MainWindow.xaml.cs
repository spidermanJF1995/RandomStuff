using System;
using System.Windows;

namespace Japanese_Study_Helper
{
    /*
     * Comments should be read with caution. I give up on them. I also copy/paste a lot
     * Making all of the comments perfect is too much to do given that this project is just a pet one anyway..
     * 
     * Maybe at a later time I will fix them, until then, that isn't happening...
     */

    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();

            double marg = SystemParameters.PrimaryScreenWidth;
            double down = (Welcome.Height + Hiragana.Height / 2);

            Welcome.Margin = new Thickness(marg / 2 - Welcome.Width / 2, 0, 0, 0);
            Hiragana.Margin = new Thickness(marg / 2 - Hiragana.Width / 2, down, 0, 0);
            Katakana.Margin = new Thickness(marg / 2 - Katakana.Width / 2, down + Katakana.Height * 1.5, 0, 0);
            HiraganaAndKatakana.Margin = new Thickness(marg / 2 - HiraganaAndKatakana.Width / 2, down + HiraganaAndKatakana.Height * 3.0, 0, 0);
        } // end MainWindow() method

        private void Hiragana_Button_Click(object sender, RoutedEventArgs e)
        {
            Hiragana window = new Hiragana();
            window.Show();
            this.Hide();
            System.Windows.Threading.Dispatcher.Run();
        } // end Hiragana_Button_Click() method

        private void Katakana_Button_Click(object sender, RoutedEventArgs e)
        {
            Katakana window = new Katakana();
            window.Show();
            this.Hide();
            System.Windows.Threading.Dispatcher.Run();
        } // end Katakana_Button_Click() method

        private void Hiragana_And_Katakana_Button_Click(object sender, RoutedEventArgs e)
        {
            HiraganaAndKatakana window = new HiraganaAndKatakana();
            window.Show();
            this.Hide();
            System.Windows.Threading.Dispatcher.Run();
        } // end Hiragana_And_Katakana_Button_Click() method

        private void Main_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            Environment.Exit(0);
        } // end Main_Closing() method
    } // end class
} // end namespace
