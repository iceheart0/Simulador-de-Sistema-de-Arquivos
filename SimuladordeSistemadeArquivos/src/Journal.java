import java.util.ArrayList;
import java.util.List;

public class Journal {
    private List<String> log;

    public Journal() {
        this.log = new ArrayList<>();
    }

    public void addEntry(String entry) {
        log.add(entry);
    }

    public List<String> getLog() {
        return log;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "log=" + log +
                '}';
    }
}