// DataStorage.java
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private static final String DATA_DIR = "academic_planner_data";
    
    public static void saveTasks(String username, List<Task> tasks) {
        ensureDataDir();
        String filename = DATA_DIR + File.separator + username + "_tasks.ser";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(new ArrayList<>(tasks));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<Task> loadTasks(String username) {
        ensureDataDir();
        String filename = DATA_DIR + File.separator + username + "_tasks.ser";
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public static void saveReviews(String username, List<ReviewSession> reviews) {
        ensureDataDir();
        String filename = DATA_DIR + File.separator + username + "_reviews.ser";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(new ArrayList<>(reviews));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<ReviewSession> loadReviews(String username) {
        ensureDataDir();
        String filename = DATA_DIR + File.separator + username + "_reviews.ser";
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<ReviewSession>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    private static void ensureDataDir() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}