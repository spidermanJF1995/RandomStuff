using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace Japanese_Study_Helper
{
    public partial class EndWindow_HiraganaAndKatakana_RandMult_Not_Perfect : Window
    {
        private List<Grid> grids = new List<Grid>();

        public EndWindow_HiraganaAndKatakana_RandMult_Not_Perfect()
        {
            InitializeComponent();

            positionElements();

            populateScrollBar();

        } // end EndWindow_HiraganaAndKatakana_RandFree_Not_Perfect() method

        private void positionElements()
        {
            double width = SystemParameters.PrimaryScreenWidth;
            double height = SystemParameters.PrimaryScreenHeight;

            MissedSomeLabel.Margin = new Thickness(width / 2 - MissedSomeLabel.Width / 2, 0, 0, 0);

            BackToMainWindow.Margin = new Thickness(1082, 949, 0, 0);

            EndWindow_Missed_ScrollBar.Margin = new Thickness(width / 2 - EndWindow_Missed_ScrollBar.Width / 2,
                                                              MissedSomeLabel.Height + 10, 0, 0);

            EndWindow_Missed_ScrollBar.Height = height * 0.60f;
        } // end positionElements()

        private void populateScrollBar()
        {
            EndWindow_Missed_ScrollBar.Content = "";

            String separate = "----------------------------------------------------------";

            for (int i = 0; i < HiraganaAndKatakana_RandMult.missedJapaneseCharacters.Count; i++)
            {
                EndWindow_Missed_ScrollBar.Content += HiraganaAndKatakana_RandMult.missedJapaneseCharacters.ToArray()[i].ToString().Trim();
                EndWindow_Missed_ScrollBar.Content += " --> ";
                EndWindow_Missed_ScrollBar.Content += HiraganaAndKatakana_RandMult.missedRanji.ToArray()[i].ToString().Trim();
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
