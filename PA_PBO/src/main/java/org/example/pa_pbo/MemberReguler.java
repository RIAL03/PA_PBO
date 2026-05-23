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
    public String ambilFasilitas() {
        return "Akses Area Gym Utama";
    }

    @Override
    public String ambilBonusSuplemen() {
        return "Tidak Ada Bonus";
    }

    // tambahkan di dalam class MemberReguler
    public void setBiaya(int biaya) {
        this.biaya = biaya;
    }
}
