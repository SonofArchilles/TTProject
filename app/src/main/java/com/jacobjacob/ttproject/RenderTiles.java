package com.jacobjacob.ttproject;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;

import com.jacobjacob.ttproject.Tile.KdTree;
import com.jacobjacob.ttproject.Tile.Tile;

import java.util.ArrayList;

import static com.jacobjacob.ttproject.LevelEditor.SelectedMaterial;
import static com.jacobjacob.ttproject.MainActivity.IMAGE;
import static com.jacobjacob.ttproject.Util.BACKGROUNDCOLOR;
import static com.jacobjacob.ttproject.Util.CHUNKCOLOR;
import static com.jacobjacob.ttproject.Util.DISPLAYINVENTORY;
import static com.jacobjacob.ttproject.Util.DRAWFILLTILERECT;
import static com.jacobjacob.ttproject.Util.FILLTILECOLOR;
import static com.jacobjacob.ttproject.Util.FILLTILERECT;
import static com.jacobjacob.ttproject.Util.FILLTILES;
import static com.jacobjacob.ttproject.Util.FRAMETIME;
import static com.jacobjacob.ttproject.Util.FRAMETIMESTART;
import static com.jacobjacob.ttproject.Util.GENERATE;
import static com.jacobjacob.ttproject.Util.HEIGHTSCREEN;
import static com.jacobjacob.ttproject.Util.HITBOX;
import static com.jacobjacob.ttproject.Util.INVENTORY;
import static com.jacobjacob.ttproject.Util.INVENTORYBACKGROUNDCOLOR;
import static com.jacobjacob.ttproject.Util.INVENTORYDISPLAYSIZE;
import static com.jacobjacob.ttproject.Util.INVENTORYSELECTTILECOLOR;
import static com.jacobjacob.ttproject.Util.INVENTORYSIZE;
import static com.jacobjacob.ttproject.Util.INVENTORYVERTICAL;
import static com.jacobjacob.ttproject.Util.KDTREE;
import static com.jacobjacob.ttproject.Util.KDTREECOPY;
import static com.jacobjacob.ttproject.Util.KDTREECOPYING;
import static com.jacobjacob.ttproject.Util.KDTREECURRENTLYBUILDING;
import static com.jacobjacob.ttproject.Util.MATERIALARRAY;
import static com.jacobjacob.ttproject.Util.PORTALLIST;
import static com.jacobjacob.ttproject.Util.SELECTEDIDINVENTORY;
import static com.jacobjacob.ttproject.Util.TEXTUREWIDTH;
import static com.jacobjacob.ttproject.Util.TILELAYER;
import static com.jacobjacob.ttproject.Util.TILELAYERSTART;
import static com.jacobjacob.ttproject.Util.TILESIZE;
import static com.jacobjacob.ttproject.Util.TILESIZETEXTURE;
import static com.jacobjacob.ttproject.Util.TILETEXTURE;
import static com.jacobjacob.ttproject.Util.WIDTHSCREEN;
import static com.jacobjacob.ttproject.Util.ZOOMFACTOR;
import static com.jacobjacob.ttproject.Util.camera;

public class RenderTiles {

    static Bitmap bmp;
    static Canvas canvas;
    Paint paint = new Paint(BACKGROUNDCOLOR);
    Rect Screenboundaries;
    float TILESIZEzoom = (float) Math.ceil(TILESIZE * (camera.getEye2D().getValue(2) / ZOOMFACTOR));

    public void initializeRenderTiles() {
        if (!KDTREECURRENTLYBUILDING) {
            KDTREECOPYING = true;
            KDTREECOPY = new KdTree();
            KDTREECOPY = KDTREE;
            KDTREECOPYING = false;
        }

        this.bmp = Bitmap.createBitmap(WIDTHSCREEN, HEIGHTSCREEN, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bmp);
        //canvas.setBitmap(bmp);
        this.TILESIZEzoom = (float) Math.ceil(TILESIZE * (camera.getEye2D().getValue(2) / ZOOMFACTOR));
        this.Screenboundaries = new Rect((int) (-TILESIZEzoom), (int) (-TILESIZEzoom), (int) (WIDTHSCREEN + TILESIZEzoom), (int) (HEIGHTSCREEN + TILESIZEzoom));
        //this.KDTREECOPY = KDTREE;
        this.paint.setColor(BACKGROUNDCOLOR);
        this.canvas.drawRect(0, 0, WIDTHSCREEN, HEIGHTSCREEN, paint);
    }


    public void DrawSelectedTile() { // Draws the TILES on the left or top, like a quick inventory
        this.paint.setColor(INVENTORYBACKGROUNDCOLOR);

        int Height7Texturewidth = HEIGHTSCREEN / TEXTUREWIDTH;
        int HeTeIn = (int) (Height7Texturewidth * INVENTORYDISPLAYSIZE);

        Rect dst;
        if (INVENTORYVERTICAL) {
            dst = new Rect(Height7Texturewidth, Height7Texturewidth, HeTeIn, HeTeIn * INVENTORYSIZE); // destination
        } else {
            dst = new Rect(Height7Texturewidth, Height7Texturewidth, HeTeIn * INVENTORYSIZE, HeTeIn); // destination
        }
        this.canvas.drawRect(dst, this.paint);


        Rect src = new Rect(0, 0, TILESIZE, TILESIZE);

        for (int i = 0; i < SELECTEDIDINVENTORY.size(); i++) {

            if (INVENTORYVERTICAL) {
                dst = new Rect(Height7Texturewidth, i * HeTeIn + Height7Texturewidth, HeTeIn, HeTeIn * (i + 1)); // destination
            } else {
                dst = new Rect(i * HeTeIn + Height7Texturewidth, Height7Texturewidth, HeTeIn * (i + 1), HeTeIn);
            }

            boolean Display = true;
            if ((DISPLAYINVENTORY && i == 0)) {
                Display = false;
            }

            if (SelectedMaterial <= INVENTORY.size() && Display) {
                try {
                    int MaterialTile = SELECTEDIDINVENTORY.get(SELECTEDIDINVENTORY.size() - i - 1); // MaterialTile

                    this.canvas.drawBitmap(TILETEXTURE.getBitmap(15, MaterialTile), src, dst, null);
                } catch (Exception e) {

                }
            }
        }
        dst = new Rect((Height7Texturewidth), (Height7Texturewidth), (int) ((Height7Texturewidth) * INVENTORYDISPLAYSIZE), (int) (Height7Texturewidth * INVENTORYDISPLAYSIZE)); // destination

        if (DISPLAYINVENTORY) {
            try {
                this.canvas.drawBitmap(TILETEXTURE.getBitmap(15, SelectedMaterial), src, dst, null);

            } catch (Exception e) {

            }
        }


        /****/
/*
        if (INVENTORYVERTICAL) {
            dst = new Rect(2 * Height7Texturewidth + HeTeIn, Height7Texturewidth, HeTeIn, Height7Texturewidth + HeTeIn);  //vertical
            paint.setColor(INVENTORYBACKGROUNDCOLOR);
        } else {
            dst = new Rect(Height7Texturewidth, 2 * Height7Texturewidth + HeTeIn, Height7Texturewidth + HeTeIn, HeTeIn);   // horizontal
            paint.setColor(CHUNKCOLOR);
        }

        canvas.drawRect(dst, paint);/**/
/****/


        //paint.setColor(INVENTORYSELECTTILECOLOR);
        //canvas.drawRect( dst, paint);
    }

    public void DrawSelectedTileLayer() { // Draws the TILES on the left or top, like a quick inventory
        if (TILELAYER > 0 && TILELAYER < TILELAYERSTART + 4) {

            this.paint.setColor(INVENTORYBACKGROUNDCOLOR);

            int Height7Texturewidth = HEIGHTSCREEN / TEXTUREWIDTH;
            int HeTeIn = (int) (Height7Texturewidth * INVENTORYDISPLAYSIZE);

            Rect dst;
            Rect src = new Rect(0, 0, TILESIZE, TILESIZE);
            int idselectedmaterial = MATERIALARRAY[SelectedMaterial].getLayer(TILELAYER/*/+ TILELAYERSTART/**/);
            int ColorSelectedmaterial = MATERIALARRAY[SelectedMaterial].getColor(TILELAYER/*/+ TILELAYERSTART/**/);


            dst = new Rect((2) * HeTeIn + Height7Texturewidth, HeTeIn + Height7Texturewidth, HeTeIn * ((2) + 1), HeTeIn * (1 + 1));
            Paint tempPaint1 = new Paint();
            tempPaint1.setColor(Color.rgb(255, 255, 255));
            canvas.drawRect(dst, tempPaint1);

            if (TILELAYERSTART < TILELAYER) {
                for (int i = 0; i < 5; i++) {

                    dst = new Rect((i) * HeTeIn + Height7Texturewidth, HeTeIn + Height7Texturewidth, HeTeIn * ((i) + 1), HeTeIn * (1 + 1));

                    if (SelectedMaterial <= INVENTORY.size()/* && Display*/) {

                        Paint temppaint = new Paint();
                        temppaint.setColor(ColorSelectedmaterial);

                        Bitmap Tilebmp = TILETEXTURE.getBitmap(idselectedmaterial + i - 2);


                        if (Tilebmp != null) {
                            ColorFilter filter = new LightingColorFilter(ColorSelectedmaterial, 0);
                            temppaint.setColorFilter(filter);
                            temppaint.setAlpha(Color.alpha(ColorSelectedmaterial));
                            Bitmap Current = Bitmap.createBitmap(TILESIZE, TILESIZE, Bitmap.Config.ARGB_8888);
                            Canvas Currentcanvas = new Canvas();
                            Currentcanvas.setBitmap(Current);
                            Currentcanvas.drawBitmap(Tilebmp, src, src, temppaint);

                            this.canvas.drawBitmap(Current, src, dst, null);
                        }
                    }
                }
            }

            dst = new Rect((2) * HeTeIn + Height7Texturewidth, HeTeIn + Height7Texturewidth, HeTeIn * ((2) + 1), HeTeIn * (1 + 1));
            Paint tempPaint = new Paint();
            if (TILELAYER == TILELAYERSTART) {
                tempPaint.setColor(MATERIALARRAY[SelectedMaterial].getColor(TILELAYERSTART));
                this.canvas.drawRect(dst, tempPaint);
            }
            tempPaint.setColor(INVENTORYSELECTTILECOLOR);
            tempPaint.setStyle(Paint.Style.STROKE);
            tempPaint.setStrokeWidth((int) (Height7Texturewidth / 5));
            canvas.drawRect(dst, tempPaint);
        }
    }


    public void DrawInventoryTiles() { // Draws Inventorytiles on Screen // like inventory to select tiles
        if (DISPLAYINVENTORY) {

            Rect src = new Rect(0, 0, TILESIZETEXTURE, TILESIZETEXTURE); //source

            this.paint.setColor(INVENTORYBACKGROUNDCOLOR);
            this.canvas.drawRect((float) (WIDTHSCREEN - HEIGHTSCREEN) / 2, 0, WIDTHSCREEN - (float) (WIDTHSCREEN - HEIGHTSCREEN) / 2, HEIGHTSCREEN, this.paint); // grey inventorybox

            float DisplayedTileSize = (float) HEIGHTSCREEN / TEXTUREWIDTH;//(HEIGHT / TEXTUREWIDTH)+TILESIZE/TILESIZEORIGINAL;

            float TileScreenx, TileScreeny;

            for (int i = 0; i < INVENTORY.size(); i++) {

                TileScreenx = (float) ((HEIGHTSCREEN * INVENTORY.get(i).getScreenposition().getValue(0) / TILESIZE) / TEXTUREWIDTH);
                TileScreeny = (float) ((HEIGHTSCREEN * INVENTORY.get(i).getScreenposition().getValue(1) / TILESIZE) / TEXTUREWIDTH);

                TileScreenx += (float) (WIDTHSCREEN - HEIGHTSCREEN) / 2;

                Rect dst = new Rect((int) TileScreenx, (int) TileScreeny, (int) (TileScreenx + DisplayedTileSize), (int) (TileScreeny + DisplayedTileSize)); // destination
                try {

                    this.canvas.drawBitmap(TILETEXTURE.getBitmap(15, i), src, dst, null);
                    //this.canvas.drawRect(dst, this.paint);
                } catch (Exception e) {

                }
                /***/

                if (SelectedMaterial == i) { // Highlights the selected tile in green when moving over it
                    this.paint.setColor(INVENTORYSELECTTILECOLOR);
                    this.canvas.drawRect(dst, this.paint);
                }

                /***/
            }
        }
    }


    public void DrawKDTreeTiles() { // draws Tiles on Screen and checks first, if the Treepart is on screen
        Vector Cal = new Vector();
        Rect src = new Rect(0, 0, TILESIZETEXTURE, TILESIZETEXTURE);
        ArrayList<Tile> VisibleTiles = KDTREECOPY.getVisibleTilesInCurrentTree();
        if (this.Screenboundaries == null) {
            this.Screenboundaries = new Rect((int) (-TILESIZEzoom), (int) (-TILESIZEzoom), (int) (WIDTHSCREEN + TILESIZEzoom), (int) (HEIGHTSCREEN + TILESIZEzoom));
        }
        //Log.d("VISIBLE TILES: ", String.valueOf(VisibleTiles.size()));
        for (int i = 0; i < VisibleTiles.size(); i++) {

            Tile currentTile = VisibleTiles.get(i);
            //currentTile.SetonscreenPosition(camera.getEye2D());
            if (VisibleTiles.get(i) != null) {
                /**/
                int TileScreenx = (int) Cal.getScreencoordinatesFromTileCoordinates(currentTile.getPosition()).getValue(0);//(currentTile.getScreenposition().getValue(0) * zoom + WIDTHSCREEN / 2);
                int TileScreeny = (int) Cal.getScreencoordinatesFromTileCoordinates(currentTile.getPosition()).getValue(1);//(currentTile.getScreenposition().getValue(1) * zoom + HEIGHTSCREEN / 2);

                Rect TileonScreen = new Rect(TileScreenx, TileScreeny, (int) (TileScreenx + TILESIZEzoom), (int) (TileScreeny + TILESIZEzoom));


                if (this.Screenboundaries.contains(TileonScreen)/*|| new Rect(0, 0, WIDTHSCREEN, HEIGHTSCREEN).intersect(draw)*/) {
                    if (VisibleTiles.get(i).getTexture() != null) {
                        try {

                            this.canvas.drawBitmap(VisibleTiles.get(i).getTexture(), src, TileonScreen, null);
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }

        ///*********/
        if (FILLTILES && !DISPLAYINVENTORY && DRAWFILLTILERECT) {  // DRAWS THE FILL AREA
            this.paint.setColor(FILLTILECOLOR);
            this.canvas.drawRect(FILLTILERECT, this.paint);
        }

        FRAMETIME = System.currentTimeMillis() - FRAMETIMESTART;
        FRAMETIMESTART = System.currentTimeMillis();
    }

    public void DrawHitbox() {
        if (this.Screenboundaries == null) {
            this.Screenboundaries = new Rect((int) (-TILESIZEzoom), (int) (-TILESIZEzoom), (int) (WIDTHSCREEN + TILESIZEzoom), (int) (HEIGHTSCREEN + TILESIZEzoom));
        }
        if (HITBOX != null) {
            for (int i = 0; i < HITBOX.size(); i++) {
                this.paint.setColor(FILLTILECOLOR);
                Vector A = /**/new Vector().getScreencoordinatesFromTileCoordinates(/**/new Vector(HITBOX.get(i).left * TILESIZE,  HITBOX.get(i).top * TILESIZE)/**/)/**/;
                Vector B = /**/new Vector().getScreencoordinatesFromTileCoordinates(/**/new Vector(HITBOX.get(i).right * TILESIZE, HITBOX.get(i).bottom * TILESIZE)/**/)/**/;

                Rect Hitbox = new Rect((int) A.getValue(0), (int) A.getValue(1), (int) B.getValue(0), (int) B.getValue(1));
                this.canvas.drawRect(Hitbox, paint);
            }
        }

        float TILESIZEzoom = (float) Math.ceil(TILESIZE * (camera.getEye2D().getValue(2) / ZOOMFACTOR)); // Width the Tile has on Screen

        Rect Hitbox = new Rect((int) ((WIDTHSCREEN - TILESIZEzoom) / 2), (int) ((HEIGHTSCREEN - TILESIZEzoom) / 2), (int) ((WIDTHSCREEN + TILESIZEzoom) / 2), (int) ((HEIGHTSCREEN + TILESIZEzoom) / 2));
        this.paint.setColor(CHUNKCOLOR);
        this.canvas.drawRect(Hitbox, this.paint);

    }

    public void DrawAndGenerateTiles() { // draws Tiles on Screen and checks first, if the Treepart is on screen

        FRAMETIMESTART = System.currentTimeMillis();

        if (this.Screenboundaries == null) {
            this.Screenboundaries = new Rect((int) (-TILESIZEzoom), (int) (-TILESIZEzoom), (int) (WIDTHSCREEN + TILESIZEzoom), (int) (HEIGHTSCREEN + TILESIZEzoom));
        }
        //float zoom = (float) camera.getEye2D().getValue(2) / ZOOMFACTOR;
        //float TILESIZEzoom = (float) Math.ceil(TILESIZE * (camera.getEye2D().getValue(2) / ZOOMFACTOR));

        Vector Cal = new Vector();
        this.Screenboundaries = new Rect((int) (-TILESIZEzoom), (int) (-TILESIZEzoom), (int) (WIDTHSCREEN + TILESIZEzoom), (int) (HEIGHTSCREEN + TILESIZEzoom));
        //Rect src = new Rect(0, 0, TILESIZE, TILESIZE);


        /*/
        ArrayList<TileColor> VisibleTiles = GENERATE.Generate();
        /*/
        //ArrayList<TileColor> VisibleTiles = GENERATE.Generate2();
        ArrayList<TileColor> VisibleTiles = GENERATE.GenerateTerrain();
        /**/

        for (int i = 0; i < VisibleTiles.size(); i++) {

            TileColor currentTile = VisibleTiles.get(i);

            /**/
            int TileScreenx = (int) Cal.getScreencoordinatesFromTileCoordinates(currentTile.getPosition()).getValue(0);//(currentTile.getScreenposition().getValue(0) * zoom + WIDTHSCREEN / 2);
            int TileScreeny = (int) Cal.getScreencoordinatesFromTileCoordinates(currentTile.getPosition()).getValue(1);//(currentTile.getScreenposition().getValue(1) * zoom + HEIGHTSCREEN / 2);
            Rect TileonScreen = new Rect(TileScreenx, TileScreeny, (int) (TileScreenx + TILESIZEzoom), (int) (TileScreeny + TILESIZEzoom));
            /*/
            int TileScreenx = (int) Cal.getScreencoordinatesFromTileCoordinatesFake3d(currentTile.getPosition(), (float)((currentTile.getDepth()))).getValue(0);
            int TileScreeny = (int) Cal.getScreencoordinatesFromTileCoordinatesFake3d(currentTile.getPosition(), (float)((currentTile.getDepth()))).getValue(1);
            Rect TileonScreen = new Rect(TileScreenx, TileScreeny, (int) (TileScreenx + TILESIZEzoom*2), (int) (TileScreeny + TILESIZEzoom*2));
            /**/


            if (this.Screenboundaries.contains(TileonScreen)/*|| new Rect(0, 0, WIDTHSCREEN, HEIGHTSCREEN).intersect(draw)*/) {
                //canvas.drawBitmap(VisibleTiles.get(i).getTexture(), src, TileonScreen, null);
                this.paint.setColor(currentTile.getColor());
                this.canvas.drawRect(TileonScreen, this.paint);
            }
        }

        ///*********/
        if (FILLTILES && !DISPLAYINVENTORY && DRAWFILLTILERECT) {  // DRAWS THE FILL AREA
            this.paint.setColor(FILLTILECOLOR);
            this.canvas.drawRect(FILLTILERECT, this.paint);
        }

        FRAMETIME = System.currentTimeMillis() - FRAMETIMESTART;
        int a = 4; // 150ms 70x70 5 noise values // 79ms 70x70 3 noise Values // 78ms 70x70 0 noise Values
        FRAMETIMESTART = System.currentTimeMillis();
    }

    public void DrawKDTree() { // Draws the KDTREE in green and red
        ArrayList<Rect> KDLVL = KDTREECOPY.getBoundaries();
        int Strokethickness = (int) TILESIZEzoom;//(int) Math.ceil(TILESIZE * zoom); // Stroke as thick as the Tiles are wide

        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(Strokethickness);


        //paint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < KDLVL.size(); i++) {

            this.paint.setColor(Color.argb(50, (int) (255 * (2 * i / KDLVL.size())), (int) (255 * (1 - 2 * i / KDLVL.size())), 120));

            Vector a = new Vector(KDLVL.get(i).left, KDLVL.get(i).top).multiplydouble(TILESIZE);
            Vector b = new Vector(KDLVL.get(i).right, KDLVL.get(i).bottom).multiplydouble(TILESIZE);

            a = a.getScreencoordinatesFromTileCoordinates(a);
            b = b.getScreencoordinatesFromTileCoordinates(b);

            Rect draw = new Rect((int) a.getValue(0), (int) a.getValue(1), (int) b.getValue(0), (int) b.getValue(1));
            if (this.Screenboundaries.contains(draw) || new Rect(0, 0, WIDTHSCREEN, HEIGHTSCREEN).intersect(draw)) {
                this.canvas.drawRect(draw, this.paint);
            }
        }
        this.paint.setStyle(Paint.Style.FILL);
        //Log.d("TILES: ",String.valueOf(KDLVL.size()));
    }


    public void DrawPortalTiles() {
        Vector Cal = new Vector();
        Rect src = new Rect(0, 0, TILESIZETEXTURE, TILESIZETEXTURE);
        ArrayList<Tile> VisibleTiles = new ArrayList<>();

        //paint.setColor(Color.rgb(0,255,0));
        //canvas.drawRect(Screenboundaries,paint);
        for (int j = 0; j < PORTALLIST.size(); j++) {
            Rect PortalboundariesOnScreen = PORTALLIST.get(j).getBoundarieOnScreen();

            //try {
            if (this.Screenboundaries == null) {
                this.Screenboundaries = new Rect((int) (-TILESIZEzoom), (int) (-TILESIZEzoom), (int) (WIDTHSCREEN + TILESIZEzoom), (int) (HEIGHTSCREEN + TILESIZEzoom));
            }
            if (this.Screenboundaries.contains(PortalboundariesOnScreen)/**/ || this.Screenboundaries.intersect(PortalboundariesOnScreen)/**/) {

                //paint.setColor(Color.rgb(255,0,0));
                //canvas.drawRect(PortalboundariesOnScreen,paint);

                VisibleTiles.clear();
                VisibleTiles.addAll(PORTALLIST.get(j).getVisibleTiles());

                for (int i = 0; i < VisibleTiles.size(); i++) {
                    if (VisibleTiles.get(i) != null) {
                        Vector Tileposition = VisibleTiles.get(i).getPosition();

                        Vector DifferencePortalsRAW = (PORTALLIST.get(j).getPositionRAW().subtract(PORTALLIST.get(PORTALLIST.get(j).getPortalPartner()).getPositionRAW())).multiplydouble(TILESIZE);

                        Tileposition = Tileposition.addVector(DifferencePortalsRAW);

                        int TileScreenx = (int) ((Cal.getScreencoordinatesFromTileCoordinates(Tileposition)).getValue(0));
                        int TileScreeny = (int) ((Cal.getScreencoordinatesFromTileCoordinates(Tileposition)).getValue(1));

                        Rect TileonScreen = new Rect(TileScreenx, TileScreeny, (int) (TileScreenx + TILESIZEzoom), (int) (TileScreeny + TILESIZEzoom));

                        if (this.Screenboundaries.contains(TileonScreen)) {
                            //paint.setAlpha(150);

                            //canvas.drawRect(TileonScreen, paint);
                            this.canvas.drawBitmap(VisibleTiles.get(i).getTexture(), src, TileonScreen, this.paint);
                            /**/
                            paint.setColor(Color.rgb(0, 255, 255));
                            paint.setAlpha(50);
                            canvas.drawRect(TileonScreen, paint);
                            paint.setAlpha(255);/***/
                        }
                    }
                }
            }
        }
    }

    public void postImage() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                IMAGE.setImageBitmap(bmp);
            }
        });
    }
}
