package urun;

public class KadinGiyim extends Urun {
    private String beden;
    public KadinGiyim(String urunAdi, String renk, double fiyat, int adet, String aciklama,
                      String beden) {
        super(urunAdi, null, renk, fiyat, adet, aciklama); // Beden üst sınıfta null bırakıldı.
        this.beden = beden;
    }
    public String getBeden() {
        return beden;
    }
    public void setBeden(String beden) {
        this.beden = beden;
    }
}
