package com.jacobjacob.ttproject;

import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;
import static com.jacobjacob.ttproject.Util.CONTEXT;
import static com.jacobjacob.ttproject.Util.DISPAYTOAST;
import static com.jacobjacob.ttproject.Util.FILE_NAME;

public class WriteFile {

    public void WriteFile(String text/*,String SAVEFILENAME*/) {

        //String text = "new save file complete!!";


        FileOutputStream fos = null;

        try {

            fos = CONTEXT.openFileOutput(FILE_NAME, MODE_PRIVATE);

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


}
