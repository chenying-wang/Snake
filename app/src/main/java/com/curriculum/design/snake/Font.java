package com.curriculum.design.snake;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Font {

    private int score;
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 aTexCoord;" +
                    "varying vec2 vTexCoord;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "  vTexCoord.x  = aTexCoord.x;" +
                    "  vTexCoord.y  = 1.0 - aTexCoord.y;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec2 vTexCoord;" +
                    "uniform sampler2D tex;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(tex, vTexCoord);" +
                    "}";



    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle, mTexPositionHandle, mTextureHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3, COORDS_PER_TEX_VERTEX = 2;

    private float squareCoords[] = new float[] {
            0.675f, 0.92f, 0.0f, // Position 0
            0.0f, 0.0f, // TexCoord 0

            0.875f, 0.92f, 0.0f, // Position 1
            0.2f, 0, // TexCoord 1

            0.875f , 0.825f, 0.0f, // Position 2
            0.2f, 0.2f, // TexCoord 2

            0.675f, 0.825f, 0.0f, // Position 3
            0.0f, 0.0f, // TexCoord 3
    };

    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    // 5 bytes per vertex and texVertex
    private final int vertexStride = (COORDS_PER_VERTEX + COORDS_PER_TEX_VERTEX) * 4;
    private final int texVertexStride = (COORDS_PER_VERTEX + COORDS_PER_TEX_VERTEX) * 4;

    // create bitmap
    private Bitmap bitmap;
    private int[] textureId = new int[1];

    public Font() {

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = SnakeGL20Renderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = SnakeGL20Renderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    public void draw() {
        initFontBitmap();
        loadTexture();

        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mTexPositionHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoord");

        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "tex");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mTexPositionHandle);

        // Set the sampler to texture unit 0
        GLES20.glUniform1i(mTextureHandle, 0);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        GLES20.glVertexAttribPointer(
                mTexPositionHandle, COORDS_PER_TEX_VERTEX,
                GLES20.GL_FLOAT, false,
                texVertexStride, vertexBuffer);

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexPositionHandle);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        score = newScore;
    }

    private int[] loadTexture() {

        // Generate a texture object
        GLES20.glGenTextures(1, textureId, 0);

        int[] result = null;

        if (textureId[0] != 0) {

            result = new int[3];
            result[0] = textureId[0]; // TEXTURE_ID
            result[1] = bitmap.getWidth(); // TEXTURE_WIDTH
            result[2] = bitmap.getHeight(); // TEXTURE_HEIGHT

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();

        } else {
            throw new RuntimeException("Error loading texture.");
        }

        return result;
    }

    private void initFontBitmap(){
        bitmap = Bitmap.createBitmap(256, 266, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.argb(255, 0, 0, 0));

        Paint p = new Paint();
        String fontType = "Roboto";
        Typeface typeface = Typeface.create(fontType, Typeface.BOLD);
        p.setColor(Color.WHITE);
        p.setTypeface(typeface);
        p.setTextSize(18);

        canvas.drawText(score+"", 175, 42, p);
    }


}

