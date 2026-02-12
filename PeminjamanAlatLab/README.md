# Sistem Peminjaman Alat Laboratorium

Sistem ini adalah aplikasi Java berbasis GUI (Swing) untuk mengelola peminjaman alat laboratorium. Aplikasi ini memiliki fitur untuk **Admin** dan **Mahasiswa**, termasuk peminjaman, pengembalian, laporan, serta manajemen alat dan mahasiswa.

---

## Fitur Utama

- Login Admin dan Mahasiswa
- Peminjaman alat laboratorium
- Pengembalian alat dengan perhitungan denda jika terlambat
- Riwayat peminjaman mahasiswa
- Laporan transaksi peminjaman
- Manajemen data alat dan mahasiswa (Admin)

---

## Struktur Folder

src/
├─ dao/ # Kelas untuk akses database (Data Access Object)
├─ database/ # Koneksi database
├─ gui/ # GUI forms (Swing)
├─ main/ # Kelas Main / Entry Point
└─ models/ # Kelas model / entitas (Mahasiswa, Alat, Transaksi, User)

## Persyaratan

- Java JDK 17+  
- NetBeans IDE  
- MySQL Server  
- JDBC Driver MySQL (pastikan `mysql-connector-java` sudah ada di library proyek)

---

## Setup Database MySQL

1. Buat database baru, misal `peminjaman_lab`
2. Buat tabel-tabel yang dibutuhkan (`mahasiswa`, `alat`, `transaksi`, `user`) sesuai skema proyek.
3. Sesuaikan koneksi di `src/database/DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/lab_db";
private static final String USER = "root";       // ganti sesuai username MySQL
private static final String PASSWORD = "password"; // ganti sesuai password MySQL

## E-book
https://ebook.webiot.id/ebooks/sistem-peminjaman-alat-laboratorium-berbasis-java-pbo-menggunakan-mysql
