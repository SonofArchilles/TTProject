package com.jacobjacob.ttproject.Savefile;

import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;
import static com.jacobjacob.ttproject.Util.CONTEXT;
import static com.jacobjacob.ttproject.Util.DISPAYTOAST;
import static com.jacobjacob.ttproject.Util.FILE_NAME;
import static com.jacobjacob.ttproject.Util.SETTINGS_OPENGL;
import static com.jacobjacob.ttproject.Util.SETTINS_NAME;

public class WriteFile {

    public void WriteFile(String text) {

        //String text = "new save file complete!!";


        FileOutputStream fos = null;

        try {

            fos = CONTEXT.openFileOutput(SETTINS_NAME, MODE_PRIVATE);

            fos.write(text.getBytes());
            //Toast.makeText(CONTEXT, "Saved to: " + CONTEXT.getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
            if (DISPAYTOAST) {
                Toast.makeText(CONTEXT, "SUCCESS: " + FILE_NAME + " saved", Toast.LENGTH_LONG).show();
            }
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

    public void SaveSettings() {


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
