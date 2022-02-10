package mvb.findDoubleFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileElement implements Comparable<FileElement> {
    private String name;
    private long size;
    private List<Path> paths = new ArrayList<>();
    private int doubles;

    public FileElement(long size, Path path) {
        this.size = size;
        this.paths.add(path);
        this.doubles = 1;
        this.name = String.valueOf(path.getFileName());
    }

    public String getKey() {
        return name + size;
    }

    public void addDoubles() {
        doubles++;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public void addPath(Path path){
        this.paths.add(path);
    }

    public String getPathsString() {
        StringBuilder rez = new StringBuilder();
        for (Path path:paths             ) {
            rez.append("      ").append(path.toString()).append('\n');
        }
        return rez.toString();
    }

    public List<Path> getPaths() {
        return paths;
    }

    public int getDoubles() {
        return doubles;
    }

//    @Override
//    public String toString() {
//        return "FileElement{" +
//                "name='" + name + '\'' +
//                ", size=" + size +
//                ", path=" + path +
//                ", doubles=" + doubles +
//                '}';
//    }

    @Override
    public int compareTo(FileElement fileElement) {

        return Long.compare(fileElement.size* fileElement.doubles,this.size*this.doubles);
    }
}
