# Tactical Command OS: Drone Fleet Telemetry Tracker

An enterprise-grade, 2-tier client-server desktop application designed to manage tactical drone deployments, hardware inventory, and geographic reconnaissance zones. Built with a Java Swing frontend and an Oracle Relational Database backend.

## 🏗️ Core Architecture & Enterprise Features
* **Role-Based Access Control (RBAC):** Strict separation of duties. Dashboards are dynamically routed based on four operational roles: Admin, Technician, Commander, and Analyst.
* **ACID-Compliant Transactions:** Mission deployments utilize strict SQL transaction blocks (`conn.setAutoCommit(false)`). If a database failure occurs mid-deployment, a `rollback()` executes to prevent corrupted inventory states.
* **Auto-Balancing Math Engine:** The logistics dashboard enforces mathematical conservation (`Total = Standby + Airborne + Maintenance`). Altering one status automatically balances the others.
* **Immutable Security Auditing:** Critical logistical and tactical actions trigger automated `INSERT` commands into an `AUDIT_LOG` table, stamped with the Oracle server's exact `SYSTIMESTAMP`.
* **Zero SQL Injection Vulnerability:** 100% of database queries utilize `PreparedStatement` bind variables (`?`), strictly neutralizing SQL injection vectors.
* **DRY UI Utility:** A centralized `TableUtils` class dynamically measures string pixel widths and auto-resizes table columns universally across all Swing modules.

## ⚙️ Tech Stack
* **Frontend:** Java (Swing GUI, AWT)
* **Backend:** Oracle Database 11g/19c (JDBC Driver)
* **Libraries:** `rs2xml.jar` (DbUtils for dynamic ResultSet mapping)

## 🚀 Setup & Installation
1. **Database Initialization:** Open your Oracle SQL worksheet (SQL*Plus or SQL Developer) and execute the `database_setup.sql` script located in the root folder. This will generate the necessary tables, sequences, foreign keys, and default users.
2. **Import Project:** Import this repository into Eclipse as a Java Project. 
3. **Configure Build Path:** Add your Oracle JDBC driver (e.g., `ojdbc8.jar`) and `rs2xml.jar` to the project's Build Path.
4. **Database Connection:** Update the `DB_URL`, `DB_USER`, and `DB_PASSWORD` variables at the top of the dashboard classes to match your local Oracle instance credentials.
5. **Launch:** Run `LoginWindow.java` to start the application.

### Default Login Credentials
* **Admin:** `admin` / `admin123`
* **Technician:** `tech1` / `tech123`
* **Commander:** `cmdr_alpha` / `cmdr123`
* **Analyst:** `intel_ops` / `intel123`
