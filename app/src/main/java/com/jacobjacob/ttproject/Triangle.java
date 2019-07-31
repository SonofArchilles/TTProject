package com.jacobjacob.ttproject;

public class Triangle implements Object{
    private Vector A,B,C,Normal;

    public Triangle(Vector A, Vector B, Vector C){
        this.A=A;
        this.B = B;
        this.C = C;
        Normal = B.subtract(A).cross(C.subtract(A)).normalize();
    }
    public Vector getVector(int vector){
        if (vector == 0){
            return A;
        }
        else if (vector == 1){
            return B;
        }
        else if (vector == 2){
            return C;
        }
        else return A;
    }
    public Vector getNormal(){
        return Normal;
    }
    public void setVector(Vector input, int vectorposition){
        if (vectorposition == 0){
            A = input;
        }
        if (vectorposition == 1){
            B = input;
        }
        if (vectorposition == 2){
            C = input;
        }
    }

    public Triangle addVector(Vector Offsetadd){
        this.A.add(Offsetadd);
        this.B.add(Offsetadd);
        this.C.add(Offsetadd);
        return new Triangle(A,B,C);
    }
}
