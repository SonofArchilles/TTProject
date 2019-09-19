package com.jacobjacob.ttproject.Savefile;

import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;
import static com.jacobjacob.ttproject.Util.*;

public class WriteFile {

    /**
     * Saves the String with all the Tile information inside the lvl a to e .txt files
     *
     * @param text all the Tiles, Materials and Animations
     */
    public void WriteFile(String text) {

        //String text = "new save file complete!!";


        FileOutputStream fos = null;

        try {

            fos = CONTEXT.openFileOutput(FILE_NAMES.get(LEVELINT), MODE_PRIVATE);

            OutputStreamWriter outputWriter = new OutputStreamWriter(fos);
            outputWriter.write(text);
            outputWriter.close();

            //fos.write(text.getBytes());

            //Toast.makeText(CONTEXT, "Saved to: " + CONTEXT.getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
            if (DISPAYTOAST) {
                Toast.makeText(CONTEXT, "SUCCESS: " + FILE_NAME + " saved", Toast.LENGTH_LONG).show();
            }
            Log.d("WriteFile", "Tiles Saved!");
            Log.d("Savefile", text);

        } catch (FileNotFoundException e) {
            Log.d("Write", "Fail 1");
            e.printStackTrace();
            Log.d("Write", "Fail 2");
        } catch (IOException e) {
            Log.d("Write", "Fail 3");
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.d("Write", "Fail 4");
                    e.printStackTrace();
                }
            }
        }
        //System.out.print(text); /SAVING WORKS !!!
    }

    public void SaveSettings() {

        //TODO Enums for simpler Saving and loading
        String text = "";
        text += "OPENGL " + String.valueOf(SETTINGS_OPENGL) + "\n";

        FileOutputStream fos = null;

        try {
            fos = CONTEXT.openFileOutput(SETTINS_NAME, MODE_PRIVATE);

            fos.write(text.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //System.out.print(text); /SAVING WORKS !!!
    }


}
