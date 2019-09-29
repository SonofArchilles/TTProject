package com.jacobjacob.ttproject.Debug;

import android.util.Log;

import com.jacobjacob.ttproject.Tile.Tile;

import java.util.ArrayList;

import static com.jacobjacob.ttproject.Util.*;

public class Debug {
    public Debug() {

    }


    public void TilesToClippboard(){
        setClipboard(LvlToString());
    }

    private void setClipboard( String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) CONTEXT.getSystemService(CONTEXT.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) CONTEXT.getSystemService(CONTEXT.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * Returns the String of the current Lvl in a savable and readable state with the Tiles in the KD-Tree, the Materials and the Animations
     * @return
     */
    private String LvlToString(){

        StringBuilder Tiles = new StringBuilder();


        ArrayList<Tile> AllTiles = KDTREECHUNKS.getTilesInCurrentTree();

        Log.d("Debug LvlToString","Number of Tiles to save: " + String.valueOf(AllTiles.size()));


        for (int i = 0; i < AllTiles.size(); i++) {
            Tile thisTile = AllTiles.get(i);
            String currentTile = (int) thisTile.getPositionRAW().getValue(0) + " " + (int) thisTile.getPositionRAW().getValue(1) + " " + (int) thisTile.getIDint() + " " + (int) thisTile.getFrames() + " " + (int) thisTile.getMaterial() + " " + (int) thisTile.getStarttime();
            Tiles.append(currentTile + "\n");
        }

        Tiles.append("m\n");
        for (int i = 0; i < MATERIALARRAY.length; i++) {
            if (MATERIALARRAY[i] != null) {
                Tiles.append(MATERIALARRAY[i].getMaterialdetails() + "\n");
            } else {
                Tiles.append(STARTINGMATERIAL.getMaterialdetails() + "\n");
            }
        }

        Tiles.append("a\n");
        for (int i = 0; i < MATERIALARRAY.length; i++) {
            if (MATERIALARRAY[i] != null && MATERIALARRAY[i].hasAnimation()) {
                Tiles.append(MATERIALARRAY[i].getAnimationdetails() + "\n");
            }
        }

        return String.valueOf(Tiles);
    }
}