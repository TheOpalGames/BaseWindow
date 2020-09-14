package net.theopalgames.polywindow;

import java.io.InputStream;
import java.util.List;

public abstract class BaseFileManager
{
    public abstract BaseFile getFile(String file);

    public abstract InputStream getInternalFileContents(String file);
}
