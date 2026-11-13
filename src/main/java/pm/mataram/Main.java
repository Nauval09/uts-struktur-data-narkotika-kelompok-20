package pm.mataram;

import pm.mataram.service.PutusanService;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== APLIKASI MANAJEMEN PUTUSAN NARKOTIKA PN MATARAM ===");
        PutusanService service = new PutusanService();
        
        // Contoh uji coba membaca dataset CSV dari folder data/
        String pathCsv = "data/putusan-50.csv";
        System.out.println("Memuat data dari: " + pathCsv);
        service.imporCsv(pathCsv);

        System.out.println("Total data termuat: " + service.getDaftarPutusan().size());
        if (service.getDaftarPutusan().size() > 0) {
            System.out.println("Contoh data pertama:");
            System.out.println(service.getDaftarPutusan().get(0));
        }
    }
}