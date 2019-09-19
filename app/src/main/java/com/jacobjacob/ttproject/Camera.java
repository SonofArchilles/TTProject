package com.jacobjacob.ttproject;

//import static com.jacobjacob.ttproject.Util.FRAMEDRAWN;
import static com.jacobjacob.ttproject.Util.*;


public class Camera {

    private int l = WIDTH/ 2;   // left
    private int r = -l;         // right
    private int t = HEIGHT / 2; // top
    private int b = -t;         // bottom
    private Vector UP = new Vector(0, 1, 0);   // up-Vector

    private Vector eye = new Vector(0, 0, 10); // Cameraposition
    private Vector eye2D = new Vector(0,0,700);
    private Vector eye2DFRAME = new Vector(0,0,700);

    private Vector Z = new Vector(0, 0, 1);    // Position the Camera looks at

    private Vector W = eye.subtract(Z).normalize();
    private Vector U = UP.cross(W).normalize();
    private Vector V = W.cross(U).normalize();

    private double d = t / Math.tan(Math.PI / 4) / 2; //Math.PI/4 == FOV
    private Vector W_d_negated = W.skalarmultiplikation(d * -1);
    public static float RotationX, RotationY;

    public Camera(Vector eye, Vector Z) {
        this.eye = eye;
        this.Z = Z;

        this.W = eye.subtract(Z).normalize();
        this.U = UP.cross(W).normalize();
        this.V = W.cross(U).normalize();

        d = t / Math.tan(Math.PI / 4) / 2;

        W_d_negated = W.skalarmultiplikation(d * -1);
    }

    public void move(float x, float y){
        RotationY = (float)(-y * mousemovespeed);

        if (RotationY > 90) {
            RotationY = 90;
        }
        if (RotationY < -90) {
            RotationY = -90;
        }
        RotationX = (float) Math.toRadians((x * mousemovespeed));
        RotationY = (float) Math.toRadians(RotationY);

        Vector direction = new Vector(Math.sin(RotationX) * Math.cos(RotationY), Math.sin(RotationY), Math.cos(RotationX) * Math.cos(RotationY));

        direction.subtract(getEye());
        Z = direction;

        W = (direction.normalize());
        U = getUP().cross(W).normalize();
        V = W.cross(U).normalize();
        setW_d_negated(W.skalarmultiplikation(-d));
    }
    public void move(Vector Positonoffset){
        setEye(Positonoffset);
        //MainActivity.uHandeler.updateScreen();
    }
    public void move2D(Vector Positonoffset){

        //setEye2D(Positonoffset);
        MOVE.Move(Positonoffset);
        //MainActivity.uHandeler.updateScreen();
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }


    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public Vector getUP() {
        return UP;
    }

    public Vector getEye2D() {
        //if (FRAMEDRAWN) {
        //    this.eye2DFRAME = this.eye2D;
        //    return this.eye2D;
        //}else {
            return this.eye2DFRAME;
        //}
    }
    public void UpdateEye2D(){
        this.eye2DFRAME = this.eye2D;
    }

    public Vector getEye() {
        return this.eye;
    }

    public void setEye(Vector eye) {
        if (eye != null) {
            this.eye = this.eye.addVector(eye);
        }
    }
    public void setEye2D(Vector eye) {
        if (eye != null) {
            this.eye2D = this.eye2D.addVector(eye);
        }
    }
    public Vector getW() {
        return W.normalize(); //W
    }

    public void setW_d_negated(Vector w_d_negated) {
        W_d_negated = w_d_negated;
    }
}
