using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Xml;
using URC_Server.Model.Configuraciones;

namespace URC_Server.Model.Inicio
{
    public class ParseInicio
    {

        public static List<Inicio> getInicioList()
        {
            //Uri uri = new Uri("/Resources/Inicio/inicio.xml", UriKind.RelativeOrAbsolute);
            //string fileUri = @"Configuracion/inicio.xml";

            List<Inicio> lista = new List<Inicio>();

            XmlDocument doc = new XmlDocument();

            switch (Config.newConfig().SelectedLanguage)
            {
                case "Español":
                    doc.LoadXml(Properties.Resources.Español_inicio);
                    break;
                case "English":
                    doc.LoadXml(Properties.Resources.English_inicio);
                    break;
                default:
                    doc.LoadXml(Properties.Resources.Español_inicio);
                    break;

            }

            XmlNode principal_node = doc["Inicio"];

            foreach (XmlNode group in principal_node.ChildNodes)
            {
                string groupName = group.Attributes["name"].InnerText;

                for (int i = 0; i < group.ChildNodes.Count; i++)
                {
                    Inicio inicio = new Inicio();

                    XmlNode item = group.ChildNodes.Item(i);
                    inicio.GroupName = groupName;
                    inicio.ShowGroupName = (i == 0) ? Visibility.Visible : Visibility.Collapsed;
                    inicio.Name = item.Attributes["name"].InnerText;

                    foreach (XmlNode image in item.ChildNodes)
                    {   
                        string imageName = image.Attributes["src"].InnerText;
                        string imageText = image.InnerText;
                        inicio.addImage(imageName, imageText);
                    }

                    lista.Add(inicio);
                }
            }

            return lista;
        }

    }
}
