package com.jacobjacob.ttproject.Input;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.jacobjacob.ttproject.OpenGL.Shapes.Square;
import com.jacobjacob.ttproject.Vector;

import static com.jacobjacob.ttproject.Util.CHUNKCOLOR;
import static com.jacobjacob.ttproject.Util.COLORDEBUG;
import static com.jacobjacob.ttproject.Util.FILE_NAME;
import static com.jacobjacob.ttproject.Util.FILE_NAMES;
import static com.jacobjacob.ttproject.Util.FILLTILECOLOR;
import static com.jacobjacob.ttproject.Util.HEIGHTSCREEN;
import static com.jacobjacob.ttproject.Util.LE;
import static com.jacobjacob.ttproject.Util.LEVELINT;
import static com.jacobjacob.ttproject.Util.MOVE;
import static com.jacobjacob.ttproject.Util.SETTINGS_OPENGL;
import static com.jacobjacob.ttproject.Util.TILESIZE;
import static com.jacobjacob.ttproject.Util.TOUCHCUSTOMBUTTONS;
import static com.jacobjacob.ttproject.Util.TOUCHPOSITION;
import static com.jacobjacob.ttproject.Util.TOUCHSTATE;
import static com.jacobjacob.ttproject.Util.WF;
import static com.jacobjacob.ttproject.Util.WIDTHSCREEN;
import static com.jacobjacob.ttproject.Util.camera;
import static com.jacobjacob.ttproject.Util.movespeed;

public class CustomButtons {

    Vector Position;
    float Width, Height;
    String Text;
    int Type, color = Color.rgb(255, 0, 0);
    Rect Box;
    Square BoundingBox;

    float left, right, top, bottom;

    //TODO Button implementations and Seekbar code for r g and b as well as a and the animationtime

    /**
     * Create a new Button with a Position, a Width and a Height as well as a Type
     *
     * @param Type     either a joystick or a button
     * @param Position The Position in Screenspacecoordinates and is directly at the center of the shape, could get inverted if OpenGl coordinates kick in
     * @param Width    total width from left to right of the shape
     * @param Height   total height from top to bottom
     * @param Colorint The color of the Button
     */
    public CustomButtons(int Type, Vector Position, float Width, float Height, int Colorint) {
        this.Position = Position;
        this.Width = Width;
        this.Height = Height;
        this.Type = Type;
        this.color = Colorint;

        float left = (float) (this.Position.getX() - (this.Width / 2));
        float right = (float) (this.Position.getX() + (this.Width / 2));
        float top = (float) (this.Position.getY() - (this.Height / 2));
        float bottom = (float) (this.Position.getY() + (this.Height / 2));

        this.Box = new Rect((int) (left), (int) (top), (int) (right), (int) (bottom));

        this.BoundingBox = new Square(new Vector(left, top), new Vector(right, top), new Vector(right, bottom), new Vector(left, bottom), Color.rgb(255, 0, 150));

        this.left = 2 * ((left / WIDTHSCREEN) * 2 - 1);
        this.right = 2 * ((right / WIDTHSCREEN) * 2 - 1);
        this.top = -2 * ((top / HEIGHTSCREEN) * 2 - 1);
        this.bottom = -2 * ((bottom / HEIGHTSCREEN) * 2 - 1);
    }

    /**
     * Create a new Button with a Position, a Width and a Height as well as a Type
     *
     * @param Type     either a joystick or a button
     * @param Position The Position in Screenspacecoordinates and is directly at the center of the shape, could get inverted if OpenGl coordinates kick in
     * @param Width    total width from left to right of the shape
     * @param Height   total height from top to bottom
     * @param Text     The Text displayed on top of the button
     * @param Colorint The color of the Button
     */
    public CustomButtons(int Type, Vector Position, float Width, float Height, String Text, int Colorint) {
        this.Position = Position;
        this.Width = Width;
        this.Height = Height;
        this.Type = Type;
        this.color = Colorint;
        this.Box = new Rect((int) (this.Position.getX() - (this.Width / 2)), (int) (this.Position.getY() - (this.Height / 2)), (int) (this.Position.getX() + (this.Width / 2)), (int) (this.Position.getY() + (this.Height / 2)));
    }

    //TODO Better Buttons class. Maybe an Interface

    /**
     * Joystickcode to Control the Cameras x and y Values
     */
    public void CustomJoyStick() {
        Vector Movementvalue = this.Position.subtract(TOUCHPOSITION);
        float MaxMovementDist = (float) (Math.sqrt(this.Width * this.Width + this.Height * this.Height) / 2); // is int Screen/ Pixelspace, might be too much
        //MaxMovementDist = 1;

        MaxMovementDist = HEIGHTSCREEN;

        if (Movementvalue.length() > MaxMovementDist) {
            Movementvalue = (Movementvalue.normalize()).multiplydouble(MaxMovementDist);
        }

        Movementvalue.setZ((float) camera.getEye2D().getZ()); // offset!!


        int r = (int) (255 * (1 - Math.sin(Math.abs(Movementvalue.normalize().getX()))));
        int g = (int) (255 * (1 - Math.sin(Math.abs(Movementvalue.normalize().getY()))));

        COLORDEBUG = Color.rgb(r, g, 0);

        float lenghtMiddleTouch = (float) (this.Position.subtract(TOUCHPOSITION)).length();
        float lenghtMiddleVertex = (float) Math.sqrt(this.Width * this.Width + this.Height * this.Height) / 2;

        Movementvalue.setZ(0);
        Movementvalue = Movementvalue.normalize().negate().multiplydouble(TILESIZE);

        float Strengthmovement = lenghtMiddleTouch / lenghtMiddleVertex;

        Movementvalue = Movementvalue.multiplydouble(Strengthmovement);

        Log.d("Position:", "X: " + (Movementvalue.getX()) + " Y: " + (Movementvalue.getY()) + " Z: " + (Movementvalue.getZ() + " Color: " + COLORDEBUG));

        MOVE.Move(Movementvalue);
    }

    public void MoveUPDOWN() {
        if (this.Type == 1) {
            camera.move2D(new Vector(0, 0, movespeed / 5)); // moves the Camera down
        } else {
            camera.move2D(new Vector(0, 0, -movespeed / 5)); // moves the Camera up
        }
    }

    public void SelectLVL() { // only adds one to the counter and loads the next level
        LEVELINT++;
        if (FILE_NAMES.size() <= LEVELINT) {
            LEVELINT = 0;
        }
        LE.SaveLevel();

        FILE_NAME = FILE_NAMES.get(LEVELINT);//adapterView.getSelectedItem().toString();

        LE.LoadLevel();


        //TILETEXTURE.deleteTextures();
        //TILETEXTURE.deleteNormals();


        //for(int i = 0; i < 10; i++){ // 10 number of updated Materials
        //    TILETEXTURE.UpdateMaterialTexture(i);
        //    TILETEXTURE.UpdateMaterialNormalTexture(i);
        //}


        //TILETEXTURE.updateTextures();        // working // reloads all the important Textures into opengl
        //TILETEXTURE.updateTexturesNormals(); // working // reloads all the important Textures into opengl
    }

    /**
     * Takes the Type int into account and then runs the buttonspecific Code
     */
    public void UpdateButton() {
        if (this.Box.contains((int) TOUCHPOSITION.getValue(0), (int) TOUCHPOSITION.getValue(1))) {
            TOUCHCUSTOMBUTTONS = true;
            if (this.Type == 0) {
                CustomJoyStick();
            }
            if (this.Type == 1 || this.Type == 2) {
                MoveUPDOWN();
            }
            if (this.Type == 3) {
                if (TOUCHSTATE == 0) { // if the touch is down, not if moving or up
                    SelectLVL();
                }
            }
            if (this.Type == 4) {
                if (TOUCHSTATE == 0) {

                    SETTINGS_OPENGL = !SETTINGS_OPENGL;
                    if (SETTINGS_OPENGL) {
                        this.color = FILLTILECOLOR; // Green
                    } else {
                        this.color = CHUNKCOLOR; // Red
                    }
                    WF.SaveSettings();

                }
            }
        }
    }

    /**
     * Draws the boundingbox with a solid, previously given color
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void draw() {
        BoundingBox.draw();
    }

    public void CustomToggleButton() {

    }

    /**
     * Returns a Rectangle in Screenspace for the Boundingbox of the Button
     *
     * @return Rect left, top, right, bottom
     */
    public Rect getBox() {
        return this.Box;
    }

    public float getLeft() {
        return this.left;
    }

    public float getRight() {
        return this.right;
    }

    public float getTop() {
        return this.top;
    }

    public float getBottom() {
        return this.bottom;
    }

    public int getColor() {
        return this.color;
    }
}
