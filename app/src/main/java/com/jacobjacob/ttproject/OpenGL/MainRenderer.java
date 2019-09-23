package com.jacobjacob.ttproject.OpenGL;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jacobjacob.ttproject.Light.PointLight;
import com.jacobjacob.ttproject.Vector;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.jacobjacob.ttproject.Util.*;


public class MainRenderer extends AppCompatActivity implements GLSurfaceView.Renderer {
    Context context;


    public MainRenderer(Context context) {
        this.context = context;
    }

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private float[] mModelMatrix = new float[16];

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mViewMatrix = new float[16];

    /**
     * Store the projection matrix. This is used to project the scene onto a 2D viewport.
     */
    private float[] mProjectionMatrix = new float[16];

    /**
     * Allocate storage for the final combined matrix. This will be passed into the shader program.
     */
    private float[] mMVPMatrix = new float[16];

    /**
     * Store our model data in a float buffer.
     */

    /**
     * This will be used to pass in the transformation matrix.
     */
    private int mMVPMatrixHandle;

    /**
     * This will be used to pass in model position information.
     */
    private int mPositionHandle;

    /**
     * This will be used to pass in model color information.
     */
    private int mColorHandle;

    /**
     * How many bytes per float.
     */
    private final int mBytesPerFloat = 4;

    /**
     * How many elements per vertex.
     */
    private final int mStrideBytes = 7 * mBytesPerFloat; // xyzrgba

    /**
     * Offset of the position data.
     */
    private final int mPositionOffset = 0;

    /**
     * Size of the position data in elements.
     */
    private final int mPositionDataSize = 3;

    /**
     * Offset of the color data.
     */
    private final int mColorOffset = 3;

    /**
     * Size of the color data in elements.
     */
    private final int mColorDataSize = 4;

    final String vertexShader = "uniform mat4 u_MVPMatrix;      \n"        // A constant representing the combined model/view/projection matrix.

            + "attribute vec4 a_Position;     \n"                          // Per-vertex position information we will pass in.
            + "attribute vec4 a_Color;        \n"                          // Per-vertex color information we will pass in.

            + "varying vec4 v_Color;          \n"                          // This will be passed into the fragment shader.

            + "void main()                    \n"                          // The entry point for our vertex shader.
            + "{                              \n" + "   v_Color = a_Color;          \n"        // Pass the color through to the fragment shader.
            // It will be interpolated across the triangle.
            + "   gl_Position = u_MVPMatrix   \n"                          // gl_Position is a special variable used to store the final position.
            + "               * a_Position;   \n"                          // Multiply the vertex by the matrix to get the final point in
            + "}                              \n";                         // normalized screen coordinates.

    final String fragmentShader = "precision mediump float;       \n"      // Set the default precision to medium. We don't need as high of a
            // precision in the fragment shader.
            + "varying vec4 v_Color;          \n"                          // This is the color from the vertex shader interpolated across the
            // triangle per fragment.
            + "void main()                    \n"                          // The entry point for our fragment shader.
            + "{                              \n"
            + "   gl_FragColor = v_Color;     \n"        // Pass the color directly through the pipeline.
            + "}                              \n";



    final String vertex_shader_texture =
            "uniform mat4 u_MVPMatrix;"

            + "uniform mat4 u_MVMatrix;" + "\n"

            + "attribute vec4 a_Position;" + "\n"

            + "varying vec3 v_Position;" + "\n"

            + "attribute vec2 a_TexCoordinate;" + "\n"
            + "varying vec2 v_TexCoordinate;" + "\n"

            + "void main()\n" + "{\n"

            + "v_Position = vec3(u_MVMatrix * a_Position);\n"

            + "v_TexCoordinate = a_TexCoordinate;\n"

            + "gl_Position = u_MVPMatrix * a_Position;\n"

            + "}";

    final String fragment_shader_texture =
            "precision mediump float;" +
              "uniform sampler2D u_Texture;\n"    // The input texture

            + "varying vec2 v_TexCoordinate;\n"  // interpolated Texture coordinate same for texture and normaltexture
            + "void main()" + "{\n"
            + "gl_FragColor = texture2D(u_Texture, v_TexCoordinate);\n" // Multiply the color by the diffuse illumination level and texture value to get final output color.\n"
            + "}";

    /**/
        final String per_pixel_vertex_shader =
                "uniform mat4 u_MVPMatrix;"// A constant representing the combined model/view/projection matrix.
                + "uniform mat4 u_MVMatrix;" + "\n"// A constant representing the combined model/view matrix.
                + "attribute vec4 a_Position;" + "\n"// Per-vertex position information we will pass in.
                + "attribute vec2 a_TexCoordinate;" + "\n" // Per-vertex texture coordinate information we will pass in.

                + "varying vec3 v_Position;" + "\n"// This will be passed into the fragment shader.


                + "attribute vec3 a_Light;" + "\n"         // Vector from the Light to the Tile

                + "varying vec3 v_Light;" + "\n"         // Vector from the Light to the Tile


                //+ "varying vec3 v_Normal;" + "\n"// This will be passed into the fragment shader.
                + "varying vec2 v_TexCoordinate;" + "\n"   // This will be passed into the fragment shader.
                // The entry point for our vertex shader.

                + "void main()\n" + "{\n"
                // Transform the vertex into eye space.
                + "v_Position = vec3(u_MVMatrix * a_Position);\n"
                // Pass through the texture coordinate.
                + "v_TexCoordinate = a_TexCoordinate;\n"

                + "v_Light = a_Light;"

                // Transform the normal's orientation into eye space.
                //+ "    v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));\n"
                // gl_Position is a special variable used to store the final position.
                // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
                + "gl_Position = u_MVPMatrix * a_Position;\n" + "}";
        /**/
    /*/
    final String per_pixel_vertex_shader = "uniform mat4 u_MVPMatrix;" + "uniform mat4 u_MVMatrix;" + "\n"

            + "attribute vec4 a_Position;" + "\n" + "varying vec3 v_Position;" + "\n"

            + "attribute vec2 a_TexCoordinate;" + "\n" + "varying vec2 v_TexCoordinate;" + "\n"

            //+ "attribute vec3 a_Light;" + "\n"
            //+ "varying vec3 v_Light;" + "\n"


            + "void main()\n" + "{\n"

            + "v_Position = vec3(u_MVMatrix * a_Position);\n"

            //+ "v_Light = a_Light;\n"

            + "v_TexCoordinate = a_TexCoordinate;\n"

            + "gl_Position = u_MVPMatrix * a_Position;\n"

            + "}";/**/
/*/
    final String per_pixel_vertex_shader =
            "uniform mat4 u_MVPMatrix;"

            + "uniform mat4 u_MVMatrix;"

            + "attribute vec4 a_Position;"
            + "varying vec3 v_Position;"

            + "attribute vec2 a_TexCoordinate;"
            + "varying vec2 v_TexCoordinate;"


            + "void main(){"

            + "v_Position = vec3(u_MVMatrix * a_Position);"

            + "v_TexCoordinate = a_TexCoordinate;"

            + "gl_Position = u_MVPMatrix * a_Position;"

            + "}";


    /**/

/*/
    final String per_pixel_fragment_shader =
            "precision mediump float;" +
            // Set the default precision to medium. We don't need as high of a
            // precision in the fragment shader.
            "uniform vec3 u_LightPos;\n"          // The position of the light in eye space.
            + "uniform sampler2D u_Texture;\n"    // The input texture
            + "varying vec3 v_Position;\n"        // Interpolated position for this fragment.
            + "varying vec4 v_Color;\n"           // This is the color from the vertex shader interpolated across the triangle per fragment.
            + "varying vec3 v_Normal;\n"
                                                  // Interpolated normal for this fragment.
            + "varying vec2 v_TexCoordinate;\n"
                                                  // Interpolated texture coordinate per fragment.
                                                  // The entry point for our fragment shader.
            + "void main()" + "{\n"
                                                  // Will be used for attenuation.
            + "float distance = length(u_LightPos - v_Position);\n"
            // Get a lighting direction vector from the light to the vertex.
            + "vec3 lightVector = normalize(u_LightPos - v_Position);\n"
            // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
            // pointing in the same direction then it will get max illumination.
            + "float diffuse = max(dot(v_Normal, lightVector), 0.0);\n"
          // Add attenuation.
            + "diffuse = diffuse * (1.0 / (1.0 + (0.10 * distance)));\n"
          // Add ambient lighting
            + "diffuse = diffuse + 0.3;  \n"
            // Multiply the color by the diffuse illumination level and texture value to get final output color.
            + "gl_FragColor = (texture2D(u_Texture, v_TexCoordinate));\n"
            + "}";
/**/

/*/
    final String per_pixel_fragment_shader =
            "precision mediump float;"
                    + "uniform sampler2D u_Texture;\n"    // The input texture
                    + "uniform sampler2D n_Texture;\n"
                    + "varying vec3 v_Light;" + "\n"         // Vector from the Light to the Tile
                    + "varying vec2 v_TexCoordinate;\n"  // interpolated Texture coordinate same for texture and normaltexture
                    + "float brightness;" + "\n"

                    + "void main()" + "{\n"
                    //+ "vec3 vec_normalTex = normalize(vec3(texture2D(n_Texture, v_TexCoordinate))-0.5);" +"\n"
                    + "vec3 vec_normalTex = (vec3(texture2D(n_Texture, v_TexCoordinate))-0.5);" +"\n"
                    //+ "vec4 vec_normalTex = (texture2D(n_Texture, v_TexCoordinate)-0.5)*2;" +"\n"
                    //+ "vec3 vec_normal = normalize(texture2D(n_Texture, v_TexCoordinate)-0.5);" + "\n" // normalized normal of the pixel / fragment

                   // + "float brightness = 0.5;" + "\n"
                    + "float dist = (length(v_Light.xyz));" + "\n"
                    //+ "brightness = dot(normalize(v_Light),vec_normalTex);" + "\n"
                    + "brightness = dot(v_Light,vec_normalTex);" + "\n"

                    + "brightness = ((0.8 * brightness * (1.0 / dist)) + 0.2);" + "\n"
                    //+ "gl_FragColor = (texture2D(u_Texture, v_TexCoordinate)) * brightness;\n"

                    + "gl_FragColor =  texture2D(u_Texture, v_TexCoordinate) * brightness;" + "\n"
                    //+ "gl_FragColor =  (texture2D(u_Texture, v_TexCoordinate)  +texture2D(n_Texture, v_TexCoordinate))*0.5;" + "\n"
                    + "}";
/**/

    /*/
    final String per_pixel_fragment_shader =
            "precision mediump float;"
                    + "uniform sampler2D u_Texture;\n"    // The input texture
                    + "uniform sampler2D n_Texture;\n"
                    + "varying vec3 v_Light;" + "\n"         // Vector from the Light to the Tile
                    + "varying vec2 v_TexCoordinate;\n"  // interpolated Texture coordinate same for texture and normaltexture
                    + "float brightness;" + "\n"

                    + "void main()" + "{\n"
                //    + "vec3 vec_normalTex = vec3(texture2D(n_Texture, v_TexCoordinate))-0.5;" +"\n"

                //    + "float dist = (length(v_Light.xyz));" + "\n"
                //    + "brightness = dot(v_Light,vec_normalTex);" + "\n"

                //    + "brightness = ((1.0 * brightness * (1.0 / dist)) + 0.0);" + "\n"

                //    + "gl_FragColor =  texture2D(u_Texture, v_TexCoordinate) * brightness;" + "\n"
                    + "vec4 color_vec = texture2D(u_Texture, v_TexCoordinate);"


                    //    + "vec3 vec_normalTex = vec3(texture2D(n_Texture, v_TexCoordinate))-0.5;" +"\n"

                    //    + "float dist = (length(v_Light.xyz));" + "\n"
                    //    + "brightness = dot(v_Light,vec_normalTex);" + "\n"

                    //    + "brightness = ((1.0 * brightness * (1.0 / dist)) + 0.0);" + "\n"

                    //    //+ "gl_FragColor =  texture2D(u_Texture, v_TexCoordinate) * brightness;" + "\n"
                    //+ "color_vec = brightness * color_vec; " + "\n"
                    + "gl_FragColor =  color_vec;" + "\n"
                    + "}";
    /**/


    /*/
    final String per_pixel_fragment_shader =
            "precision mediump float;"
                    + "uniform sampler2D u_Texture;\n"    // The input texture
                    + "uniform sampler2D n_Texture;\n"

                    + "uniform vec3 v_Light;" + "\n"         // Vector from the Light to the Tile

                    + "varying vec2 v_TexCoordinate;\n"  // interpolated Texture coordinate same for texture and normaltexture

                    + "float brightness;" + "\n"
                    + "vec4 color_vec;"
                    + "vec3 vec_normalTex;"
                    + "float dist;"

                    + "void main()" + "{\n"
                    //    + "vec3 vec_normalTex = vec3(texture2D(n_Texture, v_TexCoordinate))-0.5;" +"\n"

                    //    + "float dist = (length(v_Light.xyz));" + "\n"
                    //    + "brightness = dot(v_Light,vec_normalTex);" + "\n"

                    //    + "brightness = ((1.0 * brightness * (1.0 / dist)) + 0.0);" + "\n"

                    //    + "gl_FragColor =  texture2D(u_Texture, v_TexCoordinate) * brightness;" + "\n"
                    + "color_vec = texture2D(u_Texture, v_TexCoordinate);"


                    + "vec_normalTex = vec3(texture2D(n_Texture, v_TexCoordinate))-0.5;" +"\n"

                        //+ "vec3 Light = vec3(v_Light);"//vec3(0.5,0.5,1.0);"

                   // + "vec3 Light = vec3(1,0.0,0.0);"
                        + "dist = (length(v_Light.xyz));" + "\n"
                    + "brightness = dot(v_Light,vec_normalTex);" + "\n"


                    + "brightness = ((0.8 * brightness * (0.5 / dist)) + 0.2);" + "\n" //

                   //     //+ "gl_FragColor =  texture2D(u_Texture, v_TexCoordinate) * brightness;" + "\n"
                    + "color_vec = brightness * color_vec; " + "\n"
                    + "gl_FragColor =  color_vec;" + "\n"
                    + "}";
    /**/

    /*/
    final String per_pixel_fragment_shader =
            "precision mediump float;"
                    + "uniform sampler2D u_Texture;\n"    // The input texture
                    + "uniform sampler2D n_Texture;\n"

                    + "uniform vec3 v_Light;" + "\n"         // Vector from the Light to the Tile

                    + "varying vec2 v_TexCoordinate;\n"  // interpolated Texture coordinate same for texture and normaltexture

                    + "float brightness;" + "\n"
                    + "vec4 color_vec;"
                    + "vec3 vec_normalTex;"
                    + "float dist;"



                    + "void main()" + "{\n"
                   // + "color_vec = texture2D(u_Texture, v_TexCoordinate);"


                   // + "vec_normalTex = vec3(texture2D(n_Texture, v_TexCoordinate))-0.5;" +"\n"
                   // + "dist = (length(v_Light.xyz));" + "\n"
                   // + "brightness = dot(v_Light,vec_normalTex);" + "\n"


                   // + "brightness = ((0.8 * brightness * (0.5 / dist)) + 0.2);" + "\n"
                   // + "color_vec = brightness * color_vec; " + "\n"
                   // + "gl_FragColor =  color_vec;" + "\n"

                        // Will be used for attenuation.\n"+

                    +"float distance = length(v_Light);\n"// Get a lighting direction vector from the light to the vertex.\n"
                    +"vec3 lightVector = normalize(v_Light);\n"

                    + "vec_normalTex = vec3(texture2D(n_Texture, v_TexCoordinate))-0.5;" +"\n"

                    +"float diffuse = max(dot(vec_normalTex, lightVector), 0.0);\n" // 0 to 1

                    +"diffuse = (diffuse * (1.0 / (1.0 + (0.05 * distance))) + 0.2);\n" // + 0.5 * (1.0 / (1.0 + (0.005 * distance)))

                    + "vec4 colorvec = (diffuse * texture2D(u_Texture, v_TexCoordinate));" //diffuse*

                    +"gl_FragColor = colorvec;" // Multiply the color by the diffuse illumination level and texture value to get final output color.\n"
                    + "}";
    /**/
    /*/ // normal shading, working pretty well:
    final String per_pixel_fragment_shader = "precision mediump float;" + "uniform sampler2D u_Texture;\n"    // The input texture
            + "uniform sampler2D n_Texture;\n"

            + "uniform vec3 v_Light;" + "\n"         // Vector from the Light to the Tile

            //+"varying vec3 v_Position;"
            + "uniform vec3 v_Tile;"

            + "varying vec2 v_TexCoordinate;\n"  // interpolated Texture coordinate same for texture and normaltexture

            + "float brightness;" + "\n"
            + "vec4 color_vec;"
            + "vec3 vec_normalTex;"
            + "float dist;"


            + "void main()" + "{\n"

            + "float distance = length(v_Light - v_Tile - vec3(v_TexCoordinate,0));\n"// Get a lighting direction vector from the light to the vertex.\n"

            + "float distancepixel = length(vec2(v_Light - v_Tile - vec3(v_TexCoordinate,0)));\n"// Get a lighting direction vector from the light to the vertex.\n"

            + "vec3 lightVector = normalize(v_Light - v_Tile - vec3(v_TexCoordinate,0));\n"

            + "vec_normalTex = vec3(texture2D(n_Texture, v_TexCoordinate))-0.5;" + "\n"

            + "float diffuse = max(dot(vec_normalTex, lightVector), 0.0);\n" // 0 to 1

            + "diffuse = (diffuse * (1.0 / (1.0 + (0.05 * distance))) + 0.2);\n" // + 0.5 * (1.0 / (1.0 + (0.005 * distance)))

            + "vec4 colorvec = (diffuse * texture2D(u_Texture, v_TexCoordinate));" //diffuse*
            //+ " if(distancepixel < 0.1){"
            + "colorvec = colorvec+colorvec * (1.0/(1.0+10.0*distancepixel));"//vec4(1.0,1.0,1.0,1.0);"
            //+"}"
            + "gl_FragColor = colorvec;" // Multiply the color by the diffuse illumination level and texture value to get final output color.\n"
            + "}";
    /**/

    /**/ //Toonshading :
    final String per_pixel_fragment_shader =
            "precision mediump float;" + "uniform sampler2D u_Texture;\n"    // The input texture
            + "uniform sampler2D n_Texture;\n"

            + "uniform vec3 v_Light;" + "\n"         // Vector from the Light to the Tile

            + "uniform vec4 v_Light_Color;" + "\n"         // Vector from the Light to the Tile

            //+"varying vec3 v_Position;"
            + "uniform vec3 v_Tile;"

            + "varying vec2 v_TexCoordinate;\n"  // interpolated Texture coordinate same for texture and normaltexture

            + "float brightness;" + "\n"

            + "vec4 color_vec;"

            + "vec3 vec_normalTex;"

            + "float dist;"

            + "const float levels = -7.0;"


            + "void main(){"

            + "vec4 colorvec = (texture2D(u_Texture, v_TexCoordinate));"


            + "vec3 Light_Tile = v_Light - v_Tile - vec3(v_TexCoordinate,0);"


            + "float distance = length(Light_Tile);"// Get a lighting direction vector from the light to the vertex.\n"

            + "float distancepixel = length(vec2(Light_Tile));"// Get a lighting direction vector from the light to the vertex.\n"

            + "vec3 lightVector = normalize(Light_Tile);"



            + "vec_normalTex = vec3(texture2D(n_Texture, v_TexCoordinate))-0.5;"

            + "float diffuseold = max(dot(vec_normalTex, lightVector), 0.0);" // 0 to 1

            + "float diffuse = (diffuseold * (1.0 / (1.0 + (0.05 * distance))) + 0.2);" // + 0.5 * (1.0 / (1.0 + (0.005 * distance)))

            + "colorvec = diffuse * colorvec;" //diffuse*
            //+ " if(distancepixel < 0.1){"
            + "float diff2 = (1.0/(1.0+10.0*distancepixel));"

            + "colorvec = colorvec + colorvec * diff2 * diffuseold;"//vec4(1.0,1.0,1.0,1.0);"
            //+"}"
            + "colorvec = colorvec + v_Light_Color * diff2 + v_Light_Color *diffuseold * (1.0/(1.0+distance));"

            + "if(levels > 0.0){" +
                    "colorvec = floor(colorvec * levels)/levels;"
            + "}" +


            "gl_FragColor = colorvec;" // Multiply the color by the diffuse illumination level and texture value to get final output color.\n"

            + "}";
    /**/


    /**multiple Lights test 2: */
    /*/


    final String per_pixel_vertex_shader =
            "uniform mat4 u_MVPMatrix;"

                    + "uniform mat4 u_MVMatrix;"

                    + "attribute vec4 a_Position;"
                    + "varying vec3 v_Position;"

                    + "attribute vec2 a_TexCoordinate;"
                    + "varying vec2 v_TexCoordinate;"


                    //+ "uniform vec1 n_Lights;"
                    //+ "uniform vec3 v_Light[n_Lights.x];"        // Vector from the Light to the Tile
                    //+ "uniform vec4 v_Light_color[n_Lights.x];"




                    + "void main(){"

                    + "v_Position = vec3(u_MVMatrix * a_Position);"

                    + "v_TexCoordinate = a_TexCoordinate;"

                    + "gl_Position = u_MVPMatrix * a_Position;"

                    + "}";

    final String per_pixel_fragment_shader =
            "precision mediump float;" + "uniform sampler2D u_Texture;\n"    // The input texture
                    + "uniform sampler2D n_Texture;\n"

                    + "uniform vec1 n_Lights;"

                    + "uniform vec3 v_Light"//[n_Lights.x];"        // Vector from the Light to the Tile

                    + "uniform vec4 v_Light_color"//[n_Lights.x];"

                    + "uniform vec3 v_Tile;"

                    + "varying vec2 v_TexCoordinate;"  // interpolated Texture coordinate same for texture and normaltexture

                    + "float brightness;"

                    + "vec4 color_vec;"

                    + "vec3 vec_normalTex;"

                    + "float dist;"

                    + "const float levels = -7.0;"


                    + "void main()" + "{"



                    + "float distance = length(v_Light - v_Tile - vec3(v_TexCoordinate,0));"// Get a lighting direction vector from the light to the vertex.\n"

                    + "float distancepixel = length(vec2(v_Light - v_Tile - vec3(v_TexCoordinate,0)));"// Get a lighting direction vector from the light to the vertex.\n"

                    + "vec3 lightVector = normalize(v_Light - v_Tile - vec3(v_TexCoordinate,0));"

                    + "vec_normalTex = vec3(texture2D(n_Texture, v_TexCoordinate))-0.5;"

                    + "float diffuse = max(dot(vec_normalTex, lightVector), 0.0);" // 0 to 1

                    + "diffuse = (diffuse * (1.0 / (1.0 + (0.05 * distance))) + 0.2);" // + 0.5 * (1.0 / (1.0 + (0.005 * distance)))

                    + "vec4 colorvec = (diffuse * texture2D(u_Texture, v_TexCoordinate));" //diffuse*

                    + "float diff2 = (1.0/(1.0+10.0*distancepixel));"

                    + "colorvec = colorvec+colorvec * diff2;"

                    + "colorvec = colorvec + vec4(1,1,0,1) * diff2;" + "if(levels > 0.0){"
                    + "colorvec = floor(colorvec * levels)/levels;"
                    + "}" +
                    "gl_FragColor = colorvec;"
                    + "}";
    /**/


    /**multiple Lights: */
/*/
    final String per_pixel_fragment_shader =
            "precision mediump float;"

                    + "uniform sampler2D u_Texture;\n"    // The input texture

                    + "uniform sampler2D n_Texture;\n"

                    + "uniform vec3 v_Light;"         // Vec3 Position Light

                    + "uniform vec4 v_Light_color;"   // Vec4 Color of the Light

                    + "uniform vec3 v_Tile;"

                    + "varying vec2 v_TexCoordinate;\n"  // interpolated Texture coordinate same for texture and normaltexture

                    + "float brightness;" + "\n"

                    + "vec4 color_vec;"

                    + "vec3 vec_normalTex;"

                    + "float dist;"

                    + "const float levels = -7.0;"


                    + "void main() {\n"


                    + "colorvec = texture2D(u_Texture, v_TexCoordinate);" // Color of the Texture without changes

                    + "vec_normalTex = vec3(texture2D(n_Texture, v_TexCoordinate))-0.5;"

                    + "vec3 Tex_Coord = vec3(v_TexCoordinate,0);"





                    //+" for(int i = 0; i < v_Light.length();i++){" //v_Light.length()/3.0

                    +    "vec3 Light_vec = vec3(1.0f);"//vec3(v_Light[i*3.0],v_Light[i*3.0 + 1.0], v_Light[i*3.0 + 2.0]);"

                    +    "vec4 Light_color = vec4(1.0);"//vec4(v_Light_color[i * 4.0],v_Light_color[i * 4.0 + 1.0],v_Light_color[i * 4.0 + 2.0],v_Light_color[i * 4.0 + 3.0])"




                    +    "vec3 Light_Tile = Light_vec - v_Tile - Tex_Coord;"


                    +    "float distance = length(Light_Tile);"// Get a lighting direction vector from the light to the vertex.\n"

                    +    "float distancepixel = length(vec2(Light_Tile));"// Get a lighting direction vector from the light to the vertex.\n"

                    +    "vec3 lightVector = normalize(Light_Tile);"



                    +    "float diffuse = max(dot(vec_normalTex, lightVector), 0.0);" // 0 to 1

                    +    "diffuse = (diffuse * (1.0 / (1.0 + (0.05 * distance))) + 0.2);" // + 0.5 * (1.0 / (1.0 + (0.005 * distance)))

                    +    "colorvec = (diffuse * colorvec);"

                    +    "float diff2 = (1.0/(1.0+10.0*distancepixel));"

                    +    "colorvec = colorvec+colorvec * diff2;"
                            //+"}"
                    +    "colorvec = colorvec + vec4(1,1,0,1) * diff2;" +

                    "if(levels > 0.0){"
                    + "colorvec = floor(colorvec * levels)/levels;"
                    + "}" +

                    //"}"+


                    "gl_FragColor = colorvec;\n" // Multiply the color by the diffuse illumination level and texture value to get final output color.\n"
                    + "}";
/**/


    int Program[] = new int[2];

    PointLight Lightsource = new PointLight(new Vector(), Color.rgb(255, 255, 200));

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // Set the background clear color to gray.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);


        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);


        Program[1] = createProgram(vertexShader, fragmentShader);


        Program[0] = createProgram(per_pixel_vertex_shader, per_pixel_fragment_shader);
        GLES20.glUseProgram(Program[1]);

        //TILETEXTURE.updateTextures();        // working
        //TILETEXTURE.updateTexturesNormals(); // working
        for (int i = 0; i < MATERIALSTOTEXTURE; i++) {
            TILETEXTURE.UpdateMaterialTexture(i);
            TILETEXTURE.UpdateMaterialNormalTexture(i);
        }

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        LIGHTS.add(new PointLight(new Vector(10,10).multiplydouble(TILESIZE),Color.argb(255,255,255,1)));
        LIGHTS.add(new PointLight(new Vector(10,-10).multiplydouble(TILESIZE),Color.argb(255,255,0,1)));
        LIGHTS.add(new PointLight(new Vector(-10,-10).multiplydouble(TILESIZE),Color.argb(0,255,255,1)));
        LIGHTS.add(new PointLight(new Vector(-10,10).multiplydouble(TILESIZE),Color.argb(255,0,255,1)));
    }


    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        float ratio = 1f;//(float) width / height;
        float left = -ratio;
        float right = ratio;
        float bottom = -1.0f;
        float top = 1.0f;
        float near = 0.9999f;
        float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }


    private void setRGBA(int Colorint) {
        r = (float) (Color.red(Colorint)) / 255.0f;
        g = (float) (Color.green(Colorint)) / 255.0f;
        b = (float) (Color.blue(Colorint)) / 255.0f;
        a = (float) (Color.alpha(Colorint)) / 255.0f;
    }

    private float r, g, b, a;

    @Override
    public void onDrawFrame(GL10 glUnused) {

        UIUPDATING = true;

        /*/
        TestLoops();
        /*/
        TestLoops2();
        /**/

        GLES20.glUseProgram(Program[0]);

        camera.UpdateEye2D();

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        setRGBA(BACKGROUNDCOLOR);
        GLES20.glClearColor(r, g, b, a);

        //TODO Parallel thread that always saves all the visible Tiles, if not, the renderer is slower :(


        //ArrayList<Tile> visible = KDTREE.getVisibleTilesInCurrentTree(); // KDTREECOPY


        Log.d("Tiles: ", String.valueOf(VISIBLETILES.size()));


        Vector Cal = new Vector();
        float TILESIZEzoom = (float) Math.ceil(TILESIZE * (camera.getEye2D().getValue(2) / ZOOMFACTOR));


        mTextureUniformHandle = GLES20.glGetUniformLocation(Program[0], "u_Texture");
        TextureUniformHandleNormal = GLES20.glGetUniformLocation(Program[0], "n_Texture");



        LightUniformNumberHandle = GLES20.glGetUniformLocation(Program[0],"n_Lights");
        LightUniformHandle = GLES20.glGetUniformLocation(Program[0], "v_Light");
        LightColorUniformHandle = GLES20.glGetUniformLocation(Program[0], "v_Light_Color");



        PositionTileUniformHandle = GLES20.glGetUniformLocation(Program[0], "v_Tile");


        mTextureCoordinateHandle = GLES20.glGetAttribLocation(Program[0], "a_TexCoordinate");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(Program[0], "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(Program[0], "a_Position");


        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0, 0, -1.0f);


        float[] cubeTextureCoordinateData = {
                // Front face
                0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f};

        FloatBuffer mSquareTextureCoordinates;
        mSquareTextureCoordinates = ByteBuffer.allocateDirect(cubeTextureCoordinateData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mSquareTextureCoordinates.put(cubeTextureCoordinateData).position(0);


        // Pass in the texture coordinate information
        mSquareTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 0, mSquareTextureCoordinates);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);




        /**/

        Vector LightVec = camera.getEye2D(); // position of the Light

        float Rotationtime = 5000.0f; // in ms 5000
        float Rotationdistance = 3;//3

        float A = SystemClock.uptimeMillis() % Rotationtime;
        float Time = A / Rotationtime; // float from 0 to 1. It takes 5s to reset

        //Log.d("percentage:",String.valueOf(Time));

        float X = (float) (Rotationdistance * Math.sin(Math.PI * 2 * Time)); // rotating point in a distance of 5 tiles / units
        float Y = (float) (Rotationdistance * Math.cos(Math.PI * 2 * Time)); // rotating point in a distance of 5 tiles / units


        float[] LightPositionData = {(float) (LightVec.getValue(0) / TILESIZE) + X, (float) (LightVec.getValue(1) / TILESIZE) + Y, 10.0f / TILESIZE}; // light x, y, z
        GLES20.glUniform3f(LightUniformHandle, LightPositionData[0], LightPositionData[1], LightPositionData[2]);
        float[] LightColorData = { CUSTOM_BUTTON_SEEKBAR_RED, CUSTOM_BUTTON_SEEKBAR_GREEN, CUSTOM_BUTTON_SEEKBAR_BLUE}; // light x, y, z
        GLES20.glUniform4f(LightColorUniformHandle,LightColorData[0],LightColorData[1],LightColorData[2],1);



        /*/


        GLES20.glUniform1f(LightUniformNumberHandle,LIGHTS.size());


        float[] LightCoordinates = new float[LIGHTS.size() * 3];
        float[] LightColors = new float[LIGHTS.size() * 4];

        for (int i = 0; i < LIGHTS.size();i++){
            LightCoordinates[i*3    ] = (float)LIGHTS.get(i).getPositionvec().getX()/ TILESIZE;
            LightCoordinates[i*3 + 1] = (float)LIGHTS.get(i).getPositionvec().getY()/ TILESIZE;
            LightCoordinates[i*3 + 2] = (float)10.0f / TILESIZE;//LIGHTS.get(i).getPositionvec().getZ();

            LightColors[i*4    ] = ((float)Color.red(LIGHTS.get(i).getColorint())/255.0f);
            LightColors[i*4 + 1] = ((float)Color.green(LIGHTS.get(i).getColorint())/255.0f);
            LightColors[i*4 + 2] = ((float)Color.blue(LIGHTS.get(i).getColorint())/255.0f);
            LightColors[i*4 + 3] = ((float)Color.alpha(LIGHTS.get(i).getColorint())/255.0f);

        }

        FloatBuffer mLightCoordinates;
        mLightCoordinates = ByteBuffer.allocateDirect(LightCoordinates.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mLightCoordinates.put(LightCoordinates).position(0);


        FloatBuffer mLightColors;
        mLightColors = ByteBuffer.allocateDirect(LightColors.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mLightColors.put(LightColors).position(0);

        //GLES20.glGetUniformfv(Program[0],LightUniformHandle,LightCoordinates,0);

        GLES20.glUniform3fv(LightUniformHandle,LIGHTS.size(),mLightCoordinates);
        GLES20.glUniform4fv(LightColorUniformHandle,LIGHTS.size(),LightColors,0);


/**/




        for (int i = 0; i < VISIBLETILES.size(); i++) {

            try {
                if (VISIBLETILES.get(i).isOnScreen()) {

                    Cal = Cal.getScreencoordinatesFromTileCoordinates(VISIBLETILES.get(i).getPosition());


                    float left = (float) Cal.getValue(0);//(float) visible.get(i).getPositionRAW().getX() * Scale;//width / height;
                    float top = (float) Cal.getValue(1);//(float) visible.get(i).getPositionRAW().getY() * Scale;
                    float right = (float) Cal.getValue(0) + TILESIZEzoom;//(float) (visible.get(i).getPositionRAW().getX() + 1) * Scale;
                    float bottom = (float) Cal.getValue(1) + TILESIZEzoom;//(float) (visible.get(i).getPositionRAW().getY() + 1) * Scale;


                    left = 2 * ((left / WIDTHSCREEN) * 2 - 1);
                    top = 2 * (1 - (top / HEIGHTSCREEN) * 2);
                    right = 2 * ((right / WIDTHSCREEN) * 2 - 1);
                    bottom = 2 * (1 - (bottom / HEIGHTSCREEN) * 2);

                    try {
                        drawTextureLight(left, top, right, bottom, VISIBLETILES.get(i).getIDint(), VISIBLETILES.get(i).getMaterial(), VISIBLETILES.get(i).getPosition());
                    } catch (Exception e) {
                        Log.d("Render Texture: ", e + "");
                    }
                }
            } catch (Exception e) {
                Log.d("KDTREE: ", "Tile null error");
            }
        }


        mMVPMatrixHandle = GLES20.glGetUniformLocation(Program[1], "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(Program[1], "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(Program[1], "a_Color");

        GLES20.glUseProgram(Program[1]);


        /*/
        for (CustomButtons CB : CUSTOMBUTTONSLIST) {
            drawSquare(CB.getLeft(), CB.getTop(), CB.getRight(), CB.getBottom(), CB.getColor());
        }/**/

        for (int j = 0; j < CUSTOMBUTTONSLIST.size(); j++) {
            ArrayList<Rect> Boxes = CUSTOMBUTTONSLIST.get(j).getBoxes();
            ArrayList<Integer> BoxesColor = CUSTOMBUTTONSLIST.get(j).getColors();
            for (int i = 0; i < Boxes.size(); i++) {
                drawSquare(Boxes.get(i).left/128.0f, Boxes.get(i).top/128.0f, Boxes.get(i).right/128.0f, Boxes.get(i).bottom/128.0f,BoxesColor.get(i)); // BoxesColor.get(i)
            }
        }


        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(LightUniformHandle);
        GLES20.glDisableVertexAttribArray(LightColorUniformHandle);
        GLES20.glDisableVertexAttribArray(mMVPMatrixHandle);


    }


    /**
     * Draws a square / rect on the glsurface / screen using the shader and opengl coordinates
     *
     * @param left   left x  coordinate
     * @param top    top y coordinate
     * @param right  right x coordinate
     * @param bottom bottom y coordinate
     * @param color  Color for all 4 vertices
     */
    private void drawSquare(float left, float top, float right, float bottom, int color) { // 6 Values = 2 Triangles

        float red = ((float) Color.red(color)) / 255;
        float green = ((float) Color.green(color)) / 255;
        float blue = ((float) Color.blue(color)) / 255;
        float alpha = ((float) Color.alpha(color)) / 255;


        float[] squareVerticesData = {
                // X, Y, Z,
                // R, G, B, A
                left, bottom, 0.0f, red, green, blue, alpha,//1

                left, top, 0.0f, red, green, blue, alpha,//2

                right, top, 0.0f, red, green, blue, alpha,//3

                right, bottom, 0.0f, red, green, blue, alpha,//4

                right, top, 0.0f, red, green, blue, alpha,//3

                left, bottom, 0.0f, red, green, blue, alpha};//1

        FloatBuffer SquareVertices = ByteBuffer.allocateDirect(squareVerticesData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();

        SquareVertices.put(squareVerticesData).position(0);


        // Pass in the position information
        SquareVertices.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, mStrideBytes, SquareVertices);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        SquareVertices.position(mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false, mStrideBytes, SquareVertices);

        GLES20.glEnableVertexAttribArray(mColorHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6); // square = 4, triangle = 3
    }


    private void drawTextureLight(float left, float top, float right, float bottom, int ID, int Materialint, Vector Position) { // 6 Values = 2 Triangles


        TextureDataHandleNormal = TILETEXTURE.getTextureNormals(ID, Materialint);//TILETEXTURE.getTexture(ID, Materialint);
        // Bind the texture to this unit.

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);


        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureDataHandleNormal);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(TextureUniformHandleNormal, 0);


        mTextureDataHandle = TILETEXTURE.getTexture(ID, Materialint);/**/

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 1);


        //LightUniformHandle

        /** Store our model data in a float buffer. */
        /*/
        float[] LightPositionData = {
                (float)Lightsource.getPositionvec().getValue(0), (float)Lightsource.getPositionvec().getValue(1),  0.0f}; // light x, y, z
        /*/


        float[] PositionData = {(float) Position.getValue(0) / TILESIZE, (float) Position.getValue(1) / TILESIZE, 0.0f}; // light x, y, z
        GLES20.glUniform3f(PositionTileUniformHandle, PositionData[0], PositionData[1], PositionData[2]);


        /** Store our model data in a float buffer. */

        float[] squarePositionData = {left, bottom, 0.0f, left, top, 0.0f, right, top, 0.0f, right, bottom, 0.0f, right, top, 0.0f, left, bottom, 0.0f};


        FloatBuffer mSquarePositions = ByteBuffer.allocateDirect(squarePositionData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mSquarePositions.put(squarePositionData).position(0);

        // Pass in the position information
        mSquarePositions.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, 0, mSquarePositions);
        GLES20.glEnableVertexAttribArray(mPositionHandle);


        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        //// Pass in the modelview matrix.
        //GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }


    private void drawTexture(float left, float top, float right, float bottom, int ID, int Materialint) { // 6 Values = 2 Triangles


        TextureDataHandleNormal = TILETEXTURE.getTextureNormals(ID, Materialint);//TILETEXTURE.getTexture(ID, Materialint);
        // Bind the texture to this unit.

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);


        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureDataHandleNormal);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(TextureUniformHandleNormal, 0);


        mTextureDataHandle = TILETEXTURE.getTexture(ID, Materialint);/**/

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 1);

        //TODO Make working draw Texture class with new shaders to be able to draw textured buttons later

        /** Store our model data in a float buffer. */

        float[] squarePositionData = {left, bottom, 0.0f, left, top, 0.0f, right, top, 0.0f, right, bottom, 0.0f, right, top, 0.0f, left, bottom, 0.0f};


        FloatBuffer mSquarePositions = ByteBuffer.allocateDirect(squarePositionData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mSquarePositions.put(squarePositionData).position(0);

        // Pass in the position information
        mSquarePositions.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, 0, mSquarePositions);
        GLES20.glEnableVertexAttribArray(mPositionHandle);


        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        //// Pass in the modelview matrix.
        //GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }

    /**
     * Passes the number of Lights into the fragment shader
     */
    private int LightUniformNumberHandle;

    /**
     * This will be used to pass in the Tile itself.
     */
    private int PositionTileUniformHandle;

    /**
     * This will be used to pass in the light(s).
     */
    private int LightUniformHandle;//


    /**
     * This will be used to pass in the light(s) Color.
     */
    private int LightColorUniformHandle;

    /**
     * This will be used to pass in the texture.
     */
    private int mTextureUniformHandle;

    /**
     * This will be used to pass in the texture.
     */
    private int TextureUniformHandleNormal;
    /**
     * This will be used to pass in model texture coordinate information.
     */
    private int mTextureCoordinateHandle;

    /**
     * Size of the texture coordinate data in elements.
     */
    private int mTextureCoordinateDataSize = 2;

    /**
     * This is a handle to our texture data.
     */
    private int mTextureDataHandle;

    /**
     * This is a handle to our textures normal data.
     */
    private int TextureDataHandleNormal;


    private int createProgram(String vertexShader, String fragmentShader) {

        // Load in the vertex shader.
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, vertexShader);

            // Compile the shader.
            GLES20.glCompileShader(vertexShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }

        if (vertexShaderHandle == 0) {
            throw new RuntimeException("Error creating vertex shader.");
        }

        // Load in the fragment shader shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }

        if (fragmentShaderHandle == 0) {
            throw new RuntimeException("Error creating fragment shader.");
        }

        // Create a program object and store the handle to it.
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
            GLES20.glBindAttribLocation(programHandle, 2, "a_Normal");
            GLES20.glBindAttribLocation(programHandle, 3, "a_TexCoordinate");


            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }
        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }


        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");


        return programHandle;
    }



    private void TestLoops() { // Now updates all the Animations

        boolean TestUPDATEMATERIALS = UPDATERELOADEDMATERIALS;

        for (int i = 0; i < MATERIALARRAY.length; i++) { //TODO Updates the Texture if the Material has an Animation in a different, parallel Thread!
            try {
                if (MATERIALARRAY[i].hasAnimation()) {
                    TILETEXTURE.deleteTextures(i); // Both working perfectly!!
                    TILETEXTURE.updateTextures(i); // Both working perfectly!!

                }else {
                    if (UPDATERELOADEDMATERIALS && TestUPDATEMATERIALS){ // only loads the Bitmap to Textures if another lvl is loaded
                        TILETEXTURE.deleteTextures(i); // Both working perfectly!!
                        TILETEXTURE.updateTextures(i); // Both working perfectly!!
                    }
                }
            } catch (Exception e) {

            }
            if (TestUPDATEMATERIALS && UPDATERELOADEDMATERIALS) {
                UPDATERELOADEDMATERIALS = false;
            }
        }

    }
    private void TestLoops2() { // Now updates all the Animations that are visible on Screen!

        //boolean TestUPDATEMATERIALS = UPDATERELOADEDMATERIALS;

        if (AnimationsToUpdate.size() > 0) {
            for (int i = 0; i < AnimationsToUpdate.size(); i++) { //TODO Updates the Texture if the Material has an Animation in a different, parallel Thread!
                try {
                    TILETEXTURE.deleteTextures(AnimationsToUpdate.get(i)); // Both working perfectly!!
                    TILETEXTURE.updateTextures(AnimationsToUpdate.get(i)); // Both working perfectly!!
                }catch (Exception e){
                    Log.d("MainRendererTestLoops2","Out of Bounds");
                }
            }
        }

    }
}