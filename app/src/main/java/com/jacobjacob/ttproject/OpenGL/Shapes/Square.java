package com.jacobjacob.ttproject.OpenGL.Shapes;

import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.jacobjacob.ttproject.OpenGL.MainRenderer;
import com.jacobjacob.ttproject.Vector;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.glGetError;
import static com.jacobjacob.ttproject.Util.COLORDEBUG;
import static com.jacobjacob.ttproject.Util.HEIGHTSCREEN;
import static com.jacobjacob.ttproject.Util.WIDTHSCREEN;

/*
 * A square drawn in 2 triangles (using TRIANGLE_STRIP).
 * https://www3.ntu.edu.sg/home/ehchua/programming/android/Android_3D.html
 */


public class Square/*/ extends MainRenderer/**/ {
    // Constructor - Setup the vertex buffer

    private FloatBuffer _rectangleVFB;

    private float red, green, blue, alpha;
    private String _rectangleFragmentShaderCode = "attribute vec4 aColor;\n" + "void main() {\n" + " gl_FragColor = vec4(1.0f,0.0g,0.0f,1.0f);\n" // Color of the Rectangle
            + "}                             \n";

    private int _rectangleProgram, _rectangleAPositionLocation,_rectangleAColorLocation, _rectangleVertexShader, _rectangleFragmentShader;

    private String _rectangleVertexShaderCode = "attribute vec4 aPosition;\n" + "void main() {\n" + " gl_Position = aPosition;\n" + "}\n";


    float color[] = {this.red, this.green, this.blue, 1.0f};

    /**
     * Sqare with 4 points in Screenspace, not -1 1 -1 1 but rather 0 width 0 height
     *
     * @param Topleft     Top left point of the shape
     * @param Topright    Top right point of the shape
     * @param Bottomright Bottom right point of the shape
     * @param Bottomleft  Bottom left point of the shape
     * @param Colorint    Color of the shape
     */
    public Square(Vector Topleft, Vector Topright, Vector Bottomright, Vector Bottomleft, int Colorint) {

        //this._rectangleVertexShaderCode = "";

        this.red = (float) (Color.red(Colorint) / 255f);
        this.green = (float) (Color.green(Colorint) / 255f);
        this.blue = 1;//(float)(Color.blue(Colorint) /255f);
        this.alpha = 1;//(float)(Color.alpha(Colorint)/255f);

        this.color = new float[]{this.red, this.green, this.blue, 1.0f};

        initShapes(Topleft, Topright, Bottomright, Bottomleft);
        FragmentShader();

        this._rectangleVertexShader = loadShader(GLES20.GL_VERTEX_SHADER, this._rectangleVertexShaderCode);
        this._rectangleFragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, this._rectangleFragmentShaderCode);


        this._rectangleProgram = GLES20.glCreateProgram();


        GLES20.glAttachShader(this._rectangleProgram, this._rectangleVertexShader);
        GLES20.glAttachShader(this._rectangleProgram, this._rectangleFragmentShader);


        GLES20.glLinkProgram(this._rectangleProgram);
        this._rectangleAPositionLocation = GLES20.glGetAttribLocation(this._rectangleProgram, "aPosition");
        //this._rectangleAColorLocation = GLES20.glGetAttribLocation(this._rectangleProgram, "aColor");
        int a = 5;
    }

    private void initShapes(Vector Topleft, Vector Topright, Vector Bottomright, Vector Bottomleft) {


        float XLT = (float) ((Topleft.getValue(0) / WIDTHSCREEN) * 2 - 1);
        float YLT = (float) -((Topleft.getValue(1) / HEIGHTSCREEN) * 2 - 1);

        float XRT = (float) ((Topright.getValue(0) / WIDTHSCREEN) * 2 - 1);
        float YRT = (float) -((Topright.getValue(1) / HEIGHTSCREEN) * 2 - 1);

        float XRB = (float) ((Bottomright.getValue(0) / WIDTHSCREEN) * 2 - 1);
        float YRB = (float) -((Bottomright.getValue(1) / HEIGHTSCREEN) * 2 - 1);

        float XLB = (float) ((Bottomleft.getValue(0) / WIDTHSCREEN) * 2 - 1);
        float YLB = (float) -((Bottomleft.getValue(1) / HEIGHTSCREEN) * 2 - 1);

        float rectangleVFA[] = {XLB, YLB, 0, // Bottom left // Triangle 1 A
                XLT, YLT, 0, // Top left    // Triangle 1 B
                XRT, YRT, 0, // top right   // Triangle 1 C
                XRT, YRT, 0, // top right   // Triangle 2 C
                XRB, YRB, 0, // Bottom right// Triangle 2 D
                XLB, YLB, 0, // Bottom left // Triangle 2 A
        };


        ByteBuffer rectangleVBB = ByteBuffer.allocateDirect(rectangleVFA.length * 4);
        rectangleVBB.order(ByteOrder.nativeOrder());
        this._rectangleVFB = rectangleVBB.asFloatBuffer();
        this._rectangleVFB.put(rectangleVFA);
        this._rectangleVFB.position(0);
    }

    private void FragmentShader() {

        this._rectangleFragmentShaderCode = "uniform vec4(" + this.red + ", " + this.green + "," + this.blue + "," + this.alpha + ");" + "void main() {\n"

                + "gl_FragColor = vColor;\n" // Color of the Rectangle
                + "}\n";

        //this._rectangleFragmentShaderCode = "void main() {\n"

        //        + "gl_FragColor = vec4(" + this.red + ", " + this.green + "," + this.blue + "," + this.alpha + ");\n" // Color of the Rectangle
        //        + "}\n";
    }

    private int loadShader(int type, String source) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        glGetError();
        return shader;
    }

    // Render the shape
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void draw() {


        GLES20.glEnable(GLES20.GL_COLOR_CLEAR_VALUE);


        this.red = (float) Color.red(COLORDEBUG) / 255f;
        this.green = (float) Color.green(COLORDEBUG) / 255f;
        this.blue = (float) Color.blue(COLORDEBUG) / 255f;

        /**/
        GLES20.glUseProgram(this._rectangleProgram);
        GLES20.glVertexAttribPointer(this._rectangleAPositionLocation, 3, GLES20.GL_FLOAT, false, 12, this._rectangleVFB);


        //GLES20.glBlendColor(this.red, this.green, this.blue, 1);
        GLES20.glEnableVertexAttribArray(this._rectangleAPositionLocation);

        GLES30.glClearColor(this.red, this.green, this.blue, 1);

        // get handle to fragment shader's vColor member
        int mColorHandle = GLES20.glGetUniformLocation(this._rectangleProgram, "gl_FragColor");


        // Set color for drawing the triangle



        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        //GLES20.glUniform4fv(0,color,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);


        //GLES20.glDrawElements(GLES20.GL_TRIANGLES,6,GLES20.GL_SHORT,this._rectangleAPositionLocation);
        GLES20.glDisableVertexAttribArray(this._rectangleAPositionLocation);

    }



}
