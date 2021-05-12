using System;
using System.Collections.Generic;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;

namespace Japanese_Study_Helper
{
    public partial class EndWindowAllCorrect : Window
    {
        public EndWindowAllCorrect()
        {
            InitializeComponent();

            positionElements();
        } // end EndWindowAllCorrect() method

        private void Back_To_Main_Menu_Button_Click(object sender, RoutedEventArgs e)
        {
            MainWindow window = new MainWindow();
            window.Show();
            this.Hide();
            System.Windows.Threading.Dispatcher.Run();
        } // end Back_To_Main_Menu_Button_Click() method

        private void positionElements()
        {
            double width = SystemParameters.PrimaryScreenWidth;
            double height = SystemParameters.PrimaryScreenHeight;

            AllCorrectLabel.Margin = new Thickness(width * 0.50 - AllCorrectLabel.Width * 0.50, 0, 0, 0);

            BackToMainWindow.Margin = new Thickness(1082, 949, 0, 0);

            // 100 pixel buffer for each side
            Congrats_Image.Width = width - 200;

            // take into account the other elements
            Congrats_Image.Height = height * 0.79;

            Congrats_Image.Margin = new Thickness(100, -AllCorrectLabel.Height / 2, 0, 0);
        } // end positionElements() method

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
