package com.jacobjacob.ttproject.Tile;

import android.graphics.Rect;
import android.util.Log;

import com.jacobjacob.ttproject.Vector;

import java.util.ArrayList;
import java.util.Collections;

import static com.jacobjacob.ttproject.Util.*;
public class KdTree {

    private ArrayList<Tile> TilesInCurrentTree;
    private ArrayList<Tile> TileChildren1 = new ArrayList<>(); // Top / Right
    private ArrayList<Tile> TileChildren2 = new ArrayList<>(); // Bottom / Left
    private ArrayList<KdTree> Children = new ArrayList<>();
    private Rect BoundaryOld;
    private boolean hasChildren = false;
    private int Iteration = 0;
    private int[][] TileMaterialint;
    int Boundary;

    public KdTree(ArrayList<Tile> Tiles, Rect BoundaryOld, int Iteration) {

        this.TilesInCurrentTree = Tiles;
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

        if ((/* 2 * */KDTREEMAXITEMS) < this.TilesInCurrentTree.size()/**/ && Iteration < 20/* && Iteration != 0*/) {
            Split();
        }
        if (/*!this.hasChildren && */1 < this.TilesInCurrentTree.size()) {
            CheckIntersections();
            //Autotile();
        }
        //Autotile();
    }

    public KdTree() {

    } // to create the Kd Tree at the start

    public void setTilesInCurrentTree(ArrayList<Tile> tilesInCurrentTree) {
        this.TilesInCurrentTree = tilesInCurrentTree;
    }

    public void addTilesInCurrentTree(ArrayList<Tile> tilesInCurrentTree) {
        try {
            this.TilesInCurrentTree.addAll(tilesInCurrentTree);
        } catch (Exception e) {

        }
    }

    public void CreatenewKDTree() {
        try {

            //KDTREECURRENTLYBUILDING = true;

            //TilesInCurrentTree = getTilesInCurrentTree();

            int minX = 999, minY = 999, maxX = -999, maxY = -999;

            if (this.Iteration == 0) {

                ArrayList<Tile> allTiles = getTilesInCurrentTree();
                this.TilesInCurrentTree.clear();
                this.TilesInCurrentTree.addAll(allTiles);
                this.Children.clear();
                this.hasChildren = false;

                for (int i = 0; i < this.TilesInCurrentTree.size(); i++) {
                    if (this.TilesInCurrentTree.get(i).getPositionRAW().getValue(0) < minX) {
                        minX = (int) this.TilesInCurrentTree.get(i).getPositionRAW().getValue(0);
                    }
                    if (this.TilesInCurrentTree.get(i).getPositionRAW().getValue(1) < minY) {
                        minY = (int) this.TilesInCurrentTree.get(i).getPositionRAW().getValue(1);
                    }
                    if (this.TilesInCurrentTree.get(i).getPositionRAW().getValue(0) > maxX) {
                        maxX = (int) this.TilesInCurrentTree.get(i).getPositionRAW().getValue(0);
                    }
                    if (this.TilesInCurrentTree.get(i).getPositionRAW().getValue(1) > maxY) {
                        maxY = (int) this.TilesInCurrentTree.get(i).getPositionRAW().getValue(1);
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

                //Vector a = new Vector(this.BoundaryOld.left, this.BoundaryOld.top).multiplydouble(TILESIZE);
                //Vector b = new Vector(this.BoundaryOld.right, this.BoundaryOld.bottom).multiplydouble(TILESIZE);

                //a = a.getScreencoordinatesFromTileCoordinates(a);
                //b = b.getScreencoordinatesFromTileCoordinates(b);

                //this.CURRENTOnScreen = new Rect((int) a.getValue(0), (int) a.getValue(1), (int) b.getValue(0), (int) b.getValue(1));

                this.Boundary = (int) Math.ceil(TILESIZE * (camera.getEye2D().getValue(2) / ZOOMFACTOR));

                Autotile();
            }

            if (( /* 2 * */KDTREEMAXITEMS) < this.TilesInCurrentTree.size()) { // THE MAXIMUM IN A GIVEN CELL IS NOW KDTREEMMAXITEMS, SINCE THE TREE GETS DEVIDED INTO TWO CHILDREN HALF AS BIG
                Split();
            }
            //KDTREECURRENTLYBUILDING = false;
        } catch (Exception e) {

        }
    }


    private void Split() {
        float XMIDDLE = 0;
        float YMIDDLE = 0;

        KdTree KdTree1;
        KdTree KdTree2;
        Rect BoundaryChildren1;
        Rect BoundaryChildren2;

        if (this.Iteration % 2 == 0) { // Starts with the Y split ----

            for (int i = 0; i < this.TilesInCurrentTree.size(); i++) {
                YMIDDLE += this.TilesInCurrentTree.get(i).getPositionRAW().getValue(1);
            }

            YMIDDLE /= this.TilesInCurrentTree.size();
            YMIDDLE = (int) YMIDDLE;


            for (int i = 0; i < this.TilesInCurrentTree.size(); i++) {
                if (this.TilesInCurrentTree.get(i).getPositionRAW().getValue(1)/**/ + 0.5/**/ > YMIDDLE) { // Ysplit
                    this.TileChildren1.add(this.TilesInCurrentTree.get(i));
                } else {
                    this.TileChildren2.add(this.TilesInCurrentTree.get(i));
                }
            }

            BoundaryChildren1 = new Rect(this.BoundaryOld.left, (int) YMIDDLE, this.BoundaryOld.right, this.BoundaryOld.bottom); // Bottom Rect
            BoundaryChildren2 = new Rect(this.BoundaryOld.left, this.BoundaryOld.top, this.BoundaryOld.right, (int) YMIDDLE); // Top Rect


            KdTree1 = new KdTree(this.TileChildren1, BoundaryChildren1, this.Iteration + 1); // Top new Tree
            KdTree2 = new KdTree(this.TileChildren2, BoundaryChildren2, this.Iteration + 1); // Bottom new Tree


        } else { // Continues with the X split |  |

            for (int i = 0; i < this.TilesInCurrentTree.size(); i++) {
                XMIDDLE += this.TilesInCurrentTree.get(i).getPositionRAW().getValue(0);
            }
            XMIDDLE /= this.TilesInCurrentTree.size();
            XMIDDLE = (int) XMIDDLE;


            for (int i = 0; i < this.TilesInCurrentTree.size(); i++) {
                if (this.TilesInCurrentTree.get(i).getPositionRAW().getValue(0)/**/ + 0.5 /**/ < XMIDDLE) { // Xsplit
                    this.TileChildren1.add(this.TilesInCurrentTree.get(i)); // Left from Xall
                } else {
                    this.TileChildren2.add(this.TilesInCurrentTree.get(i)); // Right from Xall
                }
            }

            BoundaryChildren1 = new Rect(this.BoundaryOld.left, this.BoundaryOld.top, (int) XMIDDLE, this.BoundaryOld.bottom); // left Rect
            BoundaryChildren2 = new Rect((int) XMIDDLE, this.BoundaryOld.top, this.BoundaryOld.right, this.BoundaryOld.bottom); // right Rect

            KdTree1 = new KdTree(this.TileChildren1, BoundaryChildren1, this.Iteration + 1); // Left new Tree
            KdTree2 = new KdTree(this.TileChildren2, BoundaryChildren2, this.Iteration + 1); // Right new Tree
        }

        this.TilesInCurrentTree.clear();
        this.hasChildren = true;
        this.Children.clear();
        this.Children.add(KdTree1);
        this.Children.add(KdTree2);
    }


    public ArrayList<Tile> getTilesInCurrentTree() { // up to which Iteration you want the Boundary
        ArrayList<Tile> returnTilesfromChildren = new ArrayList<>();
        if (this.TilesInCurrentTree != null) {
            returnTilesfromChildren.addAll(this.TilesInCurrentTree);
        }
        for (int i = 0; i < this.Children.size(); i++) {
            returnTilesfromChildren.addAll(this.Children.get(i).getTilesInCurrentTree());
        }
        return returnTilesfromChildren;
    }

    public ArrayList<Rect> getBoundaries(/*int Iteration*/) { // up to which Iteration you want the Boundary

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
            if (this.TilesInCurrentTree != null && this.TilesInCurrentTree.size() > 0) {
                Boundary.add(this.BoundaryOld);//Boundary.addAll(/*this.TilesInCurrentTree*/);
            }
        }
        return Boundary;
    }

    //TODO make new sorting System to get the visible Tiles more efficently and faster!!!
    public ArrayList<Tile> getVisibleTilesInCurrentTree() { // Looks like it Works!
        ArrayList<Tile> returnTilesfromChildren = new ArrayList<>();

        if (this.hasChildren) {
            // Checks own Visibility

            Vector a = new Vector(this.BoundaryOld.left, this.BoundaryOld.top).multiplydouble(TILESIZE);
            Vector b = new Vector(this.BoundaryOld.right, this.BoundaryOld.bottom).multiplydouble(TILESIZE);

            a = a.getScreencoordinatesFromTileCoordinates(a);
            b = b.getScreencoordinatesFromTileCoordinates(b);

            int boundaryextra = (int) b.subtract(a).getValue(0);

            Rect Screenboundaries = new Rect(-boundaryextra * 5, -boundaryextra * 5, WIDTHSCREEN + boundaryextra * 5, HEIGHTSCREEN + boundaryextra * 5);


            Rect CURRENTOnScreen = new Rect((int) a.getValue(0), (int) a.getValue(1), (int) b.getValue(0), (int) b.getValue(1));

            if (Screenboundaries.contains(CURRENTOnScreen) && !Screenboundaries.intersect(CURRENTOnScreen) /*&& this.Iteration == 0 && a.getValue(0) < -Boundary*/) {
                returnTilesfromChildren.addAll(getAllTilesBelow());
            } else {
                if (Screenboundaries.intersect(CURRENTOnScreen)) {
                    for (int i = 0; i < this.Children.size(); i++) {
                        try {
                            returnTilesfromChildren.addAll(this.Children.get(i).getVisibleTilesInCurrentTree());
                        } catch (Exception e) {
                            Log.d("KDTREE", "CHILD = null" + e);
                        }
                    }
                }
            }
        } else {
            try {
                for (int i = 0; i < this.TilesInCurrentTree.size(); i++) {
                    if (this.TilesInCurrentTree.get(i).isOnScreen()) {
                        returnTilesfromChildren.add(this.TilesInCurrentTree.get(i));
                    }
                }
                //returnTilesfromChildren.addAll(this.TilesInCurrentTree);
            } catch (Exception e) {

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

    public ArrayList<Tile> getTile(int x, int y) { // X & Y as Tile Coordinates
        ArrayList<Tile> Tiles = new ArrayList<>();
        if (this.TilesInCurrentTree.size() < 1) {
            for (int i = 0; i < this.Children.size(); i++) {
                if (this.Children.get(i).getBoundarieOld().contains(x + 1, y + 1) || this.Children.get(i).getBoundarieOld().contains(x - 1, y - 1) || this.Children.get(i).getBoundarieOld().contains(x + 1, y - 1) || this.Children.get(i).getBoundarieOld().contains(x - 1, y + 1)) {
                    Tiles.addAll(this.Children.get(i).getTile(x, y));
                }
            }
        } else {
            for (int i = 0; i < this.TilesInCurrentTree.size(); i++) {
                if (this.TilesInCurrentTree.get(i) != null) {
                    Rect R = this.TilesInCurrentTree.get(i).getBoundaries();
                    if (R.contains(x, y)) {
                        Tile add = this.TilesInCurrentTree.get(i);
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
        Collections.reverse(this.TilesInCurrentTree);
        for (int i = this.TilesInCurrentTree.size() - 1; 1 < i; i--) {
            for (int j = i - 1; -1 < j; j--) {
                if (i != j) {
                    if (this.TilesInCurrentTree.get(i).getBoundaries().contains(this.TilesInCurrentTree.get(j).getBoundaries())) {
                        this.TilesInCurrentTree.remove(j);
                        i--;
                    }
                }
            }
        }
        Collections.reverse(this.TilesInCurrentTree);
    }


    public void Autotile() {
        for (int i = 0; i < this.TilesInCurrentTree.size(); i++) { // Adds the Material id to the List
            int X = (int) (/**/1 +/**/this.TilesInCurrentTree.get(i).getPositionRAW().getValue(0) - this.BoundaryOld.left);
            int Y = (int) (/**/1 +/**/this.TilesInCurrentTree.get(i).getPositionRAW().getValue(1) - this.BoundaryOld.top);

            if (0 < Y && 0 < X && X < this.BoundaryOld.width() /**/ + 2/**/ && Y < this.BoundaryOld.height()/**/ + 2/**/) {
                this.TileMaterialint[X][Y] = this.TilesInCurrentTree.get(i).getMaterial();
            }
        }
        for (int i = 0; i < this.TilesInCurrentTree.size(); i++) { // Changes the ID
            if (this.TilesInCurrentTree.get(i).isAutotile()/* && this.BoundaryOld.contains(this.TilesInCurrentTree.get(i).getBoundaries())*/) {
                this.TilesInCurrentTree.get(i).setID(getnewID(this.TileMaterialint, this.TilesInCurrentTree.get(i)));
                //this.TilesInCurrentTree.get(i).setMaterial();
            }
        }
    }

    public Rect getBoundarieOld() {
        return this.BoundaryOld;
    }

    public void removeTile(int x, int y) { // X & Y as Tile Coordinates
        //KDTREECURRENTLYBUILDING = true;
        if (this.TilesInCurrentTree.size() < 1) {
            for (int i = 0; i < this.Children.size(); i++) {
                if (this.Children.get(i).getBoundarieOld().contains(x + 1, y + 1) || this.Children.get(i).getBoundarieOld().contains(x - 1, y - 1) || this.Children.get(i).getBoundarieOld().contains(x + 1, y - 1) || this.Children.get(i).getBoundarieOld().contains(x - 1, y + 1)) {
                    this.Children.get(i).removeTile(x, y);
                }
            }
        } else {

            for (int i = 0; i < this.TilesInCurrentTree.size(); i++) {
                Rect R = this.TilesInCurrentTree.get(i).getBoundaries();
                //int X = (int) this.TilesInCurrentTree.get(i-1).getPositionRAW().getValue(0);
                //int Y = (int) this.TilesInCurrentTree.get(i-1).getPositionRAW().getValue(1);
                //R = new Rect(X, Y, X + 1, Y + 1);
                if (R.contains(x, y)) {
                    this.TilesInCurrentTree.remove(i);
                    i--;
                    //return;
                }
            }
        }
        //KDTREECURRENTLYBUILDING = false;
    }

    // if contained in screen && !intersects screen then pass all

    public int getnewID(int TilesinKdTree[][], Tile T) { // x and y = boundary left + top

        int X = (int) (T.getPositionRAW().getValue(0) - this.BoundaryOld.left) + 1; // XPosition in [][]
        int Y = (int) (T.getPositionRAW().getValue(1) - this.BoundaryOld.top) + 1; // YPosition in [][]
        if (X < 1) {
            X = 1;
        }
        if (Y < 1) {
            Y = 1;
        }

        int left = TilesinKdTree[X - 1][Y]; // left Tile Material
        int top = TilesinKdTree[X][Y - 1];
        int right = TilesinKdTree[X + 1][Y];
        int bottom = TilesinKdTree[X][Y + 1];

        int Material = T.getMaterial();
        /**/
        if (top == Material) {
            top = 1;
        } else {
            top = 0;
        }
        if (right == Material) {
            right = 1;
        } else {
            right = 0;
        }
        if (bottom == Material) {
            bottom = 1;
        } else {
            bottom = 0;
        }

        if (left == Material) {
            left = 1;
        } else {
            left = 0;
        }
        // now either 1 or 0
        return left * 8 + bottom * 4 + right * 2 + top;
    }
}
