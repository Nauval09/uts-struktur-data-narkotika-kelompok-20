package pm.mataram;

import java.time.LocalDate;
import java.util.Scanner;
import pm.mataram.model.Putusan;
import pm.mataram.service.PutusanService;
import pm.mataram.structure.DynamicArray;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== APLIKASI MANAJEMEN PUTUSAN NARKOTIKA PN MATARAM ===");
        System.out.println("Sistem Struktur Data UMM - Semester Ganjil 2026/2027\n");
        
        // --- INISIALISASI & MEMUAT 50 DATA CSV KE STRUKTUR UTAMA ---
        PutusanService service = new PutusanService(); 
        service.imporCsv("data/putusan-50.csv");
        DynamicArray<Putusan> dataset = service.getDaftarPutusan();
        
        Scanner scanner = new Scanner(System.in);
        int pilihan = 0;

        do {
            System.out.println("\n================ MENU UTAMA ================");
            System.out.println("1. Tampilkan Ringkasan Semua Putusan");
            System.out.println("2. Cari Putusan (Berdasarkan Case ID / No Perkara)");
            System.out.println("3. Filter Data (Jenis Narkotika / Rentang Vonis)");
            System.out.println("4. Agregasi Data (Statistik Total & Rata-rata)");
            System.out.println("5. Urutkan Data Berdasarkan Vonis");
            System.out.println("6. Tambah Data Putusan Baru");
            System.out.println("7. Hapus Data Putusan (Berdasarkan Case ID / No Perkara)");
            System.out.println("8. Lihat Riwayat Audit (Struktur Sekunder / Stack)");
            System.out.println("9. Ekspor Laporan ke File");
            System.out.println("10. Keluar");
            System.out.print("Pilih menu (1-10): ");

            if (scanner.hasNextInt()) {
                pilihan = scanner.nextInt();
                scanner.nextLine(); // Membersihkan newline

                switch (pilihan) {
                    case 1:
                        System.out.println("\n--- [FITUR TAMPIL] DAFTAR SEMUA PUTUSAN ---");
                        int totalData = dataset.size(); 
                        if (totalData == 0) {
                            System.out.println("Belum ada data di dalam struktur.");
                        } else {
                            for (int i = 0; i < totalData; i++) {
                                Putusan p = dataset.get(i); 
                                System.out.println("[" + p.getCaseId() + "] " + 
                                                   p.getNomorPerkara() + " | " + 
                                                   p.getJenisNarkotika() + " | Vonis: " + 
                                                   p.getVonisBulan() + " bln");
                            }
                        }
                        System.out.println("\nTotal data aktif dalam struktur: " + totalData + " putusan.");
                        break;
                        
                    case 2:
                        System.out.print("\nMasukkan Case ID atau No Perkara yang dicari: ");
                        String keyword = scanner.nextLine();
                        System.out.println("[PENCARIAN] Melacak '" + keyword + "' pada struktur utama...");
                        
                        boolean ditemukan = false;
                        int langkah = 0;
                        int total = dataset.size(); 
                        
                        for (int i = 0; i < total; i++) {
                            langkah++;
                            Putusan p = dataset.get(i); 
                            
                            if (p.getCaseId().equalsIgnoreCase(keyword) || p.getNomorPerkara().equalsIgnoreCase(keyword)) {
                                ditemukan = true;
                                System.out.println("\n[DATA DITEMUKAN dalam " + langkah + " langkah]");
                                System.out.println("----------------------------------------");
                                System.out.println("Case ID         : " + p.getCaseId());
                                System.out.println("Nomor Perkara   : " + p.getNomorPerkara());
                                System.out.println("Pengadilan      : " + p.getPengadilan());
                                System.out.println("Jenis Narkotika : " + p.getJenisNarkotika());
                                System.out.println("Vonis Penjara   : " + p.getVonisBulan() + " bulan");
                                System.out.println("Denda           : Rp " + p.getDendaRupiah());
                                System.out.println("----------------------------------------");
                                break;
                            }
                        }
                        
                        if (!ditemukan) {
                            System.out.println("[INFO] Data dengan kata kunci '" + keyword + "' tidak ditemukan setelah " + langkah + " langkah.");
                        }
                        break;
                        
                    case 3:
                        System.out.println("\n--- [FITUR FILTER] ---");
                        System.out.println("1. Berdasarkan Jenis Narkotika");
                        System.out.println("2. Berdasarkan Rentang Berat Barang Bukti (gram)");
                        System.out.println("3. Berdasarkan Pasal Terbukti");
                        System.out.println("4. Berdasarkan Rentang Vonis (bulan)");
                        System.out.print("Pilih sub-menu filter (1-4): ");
                        int subFilter = scanner.nextInt();
                        scanner.nextLine(); // Bersihkan newline

                        if (subFilter == 1) {
                            System.out.print("Masukkan jenis narkotika (cth: Sabu): ");
                            String cari = scanner.nextLine().toLowerCase();
                            int count = 0;
                            for (int i = 0; i < dataset.size(); i++) {
                                Putusan p = dataset.get(i);
                                if (p.getJenisNarkotika().toLowerCase().contains(cari)) {
                                    count++;
                                    System.out.println(count + ". [" + p.getCaseId() + "] " + p.getNomorPerkara() + " | " + p.getJenisNarkotika() + " | BB: " + p.getBeratBarangBuktiGram() + " gr");
                                }
                            }
                            System.out.println("Total ditemukan: " + count);

                        } else if (subFilter == 2) {
                            System.out.print("Masukkan berat minimum (gram): ");
                            double min = scanner.nextDouble();
                            System.out.print("Masukkan berat maksimum (gram): ");
                            double max = scanner.nextDouble();
                            scanner.nextLine();
                            
                            int count = 0;
                            for (int i = 0; i < dataset.size(); i++) {
                                Putusan p = dataset.get(i);
                                if (p.getBeratBarangBuktiGram() >= min && p.getBeratBarangBuktiGram() <= max) {
                                    count++;
                                    System.out.println(count + ". [" + p.getCaseId() + "] " + p.getNomorPerkara() + " | BB: " + p.getBeratBarangBuktiGram() + " gr");
                                }
                            }
                            System.out.println("Total ditemukan: " + count);

                        } else if (subFilter == 3) {
                            System.out.print("Masukkan pasal terbukti (cth: Pasal 114): ");
                            String cariPasal = scanner.nextLine().toLowerCase();
                            int count = 0;
                            for (int i = 0; i < dataset.size(); i++) {
                                Putusan p = dataset.get(i);
                                if (p.getPasalTerbukti().toLowerCase().contains(cariPasal)) {
                                    count++;
                                    System.out.println(count + ". [" + p.getCaseId() + "] " + p.getNomorPerkara() + " | Pasal: " + p.getPasalTerbukti());
                                }
                            }
                            System.out.println("Total ditemukan: " + count);

                        } else if (subFilter == 4) {
                            System.out.print("Masukkan vonis minimum (bulan): ");
                            int minVonis = scanner.nextInt();
                            System.out.print("Masukkan vonis maksimum (bulan): ");
                            int maxVonis = scanner.nextInt();
                            scanner.nextLine();
                            
                            int count = 0;
                            for (int i = 0; i < dataset.size(); i++) {
                                Putusan p = dataset.get(i);
                                if (p.getVonisBulan() >= minVonis && p.getVonisBulan() <= maxVonis) {
                                    count++;
                                    System.out.println(count + ". [" + p.getCaseId() + "] " + p.getNomorPerkara() + " | Vonis: " + p.getVonisBulan() + " bln");
                                }
                            }
                            System.out.println("Total ditemukan: " + count);
                        } else {
                            System.out.println("Pilihan sub-menu tidak valid.");
                        }
                        break;

                        
                    case 4:
                        System.out.println("\n--- [FITUR AGREGASI] STATISTIK PUTUSAN ---");
                        int totalPerkara = dataset.size();
                        
                        if (totalPerkara == 0) {
                            System.out.println("Belum ada data untuk dihitung.");
                        } else {
                            long totalVonisBulan = 0;
                            long totalDenda = 0;
                            double totalBeratBB = 0;
                            
                            // Melakukan iterasi manual melalui DynamicArray untuk agregasi
                            for (int i = 0; i < totalPerkara; i++) {
                                Putusan p = dataset.get(i);
                                totalVonisBulan += p.getVonisBulan();
                                totalDenda += p.getDendaRupiah();
                                totalBeratBB += p.getBeratBarangBuktiGram();
                            }
                            
                            double rataRataVonis = (double) totalVonisBulan / totalPerkara;
                            double rataRataBerat = totalBeratBB / totalPerkara;
                            
                            System.out.println("- Total Perkara Terdaftar    : " + totalPerkara + " kasus");
                            System.out.println("- Rata-rata Vonis Penjara    : " + String.format("%.2f", rataRataVonis) + " bulan");
                            System.out.println("- Rata-rata Berat Barang Bukti : " + String.format("%.3f", rataRataBerat) + " gram");
                            System.out.println("- Total Akumulasi Denda      : Rp " + String.format("%,d", totalDenda).replace(',', '.'));
                        }
                        break;

                        
                    case 5:
                        System.out.println("\n--- [FITUR URUT] PENGURUTAN DATA BERDASARKAN VONIS ---");
                        System.out.println("1. Urutkan Ascending (Vonis Terpendek ke Terlama)");
                        System.out.println("2. Urutkan Descending (Vonis Terlama ke Terpendek)");
                        System.out.print("Pilih metode pengurutan (1-2): ");
                        int subUrut = scanner.nextInt();
                        scanner.nextLine(); // Bersihkan newline

                        int n = dataset.size();
                        if (n <= 1) {
                            System.out.println("Data terlalu sedikit untuk diurutkan.");
                            break;
                        }

                        // Algoritma Bubble Sort manual pada DynamicArray
                        for (int i = 0; i < n - 1; i++) {
                            for (int j = 0; j < n - i - 1; j++) {
                                Putusan p1 = dataset.get(j);
                                Putusan p2 = dataset.get(j + 1);
                                
                                boolean kondisiTukar = (subUrut == 1) ? 
                                    (p1.getVonisBulan() > p2.getVonisBulan()) : // Ascending
                                    (p1.getVonisBulan() < p2.getVonisBulan());   // Descending
                                    
                                if (kondisiTukar) {
                                    Putusan temp = p1;
                                    dataset.set(j, p2);
                                    dataset.set(j + 1, temp);
                                }
                            }
                        }

                        System.out.println("\n[BERHASIL] Data berhasil diurutkan! Menampilkan seluruh data (" + n + " putusan):");
                        for (int i = 0; i < n; i++) {
                            Putusan p = dataset.get(i);
                            System.out.println((i + 1) + ". [" + p.getCaseId() + "] " + p.getNomorPerkara() + 
                                            " | " + p.getJenisNarkotika() + " | Vonis: " + p.getVonisBulan() + " bln");
                        }
                        break;

                    case 6:
                    System.out.println("\n--- [FITUR TAMBAH PUTUSAN] ---");
                    try {
                        System.out.print("Masukkan Case ID (cth: PUT-051): ");
                        String cId = scanner.nextLine();
                        System.out.print("Masukkan Nomor Perkara: ");
                        String noPerkara = scanner.nextLine();
                        System.out.print("Masukkan Pengadilan: ");
                        String pengadilan = scanner.nextLine();
                        System.out.print("Masukkan Tanggal Putusan (YYYY-MM-DD): ");
                        LocalDate tgl = LocalDate.parse(scanner.nextLine());
                        System.out.print("Masukkan Kode Terdakwa: ");
                        String trd = scanner.nextLine();
                        System.out.print("Masukkan Jenis Narkotika: ");
                        String jenis = scanner.nextLine();
                        System.out.print("Masukkan Berat Barang Bukti (gram): ");
                        double berat = scanner.nextDouble();
                        scanner.nextLine(); // clear newline
                        System.out.print("Masukkan Pasal Dakwaan: ");
                        String pDakwaan = scanner.nextLine();
                        System.out.print("Masukkan Pasal Terbukti: ");
                        String pTerbukti = scanner.nextLine();
                        System.out.print("Masukkan Tuntutan (bulan): ");
                        int tuntutan = scanner.nextInt();
                        System.out.print("Masukkan Vonis (bulan): ");
                        int vonis = scanner.nextInt();
                        System.out.print("Masukkan Denda (Rupiah): ");
                        long denda = scanner.nextLong();
                        scanner.nextLine();
                        System.out.print("Masukkan Barang Bukti: ");
                        String bb = scanner.nextLine();
                        System.out.print("Masukkan Keadaan Memberatkan: ");
                        String beratFaktor = scanner.nextLine();
                        System.out.print("Masukkan Keadaan Meringankan: ");
                        String ringanFaktor = scanner.nextLine();
                        System.out.print("Masukkan Sumber Halaman (Source Pages): ");
                        String srcPages = scanner.nextLine();

                        // Buat objek Putusan baru
                        Putusan newPutusan = new Putusan(cId, noPerkara, pengadilan, tgl, trd, jenis, berat, 
                                                        pDakwaan, pTerbukti, tuntutan, vonis, denda, 
                                                        bb, beratFaktor, ringanFaktor, srcPages);
                        
                        // Masukkan ke service / dynamic array
                        service.tambahPutusan(newPutusan);
                        System.out.println("[BERHASIL] Putusan baru berhasil ditambahkan! Total data sekarang: " + dataset.size());
                    } catch (Exception e) {
                        System.out.println("[GAGAL] Input tidak valid: " + e.getMessage());
                    }
                    break;


                    case 7:
                        System.out.println("\n--- [FITUR HAPUS PUTUSAN] ---");
                        int totalDataHapus = dataset.size();
                        if (totalDataHapus == 0) {
                            System.out.println("Struktur data kosong.");
                            break;
                        }
                        
                        System.out.println("Total data aktif saat ini: " + totalDataHapus + " record");
                        System.out.print("Masukkan Case ID atau Nomor Perkara yang ingin dihapus: ");
                        String keywordHapus = scanner.nextLine();
                        
                        // Cari dulu indeks berdasarkan Case ID / Nomor Perkara
                        int targetIndex = -1;
                        for (int i = 0; i < dataset.size(); i++) {
                            Putusan p = dataset.get(i);
                            if (p.getCaseId().equalsIgnoreCase(keywordHapus) || p.getNomorPerkara().equalsIgnoreCase(keywordHapus)) {
                                targetIndex = i;
                                break;
                            }
                        }
                        
                        if (targetIndex != -1) {
                            Putusan removed = service.hapusPutusan(targetIndex);
                            System.out.println("\n[BERHASIL] Data berhasil dihapus dari struktur!");
                            System.out.println(" > Case ID terhapus : " + removed.getCaseId());
                            System.out.println(" > Nomor Perkara    : " + removed.getNomorPerkara());
                            System.out.println(" > Sisa Data        : " + dataset.size() + " record");
                        } else {
                            System.out.println("\n[GAGAL] Data dengan keyword '" + keywordHapus + "' tidak ditemukan.");
                        }
                        break;
                        
                    case 8:
                        System.out.println("\n--- [FITUR AUDIT] RIWAYAT OPERASI (CUSTOM STACK) ---");
                        // Pastikan PutusanService Anda memiliki method getter untuk riwayatAudit
                        // public CustomStack<String> getRiwayatAudit() { return riwayatAudit; }
                        
                        pm.mataram.structure.CustomStack<String> auditStack = service.getRiwayatAudit();
                        
                        if (auditStack.isEmpty()) {
                            System.out.println("Belum ada riwayat operasi yang tercatat di dalam Stack.");
                        } else {
                            System.out.println("Total riwayat operasi tercatat: " + auditStack.size() + "\n");
                            
                            // Karena kita ingin menampilkan dari yang terbaru ke terlama tanpa merusak stack asli,
                            // kita bisa membuat stack sementara (atau memanfaatkan mekanisme salin)
                            // Cara aman: kita pop satu-satu ke stack sementara, cetak, lalu kembalikan.
                            pm.mataram.structure.CustomStack<String> tempStack = new pm.mataram.structure.CustomStack<>(auditStack.size());
                            int nomor = 1;
                            
                            while (!auditStack.isEmpty()) {
                                String riwayat = auditStack.pop();
                                System.out.println(nomor + ". " + riwayat);
                                tempStack.push(riwayat);
                                nomor++;
                            }
                            
                            // Kembalikan elemen ke stack semula agar data aslinya tidak hilang (Restore)
                            while (!tempStack.isEmpty()) {
                                auditStack.push(tempStack.pop());
                            }
                        }
                        break;

                        
                    case 9:
                        System.out.println("\n--- [FITUR EKSPOR LAPORAN] ---");
                        System.out.print("Masukkan nama file ekspor (cth: laporan-putusan.csv): ");
                        String filename = scanner.nextLine();

                        // Pastikan direktori atau nama file aman, kita simpan di folder 'data/' atau direktori kerja
                        String filePath = "data/" + filename;

                        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.File(filePath))) {
                            writer.println("CaseId,NomorPerkara,Pengadilan,JenisNarkotika,BeratBarangBuktiGram,PasalTerbukti,VonisBulan,DendaRupiah");

                            int totalEkspor = dataset.size();
                            for (int i = 0; i < totalEkspor; i++) {
                                Putusan p = dataset.get(i);
                                writer.println(p.getCaseId() + "," +
                                            p.getNomorPerkara() + "," +
                                            p.getPengadilan() + "," +
                                            p.getJenisNarkotika() + "," +
                                            p.getBeratBarangBuktiGram() + "," +
                                            p.getPasalTerbukti() + "," +
                                            p.getVonisBulan() + "," +
                                            p.getDendaRupiah());
                            }

                            System.out.println("[BERHASIL] Laporan berhasil diekspor ke '" + filePath + "' (" + totalEkspor + " baris data).");

                            // Opsional: Catat juga ke riwayat audit stack jika service Anda mendukungnya
                            // (Misalnya melalui method pencatatan audit di service)

                        } catch (java.io.FileNotFoundException e) {
                            System.out.println("[GAGAL] Terjadi kesalahan saat menulis file: " + e.getMessage());
                        }
                        break;
                        
                    case 10:
                        System.out.println("Terima kasih telah menggunakan aplikasi ini!");
                        break;
                        
                    default:
                        System.out.println("Pilihan tidak valid. Silakan masukkan angka 1 sampai 10.");
                }
            } else {
                System.out.println("Input tidak valid! Harap masukkan angka.");
                scanner.next(); 
            }
        } while (pilihan != 10);

        scanner.close();
    }
}