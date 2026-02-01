/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author USER
 */
public class Alat {
    private int id;
    private String kode;
    private String nama;
    private String kategori;
    private int stok;
    private int tersedia;
    private String kondisi;
    
    public Alat() {}
    
    public Alat(String kode, String nama, String kategori, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.kategori = kategori;
        this.stok = stok;
        this.tersedia = stok;
        this.kondisi = "Baik";
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    
    public int getStok() { return stok; }
    public void setStok(int stok) { 
        this.stok = stok;
        if (tersedia > stok) {
            tersedia = stok;
        }
    }
    
    public int getTersedia() { return tersedia; }
    public void setTersedia(int tersedia) { this.tersedia = tersedia; }
    
    public String getKondisi() { return kondisi; }
    public void setKondisi(String kondisi) { this.kondisi = kondisi; }
    
    // Helper methods
    public boolean isAvailable() {
        return tersedia > 0 && "Baik".equals(kondisi);
    }
    
    public void pinjam() {
        if (tersedia > 0) {
            tersedia--;
        }
    }
    
    public void kembalikan() {
        if (tersedia < stok) {
            tersedia++;
        }
    }
    
    @Override
    public String toString() {
        return kode + " - " + nama + " (Tersedia: " + tersedia + "/" + stok + ")";
    }
}
