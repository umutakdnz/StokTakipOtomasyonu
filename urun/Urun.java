package urun;

public class Urun {
    private String urunAdi;
    private String beden;
    private String renk;
    private double fiyat;
    private int adet;
    private String kategori;

    public Urun(String urunAdi, String beden, String renk, double fiyat, int adet, String kategori) {
        this.urunAdi = urunAdi;
        this.beden = beden;
        this.renk = renk;
        this.fiyat = fiyat;
        this.adet = adet;
        this.kategori = kategori;
    }

    public Urun() {

    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public String getBeden() {
        return beden;
    }

    public void setBeden(String beden) {
        this.beden = beden;
    }

    public String getRenk() {
        return renk;
    }

    public void setRenk(String renk) {
        this.renk = renk;
    }

    public double getFiyat() {
        return fiyat;
    }

    public void setFiyat(double fiyat) {
        this.fiyat = fiyat;
    }

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
