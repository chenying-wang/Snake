package com.curriculum.design.snake;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SnakeGL20Renderer implements GLSurfaceView.Renderer {

    private Snake mSnake;
    private  Font mScore;
    private int[] screenPixels = new int[2];
    private Control mc = new Control();

    public SnakeGL20Renderer() {
        super();
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // set background color in GLColor
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // when surface create or the beginning of program
        mSnake = new Snake();
        mScore = new Font();

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // loop every interval
        mSnake.move(mc.direction);
        mScore.setScore(mSnake.getSize());
        mScore.draw();
        mSnake.drawAll();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

}


