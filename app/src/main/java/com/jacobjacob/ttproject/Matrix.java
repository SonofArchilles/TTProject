package com.jacobjacob.ttproject;


import static com.jacobjacob.ttproject.Util.*;

public class Matrix {
    public float[][] matrix = new float[4][4];

    public Matrix ProjectionMatrix() { // ProjectionMatrix
        float fNear = 1f;
        float fFar = 10;
        float fAspectRatio;
        if (WIDTHSCREEN > HEIGHTSCREEN) {
            fAspectRatio = WIDTHSCREEN / HEIGHTSCREEN;
        } else {
            fAspectRatio = WIDTHSCREEN / HEIGHTSCREEN;
        }
        //fAspectRatio = 1;

        float fFovRad = (float) (1/Math.toRadians(8*FOV));///*(1 / (/**/2/*/4/**/ * Math.toRadians(FOV)));*/ //float)((1/Math.tan(Math.toRadians(FOV) * 0.5 )/**// (180 * Math.PI)/**/));/**float fFovRad = (float)((1/Math.tan(Math.toRadians(FOV) * 0.5 )/ (180 * Math.PI)));**/
        Matrix matrix = new Matrix();
        matrix.matrix[0][0] = fAspectRatio * fFovRad;
        matrix.matrix[1][1] = fFovRad;
        matrix.matrix[2][2] = fFar / (fFar - fNear);
        matrix.matrix[3][2] = (-fFar * fNear) / (fFar - fNear);
        matrix.matrix[2][3] = 1;
        matrix.matrix[3][3] = 0;
        return matrix;
    }

    public Matrix Matrix_MakeIdentity() {
        Matrix matrix = new Matrix();
        matrix.matrix[0][0] = 1;
        matrix.matrix[1][1] = 1;
        matrix.matrix[2][2] = 1;
        matrix.matrix[3][3] = 1;
        return matrix;
    }

    public Matrix Matrix_MakeRotationX(float fAngleRad) {
        Matrix matrix = new Matrix();
        matrix.matrix[0][0] = 1.0f;
        matrix.matrix[1][1] = (float) Math.cos(fAngleRad);
        matrix.matrix[1][2] = (float) Math.sin(fAngleRad);
        matrix.matrix[2][1] = (float) -Math.sin(fAngleRad);
        matrix.matrix[2][2] = (float) Math.cos(fAngleRad);
        matrix.matrix[3][3] = 1.0f;
        return matrix;
    }

    public Matrix Matrix_MakeRotationY(float fAngleRad) {
        Matrix matrix = new Matrix();
        matrix.matrix[0][0] = (float) Math.cos(fAngleRad);
        matrix.matrix[0][2] = (float) Math.sin(fAngleRad);
        matrix.matrix[2][0] = (float) -Math.sin(fAngleRad);
        matrix.matrix[1][1] = 1.0f;
        matrix.matrix[2][2] = (float) Math.cos(fAngleRad);
        matrix.matrix[3][3] = 1.0f;
        return matrix;
    }

    public Matrix Matrix_MakeRotationZ(float fAngleRad) {
        Matrix matrix = new Matrix();
        matrix.matrix[0][0] = (float) Math.cos(fAngleRad);
        matrix.matrix[0][1] = (float) Math.sin(fAngleRad);
        matrix.matrix[1][0] = (float) -Math.sin(fAngleRad);
        matrix.matrix[1][1] = (float) Math.cos(fAngleRad);
        matrix.matrix[2][2] = 1;
        matrix.matrix[3][3] = 1;
        return matrix;
    }

    public Matrix Matrix_MakeTranslation(float x, float y, float z) {
        Matrix matrix = new Matrix();
        matrix.matrix[0][0] = 1.0f;
        matrix.matrix[1][1] = 1.0f;
        matrix.matrix[2][2] = 1.0f;
        matrix.matrix[3][3] = 1.0f;
        matrix.matrix[3][0] = x;
        matrix.matrix[3][1] = y;
        matrix.matrix[3][2] = z;
        return matrix;
    }

    public Matrix Matrix_MultiplyMatrix(Matrix m1, Matrix m2) {
        Matrix matrix = new Matrix();
        for (int c = 0; c < 4; c++)
            for (int r = 0; r < 4; r++)
                matrix.matrix[r][c] = m1.matrix[r][0] * m2.matrix[0][c] + m1.matrix[r][1] * m2.matrix[1][c] + m1.matrix[r][2] * m2.matrix[2][c] + m1.matrix[r][3] * m2.matrix[3][c];
        return matrix;
    }


    public Matrix Matrix_PointAt(Vector pos, Vector target, Vector up) {


        Vector W = target;
        Vector U = up.cross(W).normalize(); // right
        Vector V = W.cross(U).normalize();
        float t = 1;
        float d = (float) (t / Math.tan(Math.PI / 4) / 2);
        Vector W_d_negated = W.skalarmultiplikation(d * -1);

        Vector newRight = U.negate();
        // Construct Dimensioning and Translation Matrix
        Matrix matrix = new Matrix();
        matrix.matrix[0][0] = (float) newRight.getValue(0);
        matrix.matrix[0][1] = (float) newRight.getValue(1);
        matrix.matrix[0][2] = (float) newRight.getValue(2);
        matrix.matrix[0][3] = 0.0f;

        matrix.matrix[1][0] = (float) V.getValue(0);
        matrix.matrix[1][1] = (float) V.getValue(1);
        matrix.matrix[1][2] = (float) V.getValue(2);
        matrix.matrix[1][3] = 0.0f;

        matrix.matrix[2][0] = (float) W_d_negated.getValue(0);
        matrix.matrix[2][1] = (float) W_d_negated.getValue(1);
        matrix.matrix[2][2] = (float) W_d_negated.getValue(2);
        matrix.matrix[2][3] = 0.0f;

        matrix.matrix[3][0] = (float) pos.getValue(0);
        matrix.matrix[3][1] = (float) pos.getValue(1);
        matrix.matrix[3][2] = (float) pos.getValue(2);
        matrix.matrix[3][3] = 1.0f;
        return matrix;


    }

    public Matrix Matrix_QuickInverse(Matrix m) // Only for Rotation/Translation Matrices
    {
        Matrix matrix = new Matrix();
        matrix.matrix[0][0] = m.matrix[0][0];
        matrix.matrix[0][1] = m.matrix[1][0];
        matrix.matrix[0][2] = m.matrix[2][0];
        matrix.matrix[0][3] = 0.0f;
        matrix.matrix[1][0] = m.matrix[0][1];
        matrix.matrix[1][1] = m.matrix[1][1];
        matrix.matrix[1][2] = m.matrix[2][1];
        matrix.matrix[1][3] = 0.0f;
        matrix.matrix[2][0] = m.matrix[0][2];
        matrix.matrix[2][1] = m.matrix[1][2];
        matrix.matrix[2][2] = m.matrix[2][2];
        matrix.matrix[2][3] = 0.0f;
        matrix.matrix[3][0] = -(m.matrix[3][0] * matrix.matrix[0][0] + m.matrix[3][1] * matrix.matrix[1][0] + m.matrix[3][2] * matrix.matrix[2][0]);
        matrix.matrix[3][1] = -(m.matrix[3][0] * matrix.matrix[0][1] + m.matrix[3][1] * matrix.matrix[1][1] + m.matrix[3][2] * matrix.matrix[2][1]);
        matrix.matrix[3][2] = -(m.matrix[3][0] * matrix.matrix[0][2] + m.matrix[3][1] * matrix.matrix[1][2] + m.matrix[3][2] * matrix.matrix[2][2]);
        matrix.matrix[3][3] = 1.0f;

        return matrix;
    }
}
