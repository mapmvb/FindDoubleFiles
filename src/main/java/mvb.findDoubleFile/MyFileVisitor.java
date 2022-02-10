package mvb.findDoubleFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.nio.file.FileVisitResult.*;

public class MyFileVisitor extends SimpleFileVisitor<Path> {
    TreeMap<String, FileElement> files = new TreeMap<>();
    Map<String, Long> dirs = new HashMap<>();

    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attr) {
        if (attr.isSymbolicLink()) {
            System.out.format("Symbolic link: %s ", file);
        } else if (attr.isRegularFile()) {
//            System.out.format("Regular file: %s ", file.getFileName());
            FileElement fileElement = new FileElement(attr.size(), file);
            if (!files.containsKey(fileElement.getKey())) {
                files.put(fileElement.getKey(), fileElement);
            } else {
                files.get(fileElement.getKey()).addDoubles();
                files.get(fileElement.getKey()).addPath(file);
            }
        } else {
            System.out.format("Other: %s ", file);
        }
//        System.out.println("(" + attr.size() + "bytes)");
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir,
                                              IOException exc) {
//        System.out.format("Directory: %s%n", dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file,
                                           IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }

    public void printAll() {
        final long[] saves = {0};
//        files.forEach((K,V)-> System.out.println(K.toString()+"->"+V.toString()));
        String savedBytes = "";
        files.values().stream()
                .filter(p->p.getDoubles()>1)
                .sorted()
//                .sorted(Comparator.comparing(FileElement::getDoubles).reversed()
//                        .thenComparing(Comparator.comparing(FileElement::getSize).reversed())
//                        .thenComparing(Comparator.comparing(FileElement::getName).reversed()))
                .forEach((p) -> {
                    String s = String.format("%,d",p.getSize());
                    System.out.println(p.getDoubles() + " " + s + " " + p.getName());
                    System.out.print(p.getPathsString());
                    saves[0] += p.getSize()*(p.getDoubles()-1);
                });
        savedBytes = String.format("%,d",saves[0]);
        System.out.println("Будет освобождено:"+savedBytes+"bytes");
    }

    public void showDirsWithDoubles(){
        for (Map.Entry<String,FileElement> file: files.entrySet()) {
            if( file.getValue().getDoubles() > 1) {
                for (Path path : file.getValue().getPaths()) {
//                    DirElement dirElement = new DirElement(path.toString(), file.getValue().getSize());
                    String dir = path.getParent().toString();
                    if (!dirs.containsKey(dir)) {
                        dirs.put(dir, file.getValue().getSize());
                    } else {
                        long l = dirs.get(dir) + file.getValue().getSize();
                        dirs.put(dir, l);
                    }
                }
            }
        }
        dirs.entrySet().stream()
                .sorted(Comparator.comparingLong(Map.Entry::getValue))
//                .sorted(Comparator.comparingLong(e->e.getValue()))
                .forEach((e)-> {
                    String s = String.format("%,d",e.getValue());
                    System.out.println("Size:"+s+" Dir:"+e.getKey()+";");
                });

//        System.out.println(dirs);

    }

}
