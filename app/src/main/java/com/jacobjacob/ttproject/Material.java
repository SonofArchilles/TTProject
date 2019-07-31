package com.jacobjacob.ttproject;

import android.graphics.Color;

import static com.jacobjacob.ttproject.Util.ANIMATIONPROGRESS;
import static com.jacobjacob.ttproject.Util.COLORTILELAYER1;
import static com.jacobjacob.ttproject.Util.COLORTILELAYER2;
import static com.jacobjacob.ttproject.Util.COLORTILELAYER3;
import static com.jacobjacob.ttproject.Util.STARTTIME;
import static com.jacobjacob.ttproject.Util.TILELAYER;
import static com.jacobjacob.ttproject.Util.TILETEXTURE;

public class Material {

    private int Number, Layer1, Layer2, Layer3;
    private int ColorLayer1, ColorLayer2, ColorLayer3;
    private int[][] AnimationColor;
    private boolean hasAnimation = false;
    private int Animationtime = 1000;

    public Material() {
    }

    public Material(int Number, int Layer1, int Layer2, int Layer3, int ColorLayer1, int ColorLayer2, int ColorLayer3) {
        this.Number = Number;
        this.Layer1 = Layer1;
        this.Layer2 = Layer2;
        this.Layer3 = Layer3;

        this.ColorLayer1 = ColorLayer1;
        this.ColorLayer2 = ColorLayer2;
        this.ColorLayer3 = ColorLayer3;

        COLORTILELAYER1 = this.ColorLayer1;
        COLORTILELAYER2 = this.ColorLayer2;
        COLORTILELAYER3 = this.ColorLayer3;

        AnimationColor = new int[31][3];
    }

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

    public void UpdateTileset() {

        if (this.Animationtime > 0) {

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

            int Colorstart[] = new int[3];
            int ColorstartPosition[] = new int[3];
            int Colorend[] = new int[3];
            int ColorendPosition[] = new int[3];

            //int count = 0;

            for (int i = Anim; i < this.AnimationColor.length; i++) {
                if (this.AnimationColor[i] != null) {
                    for (int j = 0; j < 3; j++) {
                        if (this.AnimationColor[i][j] != 0 && Colorend[j] == 0) {
                            Colorend[j] = this.AnimationColor[i][j];
                            ColorendPosition[j] = i;
                        }
                    }
                }
                if (Colorend[0] != 0 && Colorend[1] != 0 && Colorend[2] != 0) {
                    return;
                }
                //if (count < 1 && i == this.AnimationColor.length - 1 && (Colorend[0] == 0 || Colorend[1] == 0 || Colorend[2] == 0)) {
                //    count++;
                //    i = 0;
                //}
            }
            //count = 0;

            for (int i = Anim; -1 < i; i--) {
                if (this.AnimationColor[i] != null) {
                    for (int j = 0; j < 3; j++) {
                        if (this.AnimationColor[i][j] != 0 && Colorstart[j] == 0) {
                            Colorstart[j] = this.AnimationColor[i][j];
                            ColorstartPosition[j] = i;
                        }
                    }
                }
                if (Colorstart[0] != 0 && Colorstart[1] != 0 && Colorstart[2] != 0) {
                    return;
                }
                //if (count < 1 && i == 0 && (Colorstart[0] == 0 || Colorstart[1] == 0 || Colorstart[2] == 0)) {
                //    count++;
                //    i = this.AnimationColor.length;

                //}
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
            TILETEXTURE.UpdateTilemap(this.Number, this.Layer1, this.Layer2, this.Layer3, ColorA, ColorB, ColorC);
        }
    }

    private int interpolateColor(int C1, int C2, float Progress) {

        Vector Colorvec = (new Vector(Color.alpha(C2), Color.red(C2), Color.green(C2), Color.blue(C2))).subtract(new Vector(Color.alpha(C1), Color.red(C1), Color.green(C1), Color.blue(C1)));
        Colorvec = Colorvec.multiplydouble(Progress);

        int a = (int) (Color.alpha(C1) + Colorvec.getValue(0));
        int r = (int) (Color.red(C1) + Colorvec.getValue(1));
        int g = (int) (Color.green(C1) + Colorvec.getValue(2));
        int b = (int) (Color.blue(C1) + Colorvec.getValue(3));
        return Color.argb(a, r, g, b);
    }

    public void UpdateMaterial() {

        if (TILELAYER == 1) {
            this.ColorLayer1 = COLORTILELAYER1;
        } else if (TILELAYER == 2) {
            this.ColorLayer2 = COLORTILELAYER2;
        } else if (TILELAYER == 3) {
            this.ColorLayer3 = COLORTILELAYER3;
        } else if (TILELAYER == 4) {

            this.AnimationColor[ANIMATIONPROGRESS][0] = COLORTILELAYER1;

        } else if (TILELAYER == 5) {

            this.AnimationColor[ANIMATIONPROGRESS][1] = COLORTILELAYER2;

        } else if (TILELAYER == 6) {
            this.AnimationColor[ANIMATIONPROGRESS][2] = COLORTILELAYER3;
        }
    }

    public int getNumber() {
        return this.Number;
    }

    public boolean hasAnimation() {
        return this.hasAnimation;
    }

    public void setNumber(int Number) {
        this.Number = Number;
    }

    public String getMaterialdetails() {
        return this.Number + " " + this.Layer1 + " " + this.Layer2 + " " + this.Layer3 + " " + this.ColorLayer1 + " " + this.ColorLayer2 + " " + this.ColorLayer3;
    }

    public String getAnimationdetails() {
        String Animinfo = new String();

        for (int i = 0; i < AnimationColor.length; i++) {
            if (AnimationColor[i] != null) {
                Animinfo = Animinfo + " " + String.valueOf(i);
                for (int j = 0; j < 3; j++) {
                    Animinfo = Animinfo + " " + String.valueOf(AnimationColor[i][j]);
                }
            }
        }
        return this.Number + " " + this.Animationtime/1000 + Animinfo; // Animationtime in s
    }

    public void LoadAnimation(int[][] Animation) {
        this.AnimationColor = Animation;
    }


    public void UpdateMaterialTileset() {
        if (TILELAYER < 4) {
            TILETEXTURE.CreateTilemap(this.Number, this.Layer1, this.Layer2, this.Layer3, this.ColorLayer1, this.ColorLayer2, this.ColorLayer3);
        }
    }

    public void setLayer123(int Layer123) {
        if (Layer123 < 0) {
            Layer123 = 0;
        }
        this.Layer1 = Layer123;
        this.Layer2 = Layer123;
        this.Layer3 = Layer123;
    }
}
