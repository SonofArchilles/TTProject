package com.jacobjacob.ttproject;

import android.graphics.Rect;

import java.util.ArrayList;

import static com.jacobjacob.ttproject.Util.CHUNKCOLOR;
import static com.jacobjacob.ttproject.Util.FILLTILECOLOR;
import static com.jacobjacob.ttproject.Util.FRAMES;
import static com.jacobjacob.ttproject.Util.HEIGHTSCREEN;
import static com.jacobjacob.ttproject.Util.INVENTORY;
import static com.jacobjacob.ttproject.Util.INVENTORYDISPLAYSIZE;
import static com.jacobjacob.ttproject.Util.INVENTORYSELECTTILECOLOR;
import static com.jacobjacob.ttproject.Util.INVENTORYSIZE;
import static com.jacobjacob.ttproject.Util.INVENTORYVERTICAL;
import static com.jacobjacob.ttproject.Util.KDTREE;
import static com.jacobjacob.ttproject.Util.KDTREECOPYING;
import static com.jacobjacob.ttproject.Util.MATERIALARRAY;
import static com.jacobjacob.ttproject.Util.MAXFILLTILES;
import static com.jacobjacob.ttproject.Util.RF;
import static com.jacobjacob.ttproject.Util.SELECTEDIDINVENTORY;
import static com.jacobjacob.ttproject.Util.STARTINGMATERIAL;
import static com.jacobjacob.ttproject.Util.TEXTUREWIDTH;
import static com.jacobjacob.ttproject.Util.TILESIZE;
import static com.jacobjacob.ttproject.Util.UPDATEVIEW;
import static com.jacobjacob.ttproject.Util.WF;
import static com.jacobjacob.ttproject.Util.WIDTHSCREEN;

//import static com.jacobjacob.ttproject.Util.KDTREECOPY;

public class LevelEditor {

    public static int SelectedMaterial;
    public Vector Startposition = new Vector();

    public Vector TILEStartposition = new Vector();
    public Vector TILEEndposition = new Vector();

    public LevelEditor() {


    }

    public void Inventory() { // big inventory
        for (int i = 0; i < TEXTUREWIDTH; i++) {
            for (int j = 0; j < TEXTUREWIDTH; j++) {
                //Tile ADDTOINVENTORY = new Tile(new Vector(j, i), new Vector(j, i),FRAMES);
                //ADDTOINVENTORY.setStarttime((int)System.currentTimeMillis());
                INVENTORY.add(new Tile(new Vector(j, i), i + j * TEXTUREWIDTH, FRAMES));
            }
        }
    }

    public void setSelectTilefromInventory(float x, float y) {

        x -= (WIDTHSCREEN - HEIGHTSCREEN) / 2;
        x /= HEIGHTSCREEN;

        if (0 <= x && x <= 1) {
            y /= HEIGHTSCREEN;
            y *= TEXTUREWIDTH;

            x *= TEXTUREWIDTH;

            SelectedMaterial = ((int) x + ((int) y * TEXTUREWIDTH));
        }
    }


    public static void SELECTEDTILEIDINVENTORY() {
        if (!SELECTEDIDINVENTORY.contains(SelectedMaterial)) {
            SELECTEDIDINVENTORY.add(SelectedMaterial);
        }
        if (SELECTEDIDINVENTORY.size() > INVENTORYSIZE) {
            SELECTEDIDINVENTORY.remove(0);
        }
    }


    public boolean SelectTileFromSmallInventory(int x, int y) {

        int Height7Texturewidth = HEIGHTSCREEN / TEXTUREWIDTH;

        int HeTeIn = (int) (Height7Texturewidth * INVENTORYDISPLAYSIZE);


        Rect dst;
        if (INVENTORYVERTICAL) {
            dst = new Rect(Height7Texturewidth, Height7Texturewidth, HeTeIn, HeTeIn * INVENTORYSIZE);  //vertical

        } else {
            dst = new Rect(Height7Texturewidth, Height7Texturewidth, HeTeIn * INVENTORYSIZE, HeTeIn);   // horizontal
        }

        if (dst.contains(x, y)) {
            if (SELECTEDIDINVENTORY.size() > 0) {
                for (int i = SELECTEDIDINVENTORY.size(); i > 0; i--) {

                    if (INVENTORYVERTICAL) {
                        dst = new Rect(Height7Texturewidth, i * HeTeIn + Height7Texturewidth, HeTeIn, HeTeIn * (i + 1));
                    } else {
                        dst = new Rect(Height7Texturewidth + HeTeIn * i, Height7Texturewidth, HeTeIn * (i + 1), HeTeIn);
                    }
                    if (SELECTEDIDINVENTORY.size() > i) {
                        if (dst.contains(x, y) && SELECTEDIDINVENTORY.get(SELECTEDIDINVENTORY.size() - i - 1) != null) {
                            SelectedMaterial = SELECTEDIDINVENTORY.get(SELECTEDIDINVENTORY.size() - i - 1); // selected ID

                            int Materialone = SELECTEDIDINVENTORY.get(SELECTEDIDINVENTORY.size() - 1);
                            int Materialtwoposition = SELECTEDIDINVENTORY.size() - i - 1;

                            SELECTEDIDINVENTORY.set(SELECTEDIDINVENTORY.size() - 1, SelectedMaterial);
                            SELECTEDIDINVENTORY.set(Materialtwoposition, Materialone); // swaps the Materials inside the Array

                        }
                    }
                }
            }
            return true;
        } else return false;
    }


    public void PlaceSelectedTile(int x, int y) {
        if (!KDTREECOPYING) {
            if (!SelectTileFromSmallInventory(x, y)) { // Checks, if a Tile to place is selected, if the inventory is empty, you cannot place a Tile

                ArrayList<Tile> Tilestoadd = new ArrayList<>();
                Vector Tileposition = new Vector().getTileCoordinatesfromScreencoordinates(x, y);

                Tile newTile = new Tile(new Vector(Tileposition.getValue(0), Tileposition.getValue(1)), SelectedMaterial, FRAMES);
                int Addtime = (int) System.currentTimeMillis();
                if (SELECTEDIDINVENTORY.size() >= 1) {
                    //newTile.setMaterial(getMaterial());
                    newTile.setStarttime(Addtime);
                }
                Tilestoadd.add(newTile);
                AddTilesKDTREE(Tilestoadd);
            }
        }
    }


    public void StartFillingTile(int x, int y) { // Saves The Startposition to later fill the rectangle
        Startposition = new Vector().getClippedCoordinates(new Vector(x, y));
        TILEStartposition = new Vector().getTileCoordinatesfromScreencoordinates(x, y);
    }

    private int getMaterial() {
        if (SELECTEDIDINVENTORY.size() > 0) {
            return (int) (SELECTEDIDINVENTORY.get(SELECTEDIDINVENTORY.size() - 1)/*+1*/) / TEXTUREWIDTH;
        } else return 0;
    }


    public Rect GetFillBoundaries(int x, int y) { // Draws the green Box that shows what is Selected

        Vector Startpositionnew = TILEStartposition.getScreencoordinatesFromTileCoordinates(TILEStartposition.multiplydouble(TILESIZE));
        Vector Endpositionnew = TILEStartposition.getScreencoordinatesFromTileCoordinates((new Vector().getTileCoordinatesfromScreencoordinates(x, y)).multiplydouble(TILESIZE));

        x = (int) Endpositionnew.getValue(0);
        y = (int) Endpositionnew.getValue(1);

        int xStart = (int) Startpositionnew.getValue(0);
        int yStart = (int) Startpositionnew.getValue(1);


        /***/ // CHANGES THE COLOR ACCORDING TO THE SIZE OF THE BOX

        TILEEndposition = new Vector().getTileCoordinatesfromScreencoordinates(x, y);
        int xmin = Math.min((int) TILEStartposition.getValue(0), (int) TILEEndposition.getValue(0));
        int ymin = Math.min((int) TILEStartposition.getValue(1), (int) TILEEndposition.getValue(1));

        int xmax = Math.max((int) TILEStartposition.getValue(0), (int) TILEEndposition.getValue(0));
        int ymax = Math.max((int) TILEStartposition.getValue(1), (int) TILEEndposition.getValue(1));

        //if (!DESTROYTILES) {
        FILLTILECOLOR = INVENTORYSELECTTILECOLOR;
        //}else {
        //FILLTILECOLOR = CHUNKCOLOR;
        //}
        int AREA = (xmax - xmin) * (ymax - ymin);

        if (AREA > MAXFILLTILES) {
            //if (!DESTROYTILES) {
            FILLTILECOLOR = CHUNKCOLOR;
            //}else {
            //FILLTILECOLOR = INVENTORYSELECTTILECOLOR;
            //}
        }
        /***/


        return new Rect(xStart, yStart, x, y);
    }


    public void FillingTiles(int x, int y) {
        if (!SelectTileFromSmallInventory(x, y)) {

            ArrayList<Tile> Tilestoadd = new ArrayList<>();

            TILEEndposition = new Vector().getTileCoordinatesfromScreencoordinates(x, y);

            int xmin = Math.min((int) TILEStartposition.getValue(0), (int) TILEEndposition.getValue(0));
            int ymin = Math.min((int) TILEStartposition.getValue(1), (int) TILEEndposition.getValue(1));

            int xmax = Math.max((int) TILEStartposition.getValue(0), (int) TILEEndposition.getValue(0));
            int ymax = Math.max((int) TILEStartposition.getValue(1), (int) TILEEndposition.getValue(1));

            //FILLTILECOLOR = CHUNKCOLOR;
            if ((xmax - xmin) * (ymax - ymin) < MAXFILLTILES) { // AREA smaller than the allowed max
                //FILLTILECOLOR = INVENTORYSELECTTILECOLOR;

                //int Material = getMaterial();
                int Addtime = (int) System.currentTimeMillis();

                for (int i = xmin; i < xmax; i++) {
                    for (int j = ymin; j < ymax; j++) {
                        Tile newTile = new Tile(new Vector(i, j), SelectedMaterial, FRAMES);
                        //newTile.setMaterial(Material);
                        newTile.setStarttime(Addtime);
                        Tilestoadd.add(newTile);
                    }
                }
            }
            AddTilesKDTREE(Tilestoadd);
        }
    }

    public void AddTilesKDTREE(ArrayList<Tile> TILESTOADD) { // ADDS A LIST WITH ONE OR MORE TILES AT ONCE, IF THEY WERE ADDED ONE BY ONE IT WOULD LAG
        KDTREE.addTilesInCurrentTree(TILESTOADD);
        KDTREE.CreatenewKDTree();

        //KDTREECOPY = new KdTree();
        //KDTREECOPY = KDTREE;
    }

    public void RemoveTileKDTREE(int x, int y) {
        if (!KDTREECOPYING) {
            Vector XY = new Vector().getTileCoordinatesfromScreencoordinates(x, y);
            KDTREE.removeTile((int) XY.getValue(0), (int) XY.getValue(1));
            KDTREE.CreatenewKDTree();
        }
    }

    public void RemoveTilesKDTREE(int x, int y) {
        if (!SelectTileFromSmallInventory(x, y)) {

            TILEEndposition = new Vector().getTileCoordinatesfromScreencoordinates(x, y);

            int xmin = Math.min((int) TILEStartposition.getValue(0), (int) TILEEndposition.getValue(0));
            int ymin = Math.min((int) TILEStartposition.getValue(1), (int) TILEEndposition.getValue(1));

            int xmax = Math.max((int) TILEStartposition.getValue(0), (int) TILEEndposition.getValue(0));
            int ymax = Math.max((int) TILEStartposition.getValue(1), (int) TILEEndposition.getValue(1));

            if ((xmax - xmin) * (ymax - ymin) < MAXFILLTILES) { // AREA smaller than the allowed max
                //FILLTILECOLOR = INVENTORYSELECTTILECOLOR;
                for (int i = xmin; i < xmax; i++) {
                    for (int j = ymin; j < ymax; j++) {
                        KDTREE.removeTile(i, j);
                    }
                }
                KDTREE.CreatenewKDTree();
            }
            //AddTilesKDTREE(Tilestoadd);
        }

        //KDTREECOPY = new KdTree();
        //KDTREECOPY = KDTREE;
    }

    public void SaveLevel() {
        UPDATEVIEW = false;
        StringBuilder Tiles = new StringBuilder();


        ArrayList<Tile> AllTiles = KDTREE.getTilesInCurrentTree();

        for (int i = 0; i < AllTiles.size(); i++) {
            Tile thisTile = AllTiles.get(i);
            String currentTile = (int) thisTile.getPositionRAW().getValue(0) + " " + (int) thisTile.getPositionRAW().getValue(1) + " " + (int) thisTile.getIDint() + " " + (int) thisTile.getFrames() + " " + (int) thisTile.getMaterial() + " " + (int) thisTile.getStarttime();
            Tiles.append(currentTile + "\n");
        }

        Tiles.append("m\n");
        for (int i = 0; i < MATERIALARRAY.length; i++) {
            if (MATERIALARRAY[i] != null) {
                Tiles.append(MATERIALARRAY[i].getMaterialdetails() + "\n");
            }else {
                Tiles.append(STARTINGMATERIAL.getMaterialdetails() + "\n");
            }
        }

        Tiles.append("a\n");
        for (int i = 0; i < MATERIALARRAY.length; i++) {
            if (MATERIALARRAY[i] != null && MATERIALARRAY[i].hasAnimation()) {
                Tiles.append(MATERIALARRAY[i].getAnimationdetails() + "\n");
            }
        }

        String TilestoSave = String.valueOf(Tiles);
        //int a = 4;
        WF.WriteFile(TilestoSave);
    }

    public void LoadLevel() {
        RF.ReadFileTiles();
    }
}
