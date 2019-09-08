package com.jacobjacob.ttproject.Light;

import android.graphics.Color;

import com.jacobjacob.ttproject.Vector;

public class PointLight {
    Vector Positionvec;
    int Colorint;

    public PointLight(Vector Position){
        this.Positionvec = Position;
        this.Colorint = Color.rgb(255,255,255);
    }

    /**
     * Lightconstructor
     * @param Position Position of light as raw
     * @param Colorint Color of the Light as int
     */
    public PointLight(Vector Position,int Colorint){
        this.Positionvec = Position;
        this.Colorint = Colorint;
    }

    public void setPositionvec(Vector positionvec) {
        Positionvec = positionvec;
    }

    public void setColorint(int colorint) {
        Colorint = colorint;
    }

    public Vector getPositionvec() {
        return Positionvec;
    }

    public int getColorint() {
        return Colorint;
    }

}
