package mvb.findDoubleFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class findDoubleFiles {
    public static void main(String[] args) {

        Path startingDir = Paths.get("E:\\");
//        Path startingDir = Paths.get("I:\\CBOE\\Фото\\");
//        Path startingDir = Paths.get("I:\\CBOE\\Фото\\Поездки\\");
        MyFileVisitor pf = new MyFileVisitor();
        try {
            Files.walkFileTree(startingDir, pf);
//            System.out.println(pf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pf.printAll();
        pf.showDirsWithDoubles();
    }

}
