package com.freeworld.mando;

import java.util.List;

/**
 * Created by aroyo on 04/02/2016.
 */
public interface IMain {
    void setFragmentMandoSaveServer();
    void setFragmentMando();
    String getFragment();
    void setFragmentNewMando(String newFragment);
    void setFragment(String newFragment);
    void closeNavDrawer();
    List<String[]> getListControls();
}
