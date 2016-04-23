using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using URC_Server.Model.Configuraciones;

namespace URC_Server.Model.Conexiones
{
    public class PoolConexiones : INotifyPropertyChanged //Implementada la interfaz
    {
        Servidor server;
        Config config;

        #region PROPIEDADES

        public String QR { get; set; }

        public ObservableCollection<Conexion> Conexiones { get; private set; }

        #endregion

        public PoolConexiones()
        {
            this.server = new Servidor(this);

            config = Config.newConfig();

            Conexiones = new ObservableCollection<Conexion>();
        }

        #region Funciones

        /// <summary>
        /// Crea una nueva conexión y la añade a la lista.
        /// </summary>
        public void addConexion(TcpClient cliente)
        {
            if(Conexiones.Count >= config.MaxConnections) // If exist more connections that the max connections config.
            {
                cliente.Close();
            }else{
                Conexiones.Add(new Conexion(cliente,this));
                OnPropertyChanged("Conexiones");
            }            
        }

        /// <summary>
        /// Cierra todas las conexiones del Pool.
        /// Se debe llamar al finalizar el programa.
        /// </summary>
        public void removeAllConexion()
        {
            for (int i = 0; i < Conexiones.Count; i++)
            {
                Conexiones.ElementAt(i).Close();
            }
            server.Close();
        }

        /// <summary>
        /// Borra la conexion de la lista.
        /// </summary>
        /// <param name="con"></param>
        public void removeConexionFromList(Conexion con)
        {
            Conexiones.Remove(con);
            OnPropertyChanged("Conexiones");
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
