package jdb;

import java.awt.Color;
import java.awt.Font;
import java.sql.*;
import javax.swing.*;
import net.proteanit.sql.DbUtils;

public class AnalystDashboard extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "dansql7";
    
    private JTable table;

    public AnalystDashboard(String loggedInUser, String loggedInRole) {
        setTitle("Tactical Command OS - User: " + loggedInUser + " (" + loggedInRole + ")");
        setSize(900, 480);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblHeader = new JLabel("Analyst Data Terminal"); lblHeader.setFont(new Font("Tahoma", Font.BOLD, 16)); lblHeader.setBounds(20, 20, 200, 25); add(lblHeader);

        JButton vDronesBtn = new JButton("View Active Drones"); vDronesBtn.setBounds(20, 70, 220, 35); add(vDronesBtn);
        JButton vZonesBtn = new JButton("View Target Zones"); vZonesBtn.setBounds(20, 120, 220, 35); add(vZonesBtn);
        JButton vMissionsBtn = new JButton("View Active Flight Missions"); vMissionsBtn.setBounds(20, 170, 220, 35); add(vMissionsBtn);
        
        JButton logoutBtn = new JButton("Logout"); logoutBtn.setBounds(20, 380, 220, 35); logoutBtn.setBackground(new Color(255, 150, 150)); add(logoutBtn);

        JScrollPane scrollPane = new JScrollPane(); scrollPane.setBounds(260, 20, 600, 395); add(scrollPane);
        table = new JTable(); table.setFillsViewportHeight(true); table.setRowHeight(25); scrollPane.setViewportView(table);

        vDronesBtn.addActionListener(e -> loadTableData("SELECT * FROM DRONE_ASSETS"));
        vZonesBtn.addActionListener(e -> loadTableData("SELECT * FROM RECON_ZONES"));
        vMissionsBtn.addActionListener(e -> loadTableData("SELECT * FROM FLIGHT_MISSIONS"));
        logoutBtn.addActionListener(e -> { dispose(); new LoginWindow().setVisible(true); });

        loadTableData("SELECT * FROM DRONE_ASSETS");
    }

    private void loadTableData(String sql) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Load Error: " + ex.getMessage()); }
    }
}
