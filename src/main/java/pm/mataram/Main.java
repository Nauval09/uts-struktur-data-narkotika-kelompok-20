package pm.mataram;

import java.util.Scanner;
import pm.mataram.model.Putusan;
import pm.mataram.service.PutusanService;
import pm.mataram.structure.DynamicArray;
// (Sesuaikan import di atas jika nama package helper/loader kamu berbeda)

public class Main {
    public static void main(String[] args) {
        System.out.println("=== APLIKASI MANAJEMEN PUTUSAN NARKOTIKA PN MATARAM ===");
        System.out.println("Sistem Struktur Data UMM - Semester Ganjil 2026/2027\n");
        
        // --- INISIALISASI & MEMUAT 50 DATA CSV KE STRUKTUR UTAMA ---
       DynamicArray<Putusan> dataset = PutusanService.loadData("data/putusan-50.csv");
        
        Scanner scanner = new Scanner(System.in);
        int pilihan = 0;

        do {
            System.out.println("\n================ MENU UTAMA ================");
            System.out.println("1. Tampilkan Ringkasan Semua Putusan");
            System.out.println("2. Cari Putusan (Berdasarkan Case ID / No Perkara)");
            System.out.println("3. Filter Data (Jenis Narkotika / Rentang Vonis)");
            System.out.println("4. Agregasi Data (Statistik Total & Rata-rata)");
            System.out.println("5. Urutkan Data Berdasarkan Vonis");
            System.out.println("6. Lihat Riwayat Audit (Struktur Sekunder / Stack)");
            System.out.println("7. Ekspor Laporan ke File");
            System.out.println("8. Keluar");
            System.out.print("Pilih menu (1-8): ");

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
                        System.out.println("- Total Perkara Terdaftar : 50");
                        System.out.println("- Rata-rata Vonis Penjara : [Dihitung dari data]");
                        System.out.println("- Total Akumulasi Denda   : [Dihitung dari data]");
                        break;
                        
                    case 5:
                        System.out.println("\n--- [FITUR URUT] PENGURUTAN VONIS ---");
                        System.out.println("Mengurutkan data berdasarkan lama vonis (bulan)...");
                        break;
                        
                    case 6:
                        System.out.println("\n--- [FITUR AUDIT] RIWAYAT OPERASI (STACK/QUEUE) ---");
                        System.out.println("Menampilkan riwayat operasi dari struktur sekunder...");
                        break;
                        
                    case 7:
                        System.out.print("\nMasukkan nama file ekspor (cth: laporan-hasil.csv): ");
                        String filename = scanner.nextLine();
                        System.out.println("Menyimpan laporan ke direktori ekspor...");
                        break;
                        
                    case 8:
                        System.out.println("Terima kasih telah menggunakan aplikasi ini!");
                        break;
                        
                    default:
                        System.out.println("Pilihan tidak valid. Silakan masukkan angka 1 sampai 8.");
                }
            } else {
                System.out.println("Input tidak valid! Harap masukkan angka.");
                scanner.next(); 
            }
        } while (pilihan != 8);

        scanner.close();
    }
}