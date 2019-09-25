package com.jacobjacob.ttproject.Tile;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.jacobjacob.ttproject.Vector;

import static com.jacobjacob.ttproject.Util.*;

//import static com.jacobjacob.ttproject.MainActivity.uHandeler;

public class Tile {
    private Vector Position, PositionRAW, Screenposition;
    private int IDint = 0;
    private int Frames = 0;
    //private boolean onScreen;
    //private boolean inTilechunks;
    private Rect Boundaries;
    private Rect OldBoundaries;
    private int Starttime;

    //private boolean Autotile = false; // if this tiles appearance should be changed
    private int Material = 1;


    public Tile(Vector Position, int Material) {
        this.Starttime = (int) System.currentTimeMillis();
        this.PositionRAW = Position;
        this.Position = Position.multiplydouble(TILESIZE);
        this.Material = Material;

        this.Boundaries = new Rect((int) this.PositionRAW.getValue(0), (int) this.PositionRAW.getValue(1), (int) (this.PositionRAW.getValue(0) + 1), (int) (this.PositionRAW.getValue(1) + 1));
        this.OldBoundaries = this.Boundaries;
    }

    public Tile(Vector Position, int Material, int Frames) {
        this.Starttime = (int) System.currentTimeMillis();
        this.PositionRAW = Position;
        this.Position = Position.multiplydouble(TILESIZE);
        this.Material = Material;
        this.Frames = Frames;

        this.Boundaries = new Rect((int) this.PositionRAW.getValue(0), (int) this.PositionRAW.getValue(1), (int) (this.PositionRAW.getValue(0) + 1), (int) (this.PositionRAW.getValue(1) + 1));
        this.OldBoundaries = this.Boundaries;
    }

    public Vector getScreenposition() {
        if (this.Screenposition != null) {
            return this.Screenposition;
        } else {
            return Position;
        }
    }

    public boolean isOnScreen(){


        Vector Cal = new Vector().getScreencoordinatesFromTileCoordinates(this.getPosition());
        float TILESIZEzoom = (float) Math.ceil(TILESIZE * (camera.getEye2D().getValue(2) / ZOOMFACTOR));


        int left = (int) Cal.getValue(0);//(float) visible.get(i).getPositionRAW().getX() * Scale;//width / height;
        int top = (int) Cal.getValue(1);//(float) visible.get(i).getPositionRAW().getY() * Scale;
        int right = (int) (Cal.getValue(0) + TILESIZEzoom);//(float) (visible.get(i).getPositionRAW().getX() + 1) * Scale;
        int bottom = (int) (Cal.getValue(1) + TILESIZEzoom);//(float) (visible.get(i).getPositionRAW().getY() + 1) * Scale;

        Rect TileRect = new Rect(left,top,right,bottom);

        Rect Screen = new Rect(0,0,WIDTHSCREEN,HEIGHTSCREEN);

        if (Screen.contains(TileRect) || Screen.intersect(TileRect)) {
            return true;
        }else {
            return false;
        }
    }

    public Vector getPosition() {
        return this.Position;

    }

    public Vector getPositionRAW() {
        if (this.PositionRAW != null) {
            return this.PositionRAW;
        } else {
            return new Vector();
        }
    }

    public int getIDint() {
        return this.IDint;
    }

    public int getFrames() {
        return this.Frames;
    }


    public int getMaterial() {
        return this.Material;
    }

    public int getStarttime() {
        return this.Starttime;
    }

    public boolean isAutotile() {
        return true;//Autotile;
    }

    //public void setAutotile(boolean autotile) {
    //    this.Autotile = autotile;
    //}

    public void setID(int id) {
        this.IDint = id;
    }

    public void setPositionRAW(Vector Raw) {
        this.PositionRAW = Raw;
        this.Position = Raw.multiplydouble(TILESIZE);

    }

    public void addPositionRAW(Vector Raw) {
        this.PositionRAW = this.PositionRAW.addVector(Raw);
        this.Position = this.Position.addVector(Raw.multiplydouble(TILESIZE));

    }

    public void setMaterial(int material) {
        this.Material = material;
    }

    public void setStarttime(int starttime) {
        this.Starttime = starttime;
    }

    public Rect getBoundaries() {
        if (this.Boundaries != null) {

            return this.Boundaries;
        } else {
            return null;
        }
    }
    public Rect getOldBoundaries() {
        if (this.OldBoundaries != null) {

            return this.OldBoundaries;
        } else {
            return null;
        }
    }

    public Bitmap getTexture() {
        if (this.Frames < -1 || 1 < this.Frames) {
            //maybe add FramesArray?
            return TILETEXTURE.getBitmap(this.IDint, this.Material);
        }
        return TILETEXTURE.getBitmap(this.IDint, this.Material);
    }
}
