package com.jacobjacob.ttproject.UI;

import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import com.jacobjacob.ttproject.Debug.Debug;
import com.jacobjacob.ttproject.Vector;

import java.util.ArrayList;

import static com.jacobjacob.ttproject.Util.*;

public class CustomButtons {

    Vector Position;
    float Width, Height;
    String Text;
    int Type, color = Color.rgb(120, 120, 120), color2;
    Rect Box;
    Rect BoxDraw;
    boolean ButtonUpdated = false;

    ArrayList<Rect> Boxes = new ArrayList<>();
    ArrayList<Integer> Colors = new ArrayList<>();
    private int Accuracy = 128;


    float left, right, top, bottom;

    //TODO Button implementations and Seekbar code for r g and b as well as a and the animationtime

    //TODO implement Text to compare it to variables and change them with either Progress = float, Bool or Vector for joystick
    //TODO buttonsnapping ==> Tell Button to get Placed to the right, left, top or Bottom side of one or more Buttons


    boolean[] booleansToChange = new boolean[1];

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
        if (Type != 5) {
            this.color = Colorint;
        }
        float left = (float) (this.Position.getX() - (this.Width / 2));
        float right = (float) (this.Position.getX() + (this.Width / 2));
        float top = (float) (this.Position.getY() - (this.Height / 2));
        float bottom = (float) (this.Position.getY() + (this.Height / 2));

        this.Box = new Rect((int) (left), (int) (top), (int) (right), (int) (bottom));


        this.left = 2 * ((left / WIDTHSCREEN) * 2 - 1);
        this.top = -2 * ((top / HEIGHTSCREEN) * 2 - 1);
        this.right = 2 * ((right / WIDTHSCREEN) * 2 - 1);
        this.bottom = -2 * ((bottom / HEIGHTSCREEN) * 2 - 1);


        this.BoxDraw = new Rect((int) (this.left * this.Accuracy), (int) (this.top * this.Accuracy), (int) (this.right * this.Accuracy), (int) (this.bottom * this.Accuracy));
        this.Colors.add(this.color);
        this.color2 = Colorint;
        this.Colors.add(this.color2);
        this.Boxes.add(this.BoxDraw);
    }


    /**
     * Create a new Button with a Position, a Width and a Height as well as a Type
     *
     * @param Type             either a joystick or a button
     * @param Position         The Position in Screenspacecoordinates and is directly at the center of the shape, could get inverted if OpenGl coordinates kick in
     * @param Width            total width from left to right of the shape
     * @param Height           total height from top to bottom
     * @param Colorint         The color of the Button
     * @param BooleansToChange The Bool to change inside a List with the lenght of 1
     */
    public CustomButtons(int Type, Vector Position, float Width, float Height, int Colorint, boolean[] BooleansToChange) {
        this.Position = Position;
        this.Width = Width;
        this.Height = Height;
        this.Type = Type;
        if (Type != 5) {
            this.color = Colorint;
        }

        float left = (float) (this.Position.getX() - (this.Width / 2));
        float top = (float) (this.Position.getY() - (this.Height / 2));
        float right = (float) (this.Position.getX() + (this.Width / 2));
        float bottom = (float) (this.Position.getY() + (this.Height / 2));

        this.Box = new Rect((int) (left), (int) (top), (int) (right), (int) (bottom));


        this.left = 2 * ((left / WIDTHSCREEN) * 2 - 1);
        this.top = -2 * ((top / HEIGHTSCREEN) * 2 - 1);
        this.right = 2 * ((right / WIDTHSCREEN) * 2 - 1);
        this.bottom = -2 * ((bottom / HEIGHTSCREEN) * 2 - 1);

        this.booleansToChange = BooleansToChange;


        this.BoxDraw = new Rect((int) (this.left * this.Accuracy), (int) (this.top * this.Accuracy), (int) (this.right * this.Accuracy), (int) (this.bottom * this.Accuracy));
        this.Colors.add(this.color);
        this.color2 = Colorint;
        this.Colors.add(this.color2);
        this.Boxes.add(this.BoxDraw);
    }


    //TODO Better Buttons class. Maybe an Interface

    /**
     * Joystickcode to Control the Cameras x and y Values
     */
    public void CustomJoyStick() { // TODO use this code to make a seekbar that changes a given variable
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

    float Progress;

    public void CustomSeekbar() { // return the Progress

        Vector PositionTouch = (TOUCHPOSITION.subtract(this.Position.subtract(new Vector(this.Width / 2, this.Height / 2)))).multiply(new Vector(1 / this.Width, 1 / Height)); // Top left - Position of Touch


        Rect SeekBox = new Rect();
        float Scale;
        if (this.Width > this.Height) {
            this.Progress = (float) PositionTouch.getX();
            Scale = -(this.BoxDraw.right - this.BoxDraw.left) * (1 - this.Progress);
            SeekBox = new Rect(this.BoxDraw.left, this.BoxDraw.top, (int) (this.BoxDraw.right + Scale), this.BoxDraw.bottom);
        } else {
            this.Progress = (float) (1 - PositionTouch.getY());
            Scale = (-this.BoxDraw.top + this.BoxDraw.bottom) * (1 - this.Progress);
            SeekBox = new Rect(this.BoxDraw.left, (int) (this.BoxDraw.top + Scale), this.BoxDraw.right, this.BoxDraw.bottom);
        }


        if (Color.red(this.color2) == 255) {
            CUSTOM_BUTTON_SEEKBAR_RED = this.Progress;
        } else if (Color.green(this.color2) == 255) {
            CUSTOM_BUTTON_SEEKBAR_GREEN = this.Progress;
        } else if (Color.blue(this.color2) == 255) {
            CUSTOM_BUTTON_SEEKBAR_BLUE = this.Progress;
        }


        if (this.Boxes.size() > 1) {
            this.Boxes.set(1, SeekBox);
            this.Colors.set(1, Color.rgb((int) (this.Progress * Color.red(color2)), (int) (this.Progress * Color.green(color2)), (int) (Progress * Color.blue(color2))));
        } else {
            this.Boxes.add(1, SeekBox);
            this.Colors.add(1, Color.rgb((int) (this.Progress * Color.red(color2)), (int) (this.Progress * Color.green(color2)), (int) (Progress * Color.blue(color2))));

        }
    }

    public void MoveUPDOWN() {
        if (this.Type == 1) {
            camera.move2D(new Vector(0, 0, movespeed / 5)); // moves the Camera down
        } else {
            camera.move2D(new Vector(0, 0, -movespeed / 5)); // moves the Camera up
        }
    }

    /**
     * Loops through lvl a to e and saves the previous one and loads the next one
     */
    public void SelectLVL() { // only adds one to the counter and loads the next level

        LE.SaveLevel();


        LEVELINT++;
        if (FILE_NAMES.size() <= LEVELINT) {
            LEVELINT = 0;
        }
        FILE_NAME = FILE_NAMES.get(LEVELINT);//adapterView.getSelectedItem().toString();

        LE.LoadLevel();
    }

    //TODO easier creation of Toggle Buttons with enums!

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
                if (TOUCHSTATE == 0 && !this.ButtonUpdated) { // if the touch is down, not if moving or up
                    this.ButtonUpdated = true;

                    SelectLVL();

                    RELOADMATERIALS = true;

                }
            }
            if (this.Type == 4) {
                if (TOUCHSTATE == 0 && !this.ButtonUpdated) {
                    this.ButtonUpdated = true;

                    //TODO find a way to pass the bool variables into the class and change them inside
                    SETTINGS_OPENGL = !SETTINGS_OPENGL;

                    if (SETTINGS_OPENGL) {
                        this.Colors.set(0, FILLTILECOLOR);

                        Debug newDebug = new Debug();
                        newDebug.TilesToClippboard();





                    } else {
                        this.Colors.set(0, CHUNKCOLOR);
                    }
                    WF.SaveSettings();

                }
            }

            if (this.Type == 5) { // Seekbar
                CustomSeekbar();
            }

        }
    }

    public void UpdateButtonUp() {
        if (TOUCHSTATE == 2 && this.ButtonUpdated) {
            this.ButtonUpdated = false;
        }
    }


    public void CustomToggleButton() {

    }

    /**
     * Returns a Rectangle in Screenspace for the Boundingbox of the Button
     *
     * @return Rect left, top, right, bottom
     */
    public ArrayList<Rect> getBoxes() {
        return this.Boxes;
    }

    public ArrayList<Integer> getColors() {
        return this.Colors;
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
