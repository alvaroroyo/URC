package com.freeworld.mando.Fragments_Mandos;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.freeworld.mando.Network.ClientConnection;
import com.freeworld.mando.Network.IServerSend;
import com.freeworld.mando.R;

/**
 * Created by Alvaro Royo on 29/03/2016.
 */
public class Teclado extends Fragment implements IServerSend,TextWatcher{

    private ClientConnection connection;
    private View fragment;
    private EditText showKeyboard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();

        connection = ClientConnection.getInstance();

        fragment = inflater.inflate(R.layout.fragment_mando_teclado, container, false);


        showKeyboard = (EditText) fragment.findViewById(R.id.keyboardShow);
        showKeyboard.addTextChangedListener(this);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(showKeyboard, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void afterTextChanged(Editable editable) {}
    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if(charSequence.length() > 0) {
            char lastChar = charSequence.charAt(charSequence.length() - 1);
            if (lastChar == ' ') {
                showKeyboard.setText("");
            }
        }
    }
}
