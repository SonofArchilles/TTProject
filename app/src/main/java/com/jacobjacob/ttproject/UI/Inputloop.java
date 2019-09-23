package com.jacobjacob.ttproject.UI;

import android.util.Log;

import com.jacobjacob.ttproject.Tile.Tile;

import java.util.ArrayList;

import static com.jacobjacob.ttproject.Util.*;


public class Inputloop {

    public Inputloop() {
    }

    ArrayList<Tile> visible = new ArrayList<>();
    ArrayList<Integer> animations = new ArrayList<>();


    public void start() {
        while (UIRUNNING) {
            if (UIUPDATING) {
                UIUPDATING = false; // makes sure the thread only updates when a frame is drawn
                VISIBLETILES = visible;
                AnimationsToUpdate = animations;

                //TODO remove slow loading edges

                animations = new ArrayList<>();

                for (int i = 0; i < visible.size();i++){
                    if (MATERIALARRAY[visible.get(i).getMaterial()].hasAnimation() && !animations.contains(visible.get(i).getMaterial())){
                        animations.add(visible.get(i).getMaterial());//MATERIALARRAY[i].getNumber());
                    }
                }
                for (int i = 0; i < animations.size();i++){
                    MATERIALARRAY[animations.get(i)].UpdateTileset();
                    MATERIALLIST[animations.get(i)] = MATERIALLISTUPDATING[animations.get(i)];
                }



                if (!RELOADMATERIALS) {
                    for (int i = 0; i < MATERIALARRAY.length; i++) { //TODO Updates the Texture if the Material has an Animation in a different, parallel Thread!
                        try {
                            if (RELOADMATERIALS){

                                MATERIALARRAY[i].CreateMaterialTileset();

                                //MATERIALARRAY[i].UpdateTileset();
                                MATERIALARRAY[i].CreateMaterialTilesetNormal();

                                MATERIALLIST[i] = MATERIALLISTUPDATING[i];


                                //MATERIALNORMALS[i] = MATERIALNORMALSUPDATING[i];
                            }
                        } catch (Exception e) {

                        }
                    }
                }

                if (RELOADMATERIALS) {
                    UPDATERELOADEDMATERIALS = true;
                    RELOADMATERIALS = false;
                }


                if (TOUCHSTATE == 0) {
                    TOUCHCUSTOMBUTTONS = false; // Doesen't update the inventory or buttonplacement if not necessary
                    ActionDown();
                } else if (TOUCHSTATE == 1) {
                    ActionMove();
                } else if (TOUCHSTATE == 2) {
                    ActionUp();
                }


                visible = KDTREE.getVisibleTilesInCurrentTree();

            }
        }
    }

    public void ActionDown() {
        for (int i = 0; i < CUSTOMBUTTONSLIST.size(); i++) {
            CUSTOMBUTTONSLIST.get(i).UpdateButton();
        }
        UpdateTouch();

    }

    public void ActionMove() {
        for (int i = 0; i < CUSTOMBUTTONSLIST.size(); i++) {
            CUSTOMBUTTONSLIST.get(i).UpdateButton();
        }
        UpdateTouch();


    }

    public void ActionUp() {
        for (int i = 0; i < CUSTOMBUTTONSLIST.size(); i++) {
            CUSTOMBUTTONSLIST.get(i).UpdateButtonUp();
        }
        UpdateTouchUp();
    }


    //TODO UpdateTouchMove
    public void UpdateTouch() {
        if (!TOUCHCUSTOMBUTTONS) {
            if (DISPLAYINVENTORY) {
                LE.setSelectTilefromInventory((float) TOUCHPOSITION.getX(), (float) TOUCHPOSITION.getY());
            } else {

                if (FILLTILES) { // Starts the Boundarie to place many Tiles
                    LE.StartFillingTile((int) TOUCHPOSITION.getX(), (int) TOUCHPOSITION.getY());
                }
                if (!DESTROYTILES) { // Removes many Tiles
                    if (PLACETILE) {
                        LE.PlaceSelectedTile((int) TOUCHPOSITION.getX(), (int) TOUCHPOSITION.getY());
                    }
                } else {
                    if (PLACETILE) { // removes single Tile
                        LE.RemoveTileKDTREE((int) TOUCHPOSITION.getX(), (int) TOUCHPOSITION.getY());
                    }
                }

            }
        }
    }

    public void UpdateTouchUp() {
        if (!TOUCHCUSTOMBUTTONS) {
            if (DISPLAYINVENTORY) {
                LE.SELECTEDTILEIDINVENTORY();
            } else {
                if (FILLTILES) {
                    if (!DESTROYTILES) {
                        LE.FillingTiles((int) TOUCHPOSITION.getX(), (int) TOUCHPOSITION.getY());
                    } else {
                        LE.RemoveTilesKDTREE((int) TOUCHPOSITION.getX(), (int) TOUCHPOSITION.getY());
                    }
                }
            }
        }
    }
}
