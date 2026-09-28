package jdb;

import java.awt.Color;
import java.awt.Font;
import java.sql.*;
import javax.swing.*;
import net.proteanit.sql.DbUtils;

public class TechnicianDashboard extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "dansql7"; 

    private String sessionUser;
    private JTable table;
    
    private JTextField searchField;
    private JTextField newFleetIdField, newModelField, newStandbyField, newAirborneField, newMaintField;
    private JComboBox<String> newTypeBox;
    private JTextField updFleetIdField, updModelField, updTotalField, updAirborneField, updMaintField;
    private JComboBox<String> updTypeBox;

    public TechnicianDashboard(String loggedInUser, String loggedInRole) {
        this.sessionUser = loggedInUser;
        setTitle("Tactical Command OS - User: " + loggedInUser + " (" + loggedInRole + ")");
        setSize(1300, 720); getContentPane().setLayout(null); setLocationRelativeTo(null); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblSearchHeader = new JLabel("Fleet ID Search"); lblSearchHeader.setFont(new Font("Tahoma", Font.BOLD, 14)); lblSearchHeader.setBounds(20, 20, 200, 25); getContentPane().add(lblSearchHeader);
        searchField = new JTextField(); searchField.setBounds(20, 55, 210, 25); getContentPane().add(searchField);
        JButton searchBtn = new JButton("Search ID"); searchBtn.setBounds(20, 85, 100, 28); getContentPane().add(searchBtn);
        JButton resetSearchBtn = new JButton("Reset"); resetSearchBtn.setBounds(130, 85, 100, 28); getContentPane().add(resetSearchBtn);

        JButton logoutBtn = new JButton("Logout"); logoutBtn.setBounds(20, 620, 210, 30); logoutBtn.setBackground(new Color(255, 150, 150)); getContentPane().add(logoutBtn);

        int col2X = 245;

        // --- SECTION A: ADD ---
        JLabel lblAddHeader = new JLabel("1. Register New Fleet"); lblAddHeader.setFont(new Font("Tahoma", Font.BOLD, 13)); lblAddHeader.setBounds(col2X, 15, 260, 25); getContentPane().add(lblAddHeader);
        JLabel lblNId = new JLabel("Fleet ID:"); lblNId.setBounds(col2X, 45, 90, 22); getContentPane().add(lblNId);
        newFleetIdField = new JTextField(); newFleetIdField.setBounds(col2X + 90, 45, 160, 22); getContentPane().add(newFleetIdField);
        JLabel lblNModel = new JLabel("Model:"); lblNModel.setBounds(col2X, 70, 90, 22); getContentPane().add(lblNModel);
        newModelField = new JTextField(); newModelField.setBounds(col2X + 90, 70, 160, 22); getContentPane().add(newModelField);
        JLabel lblNType = new JLabel("Drone Type:"); lblNType.setBounds(col2X, 95, 90, 22); getContentPane().add(lblNType);
        newTypeBox = new JComboBox<>(new String[]{"Combat / Strike (UCAV)", "Surveillance (ISR)", "Electronic Warfare (EW)", "Logistics & Cargo", "Loitering Munition"}); 
        newTypeBox.setBounds(col2X + 90, 95, 160, 22); getContentPane().add(newTypeBox);
        JLabel lblNSt = new JLabel("Standby Qty:"); lblNSt.setBounds(col2X, 120, 90, 22); getContentPane().add(lblNSt);
        newStandbyField = new JTextField(); newStandbyField.setBounds(col2X + 90, 120, 160, 22); getContentPane().add(newStandbyField);
        JLabel lblNAb = new JLabel("Airborne Qty:"); lblNAb.setBounds(col2X, 145, 90, 22); getContentPane().add(lblNAb);
        newAirborneField = new JTextField(); newAirborneField.setBounds(col2X + 90, 145, 160, 22); getContentPane().add(newAirborneField);
        JLabel lblNMt = new JLabel("Maint Qty:"); lblNMt.setBounds(col2X, 170, 90, 22); getContentPane().add(lblNMt);
        newMaintField = new JTextField(); newMaintField.setBounds(col2X + 90, 170, 160, 22); getContentPane().add(newMaintField);
        
        JButton addNewFleetBtn = new JButton("Add New Fleet"); addNewFleetBtn.setBounds(col2X + 90, 200, 160, 30); 
        addNewFleetBtn.setBackground(new Color(200, 230, 201)); getContentPane().add(addNewFleetBtn);

        JSeparator sep = new JSeparator(); sep.setBounds(col2X, 240, 250, 10); getContentPane().add(sep);

        // --- SECTION B: UPDATE ---
        JLabel lblUpdHeader = new JLabel("2. Modify Drone Status "); lblUpdHeader.setFont(new Font("Tahoma", Font.BOLD, 13)); lblUpdHeader.setBounds(col2X, 255, 260, 25); getContentPane().add(lblUpdHeader);
        JLabel lblUId = new JLabel("Target ID:"); lblUId.setBounds(col2X, 285, 90, 22); getContentPane().add(lblUId);
        updFleetIdField = new JTextField(); updFleetIdField.setBounds(col2X + 90, 285, 160, 22); getContentPane().add(updFleetIdField);
        JLabel lblUModel = new JLabel("New Model:"); lblUModel.setBounds(col2X, 310, 90, 22); getContentPane().add(lblUModel);
        updModelField = new JTextField(); updModelField.setBounds(col2X + 90, 310, 160, 22); getContentPane().add(updModelField);
        JLabel lblUType = new JLabel("New Type:"); lblUType.setBounds(col2X, 335, 90, 22); getContentPane().add(lblUType);
        updTypeBox = new JComboBox<>(new String[]{"--- No Change ---", "Combat / Strike (UCAV)", "Surveillance (ISR)", "Electronic Warfare (EW)", "Logistics & Cargo", "Loitering Munition"}); 
        updTypeBox.setBounds(col2X + 90, 335, 160, 22); getContentPane().add(updTypeBox);
        
        JLabel lblUTotal = new JLabel("Total Fleet Qty:"); lblUTotal.setBounds(col2X, 360, 90, 22); getContentPane().add(lblUTotal);
        updTotalField = new JTextField(); updTotalField.setBounds(col2X + 90, 360, 160, 22); getContentPane().add(updTotalField);
        JLabel lblUAb = new JLabel("Airborne Qty:"); lblUAb.setBounds(col2X, 385, 90, 22); getContentPane().add(lblUAb);
        updAirborneField = new JTextField(); updAirborneField.setBounds(col2X + 90, 385, 160, 22); getContentPane().add(updAirborneField);
        JLabel lblUMt = new JLabel("Maint Qty:"); lblUMt.setBounds(col2X, 410, 90, 22); getContentPane().add(lblUMt);
        updMaintField = new JTextField(); updMaintField.setBounds(col2X + 90, 410, 160, 22); getContentPane().add(updMaintField);

        JButton updateFleetBtn = new JButton("Update Status"); updateFleetBtn.setBounds(col2X + 45, 465, 115, 30); getContentPane().add(updateFleetBtn);
        JButton deleteFleetBtn = new JButton("Delete"); deleteFleetBtn.setBounds(col2X + 165, 465, 85, 30); 
        deleteFleetBtn.setBackground(new Color(255, 205, 210)); getContentPane().add(deleteFleetBtn);

        JScrollPane scrollPane = new JScrollPane(); scrollPane.setBounds(515, 20, 755, 630); getContentPane().add(scrollPane);
        table = new JTable(); table.setFillsViewportHeight(true); table.setRowHeight(25); scrollPane.setViewportView(table);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0 && table.getColumnName(0).equalsIgnoreCase("FLEET_ID")) {
                    updFleetIdField.setText(table.getValueAt(row, 0).toString());
                    updModelField.setText(table.getValueAt(row, 1).toString());
                    updTypeBox.setSelectedItem(table.getValueAt(row, 2).toString());
                    int st = Integer.parseInt(table.getValueAt(row, 3).toString());
                    int ab = Integer.parseInt(table.getValueAt(row, 4).toString());
                    int mt = Integer.parseInt(table.getValueAt(row, 5).toString());
                    updTotalField.setText(String.valueOf(st + ab + mt));
                    updAirborneField.setText(String.valueOf(ab));
                    updMaintField.setText(String.valueOf(mt));
                }
            }
        });

        loadTableData("SELECT * FROM DRONE_ASSETS");

        searchBtn.addActionListener(e -> searchFleetById(searchField.getText()));
        resetSearchBtn.addActionListener(e -> { searchField.setText(""); loadTableData("SELECT * FROM DRONE_ASSETS"); });
        logoutBtn.addActionListener(e -> { dispose(); new LoginWindow().setVisible(true); });
        addNewFleetBtn.addActionListener(e -> addNewFleet()); updateFleetBtn.addActionListener(e -> updateFleet()); deleteFleetBtn.addActionListener(e -> deleteFleet());
    }

    private void loadTableData(String sql) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            table.setModel(DbUtils.resultSetToTableModel(rs)); 
            TableUtils.autoResizeColumns(table); // <-- DRY Principle Applied!
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Load Error: " + ex.getMessage()); }
    }

    private void searchFleetById(String keyword) {
        if (keyword.trim().isEmpty()) { loadTableData("SELECT * FROM DRONE_ASSETS"); return; }
        try {
            int searchId = Integer.parseInt(keyword.trim());
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM DRONE_ASSETS WHERE FLEET_ID = ?")) {
                pstmt.setInt(1, searchId);
                try (ResultSet rs = pstmt.executeQuery()) { 
                    table.setModel(DbUtils.resultSetToTableModel(rs)); 
                    TableUtils.autoResizeColumns(table); 
                }
            }
        } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Validation Error: Fleet ID search must be a valid number.");
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Search Error: " + ex.getMessage()); }
    }

    private void addNewFleet() {
        if (newFleetIdField.getText().isEmpty() || newModelField.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Fleet ID and Model are required."); return; }
        
        // --- SPECIFIC INPUT VALIDATION START ---
        int fleetId, standby, airborne, maint;
        try {
            fleetId = Integer.parseInt(newFleetIdField.getText().trim());
            standby = newStandbyField.getText().isEmpty() ? 0 : Integer.parseInt(newStandbyField.getText().trim());
            airborne = newAirborneField.getText().isEmpty() ? 0 : Integer.parseInt(newAirborneField.getText().trim());
            maint = newMaintField.getText().isEmpty() ? 0 : Integer.parseInt(newMaintField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Validation Error: ID and Quantities must be valid numbers without letters or spaces.");
            return;
        }
        // --- SPECIFIC INPUT VALIDATION END ---

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO DRONE_ASSETS (FLEET_ID, MODEL, DRONE_TYPE, QTY_STANDBY, QTY_AIRBORNE, QTY_MAINT) VALUES (?, ?, ?, ?, ?, ?)")) {
            pstmt.setInt(1, fleetId); pstmt.setString(2, newModelField.getText().trim()); pstmt.setString(3, newTypeBox.getSelectedItem().toString());
            pstmt.setInt(4, standby); pstmt.setInt(5, airborne); pstmt.setInt(6, maint); pstmt.executeUpdate();

            try (PreparedStatement auditStmt = conn.prepareStatement("INSERT INTO AUDIT_LOG (ACTION_TYPE, ACTION_DETAILS) VALUES (?, ?)")) {
                auditStmt.setString(1, "TECH_REGISTER_FLEET"); auditStmt.setString(2, "Tech " + sessionUser + " registered Fleet " + fleetId); auditStmt.executeUpdate();
            }
            loadTableData("SELECT * FROM DRONE_ASSETS"); clearAddFields(); JOptionPane.showMessageDialog(this, "New Fleet Registered.");
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Database Input Error:\n" + ex.getMessage()); }
    }

    private void updateFleet() {
        if (updFleetIdField.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Target Fleet ID is required."); return; }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int fleetId = Integer.parseInt(updFleetIdField.getText().trim());
            String currModel = "", currType = ""; int currStandby = 0, currAirborne = 0, currMaint = 0;
            
            try (PreparedStatement fetchStmt = conn.prepareStatement("SELECT MODEL, DRONE_TYPE, QTY_STANDBY, QTY_AIRBORNE, QTY_MAINT FROM DRONE_ASSETS WHERE FLEET_ID = ?")) {
                fetchStmt.setInt(1, fleetId); ResultSet rs = fetchStmt.executeQuery();
                if (rs.next()) { currModel = rs.getString(1); currType = rs.getString(2); currStandby = rs.getInt(3); currAirborne = rs.getInt(4); currMaint = rs.getInt(5); } 
                else { JOptionPane.showMessageDialog(this, "Fleet ID not found."); return; }
            }

            int currTotal = currStandby + currAirborne + currMaint;
            
            // --- SPECIFIC INPUT VALIDATION START ---
            int newTotal, newAirborne, newMaint;
            try {
                newTotal = updTotalField.getText().trim().isEmpty() ? currTotal : Integer.parseInt(updTotalField.getText().trim());
                newAirborne = updAirborneField.getText().trim().isEmpty() ? currAirborne : Integer.parseInt(updAirborneField.getText().trim());
                newMaint = updMaintField.getText().trim().isEmpty() ? currMaint : Integer.parseInt(updMaintField.getText().trim());
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Validation Error: Total, Airborne, and Maintenance quantities must be valid numbers.");
                return; 
            }
            // --- SPECIFIC INPUT VALIDATION END ---
            
            int newStandby = newTotal - newAirborne - newMaint;

            if (newStandby < 0) { JOptionPane.showMessageDialog(this, "Math Error: Airborne + Maint exceeds the Total Fleet Size (" + newTotal + ")."); return; }

            String finalModel = updModelField.getText().trim().isEmpty() ? currModel : updModelField.getText().trim();
            String finalType = updTypeBox.getSelectedIndex() == 0 ? currType : updTypeBox.getSelectedItem().toString();

            try (PreparedStatement pstmt = conn.prepareStatement("UPDATE DRONE_ASSETS SET MODEL = ?, DRONE_TYPE = ?, QTY_STANDBY = ?, QTY_AIRBORNE = ?, QTY_MAINT = ? WHERE FLEET_ID = ?")) {
                pstmt.setString(1, finalModel); pstmt.setString(2, finalType); pstmt.setInt(3, newStandby); pstmt.setInt(4, newAirborne); pstmt.setInt(5, newMaint); pstmt.setInt(6, fleetId); pstmt.executeUpdate();
            }

            try (PreparedStatement auditStmt = conn.prepareStatement("INSERT INTO AUDIT_LOG (ACTION_TYPE, ACTION_DETAILS) VALUES (?, ?)")) {
                auditStmt.setString(1, "TECH_UPDATE_INVENTORY"); auditStmt.setString(2, "Tech " + sessionUser + " adjusted Fleet " + fleetId + " (Total: " + newTotal + ", Stby: " + newStandby + ", Ab: " + newAirborne + ", Mt: " + newMaint + ")"); auditStmt.executeUpdate();
            }
            loadTableData("SELECT * FROM DRONE_ASSETS"); clearUpdateFields(); JOptionPane.showMessageDialog(this, "Status Updated Successfully.");
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Database Update Error: \n" + ex.getMessage()); }
    }

    private void deleteFleet() {
        if (updFleetIdField.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Target Fleet ID required."); return; }
        int fleetId;
        try { 
            fleetId = Integer.parseInt(updFleetIdField.getText().trim()); 
        } catch (NumberFormatException e) { 
            JOptionPane.showMessageDialog(this, "Validation Error: Fleet ID must be a number."); return; 
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM DRONE_ASSETS WHERE FLEET_ID = ?")) {
            pstmt.setInt(1, fleetId);
            if (pstmt.executeUpdate() > 0) {
                try (PreparedStatement auditStmt = conn.prepareStatement("INSERT INTO AUDIT_LOG (ACTION_TYPE, ACTION_DETAILS) VALUES (?, ?)")) { auditStmt.setString(1, "TECH_DELETE_FLEET"); auditStmt.setString(2, "Tech " + sessionUser + " decommissioned Fleet " + fleetId); auditStmt.executeUpdate(); }
                loadTableData("SELECT * FROM DRONE_ASSETS"); clearUpdateFields(); JOptionPane.showMessageDialog(this, "Fleet Decommissioned.");
            } else { JOptionPane.showMessageDialog(this, "Deletion Failed."); }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Delete Error: Active missions may rely on this fleet."); }
    }
    
    private void clearAddFields() { newFleetIdField.setText(""); newModelField.setText(""); newStandbyField.setText(""); newAirborneField.setText(""); newMaintField.setText(""); newTypeBox.setSelectedIndex(0); }
    private void clearUpdateFields() { updFleetIdField.setText(""); updModelField.setText(""); updTotalField.setText(""); updAirborneField.setText(""); updMaintField.setText(""); updTypeBox.setSelectedIndex(0); }
}