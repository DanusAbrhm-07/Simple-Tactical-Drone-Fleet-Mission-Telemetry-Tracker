package jdb;

import java.awt.Color;
import java.awt.Font;
import java.sql.*;
import javax.swing.*;
import net.proteanit.sql.DbUtils;

public class CommanderDashboard extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "dansql7"; 

    private String sessionUser;
    private JTable table;
    
    private JComboBox<Integer> droneDeployBox, zoneDeployBox, recallBox;
    private JTextField deployQtyField;
    
    private JTextField manageZoneIdField, latField, lonField;
    private JComboBox<String> latDirBox, lonDirBox, riskBox;
    private JLabel lblStandby, lblAirborne, lblMaint;

    public CommanderDashboard(String loggedInUser, String loggedInRole) {
        this.sessionUser = loggedInUser;
        setTitle("Tactical Command OS - User: " + loggedInUser + " (" + loggedInRole + ")");
        setSize(1250, 680); setLayout(null); setLocationRelativeTo(null); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblHeader1 = new JLabel("1. Mission Deployment"); lblHeader1.setFont(new Font("Tahoma", Font.BOLD, 14)); lblHeader1.setBounds(20, 15, 200, 25); add(lblHeader1);
        JLabel lblDrone = new JLabel("Fleet (Standby):"); lblDrone.setBounds(20, 45, 100, 25); add(lblDrone);
        droneDeployBox = new JComboBox<>(); droneDeployBox.setBounds(120, 45, 120, 25); add(droneDeployBox);
        JLabel lblZone = new JLabel("Target Zone:"); lblZone.setBounds(20, 75, 100, 25); add(lblZone);
        zoneDeployBox = new JComboBox<>(); zoneDeployBox.setBounds(120, 75, 120, 25); add(zoneDeployBox);
        JLabel lblQty = new JLabel("Deploy Qty:"); lblQty.setBounds(20, 105, 100, 25); add(lblQty);
        deployQtyField = new JTextField(); deployQtyField.setBounds(120, 105, 120, 25); add(deployQtyField);
        JButton deployBtn = new JButton("Launch Swarm"); deployBtn.setBounds(120, 140, 120, 30); add(deployBtn);

        JLabel lblHeaderR = new JLabel("2. Mission Recall (RTB)"); lblHeaderR.setFont(new Font("Tahoma", Font.BOLD, 14)); lblHeaderR.setBounds(20, 185, 200, 25); add(lblHeaderR);
        JLabel lblAir = new JLabel("Active Mission:"); lblAir.setBounds(20, 215, 100, 25); add(lblAir);
        recallBox = new JComboBox<>(); recallBox.setBounds(120, 215, 120, 25); add(recallBox);
        JButton recallBtn = new JButton("Recall to Base"); recallBtn.setBounds(120, 250, 120, 30); recallBtn.setBackground(new Color(255, 200, 150)); add(recallBtn);

        JSeparator sep1 = new JSeparator(); sep1.setBounds(20, 295, 220, 10); add(sep1);

        JLabel lblHeaderC = new JLabel("Total Swarm Inventory (Live)"); lblHeaderC.setFont(new Font("Tahoma", Font.BOLD, 12)); lblHeaderC.setBounds(20, 305, 200, 25); add(lblHeaderC);
        lblStandby = new JLabel("Total Ready (Standby): 0"); lblStandby.setBounds(20, 330, 200, 25); add(lblStandby);
        lblAirborne = new JLabel("Total Deployed (Airborne): 0"); lblAirborne.setBounds(20, 350, 200, 25); add(lblAirborne);
        lblMaint = new JLabel("Total In Maintenance: 0"); lblMaint.setBounds(20, 370, 200, 25); add(lblMaint);

        JSeparator sep2 = new JSeparator(); sep2.setBounds(20, 405, 220, 10); add(sep2);

        JButton viewDronesBtn = new JButton("View Fleet Grid"); viewDronesBtn.setBounds(20, 420, 220, 30); add(viewDronesBtn);
        JButton viewZonesBtn = new JButton("View Target Zones"); viewZonesBtn.setBounds(20, 460, 220, 30); add(viewZonesBtn);
        JButton viewMissionsBtn = new JButton("View Active Flight Missions"); viewMissionsBtn.setBounds(20, 500, 220, 30); add(viewMissionsBtn);
        JButton viewAuditBtn = new JButton("View Security Audit Log"); viewAuditBtn.setBounds(20, 540, 220, 30); add(viewAuditBtn);
        JButton logoutBtn = new JButton("Logout"); logoutBtn.setBounds(20, 580, 220, 30); logoutBtn.setBackground(new Color(255, 150, 150)); add(logoutBtn);

        int col2X = 270; 
        JLabel lblHeader3 = new JLabel("Target Zone Management"); lblHeader3.setFont(new Font("Tahoma", Font.BOLD, 14)); lblHeader3.setBounds(col2X, 15, 200, 25); add(lblHeader3);
        JLabel lblZId = new JLabel("Zone ID:"); lblZId.setBounds(col2X, 50, 80, 25); add(lblZId);
        manageZoneIdField = new JTextField(); manageZoneIdField.setBounds(col2X + 80, 50, 160, 25); add(manageZoneIdField);
        JLabel lblLat = new JLabel("Latitude:"); lblLat.setBounds(col2X, 80, 80, 25); add(lblLat);
        latField = new JTextField(); latField.setBounds(col2X + 80, 80, 100, 25); add(latField);
        latDirBox = new JComboBox<>(new String[]{"N", "S"}); latDirBox.setBounds(col2X + 190, 80, 50, 25); add(latDirBox);
        JLabel lblLon = new JLabel("Longitude:"); lblLon.setBounds(col2X, 110, 80, 25); add(lblLon);
        lonField = new JTextField(); lonField.setBounds(col2X + 80, 110, 100, 25); add(lonField);
        lonDirBox = new JComboBox<>(new String[]{"E", "W"}); lonDirBox.setBounds(col2X + 190, 110, 50, 25); add(lonDirBox);
        JLabel lblRisk = new JLabel("Risk Lvl:"); lblRisk.setBounds(col2X, 140, 80, 25); add(lblRisk);
        riskBox = new JComboBox<>(new String[]{"--- No Change ---", "Low", "Medium", "High", "Critical"}); riskBox.setBounds(col2X + 80, 140, 160, 25); add(riskBox);
        
        JButton addZoneBtn = new JButton("Add"); addZoneBtn.setBounds(col2X, 175, 75, 30); add(addZoneBtn);
        JButton updateZoneBtn = new JButton("Update"); updateZoneBtn.setBounds(col2X + 80, 175, 80, 30); add(updateZoneBtn);
        JButton deleteZoneBtn = new JButton("Delete"); deleteZoneBtn.setBounds(col2X + 165, 175, 75, 30); add(deleteZoneBtn);

        JScrollPane scrollPane = new JScrollPane(); scrollPane.setBounds(540, 20, 670, 590); add(scrollPane);
        table = new JTable(); table.setFillsViewportHeight(true); table.setRowHeight(25); scrollPane.setViewportView(table);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0 && table.getColumnName(0).equalsIgnoreCase("ZONE_ID")) {
                    manageZoneIdField.setText(table.getValueAt(row, 0).toString());
                }
            }
        });

        updateFleetMetrics(); refreshDynamicDropdowns(); loadTableData("SELECT * FROM DRONE_ASSETS");

        viewDronesBtn.addActionListener(e -> loadTableData("SELECT * FROM DRONE_ASSETS"));
        viewZonesBtn.addActionListener(e -> loadTableData("SELECT * FROM RECON_ZONES"));
        viewMissionsBtn.addActionListener(e -> loadTableData("SELECT * FROM FLIGHT_MISSIONS"));
        viewAuditBtn.addActionListener(e -> loadTableData("SELECT * FROM AUDIT_LOG ORDER BY LOG_TIMESTAMP DESC"));
        logoutBtn.addActionListener(e -> { dispose(); new LoginWindow().setVisible(true); });

        deployBtn.addActionListener(e -> deploySwarmDrone()); recallBtn.addActionListener(e -> recallMission());
        addZoneBtn.addActionListener(e -> addZone()); updateZoneBtn.addActionListener(e -> updateZone()); deleteZoneBtn.addActionListener(e -> deleteZone());
    }

    private void updateFleetMetrics() {
        String sql = "SELECT SUM(QTY_STANDBY), SUM(QTY_AIRBORNE), SUM(QTY_MAINT) FROM DRONE_ASSETS";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) { lblStandby.setText("Total Ready (Standby): " + rs.getInt(1)); lblAirborne.setText("Total Deployed (Airborne): " + rs.getInt(2)); lblMaint.setText("Total In Maintenance: " + rs.getInt(3)); }
        } catch (SQLException ex) {}
    }

    private void refreshDynamicDropdowns() {
        droneDeployBox.removeAllItems(); recallBox.removeAllItems(); zoneDeployBox.removeAllItems();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); Statement stmt = conn.createStatement()) {
            ResultSet rsSt = stmt.executeQuery("SELECT FLEET_ID FROM DRONE_ASSETS WHERE QTY_STANDBY > 0"); while (rsSt.next()) droneDeployBox.addItem(rsSt.getInt(1));
            ResultSet rsAb = stmt.executeQuery("SELECT MISSION_ID FROM FLIGHT_MISSIONS"); while (rsAb.next()) recallBox.addItem(rsAb.getInt(1));
            ResultSet rsZ = stmt.executeQuery("SELECT ZONE_ID FROM RECON_ZONES"); while (rsZ.next()) zoneDeployBox.addItem(rsZ.getInt(1));
        } catch (SQLException ex) {}
    }

    private void loadTableData(String sql) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            table.setModel(DbUtils.resultSetToTableModel(rs));
            TableUtils.autoResizeColumns(table); // <-- DRY Principle Applied!
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Load Error: " + ex.getMessage()); }
    }

    private void deploySwarmDrone() {
        if (droneDeployBox.getSelectedItem() == null || zoneDeployBox.getSelectedItem() == null || deployQtyField.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Ensure Fleet, Zone, and Quantity are provided."); return; }
        int selectedFleet = (Integer) droneDeployBox.getSelectedItem(); int selectedZone = (Integer) zoneDeployBox.getSelectedItem(); int qtyToDeploy;
        try { qtyToDeploy = Integer.parseInt(deployQtyField.getText()); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Quantity must be a valid number."); return; }
        if (qtyToDeploy <= 0) { JOptionPane.showMessageDialog(this, "Deployment quantity must be greater than zero."); return; }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int availableStandby = 0;
            try (PreparedStatement checkStmt = conn.prepareStatement("SELECT QTY_STANDBY FROM DRONE_ASSETS WHERE FLEET_ID = ?")) {
                checkStmt.setInt(1, selectedFleet); ResultSet rs = checkStmt.executeQuery(); if (rs.next()) availableStandby = rs.getInt(1);
            }
            if (qtyToDeploy > availableStandby) { JOptionPane.showMessageDialog(this, "Deployment Failed: Only " + availableStandby + " drones available in Standby for this fleet."); return; }
            
            // --- ENTERPRISE TRANSACTION BLOCK START ---
            conn.setAutoCommit(false); 
            try {
                try (PreparedStatement pstmt1 = conn.prepareStatement("INSERT INTO FLIGHT_MISSIONS (MISSION_ID, FLEET_ID, ZONE_ID, QTY_DEPLOYED, DEPLOYED_BY, LAUNCH_TIME) VALUES (MISSION_SEQ.NEXTVAL, ?, ?, ?, ?, SYSTIMESTAMP)")) {
                    pstmt1.setInt(1, selectedFleet); pstmt1.setInt(2, selectedZone); pstmt1.setInt(3, qtyToDeploy); pstmt1.setString(4, sessionUser); pstmt1.executeUpdate();
                }
                try (PreparedStatement pstmt2 = conn.prepareStatement("UPDATE DRONE_ASSETS SET QTY_STANDBY = QTY_STANDBY - ?, QTY_AIRBORNE = QTY_AIRBORNE + ? WHERE FLEET_ID = ?")) {
                    pstmt2.setInt(1, qtyToDeploy); pstmt2.setInt(2, qtyToDeploy); pstmt2.setInt(3, selectedFleet); pstmt2.executeUpdate();
                }
                try (PreparedStatement auditStmt = conn.prepareStatement("INSERT INTO AUDIT_LOG (ACTION_TYPE, ACTION_DETAILS) VALUES (?, ?)")) {
                    auditStmt.setString(1, "TACTICAL_LAUNCH"); auditStmt.setString(2, "Cmdr " + sessionUser + " deployed " + qtyToDeploy + " units of Fleet " + selectedFleet + " to Zone " + selectedZone); auditStmt.executeUpdate();
                }
                conn.commit(); // Save all changes if successful
            } catch (SQLException ex) {
                conn.rollback(); // Undo everything if any query fails
                throw ex; // Pass to the outer catch block to display the error
            } finally {
                conn.setAutoCommit(true);
            }
            // --- ENTERPRISE TRANSACTION BLOCK END ---

            deployQtyField.setText(""); updateFleetMetrics(); refreshDynamicDropdowns(); loadTableData("SELECT * FROM DRONE_ASSETS"); JOptionPane.showMessageDialog(this, "Launch Authorized! " + qtyToDeploy + " assets deployed.");
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Transaction Error (Rolled Back): \n" + ex.getMessage()); }
    }

    private void recallMission() {
        if (recallBox.getSelectedItem() == null) { JOptionPane.showMessageDialog(this, "No active missions to recall."); return; }
        int missionId = (Integer) recallBox.getSelectedItem();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int fleetId = 0, deployedQty = 0;
            try (PreparedStatement getMission = conn.prepareStatement("SELECT FLEET_ID, QTY_DEPLOYED FROM FLIGHT_MISSIONS WHERE MISSION_ID = ?")) {
                getMission.setInt(1, missionId); ResultSet rs = getMission.executeQuery(); if (rs.next()) { fleetId = rs.getInt(1); deployedQty = rs.getInt(2); }
            }
            
            // --- ENTERPRISE TRANSACTION BLOCK START ---
            conn.setAutoCommit(false); 
            try {
                try (PreparedStatement updateAssets = conn.prepareStatement("UPDATE DRONE_ASSETS SET QTY_AIRBORNE = QTY_AIRBORNE - ?, QTY_MAINT = QTY_MAINT + ? WHERE FLEET_ID = ?")) {
                    updateAssets.setInt(1, deployedQty); updateAssets.setInt(2, deployedQty); updateAssets.setInt(3, fleetId); updateAssets.executeUpdate();
                }
                try (PreparedStatement delMission = conn.prepareStatement("DELETE FROM FLIGHT_MISSIONS WHERE MISSION_ID = ?")) {
                    delMission.setInt(1, missionId); delMission.executeUpdate();
                }
                try (PreparedStatement auditStmt = conn.prepareStatement("INSERT INTO AUDIT_LOG (ACTION_TYPE, ACTION_DETAILS) VALUES (?, ?)")) {
                    auditStmt.setString(1, "TACTICAL_RECALL"); auditStmt.setString(2, "Cmdr " + sessionUser + " recalled Mission " + missionId + " (" + deployedQty + " units RTB)"); auditStmt.executeUpdate();
                }
                conn.commit(); // Save all changes if successful
            } catch (SQLException ex) {
                conn.rollback(); // Undo everything if any query fails
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
            // --- ENTERPRISE TRANSACTION BLOCK END ---

            updateFleetMetrics(); refreshDynamicDropdowns(); loadTableData("SELECT * FROM DRONE_ASSETS"); JOptionPane.showMessageDialog(this, "Mission " + missionId + " Concluded.");
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Transaction Error (Rolled Back): \n" + ex.getMessage()); }
    }

    // --- ZONE CRUD METHODS ---
    private void addZone() {
        if (manageZoneIdField.getText().isEmpty() || latField.getText().isEmpty() || lonField.getText().isEmpty()) return;
        String formattedCoords = latField.getText() + " " + latDirBox.getSelectedItem() + ", " + lonField.getText() + " " + lonDirBox.getSelectedItem();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO RECON_ZONES (ZONE_ID, COORDINATES, RISK_LEVEL) VALUES (?, ?, ?)")) {
            pstmt.setInt(1, Integer.parseInt(manageZoneIdField.getText())); pstmt.setString(2, formattedCoords); pstmt.setString(3, riskBox.getSelectedIndex() == 0 ? "Medium" : riskBox.getSelectedItem().toString());
            pstmt.executeUpdate(); refreshDynamicDropdowns(); loadTableData("SELECT * FROM RECON_ZONES"); manageZoneIdField.setText(""); latField.setText(""); lonField.setText(""); riskBox.setSelectedIndex(0);
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Add Zone Failed: \n" + ex.getMessage()); }
    }

    private void updateZone() {
        if (manageZoneIdField.getText().isEmpty()) return;
        if ((!latField.getText().isEmpty() && lonField.getText().isEmpty()) || (latField.getText().isEmpty() && !lonField.getText().isEmpty())) {
            JOptionPane.showMessageDialog(this, "Validation Error: Both Latitude and Longitude must be provided together, or leave both blank to keep existing coordinates."); return;
        }
        
        String formattedCoords = (!latField.getText().isEmpty() && !lonField.getText().isEmpty()) ? latField.getText() + " " + latDirBox.getSelectedItem() + ", " + lonField.getText() + " " + lonDirBox.getSelectedItem() : null;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement pstmt = conn.prepareStatement("UPDATE RECON_ZONES SET COORDINATES = NVL(?, COORDINATES), RISK_LEVEL = NVL(?, RISK_LEVEL) WHERE ZONE_ID = ?")) {
            if (formattedCoords == null) pstmt.setNull(1, java.sql.Types.VARCHAR); else pstmt.setString(1, formattedCoords);
            if (riskBox.getSelectedIndex() == 0) pstmt.setNull(2, java.sql.Types.VARCHAR); else pstmt.setString(2, riskBox.getSelectedItem().toString());
            pstmt.setInt(3, Integer.parseInt(manageZoneIdField.getText().trim()));
            if (pstmt.executeUpdate() > 0) { loadTableData("SELECT * FROM RECON_ZONES"); manageZoneIdField.setText(""); latField.setText(""); lonField.setText(""); riskBox.setSelectedIndex(0); }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Update Zone Failed: \n" + ex.getMessage()); }
    }

    private void deleteZone() {
        if (manageZoneIdField.getText().isEmpty()) return;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM RECON_ZONES WHERE ZONE_ID = ?")) {
            pstmt.setInt(1, Integer.parseInt(manageZoneIdField.getText()));
            if (pstmt.executeUpdate() > 0) { refreshDynamicDropdowns(); loadTableData("SELECT * FROM RECON_ZONES"); manageZoneIdField.setText(""); }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Delete Zone Failed. Ensure there are no active missions targeted at this zone.\n" + ex.getMessage()); }
    }
}