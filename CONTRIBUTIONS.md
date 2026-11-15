# Kontribusi Anggota Kelompok

Kelompok 20. Bukti diambil dari riwayat git, Issue, Pull Request, dan review di repositori ini.

| Nama | NIM | Peran & Kontribusi | Issue / PR |
|---|---|---|---|
| <NAMA LENGKAP> (`Nauval09`) | <NIM> | Ekstraksi data putusan 1-25 ke CSV; pengisian dan review silang `docs/data-provenance.csv`; <TAMBAHKAN: unit test `DynamicArray`, ADR-001, dll. setelah selesai> | <TAUTAN ISSUE / PR> |
| KENDRA FREDLINO RAHADI (`kendraicikiwir` / `Kendra`) | 202510370110114 | Ekstraksi data putusan 26-50 ke CSV; pengisian dan review silang `docs/data-provenance.csv`; model, service, dan struktur data kustom; CLI dan GUI; <TAMBAHKAN: unit test `CustomStack`/`PutusanService`, ADR-002, dll. setelah selesai> | <TAUTAN ISSUE / PR> |

## Catatan identitas git

Author `Kendra` dan `kendraicikiwir` adalah **orang yang sama**. Keduanya memakai email `kendrafre03@gmail.com` dan hanya berbeda konfigurasi `user.name` di perangkat yang berbeda. Daftar `.mailmap` di root repositori menyatukan keduanya.

## Rincian per anggota

### <NAMA LENGKAP> (`Nauval09`)

**Ekstraksi data**
- Data putusan 1-25: `data/putusan1-25.csv` (branch `putusan1-25`)
- Review silang: tiap nilai provenance yang diekstrak anggota lain ditandai dengan reviewer Nauval

**Provenance**
- Pengisian dan pembaruan `docs/data-provenance.csv`, misalnya commit `f463fd1`

**Implementasi / Pengujian / Dokumentasi**
- <ISI setelah dikerjakan: tautan commit dan PR>

**Pull Request dibuat:** <TAUTAN PR 1>, <TAUTAN PR 2>, <TAUTAN PR 3>
**Review diberikan (dengan komentar spesifik):** <TAUTAN REVIEW 1>, <TAUTAN REVIEW 2>

### <NAMA LENGKAP> (`kendraicikiwir` / `Kendra`)

**Ekstraksi data**
- Data putusan 26-50: `data/putusan25-50.csv` (branch `putusan25-50`)
- Melengkapi data vonis putusan 18-25: `eb512da`, `636b3e7`, `615fcee`
- Review silang provenance

**Implementasi**
- Model, service, dan struktur data kustom (`DynamicArray`, `CustomStack`, `StrukturData`): `7aca91e`
- Perbaikan merge dan pembaruan `Main.java`: `83515a4`
- GUI (`MainGUI.java`) dan pembaruan kode di `src`: `8941170`
- `.gitignore` dan pembersihan folder `target`: `7366a4d`

**Pengujian / Dokumentasi**
- <ISI setelah dikerjakan: tautan commit dan PR>

**Pull Request dibuat:** <TAUTAN PR 1>, <TAUTAN PR 2>, <TAUTAN PR 3>
**Review diberikan (dengan komentar spesifik):** <TAUTAN REVIEW 1>, <TAUTAN REVIEW 2>
**Pull Request #3** (`feat/struktur-data` ke `main`) di-merge oleh `kendraicikiwir`.
