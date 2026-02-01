/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author USER
 */
public class Mahasiswa {
    private String nim;
    private String nama;
    private String jurusan;
    private int userId;
    
    public Mahasiswa(){}
    
    public Mahasiswa(String nim, String nama, String jurusan){
        this.nim = nim;
        this.nama = nama;
        this.jurusan = jurusan;
    }
    
//    Getter & Setters
    
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getJurusan() { return jurusan; }
    public void setJurusan(String jurusan) { this.jurusan = jurusan; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    @Override
    public String toString() {
        return nim + " - " + nama + " (" + jurusan + ")";
    }
}
