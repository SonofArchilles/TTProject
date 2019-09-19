package com.jacobjacob.ttproject.Tile;


import android.graphics.Rect;

import com.jacobjacob.ttproject.Vector;

import java.util.ArrayList;
import java.util.Collections;

import static com.jacobjacob.ttproject.Util.*;
public class KdTreeChunks {


    private ArrayList<Chunk> ChunksInCurrentTree;
    private ArrayList<Chunk> ChunkChildren1 = new ArrayList<>(); // Top / Right
    private ArrayList<Chunk> ChunkChildren2 = new ArrayList<>(); // Bottom / Left

    private ArrayList<KdTreeChunks> Children = new ArrayList<>();

    private Rect BoundaryOld;
    private boolean hasChildren = false;
    private int Iteration = 0;
    private int[][] TileMaterialint;
    int Boundary;

/*/
    public KdTreeChunks(ArrayList<Chunk> Chunks, Rect BoundaryOld, int Iteration) {

        this.ChunksInCurrentTree = Chunks;
        this.Iteration = Iteration;
        this.BoundaryOld = BoundaryOld;


        this.Boundary = (int) Math.ceil(TILESIZE * (camera.getEye2D().getValue(2) / ZOOMFACTOR));

        int X = this.BoundaryOld.width() + 2;
        int Y = this.BoundaryOld.height() + 2;

        this.TileMaterialint = new int[X][Y]; // must be bigger to give the Tiles on the edges the right Orientation

        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                this.TileMaterialint[i][j] = -1;

            }
        }

        if ((KDTREEMAXITEMS) < this.ChunksInCurrentTree.size() && Iteration < 20) {
            Split();
        }
        if (1 < this.ChunksInCurrentTree.size()) {
            CheckIntersections();
        }
    }

    public KdTreeChunks() {

    } // to create the Kd Tree at the start

    public void setChunksInCurrentTree(ArrayList<Chunk> ChunksInCurrentTree) {
        this.ChunksInCurrentTree = ChunksInCurrentTree;
    }

    public void addTilesInCurrentTree(ArrayList<Chunk> ChunksInCurrentTree) {
        try {
            this.ChunksInCurrentTree.addAll(ChunksInCurrentTree);
        }catch (Exception e){

        }
    }

    public void CreatenewKDTree() {
        try {

            int minX = 999, minY = 999, maxX = -999, maxY = -999;

            if (this.Iteration == 0) {

                ArrayList<Chunk> allChunks = getChunksInCurrentTree();
                this.ChunksInCurrentTree.clear();
                this.ChunksInCurrentTree.addAll(allChunks);
                this.Children.clear();
                this.hasChildren = false;

                for (int i = 0; i < this.ChunksInCurrentTree.size(); i++) {
                    if (this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(0) < minX) {
                        minX = (int) this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(0);
                    }
                    if (this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(1) < minY) {
                        minY = (int) this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(1);
                    }
                    if (this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(0) > maxX) {
                        maxX = (int) this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(0);
                    }
                    if (this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(1) > maxY) {
                        maxY = (int) this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(1);
                    }
                }
                minX -= 1;
                minY -= 1;
                maxX += 1;
                maxY += 1;
                this.BoundaryOld = new Rect(minX, minY, maxX, maxY);

                int X = this.BoundaryOld.width() + 2;
                int Y = this.BoundaryOld.height() + 2;
                if (X < 0) {
                    X = 0;
                }
                if (Y < 0) {
                    Y = 0;
                }
                this.TileMaterialint = new int[X][Y]; // must be bigger to give the Tiles on the edges the right Orientation
                for (int i = 0; i < X; i++) {
                    for (int j = 0; j < Y; j++) {
                        this.TileMaterialint[i][j] = -1;

                    }
                }

                this.Boundary = (int) Math.ceil(TILESIZE * (camera.getEye2D().getValue(2) / ZOOMFACTOR));
            }

            if (( KDTREEMAXITEMS) < this.ChunksInCurrentTree.size()) { // THE MAXIMUM IN A GIVEN CELL IS NOW KDTREEMMAXITEMS, SINCE THE TREE GETS DEVIDED INTO TWO CHILDREN HALF AS BIG
                Split();
            }
        }catch (Exception e){

        }
    }


    private void Split() {
        float XMIDDLE = 0;
        float YMIDDLE = 0;

        KdTreeChunks KdTree1;
        KdTreeChunks KdTree2;
        Rect BoundaryChildren1;
        Rect BoundaryChildren2;

        if (this.Iteration % 2 == 0) { // Starts with the Y split ----

            for (int i = 0; i < this.ChunksInCurrentTree.size(); i++) {
                YMIDDLE += this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(1);
            }

            YMIDDLE /= this.ChunksInCurrentTree.size();
            YMIDDLE = (int) YMIDDLE;


            for (int i = 0; i < this.ChunksInCurrentTree.size(); i++) {
                if (this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(1) + 0.5 > YMIDDLE) { // Ysplit
                    this.ChunkChildren1.add(this.ChunksInCurrentTree.get(i));
                } else {
                    this.ChunkChildren2.add(this.ChunksInCurrentTree.get(i));
                }
            }

            BoundaryChildren1 = new Rect(this.BoundaryOld.left, (int) YMIDDLE, this.BoundaryOld.right, this.BoundaryOld.bottom); // Bottom Rect
            BoundaryChildren2 = new Rect(this.BoundaryOld.left, this.BoundaryOld.top, this.BoundaryOld.right, (int) YMIDDLE); // Top Rect


            KdTree1 = new KdTreeChunks(this.ChunkChildren1, BoundaryChildren1, this.Iteration + 1); // Top new Tree
            KdTree2 = new KdTreeChunks(this.ChunkChildren2, BoundaryChildren2, this.Iteration + 1); // Bottom new Tree


        } else { // Continues with the X split |  |

            for (int i = 0; i < this.ChunksInCurrentTree.size(); i++) {
                XMIDDLE += this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(0);
            }
            XMIDDLE /= this.ChunksInCurrentTree.size();
            XMIDDLE = (int) XMIDDLE;


            for (int i = 0; i < this.ChunksInCurrentTree.size(); i++) {
                if (this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(0) + 0.5  < XMIDDLE) { // Xsplit
                    this.ChunkChildren1.add(this.ChunksInCurrentTree.get(i)); // Left from Xall
                } else {
                    this.ChunkChildren2.add(this.ChunksInCurrentTree.get(i)); // Right from Xall
                }
            }

            BoundaryChildren1 = new Rect(this.BoundaryOld.left, this.BoundaryOld.top, (int) XMIDDLE, this.BoundaryOld.bottom); // left Rect
            BoundaryChildren2 = new Rect((int) XMIDDLE, this.BoundaryOld.top, this.BoundaryOld.right, this.BoundaryOld.bottom); // right Rect

            KdTree1 = new KdTreeChunks(this.ChunkChildren1, BoundaryChildren1, this.Iteration + 1); // Left new Tree
            KdTree2 = new KdTreeChunks(this.ChunkChildren2, BoundaryChildren2, this.Iteration + 1); // Right new Tree
        }

        this.ChunksInCurrentTree.clear();
        this.hasChildren = true;
        this.Children.clear();
        this.Children.add(KdTree1);
        this.Children.add(KdTree2);
    }


    public ArrayList<Chunk> getChunksInCurrentTree() { // up to which Iteration you want the Boundary
        ArrayList<Chunk> returnChunksfromChildren = new ArrayList<>();
        if (this.ChunksInCurrentTree != null) {
            returnChunksfromChildren.addAll(this.ChunksInCurrentTree);
        }
        for (int i = 0; i < this.Children.size(); i++) {
            returnChunksfromChildren.addAll(this.Children.get(i).getChunksInCurrentTree());
        }
        return returnChunksfromChildren;
    }

    public ArrayList<Rect> getBoundaries() { // up to which Iteration you want the Boundary

        ArrayList<Rect> Boundary = new ArrayList<>();
        if (this.hasChildren) {
            // Checks own Visibility

            Vector a = new Vector(this.BoundaryOld.left, this.BoundaryOld.top).multiplydouble(TILESIZE);
            Vector b = new Vector(this.BoundaryOld.right, this.BoundaryOld.bottom).multiplydouble(TILESIZE);

            a = a.getScreencoordinatesFromTileCoordinates(a);
            b = b.getScreencoordinatesFromTileCoordinates(b);

            Rect CURRENTOnScreen = new Rect((int) a.getValue(0), (int) a.getValue(1), (int) b.getValue(0), (int) b.getValue(1));

            Rect Screenboundaries = new Rect(-this.Boundary, -this.Boundary, WIDTHSCREEN + this.Boundary, HEIGHTSCREEN + this.Boundary);


            if (Screenboundaries.contains(CURRENTOnScreen) && !Screenboundaries.intersect(CURRENTOnScreen) && this.Iteration == 0) {
                Boundary.add(this.BoundaryOld);
            } else {
                if (Screenboundaries.intersect(CURRENTOnScreen) || Screenboundaries.contains(CURRENTOnScreen)) {
                    for (int i = 0; i < this.Children.size(); i++) {
                        Boundary.addAll(this.Children.get(i).getBoundaries());
                    }
                }
            }
        } else {
            if (this.ChunksInCurrentTree != null && this.ChunksInCurrentTree.size() > 0) {
                Boundary.add(this.BoundaryOld);
            }
        }
        return Boundary;
    }

    //TODO make new sorting System to get the visible Tiles more efficently and faster!!!
    public ArrayList<Tile> getVisibleTilesInCurrentTree() { // Looks like it Works!
        ArrayList<Tile> returnTilesfromChildren = new ArrayList<>();

        if (this.hasChildren) {
            // Checks own Visibility
            Rect Screenboundaries = new Rect(0, 0, WIDTHSCREEN, HEIGHTSCREEN);


            Vector a = new Vector(this.BoundaryOld.left, this.BoundaryOld.top).multiplydouble(TILESIZE);
            Vector b = new Vector(this.BoundaryOld.right, this.BoundaryOld.bottom).multiplydouble(TILESIZE);

            a = a.getScreencoordinatesFromTileCoordinates(a);
            b = b.getScreencoordinatesFromTileCoordinates(b);

            Rect CURRENTOnScreen = new Rect((int) a.getValue(0), (int) a.getValue(1), (int) b.getValue(0), (int) b.getValue(1));

            if (Screenboundaries.contains(CURRENTOnScreen) && !Screenboundaries.intersect(CURRENTOnScreen)) {
                returnTilesfromChildren.addAll(getAllTilesBelow());
            } else {
                if (Screenboundaries.intersect(CURRENTOnScreen)) {
                    for (int i = 0; i < this.Children.size(); i++) {
                        returnTilesfromChildren.addAll(this.Children.get(i).getVisibleTilesInCurrentTree());
                    }
                }
            }
        } else {
            try {
                for (int i = 0; i < this.ChunksInCurrentTree.size();i++) {
                    if (this.ChunksInCurrentTree.get(i).isOnScreen()){
                        returnTilesfromChildren.add(this.ChunksInCurrentTree.get(i));
                    }
                }
            }catch (Exception e){

            }
        }
        return returnTilesfromChildren;

    }

    public ArrayList<Tile> getTilesInsideBoundary(Rect Boundary) { // Looks like it Works!
        ArrayList<Tile> returnTilesfromChildren = new ArrayList<>();

        for (int i = Boundary.left - 1; i < Boundary.left + 1 + Boundary.width(); i++) {
            for (int j = Boundary.top - 1; j < Boundary.top + 1 + Boundary.height(); j++) {
                returnTilesfromChildren.addAll(getTile(i, j));
            }
        }
        return returnTilesfromChildren;
    }

    //TODO implement getting / Setting Method for Tiles
    public ArrayList<Tile> getTile(int x, int y) { // X & Y as Tile Coordinates
        ArrayList<Chunk> Tiles = new ArrayList<>();
        if (this.ChunksInCurrentTree.size() < 1) {
            for (int i = 0; i < this.Children.size(); i++) {
                if (this.Children.get(i).getBoundarieOld().contains(x + 1, y + 1) || this.Children.get(i).getBoundarieOld().contains(x - 1, y - 1) || this.Children.get(i).getBoundarieOld().contains(x + 1, y - 1) || this.Children.get(i).getBoundarieOld().contains(x - 1, y + 1)) {
                    Tiles.addAll(this.Children.get(i).getTile(x, y));
                }
            }
        } else {
            for (int i = 0; i < this.ChunksInCurrentTree.size(); i++) {
                if (this.ChunksInCurrentTree.get(i) != null) {
                    Rect R = this.ChunksInCurrentTree.get(i).getBoundaries();
                    if (R.contains(x, y)) {
                        Tile add = this.ChunksInCurrentTree.get(i);
                        Tiles.add(add);
                    }
                }
            }
        }
        return Tiles;
    }

    public ArrayList<Tile> getAllTilesBelow() { // Works
        ArrayList<Tile> AllTilesBelow = new ArrayList<>();
        if (this.hasChildren) {
            for (int i = 0; i < this.Children.size(); i++) {
                AllTilesBelow.addAll(0, this.Children.get(i).getAllTilesBelow());
            }
        }
        return AllTilesBelow;
    }


    public void CheckIntersections() {
        Collections.reverse(this.ChunksInCurrentTree);
        for (int i = this.ChunksInCurrentTree.size() - 1; 1 < i; i--) {
            for (int j = i - 1; -1 < j; j--) {
                if (i != j) {
                    if (this.ChunksInCurrentTree.get(i).getBoundaries().contains(this.ChunksInCurrentTree.get(j).getBoundaries())) {
                        this.ChunksInCurrentTree.remove(j);
                        i--;
                    }
                }
            }
        }
        Collections.reverse(this.ChunksInCurrentTree);
    }


    //public void Autotile() {
    //    for (int i = 0; i < this.ChunksInCurrentTree.size(); i++) { // Adds the Material id to the List
    //        int X = (int) (1 +this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(0) - this.BoundaryOld.left);
    //        int Y = (int) (1 +this.ChunksInCurrentTree.get(i).getPositionRAW().getValue(1) - this.BoundaryOld.top);

    //        if (0 < Y && 0 < X && X < this.BoundaryOld.width()  + 2 && Y < this.BoundaryOld.height() + 2) {
    //            this.TileMaterialint[X][Y] = this.ChunksInCurrentTree.get(i).getMaterial();
    //        }
    //    }
    //    for (int i = 0; i < this.ChunksInCurrentTree.size(); i++) { // Changes the ID
    //        if (this.ChunksInCurrentTree.get(i).isAutotile()) {
    //            this.ChunksInCurrentTree.get(i).setID(getnewID(this.TileMaterialint, this.ChunksInCurrentTree.get(i)));
    //        }
    //    }
    //}

    public Rect getBoundarieOld() {
        return this.BoundaryOld;
    }

    public void removeTile(int x, int y) { // X & Y as Chunk Coordinates
        if (this.ChunksInCurrentTree.size() < 1) {
            for (int i = 0; i < this.Children.size(); i++) {
                if (this.Children.get(i).getBoundarieOld().contains(x + 1, y + 1) || this.Children.get(i).getBoundarieOld().contains(x - 1, y - 1) || this.Children.get(i).getBoundarieOld().contains(x + 1, y - 1) || this.Children.get(i).getBoundarieOld().contains(x - 1, y + 1)) {
                    this.Children.get(i).removeTile(x, y);
                }
            }
        } else {

            for (int i = 0; i < this.ChunksInCurrentTree.size(); i++) {
                Rect R = this.ChunksInCurrentTree.get(i).getBoundaries();
                if (R.contains(x, y)) {
                    this.ChunksInCurrentTree.remove(i);
                    i--;
                }
            }
        }
    }/**/
}
