import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReviewSession implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String title;
    private String subject;
    private LocalDate date;
    private String topic;
    private int durationMinutes;
    private boolean completed;
    
    public ReviewSession(String title, String subject, LocalDate date, String topic, int durationMinutes) {
        this.title = title;
        this.subject = subject;
        this.date = date;
        this.topic = topic;
        this.durationMinutes = durationMinutes;
        this.completed = false;
    }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }
    
    public String getDurationFormatted() {
        return durationMinutes + " min";
    }
}