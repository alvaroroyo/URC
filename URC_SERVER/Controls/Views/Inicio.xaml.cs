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
using System.Windows.Navigation;
using System.Windows.Shapes;
using URC_Server.ViewModel;

namespace URC_Server.Controls
{
    /// <summary>
    /// Lógica de interacción para Inicio.xaml
    /// </summary>
    public partial class Inicio_xaml : UserControl
    {
        InicioViewModel vm = new InicioViewModel();
        public Inicio_xaml()
        {
            InitializeComponent();
            this.DataContext = vm;

            this.KeyUp += Inicio_xaml_KeyUp;
            //this.ListaConexiones.Visibility = System.Windows.Visibility.Hidden;
        }

        void Inicio_xaml_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Left || e.Key == Key.Down)
            {
                vm.lastImage();
            }
            else if (e.Key == Key.Right || e.Key == Key.Up)
            {
                vm.nextImage();
            }
        }
    }
}
