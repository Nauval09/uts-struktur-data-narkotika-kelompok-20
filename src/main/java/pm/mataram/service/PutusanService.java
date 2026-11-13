package pm.mataram.service;

import pm.mataram.model.Putusan;
import pm.mataram.structure.CustomStack;
import pm.mataram.structure.DynamicArray;

import java.io.*;
import java.time.LocalDate;

public class PutusanService {
    private DynamicArray<Putusan> daftarPutusan = new DynamicArray<>();
    private CustomStack<String> riwayatAudit = new CustomStack<>(50);

public static DynamicArray<Putusan> loadData(String filePath) {
    PutusanService ps = new PutusanService();
    ps.imporCsv(filePath);
    return ps.getDaftarPutusan();
}

    public void imporCsv(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Lewati header
            int lineNum = 1;
            while ((line = br.readLine()) != null) {
                lineNum++;
                try {
                    String[] col = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    if (col.length < 16) throw new IllegalArgumentException("Kolom kurang dari 16");

                    for (int i = 0; i < col.length; i++) {
                        col[i] = col[i].replaceAll("^\"|\"$", "").trim();
                    }

                    if (col[0].isEmpty() || col[1].isEmpty()) {
                        throw new IllegalArgumentException("caseId atau nomorPerkara kosong");
                    }

                    Putusan p = new Putusan(
                        col[0], col[1], col[2], LocalDate.parse(col[3]), col[4], col[5],
                        Double.parseDouble(col[6]), col[7], col[8],
                        Integer.parseInt(col[9]), Integer.parseInt(col[10]),
                        Long.parseLong(col[11]), col[12], col[13], col[14], col[15]
                    );
                    daftarPutusan.add(p);
                } catch (Exception e) {
                    System.err.println("[PERINGATAN] Baris " + lineNum + " ditolak (" + e.getMessage() + ")");
                }
            }
            riwayatAudit.push("IMPOR CSV: Total berhasil dimuat = " + daftarPutusan.size());
        } catch (IOException e) {
            System.err.println("Gagal membaca file: " + e.getMessage());
        }
    }

    public void tampilSemua() {
        for (int i = 0; i < daftarPutusan.size(); i++) {
            System.out.println((i + 1) + ". " + daftarPutusan.get(i));
        }
        riwayatAudit.push("TAMPIL: Menampilkan " + daftarPutusan.size() + " putusan");
    }

    public void tambahPutusan(Putusan p) {
        daftarPutusan.add(p);
        riwayatAudit.push("TAMBAH: Menambahkan " + p.getCaseId());
    }

    public Putusan hapusPutusan(int index) {
        Putusan removed = daftarPutusan.remove(index);
        riwayatAudit.push("HAPUS: Menghapus " + removed.getCaseId() + " di indeks " + index);
        return removed;
    }

    public Putusan cariPutusan(String keyword) {
        int steps = 0;
        for (int i = 0; i < daftarPutusan.size(); i++) {
            steps++;
            Putusan p = daftarPutusan.get(i);
            if (p.getCaseId().equalsIgnoreCase(keyword) || p.getNomorPerkara().equalsIgnoreCase(keyword)) {
                System.out.println("Ditemukan pada indeks " + i + " dalam " + steps + " langkah.");
                riwayatAudit.push("CARI: '" + keyword + "' ditemukan (" + steps + " langkah)");
                return p;
            }
        }
        System.out.println("Data '" + keyword + "' tidak ditemukan setelah " + steps + " langkah.");
        riwayatAudit.push("CARI: '" + keyword + "' tidak ditemukan (" + steps + " langkah)");
        return null;
    }

    public DynamicArray<Putusan> getDaftarPutusan() { return daftarPutusan; }
    public CustomStack<String> getRiwayatAudit() { return riwayatAudit; }
}