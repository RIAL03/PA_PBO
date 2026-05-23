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
    public String ambilFasilitas() {
        return "Akses Semua Area + Sauna + Handuk";
    }

    @Override
    public String ambilBonusSuplemen() {
        return "1 Botol Whey Protein / Bulan";
    }

    public void setLoker(String loker) {
        this.loker = loker;
    }
}