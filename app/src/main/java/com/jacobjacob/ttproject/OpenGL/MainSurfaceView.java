package com.jacobjacob.ttproject.OpenGL;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.jacobjacob.ttproject.Input.CustomButtons;
import com.jacobjacob.ttproject.OpenGL.Shapes.RawModel;
import com.jacobjacob.ttproject.Vector;

import java.nio.FloatBuffer;

import static com.jacobjacob.ttproject.Util.CONTEXT;
import static com.jacobjacob.ttproject.Util.CUSTOMBUTTONSLIST;
import static com.jacobjacob.ttproject.Util.HEIGHTSCREEN;
import static com.jacobjacob.ttproject.Util.WIDTHSCREEN;

public class MainSurfaceView extends GLSurfaceView {

    public static CustomButtons Joystick;
    public static CustomButtons MoveUp;
    public static CustomButtons MoveDown;
    public static CustomButtons SELECTLEVEL;
    public static CustomButtons SELECTOPENGL;
    public static FloatBuffer vertexBuffer;

    public static RawModel model;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public MainSurfaceView(Context context) {
        super(context);

        CONTEXT = context;

        Joystick = new CustomButtons(0, new Vector(0.2 * WIDTHSCREEN * 0.5, HEIGHTSCREEN * 0.8), (float) (WIDTHSCREEN * 0.15), (float) (WIDTHSCREEN * 0.15), Color.rgb(120, 120, 120));

        MoveDown = new CustomButtons(1, new Vector(WIDTHSCREEN * 0.22f, HEIGHTSCREEN * 0.88f), (float) (WIDTHSCREEN * 0.05f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(120, 120, 120));
        MoveUp = new CustomButtons(2, new Vector(WIDTHSCREEN * 0.22f, HEIGHTSCREEN * 0.72f), (float) (WIDTHSCREEN * 0.05f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(120, 120, 120));

        SELECTLEVEL = new CustomButtons(3, new Vector(WIDTHSCREEN * 0.3f, HEIGHTSCREEN * 0.72f), (float) (WIDTHSCREEN * 0.1f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(140, 120, 120));

        SELECTOPENGL = new CustomButtons(4, new Vector(WIDTHSCREEN * 0.3f, HEIGHTSCREEN * 0.88f), (float) (WIDTHSCREEN * 0.1f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(140, 120, 120));

        CUSTOMBUTTONSLIST.add(Joystick);
        CUSTOMBUTTONSLIST.add(MoveUp);
        CUSTOMBUTTONSLIST.add(MoveDown);
        CUSTOMBUTTONSLIST.add(SELECTLEVEL);
        CUSTOMBUTTONSLIST.add(SELECTOPENGL);

    }
}

