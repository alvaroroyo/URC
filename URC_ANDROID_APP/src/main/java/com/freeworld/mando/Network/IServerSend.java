package com.freeworld.mando.Network;

/**
 * Created by Alvaro Royo on 16/01/2016.
 */
public interface IServerSend {

    /**
     * Mando Juego:
     */

    String BTN_TOP_PRESS = "1301";
    String BTN_BOTTOM_PRESS = "1303";
    String BTN_LEFT_PRESS = "1305";
    String BTN_RIGHT_PRESS = "1307";

    String BTN_TOP_UP = "1302";
    String BTN_BOTTOM_UP = "1304";
    String BTN_LEFT_UP = "1306";
    String BTN_RIGHT_UP = "1308";

    String ARROW_TOP = "1401";
    String ARROW_TOP_UP = "1402";
    String ARROW_LEFT = "1403";
    String ARROW_LEFT_UP = "1404";
    String ARROW_RIGHT = "1405";
    String ARROW_RIGHT_UP = "1406";
    String ARROW_BOTTOM = "1407";
    String ARROW_BOTTOM_UP = "1408";

    /**
     * Mando Multimedia:
     */

    String BTN_PLAYPAUSE = "1501";
    String BTN_STOP = "1505";
    String BTN_BEFORE_PRESS = "1507";
    String BTN_AFTER_PRESS = "1509";
    String BTN_VOLUP_PRESS = "1511";
    String BTN_VOLDOWN_PRESS = "1513";

    String BTN_SHUTDOWN = "1520";

    String BTN_BEFORE_UP = "1508";
    String BTN_AFTER_UP = "1510";
    String BTN_VOLUP_UP = "1512";
    String BTN_VOLDOWN_UP = "1514";


    /**
     * Mando Presentaciones:
     */

    String BTN_NEXT = "1601";
    String BTN_LAST = "1603";
    String BTN_PRESENTATION_BLACK = "1602";
    String BTN_PRESENTATION_WHITE = "1604";
    String BTN_PRESENTATION_ESC = "1605";
    String BTN_PRESENTATION_START = "1606";

    /**
     * Mando Mouse:
     */

    String MOUSE_LEFT_CLICK = "1200";
    String MOUSE_LEFT_DOWN = "1201";
    String MOUSE_LEFT_RELEASE = "1202";
    String MOUSE_RIGHT_DOWN = "1203";
    String MOUSE_RIGHT_RELEASE = "1204";
    String MOUSE_SCROLL_DOWN = "1205";
    String MOUSE_SCROLL_UP = "1206";

}
