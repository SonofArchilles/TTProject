package com.jacobjacob.ttproject;

import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import static com.jacobjacob.ttproject.Util.KDTREECOPY;
import static com.jacobjacob.ttproject.Util.KDTREECURRENTLYBUILDING;
import static com.jacobjacob.ttproject.Util.PORTALLIST;
import static com.jacobjacob.ttproject.Util.TILESIZE;
import static com.jacobjacob.ttproject.Util.camera;

//import static com.jacobjacob.ttproject.RenderTiles.kdtreeCopy;

public class Portal {

    private int RawX, RawY;
    private float PositionX, PositionY;
    private Vector Position;
    //private boolean Active = true;
    private ArrayList<Tile> Visible = new ArrayList<>();
    private int Portal, Width = 10, Height = 10;
/*/
    public Portal(Vector Position, int Portal) {
        this.Portal = Portal;
        this.PositionX = (float) (Position.getValue(0));
        this.PositionY = (float) (Position.getValue(1));
        this.RawX = (int) (Position.getValue(0) / TILESIZE);
        this.RawY = (int) (Position.getValue(1) / TILESIZE);
        this.Position = Position;
    }/**/

    public Portal(int RawX, int RawY, int Portal) {
        this.Portal = Portal;
        this.RawX = RawX;
        this.RawY = RawY;
        this.PositionX = (float) (RawX * TILESIZE);
        this.PositionY = (float) (RawY * TILESIZE);
        this.Position = new Vector((RawX * TILESIZE), (RawY * TILESIZE));
    }

    //public void setPositionRaw(int RawX, int RawY) {
    //    this.RawX = RawX;
    //    this.RawY = RawY;
    //    this.PositionX = (float) (RawX * TILESIZE);
    //    this.PositionY = (float) (RawY * TILESIZE);
    //    this.Position = new Vector((float) (RawX * TILESIZE), (float) (RawY * TILESIZE));
    //}

    public void setPosition(Vector Position) {
        this.PositionX = (float) (Position.getValue(0));
        this.PositionY = (float) (Position.getValue(1));
        this.RawX = (int) (this.PositionX / TILESIZE);
        this.RawY = (int) (this.PositionY / TILESIZE);
        this.Position = Position;
    }

    public Vector getPosition() {
        return this.Position;
    }

    public Vector getPositionRAW() {
        return new Vector(this.RawX, this.RawY);
    }

    public int getPortalPartner() { // only 2 Portals possible
        return this.Portal == 1 ? 1 : 0;
    }


    public Rect getBoundarie() {
        return new Rect(this.RawX - this.Width, this.RawY - this.Height, this.RawX + this.Width, this.RawY + this.Height);
    }

    public Rect getBoundarieOnScreen() {
        Vector A = new Vector(this.RawX - this.Width - 1, this.RawY - this.Height - 1);
        Vector B = new Vector(this.RawX + this.Width + 1, this.RawY + this.Height + 1);
        A = A.multiplydouble(TILESIZE);
        B = B.multiplydouble(TILESIZE);
        A = A.getScreencoordinatesFromTileCoordinates(A);
        B = B.getScreencoordinatesFromTileCoordinates(B);
        return new Rect((int) A.getValue(0), (int) A.getValue(1), (int) B.getValue(0), (int) B.getValue(1));
    }

    public ArrayList<Tile> getVisibleTiles() {      // returns the Tiles that should be Visible Through the Portal
        this.Position = new Vector((RawX * TILESIZE), (RawY * TILESIZE));
        if (!KDTREECURRENTLYBUILDING) {
            this.Visible.clear();

            Vector EyeXY = new Vector(camera.getEye2D().getValue(0), camera.getEye2D().getValue(1)); // 2d Camera Vector

            Vector PortaltoCamera = (EyeXY.subtract(/*/this./*/PORTALLIST.get(this.getPortalPartner())./**/getPosition()));

            //EyeXY = EyeXY.addVector((EyeXY.subtract(this.Position)).normalize().multiplydouble(-TILESIZE));

            Vector A = ((new Vector(0, 0, 1).cross(PortaltoCamera)).normalize()).multiplydouble(TILESIZE * 5); // perpendicular to camera and this Portal

            Vector XY1 = (this.Position.addVector(A)).subtract(EyeXY);
            Vector XY2 = (this.Position.subtract(A)).subtract(EyeXY);


            XY1 = (XY1.normalize()).multiplydouble(40);
            XY2 = (XY2.normalize()).multiplydouble(40);

            int X2 = (int) (this.RawX + XY1.getValue(0));
            int Y2 = (int) (this.RawY + XY1.getValue(1));

            int X3 = (int) (this.RawX + XY2.getValue(0));
            int Y3 = (int) (this.RawY + XY2.getValue(1));

            Rect PortalSurrounding = PORTALLIST.get(getPortalPartner()).getBoundarie();

            Visible = KDTREECOPY.getTilesInsideBoundary(PortalSurrounding);

            Vector DifferencePortalsRAW = (this.getPositionRAW().subtract(PORTALLIST.get(getPortalPartner()).getPositionRAW()));
            int DifferencePortalsRAWX = (int) DifferencePortalsRAW.getValue(0);
            int DifferencePortalsRAWY = (int) DifferencePortalsRAW.getValue(1);

            float Area = area(this.RawX, this.RawY, X2, Y2, X3, Y3);

            for (int i = 0; i < Visible.size(); i++) { // test, if The Tiles are inside this Triangle
                //if (Visible.get(i) != null) {
                    try {
                        int TileX = (int) Visible.get(i).getPositionRAW().getValue(0);
                        int TileY = (int) Visible.get(i).getPositionRAW().getValue(1);

                        if (!isInside(this.RawX, this.RawY, X2, Y2, X3, Y3, (int) TileX + DifferencePortalsRAWX, (int) TileY + DifferencePortalsRAWY, Area)) {
                            //int size = Visible.size();
                            try {
                                Visible.remove(i);
                                i--;
                            } catch (Exception e) {
                                System.out.print("oppsie doopsie Portal broke 1");

                                Log.d("Portal","oppsie doopsie Portal broke 1");
                            }
                        }
                    } catch (Exception e) {
                        System.out.print("oppsie doopsie Portal broke 2");
                        Log.d("Portal","oppsie doopsie Portal broke 2");
                    }
                //}
            }
        }
        try {

            return new ArrayList<>(Visible);
        }catch (Exception e){
            System.out.print("oppsie doopsie Portal broke 3");
            Log.d("Portal","oppsie doopsie Portal broke 3");
        }
        return null;
    }

    static boolean isInside(int x1, int y1, int x2, int y2, int x3, int y3, int XTile, int YTile,float Area) { /// RAW Positions Area must be float, int = weird pattern

        /* Calculate area of triangle PBC */
        float A1 = area(XTile, YTile, x2, y2, x3, y3);

        /* Calculate area of triangle PAC */
        float A2 = area(x1, y1, XTile, YTile, x3, y3);

        /* Calculate area of triangle PAB */
        float A3 = area(x1, y1, x2, y2, XTile, YTile);

        /* Check if sum of A1, A2 and A3 is same as A */
        return (Area == A1 + A2 + A3);
    }

    static float area(int x1, int y1, int x2, int y2, int x3, int y3) {
        return (float) Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0);
    }
}
