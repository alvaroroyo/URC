using System;
using System.ComponentModel;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading;
using System.Windows;
using URC_Server.Model.Configuraciones;

namespace URC_Server.ViewModel
{
    public class MainWindowViewModel :INotifyPropertyChanged
    {
        private Config _config;
        public MainWindowViewModel()
        {
            _config = Config.newConfig();
            changeLanguage(_config.SelectedLanguage);
            
        }

        private string _errorSMS;
        /// <summary>
        /// Obtiene o establece un mensaje que aparecerá en la parte inferior derecha de la ventana principal.
        /// </summary>
        public string ErrorSMS {
            get { return _errorSMS; }
            set
            {
                _errorSMS = value;
                OnPropertyChanged("ErrorSMS");

                new Thread(() => { Thread.Sleep(2500); _errorSMS = ""; OnPropertyChanged("ErrorSMS"); }).Start();
            } 
        }

        private void changeLanguage(String newLanguage)
        {
            Uri uri = new Uri("/Resources/Languages/" + newLanguage + ".xaml", UriKind.RelativeOrAbsolute);
            Application.Current.Resources.MergedDictionaries[0].Source = uri;
        }

        public void saveConfig()
        {
            _config.setConfig();
        }

        #region PropetyChange

        public event PropertyChangedEventHandler PropertyChanged;

        protected void OnPropertyChanged(string name)
        {
            PropertyChangedEventHandler handler = PropertyChanged;
            if (handler != null)
            {
                handler(this, new PropertyChangedEventArgs(name));
            }
        }
        #endregion

    }
}
