using System;
using System.Collections.Generic;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;

namespace Japanese_Study_Helper
{
    public partial class EndWindow_Hiragana_AlphFree_NotPerfect : Window
    {
        private List<Grid> grids = new List<Grid>();

        public EndWindow_Hiragana_AlphFree_NotPerfect()
        {
            InitializeComponent();

            positionElements();

            populateScrollBar();
        } // end EndWindow_AlphFree_NotPerfect() method

        private void positionElements()
        {
            double width = SystemParameters.PrimaryScreenWidth;
            double height = SystemParameters.PrimaryScreenHeight;

            MissedSomeLabel.Margin = new Thickness(width / 2 - MissedSomeLabel.Width / 2, 0, 0, 0);


            EndWindow_Missed_ScrollBar.Margin = new Thickness(width / 2 - EndWindow_Missed_ScrollBar.Width / 2,
                                                              MissedSomeLabel.Height + 10, 0, 0);

            EndWindow_Missed_ScrollBar.Height = height * 0.60f;

            BackToMainWindow.Margin = new Thickness(1082, 949, 0, 0);

        } // end positionElements()

        private void populateScrollBar()
        {
            EndWindow_Missed_ScrollBar.Content = "";

            String separate = "----------------------------------------------------------";

            for (int i = 0; i < Hiragana_AlphFree.missedHiraganaCharacters.Count; i++)
            {
                EndWindow_Missed_ScrollBar.Content += Hiragana_AlphFree.missedHiraganaCharacters[i].ToString().Trim();
                EndWindow_Missed_ScrollBar.Content += " --> ";
                EndWindow_Missed_ScrollBar.Content += Hiragana_AlphFree.missedRanji[i].ToString().Trim();
                EndWindow_Missed_ScrollBar.Content += System.Environment.NewLine;
                EndWindow_Missed_ScrollBar.Content += separate;
                EndWindow_Missed_ScrollBar.Content += System.Environment.NewLine;
            } // end while()

        } // end populateScrollBar() method

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
