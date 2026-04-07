import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.io.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    
    private HashMap<String, String> users;
    private static final String USER_FILE = "academic_planner_data/users.ser";
    
    public LoginFrame() {
        loadUsers(); // Load saved users from file
        
        setTitle("Academic Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(44, 62, 80), 0, getHeight(), new Color(52, 73, 94));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);
        
        // Card panel for login form
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        cardPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Welcome Back label
        JLabel welcomeLabel = new JLabel("WELCOME BACK");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(44, 62, 80));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cardPanel.add(welcomeLabel, gbc);
        
        // Email label and field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cardPanel.add(emailLabel, gbc);
        
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        emailField = new JTextField(30);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        cardPanel.add(emailField, gbc);
        
        // Password label and field
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cardPanel.add(passwordLabel, gbc);
        
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        cardPanel.add(passwordField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);
        
        loginButton = new JButton("Log In");
        loginButton.setBackground(new Color(100, 149, 237));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(true);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(9, 121, 105));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setOpaque(true);
        registerButton.setBorderPainted(true);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        cardPanel.add(buttonPanel, gbc);
        
        mainPanel.add(cardPanel);
        
        // Button actions
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> openRegisterDialog());
        
        // Enter key to login
        getRootPane().setDefaultButton(loginButton);
        
        setVisible(true);
    }
    
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (users.containsKey(email) && users.get(email).equals(password)) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new MainFrame(email);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openRegisterDialog() {
        JDialog registerDialog = new JDialog(this, "Register", true);
        registerDialog.setSize(380, 420); // Slightly taller to fit the styled buttons
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setLayout(new BorderLayout());
        registerDialog.setResizable(false);

        // Main background panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Fields
        gbc.gridwidth = 1;
        gbc.gridy = 1; panel.add(new JLabel("Email:"), gbc);
        gbc.gridy = 2; 
        JTextField regEmail = new JTextField(20);
        regEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        regEmail.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        panel.add(regEmail, gbc);

        gbc.gridy = 3; panel.add(new JLabel("Password:"), gbc);
        gbc.gridy = 4; 
        JPasswordField regPassword = new JPasswordField(20);
        regPassword.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        panel.add(regPassword, gbc);

        gbc.gridy = 5; panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridy = 6; 
        JPasswordField confirmPassword = new JPasswordField(20);
        confirmPassword.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        panel.add(confirmPassword, gbc);

        // --- BUTTON STYLING ---
        
        // Register Button (Teal Green)
        JButton createButton = new JButton("Register");
        createButton.setBackground(new Color(9, 121, 105));
        createButton.setForeground(Color.WHITE);
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createButton.setFocusPainted(false);
        createButton.setContentAreaFilled(false);
        createButton.setOpaque(true);
        createButton.setUI(new BasicButtonUI()); // Ensures custom colors render properly
        createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Cancel Button (Grayish Blue)
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(136, 8, 8));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setOpaque(true);
        cancelBtn.setUI(new BasicButtonUI());
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Actions
        createButton.addActionListener(e -> {
            String email = regEmail.getText().trim();
            String pass = new String(regPassword.getPassword());
            String confirm = new String(confirmPassword.getPassword());
            
            if (email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(registerDialog, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(registerDialog, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (users.containsKey(email)) {
                JOptionPane.showMessageDialog(registerDialog, "Email already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                users.put(email, pass);
                saveUsers();
                JOptionPane.showMessageDialog(registerDialog, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                registerDialog.dispose();
            }
        });

        cancelBtn.addActionListener(e -> registerDialog.dispose());

        // Layout Buttons
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 35, 30, 35));
        bottomPanel.add(createButton);
        bottomPanel.add(cancelBtn);

        registerDialog.add(panel, BorderLayout.CENTER);
        registerDialog.add(bottomPanel, BorderLayout.SOUTH);
        registerDialog.setVisible(true);
    }
    
    // Save users to file
    private void saveUsers() {
        try {
            // Create directory if it doesn't exist
            File dir = new File("academic_planner_data");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
                oos.writeObject(users);
                System.out.println("Users saved successfully.");
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Load users from file
    @SuppressWarnings("unchecked")
    private void loadUsers() {
        File file = new File(USER_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (HashMap<String, String>) ois.readObject();
                System.out.println("Users loaded successfully. Found " + users.size() + " users.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading users: " + e.getMessage());
                createDefaultUsers();
            }
        } else {
            createDefaultUsers();
        }
    }
    
    private void createDefaultUsers() {
        users = new HashMap<>();
        users.put("student@example.com", "password123");
        users.put("admin@example.com", "admin123");
        System.out.println("Created default users.");
    }
}