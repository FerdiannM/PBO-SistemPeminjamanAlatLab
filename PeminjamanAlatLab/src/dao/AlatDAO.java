package dao;

import database.DatabaseConnection;
import models.Alat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class AlatDAO {

    // ========================
    // PRIVATE HELPER METHOD
    // ========================
    private Alat mapResultSetToAlat(ResultSet rs) throws SQLException {
        Alat alat = new Alat();
        alat.setId(rs.getInt("id"));
        alat.setKode(rs.getString("kode"));
        alat.setNama(rs.getString("nama"));
        alat.setKategori(rs.getString("kategori"));
        alat.setStok(rs.getInt("stok"));
        alat.setTersedia(rs.getInt("tersedia"));
        alat.setKondisi(rs.getString("kondisi"));
        return alat;
    }

    // ========================
    // GET ALL ALAT
    // ========================
    public List<Alat> getAllAlat() {
        List<Alat> list = new ArrayList<>();
        String sql = "SELECT * FROM alat ORDER BY kode";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToAlat(rs));
            }

        } catch (SQLException e) {
            showError(e);
        }
        return list;
    }

    // ========================
    // GET ALAT TERSEDIA
    // ========================
    public List<Alat> getAlatTersedia() {
        List<Alat> list = new ArrayList<>();
        String sql = "SELECT * FROM alat WHERE tersedia > 0 AND kondisi = 'Baik'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToAlat(rs));
            }

        } catch (SQLException e) {
            showError(e);
        }
        return list;
    }

    // ========================
    // ADD ALAT
    // ========================
    public boolean addAlat(Alat alat) {
        String sql = """
            INSERT INTO alat (kode, nama, kategori, stok, tersedia, kondisi)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, alat.getKode());
            ps.setString(2, alat.getNama());
            ps.setString(3, alat.getKategori());
            ps.setInt(4, alat.getStok());
            ps.setInt(5, alat.getTersedia());
            ps.setString(6, alat.getKondisi());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(null, "Kode alat sudah terdaftar!");
            } else {
                showError(e);
            }
            return false;
        }
    }

    // ========================
    // UPDATE STOK
    // ========================
    public boolean updateStok(String kode, int perubahan) {
        String sql = "UPDATE alat SET tersedia = tersedia + ? WHERE kode = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, perubahan);
            ps.setString(2, kode);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            showError(e);
            return false;
        }
    }

    // ========================
    // GET ALAT BY KODE
    // ========================
    public Alat getAlatByKode(String kode) {
        String sql = "SELECT * FROM alat WHERE kode = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToAlat(rs);
            }

        } catch (SQLException e) {
            showError(e);
        }
        return null;
    }

    // ========================
    // UPDATE ALAT
    // ========================
    public boolean updateAlat(Alat a) {
        String sql = """
            UPDATE alat 
            SET nama = ?, kategori = ?, stok = ?, tersedia = ?, kondisi = ?
            WHERE kode = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getNama());
            ps.setString(2, a.getKategori());
            ps.setInt(3, a.getStok());
            ps.setInt(4, a.getTersedia());
            ps.setString(5, a.getKondisi());
            ps.setString(6, a.getKode());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            showError(e);
            return false;
        }
    }

    // ========================
    // DELETE ALAT
    // ========================
    public boolean deleteAlat(String kode) {
        String sql = "DELETE FROM alat WHERE kode = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kode);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            showError(e);
            return false;
        }
    }

    // ========================
    // ERROR HANDLER
    // ========================
    private void showError(SQLException e) {
        JOptionPane.showMessageDialog(null,
                "Database Error:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
