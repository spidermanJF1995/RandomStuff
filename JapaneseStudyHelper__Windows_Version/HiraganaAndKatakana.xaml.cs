using System;
using System.Collections.Generic;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;

namespace Japanese_Study_Helper
{
    public partial class HiraganaAndKatakana : Window
    {
        public HiraganaAndKatakana()
        {
            InitializeComponent();

            double marg = SystemParameters.PrimaryScreenWidth;
            double down = (Welcome.Height + RandMult.Height / 2);

            Welcome.Margin = new Thickness(marg / 2 - Welcome.Width / 2, 0, 0, 0);
            RandMult.Margin = new Thickness(marg / 2 - RandMult.Width / 2, down, 0, 0);
            RandFree.Margin = new Thickness(marg / 2 - RandFree.Width / 2, down + RandMult.Height * 1.5, 0, 0);

            BackToMainWindow.Margin = new Thickness(1082, 949, 0, 0);

        } // end HiraganaAndKatakana() method

        private void RandMult_Button_Click(object sender, RoutedEventArgs e)
        {
            HiraganaAndKatakana_RandMult window = new HiraganaAndKatakana_RandMult();
            window.Show();
            this.Hide();
            System.Windows.Threading.Dispatcher.Run();
        } // end RandMult_Button_Click() method

        private void RandFree_Button_Click(object sender, RoutedEventArgs e)
        {
            HiraganaAndKatakana_RandFree window = new HiraganaAndKatakana_RandFree();
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
