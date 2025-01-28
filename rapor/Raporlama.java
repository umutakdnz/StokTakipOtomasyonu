//package rapor;
//
//public abstract class Raporlama {
//    private String kategori;
//    private String urunAdi;
//    private double fiyat;
//    private int adet;
//    private double toplamUcret;
//    public Raporlama(String kategori, String urunAdi, double fiyat, int adet) {
//        this.kategori = kategori;
//        this.urunAdi = urunAdi;
//        this.fiyat = fiyat;
//        this.adet = adet;
//        this.toplamUcret = fiyat * adet;
//    }
//
//    public String getKategori() {
//        return kategori;
//    }
//    public void setKategori(String kategori) {
//        this.kategori = kategori;
//    }
//    public String getUrunAdi() {
//        return urunAdi;
//    }
//    public void setUrunAdi(String urunAdi) {
//        this.urunAdi = urunAdi;
//    }
//    public double getFiyat() {
//        return fiyat;
//    }
//    public void setFiyat(double fiyat) {
//        this.fiyat = fiyat;
//        this.toplamUcret = fiyat * adet; // Toplam ücreti güncelle
//    }
//    public int getAdet() {
//        return adet;
//    }
//    public void setAdet(int adet) {
//        this.adet = adet;
//        this.toplamUcret = fiyat * adet; // Toplam ücreti güncelle
//    }
//    public double getToplamUcret() {
//        return toplamUcret;
//    }
//    public void setToplamUcret(double toplamUcret) {
//        this.toplamUcret = toplamUcret;
//    }
//}
//
//

package rapor;

public class Raporlama {
    private String kategori;
    private String urunAdi;
    private double fiyat;
    private int adet;
    private double toplamUcret;

    public Raporlama(String kategori, String urunAdi, double fiyat, int adet) {
        this.kategori = kategori;
        this.urunAdi = urunAdi;
        this.fiyat = fiyat;
        this.adet = adet;
        this.toplamUcret = fiyat * adet;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public double getFiyat() {
        return fiyat;
    }

    public void setFiyat(double fiyat) {
        this.fiyat = fiyat;
        this.toplamUcret = fiyat * adet; // Toplam ücreti güncelle
    }

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
        this.toplamUcret = fiyat * adet; // Toplam ücreti güncelle
    }

    public double getToplamUcret() {
        return toplamUcret;
    }

    public void setToplamUcret(double toplamUcret) {
        this.toplamUcret = toplamUcret;
    }
}
