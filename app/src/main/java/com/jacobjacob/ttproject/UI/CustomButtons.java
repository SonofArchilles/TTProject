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
    String Type,VariableToChange;
    int  color = Color.rgb(120, 120, 120), color2;
    Rect Box;
    Rect BoxDraw;
    boolean ButtonUpdated = false;

    ArrayList<Rect> Boxes = new ArrayList<>();
    ArrayList<Integer> Colors = new ArrayList<>();
    private int Accuracy = 128;


    float left_Opengl, right_Opengl, top_Opengl, bottom_Opengl,left, right, top, bottom;


    //TODO buttonsnapping ==> Tell Button to get Placed to the right, left, top or Bottom side of one or more Buttons


    boolean[] booleansToChange = new boolean[1];




    /**
     * Create a new Button with a Position, a Width and a Height as well as a Type
     *
     * @param ButtonType  "Joystick" - changes the Cameraposition  "Button" - updates when pressed down "ToggleButton" - only updates when pressed once "Seekbar" - slider to adjust value
     * @param VariableToChange The Sting of the Variables name that should be changed
     * @param Position The Position in Screenspacecoordinates and is directly at the center of the shape, could get inverted if OpenGl coordinates kick in
     * @param Width    total width from left to right of the shape
     * @param Height   total height from top to bottom
     * @param Colorint The color of the Button
     */
    public CustomButtons(String ButtonType,String VariableToChange, Vector Position, float Width, float Height, int Colorint) {
        this.Position = Position;
        this.Width = Width;
        this.Height = Height;


        this.Type = ButtonType;
        this.VariableToChange = VariableToChange;
        if (Type != "Seekbar") {
            this.color = Colorint;
        }
        this.left = (float) (this.Position.getX() - (this.Width / 2));
        this.right = (float) (this.Position.getX() + (this.Width / 2));
        this.top = (float) (this.Position.getY() - (this.Height / 2));
        this.bottom = (float) (this.Position.getY() + (this.Height / 2));

        this.Box = new Rect((int) (this.left), (int) (this.top), (int) (this.right), (int) (this.bottom));


        this.left_Opengl = 2 * ((this.left / WIDTHSCREEN) * 2 - 1);
        this.top_Opengl = -2 * ((this.top / HEIGHTSCREEN) * 2 - 1);
        this.right_Opengl = 2 * ((this.right / WIDTHSCREEN) * 2 - 1);
        this.bottom_Opengl = -2 * ((this.bottom / HEIGHTSCREEN) * 2 - 1);


        this.BoxDraw = new Rect((int) (this.left_Opengl * this.Accuracy), (int) (this.top_Opengl * this.Accuracy), (int) (this.right_Opengl * this.Accuracy), (int) (this.bottom_Opengl * this.Accuracy));
        this.Colors.add(this.color);
        this.color2 = Colorint;
        this.Colors.add(this.color2);
        this.Boxes.add(this.BoxDraw);
    }





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


        Rect SeekBox;
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
        if (this.VariableToChange == "MoveDown") {
            camera.move2D(new Vector(0, 0, movespeed / 5)); // moves the Camera down
        } else if (this.VariableToChange == "MoveUp") {
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

            if (this.Type == "Joystick") {
                CustomJoyStick();
            }
            if (this.Type == "Button") { // Button that updates always when it is pressed
                MoveUPDOWN();
                //TODO Add other Buttons to Update


            }
            if (this.Type == "ToggleButton") {
                if (TOUCHSTATE == 0 && !this.ButtonUpdated) { // if the touch is down, not if moving or up
                    this.ButtonUpdated = true;

                    if (this.VariableToChange == "SELECTLVL") {
                        SelectLVL();
                        RELOADMATERIALS = true;


                    }else if (this.VariableToChange == "OPENGL"){

                        SETTINGS_OPENGL = !SETTINGS_OPENGL;

                        if (SETTINGS_OPENGL) {
                            this.Colors.set(0, FILLTILECOLOR);

                            Debug newDebug = new Debug();
                            newDebug.TilesToClippboard();

                        } else {
                            this.Colors.set(0, CHUNKCOLOR);
                        }
                        WF.SaveSettings();
                    }else if (this.VariableToChange == "DRAWHITBOX"){
                        DRAWHITBOX = !DRAWHITBOX;

                        if (DRAWHITBOX) {
                            this.Colors.set(0, FILLTILECOLOR);

                            Debug newDebug = new Debug();
                            newDebug.TilesToClippboard();

                        } else {
                            this.Colors.set(0, CHUNKCOLOR);
                        }
                    }

                }
            }

            if (this.Type == "Seekbar") { // Seekbar
                CustomSeekbar();

                if (this.VariableToChange == "SELECTEDMATERIAL"){
                    SELECTEDMATERIAL = (int)(this.Progress * MATERIALSTOTEXTURE); // To select the Materials to check the Collisions
                }
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

    /**
     * left in Screencoordinates
     * @return left
     */
    public float getLeft() {
        return this.left;
    }

    /**
     * right in Screencoordinates
     * @return right
     */
    public float getRight() {
        return this.right;
    }

    /**
     * top Screencoordinates
     * @return top
     */
    public float getTop() {
        return this.top;
    }

    /**
     * bottom in Screencoordinates
     * @return bottom
     */
    public float getBottom() {
        return this.bottom;
    }

    public int getColor() {
        return this.color;
    }

    public Vector getPosition() {
        return Position;
    }

    /**
     * Updates the Drawn Box and the Hitbox
     */
    private void UpdateBox(){

        this.left = (float) (this.Position.getX() - (this.Width / 2));
        this.right = (float) (this.Position.getX() + (this.Width / 2));
        this.top = (float) (this.Position.getY() - (this.Height / 2));
        this.bottom = (float) (this.Position.getY() + (this.Height / 2));

        this.Box = new Rect((int) (this.left), (int) (this.top), (int) (this.right), (int) (this.bottom));


        this.left_Opengl = 2 * ((this.left / WIDTHSCREEN) * 2 - 1);
        this.top_Opengl = -2 * ((this.top / HEIGHTSCREEN) * 2 - 1);
        this.right_Opengl = 2 * ((this.right / WIDTHSCREEN) * 2 - 1);
        this.bottom_Opengl = -2 * ((this.bottom / HEIGHTSCREEN) * 2 - 1);


        this.BoxDraw = new Rect((int) (this.left_Opengl * this.Accuracy), (int) (this.top_Opengl * this.Accuracy), (int) (this.right_Opengl * this.Accuracy), (int) (this.bottom_Opengl * this.Accuracy));
        this.Boxes.clear();
        this.Boxes.add(this.BoxDraw);
    }

    /**
     * Places this Button on the right side of the input Button
     *  _           _
     * |_| <-dist- |_|
     *
     * input       this Button
     *
     * @param LeftButton The Button to the Left of this Button
     * @param dist Distance between the Buttons in Screen Coordinates
     */
    public void LeftToRightOf(CustomButtons LeftButton,float dist){

        float newPositionX = LeftButton.getRight() + dist + this.Width/2;

        this.Position = new Vector(newPositionX,this.Position.getY());
        UpdateBox();
    }


    /**
     * Places this Button on the left side of the input Button
     *  _           _
     * |_| -dist-> |_|
     *
     * this Button input
     *
     * @param RightButton The Button to the Right of this Button
     * @param dist Distance between the Buttons in Screen Coordinates
     */
    public void RightToLeftOf(CustomButtons RightButton,float dist){

        float newPositionX = RightButton.getLeft() - dist - this.Width/2;

        this.Position = new Vector(newPositionX,this.Position.getY());
        UpdateBox();
    }


    /**
     * Places this Button on the bottom side of the input Button
     *  _
     * |_|  input
     *
     *  ^
     *  | dist
     *  _
     * |_|  this Button
     *
     *
     * @param TopButton The Button to the Top of this Button
     * @param dist Distance between the Buttons in Screen Coordinates
     */
    public void TopToBottomOf(CustomButtons TopButton,float dist){

        float newPositionY = TopButton.getBottom() + dist + this.Height/2;

        this.Position = new Vector(this.Position.getX(),newPositionY);
        UpdateBox();
    }

    /**
     * Places this Button on the top side of the input Button
     *  _
     * |_|  this Button
     *
     *  | dist
     *  ⌄
     *  _
     * |_|  input
     *
     *
     * @param BottomButton The Button to the Bottom of this Button
     * @param dist Distance between the Buttons in Screen Coordinates
     */
    public void BottomToTopOf(CustomButtons BottomButton,float dist){

        float newPositionY = BottomButton.getTop() - dist - this.Height/2;

        this.Position = new Vector(this.Position.getX(),newPositionY);
        UpdateBox();
    }

    /**
     * Places this Button on the Top_Height + dist of the input Button
     *   ___________________
     *  |                   |
     *  _                   | dist vertical
     * |_|  this           _|_
     *                     |_| input Button
     *
     * @param TopButton The Button to the Top of this Button
     * @param dist Distance between the Buttons in Screen Coordinates
     */
    public void TopToTopOf(CustomButtons TopButton,float dist){

        float newPositionY = TopButton.getTop() + dist + this.Height/2;

        this.Position = new Vector(this.Position.getX(),newPositionY);
        UpdateBox();
    }

    /**
     * Places this Button on the Bottom_Height + dist of the input Button
     *  _
     * |_|  this            _
     *  |   dist vertical  |_| input Button
     *  |___________________|
     *
     * @param BottomButton The Button to the Right of this Button
     * @param dist Distance between the Buttons in Screen Coordinats
     */
    public void BottomToBottomOf(CustomButtons BottomButton,float dist){

        float newPositionY = BottomButton.getBottom() + dist - this.Height/2;

        this.Position = new Vector(this.Position.getX(),newPositionY);
        UpdateBox();
    }

    /**
     * Places this Button on the Bottom_Left + dist of the input Button
     *         _
     *  |-----|_|  this
     *  | dist horizontal
     *  |   _
     *  |--|_|   input Button
     *
     * @param LeftButton The Button to the Right of this Button
     * @param dist Distance between the Buttons in Screen Coordinats
     */
    public void LeftToLeftOf(CustomButtons LeftButton,float dist){

        float newPositionX = LeftButton.getLeft() + dist + this.Width/2;

        this.Position = new Vector(newPositionX,this.Position.getY());
        UpdateBox();
    }

    /**
     * Places this Button on the Bottom_Left + dist of the input Button
     *         _
     *  this  |_|-|
     *            |  dist horizontal
     *      _     |
     *     |_|----|  input Button
     *
     * @param RightButton The Button to the Right of this Button
     * @param dist Distance between the Buttons in Screen Coordinats
     */
    public void RightToRightOf(CustomButtons RightButton,float dist){

        float newPositionX = RightButton.getRight() + dist - this.Width/2;

        this.Position = new Vector(newPositionX,this.Position.getY());
        UpdateBox();
    }

    /**
     * Flips the Width and Height of the Button, but keeps the Position.
     * Previous changes like top to bottom of have to be redone, they are ignored
     *
     *
     *  ____
     * |    |       FLIP!
     * |    |            ___________
     * |    |   ===\\   |           |
     * |    |   ===//   |___________|
     * |____|
     */
    public void FlipOrientation(){

        float old_Width = this.Width;
        this.Width = this.Height;
        this.Height = old_Width;
        UpdateBox();
    }

    /**
     * Places the Button to the right side of the Screen with a certain distance from the edge
     *                   edge |
     *             _          |
     *      this  |_|--dist---|
     *                        |
     *                        |
     * @param dist distance from the edge
     */
    public void RightToRightOfScreen(float dist){

        float newPositionX = WIDTHSCREEN - dist - this.Width/2;

        this.Position = new Vector(newPositionX,this.Position.getY());

        UpdateBox();
    }


    /**
     * Places the Button to the left side of the Screen with a certain distance from the edge
     *  |
     *  |          _
     *  |--dist---|_| this
     *  |
     *  | edge
     * @param dist distance from the edge
     */
    public void LeftToLeftOfScreen(float dist){

        float newPositionX = dist + this.Width/2;

        this.Position = new Vector(newPositionX,this.Position.getY());

        UpdateBox();
    }

    /**
     * Places the Button to the left side of the Screen with a certain distance from the edge
     *
     *  _________________ edge
     *         |
     *    dist |  _
     *         |-|_| this
     *
     * @param dist distance from the edge
     */
    public void TopToTopOfScreen(float dist){

        float newPositionY = dist + this.Height/2;

        this.Position = new Vector(this.Position.getX(),newPositionY);

        UpdateBox();
    }

    /**
     * Places the Button to the left side of the Screen with a certain distance from the edge
     *            _
     *         |-|_| this
     *    dist |
     *  _______|_________ edge
     *
     * @param dist distance from the edge
     */
    public void BottomToBottomOfScreen(float dist){

        float newPositionY = HEIGHTSCREEN - dist - this.Height/2;

        this.Position = new Vector(this.Position.getX(),newPositionY);

        UpdateBox();
    }

}
