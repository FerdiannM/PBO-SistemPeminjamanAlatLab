/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author USER
 */

import java.util.Date;

public class Transaksi {
    private int id;
    private String alatCode;
    private String mahasiswaNim;
    private Date tanggalPinjam;
    private Date tanggalKembali;
    private Date batasKembali;
    private String status;
    private int denda;
    
    // Tambahan untuk join
    private String alatNama;
    private String mahasiswaNama;
    
    public Transaksi() {
        this.status = "Dipinjam";
        this.denda = 0;
    }
    
    public Transaksi(String alatCode, String mahasiswaNim, Date tanggalPinjam, int durasiHari) {
        this.alatCode = alatCode;
        this.mahasiswaNim = mahasiswaNim;
        this.tanggalPinjam = tanggalPinjam;
        this.status = "Dipinjam";
        this.denda = 0;
        // Set batas kembali (tanggal pinjam + durasi)
        long time = tanggalPinjam.getTime() + (durasiHari * 24L * 60 * 60 * 1000);
        this.batasKembali = new Date(time);
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getAlatCode() { return alatCode; }
    public void setAlatCode(String alatCode) { this.alatCode = alatCode; }
    
    public String getMahasiswaNim() { return mahasiswaNim; }
    public void setMahasiswaNim(String mahasiswaNim) { this.mahasiswaNim = mahasiswaNim; }
    
    public Date getTanggalPinjam() { return tanggalPinjam; }
    public void setTanggalPinjam(Date tanggalPinjam) { this.tanggalPinjam = tanggalPinjam; }
    
    public Date getTanggalKembali() { return tanggalKembali; }
    public void setTanggalKembali(Date tanggalKembali) { this.tanggalKembali = tanggalKembali; }
    
    public Date getBatasKembali() { return batasKembali; }
    public void setBatasKembali(Date batasKembali) { this.batasKembali = batasKembali; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getDenda() { return denda; }
    public void setDenda(int denda) { this.denda = denda; }
    
    public String getAlatNama() { return alatNama; }
    public void setAlatNama(String alatNama) { this.alatNama = alatNama; }
    
    public String getMahasiswaNama() { return mahasiswaNama; }
    public void setMahasiswaNama(String mahasiswaNama) { this.mahasiswaNama = mahasiswaNama; }
    
    // Helper methods
    public boolean isTerlambat() {
        if (tanggalKembali == null) {
            return new Date().after(batasKembali);
        }
        return tanggalKembali.after(batasKembali);
    }
    
    public void hitungDenda() {
        if (isTerlambat()) {
            long diff = new Date().getTime() - batasKembali.getTime();
            long daysLate = diff / (1000 * 60 * 60 * 24);
            denda = (int) daysLate * 5000; // Rp 5000 per hari
        }
    }
    
    public void kembalikan() {
        this.tanggalKembali = new Date();
        hitungDenda();
        this.status = "Dikembalikan";
    }
}
