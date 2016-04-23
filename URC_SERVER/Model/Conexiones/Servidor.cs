using System;
using System.Collections.Generic;
using System.Net;
using System.Net.NetworkInformation;
using System.Net.Sockets;
using System.Text.RegularExpressions;
using System.Threading;

namespace URC_Server.Model.Conexiones
{
    class Servidor
    {

        #region Variables

        PoolConexiones pool;

        private volatile TcpListener server;
        private TcpClient cliente;

        private volatile bool isConnected;

        #endregion

        public Servidor(PoolConexiones pool)
        {
            this.pool = pool;

            String ip = GetAddresses();
            int port = this.getPort();

            server = new TcpListener(IPAddress.Parse(ip), port);

            pool.QR = ip + ":" + port;

            new Thread(() => this.server_connect()).Start();
        }

        /// <summary>
        /// Arranca un servidor de escucha.
        /// </summary>
        private void server_connect()
        {
            server.Start(); //Arrancamos el socket servidor
            isConnected = true;
            while (isConnected)
            {
                try
                {
                     cliente = server.AcceptTcpClient();  //Aceptamos conexiones de cliente
                    //Hacer que el pool cree una nueva conexion
                    new Thread(() => { pool.addConexion(cliente); }).Start();
                }
                catch (Exception e)
                {
                    LogFile.Log("Exception server", e.ToString());
                }
            }
            this.Close();
        }

        /// <summary>
        /// Cierra el servidor y finaliza su hilo.
        /// </summary>
        public void Close()
        {
            isConnected = false;
            try
            {
                server.Stop();
            }
            catch (Exception e) { }
        }

        #region set IP & set Port
        /// <summary>
        /// Obtiene la ip local del ordenador.
        /// </summary>
        /// <returns>Devuelve la IP local en un String</returns>
        public String GetAddresses()
        {
            Regex regex = new Regex(@"^(?:[0-9]{1,3}\.){3}[0-9]{1,3}$");
            IPAddress[] localIPs = Dns.GetHostAddresses(Dns.GetHostName());
            foreach (IPAddress addr in localIPs)
            {
                if (regex.Match(addr.ToString()).Success)
                {
                    return addr.ToString();
                }
            }
            return null;
        }

        /// <summary>
        /// Busca un puerto estable y lo devuelve como conexion.
        /// </summary>
        /// <returns></returns>
        private int getPort()
        {
            //string localIP = GetLocalIPAddress();
            int puerto = 0;
            IPGlobalProperties ipProperties = IPGlobalProperties.GetIPGlobalProperties();
            IPEndPoint[] ipEndPoints = ipProperties.GetActiveTcpListeners();

            int intento = 52343; //Puerto por defecto.
            while (puerto == 0)
            {
                foreach (IPEndPoint endPoint in ipEndPoints)
                {
                    if (endPoint.Port != intento)
                    {
                        puerto = intento;
                    }
                    else
                    {
                        intento = new Random().Next(50000, 54500);
                    }
                }
            }
            return puerto;
        }

        #endregion
    }
}

