package com.jacobjacob.ttproject.OpenGL;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.jacobjacob.ttproject.UI.CustomButtons;
import com.jacobjacob.ttproject.Vector;

import static com.jacobjacob.ttproject.Util.*;

public class MainSurfaceView extends GLSurfaceView {

    public static CustomButtons Joystick,MoveUp,MoveDown,SELECTLEVEL,SELECTOPENGL,SEEKBARTEST,SEEKBARTEST2,SEEKBARTEST3;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public MainSurfaceView(Context context) {
        super(context);

        CONTEXT = context;


        boolean[] BoolsToChange = new boolean[1];

        Joystick = new CustomButtons("Joystick","", new Vector(0.2 * WIDTHSCREEN * 0.5, HEIGHTSCREEN * 0.8), (float) (WIDTHSCREEN * 0.15), (float) (WIDTHSCREEN * 0.15), Color.rgb(120, 120, 120));

        MoveDown = new CustomButtons("Button","MoveDown", new Vector(WIDTHSCREEN * 0.22f, HEIGHTSCREEN * 0.88f), (float) (WIDTHSCREEN * 0.05f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(120, 120, 120));
        MoveUp = new CustomButtons("Button","MoveUp", new Vector(WIDTHSCREEN * 0.22f, HEIGHTSCREEN * 0.72f), (float) (WIDTHSCREEN * 0.05f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(120, 120, 120));

        SELECTLEVEL = new CustomButtons("ToggleButton","SELECTLVL", new Vector(WIDTHSCREEN * 0.3f, HEIGHTSCREEN * 0.72f), (float) (WIDTHSCREEN * 0.1f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(140, 120, 120));



        SELECTOPENGL = new CustomButtons("ToggleButton","OPENGL", new Vector(WIDTHSCREEN * 0.3f, HEIGHTSCREEN * 0.88f), (float) (WIDTHSCREEN * 0.1f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(140, 120, 120));

        SEEKBARTEST = new CustomButtons("Seekbar","", new Vector(WIDTHSCREEN * 0.88f, HEIGHTSCREEN * 0.5f), (float) (WIDTHSCREEN * 0.05f), (float) (WIDTHSCREEN * 0.3f), Color.rgb(255, 0, 0));
        SEEKBARTEST3 = new CustomButtons("Seekbar","", new Vector(WIDTHSCREEN * 0.82f, HEIGHTSCREEN * 0.5f), (float) (WIDTHSCREEN * 0.05f), (float) (WIDTHSCREEN * 0.3f), Color.rgb(0, 0, 255));

        SEEKBARTEST2 = new CustomButtons("Seekbar","", new Vector(WIDTHSCREEN * 0.52f, HEIGHTSCREEN * 0.88f), (float) (WIDTHSCREEN * 0.3f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(0, 255, 0));

        float DistStandard = WIDTHSCREEN * 0.005f;


        MoveUp.TopToTopOf(Joystick,0);

        MoveDown.LeftToRightOf(Joystick,DistStandard);
        MoveUp.LeftToRightOf(Joystick,DistStandard);


        SELECTLEVEL.TopToTopOf(MoveUp,0);
        SELECTLEVEL.LeftToRightOf(MoveUp,DistStandard);

        MoveDown.BottomToBottomOf(Joystick,0);
        SELECTOPENGL.LeftToRightOf(MoveDown,DistStandard);
        SELECTOPENGL.BottomToBottomOf(MoveDown,0);

        SEEKBARTEST2.TopToTopOf(SELECTOPENGL,0);
        SEEKBARTEST2.LeftToRightOf(SELECTOPENGL,DistStandard);

        SEEKBARTEST3.RightToLeftOf(SEEKBARTEST,DistStandard);
        SEEKBARTEST.BottomToBottomOf(SEEKBARTEST2,0);
        SEEKBARTEST3.TopToTopOf(SEEKBARTEST,0);


        CUSTOMBUTTONSLIST.add(Joystick);
        CUSTOMBUTTONSLIST.add(MoveUp);
        CUSTOMBUTTONSLIST.add(MoveDown);
        CUSTOMBUTTONSLIST.add(SELECTLEVEL);
        CUSTOMBUTTONSLIST.add(SELECTOPENGL);
        CUSTOMBUTTONSLIST.add(SEEKBARTEST);
        CUSTOMBUTTONSLIST.add(SEEKBARTEST2);
        CUSTOMBUTTONSLIST.add(SEEKBARTEST3);

    }
}

