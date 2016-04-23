using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using URC_Server.Model.Configuraciones;

namespace URC_Server.ViewModel
{
    class ConfigViewModel
    {
        private Config _config;

        private String _selectedLanguage;

        public ConfigViewModel()
        {
            _config = Config.newConfig();
            foreach (String key in _config.Languages.Keys)
            {
                if (_config.Languages[key] == true)
                    _selectedLanguage = key;
            }
        }

        public Dictionary<String, Boolean> Languages { get { return _config.Languages; } }

        public String SelectedLanguage { get { return _selectedLanguage; } set { _config.selectLanguage(_selectedLanguage, value); _selectedLanguage = value; changeLanguage(value); } }

        public int MaxConnections { get { return _config.MaxConnections; } set { _config.MaxConnections = value; } }

        public bool QuestionBeforeConnection { get { return _config.QuestionBeforeConnection; } set { _config.QuestionBeforeConnection = value; } }

        public int MouseSensibility { get { return _config.MouseSensibility; } set { _config.MouseSensibility = value; } }

        public int ScrollSensibility { get { return _config.ScrollSensibility; } set { _config.ScrollSensibility = value; } }

        private void changeLanguage(String newLanguage)
        {
            Uri uri = new Uri("/Resources/Languages/" + newLanguage + ".xaml", UriKind.RelativeOrAbsolute);
            Application.Current.Resources.MergedDictionaries[0].Source = uri;
        }


    }
}
