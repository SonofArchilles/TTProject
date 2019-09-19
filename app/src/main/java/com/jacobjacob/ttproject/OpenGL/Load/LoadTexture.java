package com.jacobjacob.ttproject.OpenGL.Load;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.jacobjacob.ttproject.R;
import com.jacobjacob.ttproject.Vector;

import java.util.ArrayList;

import static com.jacobjacob.ttproject.Util.*;


public class LoadTexture { //rgb

    private ArrayList<Bitmap> Bitmaplist;
    ArrayList<Bitmap> RGBTilesLAYER1 = new ArrayList<>();
    ArrayList<Bitmap> RGBTilesLAYER2 = new ArrayList<>();
    ArrayList<Bitmap> RGBTilesLAYER3 = new ArrayList<>();

    private Bitmap texture;
    private Bitmap Tilemapbmp;  //from source as bmp

    Rect SrcDst = new Rect(0, 0, TILESIZE, TILESIZE);


    public LoadTexture() {
        Bitmaplist = new ArrayList<>();

        MATERIALLIST = new Bitmap[TEXTUREWIDTH * TEXTUREWIDTH][15];
        MATERIALNORMALS = new Bitmap[TEXTUREWIDTH * TEXTUREWIDTH][15];

        Bitmap LayerR, LayerG, LayerB;

        Paint paint = new Paint();
        int Pixel;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Tilemapbmp = BitmapFactory.decodeResource(CONTEXT.getResources(), R.drawable.tilemap, options);

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

                        paint.setColor(Color.argb(G, G, G, G));
                        canvasLayerG.drawRect(X1, Y1, X2, Y2, paint);

                        paint.setColor(Color.argb(B, B, B, B));
                        canvasLayerB.drawRect(X1, Y1, X2, Y2, paint);
                        paint.setColor(Color.argb(A, R, G, B));
                        canvastexture.drawRect(X1, Y1, X2, Y2, paint);
                    }
                }
                RGBTilesLAYER1.add(LayerR);
                RGBTilesLAYER2.add(LayerG);
                RGBTilesLAYER3.add(LayerB);
                Bitmaplist.add(texture);
            }
        }
    }


    /**
     * This method rotates a Bitmap
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

    /**
     * This method Overlays two Bitmaps
     *
     * @param Bottom The first bitmap on the bottom
     * @param Top    The second Bitmap that gets drawn on top of the first one
     * @return
     */
    private Bitmap OverlayTilemap(Bitmap Bottom, Bitmap Top) {

        Bitmap Out = Bitmap.createBitmap(TILESIZE, TILESIZE, Bitmap.Config.ARGB_8888);
        Canvas Output = new Canvas();
        Output.setBitmap(Out);
        Output.drawBitmap(Bottom, SrcDst, SrcDst, null);
        Output.drawBitmap(Top, SrcDst, SrcDst, null);

        return Out;
    }

    /**
     * Returns a bmp of a Bitmap with fitting ID and Material
     *
     * @param ID          The Id of the Bitmap / can range from 0 to 15
     * @param MATERIALint The Material the Texture has
     * @return
     */
    public void getTexture(int ID, int MATERIALint) { // no animations possible yet

        if (!PLACETILE/*MATERIALNORMALS[MATERIALint] != null && MATERIALint * 5 < Bitmaplist.size() && MATERIALARRAY[MATERIALint].showNormal()*/) {
            try {
                //return MATERIALNORMALS[MATERIALint][ID];
            } catch (Exception e) {
            }
        }


        if (MATERIALLIST != null && MATERIALLIST[MATERIALint] != null && ID < MATERIALLIST[MATERIALint].length) {

            // Bind to the texture in OpenGL
            final int[] textureHandle = new int[1];
            GLES20.glGenTextures(1, textureHandle, 0);


            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, MATERIALLIST[MATERIALint][ID], 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            //bitmap.recycle();

            //return ;/*/return Materiallist.get(ID)[MATERIALint];/**/

        } else {
            //return null;
        }
    }

    /**
     * Returns the Bitmap of a specific Layer to make the quick selection screen possible.
     * It does not take in an ID, the ID equals 15 / the bottom Tile
     *
     * @param MATERIALint The Material we want the Bitmap of a specific Layer from
     * @return a grey Bitmap
     */
    public Bitmap getBitmap(int MATERIALint) { // no animations possible yet
        // Material
        if (MATERIALint >= 0) {
            if (MATERIALARRAY[MATERIALint] != null && MATERIALint * 5 < Bitmaplist.size()) {
                if (TILELAYER == TILELAYERSTART + 1) {
                    return RGBTilesLAYER1.get(MATERIALint * 5);
                } else if (TILELAYER == TILELAYERSTART + 2) {
                    return RGBTilesLAYER2.get(MATERIALint * 5);
                } else if (TILELAYER == TILELAYERSTART + 3) {
                    return RGBTilesLAYER3.get(MATERIALint * 5);
                } else {
                    return null;
                }

            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    public void CreateTilemap(int Material, int Layer1, int Layer2, int Layer3, int ColorLayer0, int ColorLayer1, int ColorLayer2, int ColorLayer3) { // needs more Options as input

        Bitmap[] SingleTilemap = new Bitmap[15]; // Tiles from 0 to 15 as Bitmap
        Paint paint = new Paint();
        ColorFilter filter;

        //Layer1 = 0;
        //Layer2 = 0;
        //Layer3 = 0;
        if (Layer1 < 0) {
            Layer1 = 0;
        }
        if (Layer2 < 0) {
            Layer2 = 0;
        }
        if (Layer3 < 0) {
            Layer3 = 0;
        }
        Layer1 *= TILESOFSINGLEKIND;
        Layer2 *= TILESOFSINGLEKIND;
        Layer3 *= TILESOFSINGLEKIND;

        for (int i = 0; i < TILESOFSINGLEKIND; i++) { // i < 5 // all five starting tiles in grey

            Bitmap Current = Bitmap.createBitmap(TILESIZE, TILESIZE, Bitmap.Config.ARGB_8888);
            Canvas Currentcanvas = new Canvas();
            Currentcanvas.setBitmap(Current);
            if (i == 0) {
                paint.setColor(ColorLayer0);
                Currentcanvas.drawRect(SrcDst, paint);
            }

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

    public void UpdateTilemap(int Material, int Layer1, int Layer2, int Layer3, int ColorLayer0, int ColorLayer1, int ColorLayer2, int ColorLayer3) { // needs more Options as input

        if (MATERIALLISTUPDATING == null) {
            MATERIALLISTUPDATING = new Bitmap[TEXTUREWIDTH * TEXTUREWIDTH][15];
        }

        Bitmap[] SingleTilemap = new Bitmap[15]; // Tiles from 0 to 15 as Bitmap


        Paint paint = new Paint();
        ColorFilter filter;


        if (Layer1 < 0) {
            Layer1 = 0;
        }
        if (Layer2 < 0) {
            Layer2 = 0;
        }
        if (Layer3 < 0) {
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

    public void CreateNormals(int intMaterial, int Layer1, int Layer2, int Layer3, int alpha1, int alpha2, int alpha3) {

        Bitmap[] SingleTilemap = new Bitmap[15]; // Tiles from 0 to 15 as Bitmap
        Paint paint = new Paint();

        if (Layer1 < 0) {
            Layer1 = 0;
        }
        if (Layer2 < 0) {
            Layer2 = 0;
        }
        if (Layer3 < 0) {
            Layer3 = 0;
        }

        Layer1 *= TILESOFSINGLEKIND;
        Layer2 *= TILESOFSINGLEKIND;
        Layer3 *= TILESOFSINGLEKIND;

        for (int i = 0; i < TILESOFSINGLEKIND; i++) { // i < 5 // all five starting tiles in grey

            Bitmap Current = Bitmap.createBitmap(TILESIZE, TILESIZE, Bitmap.Config.ARGB_8888);
            Canvas Currentcanvas = new Canvas();
            Currentcanvas.setBitmap(Current);

            if (i == 0){
                paint.setColor(Color.rgb(127,127,255)); // equals 0 0 1
                //paint.setColor(Color.rgb(0,0,0)); // equals 0 0 1
                Currentcanvas.drawRect(new Rect(0,0,TILESIZEORIGINAL,TILESIZEORIGINAL), paint);
            }

            for (int j = TILESIZEORIGINAL; j < TILESIZEORIGINAL * 2; j++) {
                for (int k = TILESIZEORIGINAL; k < 2 * TILESIZEORIGINAL; k++) {


                    int ColorLayerUP = 0;
                    int ColorLayerDOWN = 0;
                    int ColorLayerLEFT = 0;
                    int ColorLayerRIGHT = 0;

                    Bitmap Layer;
                    float Alpha;

                    for (int l = 0; l < 3; l++) {

                        if (l == 0) {
                            Layer = RGBTilesLAYER1.get(Layer1 + i);
                            Alpha = alpha1;
                        } else if (l == 1) {
                            Layer = RGBTilesLAYER2.get(Layer2 + i);
                            Alpha = alpha2;
                        } else {
                            Layer = RGBTilesLAYER2.get(Layer3 + i);
                            Alpha = alpha3;
                        }

                        int jrelative = j % TILESIZEORIGINAL;
                        int krelative = k % TILESIZEORIGINAL;

                        ColorLayerUP += (int) (((Alpha / 255.0f)) * Color.red(Layer.getPixel(jrelative, (k - 1) % TILESIZEORIGINAL)));// takes the colorvalue of the three bitmaps at the same point

                        ColorLayerDOWN += (int) (((Alpha / 255.0f)) * Color.red(Layer.getPixel(jrelative, (k + 1) % TILESIZEORIGINAL)));// takes the colorvalue of the three bitmaps at the same point

                        ColorLayerLEFT += (int) (((Alpha / 255.0f)) * Color.red(Layer.getPixel((j - 1) % TILESIZEORIGINAL, krelative)));// takes the colorvalue of the three bitmaps at the same point

                        ColorLayerRIGHT += (int) (((Alpha / 255.0f)) * Color.red(Layer.getPixel((j + 1) % TILESIZEORIGINAL, krelative)));// takes the colorvalue of the three bitmaps at the same point
                    }

                    Vector NormalVec;

                    //NormalVec = (NormalVec.normalize()).multiplydouble(255);

                    float Scale = alpha1 + alpha2 + alpha3;

                    float Hor = (float) (-ColorLayerLEFT + ColorLayerRIGHT) / Scale; // value from 0 to 1
                    float Ver = (float) (-ColorLayerUP + ColorLayerDOWN) / Scale; // value from 0 to 1

                    float Sca = 1f;

                    Vector vb = new Vector(Sca, 0,Hor).normalize();
                    Vector va = new Vector(0, Sca,Ver).normalize();

                    //va = new Vector(Hor, 0,Sca).normalize();
                    //vb = new Vector(0, Ver,Sca).normalize();
                    NormalVec = (va.cross(vb)).normalize();

                    NormalVec = new Vector(NormalVec.getX(),NormalVec.getY(),Math.abs(NormalVec.getZ())).normalize();

                    int red = (int) (255-((NormalVec.getX() + 1) * 127));
                    int green = (int) (255-(NormalVec.getY() + 1) * 127);
                    int blue =  (int) ((NormalVec.getZ() + 1) * 127);

                    paint.setColor(Color.rgb(red, green, blue));
                    //Currentcanvas.drawRect(j%TILESIZEORIGINAL, k%TILESIZEORIGINAL,j%TILESIZEORIGINAL+1,k%TILESIZEORIGINAL+1, paint);
                    if ((red < 120 || red > 133) && (green < 120 || green > 133) && (blue < 120 || blue > 133)) {
                        Currentcanvas.drawPoint(j % TILESIZEORIGINAL, k % TILESIZEORIGINAL, paint);
                    }
                }
            }

            SingleTilemap[i] = Current;
        }


        Bitmap[] FinishedTilemap = getFinnishedBitmaplist(SingleTilemap);

        for (int i = 0; i < FinishedTilemap.length; i++) {
            FinishedTilemap[i] = OverlayTilemap(FinishedTilemap[15], FinishedTilemap[i]);
        }

        MATERIALNORMALS[intMaterial] = FinishedTilemap;
    }
}