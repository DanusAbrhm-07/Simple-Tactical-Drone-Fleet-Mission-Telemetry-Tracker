-- =========================================================
-- TACTICAL COMMAND OS - ORACLE DATABASE SETUP SCRIPT
-- =========================================================

-- =========================================================
-- SECTION 1: CREATE TABLES
-- =========================================================

-- 1. Authentication & Roles
CREATE TABLE USERS (
    USERNAME VARCHAR2(50) PRIMARY KEY,
    PASSWORD VARCHAR2(100) NOT NULL,
    ROLE VARCHAR2(50) NOT NULL
);

-- 2. Hardware Inventory
CREATE TABLE DRONE_ASSETS (
    FLEET_ID NUMBER PRIMARY KEY,
    MODEL VARCHAR2(100) NOT NULL,
    DRONE_TYPE VARCHAR2(100) NOT NULL,
    QTY_STANDBY NUMBER DEFAULT 0,
    QTY_AIRBORNE NUMBER DEFAULT 0,
    QTY_MAINT NUMBER DEFAULT 0
);

-- 3. Target Destinations
CREATE TABLE RECON_ZONES (
    ZONE_ID NUMBER PRIMARY KEY,
    COORDINATES VARCHAR2(100) NOT NULL,
    RISK_LEVEL VARCHAR2(50) DEFAULT 'Medium'
);

-- 4. Live Mission Board (With Foreign Key Constraints)
-- The foreign keys guarantee a Commander cannot deploy non-existent fleets, 
-- and block the deletion of any Fleet or Zone that is actively deployed.
CREATE TABLE FLIGHT_MISSIONS (
    MISSION_ID NUMBER PRIMARY KEY,
    FLEET_ID NUMBER NOT NULL,
    ZONE_ID NUMBER NOT NULL,
    QTY_DEPLOYED NUMBER NOT NULL,
    DEPLOYED_BY VARCHAR2(50) NOT NULL,
    LAUNCH_TIME TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT FK_MISSION_FLEET FOREIGN KEY (FLEET_ID) REFERENCES DRONE_ASSETS(FLEET_ID),
    CONSTRAINT FK_MISSION_ZONE FOREIGN KEY (ZONE_ID) REFERENCES RECON_ZONES(ZONE_ID)
);

-- 5. Immutable Security Audit Log
-- LOG_ID auto-increments. LOG_TIMESTAMP uses the server clock to prevent spoofing.
CREATE TABLE AUDIT_LOG (
    LOG_ID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ACTION_TYPE VARCHAR2(50) NOT NULL,
    ACTION_DETAILS VARCHAR2(500) NOT NULL,
    LOG_TIMESTAMP TIMESTAMP DEFAULT SYSTIMESTAMP
);

-- =========================================================
-- SECTION 2: CREATE SEQUENCES
-- =========================================================

-- Used by the Java code to auto-generate unique Mission IDs
CREATE SEQUENCE MISSION_SEQ
    START WITH 1000
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- =========================================================
-- SECTION 3: INSERT DEFAULT USERS
-- =========================================================

-- Create base accounts for all four operational roles
INSERT INTO USERS (USERNAME, PASSWORD, ROLE) VALUES ('admin', 'admin123', 'Admin');
INSERT INTO USERS (USERNAME, PASSWORD, ROLE) VALUES ('tech1', 'tech123', 'Technician');
INSERT INTO USERS (USERNAME, PASSWORD, ROLE) VALUES ('cmdr_alpha', 'cmdr123', 'Commander');
INSERT INTO USERS (USERNAME, PASSWORD, ROLE) VALUES ('intel_ops', 'intel123', 'Analyst');

COMMIT;

-- Note: If you ever need to reset the database, run these drop commands in reverse order:
-- DROP SEQUENCE MISSION_SEQ;
-- DROP TABLE FLIGHT_MISSIONS;
-- DROP TABLE DRONE_ASSETS;
-- DROP TABLE RECON_ZONES;
-- DROP TABLE AUDIT_LOG;
-- DROP TABLE USERS;