using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Input;
using URC_Server.Model;
using URC_Server.Model.Conexiones;

namespace URC_Server.ViewModel
{
    public class ConexionViewModel : INotifyPropertyChanged //Implementada la interfaz
    {
        PoolConexiones pool;
        public ConexionViewModel()
        {
            pool = new PoolConexiones();
            pool.PropertyChanged += poolObserver;
            QR = pool.QR;

            MainWindow.setConexion(this);

            //COMMANDS
            Close = new RelayCommand(CloseCommand_Execute, CloseCommand_CanExecute);
        }

        #region Propiedades (Getters y Setters)

        public string QR { get; private set; }

        /// <summary>
        /// Obtiene un listado de las conexiones actuales.
        /// </summary>
        public ObservableCollection<Conexion> ListaConexiones { get; set; }
        /// <summary>
        /// Obtiene o establece un objeto de tipo Conexion que ha sido seleccionado en la lista de conexiones del VIEW
        /// </summary>
        public Conexion ConexionSeleccionada { get; set; }
        /// <summary>
        /// Obtiene o establece el index de la conexión selecionada de la lista de conexiones del VIEW
        /// </summary>
        public int IndexConexion { get; set; }

        public string ErrorSMS { set { MainWindow.setStatusBar(value); } }

        #endregion

        #region FUNCIONES
        /// <summary>
        /// Cierra todas las conexiones del POOL
        /// </summary>
        public void closeAllConexions()
        {
            LogFile.Log("ConexionViewModel","Cerrando conexiones.");
            pool.removeAllConexion();
        }
        #endregion

        #region COMMANDS

        public ICommand Close { get; set; }
        public bool CloseCommand_CanExecute(object obj)
        {
            bool connection = false;
            try
            {
                connection = (ConexionSeleccionada != null) ? true : false;
            }
            catch (Exception e){}

            return connection;
        }
        public void CloseCommand_Execute(object obj)
        {
            ConexionSeleccionada.Close();
            ErrorSMS = "Desconectado...";
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

        public void poolObserver(object sender, PropertyChangedEventArgs e)
        {
            switch (e.PropertyName)
            {
                case "Conexiones":
                    ListaConexiones = new ObservableCollection<Conexion>();
                    ObservableCollection<Conexion> occ = pool.Conexiones;
                    for (int i = 0; i < occ.Count; i++)
                    {
                        ListaConexiones.Add(occ.ElementAt(i));
                    }
                    OnPropertyChanged("ListaConexiones");

                    break;
            }
        }

    }
}
