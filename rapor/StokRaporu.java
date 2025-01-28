package rapor;

public class StokRaporu extends Raporlama {
    public StokRaporu(String kategori, String urunAdi, double fiyat, int adet) {
        super(kategori, urunAdi, fiyat, adet);
    }

    public StokRaporu() {
        super("", "", 0.0, 0); // Varsayılan değerlerle başlat
    }

    public Object[][] getStokRaporuVerisi() {
        return new Object[][] {
                {"Elektronik", "Laptop", 3500.0, 10, 35000.0},
                {"Giyim", "T-Shirt", 50.0, 100, 5000.0},
                {"Elektronik", "Telefon", 2000.0, 20, 40000.0}
        };
    }
}
