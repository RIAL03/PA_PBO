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

        StringBuilder hasil = new StringBuilder("--- Daftar Member ---\n");
        for (int i = 0; i < listMember.size(); i++) {
            Member m = listMember.get(i);
            hasil.append((i + 1)).append(". ").append(m.getInfo()).append("\n");

            if (m instanceof LayananTambahan) {
                LayananTambahan layanan = (LayananTambahan) m;
                hasil.append("   - ").append(layanan.hitungDiskon(50000)).append("\n");
                hasil.append("   - ").append(layanan.berikanPoin(50000)).append("\n");
            }
            hasil.append("-----------------------\n");
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
}