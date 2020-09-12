package net.theopalgames.polywindow;

import java.io.InputStream;

public class ComputerFileManager extends BaseFileManager
{
    @Override
    public BaseFile getFile(String file)
    {
        return new ComputerFile(file);
    }

    @Override
    public InputStream getInternalFileContents(String file)
    {
//        Scanner s = new Scanner(new InputStreamReader(getClass().getResourceAsStream(file)));
//        List<String> al = new ArrayList<String>();
//
//        while (s.hasNext())
//        {
//            al.add(s.nextLine());
//        }
//
//        s.close();
//        return al;

        return getClass().getResourceAsStream(file);
    }
}
