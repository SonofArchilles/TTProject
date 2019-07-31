package com.jacobjacob.ttproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;

public class Util {


    public static int WIDTH = 1920;
    public static int HEIGHT = 1080;
    public static int WIDTHSCREEN;
    public static int HEIGHTSCREEN;
    public static int SEED = 24512654;


    //public static double  EPSILON = 0.04;

    public static double mousemovespeed = 0.3;
    public static int BACKGROUNDCOLOR = android.graphics.Color.rgb(30, 30, 120); //80,80,255
    //public static int     SCREENRESOLUTION = 50;
    public static int FOV = 25;

    public static SeekBar SeekbarALPHA;
    public static SeekBar SeekbarRED;
    public static SeekBar SeekbarGREEN;
    public static SeekBar SeekbarBLUE;
    public static SeekBar SeekbarANIMATON;

    public static Button MOVE_UP;
    public static Button MOVE_FORWARD;
    public static Button MOVE_DOWN;
    public static Button MOVE_LEFT;
    public static Button MOVE_BACK;
    public static Button MOVE_RIGHT;
    public static ToggleButton INVENTORY_TOGGLE;
    public static ToggleButton FILLPLACE;
    public static ToggleButton DRAWKDTREE;
    public static ToggleButton REMOVETILES;

    public static Button ADDLEFT;
    public static Button ADDRIGHT;
    public static Button TILEEDIT;
    public static TextView DISPLAYFRAMES;


    public static Generate GENERATE = new Generate();
    public static OpenSimplexNoise NOISE = new OpenSimplexNoise(SEED);

    public static Vector Cameraposition = new Vector(0, 0, -320); // -10 tiletexture 1
    public static Vector CameraZ = new Vector(0, 0, 1);
    public static Camera camera = new Camera(Cameraposition, CameraZ);
    public static Move MOVE = new Move();

    //public static ImageView IMAGE;
    public static boolean UPDATEVIEW = false;
    public static Context CONTEXT;

    //public static Drawable Tilemap = CONTEXT.getResources().getDrawable(R.drawable.tilemap);
    //public static Bitmap Tilemapbmp = ((BitmapDrawable) Tilemap).getBitmap();
    //public static TILESIZE = Tilemapbmp.getWidth() / 20;

    public static long FRAMETIME;   //FPS
    public static long FRAMETIMESTART;//FPS
    public static int FRAMES; // TILES Animations

    public static RenderTiles RENDERERTILES;

    public static Thread RENDERER;
    public static Thread TILEUPDATE;

    public static float FPS = 120;
    public static boolean RUNNING = true;
    public static long LASTUPDATETIME = System.currentTimeMillis();
    public static long LASTUPDATETIMETILEUPDATE = System.currentTimeMillis();
    public static long STARTTIME = System.currentTimeMillis();
    public static long MINUPDATETIME = (long) (1000 / FPS);
    public static int FRAME = 0;
    public static boolean FRAMEDRAWN = true;


    public static int TILESIZE;
    public static int TILESIZETEXTURE;
    public static int TILESIZEORIGINAL = 16;
    public static int TILESOFSINGLEKIND = 5;
    public static double movespeed = 3/****/
            ; // does not matter, changed in update
    public static Tiletexture TILETEXTURE;

    public static int TEXTUREWIDTH = 20;

    public static String FILE_NAME = "newtextfile.txt";

    public static ArrayList<String> FILE_NAMES = new ArrayList<>(Arrays.asList("lvla.txt", "lvlb.txt", "lvlc.txt", "lvld.txt", "lvle.txt"));
    public static Spinner LOADLVLSPINNER;

    /**
     * INVENTORY
     **/

    public static ArrayList<Tile> INVENTORY = new ArrayList<>();
    public static ArrayList<Integer> SELECTEDIDINVENTORY = new ArrayList<>();
    public static int INVENTORYSIZE = 7;
    public static boolean INVENTORYVERTICAL = false;
    public static float INVENTORYDISPLAYSIZE = 4;

    public static LevelEditor LE = new LevelEditor();
    public static WriteFile WF = new WriteFile();
    public static ReadFile RF = new ReadFile();

    public static boolean DISPAYTOAST = false;
    public static boolean DISPLAYINVENTORY = false;

    public static boolean PLACETILE = true;
    public static boolean FILLTILES = false;
    public static boolean DESTROYTILES = false;

    public static Rect FILLTILERECT = new Rect();
    public static int MAXFILLTILES = 1300;          // maximum ammount of Tiles you can place at once
    public static boolean DRAWFILLTILERECT = false; // Rectangle when you want to fill an area


    public static int INVENTORYBACKGROUNDCOLOR = Color.argb(150, 120, 120, 120);
    public static int INVENTORYSELECTTILECOLOR = Color.argb(150, 0, 255, 0);
    public static int CHUNKCOLOR = Color.argb(130, 255, 0, 0);
    public static int FILLTILECOLOR = Color.argb(150, 0, 255, 0);

    public static int TILELAYER = 0;

    public static int COLORTILELAYER1;// = Color.rgb(255,235,145);
    public static int COLORTILELAYER2;// = Color.rgb(130,120,50);
    public static int COLORTILELAYER3;// = Color.rgb(150,200,70);
    public static float ANIMATIONPROGRESSPERCENT;
    public static int ANIMATIONPROGRESS;


    //public static ArrayList<Material> MATERIALARRAY = new ArrayList<>();
    public static Bitmap[][] MATERIALLIST;
    public static Bitmap[][] MATERIALLISTUPDATING;
    public static Material[] MATERIALARRAY = new Material[TEXTUREWIDTH * TEXTUREWIDTH];
    public static Material STARTINGMATERIAL = new Material(1, 0, 0, 0, Color.argb(255, 120, 170, 100), Color.argb(255, 174, 160, 108), Color.argb(255, 10, 255, 255));

    public static KdTree KDTREE = new KdTree();
    public static KdTree KDTREECOPY = KDTREE;
    public static int KDTREEMAXITEMS = 80;
    public static boolean DRAWKDTREEBOOL;
    public static boolean KDTREECURRENTLYBUILDING = true;
    public static boolean KDTREECOPYING = false;

    public static int ZOOMFACTOR = 1000;
    public static int ANIMATIONTIME = 10000; //in ms // = 1s for the 2 to TEXTUREWIDTH * TEXTUREWIDTH frames

    public static Portal PORTAL = new Portal(20, 0, 1);
    public static Portal PORTAL0 = new Portal(0, 20, 0);
    public static ArrayList<Portal> PORTALLIST = new ArrayList<>(Arrays.asList(PORTAL, PORTAL0));
    public static boolean BOTHPORTALSACTIVE = true;

    public static Rect HITBOX;
}
