using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Interop;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using URC_Server.Model.Conexiones;
using URC_Server.Model.Configuraciones;

namespace URC_Server.View.Dialogs
{
    /// <summary>
    /// Lógica de interacción para CanConnectDialogg.xaml
    /// </summary>
    public partial class CanConnectDialog : Window
    {

        Conexion con;
        public CanConnectDialog(Conexion con)
        {
            InitializeComponent();

            CanConnect = false;

            this.Closing += CanConnectDialog_Closing;

            this.con = con;
        }

        void CanConnectDialog_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            this.DialogResult = true;
        }

        public bool CanConnect { get; private set; }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            CanConnect = true;
            this.DialogResult = true;
        }

        private void No_click(object sender, RoutedEventArgs e)
        {
            CanConnect = false;
            this.DialogResult = true;
        }

    }
}
