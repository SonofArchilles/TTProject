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


    public static CustomButtons Joystick,MoveUp,MoveDown,TOGGLELEVEL,TOGGLEOPENGL,TOGGLEHITBOX,TOGGLEDRAWKDTREE,SEEKBARTESTRED,SEEKBARTESTGREEN,SEEKBARTESTBLUE,SEEKBARSELECTMATERIAL;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public MainSurfaceView(Context context) {
        super(context);

        CONTEXT = context;


        boolean[] BoolsToChange = new boolean[1];

        Joystick = new CustomButtons("Joystick","", new Vector(0.2 * WIDTHSCREEN * 0.5, HEIGHTSCREEN * 0.8), (float) (WIDTHSCREEN * 0.18), (float) (WIDTHSCREEN * 0.18), Color.rgb(120, 120, 120));

        MoveDown = new CustomButtons("Button","MoveDown", new Vector(WIDTHSCREEN * 0.22f, HEIGHTSCREEN * 0.88f), (float) (WIDTHSCREEN * 0.05f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(120, 120, 120));
        MoveUp = new CustomButtons("Button","MoveUp", new Vector(WIDTHSCREEN * 0.22f, HEIGHTSCREEN * 0.72f), (float) (WIDTHSCREEN * 0.05f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(120, 120, 120));


        TOGGLELEVEL = new CustomButtons("ToggleButton","SELECTLVL", new Vector(WIDTHSCREEN * 0.3f, HEIGHTSCREEN * 0.72f), (float) (WIDTHSCREEN * 0.1f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(140, 120, 120));
        TOGGLEOPENGL = new CustomButtons("ToggleButton","OPENGL", new Vector(WIDTHSCREEN * 0.3f, HEIGHTSCREEN * 0.88f), (float) (WIDTHSCREEN * 0.1f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(140, 120, 120));
        TOGGLEHITBOX = new CustomButtons("ToggleButton","DRAWHITBOX", new Vector(WIDTHSCREEN * 0.3f, HEIGHTSCREEN * 0.88f), (float) (WIDTHSCREEN * 0.1f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(140, 120, 120));
        TOGGLEDRAWKDTREE = new CustomButtons("ToggleButton","DRAWKDTREE", new Vector(WIDTHSCREEN * 0.3f, HEIGHTSCREEN * 0.88f), (float) (WIDTHSCREEN * 0.1f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(140, 120, 120));



        SEEKBARTESTRED = new CustomButtons("Seekbar","RED", new Vector(WIDTHSCREEN * 0.88f, HEIGHTSCREEN * 0.5f), (float) (WIDTHSCREEN * 0.05f), (float) (WIDTHSCREEN * 0.3f), Color.rgb(255, 0, 0));
        SEEKBARTESTGREEN = new CustomButtons("Seekbar","GREEN", new Vector(WIDTHSCREEN * 0.82f, HEIGHTSCREEN * 0.5f), (float) (WIDTHSCREEN * 0.05f), (float) (WIDTHSCREEN * 0.3f), Color.rgb(0, 255, 0));
        SEEKBARTESTBLUE = new CustomButtons("Seekbar","BLUE", new Vector(WIDTHSCREEN * 0.95f, HEIGHTSCREEN * 0.5f), (float) (WIDTHSCREEN * 0.05f), (float) (WIDTHSCREEN * 0.3f), Color.rgb(0, 0, 255));

        SEEKBARSELECTMATERIAL = new CustomButtons("Seekbar","SELECTEDMATERIAL", new Vector(WIDTHSCREEN * 0.52f, HEIGHTSCREEN * 0.88f), (float) (WIDTHSCREEN * 0.3f), (float) (WIDTHSCREEN * 0.05f), Color.rgb(170, 170, 170));


        float DistStandard = WIDTHSCREEN * 0.015f; // Distance between the two Buttons


        //MoveUp.TopToTopOf(Joystick,0);

        //MoveUp.BottomToTopOf(MoveDown,0);
        //MoveDown.TopToBottomOf(MoveUp,DistStandard);

        Joystick.LeftToLeftOfScreen(0);
        Joystick.BottomToBottomOfScreen(0);

        MoveDown.LeftToRightOf(Joystick,DistStandard);
        MoveDown.BottomToBottomOf(Joystick,0);

        MoveUp.LeftToRightOf(Joystick,DistStandard);
        MoveUp.BottomToTopOf(MoveDown,DistStandard);

        TOGGLELEVEL.LeftToRightOf(MoveUp,DistStandard);
        TOGGLELEVEL.TopToTopOf(MoveUp,0);

        TOGGLEOPENGL.LeftToRightOf(MoveDown,DistStandard);
        TOGGLEOPENGL.BottomToBottomOf(MoveDown,0);


        SEEKBARTESTBLUE.BottomToBottomOf(TOGGLEOPENGL,0);

        SEEKBARTESTGREEN.TopToTopOf(SEEKBARTESTBLUE,0);
        SEEKBARTESTGREEN.RightToLeftOf(SEEKBARTESTBLUE,DistStandard);


        SEEKBARTESTRED.TopToTopOf(SEEKBARTESTGREEN,0);
        SEEKBARTESTRED.RightToLeftOf(SEEKBARTESTGREEN,DistStandard);



        SEEKBARSELECTMATERIAL.LeftToRightOf(TOGGLEOPENGL,DistStandard);
        SEEKBARSELECTMATERIAL.TopToTopOf(TOGGLEOPENGL,0);

        TOGGLEHITBOX.TopToTopOfScreen(DistStandard);
        TOGGLEHITBOX.RightToRightOfScreen(DistStandard);

        TOGGLEDRAWKDTREE.RightToLeftOf(TOGGLEHITBOX,DistStandard);
        TOGGLEDRAWKDTREE.TopToTopOf(TOGGLEHITBOX,0);

        //SELECTOPENGL.FlipOrientation();


        CUSTOMBUTTONSLIST.add(Joystick);
        CUSTOMBUTTONSLIST.add(MoveUp);
        CUSTOMBUTTONSLIST.add(MoveDown);

        CUSTOMBUTTONSLIST.add(TOGGLEOPENGL);
        CUSTOMBUTTONSLIST.add(TOGGLELEVEL);
        CUSTOMBUTTONSLIST.add(TOGGLEHITBOX);
        CUSTOMBUTTONSLIST.add(TOGGLEDRAWKDTREE);

        CUSTOMBUTTONSLIST.add(SEEKBARTESTRED);
        CUSTOMBUTTONSLIST.add(SEEKBARTESTGREEN);
        CUSTOMBUTTONSLIST.add(SEEKBARTESTBLUE);
        CUSTOMBUTTONSLIST.add(SEEKBARSELECTMATERIAL);

    }
}

