package com.freeworld.mando.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.freeworld.mando.Config;
import com.freeworld.mando.R;

/**
 * Created by aroyo on 04/12/2015.
 */
public class SetDeviceName extends Dialog{

    Activity activity;
    Config config;

    Button save;
    TextView deviceName;

    public SetDeviceName(Activity activity) {
        super(activity);
        this.activity = activity;
        this.config = Config.getInstance(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setdevicename);

        this.setCancelable(false);

        save = (Button) this.findViewById(R.id.btn_saveNameDevice);
        deviceName = (TextView) this.findViewById(R.id.txtSetDeviceName_Dialog);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStringConfig(config.DEVICE_NAME,deviceName.getText().toString());
                dismiss();
            }
        });
        deviceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0){
                    save.setEnabled(true);
                }else{
                    save.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
