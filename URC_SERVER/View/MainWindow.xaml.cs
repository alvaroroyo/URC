using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using URC_Server.ViewModel;


namespace URC_Server
{
    /// <summary>
    /// Lógica de interacción para MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private static ConexionViewModel conexionesViewModel;
        private static MainWindowViewModel mainWindow;
        public MainWindow()
        {
            InitializeComponent();

            //Resource.Culture = new System.Globalization.CultureInfo("en");

            mainWindow = new MainWindowViewModel();
            this.DataContext = mainWindow;
        }

        public static void setStatusBar(string sms)
        {
            if(sms != null)
                mainWindow.ErrorSMS = sms;
        }

        public static void setConexion(ConexionViewModel conexiones)
        {
            conexionesViewModel = conexiones;
        }

        private void Window_Closing(object sender, CancelEventArgs e)
        {
            mainWindow.saveConfig();
            try
            {
                conexionesViewModel.closeAllConexions();
            }
            catch (Exception ex)
            {

            }
        }

    }
}
