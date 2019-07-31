package com.jacobjacob.ttproject;

import android.graphics.Color;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.jacobjacob.ttproject.Util.CONTEXT;
import static com.jacobjacob.ttproject.Util.DISPAYTOAST;
import static com.jacobjacob.ttproject.Util.FILE_NAME;
import static com.jacobjacob.ttproject.Util.FRAMETIME;
import static com.jacobjacob.ttproject.Util.FRAMETIMESTART;
import static com.jacobjacob.ttproject.Util.KDTREE;
import static com.jacobjacob.ttproject.Util.KDTREECOPY;
import static com.jacobjacob.ttproject.Util.KDTREECURRENTLYBUILDING;
import static com.jacobjacob.ttproject.Util.MATERIALARRAY;
import static com.jacobjacob.ttproject.Util.STARTINGMATERIAL;
import static com.jacobjacob.ttproject.Util.TEXTUREWIDTH;
import static com.jacobjacob.ttproject.Util.UPDATEVIEW;


public class ReadFile {

    public void ReadFile() { // String Filename
        FileInputStream fis = null;

        try {
            fis = CONTEXT.openFileInput(FILE_NAME);

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            //Toast.makeText(CONTEXT, "Sucess!! : " + CONTEXT.getFilesDir() + "/" + FILE_NAME + " loadet", Toast.LENGTH_LONG).show();
            if (DISPAYTOAST) {
                Toast.makeText(CONTEXT, sb.toString(), Toast.LENGTH_LONG).show();
            }
            //mEditText.setText(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void ReadFileTiles() { // String Filename


        UPDATEVIEW = false;

        ArrayList<Tile> AllTiles = new ArrayList<>();

        KDTREE = new KdTree();//reset();
        MATERIALARRAY = new Material[TEXTUREWIDTH * TEXTUREWIDTH]; // reset
        int Materialint = 0;

        boolean readMaterial = false;
        boolean readMaterialAnimations = false;

        FileInputStream fis = null;

        try {
            fis = CONTEXT.openFileInput(FILE_NAME);

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");

                String[] parts = text.split(" ");

                if (String.valueOf(parts[0]).equals("m")) {
                    readMaterial = true;
                }
                if (String.valueOf(parts[0]).equals("a")) {
                    readMaterial = false;
                    readMaterialAnimations = true;
                }

                if (!readMaterial && !readMaterialAnimations) {

                    Tile newTile = new Tile(new Vector(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                    if (parts.length > 4) {
                        newTile.setMaterial(Integer.parseInt(parts[4]));
                        if (parts.length > 5) {
                            newTile.setStarttime(Integer.parseInt(parts[5]));
                        }
                    }
                    AllTiles.add(newTile);
                }
                if (readMaterial){

                    if (!String.valueOf(parts[0]).equals("m")) {

                        MATERIALARRAY[Materialint] = new Material(Integer.parseInt(parts[0]), // number
                                Integer.parseInt(parts[1]), // layer 1
                                Integer.parseInt(parts[2]), // layer 2
                                Integer.parseInt(parts[3]), // layer 3

                                Integer.parseInt(parts[4]), // color int 1
                                Integer.parseInt(parts[5]), // color int 2
                                Integer.parseInt(parts[6])); // color int 3
                        Materialint++;
                    }
                }


                if (readMaterialAnimations){

                    if (!String.valueOf(parts[0]).equals("a") && !String.valueOf(parts[0]).equals(" ")) {
                        int[][] Animation = new int[31][3];
                        if (MATERIALARRAY[Integer.parseInt(parts[0])] != null){
                            for (int i = 0; i < (parts.length-2)/4; i++){
                                Animation[Integer.parseInt(parts[i*4+2])][0] = Integer.parseInt(parts[i*4+3]);
                                Animation[Integer.parseInt(parts[i*4+2])][1] = Integer.parseInt(parts[i*4+4]);
                                Animation[Integer.parseInt(parts[i*4+2])][2] = Integer.parseInt(parts[i*4+5]);
                            }
                        }else {
                            MATERIALARRAY[Integer.parseInt(parts[0])] = STARTINGMATERIAL;
                            for (int i = 0; i < (parts.length-1)/4; i++){
                                Animation[i*4+2][0] = Integer.parseInt(parts[i*4+3]);
                                Animation[i*4+2][1] = Integer.parseInt(parts[i*4+4]);
                                Animation[i*4+2][2] = Integer.parseInt(parts[i*4+5]);
                            }
                        }

                        MATERIALARRAY[Integer.parseInt(parts[0])].LoadAnimation(Animation);
                        MATERIALARRAY[Integer.parseInt(parts[0])].setAnimationtime(Integer.parseInt(parts[1]));
                        //set Animationtime
                    }
                }
            }
            //Toast.makeText(CONTEXT, "Success!! : " + CONTEXT.getFilesDir() + "/" + FILE_NAME + " loaded", Toast.LENGTH_LONG).show();
            //Toast.makeText(CONTEXT, sb.toString(), Toast.LENGTH_LONG).show();
            if (DISPAYTOAST) {
                Toast.makeText(CONTEXT, "SUCCESS: " + FILE_NAME + " loaded", Toast.LENGTH_LONG).show();
            }
            //mEditText.setText(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        FRAMETIMESTART = System.currentTimeMillis();

        KDTREE = new KdTree();
        KDTREE.setTilesInCurrentTree(AllTiles); // 16ms
        KDTREE.CreatenewKDTree();


        KDTREECOPY = new KdTree();
        KDTREECOPY = KDTREE;


        FRAMETIME = System.currentTimeMillis() - FRAMETIMESTART; //41ms with 20 iterations // 23 with 5 iterations // 102 with unlimited Iterations and 6467 Tiles // now 770
        KDTREECURRENTLYBUILDING = false;

        //if (MATERIALARRAY.length < 1) {
        //    MATERIALARRAY[0] = STARTINGMATERIAL;
        //}
        for (int i = 0; i < MATERIALARRAY.length; i++){
            if (MATERIALARRAY[i] == null){
                Material NewMaterial = STARTINGMATERIAL;
                int Number = i;
                NewMaterial.setNumber(Number);
                MATERIALARRAY[i] = NewMaterial;
            }else {
                if (MATERIALARRAY[i].getNumber() != i){
                    MATERIALARRAY[i].setNumber(i);
                }
            }


            MATERIALARRAY[i].UpdateMaterialTileset();
        }
        UPDATEVIEW = true;

        //int a = 6;
    }
}
