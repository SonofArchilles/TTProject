package com.jacobjacob.ttproject.Material;

import android.graphics.Color;
import android.graphics.PorterDuff;

import com.jacobjacob.ttproject.Vector;

import java.util.Random;

import static com.jacobjacob.ttproject.LevelEditor.*;
import static com.jacobjacob.ttproject.Util.*;

//Test
public class Material {

    private int Number, Layer1, Layer2, Layer3;
    private int ColorLayer1, ColorLayer2, ColorLayer3, ColorLayer0;
    private int[][] AnimationColor;
    private boolean hasAnimation = false;
    private int Animationtime = 0;

    public Material() {
    }

    /**
     * Material initializer
     * The Material can then be used to change the Colors of the Tileparts to create a more diversed Tileset easily
     *
     * @param Number      The Id of the Material / unique identifier
     * @param Layer1      The first layer = red channel in the Textureimage / to change the number of a layer means to choose parts of the custom Tileset
     * @param Layer2      The second layer = green channel in the Textureimage / to change the number of a layer means to choose parts of the custom Tileset
     * @param Layer3      The third layer = blue channel in the Textureimage / to change the number of a layer means to choose parts of the custom Tileset
     * @param ColorLayer1 The Colors the different Layers get painted in
     * @param ColorLayer2 The Colors the different Layers get painted in
     * @param ColorLayer3 The Colors the different Layers get painted in
     */
    public Material(int Number, int Layer1, int Layer2, int Layer3, int ColorLayer1, int ColorLayer2, int ColorLayer3) {
        this.Number = Number;
        if (Layer1 < 0) {
            Layer1 = 0;
        }
        if (Layer2 < 0) {
            Layer2 = 0;
        }
        if (Layer3 < 0) {
            Layer3 = 0;
        }
        this.Layer1 = Layer1;
        this.Layer2 = Layer2;
        this.Layer3 = Layer3;

        this.ColorLayer1 = ColorLayer1;
        this.ColorLayer2 = ColorLayer2;
        this.ColorLayer3 = ColorLayer3;

        COLORTILELAYER1 = this.ColorLayer1;
        COLORTILELAYER2 = this.ColorLayer2;
        COLORTILELAYER3 = this.ColorLayer3;

        this.AnimationColor = new int[31][4];
    }

    /**
     * Changes the Backgroundcolor of the 15th Tile / the bottom of the Tile
     *
     * @param Colorlayer0 Color that the lowest part gets painted in
     */
    public void setColorLayer0(int Colorlayer0) {
        this.ColorLayer0 = Colorlayer0;
    }

    /**
     * int for the lenght of an Animation
     *
     * @param Anim int * 1000 is the Animationlength in ms
     */
    public void setAnimationtime(int Anim) { // in s - Time of the whole animation
        this.hasAnimation = false;
        if (Anim < 1) {
            Anim = 1;
        } else {
            this.hasAnimation = true;
        }
        Anim *= 1000;
        this.Animationtime = Anim; // in ms
    }

    public boolean showNormal() {
        if (this.Animationtime == 1000) {
            return true; //hasAnimation;
        } else {
            return false;
        }
    }

    /**
     * Takes in all the saved ints and interpolates and updates the Tileset
     */
    public void UpdateTileset() {

        int Anim = ANIMATIONPROGRESS; // int from 0 to seekbarmax(30)
        float Animfloat = ANIMATIONPROGRESS;

        if (TILELAYER == 0) {

            //ANIMATIONTIME == Time of the Animation = length of list with different Colors and times as Time progresses

            int Difference = (int) ((System.currentTimeMillis() - STARTTIME) % this.Animationtime); // loops after Certain amount of Time

            Animfloat = ((float) 30 * Difference) / (this.Animationtime); // int from -number of Frames to 0 or from 0 to numer of Frames
            Anim = (int) Animfloat;
        }
        //if (this.AnimationColor[0] == null) {
        //    this.AnimationColor[0] = this.AnimationColor[this.AnimationColor.length - 1];
        //}
        //this.AnimationColor[this.AnimationColor.length] = this.AnimationColor[1];

        int Colorstart[] = new int[4];
        int ColorstartPosition[] = new int[4];
        int Colorend[] = new int[4];
        int ColorendPosition[] = new int[4];

        int count = 0;

        for (int i = Anim; i < this.AnimationColor.length; i++) {
            if (this.AnimationColor[i] != null) {
                for (int j = 0; j < 3; j++) {
                    if (this.AnimationColor[i][j] != 0 && Colorend[j] == 0) {
                        Colorend[j] = this.AnimationColor[i][j];
                        ColorendPosition[j] = i;
                    }
                }
            }

            if (count < 1 && i == this.AnimationColor.length - 1 && (Colorend[0] == 0 || Colorend[1] == 0 || Colorend[2] == 0)) {
                count++;
                i = 0;
            }
        }
        count = 0;

        for (int i = Anim; -1 < i; i--) {
            if (this.AnimationColor[i] != null) {
                for (int j = 0; j < 3; j++) {
                    if (this.AnimationColor[i][j] != 0 && Colorstart[j] == 0) {
                        Colorstart[j] = this.AnimationColor[i][j];
                        ColorstartPosition[j] = i;
                    }
                }
            }

            if (count < 1 && i == 0 && (Colorstart[0] == 0 || Colorstart[1] == 0 || Colorstart[2] == 0)) {
                count++;
                i = this.AnimationColor.length;

            }
        }

        int ColorA = 0;
        int ColorB = 0;
        int ColorC = 0;

        for (int i = 0; i < 3; i++) {
            if (ColorendPosition[i] != ColorstartPosition[i]) {
                float A = (Animfloat - ColorstartPosition[i]);
                float B = (ColorendPosition[i] - ColorstartPosition[i]);

                float ProgresA = A / B;

                int Colorint = interpolateColor(Colorstart[i], Colorend[i], ProgresA);
                if (i == 0) {
                    ColorA = Colorint;
                }
                if (i == 1) {
                    ColorB = Colorint;
                }
                if (i == 2) {
                    ColorC = Colorint;
                }
            } else {
                if (i == 0) {
                    ColorA = Colorend[0];
                }
                if (i == 1) {
                    ColorB = Colorend[1];
                }
                if (i == 2) {
                    ColorC = Colorend[2];
                }
            }
        }
        if (SelectedMaterial == this.Number) {
            if (TILELAYER == TILELAYERSTART + 4) {
                SeekbarALPHA.setProgress(Color.alpha(ColorA));
                SeekbarRED.setProgress(Color.red(ColorA));
                SeekbarGREEN.setProgress(Color.green(ColorA));
                SeekbarBLUE.setProgress(Color.blue(ColorA));
            }
            if (TILELAYER == TILELAYERSTART + 5) {
                SeekbarALPHA.setProgress(Color.alpha(ColorB));
                SeekbarRED.setProgress(Color.red(ColorB));
                SeekbarGREEN.setProgress(Color.green(ColorB));
                SeekbarBLUE.setProgress(Color.blue(ColorB));
            }
            if (TILELAYER == TILELAYERSTART + 6) {
                SeekbarALPHA.setProgress(Color.alpha(ColorC));
                SeekbarRED.setProgress(Color.red(ColorC));
                SeekbarGREEN.setProgress(Color.green(ColorC));
                SeekbarBLUE.setProgress(Color.blue(ColorC));
            }
        }
        TILETEXTURE.UpdateTilemap(this.Number, this.Layer1, this.Layer2, this.Layer3, this.ColorLayer0, ColorA, ColorB, ColorC);
    }

    /**
     * This Method interpolates two Color integers
     *
     * @param C1       The first integer
     * @param C2       The second integer
     * @param Progress The Value between 0 and 1 that the new Color gets interpolated to
     * @return
     */
    private int interpolateColor(int C1, int C2, float Progress) {

        Vector Colorvec = (new Vector(Color.alpha(C2), Color.red(C2), Color.green(C2), Color.blue(C2))).subtract(new Vector(Color.alpha(C1), Color.red(C1), Color.green(C1), Color.blue(C1)));
        Colorvec = Colorvec.multiplydouble(Progress);

        int a = (int) (Color.alpha(C1) + Colorvec.getValue(0));
        int r = (int) (Color.red(C1) + Colorvec.getValue(1));
        int g = (int) (Color.green(C1) + Colorvec.getValue(2));
        int b = (int) (Color.blue(C1) + Colorvec.getValue(3));
        return Color.argb(a, r, g, b);
    }

    /**
     * Updates the Colorsliders to the corresponding Colors at the Layervalue
     * That makes editing the Colors easier
     */
    public void UpdateSeekbarProgress() {
        //if (this.Oldprogress != ANIMATIONPROGRESS) {
        if (TILELAYER == TILELAYERSTART) {
            SeekbarALPHA.setProgress(Color.alpha(this.ColorLayer0));
            SeekbarRED.setProgress(Color.red(this.ColorLayer0));
            SeekbarGREEN.setProgress(Color.green(this.ColorLayer0));
            SeekbarBLUE.setProgress(Color.blue(this.ColorLayer0));
        }
        if (TILELAYER == TILELAYERSTART + 1) {
            SeekbarALPHA.setProgress(Color.alpha(this.ColorLayer1));
            SeekbarRED.setProgress(Color.red(this.ColorLayer1));
            SeekbarGREEN.setProgress(Color.green(this.ColorLayer1));
            SeekbarBLUE.setProgress(Color.blue(this.ColorLayer1));
        }
        if (TILELAYER == TILELAYERSTART + 2) {
            SeekbarALPHA.setProgress(Color.alpha(this.ColorLayer2));
            SeekbarRED.setProgress(Color.red(this.ColorLayer2));
            SeekbarGREEN.setProgress(Color.green(this.ColorLayer2));
            SeekbarBLUE.setProgress(Color.blue(this.ColorLayer2));
        }
        if (TILELAYER == TILELAYERSTART + 3) {
            SeekbarALPHA.setProgress(Color.alpha(this.ColorLayer3));
            SeekbarRED.setProgress(Color.red(this.ColorLayer3));
            SeekbarGREEN.setProgress(Color.green(this.ColorLayer3));
            SeekbarBLUE.setProgress(Color.blue(this.ColorLayer3));
        }
        //this.Oldprogress = ANIMATIONPROGRESS;
        //}
    }

    /**
     * Changes the Color of the Seekbarthumb to the Color of the Animation at the current progress to make it easier to see the already saved Timeframes
     */
    public void UpdateAnimationSeekbarColor() {
        if (this.AnimationColor != null && this.AnimationColor[ANIMATIONPROGRESS] != null) {
            int AnimColor = this.AnimationColor[ANIMATIONPROGRESS][TILELAYERSTART + TILELAYER - 4];
            if (AnimColor != 0) {
                SeekbarANIMATON.getThumb().setColorFilter(AnimColor, PorterDuff.Mode.SCREEN);
            } else {
                SeekbarANIMATON.getThumb().setColorFilter(Color.rgb(0, 0, 0), PorterDuff.Mode.SCREEN);
            }
        }
    }

    /**
     * Sets Colorvalues to Seekbarvalues as well as setting the Animationcolorvalues inside an Array
     */
    public void UpdateMaterial() {
        if (TILELAYER == TILELAYERSTART) {
            this.ColorLayer0 = COLORTILELAYER0;
        } else if (TILELAYER == TILELAYERSTART + 1) {
            this.ColorLayer1 = COLORTILELAYER1;
        } else if (TILELAYER == TILELAYERSTART + 2) {
            this.ColorLayer2 = COLORTILELAYER2;
        } else if (TILELAYER == TILELAYERSTART + 3) {
            this.ColorLayer3 = COLORTILELAYER3;
        } else if (TILELAYER == TILELAYERSTART + 4 && !UPDATETILESET) {
            this.AnimationColor[ANIMATIONPROGRESS][0] = COLORTILELAYER1;
        } else if (TILELAYER == TILELAYERSTART + 5 && !UPDATETILESET) {
            this.AnimationColor[ANIMATIONPROGRESS][1] = COLORTILELAYER2;
        } else if (TILELAYER == TILELAYERSTART + 6 && !UPDATETILESET) {
            this.AnimationColor[ANIMATIONPROGRESS][2] = COLORTILELAYER3;
        }
    }

    /**
     * Get the Id of the Material
     *
     * @return The Id of the Material / unique identifier
     */
    public int getNumber() {
        return this.Number;
    }

    /**
     * Checks if the Material has an Animation
     *
     * @return a bool if the time of the Animation is greater than 1000ms
     */
    public boolean hasAnimation() {
        if (this.Animationtime > 1000) {
            return true;
        } else {
            return false;
        }
        //return this.hasAnimation;
    }

    /**
     * Sets the unique id
     *
     * @param Number The new Id of the Material
     */
    public void setNumber(int Number) {
        this.Number = Number;
    }

    /**
     * Get The Material information as String in order to save it
     *
     * @return The Id/ Number, the Layers and fitting Colors and the Backgroundcolor
     */
    public String getMaterialdetails() {
        return this.Number + " " + this.Layer1 + " " + this.Layer2 + " " + this.Layer3 + " " + this.ColorLayer1 + " " + this.ColorLayer2 + " " + this.ColorLayer3 + " " + this.ColorLayer0;
    }

    /**
     * Get The Animation information as String in order to save it
     *
     * @return A String of Colors alternating from Layer 1 to Layer 3
     */
    public String getAnimationdetails() {
        String Animinfo = new String();

        for (int i = 0; i < this.AnimationColor.length; i++) {
            if (this.AnimationColor[i] != null) {
                Animinfo = Animinfo + " " + String.valueOf(i);
                for (int j = 0; j < 3; j++) {
                    Animinfo = Animinfo + " " + String.valueOf(this.AnimationColor[i][j]);
                }
            }
        }
        return this.Number + " " + this.Animationtime / 1000 + Animinfo; // Animationtime in s
    }

    /**
     * Gives the Material the loaded int[][] that contains the Animationdetails
     *
     * @param Animation int[][] with the 3 Layers and a lenght > 1
     */
    public void LoadAnimation(int[][] Animation) {
        this.AnimationColor = Animation;
    }

    /**
     * Updates the Tileset Bitmap if (TILELAYER < TILELAYERSTART + 4)
     */
    public void UpdateMaterialTileset() {
        if (TILELAYER < TILELAYERSTART + 4) {
            TILETEXTURE.CreateTilemap(this.Number, this.Layer1, this.Layer2, this.Layer3, this.ColorLayer0, this.ColorLayer1, this.ColorLayer2, this.ColorLayer3);
            UpdateMaterialTilesetNormal();
        }
    }

    /**
     * Creates a random Material each Time its called
     * Layer1 to 3 are random
     */
    public void CreateRandomMaterialTileset() {
        //if (TILELAYER < TILELAYERSTART + 4) {
            Random rand = new Random();
            int randomLayer1 = rand.nextInt(9);
            int randomLayer2 = rand.nextInt(9);
            int randomLayer3 = rand.nextInt(9);


            TILETEXTURE.CreateTilemap(this.Number, /*/this.Layer1/*/randomLayer1/**/, /*/this.Layer2/*/randomLayer2/**/, /*/this.Layer3/*/randomLayer3/**/, this.ColorLayer0, this.ColorLayer1, this.ColorLayer2, this.ColorLayer3);
            //UpdateMaterialTilesetNormal();
        //}
    }

    public void CreateMaterialTileset() {
        TILETEXTURE.CreateTilemap(this.Number, this.Layer1, this.Layer2, this.Layer3, this.ColorLayer0, this.ColorLayer1, this.ColorLayer2, this.ColorLayer3);
    }

    public void UpdateMaterialTilesetNormal() {
        if (TILELAYER < TILELAYERSTART + 4) {
            if (CREATEFASTNORMALS) {
                TILETEXTURE.CreateNormals(this.Number, this.Layer1, this.Layer2, this.Layer3, Color.alpha(ColorLayer1), Color.alpha(ColorLayer2), Color.alpha(ColorLayer3));
            } else {
                TILETEXTURE.CreateAccurateNormals(this.Number, this.Layer1, this.Layer2, this.Layer3, Color.alpha(ColorLayer1), Color.alpha(ColorLayer2), Color.alpha(ColorLayer3));
            }
        }
    }

    public void CreateMaterialTilesetNormal() {
        if (CREATEFASTNORMALS) {
            TILETEXTURE.CreateNormals(this.Number, this.Layer1, this.Layer2, this.Layer3, Color.alpha(ColorLayer1), Color.alpha(ColorLayer2), Color.alpha(ColorLayer3));
        } else {
            TILETEXTURE.CreateAccurateNormals(this.Number, this.Layer1, this.Layer2, this.Layer3, Color.alpha(ColorLayer1), Color.alpha(ColorLayer2), Color.alpha(ColorLayer3));
        }
    }


    /**
     * Sets a new Layervalue to the Layer Corresponding to the TILELAYER
     *
     * @param Layer123 the new Value for that specific Layer
     */
    public void setLayer123(int Layer123) {
        if (TILELAYER == TILELAYERSTART + 1) {
            this.Layer1 = Layer123;
        } else if (TILELAYER == TILELAYERSTART + 2) {
            this.Layer2 = Layer123;
        } else if (TILELAYER == TILELAYERSTART + 3) {
            this.Layer3 = Layer123;
        }
    }

    /**
     * Returns the number of a Layer
     *
     * @param Layer The layer it returns the number from
     * @return
     */
    public int getLayer(int Layer) {
        if (Layer == TILELAYERSTART + 1) {
            return this.Layer1;
        } else if (Layer == TILELAYERSTART + 2) {
            return this.Layer2;
        } else if (Layer == TILELAYERSTART + 3) {
            return this.Layer3;
        } else return 0;
    }

    /**
     * Returns the number of a ColorLayer
     *
     * @param ColorLayer The layer it returns the color from
     * @return
     */
    public int getColor(int ColorLayer) {
        if (ColorLayer == TILELAYERSTART) {
            return this.ColorLayer0;
        }
        if (ColorLayer == TILELAYERSTART + 1) {
            return this.ColorLayer1;
        } else if (ColorLayer == TILELAYERSTART + 2) {
            return this.ColorLayer2;
        } else if (ColorLayer == TILELAYERSTART + 3) {
            return this.ColorLayer3;
        } else return 0;
    }
}
