using System;
using System.Collections.Generic;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;

namespace Japanese_Study_Helper
{
    public partial class Katakana : Window
    {
        public Katakana()
        {
            InitializeComponent();

            double marg = SystemParameters.PrimaryScreenWidth;
            double down = (Welcome.Height + AlphMult.Height / 2);

            Welcome.Margin = new Thickness(marg / 2 - Welcome.Width / 2, 0, 0, 0);
            AlphMult.Margin = new Thickness(marg / 2 - AlphMult.Width / 2, down, 0, 0);
            AlphFree.Margin = new Thickness(marg / 2 - AlphFree.Width / 2, down + AlphMult.Height * 1.5, 0, 0);
            RandMult.Margin = new Thickness(marg / 2 - RandMult.Width / 2, down + AlphFree.Height * 3.0, 0, 0);
            RandFree.Margin = new Thickness(marg / 2 - RandFree.Width / 2, down + RandMult.Height * 4.5, 0, 0);
            BackToMainWindow.Margin = new Thickness(1082, 949, 0, 0);

        } // end Hiragana() method

        private void AlphMult_Button_Click(object sender, RoutedEventArgs e)
        {
            Katakana_AlphMult window = new Katakana_AlphMult();
            window.Show();
            this.Hide();
            System.Windows.Threading.Dispatcher.Run();
        } // end AlphMult_Button_Click() method

        private void AlphFree_Button_Click(object sender, RoutedEventArgs e)
        {
            Katakana_AlphFree window = new Katakana_AlphFree();
            window.Show();
            this.Hide();
            System.Windows.Threading.Dispatcher.Run();
        } // end AlphFree_Button_Click() method

        private void RandMult_Button_Click(object sender, RoutedEventArgs e)
        {
            Katakana_RandMult window = new Katakana_RandMult();
            window.Show();
            this.Hide();
            System.Windows.Threading.Dispatcher.Run();
        } // end RandMult_Button_Click() method

        private void RandFree_Button_Click(object sender, RoutedEventArgs e)
        {
            Katakana_RandFree window = new Katakana_RandFree();
            window.Show();
            this.Hide();
            System.Windows.Threading.Dispatcher.Run();
        } // end RandFree_Button_Click() method

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
