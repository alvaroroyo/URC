package com.freeworld.mando.Fragments_Mandos;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.freeworld.mando.Network.ClientConnection;
import com.freeworld.mando.Network.IServerSend;
import com.freeworld.mando.R;

/**
 * Created by Alvaro Royo on 27/01/2016.
 */
public class Mouse extends Fragment implements View.OnTouchListener,IServerSend {
    LinearLayout touchpadScrollBar,touchpadSurface,touchpadBtnLeft,touchpadBtnRight;

    long click_time;

    private ClientConnection connection;

    float scrollY;
    float surfaceX,surfaceY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();

        connection = ClientConnection.getInstance();

        View view = inflater.inflate(R.layout.fragment_mando_mouse,container,false);

        touchpadScrollBar = (LinearLayout) view.findViewById(R.id.TouchpadScrollBar);
        touchpadSurface = (LinearLayout) view.findViewById(R.id.TouchpadSurface);
        touchpadBtnLeft = (LinearLayout) view.findViewById(R.id.TouchpadBtnLeft);
        touchpadBtnRight = (LinearLayout) view.findViewById(R.id.TouchpadBtnRight);

        TouchpadScrollBarCanvas touchpadScrollBarCanvas = new TouchpadScrollBarCanvas(getActivity());

        touchpadScrollBar.addView(touchpadScrollBarCanvas);

        TouchpadSurfaceCanvas touchpadSurfaceCanvas = new TouchpadSurfaceCanvas(getActivity());

        touchpadSurface.addView(touchpadSurfaceCanvas);

        TouchpadButtonsCanvas touchpadButtonsCanvasLeft = new TouchpadButtonsCanvas(getActivity(),true);
        TouchpadButtonsCanvas touchpadButtonsCanvasRight = new TouchpadButtonsCanvas(getActivity(),false);

        touchpadBtnLeft.addView(touchpadButtonsCanvasLeft);
        touchpadBtnRight.addView(touchpadButtonsCanvasRight);

        touchpadScrollBar.setOnTouchListener(this);
        touchpadSurface.setOnTouchListener(this);
        touchpadBtnLeft.setOnTouchListener(this);
        touchpadBtnRight.setOnTouchListener(this);

        return view;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_MOVE:
                v.setPressed(true);
                float eventY = event.getY();
                float eventX = event.getX();
                switch(v.getId()){
                    case R.id.TouchpadSurface:
                        int totalX = (int)(eventX - surfaceX);
                        int totalY = (int)(eventY - surfaceY);
                        connection.sendPackage(totalX + "," + totalY);
                        surfaceX = eventX;
                        surfaceY = eventY;
                        break;
                    case R.id.TouchpadScrollBar:
                            if(eventY - scrollY != 0) {
                                if (scrollY > eventY) { //Movimiento hacia arriba
                                    connection.sendPackage(MOUSE_SCROLL_UP);
                                    scrollY = eventY;
                                } else { //Movimiento hacia abajo
                                    for (int i = 0; i < 10; i++) {
                                        connection.sendPackage(MOUSE_SCROLL_DOWN);
                                    }
                                    scrollY = event.getY();
                                }
                            }
                        break;
                }
                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                v.setPressed(true);
                switch(v.getId()){
                    case R.id.TouchpadBtnLeft:
                        connection.sendPackage(MOUSE_LEFT_DOWN);
                        break;
                    case R.id.TouchpadBtnRight:
                        connection.sendPackage(MOUSE_RIGHT_DOWN);
                        break;
                    case R.id.TouchpadSurface:
                        click_time = System.currentTimeMillis();
                        surfaceX = event.getX();
                        surfaceY = event.getY();
                        break;
                    case R.id.TouchpadScrollBar:
                        scrollY = event.getY();
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                v.setPressed(false);
                switch(v.getId()){
                    case R.id.TouchpadBtnLeft:
                        connection.sendPackage(MOUSE_LEFT_RELEASE);
                        break;
                    case R.id.TouchpadBtnRight:
                        connection.sendPackage(MOUSE_RIGHT_RELEASE);
                        break;
                    case R.id.TouchpadSurface: // Click in surface
                        if(System.currentTimeMillis() - click_time < 200){
                            connection.sendPackage(MOUSE_LEFT_CLICK);
                        }
                        break;
                }
                break;
        }
        return true;
    }

    private class TouchpadSurfaceCanvas extends View {
        public TouchpadSurfaceCanvas(Context context) {super(context);}

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setColor(Color.TRANSPARENT);
            canvas.drawPaint(paint);

            int stroke = 5;
            int marginLeft = (int)(getWidth()*0.15);
            int marginTop = (int)(getHeight()*0.15);
            paint.setColor(Color.argb(100, 88, 88, 88));
            paint.setAntiAlias(true);
            paint.setStrokeWidth(stroke);
            canvas.drawLine(marginLeft, getHeight() / 2, getWidth() - marginLeft, getHeight() / 2, paint);
            canvas.drawLine(getWidth() / 2, marginTop, getWidth() / 2, getHeight() - marginTop, paint);

            paint.setColor(Color.WHITE);
            canvas.drawLine(0, getHeight()-1, getWidth(), getHeight()-1, paint);
        }
    }

    private class TouchpadButtonsCanvas extends View {
        boolean isLeftButton;
        public TouchpadButtonsCanvas(Context context,boolean isLeftButton) {
            super(context);
            this.isLeftButton = isLeftButton;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setColor(Color.TRANSPARENT);
            canvas.drawPaint(paint);
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);

            if(isLeftButton){
                canvas.drawLine(getWidth(),0, getWidth(), getHeight(), paint);
            }else{
                canvas.drawLine(0,0, 0, getHeight(), paint);
            }
        }
    }

    private class TouchpadScrollBarCanvas extends View{
        public TouchpadScrollBarCanvas(Context context){super(context);}
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setColor(Color.TRANSPARENT);
            canvas.drawPaint(paint);

            int stroke = 5;
            //Margen entre los lados. A MAYOR margen MENOR línea.
            int maxWidth = 10;
            int minWidth = 25;
            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(stroke);

            int cantidad = 10; //Sin contar con el del medio. Cantidad que se va a pintar. Debe ser un impar.

            int marginTop = (getHeight()-(stroke*(cantidad)))/(cantidad);

            canvas.drawLine(minWidth, getHeight()/2, getWidth() - minWidth, getHeight()/2, paint);

            //Dibujar rayas hacia abajo.
            for(int i = 0; i<cantidad/2;i++) {
                int y = getHeight()/2 - (marginTop * (i+1));
                if (i % 2 == 0) {
                    canvas.drawLine(maxWidth, y, getWidth() - maxWidth, y, paint);
                } else {
                    canvas.drawLine(minWidth, y, getWidth() - minWidth, y, paint);
                }
            }
            //Dibujar rayas hacia arriba.
            for(int i = 0;i<cantidad/2;i++ ){
                int y = getHeight()/2 + (marginTop * (i+1));
                if (i % 2 == 0) {
                    canvas.drawLine(maxWidth, y, getWidth() - maxWidth, y, paint);
                } else {
                    canvas.drawLine(minWidth, y, getWidth() - minWidth, y, paint);
                }
            }
        }
    }
}
