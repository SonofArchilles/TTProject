package com.jacobjacob.ttproject.OpenGL.Shapes;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.jacobjacob.ttproject.Util.HEIGHTSCREEN;
import static com.jacobjacob.ttproject.Util.WIDTHSCREEN;

/*
 * A triangle with 3 vertices.
 */
public class Triangle {
    //https://github.com/ibraimgm/opengles2-2d-demos/blob/master/src/ibraim/opengles2/Triangle2dActivity.java


    //private class Triangle2dRenderer //implements GLSurfaceView.Renderer
    //{
    private int vertexHandle;
    private int fragmentHandle;
    private int programHandle = -1;

    // These two methods help to Load/Unload the shaders used by OpenGL Es 2.0
    // Remember that now OpenGL DOES NOT CONTAIN most of the 'old' OpenGL functions;
    // Now you need to create your own vertex and fragment shaders. Yay!
    public void setup() {
        // make sure there's nothing already created
        tearDown();

        // Vertex shader source.
        // This shader uses a constant 4x4 matrix 'uScreen' and multiplies it to
        // the parameter aPosition. The x and y values of aPosition will be filled with
        // the vertices of our triangle. uScreen will be a matrix that, when multiplied
        // with the values of our position will CONVERT these values to the OpenGL coordinate
        // system. This way we can, say, inform our coordinates in 'pixels' and let OpenGL
        // figure out were the hell the pixels are. More on this later.
        String vertexSrc = "uniform mat4 uScreen;\n" + "attribute vec2 aPosition;\n" + "void main() {\n" + "  gl_Position = uScreen * vec4(aPosition.xy, 0.0, 1.0);\n" + "}";

        // Our fragment shader. Always returns the color RED.
        String fragmentSrc = "precision mediump float;\n" + "void main(void)\n" + "{\n" + "  gl_FragColor = vec4(1, 0, 0, 1);\n" + "}";

        // Lets load and compile our shaders, link the program
        // and tell OpenGL ES to use it for future drawing.
        vertexHandle = loadShader(GLES20.GL_VERTEX_SHADER, vertexSrc);
        fragmentHandle = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSrc);
        programHandle = createProgram(vertexHandle, fragmentHandle);

        GLES20.glUseProgram(programHandle);
    }

    public void tearDown() {
        if (programHandle != -1) {
            GLES20.glDeleteProgram(programHandle);
            GLES20.glDeleteShader(vertexHandle);
            GLES20.glDeleteShader(fragmentHandle);
        }
    }

    // auxiliary shader functions. Doesn't matter WHAT you're trying to do, they're
    // always the same thing.
    private int loadShader(int shaderType, String shaderSource) {
        int handle = GLES20.glCreateShader(shaderType);

        if (handle == GLES20.GL_FALSE) throw new RuntimeException("Error creating shader!");

        // set and compile the shader
        GLES20.glShaderSource(handle, shaderSource);
        GLES20.glCompileShader(handle);

        // check if the compilation was OK
        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(handle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        if (compileStatus[0] == 0) {
            String error = GLES20.glGetShaderInfoLog(handle);
            GLES20.glDeleteShader(handle);
            throw new RuntimeException("Error compiling shader: " + error);
        } else return handle;
    }

    private int createProgram(int vertexShader, int fragmentShader) {
        int handle = GLES20.glCreateProgram();

        if (handle == GLES20.GL_FALSE) throw new RuntimeException("Error creating program!");

        // attach the shaders and link the program
        GLES20.glAttachShader(handle, vertexShader);
        GLES20.glAttachShader(handle, fragmentShader);
        GLES20.glLinkProgram(handle);

        // check if the link was successful
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(handle, GLES20.GL_LINK_STATUS, linkStatus, 0);

        if (linkStatus[0] == 0) {
            String error = GLES20.glGetProgramInfoLog(handle);
            GLES20.glDeleteProgram(handle);
            throw new RuntimeException("Error in program linking: " + error);
        } else return handle;
    }


    public void onDrawFrame() {
        // get the position of 'aPosition'
        int aPos = GLES20.glGetAttribLocation(programHandle, "aPosition");

        // The triangle vertices. Note how I'm using
        // a 'pixel' coordinate system. This is not in the center of the
        // screen or anything; this is in absolute position, will vary depending
        // on the size of your display.
        float[] data = {0, WIDTHSCREEN, HEIGHTSCREEN, WIDTHSCREEN, HEIGHTSCREEN, 0,};

        // Again, a FloatBuffer will be used to pass the values
        FloatBuffer b = ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        b.put(data).position(0);

        // Enable and set the vertex attribute to accept our array.
        // This makes possible to inform all of the vertices in one call.
        GLES20.glVertexAttribPointer(aPos, 2, GLES20.GL_FLOAT, false, 0, b);
        GLES20.glEnableVertexAttribArray(aPos);

        // Clear the screen and draw the triangle
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
    //}
}
