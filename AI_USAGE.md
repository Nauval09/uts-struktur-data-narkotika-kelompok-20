# Log Penggunaan AI (AI Usage Documentation)
**Proyek:** UTS Struktur Data - Aplikasi Manajemen Putusan Narkotika  
**Kelompok:** 20  
**Program Studi / Kelas:** Ilmu Komputer / Teknik Informatika  

Dokumen ini mencatat seluruh riwayat interaksi, penggunaan prompt, keluaran (*output*), serta bentuk modifikasi manual yang dilakukan bersama bantuan kecerdasan buatan (Gemini & Claude) selama proses pengembangan proyek berlangsung.

---

## Ringkasan Transparansi Penggunaan AI
Kecerdasan buatan digunakan secara kolaboratif sebagai *co-pilot* teknis untuk:
1. Membimbing alur kerja proyek (mulai dari pencarian dataset putusan PN Mataram, validasi nomor perkara, hingga ekstraksi data ke format CSV).
2. Menyusun dokumen arsitektur (*Architecture Decision Record* / ADR) seperti pemilihan struktur *Custom Dynamic Array* tanpa *Collection Framework* bawaan Java.
3. Merancang struktur direktori proyek standar Maven, konfigurasi `pom.xml`, serta skrip pembantu (*Python scripts*).
4. Menyelesaikan kendala teknis pengelolaan repositori Git/GitHub (konfigurasi identitas, *branch*, *commit*, dan *push*).

---

## Log Interaksi & Riwayat Penggunaan

### 1. Tahap Inisialisasi & Pengumpulan Dataset (12 November 2026)
* **Aktivitas:** Diskusi mengenai perencanaan proyek, validasi jumlah data (50 putusan narkotika), dan panduan pencarian serta pengunduhan dokumen dari Direktori Putusan MA (fokus PN Mataram).
* **Prompt / Topik:** *"Apakah butuh 50 putusan?", "Cara mencari putusan PN Mataram narkotika", dan pengecekan format dakwaan UU 35/2009.*
* **Output AI:** Panduan langkah demi langkah penyaringan data, tips penamaan file, serta penjelasan mengenai pentingnya memastikan nomor perkara unik dan menghindari kasus psikotropika di luar fokus utama.
* **Modifikasi / Validasi Manual:** Mengunduh 50 dokumen PDF putusan secara mandiri dan membaginya antar anggota kelompok.

### 2. Pengolahan Data & Penyusunan Provenance (12 – 15 November 2026)
* **Aktivitas:** Ekstraksi data putusan PDF ke dalam format CSV (`data/putusan-50.csv`) dan penyusunan catatan sumber data (`docs/data-provenance.csv`).
* **Prompt / Topik:** Meminta bantuan AI untuk menyusun format kode unik (`PUT-001`, `TRD-001`), konversi satuan hukuman, serta memvalidasi data CSV dari PUT-025 hingga PUT-050.
* **Output AI:** Pembuatan draf baris CSV, tabel halaman sumber, kode skrip Python (`make_provenance.py`), serta identifikasi temuan validasi data.
* **Modifikasi / Validasi Manual:** Memeriksa ulang keakuratan isi CSV terhadap dokumen putusan asli, memperbarui file `data-provenance.csv` di GitHub, dan memastikan format kolom sesuai spesifikasi tugas.

### 3. Perancangan Struktur Proyek & Konfigurasi Maven (13 November 2026)
* **Prompt ke AI:** 
  > *"Buat struktur direktori untuk UTS struktur data kelompok 20 dengan package `pm.mataram`, lengkap dengan `pom.xml` yang menggunakan JUnit 5 dan folder dokumentasi ADR."*
* **Output AI:** Menghasilkan rancangan direktori berstandar Maven (`src/main/java/pm/mataram`, `src/test/java/pm/mataram`, `data/`, `docs/adr/`, dll.) beserta draf `pom.xml` (Java 17 & JUnit 5).
* **Modifikasi / Validasi Manual:** Menyesuaikan versi kompiler di `pom.xml` agar selaras dengan perangkat lokal masing-masing anggota kelompok.

### 4. Penyusunan Dokumentasi ADR (Architecture Decision Record) (14 November 2026)
* **Prompt ke AI:** 
  > *"Tolong buatkan draf ADR 001 mengenai pemilihan struktur data karena tugas tidak boleh menggunakan Collection Framework bawaan Java."*
* **Output AI:** Membuat draf `001-pemilihan-struktur-data.md` yang membandingkan *Custom Dynamic Array* dan *Custom Linked List* beserta analisis kompleksitas waktunya.
* **Modifikasi / Validasi Manual:** Menyelaraskan isi keputusan dengan kesepakatan kelompok untuk menggunakan *Custom Dynamic Array* yang membungkus tipe objek `Putusan`.

### 5. Pengelolaan Git, GitHub & Troubleshooting Terminal (12 – November 2026)
* **Aktivitas:** Mengatasi kendala koneksi repositori, pembuatan *branch*, serta error saat melakukan *commit*.
* **Prompt / Topik:** *"Kenapa tidak bisa commit? Muncul pesan error 'Make sure you configure your user.name and user.email in git'."*
* **Output AI:** Panduan konfigurasi identitas Git (`git config --global`) serta langkah-langkah *add*, *commit*, dan *push* via terminal VS Code.
* **Modifikasi / Validasi Manual:** Menjalankan perintah terminal menggunakan identitas akun GitHub masing-masing kontributor.

---

## Pernyataan Integritas Akademik
Seluruh anggota kelompok 20 menyatakan bahwa kode inti, logika program, pengujian, serta pemahaman atas seluruh baris kode yang diserahkan untuk penilaian UTS ini telah ditelaah, dipahami, dan diverifikasi secara mandiri. Bantuan AI murni dimanfaatkan sebagai sarana referensi teknis, dokumentasi pendukung, dan akselerasi alur kerja pengembangan perangkat lunak.

