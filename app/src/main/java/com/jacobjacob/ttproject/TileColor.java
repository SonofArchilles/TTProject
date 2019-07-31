package com.jacobjacob.ttproject;

import android.graphics.Color;
import android.graphics.Rect;

import static com.jacobjacob.ttproject.Util.TILESIZE;

public class TileColor {
    private Vector Position, PositionRAW;
    private Rect Boundaries;

    private int Red;
    private int Green;
    private int Blue;
    private float Depth;
/*/
    public TileColor(Vector Position, Vector Color) {
        this.PositionRAW = Position;
        this.Position = Position.multiplydouble(TILESIZE);

        this.Red = (int)Color.getValue(0);
        this.Green = (int)Color.getValue(1);
        this.Blue = (int)Color.getValue(2);

        this.Boundaries = new Rect((int) PositionRAW.getValue(0), (int) PositionRAW.getValue(1), (int) (PositionRAW.getValue(0) + 1), (int) (PositionRAW.getValue(1) + 1));
    }/**/

    public TileColor(Vector Position,float Depth, int Red,int Green,int Blue) {
        this.PositionRAW = Position;
        this.Position = Position.multiplydouble(TILESIZE);
        this.Red = Red;
        this.Green = Green;
        this.Blue = Blue;
        this.Depth = /*/Depth * 2;/*/(1+Depth)/**0.5f*/;/**/

        this.Boundaries = new Rect((int) PositionRAW.getValue(0), (int) PositionRAW.getValue(1), (int) (PositionRAW.getValue(0) + 1), (int) (PositionRAW.getValue(1) + 1));
    }

    public Vector getPosition() {
        return Position;

    }

    public int getColor(){
        return Color.rgb(this.Red,this.Green,this.Blue);
    }

    public float getDepth(){
        return this.Depth;
    }
}
