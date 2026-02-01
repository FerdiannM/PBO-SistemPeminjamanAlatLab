/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

/**
 *
 * @author USER
 */

import java.sql.Connection;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class DatabaseInitializer {
    public static void checkAndCreateTables(){
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String checkUsers = "SELECT COUNT(*) FROM information_schema.tables " +
                               "WHERE table_schema = 'lab_db' AND table_name = 'users'";
            
            var rs = stmt.executeQuery(checkUsers);
            rs.next();
            
            if (rs.getInt(1) == 0) {
                createTables(stmt);
                insertDefaultData(stmt);
                JOptionPane.showMessageDialog(null, "Database berhasil diinisialisasi");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inisialisasi database " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
        private static void createTables(Statement stmt) throws Exception {
        // Buat tabel users
        stmt.execute("CREATE TABLE users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "username VARCHAR(50) UNIQUE NOT NULL," +
                    "password VARCHAR(100) NOT NULL," +
                    "role ENUM('admin', 'mahasiswa') NOT NULL DEFAULT 'mahasiswa')");
        
        // Buat tabel mahasiswa
        stmt.execute("CREATE TABLE mahasiswa (" +
                    "nim VARCHAR(15) UNIQUE NOT NULL," +
                    "nama VARCHAR(100) NOT NULL," +
                    "jurusan VARCHAR(50)," +
                    "id_user INT," +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");
        
        // Buat tabel alat
        stmt.execute("CREATE TABLE alat (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "kode VARCHAR(20) UNIQUE NOT NULL," +
                    "nama VARCHAR(100) NOT NULL," +
                    "kategori VARCHAR(50)," +
                    "stok INT DEFAULT 1," +
                    "tersedia INT DEFAULT 1," +
                    "kondisi VARCHAR(20) DEFAULT 'Baik')");
        
        // Buat tabel transaksi
        stmt.execute("CREATE TABLE transaksi (" +
                    "id_transaksi INT PRIMARY KEY AUTO_INCREMENT," +
                    "alat_code INT," +
                    "mahasiswa_nim INT," +
                    "tanggal_pinjam DATE," +
                    "tanggal_kembali DATE," +
                    "batas_kembali DATE," +
                    "status VARCHAR(20) DEFAULT 'Dipinjam'," +
                    "denda INT DEFAULT 0," +
                    "FOREIGN KEY (alat_id) REFERENCES alat(id)," +
                    "FOREIGN KEY (mahasiswa_id) REFERENCES mahasiswa(nim))");
    }
    
    private static void insertDefaultData(Statement stmt) throws Exception {
        // Insert default users
        stmt.execute("INSERT INTO users (username, password, role) VALUES " +
                    "('admin', 'admin123', 'admin')," +
                    "('20230001', 'mhs123', 'mahasiswa')," +
                    "('20230002', 'mhs123', 'mahasiswa')");
        
        // Insert default mahasiswa
        stmt.execute("INSERT INTO mahasiswa (nim, nama, jurusan, id_user) VALUES " +
                    "('20230001', 'Budi Santoso', 'Informatika', 2023, 2)," +
                    "('20230002', 'Siti Aminah', 'Sistem Informasi', 2023, 3)");
        
        // Insert default alat
        stmt.execute("INSERT INTO alat (kode, nama, kategori, stok, tersedia,) VALUES " +
                    "('M001', 'Mouse Gaming', 'Peripheral', 10, 10,)," +
                    "('K001', 'Keyboard Mechanical', 'Peripheral', 5, 5)," +
                    "('H001', 'Headset USB', 'Audio', 8, 8)," +
                    "('L001', 'Laptop Asus', 'Komputer', 2, 2)");
    }
}

