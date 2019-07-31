package com.jacobjacob.ttproject;

import static com.jacobjacob.ttproject.Util.SEED;

public class Random1 {
    private int Hash = 6824;
    private long Randomnumber;


    public Random1(){
        Randomnumber = SEED + Hash + SEED * Hash;
        Randomnumber++;
    }

    public Vector returnVector(Vector Position,int staticAxis){
        float x = (float) Position.getValue(0);
        float y = (float) Position.getValue(1);
        float z = (float) Position.getValue(2);

        //if (staticAxis == 0){
        //    x = newValue(x);
//
        //    y = y;
        //    z = z;
        //}
        //if (staticAxis == 1){
            x = x;
            y = newValue(y);
            z = z;
        //}
        //if (staticAxis == 2){
        //    x = x;
        //    y = y;
        //    z = newValue(z);
        //}
        return new Vector(x,y,z);
    }

    private float newValue(float T){
        float Value = (float) (5*T * Math.sin(T));


        return Value;
    }

}
