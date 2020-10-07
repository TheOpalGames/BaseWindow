package net.theopalgames.polywindow;

/**
 * The different frameworks that can be used by PolyWindow.
 *
 * @author aehmttw and hallowizer
 */
public enum Framework {
    /**
     * The LWJGL framework, which uses OpenGL.
     */
    LWJGL,
    
    /**
     * The Java Swing framework, using the javax.swing package.
     */
    SWING,
    
    /**
     * The LibGDX framework, used to play in html and on mobile devices.
     */
    LIBGDX,
    
    /**
     * The Metal framework, will be used by macs in the future as OpenGL has been deprecated by Apple.
     */
    METAL;
}
