package net.theopalgames.basewindow.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import net.theopalgames.basewindow.BaseFile;
import net.theopalgames.basewindow.BaseFileManager;

import java.util.ArrayList;
import java.util.Arrays;

public class LibGdxFileManager extends BaseFileManager {
    @Override
    public LibGdxFile getFile(String file)
    {
        return new LibGdxFile(file);
    }

    @Override
    public ArrayList<String> getInternalFileContents(String file)
    {
        if (file.startsWith("/"))
            file = file.substring(1);

        FileHandle f = Gdx.files.internal(file);
        return new ArrayList<>(Arrays.asList(f.readString().replace("\r", "").split("\n")));
    }
}
