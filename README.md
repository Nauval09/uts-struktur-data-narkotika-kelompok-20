# UTS Struktur Data - Aplikasi Manajemen Putusan Narkotika (Kelompok 20)

Aplikasi Java untuk mengelola 50 putusan pengadilan narkotika (PN Mataram) dengan **struktur data buatan sendiri**, tanpa `ArrayList`, `Stack`, atau koleksi bawaan Java sebagai struktur utama. Tersedia antarmuka CLI (menu teks) dan GUI (Swing).

Mata kuliah Struktur Data, UMM, Semester Ganjil 2026/2027.

## Identitas Kelompok

**Kelompok 20**

| Nama | NIM | GitHub |
|---|---|---|
| Muhammad Nauval Adiaulhaq | 202510370110142 | [@Nauval09](https://github.com/Nauval09) |
| KENDRA FREDLINO RAHADI | 202510370110114 | [@kendraicikiwir](https://github.com/kendraicikiwir) |

## Struktur Data yang Dipilih

| Struktur | Kelas | Fungsi dalam aplikasi |
|---|---|---|
| **Struktur utama**: Dynamic Array | `DynamicArray<T>` | Menyimpan seluruh data putusan. Kapasitas awal 10 dan digandakan saat penuh. |
| **Struktur sekunder**: Stack | `CustomStack<T>` | Menyimpan riwayat audit operasi (tambah, hapus, dan lainnya) dengan urutan terbaru di atas. |

Keduanya mengikuti kontrak antarmuka `StrukturData<T>` (`add`, `get`, `remove`, `size`, `isEmpty`, `contains`, `clear`). Setiap operasi memvalidasi invarian struktur (`validateInvariant()`).

Alasan pemilihan dijelaskan di [`docs/adr`](docs/adr).

**Algoritma:**
- Pencarian: linear search berdasarkan Case ID atau nomor perkara, dengan jumlah langkah ditampilkan.
- Pengurutan: bubble sort manual berdasarkan vonis (ascending atau descending).

## Fitur

- Impor 50 putusan dari CSV ke struktur utama
- Tampil, cari, filter (jenis narkotika, rentang berat barang bukti, pasal terbukti, rentang vonis), agregasi statistik, dan urut data
- Tambah dan hapus putusan, setiap operasi tercatat ke riwayat audit (stack)
- Ekspor laporan ke file CSV
- Antarmuka GUI Swing dengan tabel data

## Struktur Repositori

```
data/putusan-50.csv            Dataset 50 putusan
docs/data-provenance.csv       Asal tiap nilai data (extractor, reviewer, halaman sumber)
docs/adr/                      Architecture Decision Record
src/main/java/pm/mataram/
  Main.java                    Aplikasi CLI
  MainGUI.java                 Aplikasi GUI (Swing)
  model/Putusan.java           Model data
  service/PutusanService.java  Impor CSV, tambah, hapus, cari, audit
  structure/                   DynamicArray, CustomStack, StrukturData
src/test/java/pm/mataram/      Unit test (JUnit 5)
AI_USAGE.md                    Log penggunaan AI
CONTRIBUTIONS.md               Kontribusi tiap anggota
```

## Prasyarat

- JDK 17 atau lebih baru
- Apache Maven 3.x

## Cara Build

```bash
git clone https://github.com/Nauval09/uts-struktur-data-narkotika-kelompok-20.git
cd uts-struktur-data-narkotika-kelompok-20
mvn clean compile
```

## Cara Test

```bash
mvn test
```

Test memakai JUnit 5 dan mencakup:
- `DynamicArray`: `add`, `insert`, `remove`, pertumbuhan kapasitas (resize), penolakan nilai `null`, dan indeks di luar batas
- `CustomStack`: `push`, `pop`, stack kosong, dan pertumbuhan kapasitas
- `PutusanService`: impor CSV, tambah, hapus, cari, dan pencatatan audit

## Cara Menjalankan

Jalankan dari **root repositori**, karena aplikasi membaca `data/putusan-50.csv` lewat path relatif.

```bash
# CLI
java -cp target/classes pm.mataram.Main

# GUI
java -cp target/classes pm.mataram.MainGUI
```

## Contoh Penggunaan (CLI)

Menu utama:

```
1. Tampilkan Ringkasan Semua Putusan
2. Cari Putusan (Berdasarkan Case ID / No Perkara)
3. Filter Data (Jenis Narkotika / Berat / Pasal / Rentang Vonis)
4. Agregasi Data (Statistik Total & Rata-rata)
5. Urutkan Data Berdasarkan Vonis
6. Tambah Data Putusan Baru
7. Hapus Data Putusan (Berdasarkan Case ID / No Perkara)
8. Lihat Riwayat Audit (Struktur Sekunder / Stack)
9. Ekspor Laporan ke File
10. Keluar
```

Contoh: mencari putusan berdasarkan Case ID.

```
Pilih menu (1-10): 2
Masukkan Case ID atau No Perkara yang dicari: PUT-001
[DATA DITEMUKAN dalam 1 langkah]
```

Contoh: filter berdasarkan jenis narkotika.

```
Pilih menu (1-10): 3
Pilih sub-menu filter (1-4): 1
Masukkan jenis narkotika (cth: Sabu): sabu
```

Contoh: urutkan berdasarkan vonis, dari terpendek ke terlama.

```
Pilih menu (1-10): 5
Pilih metode pengurutan (1-2): 1
```

Contoh: ekspor laporan.

```
Pilih menu (1-10): 9
Masukkan nama file ekspor (cth: laporan-putusan.csv): laporan-putusan.csv
```

## Data

- `data/putusan-50.csv`: 50 putusan PN Mataram dengan kolom nomor perkara, pengadilan, tanggal putusan, jenis narkotika, berat barang bukti, pasal dakwaan dan terbukti, tuntutan, vonis, denda, barang bukti, keadaan memberatkan dan meringankan, serta halaman sumber.
- `docs/data-provenance.csv`: tiap nilai ditelusuri ke berkas sumber, halaman, extractor, dan reviewer. Setiap nilai diekstrak oleh satu anggota dan direview oleh anggota lain.
- Identitas terdakwa dianonimkan menjadi kode (`TRD-001`, dan seterusnya).
- PDF putusan sumber tidak disertakan di repositori.

## Catatan Pengumpulan

Rilis final: tag `uts-v1.0`, dibuat dari commit yang didemonstrasikan.
