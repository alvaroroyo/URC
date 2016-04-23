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
using URC_Server.Model.Configuraciones;

namespace URC_Server.View
{
    /// <summary>
    /// Lógica de interacción para NewGroupDialog.xaml
    /// </summary>
    public partial class NewGroupDialog : Window
    {
        public NewGroupDialog()
        {
            InitializeComponent();
        }

        public string Answer { get { return this.txtGroupName.Text; } }

        private void Window_ContentRendered(object sender, EventArgs e)
        {
            txtGroupName.SelectAll();
            txtGroupName.Focus();
        }

        private void btnDialogOk_Click(object sender, RoutedEventArgs e)
        {
            this.DialogResult = true;
        }

    }
}
