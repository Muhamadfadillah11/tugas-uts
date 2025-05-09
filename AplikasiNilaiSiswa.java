import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Kelas Siswa untuk menyimpan data siswa
class Siswa {
    private String nama;
    private String nim;
    private int nilaiMatematika;
    private int nilaiBahasaIndonesia;
    private int nilaiIPA;
    private double nilaiAkhir;

    // Constructor
    public Siswa(String nama, String nim, int nilaiMatematika, int nilaiBahasaIndonesia, int nilaiIPA) {
        this.nama = nama;
        this.nim = nim;
        this.nilaiMatematika = nilaiMatematika;
        this.nilaiBahasaIndonesia = nilaiBahasaIndonesia;
        this.nilaiIPA = nilaiIPA;
        // Hitung nilai akhir (rata-rata dari 3 nilai)
        this.nilaiAkhir = (nilaiMatematika + nilaiBahasaIndonesia + nilaiIPA) / 3.0;
    }

    // Getter methods
    public String getNama() { return nama; }
    public String getNim() { return nim; }
    public int getNilaiMatematika() { return nilaiMatematika; }
    public int getNilaiBahasaIndonesia() { return nilaiBahasaIndonesia; }
    public int getNilaiIPA() { return nilaiIPA; }
    public double getNilaiAkhir() { return nilaiAkhir; }

    // Format data untuk disimpan ke file
    @Override
    public String toString() {
        return nama + "," + nim + "," + nilaiMatematika + "," + nilaiBahasaIndonesia + "," + nilaiIPA;
    }
}

// Aplikasi utama
public class AplikasiNilaiSiswa extends JFrame {
    // Data siswa
    private ArrayList<Siswa> daftarSiswa = new ArrayList<>();
    
    // Komponen UI
    private JTable tableSiswa;
    private DefaultTableModel tableModel;
    private JTextField txtNama, txtNIM, txtMatematika, txtBahasaIndonesia, txtIPA, txtCari;
    private JLabel lblRataRata = new JLabel("Rata-rata: -");

    public AplikasiNilaiSiswa() {
        // Setup frame
        setTitle("Aplikasi Nilai Siswa");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Panel untuk input data
        JPanel panelInput = new JPanel(new GridLayout(6, 2, 5, 5));
        panelInput.setBorder(BorderFactory.createTitledBorder("Input Data"));
        
        panelInput.add(new JLabel("Nama:"));
        txtNama = new JTextField();
        panelInput.add(txtNama);
        
        panelInput.add(new JLabel("NIM:"));
        txtNIM = new JTextField();
        panelInput.add(txtNIM);
        
        panelInput.add(new JLabel("Nilai Matematika:"));
        txtMatematika = new JTextField();
        panelInput.add(txtMatematika);
        
        panelInput.add(new JLabel("Nilai B. Indonesia:"));
        txtBahasaIndonesia = new JTextField();
        panelInput.add(txtBahasaIndonesia);
        
        panelInput.add(new JLabel("Nilai IPA:"));
        txtIPA = new JTextField();
        panelInput.add(txtIPA);
        
        JButton btnTambah = new JButton("Tambah Siswa");
        btnTambah.addActionListener(_ -> tambahSiswa());
        panelInput.add(btnTambah);
        
        // Panel untuk operasi
        JPanel panelOperasi = new JPanel(new FlowLayout());
        
        JButton btnSimpan = new JButton("Simpan ke File");
        btnSimpan.addActionListener(_ -> simpanKeFile());
        panelOperasi.add(btnSimpan);
        
        JButton btnBaca = new JButton("Baca dari File");
        btnBaca.addActionListener(_ -> bacaDariFile());
        panelOperasi.add(btnBaca);
        
        JButton btnUrut = new JButton("Urutkan Nilai");
        btnUrut.addActionListener(_ -> urutkanSiswa());
        panelOperasi.add(btnUrut);
        
        JButton btnHitung = new JButton("Hitung Rata-rata");
        btnHitung.addActionListener(_ -> hitungRataRata());
        panelOperasi.add(btnHitung);
        
        // Panel untuk pencarian
        JPanel panelCari = new JPanel(new FlowLayout());
        panelCari.add(new JLabel("Cari Siswa:"));
        txtCari = new JTextField(15);
        panelCari.add(txtCari);
        
        JButton btnCari = new JButton("Cari");
        btnCari.addActionListener(_ -> cariSiswa());
        panelCari.add(btnCari);
        
        // Panel untuk menampilkan info
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.add(lblRataRata);
        
        // Panel untuk kontrol (gabungan input, operasi, cari)
        JPanel panelKontrol = new JPanel(new BorderLayout());
        panelKontrol.add(panelInput, BorderLayout.NORTH);
        panelKontrol.add(panelOperasi, BorderLayout.CENTER);
        panelKontrol.add(panelCari, BorderLayout.SOUTH);
        
        // Setup tabel
        tableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Nama", "NIM", "Matematika", "B. Indonesia", "IPA", "Nilai Akhir"}
        );
        tableSiswa = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableSiswa);
        
        // Tambahkan ke frame
        add(panelKontrol, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInfo, BorderLayout.SOUTH);
    }
    
    // Tambah siswa baru
    private void tambahSiswa() {
        try {
            String nama = txtNama.getText().trim();
            String nim = txtNIM.getText().trim();
            int nilaiMatematika = Integer.parseInt(txtMatematika.getText().trim());
            int nilaiBahasaIndonesia = Integer.parseInt(txtBahasaIndonesia.getText().trim());
            int nilaiIPA = Integer.parseInt(txtIPA.getText().trim());
            
            // Validasi
            if (nama.isEmpty() || nim.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama dan NIM tidak boleh kosong");
                return;
            }
            
            if (nilaiMatematika < 0 || nilaiMatematika > 100 ||
                nilaiBahasaIndonesia < 0 || nilaiBahasaIndonesia > 100 ||
                nilaiIPA < 0 || nilaiIPA > 100) {
                JOptionPane.showMessageDialog(this, "Nilai harus antara 0-100");
                return;
            }
            
            // Tambah siswa baru
            Siswa siswa = new Siswa(nama, nim, nilaiMatematika, nilaiBahasaIndonesia, nilaiIPA);
            daftarSiswa.add(siswa);
            
            // Reset field
            txtNama.setText("");
            txtNIM.setText("");
            txtMatematika.setText("");
            txtBahasaIndonesia.setText("");
            txtIPA.setText("");
            
            // Update tabel
            updateTabel();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input nilai harus berupa angka");
        }
    }
    
    // Simpan ke file
    private void simpanKeFile() {
        try {
            String namaFile = JOptionPane.showInputDialog(this, "Masukkan nama file:");
            if (namaFile != null && !namaFile.isEmpty()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(namaFile));
                for (Siswa siswa : daftarSiswa) {
                    writer.write(siswa.toString());
                    writer.newLine();
                }
                writer.close();
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error menyimpan file: " + e.getMessage());
        }
    }
    
    // Baca dari file
    private void bacaDariFile() {
        try {
            String namaFile = JOptionPane.showInputDialog(this, "Masukkan nama file:");
            if (namaFile != null && !namaFile.isEmpty()) {
                daftarSiswa.clear();
                BufferedReader reader = new BufferedReader(new FileReader(namaFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 5) {
                        String nama = data[0].trim();
                        String nim = data[1].trim();
                        int nilaiMatematika = Integer.parseInt(data[2].trim());
                        int nilaiBahasaIndonesia = Integer.parseInt(data[3].trim());
                        int nilaiIPA = Integer.parseInt(data[4].trim());
                        
                        Siswa siswa = new Siswa(nama, nim, nilaiMatematika, nilaiBahasaIndonesia, nilaiIPA);
                        daftarSiswa.add(siswa);
                    }
                }
                reader.close();
                updateTabel();
                JOptionPane.showMessageDialog(this, "Data berhasil dimuat");
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error membaca file: " + e.getMessage());
        }
    }
    
    // Urutkan siswa berdasarkan nilai akhir (descending)
    private void urutkanSiswa() {
        Collections.sort(daftarSiswa, (s1, s2) -> 
            Double.compare(s2.getNilaiAkhir(), s1.getNilaiAkhir()));
        updateTabel();
    }
    
    // Cari siswa menggunakan binary search
    private void cariSiswa() {
        String namaCari = txtCari.getText().trim();
        if (namaCari.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan nama untuk dicari");
            return;
        }
        
        // Sort dulu untuk binary search
        ArrayList<Siswa> sortedList = new ArrayList<>(daftarSiswa);
        Collections.sort(sortedList, Comparator.comparing(Siswa::getNama));
        
        // Binary search
        Siswa hasilCari = binarySearchByName(sortedList, namaCari, 0, sortedList.size() - 1);
        
        if (hasilCari != null) {
            JOptionPane.showMessageDialog(this, "Siswa ditemukan:\nNama: " + hasilCari.getNama() + 
                "\nNIM: " + hasilCari.getNim() + "\nNilai Akhir: " + hasilCari.getNilaiAkhir());
        } else {
            JOptionPane.showMessageDialog(this, "Siswa tidak ditemukan");
        }
    }
    
    // Implementasi binary search
    private Siswa binarySearchByName(ArrayList<Siswa> list, String name, int low, int high) {
        if (low > high) return null;
        
        int mid = low + (high - low) / 2;
        int comparison = list.get(mid).getNama().compareToIgnoreCase(name);
        
        if (comparison == 0) return list.get(mid);
        if (comparison > 0) return binarySearchByName(list, name, low, mid - 1);
        return binarySearchByName(list, name, mid + 1, high);
    }
    
    // Hitung rata-rata menggunakan rekursi
    private void hitungRataRata() {
        double rataRata = hitungRataRataRekursif(0, 0, 0);
        lblRataRata.setText("Rata-rata: " + String.format("%.2f", rataRata));
    }
    
    // Implementasi rekursi untuk menghitung rata-rata
    private double hitungRataRataRekursif(int index, double total, int count) {
        // Basis: semua siswa sudah dihitung
        if (index >= daftarSiswa.size()) {
            return count > 0 ? total / count : 0;
        }
        
        // Rekursi: tambahkan nilai siswa saat ini, lanjut ke siswa berikutnya
        return hitungRataRataRekursif(
            index + 1, 
            total + daftarSiswa.get(index).getNilaiAkhir(), 
            count + 1
        );
    }
    
    // Update tabel
    private void updateTabel() {
        // Hapus semua baris
        tableModel.setRowCount(0);
        
        // Tambahkan data terbaru
        for (Siswa siswa : daftarSiswa) {
            tableModel.addRow(new Object[] {
                siswa.getNama(),
                siswa.getNim(),
                siswa.getNilaiMatematika(),
                siswa.getNilaiBahasaIndonesia(),
                siswa.getNilaiIPA(),
                String.format("%.2f", siswa.getNilaiAkhir())
            });
        }
    }
    
    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AplikasiNilaiSiswa().setVisible(true);
        });
    }
}