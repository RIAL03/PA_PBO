package org.example.pa_pbo;

public class MemberVIP extends Member implements LayananTambahan {
    private String loker;

    public MemberVIP(String nama, String id, String loker) {
        super(nama, id);
        this.loker = loker;
    }

    @Override
    public String getTipe() { return "VIP"; }

    @Override
    public String getInfo() {
        return super.getInfo() + " | Nomor Loker: " + loker;
    }

    @Override
    public String hitungDiskon(int totalBelanja) {
        return "Diskon VIP (15%): Rp" + (totalBelanja * 0.15);
    }

    @Override
    public String berikanPoin(int totalBelanja) {
        return "Poin Baru (Bonus VIP 2x): " + ((totalBelanja / 10000) * 2);
    }
}