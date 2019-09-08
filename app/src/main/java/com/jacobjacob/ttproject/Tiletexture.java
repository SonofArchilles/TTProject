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
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.util.ArrayList;

import static com.jacobjacob.ttproject.Util.CONTEXT;
import static com.jacobjacob.ttproject.Util.MATERIALARRAY;
import static com.jacobjacob.ttproject.Util.MATERIALLIST;
import static com.jacobjacob.ttproject.Util.MATERIALLISTUPDATING;
import static com.jacobjacob.ttproject.Util.MATERIALNORMALS;
import static com.jacobjacob.ttproject.Util.NORMALSTRENGTH;
import static com.jacobjacob.ttproject.Util.PLACETILE;
import static com.jacobjacob.ttproject.Util.TEXTUREWIDTH;
import static com.jacobjacob.ttproject.Util.TILELAYER;
import static com.jacobjacob.ttproject.Util.TILELAYERSTART;
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

    /**
     * Initializes the bmp Arrays. Takes the rgb values and creates new Arrays with grey images that get Colored later
     */
    public Tiletexture() { //rgb
        Bitmaplist = new ArrayList<>();

        MATERIALLIST = new Bitmap[TEXTUREWIDTH * TEXTUREWIDTH][15];
        MATERIALNORMALS = new Bitmap[TEXTUREWIDTH * TEXTUREWIDTH][15];

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
    public Bitmap getBitmap(int ID, int MATERIALint) { // no animations possible yet

        if (!PLACETILE/*MATERIALNORMALS[MATERIALint] != null && MATERIALint * 5 < Bitmaplist.size() && MATERIALARRAY[MATERIALint].showNormal()*/) {
            try {
                return MATERIALNORMALS[MATERIALint][ID];
            } catch (Exception e) {
                Log.d("TILETEXTURE","Tile not in Normal Array!");
            }
        }


        if (MATERIALLIST != null && MATERIALLIST[MATERIALint] != null && ID < MATERIALLIST[MATERIALint].length) {
            return MATERIALLIST[MATERIALint][ID];/*/return Materiallist.get(ID)[MATERIALint];/**/

        } else {
            return null;
        }
    }


    public void deleteTextures() {
        for (int i = 0; i < TEXTUREWIDTH; i++) {
            try {
                GLES20.glDeleteTextures(15, Textures[i], 0);

            } catch (Exception e) {
                Log.d("Textures: ", "Failed Deleting Textures" + e);
            }
        }
    }

    public void deleteNormals() {
        for (int i = 0; i < TEXTUREWIDTH; i++) {
            try {
                GLES20.glDeleteTextures(15, TextureNormals[i], 0);
            } catch (Exception e) {
                Log.d("Textures: ", "Failed Deleting Textures" + e);
            }
        }
    }


    /**
     * Updates the Material loads the Bitmap as a Texture
     *
     * @param MaterialToUpdate The Material that gets a new Texture
     */
    public void UpdateMaterialTexture(int MaterialToUpdate) {
        MATERIALARRAY[MaterialToUpdate].CreateMaterialTileset();

        updateTextures(MaterialToUpdate);
    }


    /**
     * Updates the Material loads the Bitmap as a Texture. This is the normal map- Texture
     *
     * @param MaterialToUpdate The Material that gets a new Texture
     */
    public void UpdateMaterialNormalTexture(int MaterialToUpdate) {
        MATERIALARRAY[MaterialToUpdate].CreateMaterialTilesetNormal();

        updateTexturesNormals(MaterialToUpdate);
    }


    public void updateTextures(int TextureToUpdate) {

        int[] textureHandle = new int[16];
        for (int j = 0; j < 16; j++) {


            GLES20.glGenTextures(1, textureHandle, j);

            try {

                if (textureHandle[j] != 0) {
                    // Bind to the texture in OpenGL
                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[j]);

                    // Set filtering
                    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

                    // Load the bitmap into the bound texture.
                    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, MATERIALLIST[TextureToUpdate][j], 0);
                }
            } catch (Exception e) {
                Log.d("Load Texture:", "" + e);
            }
            if (textureHandle[0] == 0) {
                throw new RuntimeException("Error loading texture.");
            }
            if (j == 15) {
                Textures[TextureToUpdate] = textureHandle;
            }
        }
    }

    public void updateTexturesNormals(int TextureToUpdate) {


        int[] textureHandle = new int[16];
        for (int j = 0; j < 16; j++) {

            GLES20.glGenTextures(1, textureHandle, j);

            try {

                if (textureHandle[j] != 0) {
                    // Bind to the texture in OpenGL
                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[j]);

                    // Set filtering
                    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

                    // Load the bitmap into the bound texture.
                    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, MATERIALNORMALS[TextureToUpdate][j], 0);
                }
            } catch (Exception e) {
                Log.d("Load Texture:", "" + e);
            }
            if (textureHandle[0] == 0) {
                throw new RuntimeException("Error loading texture.");
            }
            if (j == 15) {
                TextureNormals[TextureToUpdate] = textureHandle;
            }
        }
    }






















/*/
    public void updateTextures() { // new void

        TODO5 remove 4

        for (int i = 0; i < TEXTUREWIDTH; i++) {

            int[] textureHandle = new int[16];
            for (int j = 0; j < 16; j++) {


                GLES20.glGenTextures(1, textureHandle, j);

                try {

                    if (textureHandle[j] != 0) {
                        // Bind to the texture in OpenGL
                        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[j]);

                        // Set filtering
                        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

                        // Load the bitmap into the bound texture.
                        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, MATERIALLIST[i][j], 0);
                    }
                } catch (Exception e) {
                    Log.d("Load Texture:", "" + e);
                }
                if (textureHandle[0] == 0) {
                    throw new RuntimeException("Error loading texture.");
                }
                if (j == 15) {
                    Textures[i] = textureHandle;
                }
            }

        }
    }/**/

/*/
    public void updateTexturesNormals() {
        //TODO0 remove 4

        for (int i = 0; i < TEXTUREWIDTH; i++) {

            int[] textureHandle = new int[16];
            for (int j = 0; j < 16; j++) {

                GLES20.glGenTextures(1, textureHandle, j);

                try {

                    if (textureHandle[j] != 0) {
                        // Bind to the texture in OpenGL
                        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[j]);

                        // Set filtering
                        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

                        // Load the bitmap into the bound texture.
                        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, MATERIALNORMALS[i][j], 0);
                    }
                } catch (Exception e) {
                    Log.d("Load Texture:", "" + e);
                }
                if (textureHandle[0] == 0) {
                    throw new RuntimeException("Error loading texture.");
                }
                if (j == 15) {
                    TextureNormals[i] = textureHandle;
                }
            }

        }
    }/**/

    int Textures[][] = new int[TEXTUREWIDTH * TEXTUREWIDTH][15];

    int TextureNormals[][] = new int[TEXTUREWIDTH * TEXTUREWIDTH][15];

    public int getTexture(int ID, int Materialint) {

        try {
            return Textures[Materialint][ID];
        } catch (Exception e) {

        }
        return 0;
        /**/
    }

    public int getTextureNormals(int ID, int Materialint) {

        try {
            return TextureNormals[Materialint][ID];
        } catch (Exception e) {

        }
        return 0;
        /**/
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

        SrcDst = new Rect(0, 0, TILESIZE, TILESIZE);
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

        SrcDst = new Rect(0, 0, TILESIZE, TILESIZE);
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

        for (int i = 0; i < FinishedTilemap.length-1; i++) {
            FinishedTilemap[i] = OverlayTilemap(FinishedTilemap[15], FinishedTilemap[i]);
        }

        MATERIALLISTUPDATING[Material] = FinishedTilemap; // updates all Tiles from a specific Material
    }

    /**
     * Transforms a bmp[] with 5 Tiles into one with 15 Tiles
     * @param SingleTilemap The 5 Tiles as Input
     * @return The 15 Finished Tiles
     */
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


    //TODO Create accurate normals
    public void CreateNormals(int intMaterial, int Layer1, int Layer2, int Layer3, int alpha1, int alpha2, int alpha3) {

        Bitmap[] SingleTilemap = new Bitmap[15]; // Tiles from 0 to 15 as Bitmap

        SrcDst = new Rect(0, 0, TILESIZE, TILESIZE);
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

            if (i == 0) {
                paint.setColor(Color.rgb(127, 127, 255)); // equals 0 0 1
                //paint.setColor(Color.rgb(0,0,0)); // equals 0 0 1
                Currentcanvas.drawRect(new Rect(0, 0, TILESIZEORIGINAL, TILESIZEORIGINAL), paint);
            }

            for (int j = TILESIZEORIGINAL; j < 2 * TILESIZEORIGINAL; j++) {
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

                        ColorLayerUP += (int) (1/*((Alpha / 255.0f))*/ * Color.red(Layer.getPixel(jrelative, (k - 1) % TILESIZEORIGINAL)));// takes the colorvalue of the three bitmaps at the same point

                        ColorLayerDOWN += (int) (1/*((Alpha / 255.0f))*/ * Color.red(Layer.getPixel(jrelative, (k + 1) % TILESIZEORIGINAL)));// takes the colorvalue of the three bitmaps at the same point

                        ColorLayerLEFT += (int) (1/*((Alpha / 255.0f))*/ * Color.red(Layer.getPixel((j - 1) % TILESIZEORIGINAL, krelative)));// takes the colorvalue of the three bitmaps at the same point

                        ColorLayerRIGHT += (int) (1/*((Alpha / 255.0f))*/ * Color.red(Layer.getPixel((j + 1) % TILESIZEORIGINAL, krelative)));// takes the colorvalue of the three bitmaps at the same point
                    }


                    Vector NormalVec;

                    //NormalVec = (NormalVec.normalize()).multiplydouble(255);

                    float Scale = alpha1 + alpha2 + alpha3;

                    float Hor = (float) (ColorLayerLEFT - ColorLayerRIGHT) / Scale; // value from 0 to 1
                    float Ver = (float) (ColorLayerUP - ColorLayerDOWN) / Scale; // value from 0 to 1



                    Vector vb = /*/new Vector(Hor, 0, Sca).normalize();/*/new Vector(NORMALSTRENGTH, 0, Hor).normalize();/**/
                    Vector va = /*/new Vector(0, Ver, Sca).normalize();/*/new Vector(0, NORMALSTRENGTH, Ver).normalize();/**/

                    //va = new Vector(Hor, 0,Sca).normalize();
                    //vb = new Vector(0, Ver,Sca).normalize();
                    NormalVec = ((va.cross(vb)).normalize()).negate();

                    NormalVec = new Vector(NormalVec.getX(), NormalVec.getY(), Math.abs(NormalVec.getZ())).normalize();

                    int red = (int) (/**/255 - /**/((NormalVec.getX() + 1) * 127));
                    int green = (int) (/**/255 - /**/(NormalVec.getY() + 1) * 127);
                    int blue = (int) ((NormalVec.getZ() + 1) * 127);

                    //blue = 127;

                    paint.setColor(Color.rgb(red, green, blue));


                    //Currentcanvas.drawRect(j%TILESIZEORIGINAL, k%TILESIZEORIGINAL,j%TILESIZEORIGINAL+1,k%TILESIZEORIGINAL+1, paint);
                    if ((red < 123 || red > 130) && (green < 123 || green > 130)/* && (blue < 123 || blue > 130)*/) {
                        Currentcanvas.drawPoint(j % TILESIZEORIGINAL, k % TILESIZEORIGINAL, paint);
                    }
                }
            }

            SingleTilemap[i] = Current;
        }


        Bitmap[] FinishedTilemap = getFinnishedBitmaplist(SingleTilemap);

        for (int i = 0; i < FinishedTilemap.length; i++) {
            FinishedTilemap[i] = OverlayTilemap(FinishedTilemap[FinishedTilemap.length-1], FinishedTilemap[i]);
        }

        MATERIALNORMALS[intMaterial] = FinishedTilemap;
    }


    /**
     * Creates a Bitmaplist with 5 bmps for one Layer ony
     *
     * @param Layer    The chosen LayerBitmap like  5 = brick wall or 3 = sand,...
     * @param alpha    The chosen Opacity
     * @param Position The original Layer = 1, 2 or 3
     * @return
     */
    public Bitmap[] createBitmapOfSingleKind(int Position, int Layer, int alpha) {
        Bitmap[] BitmapArry = new Bitmap[TILESOFSINGLEKIND];

        Paint paint = new Paint();
        paint.setAlpha(alpha);


        for (int i = 0; i < TILESOFSINGLEKIND; i++) {
            //Create Bitmap
            Bitmap newLayer = Bitmap.createBitmap(TILESIZE, TILESIZE, Bitmap.Config.ARGB_8888);
            Canvas Currentcanvas = new Canvas();
            Currentcanvas.setBitmap(newLayer);


            if (Position == 0) {
                Currentcanvas.drawBitmap(RGBTilesLAYER1.get(Layer * TILESOFSINGLEKIND + i), 0, 0, paint);
            } else if (Position == 1) {
                Currentcanvas.drawBitmap(RGBTilesLAYER2.get(Layer * TILESOFSINGLEKIND + i), 0, 0, paint);
            } else {
                Currentcanvas.drawBitmap(RGBTilesLAYER3.get(Layer * TILESOFSINGLEKIND + i), 0, 0, paint);
            }
            BitmapArry[i] = newLayer;
        }

        return BitmapArry;
    }

    public Bitmap returnNormalBmp(int ID, Bitmap Layer1, Bitmap Layer2, Bitmap Layer3, int alpha1, int alpha2, int alpha3) {

        Paint paint = new Paint();

        Bitmap Current = Bitmap.createBitmap(TILESIZE, TILESIZE, Bitmap.Config.ARGB_8888);
        Canvas Currentcanvas = new Canvas();
        Currentcanvas.setBitmap(Current);

        if (ID == 15) {
            paint.setColor(Color.rgb(127, 127, 255)); // equals 0 0 1
            //paint.setColor(Color.rgb(0,0,0)); // equals 0 0 1
            Currentcanvas.drawRect(new Rect(0, 0, TILESIZEORIGINAL, TILESIZEORIGINAL), paint);
        }

        for (int j = TILESIZEORIGINAL; j < 2 * TILESIZEORIGINAL; j++) {
            for (int k = TILESIZEORIGINAL; k < 2 * TILESIZEORIGINAL; k++) {


                int ColorLayerUP = 0;
                int ColorLayerDOWN = 0;
                int ColorLayerLEFT = 0;
                int ColorLayerRIGHT = 0;

                Bitmap Layer;

                for (int l = 0; l < 3; l++) {

                    if (l == 0) {
                        Layer = Layer1;
                    } else if (l == 1) {
                        Layer = Layer2;
                    } else {
                        Layer = Layer3;
                    }

                    int jrelative = j % TILESIZEORIGINAL;
                    int krelative = k % TILESIZEORIGINAL;

                    ColorLayerUP += Color.red(Layer.getPixel(jrelative, (k - 1) % TILESIZEORIGINAL));// takes the colorvalue of the three bitmaps at the same point

                    ColorLayerDOWN += Color.red(Layer.getPixel(jrelative, (k + 1) % TILESIZEORIGINAL));// takes the colorvalue of the three bitmaps at the same point

                    ColorLayerLEFT += Color.red(Layer.getPixel((j - 1) % TILESIZEORIGINAL, krelative));// takes the colorvalue of the three bitmaps at the same point

                    ColorLayerRIGHT += Color.red(Layer.getPixel((j + 1) % TILESIZEORIGINAL, krelative));// takes the colorvalue of the three bitmaps at the same point
                }


                Vector NormalVec;

                float Scale = alpha1 + alpha2 + alpha3;

                float Hor = (float) (ColorLayerLEFT - ColorLayerRIGHT) / Scale; // value from 0 to 1
                float Ver = (float) (ColorLayerUP - ColorLayerDOWN) / Scale; // value from 0 to 1



                Vector vb = new Vector(NORMALSTRENGTH, 0, Hor).normalize();/**/
                Vector va = new Vector(0, NORMALSTRENGTH, Ver).normalize();/**/

                NormalVec = ((va.cross(vb)).normalize()).negate();

                NormalVec = new Vector(NormalVec.getX(), NormalVec.getY(), Math.abs(NormalVec.getZ())).normalize();

                int red = (int) (255 - ((NormalVec.getX() + 1) * 127));
                int green = (int) (255 - (NormalVec.getY() + 1) * 127);
                int blue = (int) ((NormalVec.getZ() + 1) * 127);

                //blue = 127;

                paint.setColor(Color.rgb(red, green, blue));


                //Currentcanvas.drawRect(j%TILESIZEORIGINAL, k%TILESIZEORIGINAL,j%TILESIZEORIGINAL+1,k%TILESIZEORIGINAL+1, paint);
                if ((red < 123 || red > 130) && (green < 123 || green > 130)/* && (blue < 123 || blue > 130)*/) {
                    Currentcanvas.drawPoint(j % TILESIZEORIGINAL, k % TILESIZEORIGINAL, paint);
                }
            }
        }
        return Current;
    }

    //TODO Create accurate normals
    public void CreateAccurateNormals(int intMaterial, int Layer1, int Layer2, int Layer3, int alpha1, int alpha2, int alpha3) {

        Bitmap[] SingleTilemap = new Bitmap[16]; // Tiles from 0 to 15 as Bitmap

        Bitmap[][] GreyTilemaps = new Bitmap[3][16];

        for (int i = 0; i < 3; i++) {

            Bitmap[] FinishedTilemap;

            if (i == 0) {
                FinishedTilemap = createBitmapOfSingleKind(i, Layer1, alpha1); // length of 5, only the basic Tiles
                int a = 0;
            } else if (i == 1) {
                FinishedTilemap = createBitmapOfSingleKind(i, Layer2, alpha2); // length of 5, only the basic Tiles
            } else {
                FinishedTilemap = createBitmapOfSingleKind(i, Layer3, alpha3); // length of 5, only the basic Tiles
            }

            FinishedTilemap = getFinnishedBitmaplist(FinishedTilemap);
            int b = 0;

            for (int j = 0; j < FinishedTilemap.length; j++) {
                FinishedTilemap[j] = OverlayTilemap(FinishedTilemap[FinishedTilemap.length-1], FinishedTilemap[j]);
            }
            GreyTilemaps[i] = FinishedTilemap;
        }

        for (int i = 0; i < 16; i++) {
            SingleTilemap[i] = returnNormalBmp(i, GreyTilemaps[0][i], GreyTilemaps[1][i], GreyTilemaps[2][i], alpha1, alpha2, alpha3);
        }
        int a = 0;
        for (int j = 0; j < SingleTilemap.length-1; j++) {
            SingleTilemap[j] = OverlayTilemap(SingleTilemap[15]/*SingleTilemap[SingleTilemap.length-1]*/, SingleTilemap[j]);
        }


        MATERIALNORMALS[intMaterial] = SingleTilemap;
    }
}

