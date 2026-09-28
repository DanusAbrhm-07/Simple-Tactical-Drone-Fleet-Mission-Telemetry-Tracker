package jdb;

import java.awt.Color;
import java.sql.*;
import javax.swing.*;

public class LoginWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "dansql7"; 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
    }

    public LoginWindow() {
        setTitle("Drone Management - Authentication");
        setSize(380, 320);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel roleLabel = new JLabel("Select Role:"); roleLabel.setBounds(40, 30, 80, 25); add(roleLabel);
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Analyst", "Technician", "Commander"}); 
        roleBox.setBounds(130, 30, 160, 25); add(roleBox);

        JLabel userLabel = new JLabel("Username:"); userLabel.setBounds(40, 70, 80, 25); add(userLabel);
        JTextField userText = new JTextField(); userText.setBounds(130, 70, 160, 25); add(userText);

        JLabel passLabel = new JLabel("Password:"); passLabel.setBounds(40, 110, 80, 25); add(passLabel);
        JPasswordField passText = new JPasswordField(); passText.setBounds(130, 110, 160, 25); add(passText);

        JButton loginBtn = new JButton("Authenticate"); loginBtn.setBounds(130, 160, 160, 30); add(loginBtn);
        JButton adminBtn = new JButton("Manage Users (Admin)"); adminBtn.setBounds(130, 210, 160, 30); 
        adminBtn.setBackground(new Color(255, 200, 200)); add(adminBtn);

        loginBtn.addActionListener(e -> {
            String role = roleBox.getSelectedItem().toString();
            String user = userText.getText();
            String pass = new String(passText.getPassword());

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM USERS WHERE USERNAME = ? AND PASS_WORD = ? AND USER_ROLE = ?")) {
                pstmt.setString(1, user); pstmt.setString(2, pass); pstmt.setString(3, role);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    dispose();
                    if (role.equals("Commander")) new CommanderDashboard(user, role).setVisible(true);
                    else if (role.equals("Technician")) new TechnicianDashboard(user, role).setVisible(true);
                    else new AnalystDashboard(user, role).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Access Denied: Invalid credentials.");
                }
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Database Error:\n" + ex.getMessage()); }
        });

        adminBtn.addActionListener(e -> {
            JPasswordField pf = new JPasswordField();
            if (JOptionPane.showConfirmDialog(this, pf, "Enter Master Admin Password:", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (new String(pf.getPassword()).equals("system_admin")) {
                    dispose(); new AdminWindow().setVisible(true);
                } else JOptionPane.showMessageDialog(this, "Authentication Failed.");
            }
        });
    }
}