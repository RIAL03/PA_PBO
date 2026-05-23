package org.example.pa_pbo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.Optional;

public class HelloController {

    @FXML private TextField fldNama;
    @FXML private TextField fldId;
    @FXML private ComboBox<String> comboTipe;
    @FXML private TextField fldBiaya;
    @FXML private TextField fldLoker;
    @FXML private TextArea txtAreaOutput;

    private ArrayList<Member> listMember = new ArrayList<>();

    @FXML
    public void initialize() {
        comboTipe.getItems().addAll("Reguler", "VIP");

        // INTERAKTIVITAS: Deteksi perubahan pada ComboBox Tipe
        comboTipe.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Reguler".equals(newVal)) {
                fldBiaya.setDisable(false);
                fldLoker.setDisable(true);
                fldLoker.clear(); // Bersihkan isi jika ada
            } else if ("VIP".equals(newVal)) {
                fldBiaya.setDisable(true);
                fldBiaya.clear();
                fldLoker.setDisable(false);
            }
        });

        comboTipe.setValue("Reguler"); // Default
    }

    @FXML
    protected void onTambahClick() {
        String nama = fldNama.getText();
        String id = fldId.getText();
        String tipe = comboTipe.getValue();

        if (nama.isEmpty() || id.isEmpty()) {
            txtAreaOutput.setText("Gagal: Nama dan ID tidak boleh kosong!");
            return;
        }

        try {
            if (tipe.equals("Reguler")) {
                int biaya = Integer.parseInt(fldBiaya.getText());
                listMember.add(new MemberReguler(nama, id, biaya));
                txtAreaOutput.setText("Berhasil menambah Member Reguler: " + nama);
            } else {
                String loker = fldLoker.getText();
                if(loker.isEmpty()) throw new Exception("Loker kosong");
                listMember.add(new MemberVIP(nama, id, loker));
                txtAreaOutput.setText("Berhasil menambah Member VIP: " + nama);
            }
            // Bersihkan kolom form pendaftaran setelah ditambah
            fldNama.clear(); fldId.clear(); fldBiaya.clear(); fldLoker.clear();
            fldNama.requestFocus(); // Kembalikan kursor ke kolom nama
        } catch (NumberFormatException e) {
            txtAreaOutput.setText("Gagal: Biaya harus berupa angka valid!");
        } catch (Exception e) {
            txtAreaOutput.setText("Gagal: Pastikan semua data terisi dengan benar!");
        }
    }

    @FXML
    protected void onLihatClick() {
        if (listMember.isEmpty()) {
            txtAreaOutput.setText("Data member masih kosong!");
            return;
        }

        // Tentukan panjang minimum berdasarkan panjang karakter teks Header
        int maxNo = "No".length();
        int maxId = "ID".length();
        int maxNama = "Nama".length();
        int maxTipe = "Tipe".length();
        int maxKet = "Keterangan Utama".length();
        int maxFasilitas = "Fasilitas Tambahan".length();
        int maxBonus = "Bonus Bulanan".length();

        // Hitung panjang maksimum tiap kolom secara dinamis berdasarkan data nyata
        for (Member m : listMember) {
            maxId = Math.max(maxId, m.getID().length());
            maxNama = Math.max(maxNama, m.nama.length());
            maxTipe = Math.max(maxTipe, m.getTipe().length());

            // Ambil bagian keterangan utama (Biaya / Loker) dari getInfo()
            String[] infoParts = m.getInfo().split(" \\| ");
            String ketUtama = infoParts[infoParts.length - 1];
            maxKet = Math.max(maxKet, ketUtama.length());

            String fasilitas = "Hanya Latihan";
            String bonus = "Tidak Anda";

            // Tetap menggunakan pilar Polymorphism & Abstraction
            if (m instanceof LayananTambahan) {
                LayananTambahan layanan = (LayananTambahan) m;
                fasilitas = layanan.ambilFasilitas();
                bonus = layanan.ambilBonusSuplemen();
            }
            maxFasilitas = Math.max(maxFasilitas, fasilitas.length());
            maxBonus = Math.max(maxBonus, bonus.length());
        }

        // Buat Format String dinamis sesuai dengan hasil perhitungan panjang maksimum
        String formatHeader = "%-" + maxNo + "s | %-" + maxId + "s | %-" + maxNama + "s | %-" + maxTipe + "s | %-" + maxKet + "s | %-" + maxFasilitas + "s | %-" + maxBonus + "s\n";
        String formatData   = "%-" + maxNo + "d | %-" + maxId + "s | %-" + maxNama + "s | %-" + maxTipe + "s | %-" + maxKet + "s | %-" + maxFasilitas + "s | %-" + maxBonus + "s\n";

        StringBuilder hasil = new StringBuilder();

        // Cetak Header Tabel
        hasil.append(String.format(formatHeader, "No", "ID", "Nama", "Tipe", "Keterangan Utama", "Fasilitas Tambahan", "Bonus Bulanan"));

        // Hitung total panjang baris untuk membuat garis pembatas '=' yang pas dan presisi
        int totalLebarGaris = maxNo + maxId + maxNama + maxTipe + maxKet + maxFasilitas + maxBonus + (6 * 3);
        hasil.append("=".repeat(totalLebarGaris)).append("\n");

        // Cetak setiap baris data member ke dalam tabel
        for (int i = 0; i < listMember.size(); i++) {
            Member m = listMember.get(i);

            String[] infoParts = m.getInfo().split(" \\| ");
            String ketUtama = infoParts[infoParts.length - 1];

            String fasilitas = "Hanya Latihan";
            String bonus = "Tidak Ada";
            if (m instanceof LayananTambahan) {
                LayananTambahan layanan = (LayananTambahan) m;
                fasilitas = layanan.ambilFasilitas();
                bonus = layanan.ambilBonusSuplemen();
            }

            // Gabungkan ke dalam output tabel
            hasil.append(String.format(formatData, (i + 1), m.getID(), m.nama, m.getTipe(), ketUtama, fasilitas, bonus));
        }

        txtAreaOutput.setText(hasil.toString());
    }

    @FXML
    protected void onHapusClick() {
        // Menggunakan TextInputDialog agar tidak memakai TextField pendaftaran
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Hapus Member");
        dialog.setHeaderText("Hapus Data Member");
        dialog.setContentText("Masukkan ID Member yang ingin dihapus:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(idHapus -> {
            boolean ditemukan = false;
            for (int i = 0; i < listMember.size(); i++) {
                if (listMember.get(i).getID().equals(idHapus)) {
                    ditemukan = true;
                    // Tampilkan konfirmasi bahaya sebelum menghapus
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Konfirmasi Hapus");
                    confirm.setHeaderText("Anda yakin ingin menghapus " + listMember.get(i).nama + "?");

                    if (confirm.showAndWait().get() == ButtonType.OK) {
                        listMember.remove(i);
                        txtAreaOutput.setText("Member dengan ID " + idHapus + " berhasil dihapus.");
                    } else {
                        txtAreaOutput.setText("Penghapusan dibatalkan.");
                    }
                    break;
                }
            }

            if (!ditemukan) {
                txtAreaOutput.setText("Member dengan ID " + idHapus + " tidak ditemukan!");
            }
        });
    }

    @FXML
    protected void onAbsenClick() {
        // Menggunakan pop-up dialog untuk absen
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Absen Masuk");
        dialog.setHeaderText("Sistem Kehadiran Fitness");
        dialog.setContentText("Masukkan ID Member Anda:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(idAbsen -> {
            boolean ditemukan = false;
            for (Member m : listMember) {
                if (m.getID().equals(idAbsen)) {
                    String pesan = "✅ AKSES DITERIMA!\n";
                    pesan += m.getInfo("Selamat Latihan");
                    txtAreaOutput.setText(pesan);
                    ditemukan = true;
                    break;
                }
            }
            if (!ditemukan) {
                txtAreaOutput.setText("❌ AKSES DITOLAK!\nMember dengan ID '" + idAbsen + "' belum terdaftar.");
            }
        });
    }

    @FXML
    protected void onEditClick() {
        if (listMember.isEmpty()) {
            txtAreaOutput.setText("Data member masih kosong!");
            return;
        }

        // 1) Input ID yang mau diedit
        TextInputDialog dialogId = new TextInputDialog();
        dialogId.setTitle("Edit Member");
        dialogId.setHeaderText("Edit Data Member");
        dialogId.setContentText("Masukkan ID Member yang ingin diedit:");

        Optional<String> resultId = dialogId.showAndWait();
        if (resultId.isEmpty()) return;

        String idEdit = resultId.get().trim();
        if (idEdit.isEmpty()) {
            txtAreaOutput.setText("Gagal: ID tidak boleh kosong.");
            return;
        }

        // 2) Cari member berdasarkan ID
        Member target = null;
        for (Member m : listMember) {
            if (m.getID().equals(idEdit)) {
                target = m;
                break;
            }
        }

        if (target == null) {
            txtAreaOutput.setText("Member dengan ID " + idEdit + " tidak ditemukan!");
            return;
        }

        // 3) Input Nama baru (opsional)
        TextInputDialog dialogNama = new TextInputDialog();
        dialogNama.setTitle("Edit Member");
        dialogNama.setHeaderText("Edit Nama (opsional)");
        dialogNama.setContentText("Masukkan nama baru (kosongkan jika tidak diubah):");

        Optional<String> resultNama = dialogNama.showAndWait();
        if (resultNama.isEmpty()) return;

        String namaBaru = resultNama.get().trim();
        if (!namaBaru.isEmpty()) {
            target.setNama(namaBaru);
        }

        // 4) Edit data spesifik sesuai tipe
        if (target instanceof MemberReguler) {
            TextInputDialog dialogBiaya = new TextInputDialog();
            dialogBiaya.setTitle("Edit Member Reguler");
            dialogBiaya.setHeaderText("Edit Biaya Harian");
            dialogBiaya.setContentText("Masukkan biaya harian baru (angka):");

            Optional<String> resultBiaya = dialogBiaya.showAndWait();
            if (resultBiaya.isEmpty()) return;

            try {
                int biayaBaru = Integer.parseInt(resultBiaya.get().trim());
                ((MemberReguler) target).setBiaya(biayaBaru);
            } catch (NumberFormatException e) {
                txtAreaOutput.setText("Gagal: Biaya harus berupa angka valid!");
                return;
            }

        } else if (target instanceof MemberVIP) {
            TextInputDialog dialogLoker = new TextInputDialog();
            dialogLoker.setTitle("Edit Member VIP");
            dialogLoker.setHeaderText("Edit Nomor Loker");
            dialogLoker.setContentText("Masukkan nomor loker baru:");

            Optional<String> resultLoker = dialogLoker.showAndWait();
            if (resultLoker.isEmpty()) return;

            String lokerBaru = resultLoker.get().trim();
            if (lokerBaru.isEmpty()) {
                txtAreaOutput.setText("Gagal: Nomor loker tidak boleh kosong!");
                return;
            }
            ((MemberVIP) target).setLoker(lokerBaru);
        }

        txtAreaOutput.setText("Berhasil edit member:\n" + target.getInfo());
    }
}