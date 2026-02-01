package dao;

import database.DatabaseConnection;
import models.Mahasiswa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class MahasiswaDAO {

    // =========================
    // Helper Mapper
    // =========================
    private Mahasiswa mapResultSet(ResultSet rs) throws SQLException {
        Mahasiswa mhs = new Mahasiswa();
        mhs.setNim(rs.getString("nim"));
        mhs.setNama(rs.getString("nama"));
        mhs.setJurusan(rs.getString("jurusan"));
        mhs.setUserId(rs.getInt("id_user"));
        return mhs;
    }

    // =========================
    // GET ALL MAHASISWA
    // =========================
    public List<Mahasiswa> getAllMahasiswa() {
        List<Mahasiswa> list = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // =========================
    // ADD MAHASISWA + USER
    // =========================
    public boolean addMahasiswa(Mahasiswa mhs) {
        String sqlMahasiswa = "INSERT INTO mahasiswa (nim, nama, jurusan) VALUES (?, ?, ?)";
        String sqlUser = "INSERT INTO users (username, password, role) VALUES (?, ?, 'mahasiswa')";
        String sqlUpdate = "UPDATE mahasiswa SET id_user = (SELECT id FROM users WHERE username=?) WHERE nim=?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement p1 = conn.prepareStatement(sqlMahasiswa);
                 PreparedStatement p2 = conn.prepareStatement(sqlUser);
                 PreparedStatement p3 = conn.prepareStatement(sqlUpdate)) {

                p1.setString(1, mhs.getNim());
                p1.setString(2, mhs.getNama());
                p1.setString(3, mhs.getJurusan());
                p1.executeUpdate();

                p2.setString(1, mhs.getNim());
                p2.setString(2, mhs.getNim());
                p2.executeUpdate();

                p3.setString(1, mhs.getNim());
                p3.setString(2, mhs.getNim());
                p3.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // DELETE MAHASISWA + USER
    // =========================
    public boolean deleteMahasiswa(String nim) {
        String sqlUserId = "SELECT id_user FROM mahasiswa WHERE nim=?";
        String sqlDeleteMhs = "DELETE FROM mahasiswa WHERE nim=?";
        String sqlDeleteUser = "DELETE FROM users WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            int userId = -1;
            try (PreparedStatement p = conn.prepareStatement(sqlUserId)) {
                p.setString(1, nim);
                ResultSet rs = p.executeQuery();
                if (rs.next()) userId = rs.getInt(1);
            }

            try (PreparedStatement p = conn.prepareStatement(sqlDeleteMhs)) {
                p.setString(1, nim);
                p.executeUpdate();
            }

            if (userId != -1) {
                try (PreparedStatement p = conn.prepareStatement(sqlDeleteUser)) {
                    p.setInt(1, userId);
                    p.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // GET MAHASISWA BY NIM
    // =========================
    public Mahasiswa getMahasiswaByNim(String nim) {
        String sql = "SELECT * FROM mahasiswa WHERE nim=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nim);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // =========================
    // UPDATE MAHASISWA + USER
    // =========================
    public boolean updateMahasiswa(Mahasiswa mhs) {
        String sqlMhs = "UPDATE mahasiswa SET nama=?, jurusan=? WHERE nim=?";
        String sqlUser = "UPDATE users SET username=?, password=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement p1 = conn.prepareStatement(sqlMhs);
                 PreparedStatement p2 = conn.prepareStatement(sqlUser)) {

                p1.setString(1, mhs.getNama());
                p1.setString(2, mhs.getJurusan());
                p1.setString(3, mhs.getNim());
                p1.executeUpdate();

                p2.setString(1, mhs.getNim());
                p2.setString(2, mhs.getNim());
                p2.setInt(3, mhs.getUserId());
                p2.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
