package net.theopalgames.polywindow.libgdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Matrix4;
import net.theopalgames.polywindow.*;
import net.theopalgames.polywindow.transformation.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;

public class LibGdxWindow extends Window
{
//    public Application.ApplicationType appType;

    private boolean previousKeyboard = false;

    public ImmediateModeRenderer20 renderer;
    public SpriteBatch spriteBatch;

    public static final HashMap<Integer, Integer> key_translations = new HashMap<>();

    public HashMap<String, Texture> textures = new HashMap<>();

    public float color;
    public float transparent = Color.toFloatBits(0, 0, 0, 0);

    public float colorR;
    public float colorG;
    public float colorB;
    public float colorA;

    public Matrix4 perspective = new Matrix4();

    public List<Integer> rawTextInput = new ArrayList<Integer>();

    protected int currentDrawMode = -1;
    protected boolean depthTest = false;
    protected boolean depthMask = true;
    protected boolean glow;
    protected int currentVertices = 0;
    protected int maxVertices = 1000000;

    public boolean quadMode = false;
    public int quadNum = 0;

    public float col1;
    public float qx1;
    public float qy1;
    public float qz1;

    public float col3;
    public float qx3;
    public float qy3;
    public float qz3;

    private List<Transformation> transformations;

    private final PlatformHandler platform;

    public LibGdxWindow(Game game, PlatformHandler platform) {
        super(game);
        this.platform = platform;
    }

    public LibGdxWindow(WindowManager windowManager, String name, int x, int y, int z, boolean vsync, boolean showMouse, PlatformHandler platform)
    {
        super(windowManager, name, x, y, z, vsync, showMouse);
        this.platform = platform;
    }

    Game getGame() { // accessor method because game is protected in superclass.
        return game;
    }

    public void initialize()
    {
        setupKeyMap();

        perspective.idt().setToProjection(
                (float)(-absoluteWidth / (absoluteDepth * 2.0)),
                (float)(absoluteWidth / (absoluteDepth * 2.0)),
                (float) (absoluteHeight / (absoluteDepth * 2.0)),
                (float)(-absoluteHeight / (absoluteDepth * 2.0)),
                1, (float) (absoluteDepth * 2));
        perspective.translate((float) -(absoluteWidth / 2), (float) (-absoluteHeight / 2), (float) -absoluteDepth);

        renderer = new ImmediateModeRenderer20(maxVertices, false, true, 0);
        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(this.perspective);
        fontRenderer = new LibGdxFontRenderer(this, "font.png");

        this.soundsEnabled = true;
        this.soundPlayer = new LibGdxSoundPlayer();

        this.antialiasingSupported = true;

        Gdx.input.setInputProcessor(new InputAdapter()
        {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button)
            {
                touchVectors().put(pointer, new InputVector(x, y + absoluteHeight * keyboardOffset));
                absoluteMouseX = x;
                absoluteMouseY = y + absoluteHeight * keyboardOffset;
                pressedButtons.add(button);
                validPressedButtons.add(button);
                return true;
            }

            @Override
            public boolean touchDragged(int x, int y, int pointer)
            {
                validPressedButtons.remove((Integer) 0);
                InputVector i = touchVectors().get(pointer);
                i.x = x;
                i.y = y + absoluteHeight * keyboardOffset;

                if (Math.abs(i.x - i.startX) >= 10 || Math.abs(i.y - i.startY) >= 10)
                    i.valid = false;

                absoluteMouseX = x;
                absoluteMouseY = y + absoluteHeight * keyboardOffset;
                return true;
            }

            @Override
            public boolean touchUp(int x, int y, int pointer, int button)
            {
                absoluteMouseX = -1;
                absoluteMouseY = -1;
                touchVectors().remove(pointer);
                pressedButtons.remove((Integer)button);
                validPressedButtons.remove((Integer)button);
                return true;
            }

            @Override
            public boolean keyDown(int keyCode)
            {
                //rawTextInput.add(keyCode);

                int key = translateKey(keyCode);
                pressedKeys.add(key);
                validPressedKeys.add(key);

                textPressedKeys.add(key);
                textValidPressedKeys.add(key);
                return true;
            }

            @Override
            public boolean keyTyped(char keyCode)
            {
                rawTextInput.add((int) keyCode);
                return true;
            }

            @Override
            public boolean keyUp(int keyCode)
            {
                if (Gdx.app.getType() == Application.ApplicationType.Android)
                    return true;

                rawTextInput.remove((Integer) keyCode);

                int key = translateKey(keyCode);
                pressedKeys.remove((Integer) key);
                validPressedKeys.remove((Integer) key);

                textPressedKeys.remove((Integer) key);
                textValidPressedKeys.remove((Integer) key);
                return true;
            }
        });
    }

    private Map<Integer, InputVector> touchVectors() {
        return (Map<Integer, InputVector>) (Map) touchPoints; // weird rawtype to bypass intellij nonsense
    }

    public void setupKeyMap()
    {
        key_translations.put(ESCAPE, InputCodes.KEY_ESCAPE);
        key_translations.put(F1, InputCodes.KEY_F1);
        key_translations.put(F2, InputCodes.KEY_F2);
        key_translations.put(F3, InputCodes.KEY_F3);
        key_translations.put(F4, InputCodes.KEY_F4);
        key_translations.put(F5, InputCodes.KEY_F5);
        key_translations.put(F6, InputCodes.KEY_F6);
        key_translations.put(F7, InputCodes.KEY_F7);
        key_translations.put(F8, InputCodes.KEY_F8);
        key_translations.put(F9, InputCodes.KEY_F9);
        key_translations.put(F10, InputCodes.KEY_F10);
        key_translations.put(F11, InputCodes.KEY_F11);
        key_translations.put(F12, InputCodes.KEY_F12);

        key_translations.put(GRAVE, InputCodes.KEY_GRAVE_ACCENT);
        key_translations.put(NUM_1, InputCodes.KEY_1);
        key_translations.put(NUM_2, InputCodes.KEY_2);
        key_translations.put(NUM_3, InputCodes.KEY_3);
        key_translations.put(NUM_4, InputCodes.KEY_4);
        key_translations.put(NUM_5, InputCodes.KEY_5);
        key_translations.put(NUM_6, InputCodes.KEY_6);
        key_translations.put(NUM_7, InputCodes.KEY_7);
        key_translations.put(NUM_8, InputCodes.KEY_8);
        key_translations.put(NUM_9, InputCodes.KEY_9);
        key_translations.put(NUM_0, InputCodes.KEY_0);
        key_translations.put(MINUS, InputCodes.KEY_MINUS);
        key_translations.put(EQUALS, InputCodes.KEY_EQUAL);
        key_translations.put(BACKSPACE, InputCodes.KEY_BACKSPACE);

        key_translations.put(TAB, InputCodes.KEY_TAB);
        key_translations.put(LEFT_BRACKET, InputCodes.KEY_LEFT_BRACKET);
        key_translations.put(RIGHT_BRACKET, InputCodes.KEY_RIGHT_BRACKET);
        key_translations.put(BACKSLASH, InputCodes.KEY_BACKSLASH);
        key_translations.put(SEMICOLON, InputCodes.KEY_SEMICOLON);
        key_translations.put(APOSTROPHE, InputCodes.KEY_APOSTROPHE);
        key_translations.put(ENTER, InputCodes.KEY_ENTER);

        key_translations.put(SHIFT_LEFT, InputCodes.KEY_LEFT_SHIFT);
        key_translations.put(COMMA, InputCodes.KEY_COMMA);
        key_translations.put(PERIOD, InputCodes.KEY_PERIOD);
        key_translations.put(SLASH, InputCodes.KEY_SLASH);
        key_translations.put(SHIFT_RIGHT, InputCodes.KEY_RIGHT_SHIFT);

        key_translations.put(CONTROL_LEFT, InputCodes.KEY_LEFT_CONTROL);
        key_translations.put(CONTROL_RIGHT, InputCodes.KEY_RIGHT_CONTROL);
        key_translations.put(ALT_LEFT, InputCodes.KEY_LEFT_ALT);
        key_translations.put(ALT_RIGHT, InputCodes.KEY_RIGHT_ALT);
        key_translations.put(SPACE, InputCodes.KEY_SPACE);
        key_translations.put(UP, InputCodes.KEY_UP);
        key_translations.put(DOWN, InputCodes.KEY_DOWN);
        key_translations.put(LEFT, InputCodes.KEY_LEFT);
        key_translations.put(RIGHT, InputCodes.KEY_RIGHT);

        key_translations.put(Q, InputCodes.KEY_Q);
        key_translations.put(W, InputCodes.KEY_W);
        key_translations.put(E, InputCodes.KEY_E);
        key_translations.put(R, InputCodes.KEY_R);
        key_translations.put(T, InputCodes.KEY_T);
        key_translations.put(Y, InputCodes.KEY_Y);
        key_translations.put(U, InputCodes.KEY_U);
        key_translations.put(I, InputCodes.KEY_I);
        key_translations.put(O, InputCodes.KEY_O);
        key_translations.put(P, InputCodes.KEY_P);
        key_translations.put(A, InputCodes.KEY_A);
        key_translations.put(S, InputCodes.KEY_S);
        key_translations.put(D, InputCodes.KEY_D);
        key_translations.put(F, InputCodes.KEY_F);
        key_translations.put(G, InputCodes.KEY_G);
        key_translations.put(H, InputCodes.KEY_H);
        key_translations.put(J, InputCodes.KEY_J);
        key_translations.put(K, InputCodes.KEY_K);
        key_translations.put(L, InputCodes.KEY_L);
        key_translations.put(Z, InputCodes.KEY_Z);
        key_translations.put(X, InputCodes.KEY_X);
        key_translations.put(C, InputCodes.KEY_C);
        key_translations.put(V, InputCodes.KEY_V);
        key_translations.put(B, InputCodes.KEY_B);
        key_translations.put(N, InputCodes.KEY_N);
        key_translations.put(M, InputCodes.KEY_M);
    }

    public void updatePerspective()
    {
        perspective.idt().setToProjection(
                (float)(-absoluteWidth / (absoluteDepth * 2.0)),
                (float)(absoluteWidth / (absoluteDepth * 2.0)),
                (float) (absoluteHeight / (absoluteDepth * 2.0)),
                (float)(-absoluteHeight / (absoluteDepth * 2.0)),
                1, (float) (absoluteDepth * 2));
        perspective.translate((float) -(absoluteWidth / 2), (float) (-absoluteHeight / 2), (float) -absoluteDepth);
        perspective.translate(0, (float) -(keyboardOffset * absoluteHeight), 0);

        if (!this.showKeyboard && this.keyboardOffset > 0)
            this.keyboardOffset = Math.max(0, this.keyboardOffset * Math.pow(0.98, frameFrequency) - 0.015 * frameFrequency);

        spriteBatch.setProjectionMatrix(this.perspective);
    }

    @Override
    public void setIcon(String icon)
    {

    }

    public void setDrawMode(int mode, boolean depthTest, boolean depthMask, int vertices)
    {
        this.setDrawMode(mode, depthTest, depthMask, false, vertices);
    }

    public void setDrawMode(int mode, boolean depthTest, boolean depthMask, boolean glow, int vertices)
    {
        if (this.currentVertices + vertices > maxVertices || this.currentDrawMode != mode || this.depthTest != depthTest || this.depthMask != depthMask || this.glow != glow)
        {
            this.currentVertices = 0;

            if (this.currentDrawMode != 7)
                this.renderer.end();

            this.glow = glow;
            this.currentDrawMode = mode;
            this.depthTest = depthTest;
            this.depthMask = depthMask;

            if (mode == -1)
                return;

            if (depthTest)
            {
                Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
                //Gdx.gl.glDepthMask(false);
                //Gdx.gl.glDepthFunc(depthFunc);
                Gdx.gl.glDepthFunc(GL20.GL_LESS);
            }
            else
            {
                Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
                //Gdx.gl.glDepthMask(true);
                Gdx.gl.glDepthFunc(GL20.GL_ALWAYS);
            }

            Gdx.gl.glEnable(GL20.GL_BLEND);

            if (!glow)
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            else
                Gdx.gl.glBlendFunc(GL20.GL_SRC_COLOR, GL20.GL_ONE);

            Gdx.gl.glDepthMask(depthMask);

            if (mode != 7)
                this.renderer.begin(this.perspective, mode);
        }

        this.currentVertices += vertices;
    }

    public void render()
    {
        this.startTiming();

        this.updatePerspective();

        LibGdxSoundPlayer soundPlayer = (LibGdxSoundPlayer) this.soundPlayer;

        soundPlayer.musicPlaying = soundPlayer.currentMusic != null && soundPlayer.currentMusic.isPlaying();

        if (soundPlayer.prevMusic != null && soundPlayer.fadeEnd < System.currentTimeMillis())
        {
            if (soundPlayer.prevMusicStoppable && (soundPlayer.musicID == null || !soundPlayer.musicID.equals(soundPlayer.prevMusicID)))
                soundPlayer.prevMusic.stop();

            soundPlayer.prevMusic = null;

            if (soundPlayer.currentMusic != null)
                soundPlayer.currentMusic.setVolume(soundPlayer.currentVolume);
        }

        if (soundPlayer.prevMusic != null && soundPlayer.currentMusic != null)
        {
            double frac = (System.currentTimeMillis() - soundPlayer.fadeBegin) * 1.0 / (soundPlayer.fadeEnd - soundPlayer.fadeBegin);

            if (soundPlayer.prevMusicStoppable)
                soundPlayer.prevMusic.setVolume((float) (soundPlayer.prevVolume * (1 - frac)));

            soundPlayer.currentMusic.setVolume((float) (soundPlayer.currentVolume * frac));
        }

		/*Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);*/

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (this.previousKeyboard != this.showKeyboard)
        {
//            Gdx.input.setOnscreenKeyboardVisible(Game.game.window.showKeyboard);
            Gdx.input.setOnscreenKeyboardVisible(showKeyboard);
            this.previousKeyboard = this.showKeyboard;
        }

//        if (Gdx.app.getType() == Application.ApplicationType.Android)
//            this.keyboardFraction = Tanks.keyboardHeightListener.getUsableWindowHeight();
        this.keyboardFraction = platform.getUsableWindowHeight();

        this.updater.update();
        this.drawer.draw();

        this.setDrawMode(-1, false, true, 0);

        if (Gdx.app.getType() == Application.ApplicationType.Android)
        {
            this.pressedKeys.clear();
            this.validPressedKeys.clear();

            this.textPressedKeys.clear();
            this.textValidPressedKeys.clear();
        }

        this.stopTiming();
    }

    @Override
    public void run()
    {
        // NOOP - nothing to set up, already running
    }

    @Override
    public void setShowCursor(boolean show)
    {
        // also NOOP
    }

    @Override
    public void fillOval(double x, double y, double sX, double sY)
    {
        x += sX / 2;
        y += sY / 2;

        int sides = (int) (sX + sY) / 4 + 5;

        this.setDrawMode(GL20.GL_TRIANGLES, false, true, sides * 3);
        double step = Math.PI * 2 / sides;

        float pX =  (float) (x + Math.cos(0) * sX / 2);
        float pY =  (float) (y + Math.sin(0) * sY / 2);
        double d = 0;
        for (int n = 0; n < sides; n++)
        {
            d += step;

            renderer.color(color);
            renderer.vertex(pX, pY, 0);
            pX = (float) (x + Math.cos(d) * sX / 2);
            pY = (float) (y + Math.sin(d) * sY / 2);
            renderer.color(color);
            renderer.vertex(pX, pY, 0);
            renderer.color(color);
            renderer.vertex((float) x, (float) y, 0);
        }
    }

    @Override
    public void fillOval(double x, double y, double z, double sX, double sY, boolean depthTest)
    {
        x += sX / 2;
        y += sY / 2;

        int sides = (int) (sX + sY + Math.max(z / 20, 0)) / 4 + 5;

        this.setDrawMode(GL20.GL_TRIANGLES, depthTest, colorA >= 1, sides * 3);
        double step = Math.PI * 2 / sides;

        float pX =  (float) (x + Math.cos(0) * sX / 2);
        float pY =  (float) (y + Math.sin(0) * sY / 2);
        double d = 0;
        for (int n = 0; n < sides; n++)
        {
            d += step;

            renderer.color(color);
            renderer.vertex((float) x, (float) y, (float) z);
            renderer.color(color);
            renderer.vertex(pX, pY, (float) z);
            pX = (float) (x + Math.cos(d) * sX / 2);
            pY = (float) (y + Math.sin(d) * sY / 2);
            renderer.color(color);
            renderer.vertex(pX, pY, (float) z);
        }
    }

    @Override
    public void fillFacingOval(double x, double y, double z, double sX, double sY, boolean depthTest)
    {

    }

    @Override
    public void fillGlow(double x, double y, double sX, double sY)
    {
        x += sX / 2;
        y += sY / 2;

        int sides = (int) (sX + sY) / 16 + 5;

        this.color = Color.toFloatBits(this.colorR * this.colorA, this.colorG * this.colorA, this.colorB * this.colorA, 1);
        this.setDrawMode(GL20.GL_TRIANGLES, false, false, true,sides * 3);
        double step = Math.PI * 2 / sides;

        float pX =  (float) (x + Math.cos(0) * sX / 2);
        float pY =  (float) (y + Math.sin(0) * sY / 2);
        double d = 0;
        for (int n = 0; n < sides; n++)
        {
            d += step;

            renderer.color(transparent);
            renderer.vertex(pX, pY, 0);
            pX = (float) (x + Math.cos(d) * sX / 2);
            pY = (float) (y + Math.sin(d) * sY / 2);
            renderer.color(transparent);
            renderer.vertex(pX, pY, 0);
            renderer.color(color);
            renderer.vertex((float) x, (float) y, 0);
        }
    }

    @Override
    public void fillGlow(double x, double y, double z, double sX, double sY, boolean depthTest)
    {
        x += sX / 2;
        y += sY / 2;

        int sides = (int) (sX + sY + Math.max(z / 20, 0)) / 16 + 5;

        this.color = Color.toFloatBits(this.colorR * this.colorA, this.colorG * this.colorA, this.colorB * this.colorA, 1);
        this.setDrawMode(GL20.GL_TRIANGLES, depthTest, false, true, sides * 3);
        double step = Math.PI * 2 / sides;

        float pX =  (float) (x + Math.cos(0) * sX / 2);
        float pY =  (float) (y + Math.sin(0) * sY / 2);
        double d = 0;
        for (int n = 0; n < sides; n++)
        {
            d += step;

            renderer.color(color);
            renderer.vertex((float) x, (float) y, (float) z);
            renderer.color(transparent);
            renderer.vertex(pX, pY, (float) z);
            pX = (float) (x + Math.cos(d) * sX / 2);
            pY = (float) (y + Math.sin(d) * sY / 2);
            renderer.color(transparent);
            renderer.vertex(pX, pY, (float) z);
        }
    }

    @Override
    public void fillFacingGlow(double x, double y, double z, double sX, double sY, boolean depthTest)
    {

    }

    @Override
    public void setColor(double r, double g, double b, double a)
    {
        this.colorR = (float) (r / 255.0);
        this.colorG = (float) (g / 255.0);
        this.colorB = (float) (b / 255.0);
        this.colorA = (float) (a / 255.0);
        this.color = Color.toFloatBits(colorR, colorG, colorB, colorA);
    }

    @Override
    public void setColor(double r, double g, double b)
    {
        this.colorR = (float) (r / 255.0);
        this.colorG = (float) (g / 255.0);
        this.colorB = (float) (b / 255.0);
        this.colorA = 1;
        this.color = Color.toFloatBits(colorR, colorG, colorB, 1);
    }

    @Override
    public void drawOval(double x, double y, double sX, double sY)
    {
        drawOval(x, y, 0, sX, sY);
    }

    @Override
    public void drawOval(double x, double y, double z, double sX, double sY)
    {
        x += sX / 2;
        y += sY / 2;

        int sides = (int) (sX + sY + 5);

        this.setDrawMode(GL20.GL_LINES, false, true, sides * 2);

        for (double i = 0; i < Math.PI * 2; i += Math.PI * 2 / sides)
        {
            renderer.color(color);
            renderer.vertex((float) (x + Math.cos(i) * sX / 2), (float) (y + Math.sin(i) * sY / 2), (float) z);
            renderer.color(color);
            renderer.vertex((float) (x + Math.cos(i + Math.PI * 2 / sides) * sX / 2), (float) (y + Math.sin(i + Math.PI * 2 / sides) * sY / 2), (float) z);
        }
    }

    @Override
    public void fillRect(double x, double y, double width, double height)
    {
        this.setDrawMode(GL20.GL_TRIANGLES, false, true, 6);
        renderer.color(color);
        renderer.vertex((float) x, (float) y, 0);
        renderer.color(color);
        renderer.vertex((float) (x + width), (float) y, 0);
        renderer.color(color);
        renderer.vertex((float) (x + width), (float) (y + height), 0);

        renderer.color(color);
        renderer.vertex((float) x, (float) y, 0);
        renderer.color(color);
        renderer.vertex((float) x, (float) (y + height), 0);
        renderer.color(color);
        renderer.vertex((float) (x + width), (float) (y + height), 0);
    }

    @Override
    public void fillBox(double x, double y, double z, double sX, double sY, double sZ)
    {
        fillBox(x, y, z, sX, sY, sZ, (byte) 0);
    }

    /**
     * Options byte:
     *
     * 0: default
     *
     * +1 hide behind face
     * +2 hide front face
     * +4 hide bottom face
     * +8 hide top face
     * +16 hide left face
     * +32 hide right face
     *
     * +64 draw on top
     * */
    public void fillBox(double posX, double posY, double posZ, double sX, double sY, double sZ, byte options)
    {
        float x = (float) posX;
        float y = (float) posY;
        float z = (float) posZ;
        float width = (float) sX;
        float height = (float) sY;
        float depth = (float) sZ;

        float color2 = Color.toFloatBits(this.colorR * 0.8f, this.colorG * 0.8f, this.colorB * 0.8f, this.colorA);
        float color3 = Color.toFloatBits(this.colorR * 0.6f, this.colorG * 0.6f, this.colorB * 0.6f, this.colorA);

        if ((options >> 6) % 2 == 0)
            this.setDrawMode(GL20.GL_TRIANGLES, true, true, 36);
        else
            this.setDrawMode(GL20.GL_TRIANGLES, false, true, 36);

        if (options % 2 == 0)
        {
            renderer.color(color);
            renderer.vertex(x, y, z);
            renderer.color(color);
            renderer.vertex(x + width, y, z);
            renderer.color(color);
            renderer.vertex(x + width, y + height, z);

            renderer.color(color);
            renderer.vertex(x, y, z);
            renderer.color(color);
            renderer.vertex(x + width, y + height, z);
            renderer.color(color);
            renderer.vertex(x, y + height, z);
        }

        if ((options >> 2) % 2 == 0)
        {
            renderer.color(color2);
            renderer.vertex(x, y + height, z);
            renderer.color(color2);
            renderer.vertex(x + width, y + height, z);
            renderer.color(color2);
            renderer.vertex(x + width, y + height, z + depth);

            renderer.color(color2);
            renderer.vertex(x, y + height, z);
            renderer.color(color2);
            renderer.vertex(x + width, y + height, z + depth);
            renderer.color(color2);
            renderer.vertex(x, y + height, z + depth);
        }

        if ((options >> 3) % 2 == 0)
        {
            renderer.color(color2);
            renderer.vertex(x, y, z + depth);
            renderer.color(color2);
            renderer.vertex(x + width, y, z + depth);
            renderer.color(color2);
            renderer.vertex(x + width, y, z);

            renderer.color(color2);
            renderer.vertex(x, y, z + depth);
            renderer.color(color2);
            renderer.vertex(x + width, y, z);
            renderer.color(color2);
            renderer.vertex(x, y, z);
        }

        if ((options >> 4) % 2 == 0)
        {
            renderer.color(color3);
            renderer.vertex(x, y, z + depth);
            renderer.color(color3);
            renderer.vertex(x, y, z);
            renderer.color(color3);
            renderer.vertex(x, y + height, z);

            renderer.color(color3);
            renderer.vertex(x, y, z + depth);
            renderer.color(color3);
            renderer.vertex(x, y + height, z);
            renderer.color(color3);
            renderer.vertex(x, y + height, z + depth);
        }

        if ((options >> 5) % 2 == 0)
        {
            renderer.color(color3);
            renderer.vertex(x + width, y, z);
            renderer.color(color3);
            renderer.vertex(x + width, y, z + depth);
            renderer.color(color3);
            renderer.vertex(x + width, y + height, z + depth);

            renderer.color(color3);
            renderer.vertex(x + width, y, z);
            renderer.color(color3);
            renderer.vertex(x + width, y + height, z + depth);
            renderer.color(color3);
            renderer.vertex(x + width, y + height, z);
        }

        if ((options >> 1) % 2 == 0)
        {
            renderer.color(color);
            renderer.vertex(x + width, y, z + depth);
            renderer.color(color);
            renderer.vertex(x, y, z + depth);
            renderer.color(color);
            renderer.vertex(x + width, y + height, z + depth);

            renderer.color(color);
            renderer.vertex(x + width, y + height, z + depth);
            renderer.color(color);
            renderer.vertex(x, y, z + depth);
            renderer.color(color);
            renderer.vertex(x, y + height, z + depth);
        }
    }

    @Override
    public void fillQuad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
    {
        this.setDrawMode(GL20.GL_TRIANGLES, false, true, 6);

        renderer.color(color);
        renderer.vertex((float) x1, (float) y1, 0);
        renderer.color(color);
        renderer.vertex((float) x2, (float) y2, 0);
        renderer.color(color);
        renderer.vertex((float) x3, (float) y3, 0);

        renderer.color(color);
        renderer.vertex((float) x1, (float) y1, 0);
        renderer.color(color);
        renderer.vertex((float) x4, (float) y4, 0);
        renderer.color(color);
        renderer.vertex((float) x3, (float) y3, 0);
    }

    /**
     * Options byte:
     *
     * 0: default
     *
     * +1 hide behind face
     * +2 hide front face
     * +4 hide bottom face
     * +8 hide top face
     * +16 hide left face
     * +32 hide right face
     *
     * +64 draw on top
     * */
    @Override
    public void fillQuadBox(double posx1, double posy1,
                            double posx2, double posy2,
                            double posx3, double posy3,
                            double posx4, double posy4,
                            double posz, double sizeZ,
                            byte options)
    {
        float x1 = (float) posx1;
        float x2 = (float) posx2;
        float x3 = (float) posx3;
        float x4 = (float) posx4;

        float y1 = (float) posy1;
        float y2 = (float) posy2;
        float y3 = (float) posy3;
        float y4 = (float) posy4;

        float z = (float) posz;
        float sZ = (float) sizeZ;

        float color2 = Color.toFloatBits(this.colorR * 0.8f, this.colorG * 0.8f, this.colorB * 0.8f, this.colorA);
        float color3 = Color.toFloatBits(this.colorR * 0.6f, this.colorG * 0.6f, this.colorB * 0.6f, this.colorA);

        if ((options >> 6) % 2 == 0)
            this.setDrawMode(GL20.GL_TRIANGLES, true, true, 36);
        else
            this.setDrawMode(GL20.GL_TRIANGLES, false, true, 36);

        if (options % 2 == 0)
        {
            renderer.color(color);
            renderer.vertex(x1, y1, z);
            renderer.color(color);
            renderer.vertex(x2, y2, z);
            renderer.color(color);
            renderer.vertex(x3, y3, z);

            renderer.color(color);
            renderer.vertex(x1, y1, z);
            renderer.color(color);
            renderer.vertex(x4, y4, z);
            renderer.color(color);
            renderer.vertex(x3, y3, z);
        }

        if ((options >> 2) % 2 == 0)
        {
            renderer.color(color3);
            renderer.vertex(x1, y1, z + sZ);
            renderer.color(color3);
            renderer.vertex(x2, y2, z + sZ);
            renderer.color(color3);
            renderer.vertex(x2, y2, z);

            renderer.color(color3);
            renderer.vertex(x1, y1, z + sZ);
            renderer.color(color3);
            renderer.vertex(x1, y1, z);
            renderer.color(color3);
            renderer.vertex(x2, y2, z);
        }

        if ((options >> 3) % 2 == 0)
        {
            renderer.color(color3);
            renderer.vertex(x3, y3, z + sZ);
            renderer.color(color3);
            renderer.vertex(x4, y4, z + sZ);
            renderer.color(color3);
            renderer.vertex(x4, y4, z);

            renderer.color(color3);
            renderer.vertex(x3, y3, z + sZ);
            renderer.color(color3);
            renderer.vertex(x3, y3, z);
            renderer.color(color3);
            renderer.vertex(x4, y4, z);
        }

        if ((options >> 4) % 2 == 0)
        {
            renderer.color(color2);
            renderer.vertex(x1, y1, z + sZ);
            renderer.color(color2);
            renderer.vertex(x4, y4, z + sZ);
            renderer.color(color2);
            renderer.vertex(x4, y4, z);

            renderer.color(color2);
            renderer.vertex(x1, y1, z + sZ);
            renderer.color(color2);
            renderer.vertex(x1, y1, z);
            renderer.color(color2);
            renderer.vertex(x4, y4, z);
        }

        if ((options >> 5) % 2 == 0)
        {
            renderer.color(color2);
            renderer.vertex(x3, y3, z + sZ);
            renderer.color(color2);
            renderer.vertex(x2, y2, z + sZ);
            renderer.color(color2);
            renderer.vertex(x2, y2, z);

            renderer.color(color2);
            renderer.vertex(x3, y3, z + sZ);
            renderer.color(color2);
            renderer.vertex(x3, y3, z);
            renderer.color(color2);
            renderer.vertex(x2, y2, z);
        }

        if ((options >> 1) % 2 == 0)
        {
            renderer.color(color);
            renderer.vertex(x1, y1, z + sZ);
            renderer.color(color);
            renderer.vertex(x2, y2, z + sZ);
            renderer.color(color);
            renderer.vertex(x3, y3, z + sZ);

            renderer.color(color);
            renderer.vertex(x1, y1, z + sZ);
            renderer.color(color);
            renderer.vertex(x4, y4, z + sZ);
            renderer.color(color);
            renderer.vertex(x3, y3, z + sZ);
        }
    }

    @Override
    public void drawRect(double x, double y, double sX, double sY)
    {
        this.setDrawMode(GL20.GL_LINES, false, true, 8);
        renderer.color(color);
        renderer.vertex((float) x, (float) y, 0);
        renderer.color(color);
        renderer.vertex((float) (x + sX), (float) y, 0);

        renderer.color(color);
        renderer.vertex((float) x, (float) y, 0);
        renderer.color(color);
        renderer.vertex((float) x, (float) (y + sY), 0);

        renderer.color(color);
        renderer.vertex((float) (x + sX), (float) y, 0);
        renderer.color(color);
        renderer.vertex((float) (x + sX), (float) (y + sY), 0);

        renderer.color(color);
        renderer.vertex((float) x, (float) (y + sY), 0);
        renderer.color(color);
        renderer.vertex((float) (x + sX), (float) (y + sY), 0);
    }

    @Override
    public void drawImage(double x, double y, double sX, double sY, String image, boolean scaled)
    {
        drawImage(x, y, 0, sX, sY, 0, 0, 1, 1, image, scaled, false);
    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, String image, boolean scaled)
    {
        drawImage(x, y, z, sX, sY, 0, 0, 1, 1, image, scaled, true);
    }

//    @Override
//    public void setUpPerspective()
//    {
//
//    }
//
//    @Override
//    public void applyTransformations()
//    {
//
//    }
//
//    @Override
//    public void loadPerspective()
//    {
//
//    }

    @Override
    public void drawImage(double x, double y, double sX, double sY, double u1, double v1, double u2, double v2, String image, boolean scaled)
    {
        drawImage(x, y, 0, sX, sY, u1, v1, u2, v2, image, scaled, false);
    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, double u1, double v1, double u2, double v2, String image, boolean scaled)
    {
        drawImage(x, y, z, sX, sY, u1, v1, u2, v2, image, scaled, true);
    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, double u1, double v1, double u2, double v2, String image, boolean scaled, boolean depthtest)
    {
        this.setDrawMode(7, depthtest, true, 0);

        if (image.startsWith("/"))
            image = image.substring(1);

        Texture texture = textures.get(image);

        if (texture == null)
        {
            texture = new Texture(Gdx.files.internal(image));
            textures.put(image, texture);
        }

        double width = sX * (u2 - u1);
        double height = sY * (v2 - v1);

        if (scaled)
        {
            width *= texture.getWidth();
            height *= texture.getHeight();
        }

        spriteBatch.getProjectionMatrix().translate(0, 0, (float) (z));
        spriteBatch.begin();
        spriteBatch.setColor(this.colorR, this.colorG, this.colorB, this.colorA);
        spriteBatch.draw(texture, (float) x, (float) y, (float) width, (float) height, (float) u1, (float) v1, (float) u2, (float) v2);
        spriteBatch.end();
        spriteBatch.getProjectionMatrix().translate(0, 0, (float) (-z));

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void drawImage(double x, double y, double sX, double sY, String image, double rotation, boolean scaled)
    {
        rotate(x, y, rotation);
        this.drawImage(x - sX / 2, y - sY / 2, sX, sY, image, scaled);
        rotate(x, y, -rotation);
    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, String image, double rotation, boolean scaled)
    {
        rotate(x, y, rotation);
        this.drawImage(x - sX / 2, y - sY / 2, z, sX, sY, image, scaled);
        rotate(x, y, -rotation);
    }

    @Override
    public void drawImage(double x, double y, double sX, double sY, double u1, double v1, double u2, double v2, String image, double rotation, boolean scaled)
    {
        rotate(x, y, rotation);
        this.drawImage(x - sX / 2, y - sY / 2, sX, sY, u1, v1, u2, v2, image, scaled);
        rotate(x, y, -rotation);
    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, double u1, double v1, double u2, double v2, String image, double rotation, boolean scaled)
    {
        rotate(x, y, rotation);
        this.drawImage(x - sX / 2, y - sY / 2, z, sX, sY, u1, v1, u2, v2, image, scaled);
        rotate(x, y, -rotation);
    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, double u1, double v1, double u2, double v2, String image, double rotation, boolean scaled, boolean depthtest)
    {
        rotate(x, y, rotation);
        this.drawImage(x - sX / 2, y - sY / 2, z, sX, sY, u1, v1, u2, v2, image, scaled, depthtest);
        rotate(x, y, -rotation);
    }

    public void rotate(double x, double y, double rotation)
    {
        spriteBatch.getProjectionMatrix().translate((float) x, (float) y, 0);
        spriteBatch.getProjectionMatrix().rotateRad((float) 0, (float) 0, 1, (float) rotation);
        spriteBatch.getProjectionMatrix().translate((float) -x, (float) -y, 0);
    }

    @Override
    public String getClipboard()
    {
        return Gdx.app.getClipboard().getContents();
    }

    @Override
    public void setClipboard(String s)
    {
        Gdx.app.getClipboard().setContents(s);
    }

    @Override
    public void setVsync(boolean enable)
    {

    }

    @Override
    public List<Integer> getRawTextKeys()
    {
        return rawTextInput;
    }

    @Override
    public String getKeyText(int key)
    {
        return (char) key + "";
    }

    @Override
    public String getTextKeyText(int key)
    {
        return (char) key + "";
    }

    @Override
    public int translateKey(int key)
    {
        Integer k = key_translations.get(key);

        if (k == null)
            return key;

        return k;
    }

    @Override
    public int translateTextKey(int key)
    {
        return key;
    }

//    @Override
    public void transform(double[] matrix)
    {

    }

    @Override
    public double getEdgeBounds()
    {
        return Math.max(absoluteWidth - absoluteHeight * 18 / 9, 0) / 2;
    }

    @Override
    public void setBatchMode(boolean enabled, boolean quads, boolean depth)
    {
        this.setDrawMode(GL20.GL_TRIANGLES, depth, this.colorA >= 1, 1000);
        if (quads)
        {
            quadMode = true;
            quadNum = 0;
        }
        else
            quadMode = false;
    }

    @Override
    public void setBatchMode(boolean enabled, boolean quads, boolean depth, boolean glow)
    {
        this.setDrawMode(GL20.GL_TRIANGLES, depth, this.colorA >= 1 && !glow, glow,1000);
        if (quads)
        {
            quadMode = true;
            quadNum = 0;
        }
        else
            quadMode = false;
    }

    @Override
    public void addVertex(double x, double y, double z)
    {
        if (quadMode)
        {
            if (quadNum == 0)
            {
                qx1 = (float) x;
                qy1 = (float) y;
                qz1 = (float) z;
                col1 = color;
            }
            else if (quadNum == 2)
            {
                qx3 = (float) x;
                qy3 = (float) y;
                qz3 = (float) z;
                col3 = color;
            }
            else if (quadNum == 3)
            {
                renderer.color(col1);
                renderer.vertex(qx1, qy1, qz1);
                renderer.color(col3);
                renderer.vertex(qx3, qy3, qz3);
            }
            quadNum = (quadNum + 1) % 4;
        }

        renderer.color(color);
        renderer.vertex((float) x, (float) y, (float) z);
    }

    @Override
    public void addVertex(double x, double y)
    {
        if (quadMode)
        {
            if (quadNum == 0)
            {
                qx1 = (float) x;
                qy1 = (float) y;
                qz1 = (float) 0;
                col1 = color;
            }
            else if (quadNum == 2)
            {
                qx3 = (float) x;
                qy3 = (float) y;
                qz3 = (float) 0;
                col3 = color;
            }
            else if (quadNum == 3)
            {
                renderer.color(col1);
                renderer.vertex(qx1, qy1, qz1);
                renderer.color(col3);
                renderer.vertex(qx3, qy3, qz3);
            }
            quadNum = (quadNum + 1) % 4;
        }

        renderer.color(color);
        renderer.vertex((float) x, (float) y, 0);
    }

    public void drawLinkedImage(double x, double y, double z, double sX, double sY, double u1, double v1, double u2, double v2, String image, boolean scaled, boolean depthtest)
    {
        if (image.startsWith("/"))
            image = image.substring(1);

        Texture texture = textures.get(image);

        if (texture == null)
        {
            texture = new Texture(Gdx.files.internal(image));
            textures.put(image, texture);
        }

        double width = sX * (u2 - u1);
        double height = sY * (v2 - v1);

        if (scaled)
        {
            width *= texture.getWidth();
            height *= texture.getHeight();
        }

        spriteBatch.setColor(this.colorR, this.colorG, this.colorB, this.colorA);
        spriteBatch.draw(texture, (float) x, (float) y, (float) width, (float) height, (float) u1, (float) v1, (float) u2, (float) v2);
    }

    @Override
    public List<Transformation> getTransformations() {
        return transformations;
    }

    @Override
    public Framework getFramework() {
        return Framework.LIBGDX;
    }

    @Override
    public void quit() {
        platform.quit();
    }

    @Override
    public boolean canQuit() {
        return platform.canQuit();
    }

    @Override
    protected Window newWindow(String name, int x, int y, int z, boolean vsync, boolean showMouse) {
        return platform.newWindow(getWindowManager(), name, x, y, z, vsync, showMouse);
    }

    @Override
    protected boolean canMakeNewWindow() {
        return platform.canMakeNewWindow();
    }
}

