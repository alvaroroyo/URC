using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media.Imaging;

namespace URC_Server.Model.Inicio
{
    public class Inicio
    {
        public Inicio()
        {
            _imagen = new Dictionary<BitmapImage, string>();
        }

        public string GroupName { get; set; }
        public Visibility ShowGroupName { get; set; }

        public string Name { get; set; }


        private Dictionary<BitmapImage, string> _imagen;

        public void addImage(string UriImagen, string texto)
        {
            BitmapImage imagen = new BitmapImage();
            imagen.BeginInit();
            imagen.UriSource = new Uri("/Resources/Inicio/" + UriImagen, UriKind.RelativeOrAbsolute);
            imagen.EndInit();
            _imagen.Add(imagen, texto);
        }

        public BitmapImage getImageAt(int i)
        {
            BitmapImage ret_image = new BitmapImage();
            ret_image.BeginInit();
            ret_image.UriSource = new Uri("/Resources/Inicio/404.png", UriKind.RelativeOrAbsolute);
            ret_image.EndInit();

            int sub_i = 0;
            foreach (BitmapImage imagen in _imagen.Keys)
            {
                if (sub_i == i)
                {
                    ret_image = imagen;
                }
                sub_i++;
            }
            return ret_image;
        }

        public string getTextAt(int i)
        {
            string ret_text = "";

            int sub_i = 0;
            foreach (BitmapImage imagen in _imagen.Keys)
            {
                if (sub_i == i)
                {
                    ret_text = _imagen[imagen];
                }
                sub_i++;
            }
            return ret_text;
        }

        public Dictionary<BitmapImage, string> getAllImages()
        {
            return _imagen;
        }
    }
}
