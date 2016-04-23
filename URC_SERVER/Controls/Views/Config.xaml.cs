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
    /// Lógica de interacción para Config.xaml
    /// </summary>
    public partial class Config : UserControl
    {
        public Config()
        {
            InitializeComponent();
            ConfigViewModel config = new ConfigViewModel();
            this.DataContext = config;

            this.Incremente.SetBinding(Incrementer._text, new Binding { 
                Source = DataContext,
                Path = new PropertyPath("MaxConnections"),
                Mode = BindingMode.TwoWay
            });
        }

    }
}
