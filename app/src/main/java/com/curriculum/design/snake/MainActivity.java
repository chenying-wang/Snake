package com.curriculum.design.snake;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MainActivity extends Activity
        implements GestureDetector.OnGestureListener
{
    private GLSurfaceView mGLView;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    Control mControl = new Control();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGLView = new SnakeGLSurfaceView(this);
        setContentView(mGLView);
        mDetector = new GestureDetectorCompat(this, this);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        float moveX = event2.getX() - event1.getX();
        float moveY = event2.getY() - event1.getY();
        mControl.setco(moveX, moveY);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent event) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return false;
    }
}

class SnakeGLSurfaceView extends GLSurfaceView {

    final static short UPDATE = 1;
    final static long INTERVAL = 200;

    protected SnakeGL20Renderer mRenderer;

    public SnakeGLSurfaceView(Context context){
        super(context);

        setEGLContextClientVersion(2);
        mRenderer = new SnakeGL20Renderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        new Thread(new TimerThread()).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what == UPDATE) {
                requestRender();
                super.handleMessage(msg);
            }
        }
    };

    private class TimerThread implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    //pause
                    Thread.sleep(INTERVAL);
                    //create new message
                    Message message = new Message();
                    message.what = UPDATE;
                    // send message to handler
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

}
