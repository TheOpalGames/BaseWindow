package net.theopalgames.polywindow.libgdx.html;

import java.io.*;

final class WindowConfig implements Serializable { // for serialization
    String name;
    int x;
    int y;
    int z;
    boolean vsync;
    boolean showMouse;
    
    WindowConfig(String name, int x, int y, int z, boolean vsync, boolean showMouse) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.vsync = vsync;
        this.showMouse = showMouse;
    }
    
    byte[] serialize() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(this);
        
        return out.toByteArray();
    }
    
    static WindowConfig deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream oin = new ObjectInputStream(in);
        return (WindowConfig) oin.readObject();
    }
}
