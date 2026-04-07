import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {
    private String username;
    private JPanel contentPanel;
    private JLabel pageTitleLabel;
    private JButton dashboardBtn, plannerBtn, reviewBtn;
    
    private List<Task> tasks;
    private List<ReviewSession> reviews;
    private DefaultTableModel taskTableModel;
    private DefaultTableModel reviewTableModel;
    
    public MainFrame(String username) {
    try {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) { }
    
    this.username = username;
        
        this.username = username;
        this.tasks = DataStorage.loadTasks(username);
        this.reviews = DataStorage.loadReviews(username);
        
        setTitle("Academic Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
        
        setLayout(new BorderLayout());
        
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);
        
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(contentPanel, BorderLayout.CENTER);
        
        contentPanel.add(createDashboardPanel(), "Dashboard");
        contentPanel.add(createPlannerPanel(), "Planner");
        contentPanel.add(createReviewPanel(), "Review");
        
        showPanel("Dashboard");
        
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Academic Planner");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("Welcome, " + username.split("@")[0]);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        
        JButton logoutBtn = createStyledButton("Log Out", new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame();
                dispose();
            }
        });
        
        rightPanel.add(userLabel);
        rightPanel.add(logoutBtn);
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        dashboardBtn = createSidebarButton("Dashboard", new Color(52, 152, 219));
        plannerBtn = createSidebarButton("Planner", new Color(52, 152, 219));
        reviewBtn = createSidebarButton("Review", new Color(52, 152, 219));
        
        dashboardBtn.addActionListener(e -> showPanel("Dashboard"));
        plannerBtn.addActionListener(e -> showPanel("Planner"));
        reviewBtn.addActionListener(e -> showPanel("Review"));
        
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(dashboardBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(plannerBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(reviewBtn);
        sidebar.add(Box.createVerticalGlue());
        
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBackground(new Color(236, 240, 241));
        searchField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(80, 80, 80)),
        BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        
        JButton searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchBtn.setBackground(new Color(52, 152, 219));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setOpaque(true);           // Added for color consistency
        searchBtn.setContentAreaFilled(true);
        searchBtn.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        searchBtn.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.BLACK, 1), // Black line
        BorderFactory.createEmptyBorder(6, 10, 6, 10)   // Internal padding
    ));
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().toLowerCase();
            performSearch(query);
        });
        
        searchField.addActionListener(e -> performSearch(searchField.getText().toLowerCase()));
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);
        
        sidebar.add(searchPanel);
        sidebar.add(Box.createVerticalStrut(15));
        
        return sidebar;
    }
    
    private JButton createSidebarButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.BLACK, 1), // Thin black line
        BorderFactory.createEmptyBorder(10, 15, 10, 15) // Internal padding
    ));
    
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        return button;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
private void showPanel(String panelName) {
    CardLayout cl = (CardLayout) contentPanel.getLayout();
    cl.show(contentPanel, panelName);
    
    resetSidebarButtons(); // Sets all buttons to their "inactive" base colors
    
    switch (panelName) {
        case "Dashboard":
            // Brighter blue for the background when active
            dashboardBtn.setBackground(new Color(52, 152, 219)); 
            break;
        case "Planner":
            // Brighter green for the background when active
            plannerBtn.setBackground(new Color(46, 204, 113));
            break;
        case "Review":
            // Brighter purple for the background when active
            reviewBtn.setBackground(new Color(155, 89, 182));
            break;
    }
    dashboardBtn.repaint();
    plannerBtn.repaint();
    reviewBtn.repaint();
}
    
    private void resetSidebarButtons() {
    dashboardBtn.setBackground(new Color(41, 128, 185)); // Dark Blue
    plannerBtn.setBackground(new Color(39, 174, 96));   // Dark Green
    reviewBtn.setBackground(new Color(142, 68, 173)); // Dark Purple
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        
        pageTitleLabel = new JLabel("Dashboard");
        pageTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pageTitleLabel.setForeground(new Color(44, 62, 80));
        panel.add(pageTitleLabel, BorderLayout.NORTH);
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        statsPanel.add(createStatCard("Total Tasks", "0", new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Completed Tasks", "0", new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Review Sessions", "0", new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Upcoming Deadlines", "0", new Color(241, 196, 15)));
        
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 15, 0));
        mainContent.setOpaque(false);
        
        JPanel upcomingPanel = new JPanel(new BorderLayout());
        upcomingPanel.setBackground(Color.WHITE);
        upcomingPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel upcomingLabel = new JLabel("Upcoming Tasks");
        upcomingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        upcomingPanel.add(upcomingLabel, BorderLayout.NORTH);
        
        JList<String> upcomingTaskList = new JList<>();
        upcomingTaskList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        upcomingTaskList.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        JScrollPane upcomingScroll = new JScrollPane(upcomingTaskList);
        upcomingPanel.add(upcomingScroll, BorderLayout.CENTER);
        
        JPanel reviewPanel = new JPanel(new BorderLayout());
        reviewPanel.setBackground(Color.WHITE);
        reviewPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel reviewLabel = new JLabel("Upcoming Reviews");
        reviewLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        reviewPanel.add(reviewLabel, BorderLayout.NORTH);
        
        JList<String> upcomingReviewList = new JList<>();
        upcomingReviewList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        upcomingReviewList.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        JScrollPane reviewScroll = new JScrollPane(upcomingReviewList);
        reviewPanel.add(reviewScroll, BorderLayout.CENTER);
        
        mainContent.add(upcomingPanel);
        mainContent.add(reviewPanel);
        
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(mainContent, BorderLayout.SOUTH);
        
        panel.putClientProperty("statsPanel", statsPanel);
        panel.putClientProperty("upcomingTaskList", upcomingTaskList);
        panel.putClientProperty("upcomingReviewList", upcomingReviewList);
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);
        
        return card;
    }   
    
        private void refreshDashboard() {
        JPanel dashboardPanel = (JPanel) contentPanel.getComponent(0);
        
        int totalTasks = tasks.size();
        int completedTasks = (int) tasks.stream().filter(Task::isCompleted).count();
        int totalReviews = reviews.size();
        long upcomingDeadlines = tasks.stream()
            .filter(t -> !t.isCompleted() && t.getDueDate().isAfter(LocalDate.now()))
            .count();
        
        JPanel statsPanel = (JPanel) dashboardPanel.getClientProperty("statsPanel");
        if (statsPanel != null) {
            Component[] components = statsPanel.getComponents();
            if (components.length >= 4) {
                updateStatCard((JPanel) components[0], "Total Tasks", String.valueOf(totalTasks), new Color(52, 152, 219));
                updateStatCard((JPanel) components[1], "Completed Tasks", String.valueOf(completedTasks), new Color(46, 204, 113));
                updateStatCard((JPanel) components[2], "Review Sessions", String.valueOf(totalReviews), new Color(155, 89, 182));
                updateStatCard((JPanel) components[3], "Upcoming Deadlines", String.valueOf(upcomingDeadlines), new Color(241, 196, 15));
            }
        }
        
        @SuppressWarnings("unchecked")
        JList<String> upcomingTaskList = (JList<String>) dashboardPanel.getClientProperty("upcomingTaskList");
        if (upcomingTaskList != null) {
            List<String> upcomingTasks = tasks.stream()
                .filter(t -> !t.isCompleted() && t.getDueDate().isAfter(LocalDate.now()))
                .sorted((a, b) -> a.getDueDate().compareTo(b.getDueDate()))
                .limit(10)
                .map(t -> t.getTitle() + " - " + t.getFormattedDueDate())
                .collect(java.util.stream.Collectors.toList());
            
            if (upcomingTasks.isEmpty()) {
                upcomingTasks.add("No upcoming tasks");
            }
            upcomingTaskList.setListData(upcomingTasks.toArray(new String[0]));
        }
        
        @SuppressWarnings("unchecked")
        JList<String> upcomingReviewList = (JList<String>) dashboardPanel.getClientProperty("upcomingReviewList");
        if (upcomingReviewList != null) {
            List<String> upcomingReviews = reviews.stream()
                .filter(r -> !r.isCompleted() && r.getDate().isAfter(LocalDate.now()))
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .limit(10)
                .map(r -> r.getTitle() + " - " + r.getFormattedDate())
                .collect(java.util.stream.Collectors.toList());
            
            if (upcomingReviews.isEmpty()) {
                upcomingReviews.add("No upcoming reviews");
            }
            upcomingReviewList.setListData(upcomingReviews.toArray(new String[0]));
        }
    }
    
    private void updateStatCard(JPanel card, String title, String value, Color color) {
        Component[] comps = card.getComponents();
        if (comps.length >= 2 && comps[0] instanceof JLabel && comps[1] instanceof JLabel) {
            ((JLabel) comps[0]).setText(title);
            ((JLabel) comps[1]).setText(value);
            ((JLabel) comps[1]).setForeground(color);
        }
    }
    
    private JPanel createPlannerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setOpaque(false);
        
        JButton addTaskBtn = createStyledButton("+ Add Task", new Color(46, 204, 113));
        JButton deleteTaskBtn = createStyledButton("Delete Selected", new Color(231, 76, 60));
        
        addTaskBtn.addActionListener(e -> openAddTaskDialog());
        deleteTaskBtn.addActionListener(e -> deleteSelectedTask());
        
        toolbar.add(addTaskBtn);
        toolbar.add(deleteTaskBtn);
        
        String[] columns = {"Title", "Subject", "Due Date", "Priority", "Status"};
        taskTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        
        JTable taskTable = new JTable(taskTableModel);
        taskTable.setRowHeight(30);
        taskTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taskTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        taskTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        
        taskTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 4) {
                int row = e.getFirstRow();
                tasks.get(row).setCompleted((Boolean) taskTableModel.getValueAt(row, 4));
                saveAllData();
                refreshDashboard();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createReviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setOpaque(false);
        
        JButton addReviewBtn = createStyledButton("+ Schedule Review", new Color(46, 204, 113));
        JButton deleteReviewBtn = createStyledButton("Delete Selected", new Color(231, 76, 60));
        
        addReviewBtn.addActionListener(e -> openAddReviewDialog());
        deleteReviewBtn.addActionListener(e -> deleteSelectedReview());
        
        toolbar.add(addReviewBtn);
        toolbar.add(deleteReviewBtn);
        
        String[] columns = {"Title", "Subject", "Date", "Topic", "Duration", "Status"};
        reviewTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        
        JTable reviewTable = new JTable(reviewTableModel);
        reviewTable.setRowHeight(30);
        reviewTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reviewTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        reviewTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        
        reviewTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 5) {
                int row = e.getFirstRow();
                reviews.get(row).setCompleted((Boolean) reviewTableModel.getValueAt(row, 5));
                saveAllData();
                refreshDashboard();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(reviewTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void refreshTaskTable() {
        taskTableModel.setRowCount(0);
        for (Task task : tasks) {
            taskTableModel.addRow(new Object[]{
                task.getTitle(),
                task.getSubject(),
                task.getFormattedDueDate(),
                task.getPriority().getDisplayName(),
                task.isCompleted()
            });
        }
    }
    
    private void refreshReviewTable() {
        reviewTableModel.setRowCount(0);
        for (ReviewSession review : reviews) {
            reviewTableModel.addRow(new Object[]{
                review.getTitle(),
                review.getSubject(),
                review.getFormattedDate(),
                review.getTopic(),
                review.getDurationFormatted(),
                review.isCompleted()
            });
        }
    }

        private void openAddTaskDialog() {
        JDialog dialog = new JDialog(this, "Add New Task", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField titleField = new JTextField(20);
        JTextField subjectField = new JTextField(20);
        JTextField dueDateField = new JTextField(LocalDate.now().toString());
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        JComboBox<Task.Priority> priorityCombo = new JComboBox<>(Task.Priority.values());
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Subject:"), gbc);
        gbc.gridx = 1;
        formPanel.add(subjectField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Due Date (YYYY-MM-DD):*"), gbc);
        gbc.gridx = 1;
        formPanel.add(dueDateField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(descArea), gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Priority:"), gbc);
        gbc.gridx = 1;
        formPanel.add(priorityCombo, gbc);
        
        JButton saveBtn = createStyledButton("Save Task", new Color(46, 204, 113));
        JButton cancelBtn = createStyledButton("Cancel", new Color(149, 165, 166));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String dueDateStr = dueDateField.getText().trim();
            
            if (title.isEmpty() || dueDateStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                LocalDate dueDate = LocalDate.parse(dueDateStr);
                Task task = new Task(
                    title,
                    descArea.getText(),
                    dueDate,
                    subjectField.getText().isEmpty() ? "General" : subjectField.getText(),
                    (Task.Priority) priorityCombo.getSelectedItem()
                );
                tasks.add(task);
                saveAllData();
                refreshTaskTable();
                refreshDashboard();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Task added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format. Use YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }
    
    private void openAddReviewDialog() {
        JDialog dialog = new JDialog(this, "Schedule Review Session", true);
        dialog.setSize(450, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField titleField = new JTextField(20);
        JTextField subjectField = new JTextField(20);
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JTextField topicField = new JTextField(20);
        JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(60, 15, 480, 15));
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Subject:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(subjectField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Date (YYYY-MM-DD):*"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Topic:"), gbc);
        gbc.gridx = 1;
        formPanel.add(topicField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Duration (minutes):"), gbc);
        gbc.gridx = 1;
        formPanel.add(durationSpinner, gbc);
        
        JButton saveBtn = createStyledButton("Schedule Review", new Color(46, 204, 113));
        JButton cancelBtn = createStyledButton("Cancel", new Color(149, 165, 166));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String subject = subjectField.getText().trim();
            String dateStr = dateField.getText().trim();
            
            if (title.isEmpty() || subject.isEmpty() || dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                LocalDate date = LocalDate.parse(dateStr);
                ReviewSession review = new ReviewSession(
                    title,
                    subject,
                    date,
                    topicField.getText(),
                    (int) durationSpinner.getValue()
                );
                reviews.add(review);
                saveAllData();
                refreshReviewTable();
                refreshDashboard();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Review session scheduled!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format. Use YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }
    
    private void deleteSelectedTask() {
        JPanel plannerPanel = (JPanel) contentPanel.getComponent(1);
        JScrollPane scrollPane = (JScrollPane) plannerPanel.getComponent(1);
        JTable table = (JTable) scrollPane.getViewport().getView();
        
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < tasks.size()) {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this task?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tasks.remove(selectedRow);
                saveAllData();
                refreshTaskTable();
                refreshDashboard();
                JOptionPane.showMessageDialog(this, "Task deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void deleteSelectedReview() {
        JPanel reviewPanel = (JPanel) contentPanel.getComponent(2);
        JScrollPane scrollPane = (JScrollPane) reviewPanel.getComponent(1);
        JTable table = (JTable) scrollPane.getViewport().getView();
        
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < reviews.size()) {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this review session?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                reviews.remove(selectedRow);
                saveAllData();
                refreshReviewTable();
                refreshDashboard();
                JOptionPane.showMessageDialog(this, "Review session deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a review to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void performSearch(String query) {
        if (query.isEmpty()) {
            refreshTaskTable();
            refreshReviewTable();
            return;
        }
        
        List<Task> filteredTasks = tasks.stream()
            .filter(t -> t.getTitle().toLowerCase().contains(query) ||
                        t.getSubject().toLowerCase().contains(query) ||
                        t.getDescription().toLowerCase().contains(query))
            .collect(java.util.stream.Collectors.toList());
        
        taskTableModel.setRowCount(0);
        for (Task task : filteredTasks) {
            taskTableModel.addRow(new Object[]{
                task.getTitle(),
                task.getSubject(),
                task.getFormattedDueDate(),
                task.getPriority().getDisplayName(),
                task.isCompleted()
            });
        }
        
        List<ReviewSession> filteredReviews = reviews.stream()
            .filter(r -> r.getTitle().toLowerCase().contains(query) ||
                        r.getSubject().toLowerCase().contains(query) ||
                        r.getTopic().toLowerCase().contains(query))
            .collect(java.util.stream.Collectors.toList());
        
        reviewTableModel.setRowCount(0);
        for (ReviewSession review : filteredReviews) {
            reviewTableModel.addRow(new Object[]{
                review.getTitle(),
                review.getSubject(),
                review.getFormattedDate(),
                review.getTopic(),
                review.getDurationFormatted(),
                review.isCompleted()
            });
        }
        
        if (filteredTasks.isEmpty() && filteredReviews.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No results found for: " + query, "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void saveAllData() {
        DataStorage.saveTasks(username, tasks);
        DataStorage.saveReviews(username, reviews);
    }
}