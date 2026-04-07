import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.Color;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String title;
    private String description;
    private LocalDate dueDate;
    private String subject;
    private Priority priority;
    private boolean completed;
    
    public enum Priority {
        HIGH("High"),
        MEDIUM("Medium"),
        LOW("Low");
        
        private String displayName;
        
        Priority(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public Task(String title, String description, LocalDate dueDate, String subject, Priority priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.subject = subject;
        this.priority = priority;
        this.completed = false;
    }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    public String getFormattedDueDate() {
        return dueDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }
    
    public Color getPriorityColor() {
        switch (priority) {
            case HIGH: return new Color(231, 76, 60);
            case MEDIUM: return new Color(241, 196, 15);
            case LOW: return new Color(46, 204, 113);
            default: return Color.GRAY;
        }
    }
}