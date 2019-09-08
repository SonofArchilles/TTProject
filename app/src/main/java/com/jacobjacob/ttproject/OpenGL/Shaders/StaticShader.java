package com.jacobjacob.ttproject.OpenGL.Shaders;

import android.graphics.Shader;

public class StaticShader extends ShaderProgram{

    private static final String VERTEX_FILE = "com.jacobjacob.ttproject/OpenGL/Shaders/vertextShader.txt";
    private static final String FRAGMENT_FILE = "com.jacobjacob.ttproject/OpenGL/Shaders/fragmentShader.txt";

    public StaticShader(){
        super(VERTEX_FILE,FRAGMENT_FILE);

    }
    @Override
    protected void bindAttributes(){
        super.bindAttribute(0,"position");
        //super.bindAttribute(1,"textureCoords");
    }

}
