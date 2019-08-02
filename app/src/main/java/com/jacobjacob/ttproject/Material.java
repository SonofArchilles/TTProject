package com.jacobjacob.ttproject;

import android.graphics.Color;
import android.graphics.PorterDuff;

import static com.jacobjacob.ttproject.LevelEditor.SelectedMaterial;
import static com.jacobjacob.ttproject.Util.ANIMATIONPROGRESS;
import static com.jacobjacob.ttproject.Util.COLORTILELAYER1;
import static com.jacobjacob.ttproject.Util.COLORTILELAYER2;
import static com.jacobjacob.ttproject.Util.COLORTILELAYER3;
import static com.jacobjacob.ttproject.Util.STARTTIME;
import static com.jacobjacob.ttproject.Util.SeekbarALPHA;
import static com.jacobjacob.ttproject.Util.SeekbarANIMATON;
import static com.jacobjacob.ttproject.Util.SeekbarBLUE;
import static com.jacobjacob.ttproject.Util.SeekbarGREEN;
import static com.jacobjacob.ttproject.Util.SeekbarRED;
import static com.jacobjacob.ttproject.Util.TILELAYER;
import static com.jacobjacob.ttproject.Util.TILETEXTURE;
import static com.jacobjacob.ttproject.Util.UPDATETILESET;

//Test
public class Material {

    private int Number, Layer1, Layer2, Layer3;
    private int ColorLayer1, ColorLayer2, ColorLayer3;
    private int[][] AnimationColor;
    private boolean hasAnimation = false;
    private int Animationtime = 0;

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

        this.AnimationColor = new int[31][4];
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
            if (TILELAYER == 4) {
                SeekbarALPHA.setProgress(Color.alpha(ColorA));
                SeekbarRED.setProgress(Color.red(ColorA));
                SeekbarGREEN.setProgress(Color.green(ColorA));
                SeekbarBLUE.setProgress(Color.blue(ColorA));
            }
            if (TILELAYER == 5) {
                SeekbarALPHA.setProgress(Color.alpha(ColorB));
                SeekbarRED.setProgress(Color.red(ColorB));
                SeekbarGREEN.setProgress(Color.green(ColorB));
                SeekbarBLUE.setProgress(Color.blue(ColorB));
            }
            if (TILELAYER == 6) {
                SeekbarALPHA.setProgress(Color.alpha(ColorC));
                SeekbarRED.setProgress(Color.red(ColorC));
                SeekbarGREEN.setProgress(Color.green(ColorC));
                SeekbarBLUE.setProgress(Color.blue(ColorC));
            }
        }
        TILETEXTURE.UpdateTilemap(this.Number, this.Layer1, this.Layer2, this.Layer3, ColorA, ColorB, ColorC);
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

    public void UpdateSeekbarProgress() {
        //if (this.Oldprogress != ANIMATIONPROGRESS) {
        if (TILELAYER == 1) {
            SeekbarALPHA.setProgress(Color.alpha(this.ColorLayer1));
            SeekbarRED.setProgress(Color.red(this.ColorLayer1));
            SeekbarGREEN.setProgress(Color.green(this.ColorLayer1));
            SeekbarBLUE.setProgress(Color.blue(this.ColorLayer1));
        }
        if (TILELAYER == 2) {
            SeekbarALPHA.setProgress(Color.alpha(this.ColorLayer2));
            SeekbarRED.setProgress(Color.red(this.ColorLayer2));
            SeekbarGREEN.setProgress(Color.green(this.ColorLayer2));
            SeekbarBLUE.setProgress(Color.blue(this.ColorLayer2));
        }
        if (TILELAYER == 3) {
            SeekbarALPHA.setProgress(Color.alpha(this.ColorLayer3));
            SeekbarRED.setProgress(Color.red(this.ColorLayer3));
            SeekbarGREEN.setProgress(Color.green(this.ColorLayer3));
            SeekbarBLUE.setProgress(Color.blue(this.ColorLayer3));
        }
        //this.Oldprogress = ANIMATIONPROGRESS;
        //}
    }

    public void UpdateAnimationSeekbarColor() {
        if (this.AnimationColor != null && this.AnimationColor[ANIMATIONPROGRESS] != null) {
            int AnimColor = this.AnimationColor[ANIMATIONPROGRESS][TILELAYER - 4];
            if (AnimColor != 0) {
                SeekbarANIMATON.getThumb().setColorFilter(AnimColor, PorterDuff.Mode.SCREEN);
            } else {
                SeekbarANIMATON.getThumb().setColorFilter(Color.rgb(0, 0, 0), PorterDuff.Mode.SCREEN);
            }
        }
    }

    public void UpdateMaterial() {
        if (TILELAYER == 1) {
            this.ColorLayer1 = COLORTILELAYER1;
        } else if (TILELAYER == 2) {
            this.ColorLayer2 = COLORTILELAYER2;
        } else if (TILELAYER == 3) {
            this.ColorLayer3 = COLORTILELAYER3;
        } else if (TILELAYER == 4 && !UPDATETILESET) {
            this.AnimationColor[ANIMATIONPROGRESS][0] = COLORTILELAYER1;
        } else if (TILELAYER == 5 && !UPDATETILESET) {
            this.AnimationColor[ANIMATIONPROGRESS][1] = COLORTILELAYER2;
        } else if (TILELAYER == 6 && !UPDATETILESET) {
            this.AnimationColor[ANIMATIONPROGRESS][2] = COLORTILELAYER3;
        }
    }

    public int getNumber() {
        return this.Number;
    }

    public boolean hasAnimation() {
        if (this.Animationtime > 1000) {
            return true;
        } else {
            return false;
        }
        //return this.hasAnimation;
    }

    public void setNumber(int Number) {
        this.Number = Number;
    }

    public String getMaterialdetails() {
        return this.Number + " " + this.Layer1 + " " + this.Layer2 + " " + this.Layer3 + " " + this.ColorLayer1 + " " + this.ColorLayer2 + " " + this.ColorLayer3;
    }

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

    public void LoadAnimation(int[][] Animation) {
        this.AnimationColor = Animation;
    }


    public void UpdateMaterialTileset() {
        if (TILELAYER < 4) {
            TILETEXTURE.CreateTilemap(this.Number, this.Layer1, this.Layer2, this.Layer3, this.ColorLayer1, this.ColorLayer2, this.ColorLayer3);
        }
    }

    public void setLayer123(int Layer123) {
        if (TILELAYER == 1) {
            this.Layer1 = Layer123;
            this.Layer2 = Layer123;
            this.Layer3 = Layer123;
        } else if (TILELAYER == 2) {
            this.Layer2 = Layer123;
            this.Layer3 = Layer123;
        } else if (TILELAYER == 3) {
            this.Layer3 = Layer123;
        }
    }
}
