using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Forms;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using URC_Server.Model.Configuraciones;

namespace URC_Server.View.Dialogs
{
    /// <summary>
    /// Lógica de interacción para AsignarTeclaDialog.xaml
    /// </summary>
    public partial class AsignarTeclaDialog : Window
    {
        private Int32 _tecla;
        public AsignarTeclaDialog()
        {
            InitializeComponent();
            this.KeyDown += new System.Windows.Input.KeyEventHandler(onKeyDown);
        }

        private void onKeyDown(object sender, System.Windows.Input.KeyEventArgs e)
        {
            try
            {
                _tecla = KeyInterop.VirtualKeyFromKey(e.Key);
            }
            catch (Exception ex){}
            this.DialogResult = true;
        }

        public Int32 Tecla { get { return _tecla; } }

    }
}
