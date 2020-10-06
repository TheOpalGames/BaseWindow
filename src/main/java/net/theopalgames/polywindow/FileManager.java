package net.theopalgames.polywindow;

import java.io.InputStream;

public abstract class FileManager
{
    public abstract File getFile(String file);

    public abstract InputStream getInternalFileContents(String file);
}
