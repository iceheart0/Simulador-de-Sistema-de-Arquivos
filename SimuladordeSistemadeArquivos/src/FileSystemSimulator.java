import java.util.HashMap;
import java.util.Map;

public class FileSystemSimulator {
    private Directory root;
    private Journal journal;
    private Map<String, Directory> directoryMap;
    private Map<String, File> fileMap;

    public FileSystemSimulator() {
        this.root = new Directory("root");
        this.journal = new Journal();
        this.directoryMap = new HashMap<>();
        this.fileMap = new HashMap<>();
        directoryMap.put("root", root);
    }

    private String joinPath(String[] parts, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            if (i > start) {
                sb.append("/");
            }
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    public void createFile(String path, String name) {
        Directory dir = directoryMap.get(path);
        if (dir != null) {
            File file = new File(name);
            dir.addFile(file);
            fileMap.put(path + "/" + name, file);
            journal.addEntry("Created file: " + path + "/" + name);
        } else {
            System.out.println("Directory not found: " + path);
        }
    }

    public void createDirectory(String path, String name) {
        Directory parentDir = directoryMap.get(path);
        if (parentDir != null) {
            Directory newDir = new Directory(name);
            parentDir.addDirectory(newDir);
            directoryMap.put(path + "/" + name, newDir);
            journal.addEntry("Created directory: " + path + "/" + name);
        } else {
            System.out.println("Directory not found: " + path);
        }
    }

    public void deleteFile(String path) {
        File file = fileMap.get(path);
        if (file != null) {
            String[] parts = path.split("/");
            String dirPath = joinPath(parts, 0, parts.length - 1);
            Directory dir = directoryMap.get(dirPath);
            if (dir != null) {
                dir.removeFile(file);
                fileMap.remove(path);
                journal.addEntry("Deleted file: " + path);
            }
        } else {
            System.out.println("File not found: " + path);
        }
    }

    public void deleteDirectory(String path) {
        Directory dir = directoryMap.get(path);
        if (dir != null) {
            String[] parts = path.split("/");
            String parentPath = joinPath(parts, 0, parts.length - 1);
            Directory parentDir = directoryMap.get(parentPath);
            if (parentDir != null) {
                parentDir.removeDirectory(dir);
                directoryMap.remove(path);
                journal.addEntry("Deleted directory: " + path);
            }
        } else {
            System.out.println("Directory not found: " + path);
        }
    }

    public void renameFile(String oldPath, String newName) {
        File file = fileMap.get(oldPath);
        if (file != null) {
            String[] parts = oldPath.split("/");
            String dirPath = joinPath(parts, 0, parts.length - 1);
            String newPath = dirPath + "/" + newName;
            file.setName(newName);
            fileMap.remove(oldPath);
            fileMap.put(newPath, file);
            journal.addEntry("Renamed file from " + oldPath + " to " + newPath);
        } else {
            System.out.println("File not found: " + oldPath);
        }
    }

    public void renameDirectory(String oldPath, String newName) {
        Directory dir = directoryMap.get(oldPath);
        if (dir != null) {
            String[] parts = oldPath.split("/");
            String parentPath = joinPath(parts, 0, parts.length - 1);
            String newPath = parentPath + "/" + newName;
            dir.setName(newName);
            directoryMap.remove(oldPath);
            directoryMap.put(newPath, dir);
            journal.addEntry("Renamed directory from " + oldPath + " to " + newPath);
        } else {
            System.out.println("Directory not found: " + oldPath);
        }
    }

    public void listDirectory(String path) {
        Directory dir = directoryMap.get(path);
        if (dir != null) {
            System.out.println("Listing directory: " + path);
            System.out.println("Directories:");
            for (Directory subDir : dir.getDirectories()) {
                System.out.println(subDir.getName());
            }
            System.out.println("Files:");
            for (File file : dir.getFiles()) {
                System.out.println(file.getName());
            }
        } else {
            System.out.println("Directory not found: " + path);
        }
    }

    public void printJournal() {
        System.out.println("Journal log:");
        for (String entry : journal.getLog()) {
            System.out.println(entry);
        }
    }

    public static void main(String[] args) {
        FileSystemSimulator simulator = new FileSystemSimulator();

        simulator.createDirectory("root", "documents");
        simulator.createFile("root/documents", "file1.txt");
        simulator.createFile("root/documents", "file2.txt");
        simulator.listDirectory("root/documents");

        simulator.renameFile("root/documents/file1.txt", "file1-renamed.txt");
        simulator.listDirectory("root/documents");

        simulator.deleteFile("root/documents/file2.txt");
        simulator.listDirectory("root/documents");

        simulator.deleteDirectory("root/documents");
        simulator.listDirectory("root");

        simulator.printJournal();
    }
}
