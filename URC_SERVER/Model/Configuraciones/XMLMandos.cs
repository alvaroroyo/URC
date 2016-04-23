using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Windows.Media.Imaging;
using System.Xml;

namespace URC_Server.Model.Configuraciones
{
    public class XMLMandos
    {
        /// <summary>
        /// Obtiene un mando a partir de un fichero XML de sistema.
        /// </summary>
        /// <param name="nombreMando">Nombre del mando</param>
        /// <returns>Objeto Mando</returns>
        public static List<Mando> getMandos(string XMLUri)
        {
            List<Mando> mandos = new List<Mando>();

            foreach (string file in Directory.EnumerateFiles(XMLUri, "*.xml"))
            {
                Mando mando = new Mando();

                XmlDocument doc = new XmlDocument();

                doc.Load(file);

                XmlNode principal_node = doc["Mando"];

                mando.XmlUri = file;

                mando.Name = principal_node.Attributes["name"].InnerText;

                mando.Icon = new BitmapImage();
                mando.Icon.BeginInit();
                mando.Icon.UriSource = new Uri("/Resources/Icons/Mandos/" + mando.Name + ".png", UriKind.RelativeOrAbsolute);
                mando.Icon.EndInit();

                mando.Modificable = (principal_node.Attributes["modificable"].InnerText.ToLower() == "true") ? true : false;

                foreach (XmlNode secondary_nodes in principal_node.ChildNodes)
                {
                    string groupName = secondary_nodes.Attributes["name"].InnerText;

                    mando.addGroup(groupName);

                    foreach (XmlNode btn_node in secondary_nodes.ChildNodes)
                    {
                        mando.addButton(groupName, btn_node.Attributes["name"].InnerText, btn_node.Attributes["valor"].InnerText);
                    }
                }
                mandos.Add(mando);
            }

            return mandos;
        }
        /// <summary>
        /// Obtiene los controles del mando y grupo especificado del XML.
        /// </summary>
        /// <returns>Mando</returns>
        public static Dictionary<string, Keys> getKeys(string mando, string group)
        {
            string XmlUri = @"Configuracion/Mandos/" + mando + ".xml";

            Dictionary<string, Keys> controles = new Dictionary<string, Keys>();

                XmlDocument doc = new XmlDocument();

                doc.Load(XmlUri);

                XmlNode principal_node = doc["Mando"];

                foreach (XmlNode secondary_nodes in principal_node.ChildNodes)
                {
                    string groupName = secondary_nodes.Attributes["name"].InnerText;
                    if (groupName == group)
                    {
                        foreach (XmlNode btn_node in secondary_nodes.ChildNodes)
                        {
                            controles.Add(btn_node.Attributes["name"].InnerText, (Keys)Enum.Parse(typeof(Keys), btn_node.Attributes["valor"].InnerText, true));
                        }
                    }
                }

            return controles;
        }
        /// <summary>
        /// Crea un nuevo fichere XML de solo lectura para el mando.
        /// </summary>
        /// <param name="XMLUri"></param>
        /// <param name="objetoMando"></param>
        public static void updateXML(Mando mando)
        {
            File.SetAttributes(mando.XmlUri, FileAttributes.Normal);
            File.Delete(mando.XmlUri);
            using (System.IO.StreamWriter file = new System.IO.StreamWriter(mando.XmlUri))
            {
                file.WriteLine("<Mando name='" + mando.Name + "' modificable='"+mando.Modificable+"'>");

                Dictionary<string, Dictionary<string, Keys>> grupos = mando.Grupos;

                foreach (string keys in grupos.Keys)
                {
                    file.WriteLine("\t<group name='"+keys+"'>");

                    Dictionary<string, Keys> buttons = grupos[keys];

                    foreach (string btn in buttons.Keys)
                    {
                        file.WriteLine("\t\t<btn name='"+btn+"' valor='"+buttons[btn]+"' />");
                    }

                    file.WriteLine("\t</group>");
                }

                file.WriteLine("</Mando>");

            }
            File.SetAttributes(mando.XmlUri, FileAttributes.ReadOnly);
        }
        /// <summary>
        /// Obtiene los grupos de un mando
        /// </summary>
        /// <param name="nombreMando">Nombre del mando del que se desea obtener los grupos.</param>
        /// <returns>String[]</returns>
        public static List<string> getGroups(string nombreMando)
        {
            string XmlUri = @"Configuracion/Mandos/" + nombreMando + ".xml";

            List<string> grupos = new List<string>();

            XmlDocument doc = new XmlDocument();

            doc.Load(XmlUri);

            XmlNode principal_node = doc["Mando"];

            for (int i = 0; i < principal_node.ChildNodes.Count; i++)
            {
                XmlNode secondary_nodes = principal_node.ChildNodes.Item(i);
                grupos.Add(secondary_nodes.Attributes["name"].InnerText);
            }

            return grupos;
        }
    }
}
