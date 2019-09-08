package com.jacobjacob.ttproject.OpenGL.Projection;

import android.opengl.Matrix;

public class Camera {


    // Position the eye behind the origin.
    float eyeX = 0.0f;
    float eyeY = 0.0f;
    float eyeZ = 1.5f;

    // We are looking toward the distance
    float lookX = 0.0f;
    float lookY = 0.0f;
    float lookZ = -5.0f;

    // Set our up vector. This is where our head would be pointing were we holding the camera.
    float upX = 0.0f;
    float upY = 1.0f;
    float upZ = 0.0f;

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mViewMatrix = new float[16];

    public Camera(float x, float y, float z){

        this.eyeX = x;
        this.eyeY = y;
        this.eyeZ = z;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.

    }

    public void ChangePosition(int x, int y, int z){
        this.eyeX += x;
        this.eyeY += y;
        this.eyeZ += z;
    }


}
