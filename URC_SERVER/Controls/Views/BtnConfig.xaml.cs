﻿using System;
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
    /// Lógica de interacción para BtnConfig.xaml
    /// </summary>
    public partial class BtnConfig : UserControl
    {
        public BtnConfig()
        {
            InitializeComponent();
            this.DataContext = new BtnConfigViewModel();
        }
    }
}
