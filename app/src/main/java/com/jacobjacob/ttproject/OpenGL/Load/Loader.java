package com.jacobjacob.ttproject.OpenGL.Load;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.jacobjacob.ttproject.OpenGL.Shapes.RawModel;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public RawModel loadToVAO(float[] positions) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length / 3);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private int createVAO() {
        int vaoID = vaos.get(vaos.size() ) + 1;// = GLES30.glGenVertexArrays();
        GLES30.glBindVertexArray(vaoID);
        vaos.add(vaoID);
        return vaoID;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void cleanUP(){
        for (int vao:vaos){
            GLES30.glDeleteVertexArrays(vao,null);
        }
        for (int vbo:vbos){
            GLES30.glDeleteBuffers(vbo,null);
        }
    }

    private void storeDataInAttributeList(int attributeNumber, float[] data) {

        int[] dataint = new int[data.length];
        for (int i = 0; i < data.length; i++){
            dataint[i] =(int) data[i];
        }

        GLES30.glGenBuffers(GLES30.GL_ARRAY_BUFFER,dataint,0);

        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, buffer.array().length, buffer, GLES20.GL_STATIC_DRAW);
        GLES20.glVertexAttribPointer(attributeNumber, 3, GLES20.GL_FLOAT, false, 0, 0);

        int vaoID = vaos.get(vaos.size()) + 1;
        GLES20.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vaoID);
        vbos.add(vaoID);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void unbindVAO() {
        GLES30.glBindVertexArray(0);
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = null;// = //new FloatBuffer(0,0,data.length,data.length);//BufferUtils.create;
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}
