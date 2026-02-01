package dao;

import database.DatabaseConnection;
import models.Transaksi;
import models.Mahasiswa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class TransaksiDAO {

    // =========================
    // Helper Mapper
    // =========================
    private Transaksi mapResultSet(ResultSet rs) throws SQLException {
        Transaksi trx = new Transaksi();
        trx.setId(rs.getInt("id_transaksi"));
        trx.setAlatCode(rs.getString("alat_code"));
        trx.setMahasiswaNim(rs.getString("mahasiswa_nim"));
        trx.setTanggalPinjam(rs.getDate("tanggal_pinjam"));
        trx.setTanggalKembali(rs.getDate("tanggal_kembali"));
        trx.setBatasKembali(rs.getDate("batas_kembali"));
        trx.setStatus(rs.getString("status"));
        trx.setDenda(rs.getInt("denda"));

        // optional join field
        try { trx.setAlatNama(rs.getString("alat_nama")); } catch (SQLException ignored) {}
        try { trx.setMahasiswaNama(rs.getString("mahasiswa_nama")); } catch (SQLException ignored) {}
        try { trx.setAlatNama(rs.getString("nama_alat")); } catch (SQLException ignored) {}
        try { trx.setMahasiswaNama(rs.getString("nama_mahasiswa")); } catch (SQLException ignored) {}

        return trx;
    }

    // =========================
    // ADD TRANSAKSI (MAHASISWA)
    // =========================
    public boolean addTransaksiMahasiswa(Transaksi transaksi, Mahasiswa mahasiswa) {

        // keamanan login
        if (!transaksi.getMahasiswaNim().equals(mahasiswa.getNim())) {
            throw new SecurityException(
                "AKSES DITOLAK: NIM transaksi tidak sesuai user login"
            );
        }

        return insertTransaksi(transaksi, mahasiswa.getNim());
    }

    // =========================
    // ADD TRANSAKSI (ADMIN)
    // =========================
    public boolean addTransaksiAdmin(Transaksi transaksi) {
        return insertTransaksi(transaksi, transaksi.getMahasiswaNim());
    }

    // =========================
    // HELPER INSERT TRANSAKSI
    // =========================
    private boolean insertTransaksi(Transaksi trx, String nim) {
        String sql = """
            INSERT INTO transaksi 
            (alat_code, mahasiswa_nim, tanggal_pinjam, batas_kembali, status, denda)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trx.getAlatCode());
            pstmt.setString(2, nim);
            pstmt.setDate(3, new java.sql.Date(trx.getTanggalPinjam().getTime()));
            pstmt.setDate(4, new java.sql.Date(trx.getBatasKembali().getTime()));
            pstmt.setString(5, trx.getStatus());
            pstmt.setInt(6, trx.getDenda());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // TRANSAKSI AKTIF (DIPINJAM)
    // =========================
    public List<Transaksi> getTransaksiAktif() {
        String sql = """
            SELECT t.*, 
                   a.nama AS alat_nama, 
                   m.nama AS mahasiswa_nama
            FROM transaksi t
            JOIN alat a ON t.alat_code = a.kode
            JOIN mahasiswa m ON t.mahasiswa_nim = m.nim
            WHERE t.status = 'Dipinjam'
        """;
        return getList(sql);
    }

    // =========================
    // UPDATE TRANSAKSI (KEMBALI)
    // =========================
    public boolean updateTransaksi(Transaksi trx) {
        String sql = """
            UPDATE transaksi 
            SET tanggal_kembali = ?, status = ?, denda = ?
            WHERE id_transaksi = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, new java.sql.Date(trx.getTanggalKembali().getTime()));
            pstmt.setString(2, trx.getStatus());
            pstmt.setInt(3, trx.getDenda());
            pstmt.setInt(4, trx.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // TRANSAKSI MAHASISWA (AKTIF)
    // =========================
    public List<Transaksi> getTransaksiByMahasiswa(String nim) {
        String sql = """
            SELECT t.*, 
                   a.nama AS nama_alat, 
                   m.nama AS nama_mahasiswa
            FROM transaksi t
            JOIN alat a ON t.alat_code = a.kode
            JOIN mahasiswa m ON t.mahasiswa_nim = m.nim
            WHERE t.mahasiswa_nim = ? AND t.status = 'Dipinjam'
        """;

        return getListWithParam(sql, nim);
    }

    // =========================
    // RIWAYAT MAHASISWA
    // =========================
    public List<Transaksi> getTransaksiDikembalikanByMahasiswa(String nim) {
        String sql = """
            SELECT t.*, 
                   a.nama AS alat_nama, 
                   m.nama AS mahasiswa_nama
            FROM transaksi t
            JOIN alat a ON t.alat_code = a.kode
            JOIN mahasiswa m ON t.mahasiswa_nim = m.nim
            WHERE t.mahasiswa_nim = ? 
              AND LOWER(t.status) = 'dikembalikan'
            ORDER BY t.tanggal_pinjam DESC
        """;

        return getListWithParam(sql, nim);
    }

    // =========================
    // SEMUA TRANSAKSI (ADMIN)
    // =========================
    public List<Transaksi> getAllTransaksi() {
        String sql = """
            SELECT t.*, 
                   a.nama AS alat_nama, 
                   m.nama AS mahasiswa_nama
            FROM transaksi t
            JOIN alat a ON t.alat_code = a.kode
            JOIN mahasiswa m ON t.mahasiswa_nim = m.nim
            ORDER BY t.tanggal_pinjam DESC
        """;

        return getList(sql);
    }

    // =========================
    // HELPER GET LIST (NO PARAM)
    // =========================
    private List<Transaksi> getList(String sql) {
        List<Transaksi> list = new ArrayList<>();

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
    // HELPER GET LIST (WITH PARAM)
    // =========================
    private List<Transaksi> getListWithParam(String sql, String param) {
        List<Transaksi> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, param);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
