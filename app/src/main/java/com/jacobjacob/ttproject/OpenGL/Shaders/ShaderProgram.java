package com.jacobjacob.ttproject.OpenGL.Shaders;

import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.microedition.khronos.opengles.GL11;

public abstract class ShaderProgram {

    private int programID;
    private int vertextShaderID;
    private int fragmentShaderID;

    public ShaderProgram(String vertextFile, String fragmentFile){

        vertextShaderID = loadShader(vertextFile,GLES20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile,GLES20.GL_FRAGMENT_SHADER);
        programID = GLES20.glCreateProgram();
        GLES20.glAttachShader(programID, vertextShaderID);
        GLES20.glAttachShader(programID, fragmentShaderID);
        GLES20.glLinkProgram(programID);
        GLES20.glValidateProgram(programID);
        bindAttributes();

    }

    public void start(){
        GLES20.glUseProgram(programID);
    }
    public void stop(){
        GLES20.glUseProgram(0);
    }

    public void CleanUp(){
        stop();
        GLES20.glDetachShader(programID,vertextShaderID);
        GLES20.glDetachShader(programID,fragmentShaderID);
        GLES20.glDeleteShader(vertextShaderID);
        GLES20.glDeleteShader(fragmentShaderID);
        GLES20.glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute,String variableName){
        GLES20.glBindAttribLocation(programID,attribute,variableName);
    }


    private static int loadShader(String file, int type){
        StringBuilder shaderSource = new StringBuilder();
        try {


            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }reader.close();
        }catch (Exception e){
            Log.d("Reader: ", e.getMessage());
            System.exit(-1);
        }

        int shaderID = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shaderID, String.valueOf(shaderSource));
        GLES20.glCompileShader(shaderID);

        if (GLES20.GL_COMPILE_STATUS == GL11.GL_FALSE){
            //System.out.println(GLES20.glGetShaderInfoLog((int)shaderID,(int)500));
            System.err.println("Could not Compile Shader");
            System.exit(-1);
        }

        return shaderID;
    }

}
