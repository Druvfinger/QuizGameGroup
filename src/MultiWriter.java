import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MultiWriter {
    private List<PrintWriter> writers = new ArrayList<>();

    public void addWriter(PrintWriter writer) {
        writers.add(writer);
    }

    public List<PrintWriter> getWriters() {
        return writers;
    }
}