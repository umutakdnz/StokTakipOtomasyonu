//package rapor;
//
//public class SatisRaporu extends Raporlama {
//    public SatisRaporu() {
//        super(getKategori(), urunAdi, fiyat, adet);
//    }
//
//    public Object[][] getSatisRaporuVerisi() {
//        // Örnek veriler döndürüyoruz, burada veritabanından veya diğer kaynaklardan veri alabilirsiniz
//        return new Object[][] {
//                {"Elektronik", "Telefon", 1500.0, 10, 15000.0},
//                {"Elektronik", "Laptop", 3500.0, 5, 17500.0},
//                {"Giyim", "T-Shirt", 50.0, 30, 1500.0}
//        };
//    }
//
//}

package rapor;

public class SatisRaporu extends Raporlama {
    public SatisRaporu(String kategori, String urunAdi, double fiyat, int adet) {
        super(kategori, urunAdi, fiyat, adet);

    }
}
