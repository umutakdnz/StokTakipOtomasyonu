package urun;

public class ErkekGiyim extends Urun {
    public ErkekGiyim(String urunAdi, String beden, String renk, double fiyat, int adet,
                      String aciklama) {
        super(urunAdi, beden, renk, fiyat, adet, aciklama);
    }
    public String getBeden() {
        return super.getBeden();
    }
    public void setBeden(String beden) {
        super.setBeden(beden);
    }
}

