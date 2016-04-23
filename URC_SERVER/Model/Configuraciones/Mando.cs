using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Windows.Media.Imaging;

namespace URC_Server.Model.Configuraciones
{
    public class Mando
    {
        public Mando()
        {
            groups = new Dictionary<string, Dictionary<string, Keys>>();
        }

        #region Variables privadas

        private Dictionary<string, Dictionary<string, Keys>> groups;

        #endregion

        /// <summary>
        /// Obtiene o establece la URI del XML de este objeto mando.
        /// </summary>
        public string XmlUri { get; set; }
        /// <summary>
        /// Obtiene o establece el nombre del mando.
        /// </summary>
        public string Name { get; set; }
        /// <summary>
        /// Obtiene o establece si el mando es modificable y por tanto se pueden añadir grupos nuevos.
        /// </summary>
        public bool Modificable { get; set; }

        public BitmapImage Icon { get; set; }
        /// <summary>
        /// Obtiene los grupos de botones de Mando.
        /// </summary>
        public Dictionary<string, Dictionary<string, Keys>> Grupos { get { return groups; } }

        #region Functions
        /// <summary>
        /// Añade un grupo nuevo a la coleccion
        /// </summary>
        /// <param name="name">Nombre del grupo</param>
        public void addGroup(string name)
        {
            groups.Add(name, new Dictionary<string, Keys>());
        }
        /// <summary>
        /// Añade un grupo nuevo a la coleccion
        /// </summary>
        /// <param name="name">Nombre del grupo</param>
        /// <param name="_default">Diferenciador</param>
        public void addGroup(string name, bool _default)
        {
            try
            {
                Dictionary<string, Keys> nuevoGrupo = new Dictionary<string, Keys>();
                foreach (string key in groups["Default"].Keys)
                {
                    nuevoGrupo.Add(key, groups["Default"][key]);
                }
                groups.Add(name, nuevoGrupo);
                XMLMandos.updateXML(this);

            }
            catch (ArgumentException ae)
            {
                throw new Exception("Ya existe este nombre de grupo.");
            }
        }
        /// <summary>
        /// Cambia el nombre de un grupo
        /// </summary>
        /// <param name="oldName">Antiguo nombre del grupo</param>
        /// <param name="newName">Nuevo nombre del grupo</param>
        public void updateGroup(string oldName, string newName)
        {
            Dictionary<string, Keys> group = groups[oldName];
            groups.Remove(oldName);
            groups.Add(newName, group);
            XMLMandos.updateXML(this);
        }
        /// <summary>
        /// Borra un grupo de botones de usuario.
        /// </summary>
        /// <param name="name">Nombre del grupo a borrar</param>
        public void deleteGroup(string name)
        {
            groups.Remove(name);
            XMLMandos.updateXML(this);
        }
        /// <summary>
        /// Añade un boton al objeto mando.
        /// </summary>
        /// <param name="group">Grupo al que pertenece el boton</param>
        /// <param name="btn">Identificador del boton</param>
        /// <param name="key">Key que pulsará el boton. System.Windows.Forms.Key como String</param>
        public void addButton(string group, string btn, string key)
        {
            key = (key.Length == 0) ? "None" : key;

            Dictionary<string, Keys> btns = groups[group];

            btns.Add(btn, (Keys)Enum.Parse(typeof(Keys), key, true));
        }
        /// <summary>
        /// Cambia el valor de un boton.
        /// </summary>
        /// <param name="group">Grupo al que pertenece el boton</param>
        /// <param name="btn">Identificador del boton</param>
        /// <param name="key">Key que pulsará el boton</param>
        public void updateButton(string group, string btn, Keys key)
        {
            groups[group][btn] = key;
            XMLMandos.updateXML(this);
        }
        /// <summary>
        /// Obtiene los botones de un grupo de botones.
        /// </summary>
        /// <param name="group">Nombre del grupo.</param>
        /// <returns></returns>
        public Dictionary<string, Keys> getButtons(string group)
        {
            return groups[group];
        }

        #endregion
    }
}
