package org.example.pa_pbo;

// Abstraction
public abstract class Member {
    // Encapsulation (protected)
    protected String nama;
    protected String id;

    public Member(String nama, String id) {
        this.nama = nama;
        this.id = id;
    }

    public abstract String getTipe();

    // Getter untuk ID (Dibutuhkan untuk fitur Hapus di HelloController)
    public String getID() {
        return id;
    }

    // Polymorphism Overloading 1 (Tanpa parameter)
    public String getInfo() {
        return "ID: " + id + " | Nama: " + nama + " | Tipe: " + getTipe();
    }

    // Polymorphism Overloading 2 (Dengan parameter String)
    public String getInfo(String salam) {
        return salam + ", " + nama + "!\nID: " + id + " | Nama: " + nama + " | Tipe: " + getTipe();
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}