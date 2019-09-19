package com.jacobjacob.ttproject.Tile;

import android.graphics.Rect;

import com.jacobjacob.ttproject.Vector;

import java.util.ArrayList;

import static com.jacobjacob.ttproject.Util.*;

public class Chunk {

    private int Size = 20;
    private Rect Boundingbox;
    private int X, Y;

    private Vector PositionChunkspace;

    private Tile[][] TilesinChunk;

    public Chunk(int X, int Y) { // Position in Chunkspace!!!
        this.Boundingbox = new Rect(X, Y, X + this.Size, Y + this.Size);
        this.TilesinChunk = new Tile[this.Size][this.Size];
        this.X = X;
        this.Y = Y;
        this.PositionChunkspace = new Vector(X, Y);
    }

    /**
     * Returns the Boundingbox / Hitbox that contains the Tiles
     *
     * @return Rect as Boundingbox
     */
    public Rect getBoundaries() {
        return this.Boundingbox;
    }

    /**
     * The Position is in Chunkspace, therfore the Width is not the Tilewidth, but the Width of the Chunk * the Tilewidth!!!
     *
     * @return the Position as a vector
     */
    public Vector getPositionRAW() {
        return this.PositionChunkspace;
    }

    public boolean isOnScreen() {

        Vector Cal = new Vector().getScreencoordinatesFromTileCoordinates(this.PositionChunkspace.multiplydouble(this.Size * TILESIZE));
        float TILESIZEzoom = (float) Math.ceil(this.Size * TILESIZE * (camera.getEye2D().getValue(2) / ZOOMFACTOR));


        int left = (int) Cal.getValue(0);//(float) visible.get(i).getPositionRAW().getX() * Scale;//width / height;
        int top = (int) Cal.getValue(1);//(float) visible.get(i).getPositionRAW().getY() * Scale;
        int right = (int) (Cal.getValue(0) + TILESIZEzoom);//(float) (visible.get(i).getPositionRAW().getX() + 1) * Scale;
        int bottom = (int) (Cal.getValue(1) + TILESIZEzoom);//(float) (visible.get(i).getPositionRAW().getY() + 1) * Scale;

        Rect TileRect = new Rect(left, top, right, bottom);

        Rect Screen = new Rect(0, 0, WIDTHSCREEN, HEIGHTSCREEN);

        if (Screen.contains(TileRect) || Screen.intersect(TileRect)) {
            return true;
        } else {
            return false;
        }
    }

    public void addTile(Tile TileToAdd) {
        if (this.Boundingbox.contains(TileToAdd.getBoundaries())) {
            TilesinChunk[(int) (this.X - TileToAdd.getPositionRAW().getX())][(int) (this.Y - TileToAdd.getPositionRAW().getY())] = TileToAdd;
        }
    }

    public Tile getTile(int X, int Y) {
        if (this.Boundingbox.contains(X, Y)) {
            try {
                return this.TilesinChunk[this.X - X][this.Y - Y];
            } catch (Exception e) {
                try {
                    return this.TilesinChunk[X - this.X][Y - this.Y];
                } catch (Exception f) {

                }
            }
        }
        return null;
    }

    public void removeTile() {

    }

    public ArrayList<Tile> getTilesInCurrentChunk() { // up to which Iteration you want the Boundary
        ArrayList<Tile> returnTilesInChunk = new ArrayList<>();
        for (int i = 0; i < this.Size; i++) {
            for (int j = 0; j < this.Size; j++) {
                returnTilesInChunk.add(this.TilesinChunk[i][j]);
            }
        }
        return returnTilesInChunk;
    }

}
