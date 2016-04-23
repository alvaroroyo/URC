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
using URC_Server.Model;

namespace URC_Server.Controls
{
    /// <summary>
    /// Lógica de interacción para Incrementer.xaml
    /// </summary>
    public partial class Incrementer : UserControl
    {
        public static readonly DependencyProperty _text = DependencyProperty.Register("Texto", typeof(int), typeof(Incrementer),null);
        public static readonly DependencyProperty _min = DependencyProperty.Register("Min", typeof(int), typeof(Incrementer), null);
        public static readonly DependencyProperty _max = DependencyProperty.Register("Max", typeof(int), typeof(Incrementer), null);

        public Incrementer()
        {
            InitializeComponent();

            Texto = 0;
            Min = 0;
            Max = 999;

            BtnMax = new RelayCommand(BtnMax_E, BtnMax_CE);
            BtnMin = new RelayCommand(BtnMin_E, BtnMin_CE);

            this.DataContext = this;
        }

        public int Texto { get { return (int)GetValue(_text); } set { SetValue(_text, value); } }
        public int Min { get { return (int)GetValue(_min); } set { SetValue(_min, value); } }
        public int Max { get { return (int)GetValue(_max); } set { SetValue(_max, value); } }

        #region COMMANDS

        public ICommand BtnMax { get; set; }
        public ICommand BtnMin { get; set; }

        public bool BtnMax_CE(object obj)
        {
            return (Texto < Max) ? true : false;
        }
        public void BtnMax_E(object obj)
        {
            Texto++;
        }

        public bool BtnMin_CE(object obj)
        {
            return (Texto > Min) ? true : false;
        }
        public void BtnMin_E(object obj)
        {
            Texto--;
        }

        #endregion

    }
}
