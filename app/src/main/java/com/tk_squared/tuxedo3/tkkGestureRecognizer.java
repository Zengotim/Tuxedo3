package com.tk_squared.tuxedo3;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zengo on 1/12/2016.
 * Cuz Zengo rocks out loud!
 */
public class tkkGestureRecognizer implements View.OnTouchListener{

    //set true to catch, false to allow fallthrough
    private boolean _catch_lr = false; public void setCatchLR(boolean lr){_catch_lr = lr;}
    private boolean _catch_rl = false; public void setCatchRL(boolean rl){_catch_rl = rl;}
    private boolean _catch_tb = false; public void setCatchTB(boolean tb){_catch_tb = tb;}
    private boolean _catch_bt = false; public void setCatchBT(boolean bt){_catch_bt = bt;}

    public tkkGestureRecognizer(){}

    public tkkGestureRecognizer(boolean lr, boolean rl, boolean tb, boolean bt){
        _catch_bt = bt; _catch_tb = tb;
        _catch_lr = lr; _catch_rl = rl;
    }

    public enum Action {
        LR, // Left to Right
        RL, // Right to Left
        TB, // Top to bottom
        BT, // Bottom to Top
        None // when no action was detected
    }

    private static final int MIN_DISTANCE = 100;
    private float down_x, down_y, up_x, up_y;
    private Action tkkGestureDetected = Action.None;

    public boolean gestureDetected(){
        return tkkGestureDetected != Action.None;
    }

    public Action getGesture(){
        return tkkGestureDetected;
    }

    public boolean onTouch(View v, MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                down_x = event.getX();
                down_y = event.getY();
                tkkGestureDetected = Action.None;
                return false; //fallthrough for click events
            }
            case MotionEvent.ACTION_MOVE: {
                up_x = event.getX();
                up_y = event.getY();
                float deltaX = down_x - up_x;
                float deltaY = down_y - up_y;

                //Horizontal swipe detect
                if (_catch_rl || _catch_lr){
                if (Math.abs(deltaX) > MIN_DISTANCE){
                    if (deltaX < 0){
                        if (!_catch_lr)return false;
                        tkkGestureDetected = Action.LR;
                        return true;
                    }else{
                        if (!_catch_rl)return false;
                        tkkGestureDetected = Action.RL;
                        return true;
                    }
                }}
                //Vertical swipe detect
                if (_catch_tb || _catch_bt){
                if (Math.abs(deltaY) > MIN_DISTANCE){
                    if (deltaY < 0){
                        if (!_catch_tb)return false;
                        tkkGestureDetected = Action.TB;
                        return true;
                    }else{
                        if (!_catch_bt)return false;
                        tkkGestureDetected = Action.BT;
                        return true;
                    }
                }
                }
            }
        }
        return false;
    }
}
