package org.example.pa_pbo;

public class MemberReguler extends Member implements LayananTambahan {
    private int biaya;

    public MemberReguler(String nama, String id, int biaya) {
        super(nama, id);
        this.biaya = biaya;
    }

    @Override
    public String getTipe() { return "Reguler"; }

    @Override
    public String getInfo() {
        return super.getInfo() + " | Biaya Harian: Rp" + biaya;
    }

    @Override
    public String hitungDiskon(int totalBelanja) {
        return "Diskon Reguler (5%): Rp" + (totalBelanja * 0.05);
    }

    @Override
    public String berikanPoin(int totalBelanja) {
        return "Poin Baru: " + (totalBelanja / 10000);
    }
}