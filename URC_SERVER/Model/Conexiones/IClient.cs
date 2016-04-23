using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace URC_Server.Model.Conexiones
{
    public class IClient
    {
        /**
         * Mando Juego:
         */

        public const String BTN_TOP_PRESS = "1301";
        public const String BTN_BOTTOM_PRESS = "1303";
        public const String BTN_LEFT_PRESS = "1305";
        public const String BTN_RIGHT_PRESS = "1307";

        public const String BTN_TOP_UP = "1302";
        public const String BTN_BOTTOM_UP = "1304";
        public const String BTN_LEFT_UP = "1306";
        public const String BTN_RIGHT_UP = "1308";

        public const String ARROW_TOP = "1401";
        public const String ARROW_TOP_UP = "1402";
        public const String ARROW_LEFT = "1403";
        public const String ARROW_LEFT_UP = "1404";
        public const String ARROW_RIGHT = "1405";
        public const String ARROW_RIGHT_UP = "1406";
        public const String ARROW_BOTTOM = "1407";
        public const String ARROW_BOTTOM_UP = "1408";

        /**
         * Mando Multimedia:
         */

        public const String BTN_PLAYPAUSE = "1501";
        public const String BTN_STOP = "1505";
        public const String BTN_BEFORE_PRESS = "1507";
        public const String BTN_AFTER_PRESS = "1509";
        public const String BTN_VOLUP_PRESS = "1511";
        public const String BTN_VOLDOWN_PRESS = "1513";

        public const String BTN_SHUTDOWN = "1520";

        public const String BTN_BEFORE_UP = "1508";
        public const String BTN_AFTER_UP = "1510";
        public const String BTN_VOLUP_UP = "1512";
        public const String BTN_VOLDOWN_UP = "1514";


        /**
         * Mando Presentaciones:
         */

        public const String BTN_NEXT = "1601";
        public const String BTN_LAST = "1603";
        public const String BTN_PRESENTATION_BLACK = "1602";
        public const String BTN_PRESENTATION_WHITE = "1604";
        public const String BTN_PRESENTATION_ESC = "1605";
        public const String BTN_PRESENTATION_START = "1606";
		
		/**
		* Mando Mouse:
		*/
        public const String MOUSE_LEFT_CLICK = "1200";
		public const String MOUSE_LEFT_DOWN = "1201";
		public const String MOUSE_LEFT_RELEASE = "1202";
		public const String MOUSE_RIGHT_DOWN = "1203";
		public const String MOUSE_RIGHT_RELEASE = "1204";
        public const String MOUSE_SCROLL_DOWN = "1205";
        public const String MOUSE_SCROLL_UP = "1206";
    }
}
