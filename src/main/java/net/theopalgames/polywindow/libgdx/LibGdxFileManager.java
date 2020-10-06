package net.theopalgames.polywindow.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import net.theopalgames.polywindow.FileManager;

import java.io.InputStream;

public class LibGdxFileManager extends FileManager {
    @Override
    public LibGdxFile getFile(String file)
    {
        return new LibGdxFile(file);
    }

    @Override
    public InputStream getInternalFileContents(String file)
    {
        if (file.startsWith("/"))
            file = file.substring(1);

        FileHandle f = Gdx.files.internal(file);
//        return new ArrayList<>(Arrays.asList(f.readString().replace("\r", "").split("\n")));

        return f.read();
    }
}
