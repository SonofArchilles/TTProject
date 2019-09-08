package com.jacobjacob.ttproject;

import android.util.Log;

import static com.jacobjacob.ttproject.Util.DRAWKDTREEBOOL;
import static com.jacobjacob.ttproject.Util.FRAMEDRAWN;
import static com.jacobjacob.ttproject.Util.KDTREECURRENTLYBUILDING;
import static com.jacobjacob.ttproject.Util.LASTUPDATETIME;
import static com.jacobjacob.ttproject.Util.MATERIALARRAY;
import static com.jacobjacob.ttproject.Util.MATERIALLIST;
import static com.jacobjacob.ttproject.Util.MATERIALLISTUPDATING;
import static com.jacobjacob.ttproject.Util.RENDERERTILES;
import static com.jacobjacob.ttproject.Util.RUNNING;
import static com.jacobjacob.ttproject.Util.TILELAYER;
import static com.jacobjacob.ttproject.Util.TILELAYERSTART;
import static com.jacobjacob.ttproject.Util.TILESIZE;
import static com.jacobjacob.ttproject.Util.UPDATEVIEW;
import static com.jacobjacob.ttproject.Util.camera;
import static com.jacobjacob.ttproject.Util.movespeed;

public class UpdateHandler/* extends AppCompatActivity*/ {

    static com.jacobjacob.ttproject.Old.Rasterizer Rasterize;
    //public static RenderTiles RenderTiles;
    static boolean UPDATEBITMAP;
    //boolean check = true;


    UpdateHandler() {
        Rasterize = new com.jacobjacob.ttproject.Old.Rasterizer();
        RENDERERTILES = new RenderTiles();
        movespeed = 2 * TILESIZE / 3;
    }

    public void updateScreen() {


        //updateTiles();

        RENDERERTILES.initializeRenderTiles();

        //RenderTiles.DrawAndGenerateTiles(); // Generates the ColorTiles and generates the mao

        //RenderTiles.DrawHitbox();

        RENDERERTILES.DrawKDTreeTiles();

        //if (!KDTREECURRENTLYBUILDING) {
        //RenderTiles.DrawPortalTiles();
        //}


        if (DRAWKDTREEBOOL) {
            RENDERERTILES.DrawKDTree(); // Draws the KD-Tree in Red to Green
        }

        RENDERERTILES.DrawSelectedTile(); /** Draws the Inventory to the left or top*/
        try {

            RENDERERTILES.DrawSelectedTileLayer();
        } catch (Exception e) {
            Log.d("Renderer: ", "Draw Tile Layer" + e);
        }
        RENDERERTILES.DrawInventoryTiles(); /**Inventory to select IDs**/

        RENDERERTILES.postImage();
    }

    void run() {
        int FRAMES = 0;
        long FRAMESSTARTTIME = System.currentTimeMillis();

        while (RUNNING) {
            //UPDATEVIEW = true;
            if (UPDATEVIEW) {
                if (/*System.currentTimeMillis() - LASTUPDATETIME >= MINUPDATETIME ||/**/true) { // the minimum ammount of Time has passed, you may now update the Screen
                    //long Starttime = System.currentTimeMillis();
                    if (FRAMEDRAWN && !KDTREECURRENTLYBUILDING) {
                        //thisIMAGE.findViewById(R.id.SCREEN);
                        camera.UpdateEye2D();
                        FRAMEDRAWN = false;
                        updateScreen();

                        //FRAME++;
                        //if (FRAME > FPS) {
                        //    FRAME = 0;
                        //}

                        //FRAMES++;
                        //Log.d("New Frame", String.valueOf(FRAME));
                        //Log.d("Time in S:", String.valueOf(System.currentTimeMillis() / 1000));
                        //long timeforframe = System.currentTimeMillis() - LASTUPDATETIME; // ca 40 to 60ms per Frame
                        LASTUPDATETIME = System.currentTimeMillis();

                        FRAMEDRAWN = true;
                        UPDATEBITMAP = !UPDATEBITMAP;
                        //TILETEXTURE;
                        FRAMES++;

                    }
                }
                if (System.currentTimeMillis() - FRAMESSTARTTIME >= 1000) {
                    FRAMESSTARTTIME = System.currentTimeMillis();
                    Log.d("FPS:", String.valueOf(FRAMES));
                    FRAMES = 0;
                }
            }
        }
    }

    void runTileUpdates() {
        boolean UPDATEBITMAP2 = false;
        while (RUNNING) {
            if (UPDATEBITMAP != UPDATEBITMAP2 && FRAMEDRAWN && UPDATEVIEW /*&& TILELAYER == 0*/) {
                for (int i = 0; i < MATERIALARRAY.length; i++) {
                    if (MATERIALARRAY[i] != null) {
                        if ((MATERIALARRAY[i].hasAnimation() && (TILELAYER > 3 + TILELAYERSTART) || MATERIALARRAY[i].showNormal())) {
                            MATERIALARRAY[i].UpdateTileset();
                            MATERIALLIST[i] = MATERIALLISTUPDATING[i];
                        }
                    }
                }
                UPDATEBITMAP2 = UPDATEBITMAP;
            }
        }
    }
}
