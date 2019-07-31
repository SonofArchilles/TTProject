package com.jacobjacob.ttproject;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import static com.jacobjacob.ttproject.LevelEditor.SelectedMaterial;
import static com.jacobjacob.ttproject.Util.ADDLEFT;
import static com.jacobjacob.ttproject.Util.ADDRIGHT;
import static com.jacobjacob.ttproject.Util.ANIMATIONPROGRESS;
import static com.jacobjacob.ttproject.Util.ANIMATIONPROGRESSPERCENT;
import static com.jacobjacob.ttproject.Util.COLORTILELAYER1;
import static com.jacobjacob.ttproject.Util.COLORTILELAYER2;
import static com.jacobjacob.ttproject.Util.COLORTILELAYER3;
import static com.jacobjacob.ttproject.Util.CONTEXT;
import static com.jacobjacob.ttproject.Util.DESTROYTILES;
import static com.jacobjacob.ttproject.Util.DISPAYTOAST;
import static com.jacobjacob.ttproject.Util.DISPLAYFRAMES;
import static com.jacobjacob.ttproject.Util.DISPLAYINVENTORY;
import static com.jacobjacob.ttproject.Util.DRAWFILLTILERECT;
import static com.jacobjacob.ttproject.Util.DRAWKDTREE;
import static com.jacobjacob.ttproject.Util.DRAWKDTREEBOOL;
import static com.jacobjacob.ttproject.Util.FILE_NAME;
import static com.jacobjacob.ttproject.Util.FILE_NAMES;
import static com.jacobjacob.ttproject.Util.FILLPLACE;
import static com.jacobjacob.ttproject.Util.FILLTILERECT;
import static com.jacobjacob.ttproject.Util.FILLTILES;
import static com.jacobjacob.ttproject.Util.FRAMES;
import static com.jacobjacob.ttproject.Util.FRAMETIME;
import static com.jacobjacob.ttproject.Util.HEIGHT;
import static com.jacobjacob.ttproject.Util.HEIGHTSCREEN;
import static com.jacobjacob.ttproject.Util.INVENTORY_TOGGLE;
import static com.jacobjacob.ttproject.Util.KDTREECURRENTLYBUILDING;
import static com.jacobjacob.ttproject.Util.LE;
import static com.jacobjacob.ttproject.Util.LOADLVLSPINNER;
import static com.jacobjacob.ttproject.Util.MATERIALARRAY;
import static com.jacobjacob.ttproject.Util.MOVE_BACK;
import static com.jacobjacob.ttproject.Util.MOVE_DOWN;
import static com.jacobjacob.ttproject.Util.MOVE_FORWARD;
import static com.jacobjacob.ttproject.Util.MOVE_LEFT;
import static com.jacobjacob.ttproject.Util.MOVE_RIGHT;
import static com.jacobjacob.ttproject.Util.MOVE_UP;
import static com.jacobjacob.ttproject.Util.NOISE;
import static com.jacobjacob.ttproject.Util.PLACETILE;
import static com.jacobjacob.ttproject.Util.REMOVETILES;
import static com.jacobjacob.ttproject.Util.RENDERER;
import static com.jacobjacob.ttproject.Util.SeekbarALPHA;
import static com.jacobjacob.ttproject.Util.SeekbarANIMATON;
import static com.jacobjacob.ttproject.Util.SeekbarBLUE;
import static com.jacobjacob.ttproject.Util.SeekbarGREEN;
import static com.jacobjacob.ttproject.Util.SeekbarRED;
import static com.jacobjacob.ttproject.Util.TEXTUREWIDTH;
import static com.jacobjacob.ttproject.Util.TILEEDIT;
import static com.jacobjacob.ttproject.Util.TILELAYER;
import static com.jacobjacob.ttproject.Util.TILETEXTURE;
import static com.jacobjacob.ttproject.Util.TILEUPDATE;
import static com.jacobjacob.ttproject.Util.WIDTH;
import static com.jacobjacob.ttproject.Util.WIDTHSCREEN;
import static com.jacobjacob.ttproject.Util.camera;
import static com.jacobjacob.ttproject.Util.movespeed;

public class MainActivity extends AppCompatActivity {

    public float xmove, ymove, xmove2, ymove2, height;
    public static ImageView IMAGE;
    public static boolean UHANDELERSTARTED = false;

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

        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        TypedArray a = getApplicationContext().obtainStyledAttributes(new TypedValue().data, textSizeAttr);
        height = a.getDimensionPixelSize(0, 0);
        a.recycle();


        IMAGE = findViewById(R.id.SCREEN);
        CONTEXT = getApplicationContext();
        TILETEXTURE = new Tiletexture();

        LE.Inventory();

        NOISE.setSeed(System.currentTimeMillis() / 1000);

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
        FILLPLACE = findViewById(R.id.FILLPLACE);
        DRAWKDTREE = findViewById(R.id.DRAWKDTREE);
        ADDLEFT = findViewById(R.id.ADDLEFT);
        ADDRIGHT = findViewById(R.id.ADDRIGHT);
        DISPLAYFRAMES = findViewById(R.id.DISPLAYFRAMES);
        REMOVETILES = findViewById(R.id.REMOVETILES);

        /**/
        DISPLAYFRAMES.setVisibility(View.INVISIBLE);
        ADDLEFT.setVisibility(View.INVISIBLE);
        ADDRIGHT.setVisibility(View.INVISIBLE);
        REMOVETILES.setVisibility(View.INVISIBLE);
        DRAWKDTREE.setVisibility(View.INVISIBLE);
        FILLPLACE.setVisibility(View.INVISIBLE);

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
        myAdapter.setDropDownViewResource(/**/android.R.layout.simple_spinner_dropdown_item/**/);
        LOADLVLSPINNER.setAdapter(myAdapter);


        LOADLVLSPINNER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LE.SaveLevel();
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
                    /***///DISPLAYFRAMES.setVisibility(View.INVISIBLE);
                    /***///ADDLEFT.setVisibility(View.INVISIBLE);
                    /***///ADDRIGHT.setVisibility(View.INVISIBLE);


                    REMOVETILES.setVisibility(View.INVISIBLE);
                    DRAWKDTREE.setVisibility(View.INVISIBLE);
                    FILLPLACE.setVisibility(View.INVISIBLE);


                } else {
                    /***///DISPLAYFRAMES.setVisibility(View.VISIBLE);
                    /***///ADDLEFT.setVisibility(View.VISIBLE);
                    /***///ADDRIGHT.setVisibility(View.VISIBLE);


                    REMOVETILES.setVisibility(View.VISIBLE);
                    DRAWKDTREE.setVisibility(View.VISIBLE);
                    FILLPLACE.setVisibility(View.VISIBLE);


                }
                //uHandeler.updateScreen();
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
                //uHandeler.updateScreen();
            }
        });


        ADDLEFT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FRAMES--;

                if (FRAMES < -TEXTUREWIDTH * TEXTUREWIDTH) {
                    FRAMES = TEXTUREWIDTH * TEXTUREWIDTH;
                }

                if (TILELAYER < 4) {
                    MATERIALARRAY[SelectedMaterial].setLayer123(FRAMES);
                    MATERIALARRAY[SelectedMaterial].UpdateMaterialTileset();
                    DISPLAYFRAMES.setText("Material: " + String.valueOf(FRAMES));
                } else {
                    DISPLAYFRAMES.setText("Animationtime: " + String.valueOf(FRAMES) + "s");
                    MATERIALARRAY[SelectedMaterial].setAnimationtime(FRAMES);
                }

                //uHandeler.updateScreen();
            }
        });
        ADDRIGHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FRAMES++;


                if (FRAMES > TEXTUREWIDTH * TEXTUREWIDTH) {
                    FRAMES = -TEXTUREWIDTH * TEXTUREWIDTH;
                }

                if (TILELAYER < 4) {
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
                if (TILELAYER > 6) {
                    TILELAYER = 0;
                }
                if (TILELAYER == 0) {
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
                } else if (TILELAYER < 4) {

                    TILEEDIT.setText("LAYER: " + String.valueOf(TILELAYER));
                    DISPLAYFRAMES.setText("Material: " + String.valueOf(FRAMES));
                    SeekbarALPHA.setVisibility(View.VISIBLE);
                    SeekbarRED.setVisibility(View.VISIBLE);
                    SeekbarGREEN.setVisibility(View.VISIBLE);
                    SeekbarBLUE.setVisibility(View.VISIBLE);
                    SeekbarANIMATON.setVisibility(View.INVISIBLE);

                    ADDLEFT.setVisibility(View.VISIBLE);
                    ADDRIGHT.setVisibility(View.VISIBLE);
                    DISPLAYFRAMES.setVisibility(View.VISIBLE);
                } else {
                    DISPLAYFRAMES.setText("Animationtime: " + String.valueOf(FRAMES) + "s");
                    TILEEDIT.setText("ANIMATIONLAYER: " + String.valueOf(TILELAYER - 3));
                    SeekbarANIMATON.setVisibility(View.VISIBLE);
                }

            }
        });

        SeekbarALPHA.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (TILELAYER == 1 || TILELAYER == 4) {
                    COLORTILELAYER1 = Color.argb(progress, Color.red(COLORTILELAYER1), Color.green(COLORTILELAYER1), Color.blue(COLORTILELAYER1));
                } else if (TILELAYER == 2 || TILELAYER == 5) {
                    COLORTILELAYER2 = Color.argb(progress, Color.red(COLORTILELAYER2), Color.green(COLORTILELAYER2), Color.blue(COLORTILELAYER2));
                } else if (TILELAYER == 3 || TILELAYER == 6) {
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
                if (TILELAYER == 1 || TILELAYER == 4) {
                    COLORTILELAYER1 = Color.argb(Color.alpha(COLORTILELAYER1), progress, Color.green(COLORTILELAYER1), Color.blue(COLORTILELAYER1));
                } else if (TILELAYER == 2 || TILELAYER == 5) {
                    COLORTILELAYER2 = Color.argb(Color.alpha(COLORTILELAYER2), progress, Color.green(COLORTILELAYER2), Color.blue(COLORTILELAYER2));
                } else if (TILELAYER == 3 || TILELAYER == 6) {
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
                if (TILELAYER == 1 || TILELAYER == 4) {
                    COLORTILELAYER1 = Color.argb(Color.alpha(COLORTILELAYER1), Color.red(COLORTILELAYER1), progress, Color.blue(COLORTILELAYER1));
                } else if (TILELAYER == 2 || TILELAYER == 5) {
                    COLORTILELAYER2 = Color.argb(Color.alpha(COLORTILELAYER2), Color.red(COLORTILELAYER2), progress, Color.blue(COLORTILELAYER2));
                } else if (TILELAYER == 3 || TILELAYER == 6) {
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
                if (TILELAYER == 1 || TILELAYER == 4) {
                    COLORTILELAYER1 = Color.argb(Color.alpha(COLORTILELAYER1), Color.red(COLORTILELAYER1), Color.green(COLORTILELAYER1), progress);
                } else if (TILELAYER == 2 || TILELAYER == 5) {
                    COLORTILELAYER2 = Color.argb(Color.alpha(COLORTILELAYER2), Color.red(COLORTILELAYER2), Color.green(COLORTILELAYER2), progress);
                } else if (TILELAYER == 3 || TILELAYER == 6) {
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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                Display display = getWindowManager().getDefaultDisplay();
                android.graphics.Point size = new Point();
                display.getSize(size);
                WIDTHSCREEN = size.x;
                HEIGHTSCREEN = size.y;

                if (WIDTH <= HEIGHT/* && WIDTHSCREEN > HEIGHTSCREEN*/) {
                    int width = HEIGHT;
                    HEIGHT = WIDTH;
                    WIDTH = width;
                } /* if (WIDTH < HEIGHT && WIDTHSCREEN > HEIGHTSCREEN)*/

                xmove2 = event.getX() - xmove;
                ymove2 = event.getY() - ymove;
                camera.move(-xmove2 + event.getX(), -ymove2 + event.getY());


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


                //uHandeler.updateScreen();

                break;
            case MotionEvent.ACTION_MOVE:

                xmove = event.getX() - xmove2;
                ymove = event.getY() - ymove2;

                camera.move(xmove, ymove);


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

                DRAWFILLTILERECT = true;

                //UpdateHandler updateHandler = new UpdateHandler();
                //updateHandler.updateScreen();

                break;
            case MotionEvent.ACTION_UP:
                camera.move(xmove, ymove);
                //Toast.makeText(getApplicationContext(), "X: " + camera.getW().getX() + " " + "Y: " + camera.getW().getY() + " " + "Z: " + camera.getW().getZ() + " " + "Length: " + camera.getW().length(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "X: " + camera.getEye().getX() + " " + "Y: " + camera.getEye().getY() + " " + "Z: " + camera.getEye().getZ(), Toast.LENGTH_SHORT).show();
                if (FRAMETIME > 0 && DISPAYTOAST) {
                    Toast.makeText(getApplicationContext(), "FRAMERATE: " + (1000 / FRAMETIME) + " FPS", Toast.LENGTH_SHORT).show();
                }
                if (DISPLAYINVENTORY) LE.SELECTEDTILEIDINVENTORY();
                else {

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
                DRAWFILLTILERECT = false;


                //uHandeler.updateScreen();
                //Toast.makeText(getApplicationContext(), String.valueOf((float) camera.getEye2D().getValue(2) / ZOOMFACTOR), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
