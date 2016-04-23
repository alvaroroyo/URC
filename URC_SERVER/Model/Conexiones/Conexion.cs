using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Windows;
using System.Windows.Forms;
using URC_Server.Model.Configuraciones;
using URC_Server.View.Dialogs;

namespace URC_Server.Model.Conexiones
{
    public class Conexion : KeyManager,INotifyPropertyChanged
    {
        #region VARIABLES

        //private KeyManager keyManager;

        private TcpClient cliente;
        private NetworkStream networkStream;

        private PoolConexiones pool;

        private Config config;

        #endregion

        #region Propiedades

        private string _nombre;
        public string Nombre { get { return _nombre; } set { _nombre = value; OnPropertyChanged("Nombre"); } }

        private string _dispositivo;
        public string TipoDispositivo { get { return _dispositivo; } set { _dispositivo = value; OnPropertyChanged("TipoDispositivo"); } }

        private string _mando;
        public string Mando { get { return _mando; } set { _mando = value; OnPropertyChanged("Mando");  } }

        private string _grupoMando;
        public string GrupoMando {
            get { return _grupoMando; }
            set 
            { 
                _grupoMando = value;
                Controles = XMLMandos.getKeys(Mando,value);
            }
        }

        public Dictionary<string, Keys> Controles { get; set; }

        #endregion

        public Conexion(TcpClient cliente,PoolConexiones pool)
        {
            this.config = Config.newConfig();

            this.cliente = cliente;

            //this.keyManager = new KeyManager();

            this.pool = pool;            

            if(config.QuestionBeforeConnection){
                Thread newWindowThread = new Thread(new ThreadStart(dialogShow));
                newWindowThread.SetApartmentState(ApartmentState.STA);
                newWindowThread.IsBackground = true;
                newWindowThread.Start();
            }
            else
            {
                new Thread(() => this.serverListener()).Start();
            }

        }

        private void dialogShow(){
            CanConnectDialog dialog = new CanConnectDialog(this);
            if (dialog.ShowDialog() == true)
            {
                if (dialog.CanConnect)
                {
                    new Thread(() => this.serverListener()).Start();
                }
                else
                {
                    Close();
                }
            }
        }

        #region COMUNICACION

        private void serverListener()
        {
            try
            {
                networkStream = cliente.GetStream();

                String data;

                while (networkStream.ReadByte() > 0)
                {
                    byte[] bytes = new Byte[cliente.Available];
                    networkStream.Read(bytes, 0, bytes.Length);
                    if (bytes.Length > 0)
                    {
                        data = Encoding.UTF8.GetString(bytes);
                        //LogFile.Log("Serverrecive", data);
                        if (data.Substring(0, 1) == ":")
                        {
                            string[] datos = data.Split(':');
                            this.Nombre = datos[1];
                            this.TipoDispositivo = (datos[2] == "false") ? "Móvil" : "Tablet";
                            this.Mando = datos[3];
                            this.GrupoMando = datos[4];

                            sendClient("Groups", string.Join(",", XMLMandos.getGroups(datos[3])));
                        }
                        else
                        {
                            switch (Mando)
                            {
                                case "Juego":
                                    gamePad(data);
                                    break;
                                case "Multimedia":
                                    multimedia(data);
                                    break;
                                case "Presentaciones":
                                    presentation(data);
                                    break;
                                case "Mouse":
                                    mouse(data);
                                    break;
                            }
                        }
                    }
                }
                //Comunicar desconexion del cliente al pool.
                Close();
            }
            catch (SocketException se)
            {
                Close();
            }
            catch (Exception e)
            {
                Close();
            }
        }

        public void sendClient(string cabecera, string mensaje)
        {
            byte[] myWriteBuffer = Encoding.UTF8.GetBytes(cabecera + "|" + mensaje + "\n");
            networkStream.Write(myWriteBuffer, 0, myWriteBuffer.Length);
        }

        #endregion

        #region Funciones
        /// <summary>
        /// Close this connection
        /// </summary>
        public void Close()
        {
            try
            {
                networkStream.Close();
            }catch(Exception e){}
            try
            {
                cliente.Close();
            }
            catch (Exception e){}
            try
            {
                pool.removeConexionFromList(this);
            }
            catch (Exception e){}
        }

        #endregion

        #region TRATAMIENTO DE MANDOS

        private void gamePad(String key)
        {
            switch (key)
            {
                //Botones de acciones
                case IClient.BTN_TOP_PRESS:
                    keyPress(Controles["Boton de control arriba"]);
                    break;
                case IClient.BTN_BOTTOM_PRESS:
                    keyPress(Controles["Boton de control abajo"], 200);
                    break;
                case IClient.BTN_LEFT_PRESS:
                    keyPress(Controles["Boton de control izquierda"]);
                    break;
                case IClient.BTN_RIGHT_PRESS:
                    keyPress(Controles["Boton de control derecha"]);
                    break;
                case IClient.BTN_TOP_UP:
                    keyRelease(Controles["Boton de control arriba"]);
                    break;
                case IClient.BTN_BOTTOM_UP:
                    keyRelease(Controles["Boton de control abajo"]);
                    break;
                case IClient.BTN_LEFT_UP:
                    keyRelease(Controles["Boton de control izquierda"]);                    
                    break;
                case IClient.BTN_RIGHT_UP:
                    keyRelease(Controles["Boton de control derecha"]);
                    break;

                //Botones de control. Flechas
                case IClient.ARROW_TOP:
                    keyPress(Controles["Boton de movimiento arriba"]);
                    break;
                case IClient.ARROW_LEFT:
                    keyPress(Controles["Boton de movimiento izquierda"]);
                    break;
                case IClient.ARROW_RIGHT:
                    keyPress(Controles["Boton de movimiento derecha"]);
                    break;
                case IClient.ARROW_BOTTOM:
                    keyPress(Controles["Boton de movimiento abajo"]);
                    break;
                case IClient.ARROW_TOP_UP:
                    keyRelease(Controles["Boton de movimiento arriba"]);
                    break;
                case IClient.ARROW_LEFT_UP:
                    keyRelease(Controles["Boton de movimiento izquierda"]);
                    break;
                case IClient.ARROW_RIGHT_UP:
                    keyRelease(Controles["Boton de movimiento derecha"]);
                    break;
                case IClient.ARROW_BOTTOM_UP:
                    keyRelease(Controles["Boton de movimiento abajo"]);
                    break;
            }
        }

        private void multimedia(String key)
        {
            switch (key)
            {
                case IClient.BTN_PLAYPAUSE:
                    keyDown(Keys.MediaPlayPause);
                    break;
                case IClient.BTN_STOP:
                    keyDown(Keys.MediaStop);
                    break;
                case IClient.BTN_VOLDOWN_PRESS:
                    keyPress(Keys.VolumeDown);
                    break;
                case IClient.BTN_VOLUP_PRESS:
                    keyPress(Keys.VolumeUp);
                    break;
                case IClient.BTN_VOLDOWN_UP:
                    keyRelease(Keys.VolumeDown);
                    break;
                case IClient.BTN_VOLUP_UP:
                    keyRelease(Keys.VolumeUp);
                    break;
                case IClient.BTN_AFTER_PRESS:
                    keyPress(Keys.MediaNextTrack);
                    break;
                case IClient.BTN_AFTER_UP:
                    keyRelease(Keys.MediaNextTrack);
                    break;
                case IClient.BTN_BEFORE_PRESS:
                    keyPress(Keys.MediaPreviousTrack);
                    break;
                case IClient.BTN_BEFORE_UP:
                    keyRelease(Keys.MediaPreviousTrack);
                    break;
                case IClient.BTN_SHUTDOWN:
                    Process.Start("shutdown", "/s /t 0");
                    break;
            }
        }

        private void presentation(String key)
        {
            switch (key)
            {
                case IClient.BTN_NEXT:
                    keyDown(Keys.Right);
                    break;
                case IClient.BTN_LAST:
                    keyDown(Keys.Left);
                    break;
                case IClient.BTN_PRESENTATION_BLACK:
                    keyDown(Keys.B);
                    break;
                case IClient.BTN_PRESENTATION_WHITE:
                    keyDown(Keys.W);
                    break;
                case IClient.BTN_PRESENTATION_ESC:
                    keyDown(Keys.Escape);
                    break;
                case IClient.BTN_PRESENTATION_START:
                    keyDown(Keys.F5);
                    break;
            }
        }

        private void mouse(String key)
        {
            switch (key)
            {   case IClient.MOUSE_LEFT_CLICK:
                    mouseLeftClick();
                    break;
                case IClient.MOUSE_LEFT_DOWN:
                    mouseLeftClickDown();
                    break;
                case IClient.MOUSE_LEFT_RELEASE:
                    mouseLeftClickUp();
                    break;
                case IClient.MOUSE_RIGHT_DOWN:
                    mouseRightClickDown();
                    break;
                case IClient.MOUSE_RIGHT_RELEASE:
                    mouseRightClickUp();
                    break;
                case IClient.MOUSE_SCROLL_DOWN:
                    mouseWheel(-config.ScrollSensibility);
                    break;
                case IClient.MOUSE_SCROLL_UP:
                    mouseWheel(config.ScrollSensibility);
                    break;
                default:
                    string[] data;
                    if((data = key.Split(',')).Length == 2){
                        mouseMove(getMouseX() + (Int32.Parse(data[0]) * config.MouseSensibility), getMouseY() + (Int32.Parse(data[1])) * config.MouseSensibility);
                    }
                    break;
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
        #endregion
    }
}
