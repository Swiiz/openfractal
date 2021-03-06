package net.openfractal.graphics;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import net.openfractal.Main;
import net.openfractal.OpenFractal;
import net.openfractal.Utils;
import net.openfractal.maths.Vector2;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public class MainFrame extends MouseAdapter implements GLEventListener {

    private final String vertexShader = Utils.loadFileAsString(MainFrame.class.getResourceAsStream("/shaders/vertex.glsl")),
                         fragmentShader = Utils.loadFileAsString(MainFrame.class.getResourceAsStream("/shaders/fragment.glsl"));
    private final ShaderProgram sp = new ShaderProgram();

    private final Frame frame;
    private final FPSAnimator animator;

    private int width = 640, height = 400;

    private long initTimeMillis;
    private float time = 0f;

    private boolean zoomIn, zoomOut, toggle = false;

    private Vector2 position = new Vector2();
    private float zoom = -1f;

    private boolean queueReloadInjected = false;
    private String injectedShader;


    public MainFrame() {
        //getting the capabilities object of GL2 profile
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // The canvas
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        glcanvas.addGLEventListener(this);
        glcanvas.setSize(width, height);

        //creating frame
        frame = new Frame ("OpenFractal");
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                Main.end();
            }
        });
        glcanvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == 27 /* escape */) toggle = false;
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        glcanvas.addMouseListener(this);
            //adding canvas to frame
        frame.add(glcanvas);
        frame.setSize( 640, 480 );
        frame.setVisible(true);

        animator = new FPSAnimator(glcanvas, 144);
        animator.start();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        if(queueReloadInjected) {
            destroyShaders(drawable);
            linkShaders(drawable);
            queueReloadInjected = false;
        }


        float nTime = (System.currentTimeMillis() - initTimeMillis) / 1000f;
        float deltaTime = time - nTime;
        time = nTime;

        final GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        sp.useProgram(gl, true);
        gl.glUniform2f(gl.glGetUniformLocation(sp.program(), "of_dimensions"), width, height);
        gl.glUniform2f(gl.glGetUniformLocation(sp.program(), "of_cursorPos"), getCursorPos().x, getCursorPos().y);
        gl.glUniform2f(gl.glGetUniformLocation(sp.program(), "of_cursorPosFromCenter"), getCursorPosDeltaFromCenter().x, getCursorPosDeltaFromCenter().y);

        if(toggle) position = position.sub(getCursorPosDeltaFromCenter().devide(new Vector2(width, height)).sub(new Vector2(1f, 0f)).mul(deltaTime));

        gl.glUniform2f(gl.glGetUniformLocation(sp.program(), "of_position"), position.x, position.y);

        if(zoomIn) zoom += 0.3f * deltaTime;
        if(zoomOut) zoom -= 0.3f * deltaTime;
        gl.glUniform1f(gl.glGetUniformLocation(sp.program(), "of_zoom"), (float) Math.exp(zoom));

        gl.glUniform1f(gl.glGetUniformLocation(sp.program(), "of_time"), time);

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
        animator.stop();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        reloadInjectedShader();

        initTimeMillis = System.currentTimeMillis();

        GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        this.width = width; this.height = height;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        toggle = true;
        if(e.getButton() == 1) zoomIn = true;
        if(e.getButton() == 3) zoomOut = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == 1) zoomIn = false;
        if(e.getButton() == 3) zoomOut = false;
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

    private void destroyShaders(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        if(sp.inUse()) sp.useProgram(gl, false);
        sp.destroy(gl);
    }

    public void reloadInjectedShader() {
        try {
            injectedShader = Utils.loadFileAsString(new FileInputStream(OpenFractal.get().injectedShaderPath.toString()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        queueReloadInjected = true;
    }

    private Vector2 getCursorPos() {
        return new Vector2(MouseInfo.getPointerInfo().getLocation().x - frame.getLocation().x, MouseInfo.getPointerInfo().getLocation().y - frame.getLocation().y);
    }

    private Vector2 getCursorPosDeltaFromCenter() {
        return new Vector2(width/2f + frame.getLocation().x + MouseInfo.getPointerInfo().getLocation().x, height/2f + frame.getLocation().y - MouseInfo.getPointerInfo().getLocation().y);
    }
}
