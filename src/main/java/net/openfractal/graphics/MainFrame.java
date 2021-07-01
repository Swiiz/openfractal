package net.openfractal.graphics;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import net.openfractal.Main;
import net.openfractal.Utils;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame implements GLEventListener {

    private final String vertexShader = Utils.loadFileAsString("/shaders/vertex.glsl");
    private final String fragmentShader = Utils.loadFileAsString("/shaders/fragment.glsl");
    private final String injectedShader = """
            const vec4 colorPalette = -vec4(0,23,21,0);
            const vec4 finalColor = vec4(.0);
            const int hueModulus = 30;
                        
            uniform vec2 dims;
                        
            out vec4 fragColor;
                        
            void main() {
                float zoom = 5.;
                int recursion = 50, i = 0;
                vec2 c = ((gl_FragCoord.xy / dims.y) - .5 - vec2(0.5*dims.x/dims.y-0.5, 0.)) * zoom, z = c;
                for(; i <= recursion && length(z) <= 2. ; i++ ) z = cpx_pow(z, 2.) + c;
                fragColor =  i - 1 == recursion ? finalColor : .6 + .6 * cos( 6.3 *  (float((i - 1) % hueModulus) / float(hueModulus)) + colorPalette);
            }
            """;

    private final ShaderProgram sp = new ShaderProgram();

    private int width = 640, height = 400;

    public MainFrame() {
        //getting the capabilities object of GL2 profile
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // The canvas
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        glcanvas.addGLEventListener(this);
        glcanvas.setSize(width, height);

        //creating frame
        final Frame frame = new Frame ("Main frame");
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                Main.end();
            }
        });
            //adding canvas to frame
        frame.add(glcanvas);
        frame.setSize( 640, 480 );
        frame.setVisible(true);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        sp.useProgram(gl, true);
        gl.glUniform2f(gl.glGetUniformLocation(sp.program(), "dims"), width, height);
        gl.glBegin(GL2.GL_QUAD_STRIP);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex2f(-1, -1);
        gl.glVertex2f(1, -1);
        gl.glVertex2f(-1, 1);
        gl.glVertex2f(1, 1);
        gl.glEnd();
        sp.useProgram(gl, false);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        //method body
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        linkShaders(drawable);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        this.width = width; this.height = height;
    }

    private void linkShaders(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        final ShaderCode vs = new ShaderCode(GL2ES2.GL_VERTEX_SHADER, 1, new CharSequence[][] { { new StringBuilder(vertexShader) } });
        vs.defaultShaderCustomization(gl, true, true);
        sp.add(gl, vs, System.err);
        final ShaderCode fs = new ShaderCode(GL2ES2.GL_FRAGMENT_SHADER, 1, new CharSequence[][] { { new StringBuilder(fragmentShader).append(injectedShader) } });
        fs.defaultShaderCustomization(gl, true, true);
        sp.add(gl, fs, System.err);
        sp.link(gl, System.out);
    }

}
