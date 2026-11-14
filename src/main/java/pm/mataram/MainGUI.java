package pm.mataram;

import pm.mataram.model.Putusan;
import pm.mataram.service.PutusanService;
import pm.mataram.structure.CustomStack;
import pm.mataram.structure.DynamicArray;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDate;

public class MainGUI extends JFrame {
    private PutusanService service;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel statusLabel;

    private final String MASTER_CSV = "data/putusan-50.csv";
    private final String BACKUP_CSV = "data/putusan-tersimpan.csv";

    private final Color BG_DARK = new Color(15, 23, 42);
    private final Color BG_PANEL = new Color(30, 41, 59);
    private final Color TEXT_LIGHT = new Color(248, 250, 252);
    private final Color TEXT_MUTED = new Color(148, 163, 184);
    private final Color ACCENT_BLUE = new Color(37, 99, 235);
    private final Color ACCENT_GREEN = new Color(16, 185, 129);
    private final Color ACCENT_AMBER = new Color(217, 119, 6);
    private final Color ACCENT_RED = new Color(239, 68, 68);

    public MainGUI() {
        service = new PutusanService();
        muatDataPintar();

        setTitle("Sistem Manajemen Putusan Narkotika - PN Mataram (UTS Struktur Data)");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(BG_DARK);

        // --- PANEL ATAS ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 12));
        topPanel.setBorder(new EmptyBorder(15, 15, 5, 15));
        topPanel.setBackground(BG_DARK);

        JPanel headerBox = new JPanel(new GridLayout(2, 1));
        headerBox.setOpaque(false);

        JLabel titleLabel = new JLabel("SISTEM MANAJEMEN PUTUSAN NARKOTIKA PN MATARAM", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_LIGHT);

        JLabel subtitleLabel = new JLabel("Implementasi Struktur Data Kustom (DynamicArray & CustomStack)", JLabel.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLabel.setForeground(TEXT_MUTED);

        headerBox.add(titleLabel);
        headerBox.add(subtitleLabel);
        topPanel.add(headerBox, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 6, 8, 8));
        buttonPanel.setOpaque(false);

        JButton btnRefresh = createStyledButton("Tampil Semua", ACCENT_BLUE, Color.WHITE);
        JButton btnCari = createStyledButton("Cari (Langkah)", BG_PANEL, TEXT_LIGHT);
        JButton btnFilter = createStyledButton("Filter (4 Kriteria)", BG_PANEL, TEXT_LIGHT);
        JButton btnUrut = createStyledButton("Urut Vonis", BG_PANEL, TEXT_LIGHT);
        JButton btnAgregasi = createStyledButton("Statistik Agregasi", BG_PANEL, TEXT_LIGHT);
        JButton btnAudit = createStyledButton("Riwayat Audit", BG_PANEL, TEXT_LIGHT);

        JButton btnTambah = createStyledButton("+ Tambah Data", ACCENT_GREEN, Color.WHITE);
        JButton btnUpdate = createStyledButton("Edit Data", ACCENT_AMBER, Color.WHITE);
        JButton btnHapus = createStyledButton("- Hapus Data", ACCENT_RED, Color.WHITE);
        JButton btnEkspor = createStyledButton("Ekspor CSV", BG_PANEL, TEXT_LIGHT);
        JButton btnReset = createStyledButton("Reset Data", new Color(100, 116, 139), Color.WHITE);
        JButton btnKeluar = createStyledButton("Keluar", new Color(71, 85, 105), Color.WHITE);

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnCari);
        buttonPanel.add(btnFilter);
        buttonPanel.add(btnUrut);
        buttonPanel.add(btnAgregasi);
        buttonPanel.add(btnAudit);

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnEkspor);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnKeluar);

        topPanel.add(buttonPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // --- PANEL TENGAH (TABEL) ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(0, 15, 0, 15));
        centerPanel.setBackground(BG_DARK);

        String[] columns = {"Case ID", "Nomor Perkara", "Terdakwa", "Jenis Narkotika", "Berat (gr)", "Pasal Terbukti", "Vonis (bln)", "Denda (Rp)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setBackground(BG_PANEL);
        table.setForeground(TEXT_LIGHT);
        table.setSelectionBackground(new Color(51, 65, 85));
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setGridColor(new Color(51, 65, 85));

        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(51, 65, 85));
        table.getTableHeader().setForeground(TEXT_LIGHT);

        TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(75);
        colModel.getColumn(1).setPreferredWidth(185);
        colModel.getColumn(2).setPreferredWidth(85);
        colModel.getColumn(3).setPreferredWidth(160);
        colModel.getColumn(4).setPreferredWidth(80);
        colModel.getColumn(5).setPreferredWidth(250);
        colModel.getColumn(6).setPreferredWidth(80);
        colModel.getColumn(7).setPreferredWidth(115);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(TEXT_LIGHT);
        centerRenderer.setBackground(BG_PANEL);
        colModel.getColumn(0).setCellRenderer(centerRenderer);
        colModel.getColumn(2).setCellRenderer(centerRenderer);
        colModel.getColumn(6).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        rightRenderer.setForeground(TEXT_LIGHT);
        rightRenderer.setBackground(BG_PANEL);
        colModel.getColumn(4).setCellRenderer(rightRenderer);
        colModel.getColumn(7).setCellRenderer(rightRenderer);

        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
        defaultRenderer.setForeground(TEXT_LIGHT);
        defaultRenderer.setBackground(BG_PANEL);
        colModel.getColumn(1).setCellRenderer(defaultRenderer);
        colModel.getColumn(3).setCellRenderer(defaultRenderer);
        colModel.getColumn(5).setCellRenderer(defaultRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BG_PANEL);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // --- PANEL BAWAH ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(6, 15, 10, 15));
        bottomPanel.setBackground(BG_DARK);

        statusLabel = new JLabel(" Siap | Total data aktif: " + service.getDaftarPutusan().size());
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusLabel.setForeground(TEXT_MUTED);
        bottomPanel.add(statusLabel, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable(service.getDaftarPutusan());

        // --- EVENT LISTENERS ---
        btnRefresh.addActionListener(e -> {
            refreshTable(service.getDaftarPutusan());
            statusLabel.setText(" Menampilkan seluruh data (" + service.getDaftarPutusan().size() + " record)");
        });

        btnCari.addActionListener(e -> aksiCari());
        btnFilter.addActionListener(e -> aksiFilter());
        btnUrut.addActionListener(e -> aksiUrut());
        btnTambah.addActionListener(e -> aksiTambah());
        btnUpdate.addActionListener(e -> aksiUpdate());
        btnHapus.addActionListener(e -> aksiHapus());
        btnAgregasi.addActionListener(e -> aksiAgregasi());
        btnAudit.addActionListener(e -> aksiAudit());
        btnEkspor.addActionListener(e -> aksiEkspor());
        btnReset.addActionListener(e -> aksiReset());
        btnKeluar.addActionListener(e -> System.exit(0));
    }

    private void muatDataPintar() {
        File fileBackup = new File(BACKUP_CSV);
        if (fileBackup.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileBackup))) {
                String line = br.readLine();
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] c = line.split("\\|");
                    if (c.length >= 9) {
                        Putusan p = new Putusan(
                                c[0], c[1], "Pengadilan Negeri Mataram",
                                LocalDate.parse(c[2]), c[3], c[4],
                                Double.parseDouble(c[5]), c[6], c[6],
                                Integer.parseInt(c[7]), Integer.parseInt(c[7]),
                                Long.parseLong(c[8]), "Barang bukti narkotika", "-", "-", "Hal 1-20"
                        );
                        service.tambahPutusan(p);
                    }
                }
                return;
            } catch (Exception ex) {
                System.err.println("Gagal memuat backup, menggunakan master CSV.");
            }
        }
        service.imporCsv(MASTER_CSV);
    }

    private void simpanKeBackup() {
        try (PrintWriter pw = new PrintWriter(new File(BACKUP_CSV))) {
            pw.println("CaseId|NomorPerkara|Tanggal|Terdakwa|JenisNarkotika|Berat|Pasal|Vonis|Denda");
            DynamicArray<Putusan> list = service.getDaftarPutusan();
            for (int i = 0; i < list.size(); i++) {
                Putusan p = list.get(i);
                String tgl = (p.getTanggalPutusan() != null) ? p.getTanggalPutusan().toString() : "2026-01-01";
                pw.println(
                        p.getCaseId() + "|" +
                        p.getNomorPerkara() + "|" +
                        tgl + "|" +
                        p.getTerdakwaKode() + "|" +
                        p.getJenisNarkotika() + "|" +
                        p.getBeratBarangBuktiGram() + "|" +
                        p.getPasalTerbukti() + "|" +
                        p.getVonisBulan() + "|" +
                        p.getDendaRupiah()
                );
            }
        } catch (Exception ex) {
            System.err.println("Gagal menyimpan backup: " + ex.getMessage());
        }
    }

    private void aksiReset() {
        int opsi = JOptionPane.showConfirmDialog(this, "Kembalikan data ke awal (hapus semua data tambahan)?", "Reset", JOptionPane.YES_NO_OPTION);
        if (opsi == JOptionPane.YES_OPTION) {
            File f = new File(BACKUP_CSV);
            if (f.exists()) f.delete();
            service = new PutusanService();
            service.imporCsv(MASTER_CSV);
            refreshTable(service.getDaftarPutusan());
            JOptionPane.showMessageDialog(this, "Data berhasil di-reset ke default awal.");
        }
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(71, 85, 105), 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void refreshTable(DynamicArray<Putusan> data) {
        tableModel.setRowCount(0);
        for (int i = 0; i < data.size(); i++) {
            Putusan p = data.get(i);
            tableModel.addRow(new Object[]{
                    p.getCaseId(),
                    p.getNomorPerkara(),
                    p.getTerdakwaKode(),
                    p.getJenisNarkotika(),
                    p.getBeratBarangBuktiGram(),
                    p.getPasalTerbukti(),
                    p.getVonisBulan(),
                    String.format("%,d", p.getDendaRupiah())
            });
        }
        statusLabel.setText(" Menampilkan: " + data.size() + " record  |  Kapasitas Aktif DynamicArray: " + service.getDaftarPutusan().size() + " record");
    }

    private void aksiCari() {
        String keyword = JOptionPane.showInputDialog(this, "Masukkan Case ID atau Nomor Perkara:");
        if (keyword == null || keyword.trim().isEmpty()) return;

        DynamicArray<Putusan> list = service.getDaftarPutusan();
        int steps = 0;
        Putusan found = null;
        int foundIdx = -1;

        for (int i = 0; i < list.size(); i++) {
            steps++;
            Putusan p = list.get(i);
            if (p.getCaseId().equalsIgnoreCase(keyword.trim()) || p.getNomorPerkara().equalsIgnoreCase(keyword.trim())) {
                found = p;
                foundIdx = i;
                break;
            }
        }

        if (found != null) {
            service.getRiwayatAudit().push("GUI CARI: '" + keyword + "' ditemukan (" + steps + " langkah)");
            DynamicArray<Putusan> hasil = new DynamicArray<>();
            hasil.add(found);
            refreshTable(hasil);
            JOptionPane.showMessageDialog(this, "Ditemukan pada indeks ke-" + foundIdx + " dalam " + steps + " langkah!");
        } else {
            service.getRiwayatAudit().push("GUI CARI: '" + keyword + "' tidak ditemukan (" + steps + " langkah)");
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan setelah " + steps + " langkah.", "Info", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aksiFilter() {
        JDialog dialog = new JDialog(this, "Menu Filter Data", true);
        dialog.setSize(420, 240);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        formPanel.setBackground(BG_PANEL);

        JLabel lblInstruksi = new JLabel("Pilih kriteria filter yang ingin digunakan:");
        lblInstruksi.setForeground(TEXT_LIGHT);
        lblInstruksi.setFont(new Font("SansSerif", Font.BOLD, 12));

        String[] opsiKriteria = {
            "1. Jenis Narkotika (Cth: Sabu, Ganja)",
            "2. Rentang Berat Barang Bukti (gram)",
            "3. Pasal Terbukti (Cth: Pasal 114)",
            "4. Rentang Vonis (bulan)"
        };

        JComboBox<String> comboKriteria = new JComboBox<>(opsiKriteria);
        comboKriteria.setFont(new Font("SansSerif", Font.PLAIN, 12));
        comboKriteria.setBackground(BG_DARK);
        comboKriteria.setForeground(Color.WHITE);

        formPanel.add(lblInstruksi);
        formPanel.add(comboKriteria);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(BG_DARK);

        JButton btnLanjut = new JButton("Lanjutkan");
        btnLanjut.setBackground(ACCENT_BLUE);
        btnLanjut.setForeground(Color.WHITE);
        btnLanjut.setFocusPainted(false);

        JButton btnBatal = new JButton("Batal");
        btnBatal.setBackground(new Color(71, 85, 105));
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setFocusPainted(false);

        btnLanjut.addActionListener(e -> {
            String pilihan = (String) comboKriteria.getSelectedItem();
            dialog.dispose();
            bukaFormInputFilter(pilihan);
        });

        btnBatal.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnLanjut);
        btnPanel.add(btnBatal);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void bukaFormInputFilter(String pilihan) {
        if (pilihan == null) return;

        JDialog dialog = new JDialog(this, "Filter: " + pilihan.substring(3), true);
        dialog.setSize(420, 230);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(BG_PANEL);

        JLabel lblInput1 = new JLabel();
        lblInput1.setForeground(TEXT_LIGHT);
        lblInput1.setFont(new Font("SansSerif", Font.BOLD, 12));

        JTextField field1 = new JTextField();
        field1.setBackground(BG_DARK);
        field1.setForeground(Color.WHITE);
        field1.setCaretColor(Color.WHITE);

        JLabel lblInput2 = new JLabel();
        lblInput2.setForeground(TEXT_LIGHT);
        lblInput2.setFont(new Font("SansSerif", Font.BOLD, 12));

        JTextField field2 = new JTextField();
        field2.setBackground(BG_DARK);
        field2.setForeground(Color.WHITE);
        field2.setCaretColor(Color.WHITE);

        if (pilihan.startsWith("1")) {
            lblInput1.setText("Jenis Narkotika:");
            formPanel.add(lblInput1);
            formPanel.add(field1);
            lblInput2.setVisible(false);
            field2.setVisible(false);
        } else if (pilihan.startsWith("3")) {
            lblInput1.setText("Pasal Terbukti:");
            formPanel.add(lblInput1);
            formPanel.add(field1);
            lblInput2.setVisible(false);
            field2.setVisible(false);
        } else {
            lblInput1.setText(pilihan.startsWith("2") ? "Berat Min (gram):" : "Vonis Min (bulan):");
            lblInput2.setText(pilihan.startsWith("2") ? "Berat Max (gram):" : "Vonis Max (bulan):");
            formPanel.add(lblInput1);
            formPanel.add(field1);
            formPanel.add(lblInput2);
            formPanel.add(field2);
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(BG_DARK);

        JButton btnTerapkan = new JButton("Terapkan Filter");
        btnTerapkan.setBackground(ACCENT_BLUE);
        btnTerapkan.setForeground(Color.WHITE);
        btnTerapkan.setFocusPainted(false);

        JButton btnBatal = new JButton("Kembali");
        btnBatal.setBackground(new Color(71, 85, 105));
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setFocusPainted(false);

        btnTerapkan.addActionListener(e -> {
            DynamicArray<Putusan> list = service.getDaftarPutusan();
            DynamicArray<Putusan> hasil = new DynamicArray<>();

            try {
                if (pilihan.startsWith("1")) {
                    String keyword = field1.getText().trim().toLowerCase();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getJenisNarkotika().toLowerCase().contains(keyword)) {
                            hasil.add(list.get(i));
                        }
                    }
                } else if (pilihan.startsWith("3")) {
                    String keyword = field1.getText().trim().toLowerCase();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getPasalTerbukti().toLowerCase().contains(keyword)) {
                            hasil.add(list.get(i));
                        }
                    }
                } else if (pilihan.startsWith("2")) {
                    double min = Double.parseDouble(field1.getText().trim());
                    double max = Double.parseDouble(field2.getText().trim());
                    for (int i = 0; i < list.size(); i++) {
                        double val = list.get(i).getBeratBarangBuktiGram();
                        if (val >= min && val <= max) hasil.add(list.get(i));
                    }
                } else if (pilihan.startsWith("4")) {
                    int min = Integer.parseInt(field1.getText().trim());
                    int max = Integer.parseInt(field2.getText().trim());
                    for (int i = 0; i < list.size(); i++) {
                        int val = list.get(i).getVonisBulan();
                        if (val >= min && val <= max) hasil.add(list.get(i));
                    }
                }

                simpanKeBackup();
                service.getRiwayatAudit().push("GUI FILTER: " + pilihan.substring(3) + " -> " + hasil.size() + " data ditemukan");
                refreshTable(hasil);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Format input atau angka tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBatal.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnTerapkan);
        btnPanel.add(btnBatal);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void aksiUrut() {
        JDialog dialog = new JDialog(this, "Urutkan Berdasarkan Vonis", true);
        dialog.setSize(440, 270);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        formPanel.setBackground(BG_PANEL);

        JLabel lblInstruksi = new JLabel("Pilih metode pengurutan masa vonis (bulan):");
        lblInstruksi.setForeground(TEXT_LIGHT);
        lblInstruksi.setFont(new Font("SansSerif", Font.BOLD, 12));
        formPanel.add(lblInstruksi, BorderLayout.NORTH);

        JPanel btnChoicePanel = new JPanel(new GridLayout(2, 1, 0, 12));
        btnChoicePanel.setOpaque(false);

        // Membuat tombol dengan sudut lengkung (Rounded Button style)
        JButton btnAsc = createRoundedButton("Ascending (Terpendek ➔ Terlama)", ACCENT_AMBER, Color.WHITE);
        JButton btnDesc = createRoundedButton("Descending (Terlama ➔ Terpendek)", ACCENT_AMBER, Color.WHITE);

        btnChoicePanel.add(btnAsc);
        btnChoicePanel.add(btnDesc);
        formPanel.add(btnChoicePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(BG_DARK);

        JButton btnBatal = new JButton("Batal");
        btnBatal.setBackground(new Color(71, 85, 105));
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setFocusPainted(false);
        btnBatal.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        btnBatal.addActionListener(e -> dialog.dispose());

        bottomPanel.add(btnBatal);

        btnAsc.addActionListener(e -> {
            dialog.dispose();
            eksekusiUrut(0);
        });

        btnDesc.addActionListener(e -> {
            dialog.dispose();
            eksekusiUrut(1);
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Helper method untuk membuat tombol dengan sudut lengkung halus (Rounded)
    private JButton createRoundedButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                // Menggambar kotak dengan sudut melengkung (arc width & height = 16)
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void eksekusiUrut(int pilih) {
        DynamicArray<Putusan> list = service.getDaftarPutusan();
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Putusan p1 = list.get(j);
                Putusan p2 = list.get(j + 1);
                boolean tukar = (pilih == 0) ? (p1.getVonisBulan() > p2.getVonisBulan()) : (p1.getVonisBulan() < p2.getVonisBulan());
                if (tukar) {
                    list.set(j, p2);
                    list.set(j + 1, p1);
                }
            }
        }
        simpanKeBackup();
        service.getRiwayatAudit().push("GUI URUT: Vonis " + (pilih == 0 ? "Ascending" : "Descending"));
        refreshTable(list);
    }

    private void aksiTambah() {
        JDialog dialog = new JDialog(this, "Tambah Putusan Baru", true);
        dialog.setSize(450, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 8, 8));
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        formPanel.setBackground(BG_PANEL);

        JTextField fId = new JTextField("PUT-051");
        JTextField fNo = new JTextField("407/Pid.Sus/2026/PN Mtr");
        JTextField fTgl = new JTextField("2026-10-15");
        JTextField fTrd = new JTextField("TRD-051");
        JTextField fJenis = new JTextField("Sabu");
        JTextField fBerat = new JTextField("1.25");
        JTextField fPasal = new JTextField("Pasal 114 ayat (1)");
        JTextField fVonis = new JTextField("60");
        JTextField fDenda = new JTextField("1000000000");

        JTextField[] fields = {fId, fNo, fTgl, fTrd, fJenis, fBerat, fPasal, fVonis, fDenda};
        String[] labels = {"Case ID:", "Nomor Perkara:", "Tanggal (YYYY-MM-DD):", "Kode Terdakwa:", "Jenis Narkotika:", "Berat BB (gram):", "Pasal Terbukti:", "Vonis (bulan):", "Denda (Rp):"};

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(TEXT_LIGHT);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            formPanel.add(lbl);

            fields[i].setBackground(BG_DARK);
            fields[i].setForeground(Color.WHITE);
            fields[i].setCaretColor(Color.WHITE);
            formPanel.add(fields[i]);
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(BG_DARK);

        JButton btnSave = new JButton("Simpan");
        btnSave.setBackground(ACCENT_GREEN);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);

        JButton btnCancel = new JButton("Batal");
        btnCancel.setBackground(new Color(71, 85, 105));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        btnSave.addActionListener(e -> {
            try {
                Putusan p = new Putusan(
                        fId.getText().trim(), fNo.getText().trim(), "Pengadilan Negeri Mataram",
                        LocalDate.parse(fTgl.getText().trim()), fTrd.getText().trim(), fJenis.getText().trim(),
                        Double.parseDouble(fBerat.getText().trim()), fPasal.getText().trim(), fPasal.getText().trim(),
                        Integer.parseInt(fVonis.getText().trim()), Integer.parseInt(fVonis.getText().trim()),
                        Long.parseLong(fDenda.getText().trim()), "Barang bukti narkotika", "-", "-", "Hal 1-20"
                );
                service.tambahPutusan(p);
                simpanKeBackup();
                refreshTable(service.getDaftarPutusan());
                JOptionPane.showMessageDialog(dialog, "Berhasil menambahkan " + p.getCaseId() + " (Tersimpan Permanen!)");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Format input salah: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void aksiUpdate() {
        int selectedRow = table.getSelectedRow();
        String targetCaseId = "";

        if (selectedRow != -1) {
            targetCaseId = tableModel.getValueAt(selectedRow, 0).toString();
        } else {
            targetCaseId = JOptionPane.showInputDialog(this, "Silakan masukkan Case ID yang ingin diedit:", "Edit Data", JOptionPane.QUESTION_MESSAGE);
            if (targetCaseId == null || targetCaseId.trim().isEmpty()) return;
        }

        DynamicArray<Putusan> list = service.getDaftarPutusan();
        int targetIdx = -1;
        Putusan targetPutusan = null;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCaseId().equalsIgnoreCase(targetCaseId.trim())) {
                targetIdx = i;
                targetPutusan = list.get(i);
                break;
            }
        }

        if (targetPutusan == null) {
            JOptionPane.showMessageDialog(this, "Data dengan Case ID '" + targetCaseId + "' tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Putusan: " + targetPutusan.getCaseId(), true);
        dialog.setSize(450, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 8, 8));
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        formPanel.setBackground(BG_PANEL);

        JTextField fId = new JTextField(targetPutusan.getCaseId());
        fId.setEditable(false);
        JTextField fNo = new JTextField(targetPutusan.getNomorPerkara());
        JTextField fTgl = new JTextField(targetPutusan.getTanggalPutusan() != null ? targetPutusan.getTanggalPutusan().toString() : "2026-10-15");
        JTextField fTrd = new JTextField(targetPutusan.getTerdakwaKode());
        JTextField fJenis = new JTextField(targetPutusan.getJenisNarkotika());
        JTextField fBerat = new JTextField(String.valueOf(targetPutusan.getBeratBarangBuktiGram()));
        JTextField fPasal = new JTextField(targetPutusan.getPasalTerbukti());
        JTextField fVonis = new JTextField(String.valueOf(targetPutusan.getVonisBulan()));
        JTextField fDenda = new JTextField(String.valueOf(targetPutusan.getDendaRupiah()));

        JTextField[] fields = {fId, fNo, fTgl, fTrd, fJenis, fBerat, fPasal, fVonis, fDenda};
        String[] labels = {"Case ID (Locked):", "Nomor Perkara:", "Tanggal (YYYY-MM-DD):", "Kode Terdakwa:", "Jenis Narkotika:", "Berat BB (gram):", "Pasal Terbukti:", "Vonis (bulan):", "Denda (Rp):"};

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(TEXT_LIGHT);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            formPanel.add(lbl);

            fields[i].setBackground(BG_DARK);
            fields[i].setForeground(Color.WHITE);
            fields[i].setCaretColor(Color.WHITE);
            formPanel.add(fields[i]);
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(BG_DARK);

        JButton btnSave = new JButton("Simpan Perubahan");
        btnSave.setBackground(ACCENT_AMBER);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);

        JButton btnCancel = new JButton("Batal");
        btnCancel.setBackground(new Color(71, 85, 105));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        final Putusan finalTargetPutusan = targetPutusan;
        final int idxToUpdate = targetIdx;

        btnSave.addActionListener(e -> {
            try {
                Putusan updatedP = new Putusan(
                        fId.getText().trim(), fNo.getText().trim(), "Pengadilan Negeri Mataram",
                        LocalDate.parse(fTgl.getText().trim()), fTrd.getText().trim(), fJenis.getText().trim(),
                        Double.parseDouble(fBerat.getText().trim()), fPasal.getText().trim(), fPasal.getText().trim(),
                        Integer.parseInt(fVonis.getText().trim()), Integer.parseInt(fVonis.getText().trim()),
                        Long.parseLong(fDenda.getText().trim()), "Barang bukti narkotika", "-", "-", "Hal 1-20"
                );

                list.set(idxToUpdate, updatedP);
                simpanKeBackup();
                service.getRiwayatAudit().push("GUI UPDATE: Berhasil mengubah data " + finalTargetPutusan.getCaseId());

                refreshTable(service.getDaftarPutusan());
                JOptionPane.showMessageDialog(dialog, "Perubahan data " + finalTargetPutusan.getCaseId() + " berhasil disimpan permanen!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Format input update salah: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void aksiHapus() {
        int selectedRow = table.getSelectedRow();
        String defaultVal = (selectedRow != -1) ? tableModel.getValueAt(selectedRow, 0).toString() : "";

        String keyword = JOptionPane.showInputDialog(this, "Masukkan Case ID / No Perkara yang akan dihapus:", defaultVal);
        if (keyword == null || keyword.trim().isEmpty()) return;

        DynamicArray<Putusan> list = service.getDaftarPutusan();
        int targetIdx = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCaseId().equalsIgnoreCase(keyword.trim()) || list.get(i).getNomorPerkara().equalsIgnoreCase(keyword.trim())) {
                targetIdx = i;
                break;
            }
        }

        if (targetIdx != -1) {
            Putusan removed = service.hapusPutusan(targetIdx);
            simpanKeBackup();
            refreshTable(list);
            JOptionPane.showMessageDialog(this, "Berhasil menghapus: " + removed.getCaseId() + " (Tersimpan Permanen!)");
        } else {
            JOptionPane.showMessageDialog(this, "Data '" + keyword + "' tidak ditemukan.", "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aksiAgregasi() {
        DynamicArray<Putusan> list = service.getDaftarPutusan();
        int total = list.size();
        if (total == 0) return;

        long totalVonis = 0, totalDenda = 0;
        double totalBerat = 0;
        for (int i = 0; i < total; i++) {
            totalVonis += list.get(i).getVonisBulan();
            totalDenda += list.get(i).getDendaRupiah();
            totalBerat += list.get(i).getBeratBarangBuktiGram();
        }

        String msg = String.format(
                "Total Perkara : %d kasus\nRata-rata Vonis : %.2f bulan\nRata-rata Berat BB : %.3f gram\nTotal Akumulasi Denda : Rp %,d",
                total, (double) totalVonis / total, totalBerat / total, totalDenda
        );
        JOptionPane.showMessageDialog(this, msg, "Statistik Agregasi Putusan", JOptionPane.INFORMATION_MESSAGE);
    }

    private void aksiAudit() {
        CustomStack<String> stack = service.getRiwayatAudit();
        if (stack.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Riwayat audit masih kosong.");
            return;
        }

        StringBuilder sb = new StringBuilder("Riwayat Operasi (LIFO - Terbaru di Atas):\n\n");
        CustomStack<String> temp = new CustomStack<>(stack.size());
        int no = 1;
        while (!stack.isEmpty()) {
            String item = stack.pop();
            sb.append(no++).append(". ").append(item).append("\n");
            temp.push(item);
        }
        while (!temp.isEmpty()) {
            stack.push(temp.pop());
        }

        JTextArea textArea = new JTextArea(sb.toString(), 15, 45);
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Riwayat Audit (CustomStack)", JOptionPane.INFORMATION_MESSAGE);
    }

    private void aksiEkspor() {
        String filename = JOptionPane.showInputDialog(this, "Nama file ekspor di folder data/:", "laporan-gui.csv");
        if (filename == null || filename.trim().isEmpty()) return;

        String path = "data/" + filename.trim();
        try (PrintWriter pw = new PrintWriter(new File(path))) {
            pw.println("CaseId,NomorPerkara,Pengadilan,JenisNarkotika,BeratBarangBuktiGram,PasalTerbukti,VonisBulan,DendaRupiah");
            DynamicArray<Putusan> list = service.getDaftarPutusan();
            for (int i = 0; i < list.size(); i++) {
                Putusan p = list.get(i);
                pw.println(p.getCaseId() + "," + p.getNomorPerkara() + "," + p.getPengadilan() + "," +
                        p.getJenisNarkotika() + "," + p.getBeratBarangBuktiGram() + "," +
                        p.getPasalTerbukti() + "," + p.getVonisBulan() + "," + p.getDendaRupiah());
            }
            service.getRiwayatAudit().push("GUI EKSPOR: Berhasil ke " + path);
            JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke " + path);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal ekspor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }
}