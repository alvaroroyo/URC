using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace URC_Server.Model.Configuraciones
{
    public class Config : INotifyPropertyChanged
    {
        private string fileUri = @"Configuracion/config.xml";

        #region Propiedades
        private Dictionary<String, Boolean> _languages;
        public Dictionary<String, Boolean> Languages { get { return _languages; } }

        private int _maxConnections;
        public int MaxConnections { get { return _maxConnections; } set { _maxConnections = value; OnPropertyChanged("MaxConnections"); } }

        private bool _questionBeforeConnection;
        public bool QuestionBeforeConnection { get { return _questionBeforeConnection; } set { _questionBeforeConnection = value; OnPropertyChanged("QuestionBeforeConnection"); } }

        public String SelectedLanguage { get; set; }

        public Dictionary<String, String> Codes { get; private set; }

        private int _mouseSensibility;
        public int MouseSensibility { get { return _mouseSensibility; } set { _mouseSensibility = value; OnPropertyChanged("MouseSensibility"); } }
        private int _scrollSensibility;
        public int ScrollSensibility { get { return _scrollSensibility; } set { _scrollSensibility = value; OnPropertyChanged("ScrollSensibility"); } }

        #endregion

        private static Config _instancia;
        public static Config newConfig()
        {
            return (_instancia == null) ? new Config() : _instancia;
        }
        private Config()
        {
            _languages = new Dictionary<string, bool>();
            Codes = new Dictionary<string, string>();
            getConfig();
        }

        public void selectLanguage(String oldLanguageSelected, String newLanguageSelected)
        {
            _languages[oldLanguageSelected] = false;
            _languages[newLanguageSelected] = true;

            setConfig();
        }

        public String getCode(String selectedLanguage)
        {
            return Codes[selectedLanguage];
        }

        private void getConfig()
        {
            XmlDocument doc = new XmlDocument();

            doc.Load(fileUri);

            foreach (XmlNode LanguageNode in doc["Config"]["Languages"].ChildNodes)
            {
                String languageName = LanguageNode.Attributes["name"].InnerText;
                String languageCode = LanguageNode.Attributes["code"].InnerText;
                Boolean languageSelected = (LanguageNode.Attributes["selected"].InnerText == "True") ? true : false;
                Languages.Add(languageName, languageSelected);
                Codes.Add(languageName,languageCode);
                if (languageSelected)
                    SelectedLanguage = languageName;
            }

            MaxConnections = Int32.Parse(doc["Config"]["MaxConnections"].InnerText);

            QuestionBeforeConnection = (doc["Config"]["QuestionBeforeConnection"].InnerText == "True") ? true : false;

            MouseSensibility = Int32.Parse(doc["Config"]["MouseSensibility"].InnerText);

            ScrollSensibility = Int32.Parse(doc["Config"]["ScrollSensibility"].InnerText);

        }

        public void setConfig()
        {
            File.SetAttributes(fileUri, FileAttributes.Normal); //READ-ONLY FILE
            File.Delete(fileUri);
            using (System.IO.StreamWriter file = new System.IO.StreamWriter(fileUri))
            {
                file.WriteLine("<Config>");

                file.WriteLine("\t<Languages>");
                foreach (String LanguageName in Languages.Keys)
                {
                    file.WriteLine("\t\t<Language name='" + LanguageName + "' code='"+ Codes[LanguageName] +"' selected='" + Languages[LanguageName].ToString() + "'/>");
                }
                file.WriteLine("\t</Languages>");

                file.WriteLine("\t<MaxConnections>" + MaxConnections + "</MaxConnections>");

                file.WriteLine("\t<QuestionBeforeConnection>" + QuestionBeforeConnection.ToString() + "</QuestionBeforeConnection>");

                file.WriteLine("\t<MouseSensibility>" + MouseSensibility + "</MouseSensibility>");

                file.WriteLine("\t<ScrollSensibility>" + ScrollSensibility + "</ScrollSensibility>");

                file.WriteLine("</Config>");
            }

            File.SetAttributes(fileUri, FileAttributes.ReadOnly); //READ-ONLY FILE
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
