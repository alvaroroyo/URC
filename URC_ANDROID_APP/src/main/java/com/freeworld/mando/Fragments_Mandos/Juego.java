package com.freeworld.mando.Fragments_Mandos;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.freeworld.mando.Network.IServerSend;
import com.freeworld.mando.Network.ClientConnection;
import com.freeworld.mando.R;

/**
 * Created by aroyo on 25/11/2015.
 */
public class Juego extends Fragment implements View.OnTouchListener,IServerSend {

    ImageView jostick,FL,FT,FR,FB;

    private ClientConnection connection;

    double centroJostickX = 0,centroJostickY = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();

        connection = ClientConnection.getInstance();

        View fragment = inflater.inflate(R.layout.fragment_mando_juego,container,false);

        FL = (ImageView) fragment.findViewById(R.id.FL);
        FT = (ImageView) fragment.findViewById(R.id.FT);
        FR = (ImageView) fragment.findViewById(R.id.FR);
        FB = (ImageView) fragment.findViewById(R.id.FB);
        jostick = (ImageView) fragment.findViewById(R.id.jostick);

        fragment.findViewById(R.id.btnB).setOnTouchListener(this); //Asignar directamente OnTouch por que no se van a modificar en código
        fragment.findViewById(R.id.btnL).setOnTouchListener(this); //Asignar directamente OnTouch por que no se van a modificar en código
        fragment.findViewById(R.id.btnR).setOnTouchListener(this); //Asignar directamente OnTouch por que no se van a modificar en código
        fragment.findViewById(R.id.btnT).setOnTouchListener(this); //Asignar directamente OnTouch por que no se van a modificar en código

        FL.setOnTouchListener(this);
        FT.setOnTouchListener(this);
        FR.setOnTouchListener(this);
        FB.setOnTouchListener(this);
        jostick.setOnTouchListener(this);

        return fragment;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(centroJostickY == 0){
            setVariables();
        }
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_DOWN:
                    switch (v.getId()) {
                        //Botones de juego
                        case R.id.btnB:
                            connection.sendPackage(BTN_BOTTOM_PRESS);
                            break;
                        case R.id.btnL:
                            connection.sendPackage(BTN_LEFT_PRESS);
                            break;
                        case R.id.btnR:
                            connection.sendPackage(BTN_RIGHT_PRESS);
                            break;
                        case R.id.btnT:
                            connection.sendPackage(BTN_TOP_PRESS);
                            break;
                        case R.id.FB:
                            connection.sendPackage(ARROW_BOTTOM);
                            break;
                        case R.id.FT:
                            connection.sendPackage(ARROW_TOP);
                            break;
                        case R.id.FL:
                            connection.sendPackage(ARROW_LEFT);
                            break;
                        case R.id.FR:
                            connection.sendPackage(ARROW_RIGHT);
                            break;
                    }
                    break;
                /*case MotionEvent.ACTION_MOVE:
                    if (v.getId() == R.id.jostick) {
                        float eventRX = event.getRawX();
                        float eventRY = event.getRawY();
                        int potencia = getPotencia(eventRX, eventRY);
                        potencia = (potencia > 13) ? 13 : potencia; //RESTRICTION MAX POT

                        int inclinacion = getInclinacion(eventRX, eventRY);

                        int inclinacionIzq = 0;
                        int inclinacionDer = 0;

                        if (eventRX < centroJostickX && eventRY < centroJostickY) { //ARRIBA IZQ

                            inclinacionIzq = inclinacion;

                        } else if (eventRX < centroJostickX && eventRY > centroJostickY) { //ABAJO IZQ

                            inclinacionIzq = inclinacion;
                            potencia = -potencia;

                        } else if (eventRX > centroJostickX && eventRY < centroJostickY) { //ARRIBA DER

                            inclinacionDer = -inclinacion;

                        } else if (eventRX > centroJostickX && eventRY > centroJostickY) { //ABAJO DER

                            inclinacionDer = -inclinacion;
                            potencia = -potencia;

                        }
                    }
                    break;*/
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_UP:
                    switch (v.getId()) {
                        case R.id.jostick:
                            System.out.println("Se ha dejado de pulsar el principal_game_jostick");
                            System.out.println("Potencia: 0");
                            System.out.println("Inclinacion: 90");
                            break;
                        case R.id.btnB:
                            connection.sendPackage(BTN_BOTTOM_UP);
                            break;
                        case R.id.btnT:
                            connection.sendPackage(BTN_TOP_UP);
                            break;
                        case R.id.btnL:
                            connection.sendPackage(BTN_LEFT_UP);
                            break;
                        case R.id.btnR:
                            connection.sendPackage(BTN_RIGHT_UP);
                            break;
                        case R.id.FB:
                            connection.sendPackage(ARROW_BOTTOM_UP);
                            break;
                        case R.id.FT:
                            connection.sendPackage(ARROW_TOP_UP);
                            break;
                        case R.id.FL:
                            connection.sendPackage(ARROW_LEFT_UP);
                            break;
                        case R.id.FR:
                            connection.sendPackage(ARROW_RIGHT_UP);
                            break;
                    }
                    break;
            }
        return true;
    }

    /**
     * Calculo de posiciones y vectores.
     */
    private void setVariables(){
        this.centroJostickY = (jostick.getBottom() - jostick.getHeight()/2)+28;
        this.centroJostickX = (jostick.getRight() - jostick.getWidth()/2)-1;
    }

    /**
     * Obtiene la potencia del joystick. La potencia es la distancia de la recta desde el centro hasta el punto donde se encuentra el dedo.
     * @param pX Coordenada X donde se encuentra el dedo **( getRawX )**
     * @param pY Coordenada Y donde se encuentra el dedo **( getRawY )**
     * @return Integer
     */
    private int getPotencia(double pX, double pY){
        return (int)(Math.floor(Math.sqrt(Math.pow((pX - centroJostickX), 2) + Math.pow((pY - centroJostickY), 2)) * 0.1));
    }

    /**
     * Obtiene el angulo de inclinacion de la recta respecto al eje x del joystick.
     * @param pX Coordenada X donde se encuentra el dedo **( getRawX )**
     * @param pY Coordenada Y donde se encuentra el dedo **( getRawY )**
     * @return Integer
     */
    private int getInclinacion(double pX, double pY){
        return (int) (Math.toDegrees(Math.atan((centroJostickY - pY) / (centroJostickX - pX))));
    }

    /**
     * Cambia de control de movimiento: Flechas <-> Joystick
     */
    public void changeControl(){
        if(jostick.getVisibility() == View.GONE){
            FL.setVisibility(View.GONE);
            FT.setVisibility(View.GONE);
            FR.setVisibility(View.GONE);
            FB.setVisibility(View.GONE);
            jostick.setVisibility(View.VISIBLE);
        }else{
            jostick.setVisibility(View.GONE);
            FL.setVisibility(View.VISIBLE);
            FT.setVisibility(View.VISIBLE);
            FR.setVisibility(View.VISIBLE);
            FB.setVisibility(View.VISIBLE);
        }
    }

}
