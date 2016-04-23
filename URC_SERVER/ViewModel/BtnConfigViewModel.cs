using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Windows.Input;
using System.Windows.Media.Imaging;
using URC_Server.Model;
using URC_Server.Model.Configuraciones;
using URC_Server.View;
using URC_Server.View.Dialogs;

namespace URC_Server.ViewModel
{
    class BtnConfigViewModel : INotifyPropertyChanged
    {
        public BtnConfigViewModel()
        {
            _mandos = XMLMandos.getMandos(@"Configuracion\Mandos");
            SelectedMando = _mandos.ElementAt(0);
            _groupName = "Default";

            //COMMANDS
            AddGroup = new RelayCommand(AddGroup_E, AddGroup_CE);
            AsignarTecla = new RelayCommand(AsignarTecla_E, AsignarTecla_CE);
            DeleteGroup = new RelayCommand(DeleteGroup_E, DeleteGroup_CE);
        }

        #region Variables Privadas

        List<Mando> _mandos;

        Mando _mando;

        int _groupIndex;
        string _groupName;

        int _buttonIndex;

        BitmapImage _buttonImage;
        string _buttonName;
        string _buttonKey;

        #endregion

        #region Propiedades
        /// <summary>
        /// Obtiene los tipos de mando.
        /// </summary>
        public List<Mando> Mandos { get { return _mandos; } }
        /// <summary>
        /// Obtiene o establece los grupos de botones del mando.
        /// </summary>
        public Dictionary<string, Dictionary<string, Keys>> Grupos { get; set; }
        /// <summary>
        /// Obtiene el nombre del grupo seleccionado
        /// </summary>
        public string SelectedGroupName { get { return _groupName; } }
        /// <summary>
        /// Obtiene o establece la lista de botones del mando segun el grupo seleccionado;
        /// </summary>
        public Dictionary<string, Keys> Botones { get; set; }
        /// <summary>
        /// Obtiene o establece el mando seleccionado de la lista.
        /// </summary>
        public Mando SelectedMando { 
            get { return _mando; } 
            set { 
                _mando = value;
                if (value == null)
                {
                    Grupos = null;
                    GroupIndex = -1;
                    Botones = null;
                }
                else
                {
                    Grupos = value.Grupos;
                    List<string> nombresGrupos = new List<String>();
                    foreach (string key in Grupos.Keys)
                    {
                        nombresGrupos.Add(key);
                    }
                    GroupIndex = 0;
                    SelectedButtonIndex = 0;
                }
                OnPropertyChanged(new String[] { "SelectedMando", "Botones", "Grupos", "GruposName" }); 
            } 
        }
        /// <summary>
        /// Obtiene o establece el indice del ComboBox
        /// </summary>
        public int GroupIndex
        { 
            get 
            {
                return _groupIndex;
            }
            set 
            {
                _groupIndex = value;
                if (value != -1)
                {
                    int i = 0;
                    foreach (string key in Grupos.Keys)
                    {
                        if (i == value)
                        {
                            Botones = Grupos[key];
                            _groupName = key;
                            SelectedButtonIndex = -1;
                        }
                        i++;
                    }
                    OnPropertyChanged(new String[] { "Botones", "GroupIndex" });
                }
            }
        }
        /// <summary>
        /// Obtiene o establece el index del boton seleccionado.
        /// </summary>
        public int SelectedButtonIndex
        {
            get { return _buttonIndex; }
            set
            {
                _buttonIndex = value;
                if (value >= 0)
                {
                    int i = 0;
                    foreach (string key in Botones.Keys)
                    {
                        if (i == value)
                        {
                            _buttonName = key;
                            _buttonKey = "Asignado: " + Botones[key];
                            _buttonImage = new BitmapImage();
                            _buttonImage.BeginInit();
                            _buttonImage.UriSource = new Uri("/Resources/Icons/Mandos/" + SelectedMando.Name + "/" + _buttonName.Replace(" ", "_") + ".png", UriKind.RelativeOrAbsolute);
                            _buttonImage.EndInit();
                        }
                        i++;
                    }
                    OnPropertyChanged(new String[] { "SelectedButtonIndex", "ButtonImage", "ButtonName", "ButtonKey" });
                }
                else
                {
                    _buttonImage = null;
                    _buttonName = null;
                    _buttonKey = null;
                    OnPropertyChanged(new String[]{"ButtonImage","ButtonName","ButtonKey"});
                }            
            }
        }
        /// <summary>
        /// Obtiene la imagen del boton seleccionado
        /// </summary>
        public BitmapImage ButtonImage { get { return _buttonImage; } }
        /// <summary>
        /// Obtiene el nombre del boton seleccionado
        /// </summary>
        public string ButtonName { get { return _buttonName; } }
        /// <summary>
        /// Obtiene la Key del boton seleccionado
        /// </summary>
        public string ButtonKey { get { return _buttonKey; } }

        #endregion

        #region Commands

        public ICommand AddGroup { get; set; }
        public bool AddGroup_CE(object obj)
        {
            return (SelectedMando != null) ? SelectedMando.Modificable : false;
        }
        public void AddGroup_E(object obj)
        {
            NewGroupDialog dialog = new NewGroupDialog();
            if (dialog.ShowDialog() == true)
            {
                try
                {
                    Mando mando = SelectedMando;
                    SelectedMando.addGroup(dialog.Answer, true);
                    SelectedMando = null;
                    SelectedMando = mando;
                    GroupIndex = Grupos.Count - 1;
                    SelectedButtonIndex = 0;
                    OnPropertyChanged(new String[] { "SelectedMando", "Grupos", "GroupIndex" });
                    MainWindow.setStatusBar("Grupo creado...");
                }
                catch (Exception e)
                {
                    MessageBox.Show(e.Message);
                }
            }
        }

        public ICommand AsignarTecla { get; set; }
        public bool AsignarTecla_CE(object obj)
        {
            return (SelectedMando != null && SelectedGroupName != "" && SelectedGroupName != "Default") ? SelectedMando.Modificable : false;
        }
        public void AsignarTecla_E(object obj)
        {
            AsignarTeclaDialog dialog = new AsignarTeclaDialog();
            if (dialog.ShowDialog() == true)
            {
                SelectedMando.updateButton(SelectedGroupName, ButtonName, (Keys)dialog.Tecla);
                _buttonKey = "Asignado: " + KeyInterop.KeyFromVirtualKey(dialog.Tecla).ToString();
                OnPropertyChanged(new String[] { "SelectedMando", "ButtonKey" });
                MainWindow.setStatusBar("Tecla asignada... ");
            }
        }

        public ICommand DeleteGroup { get; set; }
        public bool DeleteGroup_CE(object obj)
        {
            return (SelectedGroupName != "" && SelectedGroupName != "Default" && SelectedGroupName != null) ? true : false;
        }
        public void DeleteGroup_E(object obj)
        {
            ModalYesNoDialog dialog = new ModalYesNoDialog(SelectedGroupName);
            if (dialog.ShowDialog() == true)
            {
                Mando mando = SelectedMando;
                SelectedMando.deleteGroup(SelectedGroupName);
                SelectedMando = null;
                SelectedMando = mando;
                GroupIndex = Grupos.Count - 1;
                SelectedButtonIndex = 0;
                OnPropertyChanged(new String[] { "SelectedMando", "Grupos" });
                MainWindow.setStatusBar("Grupo eliminado...");
            }
        }

        #endregion

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

        protected void OnPropertyChanged(string[] name)
        {
            PropertyChangedEventHandler handler = PropertyChanged;
            if (handler != null)
            {
                for (int i = 0; i < name.Length; i++)
                {
                    handler(this, new PropertyChangedEventArgs(name[i]));
                }
            }
        }
        #endregion
    }
}