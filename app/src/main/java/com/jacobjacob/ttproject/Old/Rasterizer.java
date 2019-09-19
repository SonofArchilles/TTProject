package com.jacobjacob.ttproject.Old;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jacobjacob.ttproject.Matrix;
import com.jacobjacob.ttproject.Old.Triangle;
import com.jacobjacob.ttproject.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.jacobjacob.ttproject.Util.*;

public class Rasterizer {
    static Bitmap bmp;
    static Canvas canvas;
    Paint paint = new Paint(BACKGROUNDCOLOR);
    static ArrayList<Object> objectsrendering = new ArrayList<>();
    static ArrayList<Object> objectstorender = new ArrayList<>();
    static ArrayList<Triangle> clippedTriangles = new ArrayList<>();
    float pDepthBuffer[][] = new float[WIDTH][HEIGHT];
    int colorR;
    int colorG;
    int colorB;


    public void InitializeRasterizer() {
        pDepthBuffer = new float[WIDTH][HEIGHT];
        bmp = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bmp);
        objectstorender.clear();
        objectsrendering.clear();
        clippedTriangles.clear();
        paint.setColor(BACKGROUNDCOLOR);
        canvas.drawRect(0, 0, WIDTH, HEIGHT, paint);
        paint.setColor(Color.rgb(200,200,255));
    }

    public void RenderImage(ArrayList<Object> ObjectsToRasterize) { // Rendering pipeline
        objectstorender.addAll(ObjectsToRasterize);
        resetDepthBuffer();
        for (Object o: objectstorender){
            if (o instanceof Triangle) {
                CreateTriangles(o);
                //ClipTriangles(o);/**Clipping Routine missing!!**/ // Clipping against the screen
            }
            //else if (o instanceof Vector){
            //    CreateLines();
            //}
        }
        ClipTriangles();
        //objectsrendering.clear();
        //objectsrendering.addAll(this.clippedTriangles);

        Sortlist();

        DrawTriangle(); // objectsrendering
    }

    public Bitmap getBmp() { // Returns the rendered Image
        return bmp;
    }


    private void resetDepthBuffer() {
        for (int i = 0; i < WIDTH; i++) {// all values of the depth buffer have the value 1
            for (int j = 0; j < HEIGHT; j++) {
                pDepthBuffer[i][j] = Float.MAX_VALUE - 1;
            }
        }
    }

    private void CreateTriangles(Object o) {

        Matrix matCamera = new Matrix();
        matCamera = matCamera.Matrix_PointAt(camera.getEye(), camera.getW(), new Vector(0, 1, 0));

        Matrix matView = matCamera.Matrix_QuickInverse(matCamera);

        //for (Object o : objectstorender) {

            Vector normal = o.getNormal();
            Vector position = o.getVector(0);

            //Vector visibleposition = position.subtract(camera.getEye());
            float visible = (float) position.subtract(camera.getEye()).skalarprodukt(normal.negate());

            if (visible >= 0 || true) { // does not work correctly // true = Triangle visible from both sides
                // Double sided or visible

                // 3d --> 2d                    original Vector, rotated Vector, Matrix()

                Triangle viewed = new Triangle(new Vector(), new Vector(), new Vector());

                viewed.setVector((new Vector().Matrix_MultiplyVector(matView, o.getVector(0))), 0);
                viewed.setVector((new Vector().Matrix_MultiplyVector(matView, o.getVector(1))), 1);
                viewed.setVector((new Vector().Matrix_MultiplyVector(matView, o.getVector(2))), 2);

                //Triangle triViewed = viewed; //new Triangle(a, b, c, Color.rgb(red, green, blue));


                // Clip Viewed Triangle against near plane, this could form two additional
                // additional triangles.


                int nClippedTriangles;
                Triangle clipped[] = new Triangle[2];
                clipped[0] = viewed;
                clipped[1] = viewed;

                nClippedTriangles = 0;
                ArrayList<Triangle> clippedTriangles = new Vector().Triangle_ClipAgainstPlane(new Vector(0.0f, 0.0f, 0.1f ), new Vector(0.0f, 0.0f, 1.0f ), viewed);
                nClippedTriangles = clippedTriangles.size();
                if (nClippedTriangles >= 1){
                    clipped[0] = clippedTriangles.get(0);
                    if (nClippedTriangles >= 2) {
                        clipped[1] = clippedTriangles.get(1);
                    }
                }

                // We may end up with multiple triangles form the clip, so project as
                // required
                /**/

                if (clipped[1].getVector(0).getValue(2) > 0 & clipped[1].getVector(1).getValue(2) > 0 & clipped[1].getVector(2).getValue(2) > 0) {
                    for (int n = 0; n < nClippedTriangles; n++) {

                        // Project triangles from 3D --> 2D
                        Matrix mat = new Matrix();

                        Triangle triProjected = new Triangle(new Vector().Matrix_MultiplyVector(mat.ProjectionMatrix(), clipped[n].getVector(0)), new Vector().Matrix_MultiplyVector(mat.ProjectionMatrix(), clipped[n].getVector(1)), new Vector().Matrix_MultiplyVector(mat.ProjectionMatrix(), clipped[n].getVector(2)));
                        triProjected.setVector(new Vector().Matrix_MultiplyVector(mat.ProjectionMatrix(), clipped[n].getVector(0)), 0);
                        triProjected.setVector(new Vector().Matrix_MultiplyVector(mat.ProjectionMatrix(), clipped[n].getVector(1)), 1);
                        triProjected.setVector(new Vector().Matrix_MultiplyVector(mat.ProjectionMatrix(), clipped[n].getVector(2)), 2);

                        // Scale into view, we moved the normalising into cartesian space
                        // out of the matrix.vector function from the previous videos, so
                        // do this manually
                        Triangle tridouble = new Triangle(triProjected.getVector(0).multiplydouble((float) (1 / triProjected.getVector(0).getW())), triProjected.getVector(1).multiplydouble((float) (1 / triProjected.getVector(1).getW())), triProjected.getVector(2).multiplydouble((float) (1 / triProjected.getVector(2).getW())));
                        tridouble.setVector(triProjected.getVector(0).multiplydouble((float) (1 / triProjected.getVector(0).getW())), 0);
                        tridouble.setVector(triProjected.getVector(1).multiplydouble((float) (1 / triProjected.getVector(1).getW())), 1);
                        tridouble.setVector(triProjected.getVector(2).multiplydouble((float) (1 / triProjected.getVector(2).getW())), 2);

                        // X/Y are inverted so put them back
                        triProjected.setVector(new Vector(-tridouble.getVector(0).getValue(0), -tridouble.getVector(0).getValue(1), tridouble.getVector(0).getValue(2)), 0);
                        triProjected.setVector(new Vector(-tridouble.getVector(1).getValue(0), -tridouble.getVector(1).getValue(1), tridouble.getVector(1).getValue(2)), 1);
                        triProjected.setVector(new Vector(-tridouble.getVector(2).getValue(0), -tridouble.getVector(2).getValue(1), tridouble.getVector(2).getValue(2)), 2);


                        // Offset verts into visible normalised space
                        tridouble.setVector(triProjected.getVector(0).addVector(new Vector(1, 1, 0)), 0);
                        tridouble.setVector(triProjected.getVector(1).addVector(new Vector(1, 1, 0)), 1);
                        tridouble.setVector(triProjected.getVector(2).addVector(new Vector(1, 1, 0)), 2);


                        triProjected.setVector(new Vector(tridouble.getVector(0).getValue(0) * WIDTH / 2, tridouble.getVector(0).getValue(1) * HEIGHT / 2, tridouble.getVector(0).getValue(2)), 0);
                        triProjected.setVector(new Vector(tridouble.getVector(1).getValue(0) * WIDTH / 2, tridouble.getVector(1).getValue(1) * HEIGHT / 2, tridouble.getVector(1).getValue(2)), 1);
                        triProjected.setVector(new Vector(tridouble.getVector(2).getValue(0) * WIDTH / 2, tridouble.getVector(2).getValue(1) * HEIGHT / 2, tridouble.getVector(2).getValue(2)), 2);

                        objectsrendering.add(triProjected);
                    }
                }
            }
        //}
    }
    void CreateLines(){
        Matrix matCamera = new Matrix();
        matCamera = matCamera.Matrix_PointAt(camera.getEye(), camera.getW(), new Vector(0, 1, 0));

        Matrix matView = matCamera.Matrix_QuickInverse(matCamera);

        for (Object o : objectstorender) {

            Vector position = o.getVector(0);

            Triangle viewed = new Triangle(new Vector(), new Vector(), new Vector());

            viewed.setVector((new Vector().Matrix_MultiplyVector(matView, o.getVector(0))), 0);

            //Triangle triViewed = viewed; //new Triangle(a, b, c, Color.rgb(red, green, blue));


            // Clip Viewed Triangle against near plane, this could form two additional
            // additional triangles.


            int nClippedTriangles;
            Triangle clipped[] = new Triangle[2];
            clipped[0] = viewed;
            clipped[1] = viewed;

            nClippedTriangles = 1;


            // We may end up with multiple triangles form the clip, so project as
            // required
            /**/

            if (clipped[1].getVector(0).getValue(2) > 0 & clipped[1].getVector(1).getValue(2) > 0 & clipped[1].getVector(2).getValue(2) > 0) {
                for (int n = 0; n < nClippedTriangles; n++) {

                    // Project triangles from 3D --> 2D
                    Matrix mat = new Matrix();

                    Triangle triProjected = new Triangle(new Vector().Matrix_MultiplyVector(mat.ProjectionMatrix(), clipped[n].getVector(0)), new Vector().Matrix_MultiplyVector(mat.ProjectionMatrix(), clipped[n].getVector(1)), new Vector().Matrix_MultiplyVector(mat.ProjectionMatrix(), clipped[n].getVector(2)));
                    triProjected.setVector(new Vector().Matrix_MultiplyVector(mat.ProjectionMatrix(), clipped[n].getVector(0)), 0);

                    // Scale into view, we moved the normalising into cartesian space
                    // out of the matrix.vector function from the previous videos, so
                    // do this manually
                    Triangle tridouble = new Triangle(triProjected.getVector(0).multiplydouble((float) (1 / triProjected.getVector(0).getW())), triProjected.getVector(1).multiplydouble((float) (1 / triProjected.getVector(1).getW())), triProjected.getVector(2).multiplydouble((float) (1 / triProjected.getVector(2).getW())));
                    tridouble.setVector(triProjected.getVector(0).multiplydouble((float) (1 / triProjected.getVector(0).getW())), 0);

                    // X/Y are inverted so put them back
                    triProjected.setVector(new Vector(-tridouble.getVector(0).getValue(0), -tridouble.getVector(0).getValue(1), tridouble.getVector(0).getValue(2)), 0);
                    // Offset verts into visible normalised space
                    tridouble.setVector(triProjected.getVector(0).addVector(new Vector(1, 1, 0)), 0);
                    triProjected.setVector(new Vector(tridouble.getVector(0).getValue(0) * WIDTH / 2, tridouble.getVector(0).getValue(1) * HEIGHT / 2, tridouble.getVector(0).getValue(2)), 0);

                    objectsrendering.add(triProjected);
                }
            }
        }
    }

    private void ClipTriangles() {
        /*for (Object o : objectstorender){
            Triangle Old = new Triangle(o.getVector(0),o.getVector(1),o.getVector(2));
            if (0 < o.getVector(0).getValue(0) && o.getVector(0).getValue(0) < WIDTH &&
                    0 < o.getVector(1).getValue(0) && o.getVector(0).getValue(0) < WIDTH &&
                    0 < o.getVector(2).getValue(0) && o.getVector(0).getValue(0) < WIDTH &&

                    0 < o.getVector(0).getValue(0) && o.getVector(0).getValue(0) < HEIGHT &&
                    0 < o.getVector(1).getValue(0) && o.getVector(0).getValue(0) < HEIGHT &&
                    0 < o.getVector(2).getValue(0) && o.getVector(0).getValue(0) < HEIGHT){

                clippedTriangles.add(Old);
            } // Triangle inside Screen




        }
        objectsrendering.clear();
        objectsrendering.addAll(clippedTriangles);*/
    }

    private void clipPolygon() {


    }
    private void Sortlist() {
        Collections.sort(objectsrendering, new Comparator<Object>() {
            @Override
            public int compare(Object t0, Object t1) {
                float t0Z = (float) (t0.getVector(1).getValue(2));
                float t1Z = (float) (t1.getVector(1).getValue(2));

                return Float.valueOf(t1Z).compareTo(t0Z);

            }
        });
    }


    private void DrawTriangle() {
        for (Object o : objectsrendering) {
            Vector pv0 = o.getVector(0);
            Vector pv1 = o.getVector(1);
            Vector pv2 = o.getVector(2);


            if (pv1.getY() < pv0.getY()) {
                Vector a = pv0;
                pv0 = pv1;
                pv1 = a;
            }
            if (pv2.getY() < pv1.getY()) {
                Vector a = pv1;
                pv1 = pv2;
                pv2 = a;
            }
            if (pv1.getY() < pv0.getY()) {
                Vector a = pv0;
                pv0 = pv1;
                pv1 = a;
            }
            if (pv0.getY() == pv1.getY()) { // natural flat top
                //sorting vertices
                if (pv1.getX() < pv0.getX()) {
                    Vector a = pv0;
                    pv0 = pv1;
                    pv1 = a;
                }
                int yStart = (int) Math.ceil(pv0.getValue(1) - 0.5);
                int yEnd = (int) Math.ceil(pv2.getValue(1) - 0.5);
                DrawTriangleTop(pv0, pv1, pv2, yStart, yEnd);
            } else if (pv1.getY() == pv2.getY()) {
                //sorting vertices
                if (pv2.getX() < pv1.getX()) {
                    Vector a = pv1;
                    pv1 = pv2;
                    pv2 = a;
                }
                int yStart = (int) pv0.getValue(1);
                int yEnd = (int) pv1.getValue(1);
                DrawTriangleBottom(pv0, pv1, pv2, yStart, yEnd);
            } else { // general triangle
                //find splitting vertex
                final float alphaSplit = (float) ((pv1.getY() - pv0.getY()) / (pv2.getY() - pv0.getY()));
                final Vector vi = pv0.addVector((pv2.subtract(pv0)).multiplydouble(alphaSplit));

                if (pv1.getX() < vi.getX()) { // major right


                    int yStart1 = (int) Math.ceil(pv1.getValue(1) - 0.5);
                    int yEnd1 = (int) Math.ceil(pv2.getValue(1) - 0.5);

                    DrawTriangleBottom(pv0, pv1, vi, yStart1, yEnd1);


                    int yStart = (int) pv0.getValue(1);
                    int yEnd = (int) pv1.getValue(1);

                    DrawTriangleTop(pv1, vi, pv2, yStart, yEnd); // top flat
                } else { // major left


                    int yStart = (int) pv0.getValue(1);
                    int yEnd = (int) vi.getValue(1);

                    DrawTriangleBottom(pv0, vi, pv1, yStart, yEnd);

                    int yStart1 = (int) Math.ceil(vi.getValue(1) - 0.5);
                    int yEnd1 = (int) Math.ceil(pv2.getValue(1) - 0.5); // top flat


                    DrawTriangleTop(vi, pv1, pv2, yStart1, yEnd1);
                }
            }
        }
    }

    private void DrawTriangleBottom(Vector a, Vector b, Vector c, int yStart2, int yEnd2) {

        float m0 = (float) ((b.getValue(0) - a.getValue(0)) / (b.getValue(1) - a.getValue(1)));
        float m1 = (float) ((c.getValue(0) - a.getValue(0)) / (c.getValue(1) - a.getValue(1)));

        float xmin = (float) Math.min(a.getValue(0), Math.min(b.getValue(0), c.getValue(0)));
        float xmax = (float) Math.max(a.getValue(0), Math.max(b.getValue(0), c.getValue(0)));

        float curx1 = (float) (a.getValue(0));
        float curx2 = (float) (a.getValue(0));

        int yStart = (int) a.getValue(1);
        int yEnd = (int) b.getValue(1);

        for (int y = yStart; y <= yEnd; y++) {
            for (int x = (int) curx1; x < curx2; x++) {

                if (x < xmin) {
                    x = (int) xmin;
                }
                if (x < 0) {
                    x = 0;
                }
                if (xmax < x) {
                    break;
                }
                if (0 < x && x < WIDTH && 0 < y && y < HEIGHT) {

                    float z = getZ(a, b, c, curx1, curx2, x, yStart, yEnd, y, false, yStart2, yEnd2);
                    TrianglegetColor(x, y, z);
                }
            }
            curx1 += m0;
            curx2 += m1;
        }
    }

    private void DrawTriangleTop(Vector a, Vector b, Vector c, int yStart2, int yEnd2) { // a = ymax c = xmin

        float m0 = (float) ((c.getValue(0) - a.getValue(0)) / (c.getValue(1) - a.getValue(1)));
        float m1 = (float) ((c.getValue(0) - b.getValue(0)) / (c.getValue(1) - b.getValue(1)));

        int yStart = (int) Math.ceil(a.getValue(1) - 0.5);
        int yEnd = (int) Math.ceil(c.getValue(1) - 0.5);
        if (yStart > HEIGHT) {
            yStart = HEIGHT;
        }
        if (yEnd > HEIGHT) {
            yEnd = HEIGHT;
        }

        if (yEnd < 0) {
            yEnd = 0;
        }
        for (int y = yStart; y < yEnd; y++) {
            final float px0 = (float) ((m0 * (y + 0.5 - a.getValue(1))) + a.getValue(0));
            final float px1 = (float) ((m1 * (y + 0.5 - b.getValue(1))) + b.getValue(0));


            int xStart = (int) (Math.ceil(px0 - 0.5));
            int xEnd = (int) (Math.ceil(px1 - 0.5));
            if (xStart > WIDTH) {
                xStart = WIDTH;
            }
            if (xEnd > WIDTH) {
                xEnd = WIDTH;
            }
            if (xStart < 0) {
                xStart = 0;
            }
            if (xEnd < 0) {
                xEnd = 0;
            }
            for (int x = xStart; x < xEnd; x++) {
                if (x < 0) {
                    x = 0;
                }
                if (WIDTH < x) {
                    break;
                }
                if (0 < x && x < WIDTH && 0 < y && y < HEIGHT) {

                    float z = getZ(a, b, c, xStart, xEnd, x, yStart, yEnd, y, true, yStart2, yEnd2);

                    TrianglegetColor(x, y, z);
                }
            }
        }
    }

    private float getZ(Vector p1, Vector p2, Vector p3, float xStart, float xEnd, float x, float yStart, float yEnd, float y, boolean TopBottom, int yStart2, int yEnd2) {

        float z1 = (float) (p1.getValue(2)); //xmin
        float z2 = (float) (p2.getValue(2)); //xmax
        float z3 = (float) (p3.getValue(2)); //ymax
        /*
        if (z1 < 0) {
            z1 = 0;
        }
        if (z2 < 0) {
            z2 = 0;
        }
        if (z3 < 0) {
            z3 = 0;
        }*/

        float xpercentage = (x - xStart) / (xEnd - xStart);

        float ypercentage;
        if (TopBottom) {
            ypercentage = ((y - yStart) + yEnd2 - yStart2) / (yEnd - yStart + yEnd2 - yStart2);
        } else {

            ypercentage = (y - yStart) / (yEnd - yStart + yEnd2 - yStart2);
        }

        float a1 = (1 - ypercentage);
        float b1 = (ypercentage) * (xpercentage);
        float c1 = (ypercentage) * (1 - xpercentage);

        return z1 * a1 + z2 * b1 + z3 * c1; // z
    }

    private void TrianglegetColor(int x, int y, float z) {

        if (z < pDepthBuffer[x][y]) {
            pDepthBuffer[x][y] = z;

            canvas.drawPoint(x, y, paint);
        }
    }
}
