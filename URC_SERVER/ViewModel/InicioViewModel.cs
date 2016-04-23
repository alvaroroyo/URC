using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using System.Windows.Media.Imaging;
using URC_Server.Model;
using URC_Server.Model.Inicio;

namespace URC_Server.ViewModel
{
    class InicioViewModel : INotifyPropertyChanged
    {
        public InicioViewModel()
        {
            _inicio = ParseInicio.getInicioList();

            //COMMANDS
            LastImage = new RelayCommand(LastImage_Execute, o => true);
            NextImage = new RelayCommand(NextImage_Execute, o => true);
        }

        private List<Inicio> _inicio;
        public List<Inicio> Inicio { get { return _inicio; } }

        private Inicio _selectedInicio;
        public Inicio SelectedInicio 
        {
            get { return _selectedInicio; }
            set 
            {
                _selectedInicio = value;
                InicioImage = value.getImageAt(0);
                InicioText = value.getTextAt(0);
                _imageIndex = 0;
                _maxImages = value.getAllImages().Count;
                OnPropertyChanged(new string[]{"InicioImage","InicioText"});
            } 
        }

        public int SelectedInicioIndex { get; set; }

        public BitmapImage InicioImage { get; set; }
        public string InicioText { get; set; }

        private int _imageIndex;
        private int _maxImages;

        #region COMMANDS

        public ICommand LastImage { get; set; }
        public void LastImage_Execute(object obj)
        {
            lastImage();
        }


        public ICommand NextImage { get; set; }
        public void NextImage_Execute(object obj)
        {
            nextImage();
        }

        #endregion

        #region FUNCIONES

        public void nextImage()
        {
            if (_imageIndex < _maxImages - 1)
            {
                _imageIndex++;
                InicioImage = SelectedInicio.getImageAt(_imageIndex);
                InicioText = SelectedInicio.getTextAt(_imageIndex);
                OnPropertyChanged(new string[] { "InicioImage", "InicioText" });
            }
        }

        public void lastImage()
        {
            if (_imageIndex > 0)
            {
                _imageIndex--;
                InicioImage = SelectedInicio.getImageAt(_imageIndex);
                InicioText = SelectedInicio.getTextAt(_imageIndex);
                OnPropertyChanged(new string[] { "InicioImage", "InicioText" });
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
