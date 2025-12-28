package exe3KnockKnock;
import java.net.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.util.*;

public class RuppinRegistrationServer 
{
    public static final List<RuppinClient> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4445)) {
            System.out.println("RuppinServer is listening on port 4445...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("=== RUPPIN SERVER ACCEPTED: " + clientSocket + " ===");
                new Thread(new RuppinClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
        
    }
    public static void backupIfNeeded() {
        int size = clients.size();
        if (size % 3 != 0) return;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String stamp = LocalDateTime.now().format(fmt);

        Path dir = Paths.get("backups");              
        Path file = dir.resolve("backup_" + stamp + ".csv");

        try {
            Files.createDirectories(dir);

            try (BufferedWriter bw = Files.newBufferedWriter(file)) {
                bw.write("Username,Password,YearsAtRuppin,AcademicStatus");
                bw.newLine();

                for (RuppinClient c : clients) {
                    bw.write(csv(c.getUsername()) + "," +
                             csv(c.getPassword()) + "," +
                             c.getYearsAtRuppin() + "," +
                             csv(c.getAcademicStatus()));
                    bw.newLine();
                }
            }

            System.out.println("BACKUP CREATED: " + file.toAbsolutePath());

        } catch (Exception e) {
            System.err.println("Backup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String csv(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }

}

