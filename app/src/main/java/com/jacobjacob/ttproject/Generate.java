package com.jacobjacob.ttproject;

import com.jacobjacob.ttproject.Tile.Tile;

import java.util.ArrayList;

import static com.jacobjacob.ttproject.Util.FRAMES;
import static com.jacobjacob.ttproject.Util.NOISE;
import static com.jacobjacob.ttproject.Util.TILESIZE;
import static com.jacobjacob.ttproject.Util.camera;

public class Generate {

    public Generate() {
    }


    public ArrayList<TileColor> GenerateTerrain() {
        ArrayList<TileColor> Generated = new ArrayList<>();

        int nOutputWidth = 30;
        int nOutputHeight = 30;
        long a = System.currentTimeMillis();
        int TileX = (int) (+camera.getEye2D().getValue(0) / TILESIZE) - nOutputWidth / 2; // later to get Tilescaling right
        int TileY = (int) (+camera.getEye2D().getValue(1) / TILESIZE) - nOutputHeight / 2; // later to get Tilescaling right

        int Level = 25;
        float Depth;
        float ScaleX = /*/(float) (FRAMES * 0.2)/*/ 1/**/;
        float ScaleY = /*/(float) (FRAMES * 0.2)/*/ 1/**/;
        float ScaleT = 1;

        //NOISE.setSeed(FRAMES); // changes the SEED


        for (int i = 0; i < nOutputWidth; i++) {

            int xold = (int) (TileX + i - nOutputWidth / 2/**/);

            int x = (int)((camera.getEye().getValue(0)/TILESIZE) + ScaleX * (i-nOutputWidth/2));
            int xplace = (int) (TileX +/*ScaleX * */i/**/ - nOutputWidth / 2/**/);

            x = xplace;
            x = (int)((camera.getEye2D().getValue(0)/TILESIZE) + ScaleX * (i-(nOutputWidth/2)));

            for (int j = 0; j < nOutputHeight; j++) {


                //int y = (int) (TileY + /*ScaleY * */j/**/ - nOutputHeight / 2/**/);
                int y = (int)((camera.getEye().getValue(1)/TILESIZE) + ScaleY * (j-nOutputHeight/2));
                int yplace = (int) (TileY + j - nOutputHeight / 2/**/);

                y = (int)((camera.getEye2D().getValue(1)/TILESIZE) + (ScaleY * (j-(nOutputHeight/2))));

                //y = yplace;

                float N = (float) NOISE.eval(/*ScaleX * */x * 0.5,  /*ScaleY * */y * 0.5, ScaleT * FRAMES * 0.4); // between -1 and 1
                float N2 = (float) NOISE.eval(/*ScaleX * */x * 0.1,  /*ScaleY * */y * 0.1, ScaleT * FRAMES * 0.04); // between -1 and 1
                float N3 = (float) NOISE.eval(/*ScaleX * */x * 0.05, /*ScaleY * */y * 0.05, ScaleT * FRAMES * 0.004);

                float N4 = (float) NOISE.eval(/*ScaleX * */x * 0.01, /*ScaleY * */y * 0.01, ScaleT * FRAMES * 0.001); // water
                float N5 = (float) NOISE.eval(/*ScaleX * */x * 0.2,  /*ScaleY * */y * 0.2, ScaleT * FRAMES * 0.01); // water2

                /**/
                N = (N + 1) / 2;
                N2 = (N2 + 1) / 2;
                N3 = (N3 + 1) / 2;
                N4 = (N4 + 1) / 2;
                N5 = (N5 + 1) / 2;

                //N4 = (float)((N4+N5*0.5)/(1.5));
                float N4A = N4;
                N4 *= N5;
                N4 = ((N4 + N4A) / 2);

                N3 *= N3; // mountains

                N2 = (float) ((N4 * 1.5 + N * 0.1 + N2 * 0.5 + N3) * (1 / 3.1)); // range 0 to 1

                /*
                N2 *= 255;
                N2 = (int)N2/25; // 0 to 255
                N2 *= 25;*/


                //N2 *= 100;
                //N2 = (int)N2/Level; // 0 to 255
                N2 = (int) (N2 * Level);
                N2 /= Level;
                Depth = N2;
                N2 *= 255;


                //N2 = (int)(1000 * N2)/(Level);
                //N2 *= Level/100; // (N2/Level)
                //N2 = N2/100;
                //N2 *= 255; //0 to 1 to rgb

                /**/
                //N = 255 * N1;

                /**/
                //N = (int) ((1 + N) * 127.5); // between 0 and 255

                int R = (int) N2;//(N2 * N4);
                int G = R;
                int B = R;
                /**/
                if (0.25 > N4) {
                    if (N2 < 200) {
                        //R = //(int) N2+55;
                        //G = //(int)(255 - (255-N2));
                        //B = (int)N2 + 55;
                        if (N2 < 120) {
                            R = (int) (150/* * N4*/);
                            G = (int) (N2/* * N4*/);
                            B = G;
                            if (N2 < 100) {
                                R = (int) (120/* * N4*/);
                                G = (int) (N2/* * N4*/);
                                B = (int) (G/* * N4*/);
                                if (N2 < 70) {
                                    R = (int) (90/* * N4*/);//110
                                    G = (int) (150/* * N4*/);
                                    B = (int) (N2/* * N4*/ * 1.5);
                                    if (N2 < 60) {
                                        R = (int) (255/* * N4*/);//210
                                        G = (int) (220/* * N4*/);
                                        B = (int) (N2/* * N4*/ + 10);
                                        if (N2 < 50) {
                                            B = (int) (150/* * N4*/);
                                            G = (int) (75/* * N4*/);//(int) N2;
                                            R = (int) (N2/* * N4*/ + 10);//G;
                                            if (N2 < 30) {
                                                R = (int) (N2/* * N4*/);
                                                G = (int) (45/* * N4*/);
                                                B = (int) (130/* * N4*/);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }/**/ else {
                    R = 40;
                    G = (int) (N4 * 255);
                    B = (int) (2 * N4 * 255);
                }
                /**/


                /*/
                if (0.6 < N1){
                    G = 220;
                    R = (int)N;
                    B = R;
                    if (255 * 0.6 < N){
                        R = 255;
                        G = 255;
                        B = 255;
                    }
                    if (N < 255/3){
                        R = 0;
                        G = 0;
                        B = 255;
                    }
                }
                if(N1 < 0.3) {
                    R = 220;
                    G = (int)N;
                    B = G;
                }/**/

                /*/
                if (200 < N && N <= 255) {
                    R = 140;
                    G = 100;
                    B = 100;
                }
                if (140 < N && N <= 200) {
                    R = 120;
                    G = 140;
                    B = 75;
                }
                if (100 < N && N <= 140) { // new ColorClass for Terrain/...
                    G = 180;
                    R = 50;
                    B = 50;
                }
                if (70 < N & N <= 100) {
                    B = 130;
                    R = 70;
                    B = 70;
                }
                if (0 < N && N <= 70) {
                    R = 0;
                    G = 0;
                    B = 130;
                }
                if (140 < N && N < 160) {
                    R = 140;
                    G = 200;
                    B = 140;
                }


                /**/


                /*/
                if (20 < N && N < 50 || 80 < N && N < 100 || 130 < N && N < 150 || 180 < N && N < 210){
                    R = 0;
                    G = 0;
                    B = 0;
                    if (130 < N && N < 150){
                        R = 255;
                    }
                    if (80 < N && N < 100){
                        B = 255;
                    }
                }else {
                    R = 255;
                    G = 255;
                    B = 255;
                }

                /**/

                // N = ID

                R = (int) (Depth*255);//(N2 * N4);
                G = R;
                B = R;

                Generated.add(new TileColor(new Vector(xplace + nOutputWidth / 2, yplace + nOutputHeight / 2),Depth, R, G, B));
            }
        }
        long b = System.currentTimeMillis() - a;
        //12s for 6400 Tiles // 17ms for 900 // ca 40ms 2500 // 50ms 2500
        return Generated;
    }

    public ArrayList<Tile> getTerrainTiles() {
        ArrayList<Tile> Generated = new ArrayList<>();

        int nOutputWidth = 16;
        int nOutputHeight = 16;

        int TileX = (int) (+camera.getEye2D().getValue(0) / TILESIZE) - nOutputWidth / 2;
        int TileY = (int) (+camera.getEye2D().getValue(1) / TILESIZE) - nOutputHeight / 2;

        for (int i = 0; i < nOutputWidth; i++) {
            for (int j = 0; j < nOutputHeight; j++) {

                int x = TileX + i/* - nOutputWidth / 2*/;
                int y = TileY + j/* - nOutputHeight / 2*/;

                float N = (float) NOISE.eval(x, y, FRAMES * 0.1); // between -1 and 1

                N = (int) ((1 + N) * 127.5); // between 0 and 255

                if (150<N && N < 200) { // Coloring rules could apply and change the Material of the Tile
                    Tile Add = new Tile(new Vector(x, y), (int) N);
                    //Add.setAutotile(true);
                    Generated.add(Add);
                }
            }
        }
        return Generated;
    }








    public ArrayList<Tile> getTerrainTiles2() {
        ArrayList<Tile> Generated = new ArrayList<>();

        int nOutputWidth = 30;
        int nOutputHeight = 30;
        long a = System.currentTimeMillis();
        int TileX = (int) (+camera.getEye2D().getValue(0) / TILESIZE) - nOutputWidth / 2; // later to get Tilescaling right
        int TileY = (int) (+camera.getEye2D().getValue(1) / TILESIZE) - nOutputHeight / 2; // later to get Tilescaling right

        int Level = 25;
        float ScaleX = /*/(float) (FRAMES * 0.2)/*/ 1/**/;
        float ScaleY = /*/(float) (FRAMES * 0.2)/*/ 1/**/;
        float ScaleT = 1;

        //NOISE.setSeed(FRAMES); // changes the SEED


        for (int i = 0; i < nOutputWidth; i++) {

            int xold = (int) (TileX + i - nOutputWidth / 2/**/);

            int x = (int)((camera.getEye().getValue(0)/TILESIZE) + ScaleX * (i-nOutputWidth/2));
            int xplace = (int) (TileX +/*ScaleX * */i/**/ - nOutputWidth / 2/**/);

            x = xplace;
            x = (int)((camera.getEye2D().getValue(0)/TILESIZE) + ScaleX * (i-(nOutputWidth/2)));

            for (int j = 0; j < nOutputHeight; j++) {


                //int y = (int) (TileY + /*ScaleY * */j/**/ - nOutputHeight / 2/**/);
                int y = (int)((camera.getEye().getValue(1)/TILESIZE) + ScaleY * (j-nOutputHeight/2));
                int yplace = (int) (TileY + j - nOutputHeight / 2/**/);

                y = (int)((camera.getEye2D().getValue(1)/TILESIZE) + (ScaleY * (j-(nOutputHeight/2))));

                //y = yplace;

                float N = (float) NOISE.eval(/*ScaleX * */x * 0.5,  /*ScaleY * */y * 0.5, ScaleT * FRAMES * 0.4); // between -1 and 1
                float N2 = (float) NOISE.eval(/*ScaleX * */x * 0.1,  /*ScaleY * */y * 0.1, ScaleT * FRAMES * 0.04); // between -1 and 1
                float N3 = (float) NOISE.eval(/*ScaleX * */x * 0.05, /*ScaleY * */y * 0.05, ScaleT * FRAMES * 0.004);

                float N4 = (float) NOISE.eval(/*ScaleX * */x * 0.01, /*ScaleY * */y * 0.01, ScaleT * FRAMES * 0.001); // water
                float N5 = (float) NOISE.eval(/*ScaleX * */x * 0.2,  /*ScaleY * */y * 0.2, ScaleT * FRAMES * 0.01); // water2

                /**/
                N = (N + 1) / 2;
                N2 = (N2 + 1) / 2;
                N3 = (N3 + 1) / 2;
                N4 = (N4 + 1) / 2;
                N5 = (N5 + 1) / 2;

                //N4 = (float)((N4+N5*0.5)/(1.5));
                float N4A = N4;
                N4 *= N5;
                N4 = ((N4 + N4A) / 2);

                N3 *= N3; // mountains

                N2 = (float) ((N4 * 1.5 + N * 0.1 + N2 * 0.5 + N3) * (1 / 3.1)); // range 0 to 1

                N2 = (int) (N2 * Level);
                N2 /= Level;
                N2 *= 255;

                int R = (int) N2;//(N2 * N4);
                int G = R;
                int B = R;
                /**/

                Tile Add = new Tile(new Vector(xplace + nOutputWidth / 2, yplace + nOutputHeight / 2), (int) N);
                if (0.25 > N4) {
                    if (N2 < 200) {
                        //R = //(int) N2+55;
                        //G = //(int)(255 - (255-N2));
                        //B = (int)N2 + 55;

                        Add.setMaterial(7);
                        if (N2 < 120) {
                            R = (int) (150/* * N4*/);
                            G = (int) (N2/* * N4*/);
                            B = G;

                            Add.setMaterial(6);
                            if (N2 < 100) {
                                R = (int) (120/* * N4*/);
                                G = (int) (N2/* * N4*/);
                                B = (int) (G/* * N4*/);

                                Add.setMaterial(5);
                                if (N2 < 70) {
                                    R = (int) (90/* * N4*/);//110
                                    G = (int) (150/* * N4*/);
                                    B = (int) (N2/* * N4*/ * 1.5);

                                    Add.setMaterial(4);
                                    if (N2 < 60) {
                                        R = (int) (255/* * N4*/);//210
                                        G = (int) (220/* * N4*/);
                                        B = (int) (N2/* * N4*/ + 10);

                                        Add.setMaterial(3);
                                        if (N2 < 50) {
                                            B = (int) (150/* * N4*/);
                                            G = (int) (75/* * N4*/);//(int) N2;
                                            R = (int) (N2/* * N4*/ + 10);//G;

                                            Add.setMaterial(2);
                                            if (N2 < 30) {
                                                R = (int) (N2/* * N4*/);
                                                G = (int) (45/* * N4*/);
                                                B = (int) (130/* * N4*/);
                                                Add.setMaterial(1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }/**/ else {
                    R = 40;
                    G = (int) (N4 * 255);
                    B = (int) (2 * N4 * 255);

                    Add.setMaterial(3);
                }

                //Add.setAutotile(true);
                Generated.add(Add);
            }
        }
        return Generated;
    }

}
