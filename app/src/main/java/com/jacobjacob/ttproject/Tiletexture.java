package com.jacobjacob.ttproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

import static com.jacobjacob.ttproject.Util.CONTEXT;
import static com.jacobjacob.ttproject.Util.MATERIALARRAY;
import static com.jacobjacob.ttproject.Util.MATERIALLIST;
import static com.jacobjacob.ttproject.Util.MATERIALLISTUPDATING;
import static com.jacobjacob.ttproject.Util.TEXTUREWIDTH;
import static com.jacobjacob.ttproject.Util.TILELAYER;
import static com.jacobjacob.ttproject.Util.TILESIZE;
import static com.jacobjacob.ttproject.Util.TILESIZEORIGINAL;
import static com.jacobjacob.ttproject.Util.TILESIZETEXTURE;
import static com.jacobjacob.ttproject.Util.TILESOFSINGLEKIND;

public class Tiletexture {
    private ArrayList<Bitmap> Bitmaplist;

    //private Bitmap[][] MATERIALLIST;
    //TODO das und das anpassen
    ArrayList<Bitmap> RGBTilesLAYER1 = new ArrayList<>();
    ArrayList<Bitmap> RGBTilesLAYER2 = new ArrayList<>();
    ArrayList<Bitmap> RGBTilesLAYER3 = new ArrayList<>();
    private ArrayList<Bitmap> BitmaplistLLOD; // low level of detail

    private Bitmap texture;     //32 x 32
    //private Bitmap texturellod;     //
    private Bitmap Tilemapbmp;  //from source as bmp
    private Rect SrcDst = new Rect(0, 0, TILESIZE, TILESIZE);
    private long Starttime;

    public Tiletexture() { //rgb
        Bitmaplist = new ArrayList<>();

        MATERIALLIST = new Bitmap[TEXTUREWIDTH * TEXTUREWIDTH][15];


        Bitmap LayerR, LayerG, LayerB;
        //BitmaplistLLOD = new ArrayList<>();

        Paint paint = new Paint();
        int Pixel;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Tilemapbmp = BitmapFactory.decodeResource(CONTEXT.getResources(), R.drawable.tilemap, options);
        //texturellod = BitmapFactory.decodeResource(CONTEXT.getResources(),R.drawable.tilemap);

        TILESIZE = Tilemapbmp.getWidth() / TEXTUREWIDTH;
        TILESIZETEXTURE = TILESIZE; // The size of the source // if the camera is to far away, the source needs to get scaled accordingly to make it run faster with 10000 Tiles on the screen

        for (int row = 0; row < TEXTUREWIDTH; row++) {
            for (int column = 0; column < TEXTUREWIDTH; column++) {

                LayerR = Bitmap.createBitmap(TILESIZE, TILESIZE, Bitmap.Config.ARGB_8888);
                LayerG = Bitmap.createBitmap(TILESIZE, TILESIZE, Bitmap.Config.ARGB_8888);
                LayerB = Bitmap.createBitmap(TILESIZE, TILESIZE, Bitmap.Config.ARGB_8888);
                texture = Bitmap.createBitmap(TILESIZE, TILESIZE, Bitmap.Config.ARGB_8888);

                Canvas canvasLayerR = new Canvas(LayerR);
                Canvas canvasLayerG = new Canvas(LayerG);
                Canvas canvasLayerB = new Canvas(LayerB);
                Canvas canvastexture = new Canvas(texture);

                for (int i = 0; i < TILESIZEORIGINAL; i++) {
                    int X1 = (int) ((i * TILESIZE) / TILESIZEORIGINAL);
                    int X2 = (int) (((i + 1) * TILESIZE) / TILESIZEORIGINAL);

                    for (int j = 0; j < TILESIZEORIGINAL; j++) {
                        int Y1 = (int) ((j * TILESIZE) / TILESIZEORIGINAL);
                        int Y2 = (int) (((j + 1) * TILESIZE) / TILESIZEORIGINAL);


                        int PixelX = (int) ((column * TILESIZE + i * TILESIZE / TILESIZEORIGINAL));
                        int PixelY = (int) ((row * TILESIZE + j * TILESIZE / TILESIZEORIGINAL));

                        Pixel = Tilemapbmp.getPixel(PixelX, PixelY);

                        int A = Color.alpha(Pixel);
                        int R = Color.red(Pixel);
                        int G = Color.green(Pixel);
                        int B = Color.blue(Pixel);
                        if (R < 20) {
                            R = 0;
                        }
                        if (G < 20) {
                            G = 0;
                        }
                        if (B < 20) {
                            B = 0;
                        }
                        paint.setColor(Color.argb(R, R, R, R));
                        canvasLayerR.drawRect(X1, Y1, X2, Y2, paint);
                        //canvasLayerR.drawPoint(X1, Y1, paint);

                        paint.setColor(Color.argb(G, G, G, G));
                        canvasLayerG.drawRect(X1, Y1, X2, Y2, paint);
                        //canvasLayerG.drawPoint(X1, Y1, paint);

                        paint.setColor(Color.argb(B, B, B, B));
                        canvasLayerB.drawRect(X1, Y1, X2, Y2, paint);
                        //canvasLayerB.drawPoint(X1, Y1, paint);

                        paint.setColor(Color.argb(A, R, G, B));
                        canvastexture.drawRect(X1, Y1, X2, Y2, paint);
                        //canvastexture.drawPoint(X1, Y1, paint);
                    }
                }
                RGBTilesLAYER1.add(LayerR);
                RGBTilesLAYER2.add(LayerG);
                RGBTilesLAYER3.add(LayerB);
                Bitmaplist.add(texture);
                //list for R G B in order / sorted /
            }
        }
        /** Sorting Works, next a class to Color and Composite a Tilemap with Colors and a way to create custom Tilemaps
         * 5 Tiles are needed to create one Tilemap.
         * With Colorsliders and selection int? I can then take the r g b bmps and give them the selected Color to put them all together inside a new Array for that specific new Material
         * **/
        int a = 0;
        //CreateTilemap();
    }


    /**
     * This method rotates
     *
     * @param Input   the bitmap that should be rotated
     * @param Degrees the amount of rotation
     * @return the rotated bitmap
     */
    private Bitmap Rotate(Bitmap Input, int Degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(Degrees);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(Input, Input.getWidth(), Input.getHeight(), true);
        return /* Bitmap rotatedBitmap = */Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    private Bitmap OverlayTilemap(Bitmap Bottom, Bitmap Top) {

        Bitmap Out = Bitmap.createBitmap(TILESIZE, TILESIZE, Bitmap.Config.ARGB_8888);
        Canvas Output = new Canvas();
        Output.setBitmap(Out);
        Output.drawBitmap(Bottom, SrcDst, SrcDst, null);
        Output.drawBitmap(Top, SrcDst, SrcDst, null);

        return Out;
    }

    public Bitmap getBitmap(int ID, int MATERIALint) { // no animations possible yet
        //MATERIALint = 0;
        //if (MATERIALLIST == null){
        //    MATERIALLIST = new Bitmap[TEXTUREWIDTH * TEXTUREWIDTH][15];
        //}
        if (MATERIALLIST != null && MATERIALLIST[MATERIALint] != null && ID < MATERIALLIST[MATERIALint].length) {

            /**/
            Bitmap imagetexture;
            imagetexture = MATERIALLIST[MATERIALint][ID];

            return imagetexture;/*/return Materiallist.get(ID)[MATERIALint];/**/

        } else {
            return null;
        }
    }

    public Bitmap getBitmap(int MATERIALint) { // no animations possible yet
        // Material
        if (MATERIALint >= 0) {
            if (MATERIALARRAY[MATERIALint] != null && MATERIALint * 5 < Bitmaplist.size()) {
                if (TILELAYER == 1) {
                    return RGBTilesLAYER1.get(MATERIALint * 5);
                } else if (TILELAYER == 2) {
                    return RGBTilesLAYER2.get(MATERIALint * 5);
                } else if (TILELAYER == 3) {
                    return RGBTilesLAYER3.get(MATERIALint * 5);
                } else {
                    return null;
                }

            } else {
                return null;
            }
        }else {
            return null;
        }
    }


    public void CreateTilemap(int Material, int Layer1, int Layer2, int Layer3, int ColorLayer1, int ColorLayer2, int ColorLayer3) { // needs more Options as input

        Bitmap[] SingleTilemap = new Bitmap[15]; // Tiles from 0 to 15 as Bitmap

        SrcDst = new Rect(0, 0, TILESIZE, TILESIZE);
        Paint paint = new Paint();
        ColorFilter filter;

        //Layer1 = 0;
        //Layer2 = 0;
        //Layer3 = 0;
        if (Layer1 < 0){
            Layer1 = 0;
        }
        if (Layer2 < 0){
            Layer2 = 0;
        }
        if (Layer3 < 0){
            Layer3 = 0;
        }
        Layer1 *= TILESOFSINGLEKIND;
        Layer2 *= TILESOFSINGLEKIND;
        Layer3 *= TILESOFSINGLEKIND;

        for (int i = 0; i < TILESOFSINGLEKIND; i++) { // i < 5 // all five starting tiles in grey

            Bitmap Current = Bitmap.createBitmap(TILESIZE, TILESIZE, Bitmap.Config.ARGB_8888);
            Canvas Currentcanvas = new Canvas();
            Currentcanvas.setBitmap(Current);


            filter = new LightingColorFilter(ColorLayer1, 0);
            paint.setColorFilter(filter);
            paint.setAlpha(Color.alpha(ColorLayer1));
            Currentcanvas.drawBitmap(RGBTilesLAYER1.get(Layer1 + i), SrcDst, SrcDst, paint);

            filter = new LightingColorFilter(ColorLayer2, 0);
            paint.setColorFilter(filter);
            paint.setAlpha(Color.alpha(ColorLayer2));
            Currentcanvas.drawBitmap(RGBTilesLAYER2.get(Layer2 + i), SrcDst, SrcDst, paint);

            filter = new LightingColorFilter(ColorLayer3, 0);
            paint.setColorFilter(filter);
            paint.setAlpha(Color.alpha(ColorLayer3));
            Currentcanvas.drawBitmap(RGBTilesLAYER3.get(Layer3 + i), SrcDst, SrcDst, paint);
            SingleTilemap[i] = Current;
        }

        Bitmap[] FinishedTilemap = getFinnishedBitmaplist(SingleTilemap);

        for (int i = 0; i < FinishedTilemap.length; i++) {
            FinishedTilemap[i] = OverlayTilemap(FinishedTilemap[15], FinishedTilemap[i]);
        }
        MATERIALLIST[Material] = FinishedTilemap; // updates all Tiles from a specific Material
    }

    public void UpdateTilemap(int Material, int Layer1, int Layer2, int Layer3, int ColorLayer1, int ColorLayer2, int ColorLayer3) { // needs more Options as input

        if (MATERIALLISTUPDATING == null) {
            MATERIALLISTUPDATING = new Bitmap[TEXTUREWIDTH * TEXTUREWIDTH][15];
        }

        Bitmap[] SingleTilemap = new Bitmap[15]; // Tiles from 0 to 15 as Bitmap

        SrcDst = new Rect(0, 0, TILESIZE, TILESIZE);
        Paint paint = new Paint();
        ColorFilter filter;

        Layer1 *= TILESOFSINGLEKIND;
        Layer2 *= TILESOFSINGLEKIND;
        Layer3 *= TILESOFSINGLEKIND;

        for (int i = 0; i < TILESOFSINGLEKIND; i++) { // i < 5 // all five starting tiles in grey

            Bitmap Current = Bitmap.createBitmap(TILESIZE, TILESIZE, Bitmap.Config.ARGB_8888);
            Canvas Currentcanvas = new Canvas();
            Currentcanvas.setBitmap(Current);


            filter = new LightingColorFilter(ColorLayer1, 0);
            paint.setColorFilter(filter);
            paint.setAlpha(Color.alpha(ColorLayer1));
            Currentcanvas.drawBitmap(RGBTilesLAYER1.get(Layer1 + i), SrcDst, SrcDst, paint);

            filter = new LightingColorFilter(ColorLayer2, 0);
            paint.setColorFilter(filter);
            paint.setAlpha(Color.alpha(ColorLayer2));
            Currentcanvas.drawBitmap(RGBTilesLAYER2.get(Layer2 + i), SrcDst, SrcDst, paint);

            filter = new LightingColorFilter(ColorLayer3, 0);
            paint.setColorFilter(filter);
            paint.setAlpha(Color.alpha(ColorLayer3));
            Currentcanvas.drawBitmap(RGBTilesLAYER3.get(Layer3 + i), SrcDst, SrcDst, paint);
            SingleTilemap[i] = Current;
        }

        Bitmap[] FinishedTilemap = getFinnishedBitmaplist(SingleTilemap);

        for (int i = 0; i < FinishedTilemap.length; i++) {
            FinishedTilemap[i] = OverlayTilemap(FinishedTilemap[15], FinishedTilemap[i]);
        }

        MATERIALLISTUPDATING[Material] = FinishedTilemap; // updates all Tiles from a specific Material
    }

    private Bitmap[] getFinnishedBitmaplist(Bitmap[] SingleTilemap) {
        Bitmap[] FinishedTilemap = new Bitmap[16];

        FinishedTilemap[15] = SingleTilemap[0];
        /**Need the Bottom underneath*/
        FinishedTilemap[14] = SingleTilemap[4];
        FinishedTilemap[13] = Rotate(SingleTilemap[4], 90);
        FinishedTilemap[12] = Rotate(SingleTilemap[1], 90);
        FinishedTilemap[11] = Rotate(SingleTilemap[4], 180);
        FinishedTilemap[10] = OverlayTilemap(Rotate(SingleTilemap[4], 180), SingleTilemap[4]);
        FinishedTilemap[9] = Rotate(SingleTilemap[1], 180);
        FinishedTilemap[8] = Rotate(SingleTilemap[2], 90);
        FinishedTilemap[7] = Rotate(SingleTilemap[4], 270);
        FinishedTilemap[6] = SingleTilemap[1];
        FinishedTilemap[5] = Rotate(FinishedTilemap[10], 90);
        FinishedTilemap[4] = SingleTilemap[2];
        FinishedTilemap[3] = Rotate(SingleTilemap[1], 270);
        FinishedTilemap[2] = Rotate(SingleTilemap[2], 270);
        FinishedTilemap[1] = Rotate(SingleTilemap[2], 180);
        FinishedTilemap[0] = OverlayTilemap(SingleTilemap[3], Rotate(SingleTilemap[3], 90));
        FinishedTilemap[0] = OverlayTilemap(FinishedTilemap[0], Rotate(FinishedTilemap[0], 180));
        return FinishedTilemap;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}

