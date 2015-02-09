package com.game.framework.impl;

import android.view.MotionEvent;
import android.view.View;

import com.game.framework.Input;
import com.game.framework.Pool;

import java.util.ArrayList;
import java.util.List;

public class ComboTouchHandler implements TouchHandler {

    boolean singleTouchMode;

    boolean[] isTouched = new boolean[20];
    int[] touchX = new int[20];
    int[] touchY = new int[20];
    Pool<Input.TouchEvent> touchEventPool;
    List<Input.TouchEvent> touchEvents = new ArrayList<Input.TouchEvent>();
    List<Input.TouchEvent> touchEventsBuffer = new ArrayList<Input.TouchEvent>();
    float scaleX;
    float scaleY;


    public ComboTouchHandler(View view, float scaleX, float scaleY)
    {
        Pool.PoolObjectFactory<Input.TouchEvent> factory = new Pool.PoolObjectFactory<Input.TouchEvent>() {
            @Override
            public Input.TouchEvent createObject() {
                return new Input.TouchEvent();
            }
        };
        touchEventPool = new Pool<Input.TouchEvent>(factory, 100);

        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {

            if(singleTouchMode)
            {
                Input.TouchEvent touchEvent = touchEventPool.newObject();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchEvent.type = Input.TouchEvent.TOUCH_DOWN;
                        isTouched[0] = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchEvent.type = Input.TouchEvent.TOUCH_DRAGGED;
                        isTouched[0] = true;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        touchEvent.type = Input.TouchEvent.TOUCH_UP;
                        isTouched[0] = false;
                        break;
                }

                touchEvent.x = touchX[0] = (int)(event.getX() * scaleX);
                touchEvent.y = touchY[0] = (int)(event.getY() * scaleY);
                touchEventsBuffer.add(touchEvent);

                return true;
            }

            else
            {
                int action = event.getAction() & MotionEvent.ACTION_MASK;
                int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                int pointerId = event.getPointerId(pointerIndex);
                Input.TouchEvent touchEvent;

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        touchEvent = touchEventPool.newObject();
                        touchEvent.type = Input.TouchEvent.TOUCH_DOWN;
                        touchEvent.pointer = pointerId;
                        touchEvent.x = touchX[pointerId] = (int) (event
                                .getX(pointerIndex) * scaleX);
                        touchEvent.y = touchY[pointerId] = (int) (event
                                .getY(pointerIndex) * scaleY);
                        isTouched[pointerId] = true;
                        touchEventsBuffer.add(touchEvent);
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_CANCEL:
                        touchEvent = touchEventPool.newObject();
                        touchEvent.type = Input.TouchEvent.TOUCH_UP;
                        touchEvent.pointer = pointerId;
                        touchEvent.x = touchX[pointerId] = (int) (event
                                .getX(pointerIndex) * scaleX);
                        touchEvent.y = touchY[pointerId] = (int) (event
                                .getY(pointerIndex) * scaleY);
                        isTouched[pointerId] = false;
                        touchEventsBuffer.add(touchEvent);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int pointerCount = event.getPointerCount();
                        for (int i = 0; i < pointerCount; i++) {
                            pointerIndex = i;
                            pointerId = event.getPointerId(pointerIndex);

                            touchEvent = touchEventPool.newObject();
                            touchEvent.type = Input.TouchEvent.TOUCH_DRAGGED;
                            touchEvent.pointer = pointerId;
                            touchEvent.x = touchX[pointerId] = (int) (event
                                    .getX(pointerIndex) * scaleX);
                            touchEvent.y = touchY[pointerId] = (int) (event
                                    .getY(pointerIndex) * scaleY);
                            touchEventsBuffer.add(touchEvent);
                        }
                        break;
                }

                return true;
            }


        }
    }

    @Override
    public boolean isTouchDown(int pointer) {

        synchronized (this) {
           if(singleTouchMode) pointer = 0;

           if (pointer < 0 || pointer >= 20)
               return false;
           else
               return isTouched[pointer];
        }
    }

    @Override
    public int getTouchX(int pointer) {
        synchronized (this) {
            if(singleTouchMode) pointer = 0;


            if (pointer < 0 || pointer >= 20)
                return 0;
            else
                return touchX[pointer];
        }
    }

    @Override
    public int getTouchY(int pointer) {
        synchronized (this) {
            if(singleTouchMode) pointer = 0;

            if (pointer < 0 || pointer >= 20)
                return 0;
            else
                return touchY[pointer];
        }
    }

    @Override
    public List<Input.TouchEvent> getTouchEvents() {
        synchronized(this) {
            int len = touchEvents.size();
            for( int i = 0; i < len; i++ )
                touchEventPool.free(touchEvents.get(i));
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }



    public void setSingleTouchMode()
    {
        singleTouchMode = true;
    }

    public void setMultiTouchMode()
    {
        singleTouchMode = false;
    }
}
