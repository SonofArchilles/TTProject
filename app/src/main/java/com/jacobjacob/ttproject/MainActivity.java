package com.jacobjacob.ttproject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jacobjacob.ttproject.UI.Inputloop;

import static com.jacobjacob.ttproject.LevelEditor.SelectedMaterial;
import static com.jacobjacob.ttproject.Util.*;

public class MainActivity extends AppCompatActivity {

    public float xmove, ymove, xmove2, ymove2;
    public static ImageView IMAGE;
    public static boolean UHANDELERSTARTED = false;
    public static boolean UILOOPER = false;
    public static com.jacobjacob.ttproject.OpenGL.MainSurfaceView mainSurfaceView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);


        Display display = getWindowManager().getDefaultDisplay();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        android.graphics.Point size = new Point();
        display.getSize(size);
        WIDTHSCREEN = size.x;
        HEIGHTSCREEN = size.y;


        CONTEXT = this.getApplicationContext();

        try {
            RF.ReadSettings();
        }catch (Exception e){
            Log.d("ReadSettings","Settings maybe not there");
        }

        //OPENGL = true;


        if (OPENGL) {
            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ConfigurationInfo info = am.getDeviceConfigurationInfo();
            boolean supportES2 = (info.reqGlEsVersion >= 0x30000);
            if (supportES2) {
                com.jacobjacob.ttproject.OpenGL.MainRenderer mainRenderer = new com.jacobjacob.ttproject.OpenGL.MainRenderer(/**/getApplicationContext()/**/);

                mainSurfaceView = new com.jacobjacob.ttproject.OpenGL.MainSurfaceView(this);
                mainSurfaceView.setEGLContextClientVersion(3);
                mainSurfaceView.setRenderer(mainRenderer);
                this.setContentView(mainSurfaceView);

            } else {
                Log.e("OpenGLES 2", "Your device doesn't support ES2. (" + info.reqGlEsVersion + ")");
            }


            IMAGE = findViewById(R.id.SCREEN);
            CONTEXT = getApplicationContext();
            TILETEXTURE = new Tiletexture();
            LE.Inventory();
            NOISE.setSeed(System.currentTimeMillis() / 1000);

            LE.LoadLevel();

            //LE.SaveLevel();

            for (int i = 0; i < MATERIALARRAY.length; i++) {
                MATERIALARRAY[i].CreateMaterialTilesetNormal();
            }


            if (!UILOOPER) {
                UILOOPER = true;
                UILOOPTHREAD = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        Inputloop INPUTLOOP = new Inputloop();
                        INPUTLOOP.start();
                        //function one or whatever
                    }
                });
                UILOOPTHREAD.start();
            }

        } /**/ else {
            //setContentView(mainSurfaceView);


            IMAGE = findViewById(R.id.SCREEN);
            CONTEXT = getApplicationContext();
            TILETEXTURE = new Tiletexture();

            LE.Inventory();

            NOISE.setSeed(System.currentTimeMillis() / 1000);
            try {
                LE.LoadLevel();
            }catch (Exception e){
                Log.d("LoadLevel","Couldn't be loaded");
            }
            SeekbarALPHA = new SeekBar(CONTEXT);
            SeekbarRED = new SeekBar(CONTEXT);
            SeekbarGREEN = new SeekBar(CONTEXT);
            SeekbarBLUE = new SeekBar(CONTEXT);
            SeekbarANIMATON = new SeekBar(CONTEXT);
            MOVE_UP = new Button(CONTEXT);
            MOVE_FORWARD = new Button(CONTEXT);
            MOVE_DOWN = new Button(CONTEXT);
            MOVE_LEFT = new Button(CONTEXT);
            MOVE_BACK = new Button(CONTEXT);
            MOVE_RIGHT = new Button(CONTEXT);
            INVENTORY_TOGGLE = new ToggleButton(CONTEXT);
            OPENGL_TOGGLE = new ToggleButton(CONTEXT);
            FILLPLACE = new ToggleButton(CONTEXT);
            DRAWKDTREE = new ToggleButton(CONTEXT);
            REMOVETILES = new ToggleButton(CONTEXT);
            ADDLEFT = new Button(CONTEXT);
            ADDRIGHT = new Button(CONTEXT);
            TILEEDIT = new Button(CONTEXT);
            DISPLAYFRAMES = new TextView(CONTEXT);


            TILEEDIT = findViewById(R.id.TOGGLETEXTUREEDIT);
            SeekbarALPHA = findViewById(R.id.seekBarALPHA);
            SeekbarRED = findViewById(R.id.seekBarRED);
            SeekbarGREEN = findViewById(R.id.seekBarGREEN);
            SeekbarBLUE = findViewById(R.id.seekBarBLUE);
            SeekbarANIMATON = new SeekBar(this.getApplicationContext());
            SeekbarANIMATON = findViewById(R.id.seekBarANIMATION);

            MOVE_UP = findViewById(R.id.MOVE_UP);
            MOVE_FORWARD = findViewById(R.id.MOVE_FORWARD);
            MOVE_DOWN = findViewById(R.id.MOVE_DOWN);
            MOVE_LEFT = findViewById(R.id.MOVE_LEFT);
            MOVE_BACK = findViewById(R.id.MOVE_BACK);
            MOVE_RIGHT = findViewById(R.id.MOVE_RIGHT);
            INVENTORY_TOGGLE = findViewById(R.id.INVENTORY_TOGGLE);
            OPENGL_TOGGLE = findViewById(R.id.OPENGL_TOGGLE);
            FILLPLACE = findViewById(R.id.FILLPLACE);
            DRAWKDTREE = findViewById(R.id.DRAWKDTREE);
            ADDLEFT = findViewById(R.id.ADDLEFT);
            ADDRIGHT = findViewById(R.id.ADDRIGHT);
            DISPLAYFRAMES = findViewById(R.id.DISPLAYFRAMES);
            REMOVETILES = findViewById(R.id.REMOVETILES);

            DISPLAYFRAMES.setVisibility(View.INVISIBLE);
            ADDLEFT.setVisibility(View.INVISIBLE);
            ADDRIGHT.setVisibility(View.INVISIBLE);
            REMOVETILES.setVisibility(View.INVISIBLE);
            DRAWKDTREE.setVisibility(View.INVISIBLE);
            FILLPLACE.setVisibility(View.INVISIBLE);
            OPENGL_TOGGLE.setVisibility(View.INVISIBLE);

            TILEEDIT.setText("EDIT TILE");
            SeekbarALPHA.setVisibility(View.INVISIBLE);
            SeekbarRED.setVisibility(View.INVISIBLE);
            SeekbarGREEN.setVisibility(View.INVISIBLE);
            SeekbarBLUE.setVisibility(View.INVISIBLE);
            SeekbarANIMATON.setVisibility(View.INVISIBLE);

            SeekbarALPHA.getThumb().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.SCREEN);
            SeekbarRED.getThumb().setColorFilter(Color.rgb(255, 0, 0), PorterDuff.Mode.SCREEN);
            SeekbarGREEN.getThumb().setColorFilter(Color.rgb(0, 255, 0), PorterDuff.Mode.SCREEN);
            SeekbarBLUE.getThumb().setColorFilter(Color.rgb(0, 0, 255), PorterDuff.Mode.SCREEN);

            SeekbarALPHA.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.MULTIPLY));
            SeekbarRED.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.rgb(255, 0, 0), PorterDuff.Mode.MULTIPLY));
            SeekbarGREEN.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.rgb(0, 255, 0), PorterDuff.Mode.MULTIPLY));
            SeekbarBLUE.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.rgb(0, 0, 255), PorterDuff.Mode.MULTIPLY));


            LOADLVLSPINNER = findViewById(R.id.LoadLVL);
            ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, FILE_NAMES);
            myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            LOADLVLSPINNER.setAdapter(myAdapter);

            //mainSurfaceView.add

            LOADLVLSPINNER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                    LE.SaveLevel();

                    LEVELINT = i;
                    FILE_NAME = adapterView.getSelectedItem().toString();

                    LE.LoadLevel();

                    //uHandeler.updateScreen();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            INVENTORY_TOGGLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    DISPLAYINVENTORY = b;
                    if (!DISPLAYINVENTORY) {
                        //DISPLAYFRAMES.setVisibility(View.INVISIBLE);
                        //ADDLEFT.setVisibility(View.INVISIBLE);
                        //ADDRIGHT.setVisibility(View.INVISIBLE);


                        REMOVETILES.setVisibility(View.INVISIBLE);
                        DRAWKDTREE.setVisibility(View.INVISIBLE);
                        FILLPLACE.setVisibility(View.INVISIBLE);
                        OPENGL_TOGGLE.setVisibility(View.INVISIBLE);

                    } else {
                        //DISPLAYFRAMES.setVisibility(View.VISIBLE);
                        //ADDLEFT.setVisibility(View.VISIBLE);
                        //ADDRIGHT.setVisibility(View.VISIBLE);


                        REMOVETILES.setVisibility(View.VISIBLE);
                        DRAWKDTREE.setVisibility(View.VISIBLE);
                        FILLPLACE.setVisibility(View.VISIBLE);
                        OPENGL_TOGGLE.setVisibility(View.VISIBLE);


                    }
                }
            });

            OPENGL_TOGGLE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    SETTINGS_OPENGL = b;
                    WF.SaveSettings();
                }
            });

            FILLPLACE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b) {
                        FILLTILES = true;
                        PLACETILE = false;
                    } else {
                        FILLTILES = false;
                        PLACETILE = true;
                    }
                    if (TILELAYER > TILELAYERSTART + 3) {
                        FILLTILES = false;
                        PLACETILE = false;
                    }

                }
            });

            REMOVETILES.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    DESTROYTILES = b;

                }
            });

            DRAWKDTREE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean drawkdtreebutton) {
                    DRAWKDTREEBOOL = drawkdtreebutton;
                }
            });


            ADDLEFT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FRAMES--;

                    if (FRAMES < -TEXTUREWIDTH * TEXTUREWIDTH) {
                        FRAMES = TEXTUREWIDTH * TEXTUREWIDTH;
                    }

                    if (TILELAYER < TILELAYERSTART + 4) {
                        MATERIALARRAY[SelectedMaterial].setLayer123(FRAMES);
                        MATERIALARRAY[SelectedMaterial].UpdateMaterialTileset();
                        DISPLAYFRAMES.setText("Material: " + String.valueOf(FRAMES));
                    } else {
                        DISPLAYFRAMES.setText("Animationtime: " + String.valueOf(FRAMES) + "s");
                        MATERIALARRAY[SelectedMaterial].setAnimationtime(FRAMES);
                    }
                }
            });
            ADDRIGHT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FRAMES++;


                    if (FRAMES > TEXTUREWIDTH * TEXTUREWIDTH) {
                        FRAMES = -TEXTUREWIDTH * TEXTUREWIDTH;
                    }

                    if (TILELAYER < TILELAYERSTART + 4) {
                        MATERIALARRAY[SelectedMaterial].setLayer123(FRAMES);
                        MATERIALARRAY[SelectedMaterial].UpdateMaterialTileset();
                        DISPLAYFRAMES.setText("Material: " + String.valueOf(FRAMES));
                    } else {
                        DISPLAYFRAMES.setText("Animationtime: " + String.valueOf(FRAMES) + "s");
                        MATERIALARRAY[SelectedMaterial].setAnimationtime(FRAMES);
                    }
                }
            });

            MOVE_RIGHT.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) { //right bottomright
                    //camera.move(new Vector(movespeed * Math.cos(camera.RotationX), 0, -movespeed * Math.sin(camera.RotationX)));
                    camera.move2D(new Vector(movespeed, 0));
                }
            });

            MOVE_RIGHT.setOnTouchListener(new View.OnTouchListener() { //right bootomright
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //camera.move(new Vector(movespeed * Math.cos(camera.RotationX), 0, -movespeed * Math.sin(camera.RotationX)));
                    camera.move2D(new Vector(movespeed, 0));
                    return true;
                }
            });

            MOVE_BACK.setOnClickListener(new View.OnClickListener() { //backwards bottommid
                @Override
                public void onClick(View v) {
                    //camera.move(new Vector(movespeed * Math.sin(camera.RotationX), 0, movespeed * Math.cos(camera.RotationX)));
                    camera.move2D(new Vector(0, movespeed));
                }
            });

            MOVE_BACK.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) { //backwards bottommid
                    //camera.move(new Vector(movespeed * Math.sin(camera.RotationX), 0, movespeed * Math.cos(camera.RotationX)));
                    camera.move2D(new Vector(0, movespeed));
                    return true;
                }
            });


            MOVE_FORWARD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //forward topmid
                    //camera.move(new Vector(-movespeed * Math.sin(camera.RotationX), 0, -movespeed * Math.cos(camera.RotationX)));
                    camera.move2D(new Vector(0, -movespeed));
                }
            });
            MOVE_FORWARD.setOnTouchListener(new View.OnTouchListener() { //forward topmid
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //camera.move(new Vector(-movespeed * Math.sin(camera.RotationX), 0, -movespeed * Math.cos(camera.RotationX)));
                    camera.move2D(new Vector(0, -movespeed));
                    return true;
                }
            });

            MOVE_LEFT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //left bottomleft
                    //camera.move(new Vector(-movespeed * Math.cos(camera.RotationX), 0, movespeed * Math.sin(camera.RotationX)));
                    camera.move2D(new Vector(-movespeed, 0));
                }
            });
            MOVE_LEFT.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) { //left bottomleft
                    //camera.move(new Vector(-movespeed * Math.cos(camera.RotationX), 0, movespeed * Math.sin(camera.RotationX)));
                    camera.move2D(new Vector(-movespeed, 0));
                    return true;
                }
            });

            MOVE_DOWN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //camera.move(new Vector(0, -movespeed, 0));
                    camera.move2D(new Vector(0, 0, movespeed / 5));
                }
            });


            MOVE_DOWN.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //camera.move(new Vector(0, -movespeed, 0));
                    camera.move2D(new Vector(0, 0, movespeed / 5));
                    return true;
                }
            });

            MOVE_UP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //camera.move(new Vector(0, movespeed, 0));
                    camera.move2D(new Vector(0, 0, -movespeed / 5));
                }
            });
            MOVE_UP.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //camera.move(new Vector(0, movespeed, 0));
                    camera.move2D(new Vector(0, 0, -movespeed / 5));
                    return true;
                }
            });

            if (!UHANDELERSTARTED) {
                UHANDELERSTARTED = true;
                RENDERER = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        UpdateHandler newUhandeler = new UpdateHandler();
                        newUhandeler.run();
                        //function one or whatever
                    }
                });
                RENDERER.start();

                TILEUPDATE = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        UpdateHandler newUhandeler = new UpdateHandler();
                        newUhandeler.runTileUpdates();
                        //function one or whatever
                    }
                });
                TILEUPDATE.start();

            }

            TILEEDIT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TILELAYER++;
                    if (TILELAYER > TILELAYERSTART + 6) {
                        TILELAYER = 0;
                    }
                    if (TILELAYER == 0) {
                        FILLTILES = FILLPLACE.isChecked();
                        PLACETILE = !FILLPLACE.isChecked();
                        TILEEDIT.setText("EDIT TILE");
                        DISPLAYFRAMES.setText("Material: " + String.valueOf(FRAMES));
                        SeekbarALPHA.setVisibility(View.INVISIBLE);
                        SeekbarRED.setVisibility(View.INVISIBLE);
                        SeekbarGREEN.setVisibility(View.INVISIBLE);
                        SeekbarBLUE.setVisibility(View.INVISIBLE);
                        SeekbarANIMATON.setVisibility(View.INVISIBLE);
                        ADDLEFT.setVisibility(View.INVISIBLE);
                        ADDRIGHT.setVisibility(View.INVISIBLE);
                        DISPLAYFRAMES.setVisibility(View.INVISIBLE);
                    } else if (TILELAYER < TILELAYERSTART + 4) {
                        if (TILELAYER == TILELAYERSTART) {
                            TILEEDIT.setText("BACK");
                        } else {
                            TILEEDIT.setText("LAYER: " + String.valueOf(TILELAYER));
                        }
                        DISPLAYFRAMES.setText("Material: " + String.valueOf(FRAMES));
                        SeekbarALPHA.setVisibility(View.VISIBLE);
                        SeekbarRED.setVisibility(View.VISIBLE);
                        SeekbarGREEN.setVisibility(View.VISIBLE);
                        SeekbarBLUE.setVisibility(View.VISIBLE);
                        SeekbarANIMATON.setVisibility(View.INVISIBLE);

                        ADDLEFT.setVisibility(View.VISIBLE);
                        ADDRIGHT.setVisibility(View.VISIBLE);
                        DISPLAYFRAMES.setVisibility(View.VISIBLE);
                        MATERIALARRAY[SelectedMaterial].UpdateSeekbarProgress();

                    } else {
                        FILLTILES = false;
                        PLACETILE = false;
                        DISPLAYFRAMES.setText("Animationtime: " + String.valueOf(FRAMES) + "s");
                        TILEEDIT.setText("ANIMATIONLAYER: " + String.valueOf(TILELAYER - 3 - TILELAYERSTART));
                        SeekbarANIMATON.setVisibility(View.VISIBLE);
                    }

                }
            });

            SeekbarALPHA.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if (TILELAYER == TILELAYERSTART) {
                        COLORTILELAYER0 = Color.argb(progress, Color.red(COLORTILELAYER0), Color.green(COLORTILELAYER0), Color.blue(COLORTILELAYER0));
                    } else if (TILELAYER == TILELAYERSTART + 1 || TILELAYER == TILELAYERSTART + 4) {
                        COLORTILELAYER1 = Color.argb(progress, Color.red(COLORTILELAYER1), Color.green(COLORTILELAYER1), Color.blue(COLORTILELAYER1));
                    } else if (TILELAYER == TILELAYERSTART + 2 || TILELAYER == TILELAYERSTART + 5) {
                        COLORTILELAYER2 = Color.argb(progress, Color.red(COLORTILELAYER2), Color.green(COLORTILELAYER2), Color.blue(COLORTILELAYER2));
                    } else if (TILELAYER == TILELAYERSTART + 3 || TILELAYER == TILELAYERSTART + 6) {
                        COLORTILELAYER3 = Color.argb(progress, Color.red(COLORTILELAYER3), Color.green(COLORTILELAYER3), Color.blue(COLORTILELAYER3));
                    }

                    MATERIALARRAY[SelectedMaterial].UpdateMaterial();//selected Material
                    MATERIALARRAY[SelectedMaterial].UpdateMaterialTileset();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            SeekbarRED.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (TILELAYER == TILELAYERSTART) {
                        COLORTILELAYER0 = Color.argb(Color.alpha(COLORTILELAYER0), progress, Color.green(COLORTILELAYER0), Color.blue(COLORTILELAYER0));
                    } else if (TILELAYER == TILELAYERSTART + 1 || TILELAYER == TILELAYERSTART + 4) {
                        COLORTILELAYER1 = Color.argb(Color.alpha(COLORTILELAYER1), progress, Color.green(COLORTILELAYER1), Color.blue(COLORTILELAYER1));
                    } else if (TILELAYER == TILELAYERSTART + 2 || TILELAYER == TILELAYERSTART + 5) {
                        COLORTILELAYER2 = Color.argb(Color.alpha(COLORTILELAYER2), progress, Color.green(COLORTILELAYER2), Color.blue(COLORTILELAYER2));
                    } else if (TILELAYER == TILELAYERSTART + 3 || TILELAYER == TILELAYERSTART + 6) {
                        COLORTILELAYER3 = Color.argb(Color.alpha(COLORTILELAYER3), progress, Color.green(COLORTILELAYER3), Color.blue(COLORTILELAYER3));
                    }

                    MATERIALARRAY[SelectedMaterial].UpdateMaterial();//selected Material
                    MATERIALARRAY[SelectedMaterial].UpdateMaterialTileset();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            SeekbarGREEN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (TILELAYER == TILELAYERSTART) {
                        COLORTILELAYER0 = Color.argb(Color.alpha(COLORTILELAYER0), Color.red(COLORTILELAYER0), progress, Color.blue(COLORTILELAYER0));
                    } else if (TILELAYER == TILELAYERSTART + 1 || TILELAYER == TILELAYERSTART + 4) {
                        COLORTILELAYER1 = Color.argb(Color.alpha(COLORTILELAYER1), Color.red(COLORTILELAYER1), progress, Color.blue(COLORTILELAYER1));
                    } else if (TILELAYER == TILELAYERSTART + 2 || TILELAYER == TILELAYERSTART + 5) {
                        COLORTILELAYER2 = Color.argb(Color.alpha(COLORTILELAYER2), Color.red(COLORTILELAYER2), progress, Color.blue(COLORTILELAYER2));
                    } else if (TILELAYER == TILELAYERSTART + 3 || TILELAYER == TILELAYERSTART + 6) {
                        COLORTILELAYER3 = Color.argb(Color.alpha(COLORTILELAYER3), Color.red(COLORTILELAYER3), progress, Color.blue(COLORTILELAYER3));
                    }

                    MATERIALARRAY[SelectedMaterial].UpdateMaterial();//selected Material
                    MATERIALARRAY[SelectedMaterial].UpdateMaterialTileset();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            SeekbarBLUE.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (TILELAYER == TILELAYERSTART) {
                        COLORTILELAYER0 = Color.argb(Color.alpha(COLORTILELAYER0), Color.red(COLORTILELAYER0), Color.green(COLORTILELAYER0), progress);
                    } else if (TILELAYER == TILELAYERSTART + 1 || TILELAYER == TILELAYERSTART + 4) {
                        COLORTILELAYER1 = Color.argb(Color.alpha(COLORTILELAYER1), Color.red(COLORTILELAYER1), Color.green(COLORTILELAYER1), progress);
                    } else if (TILELAYER == TILELAYERSTART + 2 || TILELAYER == TILELAYERSTART + 5) {
                        COLORTILELAYER2 = Color.argb(Color.alpha(COLORTILELAYER2), Color.red(COLORTILELAYER2), Color.green(COLORTILELAYER2), progress);
                    } else if (TILELAYER == TILELAYERSTART + 3 || TILELAYER == TILELAYERSTART + 6) {
                        COLORTILELAYER3 = Color.argb(Color.alpha(COLORTILELAYER3), Color.red(COLORTILELAYER3), Color.green(COLORTILELAYER3), progress);
                    }
                    MATERIALARRAY[SelectedMaterial].UpdateMaterial();//selected Material
                    MATERIALARRAY[SelectedMaterial].UpdateMaterialTileset();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });


            SeekbarANIMATON.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    ANIMATIONPROGRESSPERCENT = (((float) seekBar.getProgress()) / ((float) SeekbarANIMATON.getMax()));
                    ANIMATIONPROGRESS = seekBar.getProgress();
                    MATERIALARRAY[SelectedMaterial].UpdateAnimationSeekbarColor();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    UPDATETILESET = true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    UPDATETILESET = false;
                }
            });
        }
        /**/
    }


    /**/
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            //case MotionEvent.ACTION_HOVER_ENTER:

            case MotionEvent.ACTION_DOWN:

                TOUCHSTATE = 0;

                TOUCHCUSTOMBUTTONS = false;

                TOUCHPOSITION = new Vector(event.getX(), event.getY());

                /*/
                if (OPENGL) { // update the custom buttons
                    for (int i = 0; i < CUSTOMBUTTONSLIST.size(); i++) {
                        CUSTOMBUTTONSLIST.get(i).UpdateButton();
                    }
                }/**/

                xmove2 = event.getX() - xmove;
                ymove2 = event.getY() - ymove;


                camera.move(-xmove2 + event.getX(), -ymove2 + event.getY());

                if (!OPENGL) {
                    if (DISPLAYINVENTORY) {
                        LE.setSelectTilefromInventory(event.getX(), event.getY());
                    } else {

                        KDTREECURRENTLYBUILDING = true;
                        if (FILLTILES) { // Starts the Boundarie to place many Tiles
                            LE.StartFillingTile((int) event.getX(), (int) event.getY());
                        }
                        if (!DESTROYTILES) { // Removes many Tiles
                            if (PLACETILE) {
                                LE.PlaceSelectedTile((int) event.getX(), (int) event.getY());
                            }
                        } else {
                            if (PLACETILE) { // removes single Tile
                                LE.RemoveTileKDTREE((int) event.getX(), (int) event.getY());
                            }
                        }

                        KDTREECURRENTLYBUILDING = false;
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:

                TOUCHSTATE = 1;

                TOUCHCUSTOMBUTTONS = false;
                /*/
                if (OPENGL) { // update the custom buttons
                    for (int i = 0; i < CUSTOMBUTTONSLIST.size(); i++) {
                        CUSTOMBUTTONSLIST.get(i).UpdateButton();
                    }
                }/**/

                TOUCHPOSITION = new Vector(event.getX(), event.getY());

                xmove = event.getX() - xmove2;
                ymove = event.getY() - ymove2;

                camera.move(xmove, ymove);


                if (!OPENGL) {
                    if (DISPLAYINVENTORY) {
                        LE.setSelectTilefromInventory(event.getX(), event.getY());
                    } else {

                        KDTREECURRENTLYBUILDING = true;
                        if (FILLTILES) { // Tileboundarie
                            FILLTILERECT = LE.GetFillBoundaries((int) event.getX(), (int) event.getY());
                        }

                        if (!DESTROYTILES) {
                            if (PLACETILE) {// Adds single Tile
                                LE.PlaceSelectedTile((int) event.getX(), (int) event.getY());
                            }

                        } else {
                            if (PLACETILE) {// Removes single Tiles
                                LE.RemoveTileKDTREE((int) event.getX(), (int) event.getY());
                            }
                        }

                        KDTREECURRENTLYBUILDING = false;
                    }
                }

                DRAWFILLTILERECT = true;

                break;
            case MotionEvent.ACTION_UP:

                TOUCHSTATE = 2;

                TOUCHPOSITION = new Vector(event.getX(), event.getY());

                //TOUCHCUSTOMBUTTONS = false;
                //if (OPENGL) { // update the custom buttons
                //    for (int i = 0; i < CUSTOMBUTTONSLIST.size(); i++) {
                //        CUSTOMBUTTONSLIST.get(i).UpdateButton();
                //    }
                //}
                camera.move(xmove, ymove);

                if (!OPENGL) {
                    if (DISPLAYINVENTORY) {
                        LE.SELECTEDTILEIDINVENTORY();
                    } else {

                        KDTREECURRENTLYBUILDING = true;
                        if (FILLTILES) {
                            if (!DESTROYTILES) {
                                LE.FillingTiles((int) event.getX(), (int) event.getY());
                            } else {
                                LE.RemoveTilesKDTREE((int) event.getX(), (int) event.getY());
                            }
                        }

                        KDTREECURRENTLYBUILDING = false;
                    }
                }
                DRAWFILLTILERECT = false;
                break;
        }
        return true;
    }

}
