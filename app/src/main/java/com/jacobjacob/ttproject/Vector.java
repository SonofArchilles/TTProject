package com.jacobjacob.ttproject;

import java.util.ArrayList;

import static com.jacobjacob.ttproject.Util.HEIGHTSCREEN;
import static com.jacobjacob.ttproject.Util.TILESIZE;
import static com.jacobjacob.ttproject.Util.WIDTHSCREEN;
import static com.jacobjacob.ttproject.Util.ZOOMFACTOR;
import static com.jacobjacob.ttproject.Util.camera;

public class Vector {
    private double x, y, z, w, u, v;

    public Vector() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 1;
    }

    public Vector(double x) {
        this.x = x;
        this.y = 0;
        this.z = 0;
        this.w = 1;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.w = 1;
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;
    }

    public Vector(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector(double x, double y, double z, double u, double v) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;
        this.u = u;
        this.v = v;
    }

    public void add(Vector b) {
        this.x += b.x;
        this.y += b.y;
        this.z += b.z;
    }

    public void subtractVector(Vector b) {
        this.x -= b.x;
        this.y -= b.y;
        this.z -= b.z;
    }

    public Vector subtract(Vector b) {
        return new Vector(this.x - b.x, this.y - b.y, this.z - b.z,this.w - b.w);
    }

    public Vector addVector(Vector b) {
        return new Vector(this.x + b.x, this.y + b.y, this.z + b.z);
    }


    public double skalarprodukt(Vector b) {//vectorlength /dot
        return Math.sqrt(this.x * b.x + this.y * b.y + this.z * b.z);
    }


    public Vector skalarmultiplikation(double d) {// d = distance/multiplicator

        return new Vector(d * this.x, d * this.y, d * this.z);
    }

    public Vector normalize() {
        double length = skalarprodukt(this);
        if (Math.abs(length) == 0) {
            return new Vector();
        } else {
            return new Vector(this.x / length, this.y / length, this.z / length);
        }
    }

    public Vector cross(Vector b) {
        return new Vector((this.y * b.z) - (this.z * b.y), (this.z * b.x) - (this.x * b.z), (this.x * b.y) - (this.y * b.x));
    }

    public double prod(Vector b) {
        return this.x * b.x + this.y * b.y + this.z * b.z;
    }


    public Vector multiply(Vector b) {
        return new Vector(this.x * b.x, this.y * b.y, this.z * b.z);
    }

    public Vector multiplydouble(double b) {
        return new Vector(this.x * b, this.y * b, this.z * b, this.w * b);
    }

    public double multiplygetdouble(double b) {
        return this.x * b + this.y * b + this.z * b;
    }

    public Vector reflectedVector(Vector direction, Vector position, Vector normal) {

        //Vector V = RayTracer.camera.getEye().subtract(position).normalize();

        Vector V = direction.normalize();
        double NV = Math.max(normal.prod(V), 0);//angle

        Vector refl = normal.skalarmultiplikation(NV * 2).subtract(V).normalize(); // reflected ray / V reflected on NV

        return refl;
    }

    public Vector getVectorPosition(Vector origin, Vector direction, double d) {
        return origin.addVector(direction.multiplydouble(d));
    }

    public double getAngle(Vector a, Vector b) {
        a = a.normalize();
        b = b.normalize();
        //return Math.toRadians(Math.acos(a.skalarprodukt(b)));
        return Math.toDegrees(Math.acos(a.skalarprodukt(b)));
    }

    public Vector getRotatedX(Vector vector, double angle) {
        double cosangle = Math.cos(angle);
        double sinangle = Math.sin(angle);

        return new Vector(vector.getX(), (vector.getY() * cosangle) - (vector.getZ() * sinangle), (vector.getY() * sinangle) + (vector.getZ() * cosangle));
    }

    public Vector getRotatedY(Vector vector, double angle) {
        double cosangle = Math.cos(angle);
        double sinangle = Math.sin(angle);

        return new Vector(((vector.getX() * cosangle) + (vector.getZ() * sinangle)), vector.getY(), (-vector.getX() * sinangle) + (vector.getZ() * cosangle));
    }

    public Vector getRotatedZ(Vector vector, double angle) {
        double cosangle = Math.cos(angle);
        double sinangle = Math.sin(angle);

        return new Vector((vector.getX() * cosangle) - (vector.getY() * sinangle), (vector.getX() * sinangle) + (vector.getY() * cosangle), vector.getZ());
    }

    public Vector RotateVectorSystem(Vector direction, Vector vecn1, Vector veca1, Vector vecn2, Vector veca2) {
        double length = direction.length();
        Vector rot = direction.normalize();

        Vector vectorn1 = vecn1.normalize();
        Vector vectora1 = veca1.normalize();
        Vector vectorn2 = vecn2.normalize();
        Vector vectora2 = veca2.normalize();
        Vector vectorb1 = (vecn1.cross(veca1)).normalize();
        Vector vectorb2 = (vecn2.cross(veca2)).normalize();


        double n1 = vectorn1.normalize().getX();
        double n2 = vectorn1.normalize().getY();
        double n3 = vectorn1.normalize().getZ();

        double a1 = vectora1.normalize().getX();
        double a2 = vectora1.normalize().getY();
        double a3 = vectora1.normalize().getZ();

        double b1 = vectorb1.normalize().getX();
        double b2 = vectorb1.normalize().getY();
        double b3 = vectorb1.normalize().getZ();

        double n11 = vectorn2.normalize().getX();
        double n21 = vectorn2.normalize().getY();
        double n31 = vectorn2.normalize().getZ();

        double a11 = vectora2.normalize().getX();
        double a21 = vectora2.normalize().getY();
        double a31 = vectora2.normalize().getZ();

        double b11 = vectorb2.normalize().getX();
        double b21 = vectorb2.normalize().getY();
        double b31 = vectorb2.normalize().getZ();

        double c1 = rot.normalize().getX();
        double c2 = rot.normalize().getY();
        double c3 = rot.normalize().getZ();

        double a3b1n2b2n1 = a3 * (b1 * n2 - b2 * n1);
        double a2b1n3b3n1 = a2 * (b1 * n3 - b3 * n1);
        double a1b2n3b3n2 = a1 * (b2 * n3 - b3 * n2);
        double divider = a1b2n3b3n2 - a2b1n3b3n1 + a3b1n2b2n1;
        double c1n2c2n1 = c1 * n2 - c2 * n1;
        double c1n3c3n1 = c1 * n3 - c3 * n1;
        double c2n3c3n2 = c2 * n3 - c3 * n2;

        double o = (a1 * (b2 * c3 - b3 * c2) - a2 * (b1 * c3 - b3 * c1) + a3 * (b1 * c2 - b2 * c1)) / (divider);
        double p = -((b1 * (c2n3c3n2) - b2 * (c1n3c3n1) + b3 * (c1n2c2n1)) / (divider));
        double q = (a1 * (c2n3c3n2) - a2 * (c1n3c3n1) + a3 * (c1n2c2n1)) / (divider);

        double xnew = n11 * o + a11 * p + b11 * q;
        double ynew = n21 * o + a21 * p + b21 * q;
        double znew = n31 * o + a31 * p + b31 * q;

        Vector translated = new Vector(xnew, ynew, znew);

        return translated.normalize().multiplydouble(length);
    }

    public Vector Matrix_MultiplyVector(Matrix m, Vector i) {
        Vector v = new Vector();
        v.setX((float) ((i.getX() * m.matrix[0][0]) + (i.getY() * m.matrix[1][0]) + (i.getZ() * m.matrix[2][0]) + (i.getW() * m.matrix[3][0])));
        v.setY((float) ((i.getX() * m.matrix[0][1]) + (i.getY() * m.matrix[1][1]) + (i.getZ() * m.matrix[2][1]) + (i.getW() * m.matrix[3][1])));
        v.setZ((float) ((i.getX() * m.matrix[0][2]) + (i.getY() * m.matrix[1][2]) + (i.getZ() * m.matrix[2][2]) + (i.getW() * m.matrix[3][2])));
        v.setW((float) ((i.getX() * m.matrix[0][3]) + (i.getY() * m.matrix[1][3]) + (i.getZ() * m.matrix[2][3]) + (i.getW() * m.matrix[3][3])));
        return v;
    }


    public Vector interpolate(final Vector src, final Vector dst, float alpha) {
        return (dst.subtract(src)).multiplydouble(alpha).addVector(src);
    }

    Vector Vector_IntersectPlane(Vector plane_p, Vector plane_n, Vector lineStart, Vector lineEnd) {
        plane_n = plane_n.normalize();
        float plane_d = (float) -plane_n.skalarprodukt(plane_p);
        float ad = (float) lineStart.skalarprodukt(plane_n);
        float bd = (float) lineEnd.skalarprodukt(plane_n);
        float t = (-plane_d - ad) / (bd - ad);
        Vector lineStartToEnd = lineEnd.subtract(lineStart);
        Vector lineToIntersect = lineStartToEnd.multiplydouble(t);
        return lineStart.addVector(lineToIntersect);
    }

    public float distVectorPlane(Vector p, Vector plane_n, Vector plane_p) {
        p = p.normalize();
        return (float) ((plane_n.x * p.x + plane_n.y * p.y + plane_n.z * p.z) - plane_n.skalarprodukt(plane_p));
    }

    public ArrayList<com.jacobjacob.ttproject.Old.Triangle> Triangle_ClipAgainstPlane(Vector plane_p, Vector plane_n, com.jacobjacob.ttproject.Old.Triangle in_tri) {
        com.jacobjacob.ttproject.Old.Triangle out_tri1, out_tri2;
        com.jacobjacob.ttproject.Old.Triangle output1;
        com.jacobjacob.ttproject.Old.Triangle output2;
        ArrayList<com.jacobjacob.ttproject.Old.Triangle> outputArray = new ArrayList<>();
        // Make sure plane normal is indeed normal
        plane_n = plane_n.normalize();


        // Create two temporary storage arrays to classify points either side of plane
        // If distance sign is positive, point lies on "inside" of plane
        Vector inside_points[] = new Vector[3];
        int nInsidePointCount = 0;
        Vector outside_points[] = new Vector[3];
        int nOutsidePointCount = 0;

        // Get signed distance of each point in triangle to plane
        float d0 = (float) (plane_n.x * in_tri.getVector(0).getValue(0) + plane_n.y * in_tri.getVector(0).getValue(1) + plane_n.z * in_tri.getVector(0).getValue(2) - plane_n.skalarprodukt(plane_p));
        float d1 = (float) (plane_n.x * in_tri.getVector(1).getValue(0) + plane_n.y * in_tri.getVector(1).getValue(1) + plane_n.z * in_tri.getVector(1).getValue(2) - plane_n.skalarprodukt(plane_p));
        float d2 = (float) (plane_n.x * in_tri.getVector(2).getValue(0) + plane_n.y * in_tri.getVector(2).getValue(1) + plane_n.z * in_tri.getVector(2).getValue(2) - plane_n.skalarprodukt(plane_p));

        if (d0 >= 0) {
            inside_points[nInsidePointCount++] = in_tri.getVector(0);
        } else {
            outside_points[nOutsidePointCount++] = in_tri.getVector(0);
        }
        if (d1 >= 0) {
            inside_points[nInsidePointCount++] = in_tri.getVector(1);
        } else {
            outside_points[nOutsidePointCount++] = in_tri.getVector(1);
        }
        if (d2 >= 0) {
            inside_points[nInsidePointCount++] = in_tri.getVector(2);
        } else {
            outside_points[nOutsidePointCount++] = in_tri.getVector(2);
        }

        // Now classify triangle points, and break the input triangle into
        // smaller output triangles if required. There are four possible
        // outcomes...

        if (nInsidePointCount == 0) {
            // All points lie on the outside of plane, so clip whole triangle
            // It ceases to exist

            return outputArray; // No returned triangles are valid
        }

        if (nInsidePointCount == 3) {
            // All points lie on the inside of plane, so do nothing
            // and allow the triangle to simply pass through
            out_tri1 = in_tri;
            outputArray.add(out_tri1);
            return outputArray; // Just the one returned original triangle is valid
        }

        if (nInsidePointCount == 1 && nOutsidePointCount == 2) {
            // Triangle should be clipped. As two points lie outside
            // the plane, the triangle simply becomes a smaller triangle

            // Copy appearance info to new triangle
            out_tri1 = in_tri;

            // The inside point is valid, so keep that...
            out_tri1.setVector(inside_points[0], 0);

            // but the two new points are at the locations where the
            // original sides of the triangle (lines) intersect with the plane

            out_tri1.setVector(Vector_IntersectPlane(plane_p, plane_n, inside_points[0], outside_points[0]), 1);
            out_tri1.setVector(Vector_IntersectPlane(plane_p, plane_n, inside_points[0], outside_points[1]), 2);

            outputArray.add(out_tri1);
            return outputArray; // Return the newly formed single triangle
        }

        if (nInsidePointCount == 2 && nOutsidePointCount == 1) {
            // Triangle should be clipped. As two points lie inside the plane,
            // the clipped triangle becomes a "quad". Fortunately, we can
            // represent a quad with two new triangles

            // Copy appearance info to new triangles
            out_tri1 = in_tri;

            out_tri2 = in_tri;

            // The first triangle consists of the two inside points and a new
            // point determined by the location where one side of the triangle
            // intersects with the plane

            out_tri1.setVector(inside_points[0], 0);
            out_tri1.setVector(inside_points[1], 1);
            out_tri1.setVector(Vector_IntersectPlane(plane_p, plane_n, inside_points[0], outside_points[0]), 2);
            // The second triangle is composed of one of he inside points, a
            // new point determined by the intersection of the other side of the
            // triangle and the plane, and the newly created point above

            out_tri2.setVector(inside_points[1], 0);
            out_tri2.setVector(out_tri1.getVector(2), 1);
            out_tri2.setVector(Vector_IntersectPlane(plane_p, plane_n, inside_points[1], outside_points[0]), 2);

            outputArray.add(out_tri1);
            outputArray.add(out_tri2);
            return outputArray; // Return two newly formed triangles which form a quad
        } else {
            return outputArray;
        }
    }



    public Vector getClippedCoordinates(Vector a) {

        int x = (int) getTileCoordinatesfromScreencoordinates((int)a.getValue(0),(int)a.getValue(1)).getValue(0);
        int y = (int) getTileCoordinatesfromScreencoordinates((int)a.getValue(0),(int)a.getValue(1)).getValue(1);

        float TILESIZEzoom = (float) Math.ceil(TILESIZE * (camera.getEye2D().getValue(2) / ZOOMFACTOR));
        Vector Screen = new Vector(x,y).multiplydouble(TILESIZEzoom);

        return Screen.getScreencoordinatesFromTileCoordinates(Screen);
    }



    public Vector getTileCoordinatesfromScreencoordinates(int x, int y) {

        x -= (int) (Math.ceil(TILESIZE * (camera.getEye2D().getValue(2) / ZOOMFACTOR)) / 2);
        y -= (int) (Math.ceil(TILESIZE * (camera.getEye2D().getValue(2) / ZOOMFACTOR)) / 2);

        float zoom = (float) camera.getEye2D().getValue(2) / ZOOMFACTOR;

        //x += zoom;
        y += zoom;

        int newx = (int) (Math.round((((x - (WIDTHSCREEN / 2)) / zoom) + camera.getEye2D().getValue(0)) / TILESIZE));
        int newy = (int) (Math.round((((y - (HEIGHTSCREEN / 2)) / zoom) + camera.getEye2D().getValue(1)) / TILESIZE));

        return new Vector(newx, newy);
    }


    public Vector getScreencoordinatesFromTileCoordinates(Vector Position) { // NOT RAW Position

        float zoom = (float) camera.getEye2D().getValue(2) / ZOOMFACTOR;

        Vector Positionnew = (Position.subtract(camera.getEye2D())).multiplydouble(zoom);

        int TileScreenx = (int) (Positionnew.getValue(0) + WIDTHSCREEN / 2);
        int TileScreeny = (int) (Positionnew.getValue(1) + HEIGHTSCREEN / 2);


        return new Vector(TileScreenx, TileScreeny);
    }

    public Vector getScreencoordinatesFromTileCoordinatesFake3d(Vector Position,float Depth) { // NOT RAW Position

        float DepthScale = 1;//(float)(camera.getEye2D().getValue(2)/TILESIZE);//0.5f;

        float zoom = (float) camera.getEye2D().getValue(2) / ZOOMFACTOR;
        /**/
        Vector Positionnew = (Position.subtract(camera.getEye2D())).multiplydouble(zoom);

        int TileScreenx = (int) (Positionnew.getValue(0) * DepthScale * Depth + WIDTHSCREEN / 2);
        int TileScreeny = (int) (Positionnew.getValue(1) * DepthScale * Depth + HEIGHTSCREEN / 2);
        /*/

        Vector Positionnew = (Position.subtract(camera.getEye2D())).multiplydouble(zoom);
        Vector Pos = Positionnew.normalize().multiplydouble(Depth);
        int TileScreenx = (int) (Pos.getValue(0) * DepthScale + Positionnew.getValue(0)+ WIDTHSCREEN / 2);
        int TileScreeny = (int) (Pos.getValue(1) * DepthScale + Positionnew.getValue(1)+ HEIGHTSCREEN / 2);
        /**/

        return new Vector(TileScreenx, TileScreeny);
    }

    public Vector negate() {
        return new Vector(-this.x, -this.y, -this.z);
    }

    public double length() {
        return this.skalarprodukt(this);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public double getW() {
        return this.w;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setW(float w) {
        this.w = w;
    }

    public double getValue(int a) {
        if (a == 0) {
            return this.x;
        } else if (a == 1) {
            return this.y;
        } else if (a == 2) {
            return this.z;
        } else if (a == 3){
            return this.w;
        }else {
            return this.x;
        }
    }
}
