package jdb;

import java.awt.Color;
import java.sql.*;
import javax.swing.*;

public class AdminWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "dansql7";

    private JComboBox<String> updateTargetUser, deleteTargetUser;

    public AdminWindow() {
        setTitle("Admin Control - Advanced User Management");
        setSize(450, 560);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- PANEL 1: ADD NEW USER ---
        JPanel addPanel = new JPanel(null); addPanel.setBounds(20, 10, 400, 140);
        addPanel.setBorder(BorderFactory.createTitledBorder("1. Add New User")); add(addPanel);
        
        JLabel lblAddU = new JLabel("New Username:"); lblAddU.setBounds(20, 25, 100, 25); addPanel.add(lblAddU);
        JTextField txtAddU = new JTextField(); txtAddU.setBounds(130, 25, 120, 25); addPanel.add(txtAddU);
        JLabel lblAddP = new JLabel("Password:"); lblAddP.setBounds(260, 25, 80, 25); addPanel.add(lblAddP);
        JTextField txtAddP = new JTextField(); txtAddP.setBounds(260, 50, 120, 25); addPanel.add(txtAddP);
        JLabel lblAddR = new JLabel("Assign Role:"); lblAddR.setBounds(20, 60, 100, 25); addPanel.add(lblAddR);
        JComboBox<String> addRoleBox = new JComboBox<>(new String[]{"Analyst", "Technician", "Commander"}); addRoleBox.setBounds(130, 60, 120, 25); addPanel.add(addRoleBox);
        JButton btnAdd = new JButton("Create User"); btnAdd.setBounds(130, 95, 120, 30); addPanel.add(btnAdd);

        // --- PANEL 2: UPDATE EXISTING USER ---
        JPanel updatePanel = new JPanel(null); updatePanel.setBounds(20, 160, 400, 140);
        updatePanel.setBorder(BorderFactory.createTitledBorder("2. Update User Credentials")); add(updatePanel);

        JLabel lblUpdU = new JLabel("Select User:"); lblUpdU.setBounds(20, 25, 100, 25); updatePanel.add(lblUpdU);
        updateTargetUser = new JComboBox<>(); updateTargetUser.setBounds(130, 25, 120, 25); updatePanel.add(updateTargetUser);
        JLabel lblUpdP = new JLabel("New Password:"); lblUpdP.setBounds(260, 25, 100, 25); updatePanel.add(lblUpdP);
        JTextField txtUpdP = new JTextField(); txtUpdP.setBounds(260, 50, 120, 25); updatePanel.add(txtUpdP);
        JLabel lblUpdR = new JLabel("New Role:"); lblUpdR.setBounds(20, 60, 100, 25); updatePanel.add(lblUpdR);
        JComboBox<String> updateRoleBox = new JComboBox<>(new String[]{"--- No Change ---", "Analyst", "Technician", "Commander"}); updateRoleBox.setBounds(130, 60, 120, 25); updatePanel.add(updateRoleBox);
        JButton btnUpdate = new JButton("Update User"); btnUpdate.setBounds(130, 95, 120, 30); updatePanel.add(btnUpdate);

        // --- PANEL 3: DELETE USER ---
        JPanel deletePanel = new JPanel(null); deletePanel.setBounds(20, 310, 400, 130);
        deletePanel.setBorder(BorderFactory.createTitledBorder("3. Revoke Access")); add(deletePanel);

        JLabel lblDelU = new JLabel("Select User:"); lblDelU.setBounds(20, 30, 100, 25); deletePanel.add(lblDelU);
        deleteTargetUser = new JComboBox<>(); deleteTargetUser.setBounds(130, 30, 120, 25); deletePanel.add(deleteTargetUser);
        JLabel lblDelConf = new JLabel("Master Pass:"); lblDelConf.setBounds(260, 30, 100, 25); deletePanel.add(lblDelConf);
        JPasswordField txtDelPass = new JPasswordField(); txtDelPass.setBounds(260, 55, 120, 25); deletePanel.add(txtDelPass);
        JButton btnDelete = new JButton("Delete User"); btnDelete.setBounds(130, 80, 120, 30); btnDelete.setBackground(new Color(255, 150, 150)); deletePanel.add(btnDelete);

        // --- NAVIGATION ---
        JButton btnBack = new JButton("Back to Login"); btnBack.setBounds(150, 465, 150, 30); add(btnBack);

        // --- ACTIONS ---
        refreshUserDropdowns();
        btnBack.addActionListener(e -> { dispose(); new LoginWindow().setVisible(true); });

        btnAdd.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO USERS (USERNAME, PASS_WORD, USER_ROLE) VALUES (?, ?, ?)")) {
                pstmt.setString(1, txtAddU.getText()); pstmt.setString(2, txtAddP.getText()); pstmt.setString(3, addRoleBox.getSelectedItem().toString());
                pstmt.executeUpdate(); JOptionPane.showMessageDialog(this, "User Created."); txtAddU.setText(""); txtAddP.setText(""); refreshUserDropdowns();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        btnUpdate.addActionListener(e -> {
            if (updateTargetUser.getSelectedItem() == null) return;
            String sql = "UPDATE USERS SET PASS_WORD = NVL(?, PASS_WORD), USER_ROLE = NVL(?, USER_ROLE) WHERE USERNAME = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                if (txtUpdP.getText().isEmpty()) pstmt.setNull(1, java.sql.Types.VARCHAR); else pstmt.setString(1, txtUpdP.getText());
                if (updateRoleBox.getSelectedIndex() == 0) pstmt.setNull(2, java.sql.Types.VARCHAR); else pstmt.setString(2, updateRoleBox.getSelectedItem().toString());
                pstmt.setString(3, updateTargetUser.getSelectedItem().toString());
                pstmt.executeUpdate(); JOptionPane.showMessageDialog(this, "User Updated."); txtUpdP.setText(""); updateRoleBox.setSelectedIndex(0);
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        btnDelete.addActionListener(e -> {
            if (deleteTargetUser.getSelectedItem() == null) return;
            if (!new String(txtDelPass.getPassword()).equals("system_admin")) {
                JOptionPane.showMessageDialog(this, "Action Denied: Invalid Master Password."); return;
            }
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM USERS WHERE USERNAME = ?")) {
                pstmt.setString(1, deleteTargetUser.getSelectedItem().toString());
                pstmt.executeUpdate(); JOptionPane.showMessageDialog(this, "User Permanently Deleted."); txtDelPass.setText(""); refreshUserDropdowns();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Delete Error: Ensure user is not tied to mission logs."); }
        });
    }

    private void refreshUserDropdowns() {
        updateTargetUser.removeAllItems(); deleteTargetUser.removeAllItems();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT USERNAME FROM USERS")) {
            while (rs.next()) { updateTargetUser.addItem(rs.getString(1)); deleteTargetUser.addItem(rs.getString(1)); }
        } catch (SQLException ex) {}
    }
}