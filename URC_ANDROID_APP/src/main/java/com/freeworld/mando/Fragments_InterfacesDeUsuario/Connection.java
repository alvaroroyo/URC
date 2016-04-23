package com.freeworld.mando.Fragments_InterfacesDeUsuario;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.freeworld.mando.Network.ClientConnection;
import com.freeworld.mando.R;

import java.util.regex.Pattern;

/**
 * Created by AlvaroRoyo on 25/11/2015.
 */
public class Connection extends Fragment implements View.OnClickListener {
    View vista;
    Animation btnAnimationGood;

    private static final Pattern QRCODE = Pattern.compile("[0-9]+:+[0-9]{5}");
    private static final Pattern IPADDRESS_PATTERN = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private ClientConnection connection;

    EditText qrCode;
    ImageView btnQR;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();

        connection = ClientConnection.getInstance();

        btnAnimationGood = AnimationUtils.loadAnimation(getActivity(), R.anim.btn_save_good); //Cargar la animación del boton qr

        vista = inflater.inflate(R.layout.fragment_ui_connection,container,false);

        vista.findViewById(R.id.btnConnect).setOnClickListener(this);
        btnQR = (ImageView) vista.findViewById(R.id.btn_qrReader);
        btnQR.setOnClickListener(this);

        qrCode = (EditText) vista.findViewById(R.id.txtQRCode);

        return vista;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_qrReader:
                btnQR.startAnimation(btnAnimationGood);
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

                    startActivityForResult(intent, 0);
                }catch(Exception e){
                    AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                    build.setTitle(getText(R.string.getApp));
                    build.setMessage(getText(R.string.getApp_text));
                    build.setPositiveButton(getText(R.string.YES), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                            startActivity(marketIntent);
                            dialog.dismiss();
                        }
                    });
                    build.setNegativeButton(getText(R.string.NO), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), getText(R.string.getApp_decline), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = build.create();
                    dialog.show();
                }
                break;
            case R.id.btnConnect:
                try{
                    String[] datos = getQrData(qrCode.getText().toString());
                    connection.setConnection(getActivity(), datos[0], datos[1]);
                }catch(Exception e){Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();}
                break;
        }
    }

    /**
     * Respuesta de la aplicación de QR
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            String contents;
            if (data != null && (contents = data.getStringExtra("SCAN_RESULT")).length() > 0) {
                try {
                    String[] datosServer = getQrData(contents);
                    connection.setConnection(getActivity(), datosServer[0], datosServer[1]);
                }catch(Exception e){Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();}
            }
        }
    }

    /**
     * Desde el codigo QR devuelve un string[] con la IP y el Puerto respectivamente si el QR es valido.
     * @param data
     * @return Sting[]
     * @throws Exception
     */
    private String[] getQrData(String data) throws Exception{
        String[] datos;
        if(!QRCODE.matcher(data).find()){
            throw new Exception(getText(R.string.qr_invalid).toString());
        }else{
            datos = data.split(":");
            if(!IPADDRESS_PATTERN.matcher(datos[0]).matches()){
                throw new Exception(getText(R.string.ip_invalid).toString());
            }else if(Integer.parseInt(datos[1]) < 49152 &&  Integer.parseInt(datos[1]) > 65535){
                throw new Exception(getText(R.string.port_invalid).toString());
            }
        }
        return datos;
    }
}