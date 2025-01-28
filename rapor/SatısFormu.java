package rapor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import urun.*;
import kullanici.*;

public class SatısFormu extends JFrame {
    private JComboBox<String> kategoriComboBox;
    private JTextField urun_adiField, bedenField, renkField, fiyatField, adetField;
    private JButton satisButton, cikisButton;
    private JTable satisTablosu, stokTablosu;
    private DefaultTableModel satisTableModel, stokTableModel;

    public SatısFormu() {
        setTitle("Satış Paneli");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sol panel oluşturuluyor
        JPanel solPanel = new JPanel();
        solPanel.setLayout(new GridLayout(8, 2, 5, 5));

        solPanel.add(new JLabel("Kategori:"));
        kategoriComboBox = new JComboBox<>(new String[]{"Kategori Seçin", "Erkek Giyim", "Kadın Giyim", "Çocuk Giyim"});
        solPanel.add(kategoriComboBox);

        solPanel.add(new JLabel("Ürün Adı:"));
        urun_adiField = new JTextField();
        solPanel.add(urun_adiField);

        solPanel.add(new JLabel("Beden:"));
        bedenField = new JTextField();
        solPanel.add(bedenField);

        solPanel.add(new JLabel("Renk:"));
        renkField = new JTextField();
        solPanel.add(renkField);

        solPanel.add(new JLabel("Fiyat:"));
        fiyatField = new JTextField();
        solPanel.add(fiyatField);

        solPanel.add(new JLabel("Adet:"));
        adetField = new JTextField();
        solPanel.add(adetField);

        cikisButton = new JButton("Çıkış");
        solPanel.add(cikisButton);

        satisButton = new JButton("Satışı Gerçekleştir");
        solPanel.add(satisButton);

        add(solPanel, BorderLayout.WEST);

        // Sağ panel oluşturuluyor
        JPanel sagPanel = new JPanel(new BorderLayout());

        // Satışı yapılan ürünler başlığı ve tabloyu ekle
        JPanel satisUstPanel = new JPanel(new BorderLayout());
        satisUstPanel.add(new JLabel("Satışı Yapılan Ürünler", SwingConstants.CENTER), BorderLayout.NORTH);
        satisTableModel = new DefaultTableModel(new String[]{"Kategori", "Ürün Adı", "Beden", "Renk", "Fiyat", "Adet"}, 0);
        satisTablosu = new JTable(satisTableModel);
        satisUstPanel.add(new JScrollPane(satisTablosu), BorderLayout.CENTER);
        sagPanel.add(satisUstPanel, BorderLayout.NORTH);

        // Stoktaki ürünler başlığı ve tabloyu ekle
        JPanel stokAltPanel = new JPanel(new BorderLayout());
        stokAltPanel.add(new JLabel("Stoktaki Ürünler", SwingConstants.CENTER), BorderLayout.NORTH);
        stokTableModel = new DefaultTableModel(new String[]{"Kategori", "Ürün Adı", "Beden", "Renk", "Fiyat", "Adet"}, 0);
        stokTablosu = new JTable(stokTableModel);
        stokAltPanel.add(new JScrollPane(stokTablosu), BorderLayout.CENTER);
        sagPanel.add(stokAltPanel, BorderLayout.CENTER);

        add(sagPanel, BorderLayout.CENTER);

        // Buton işlemleri
        satisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                satisGerceklestir();
            }
        });

        cikisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new KullaniciFormu().setVisible(true);
            }
        });

        // Tabloları doldur
        stokTablosunuDoldur();
        satisTablosunuDoldur();
    }

    private void satisGerceklestir() {
        String kategori = (String) kategoriComboBox.getSelectedItem();
        String urunAdi = urun_adiField.getText().trim();
        String beden = bedenField.getText().trim();
        String renk = renkField.getText().trim();
        String fiyatStr = fiyatField.getText().trim();
        String adetStr = adetField.getText().trim();

        if (kategori.isEmpty() || urunAdi.isEmpty() || beden.isEmpty() || renk.isEmpty() || fiyatStr.isEmpty() || adetStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double fiyat = Double.parseDouble(fiyatStr);
            int adet = Integer.parseInt(adetStr);

            // Raporlama nesnesi oluştur
            Raporlama satisRaporu;
            satisRaporu = new Raporlama(kategori, urunAdi, fiyat, adet);

            // Veritabanına satış işlemini ekle
            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO stok_takip.satis_raporu (kategori, urun_adi, beden, renk, fiyat, adet) VALUES (?, ?, ?, ?, ?, ?)");) {
                ps.setString(1, satisRaporu.getKategori());
                ps.setString(2, satisRaporu.getUrunAdi());
                ps.setString(3, beden);
                ps.setString(4, renk);
                ps.setDouble(5, satisRaporu.getFiyat());
                ps.setInt(6, satisRaporu.getAdet());
                ps.executeUpdate();
            }

            // Stok tablosunu güncelle
            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement ps = conn.prepareStatement("UPDATE stok_takip.stok_raporu SET adet = adet - ? WHERE kategori = ? AND urun_adi = ? AND beden = ? AND renk = ? AND fiyat = ?");) {
                ps.setInt(1, satisRaporu.getAdet());
                ps.setString(2, satisRaporu.getKategori());
                ps.setString(3, satisRaporu.getUrunAdi());
                ps.setString(4, beden);
                ps.setString(5, renk);
                ps.setDouble(6, satisRaporu.getFiyat());
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Satış işlemi başarıyla gerçekleştirildi!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);

            // Tabloyu güncelle
            satisTableModel.addRow(new Object[]{
                    satisRaporu.getKategori(),
                    satisRaporu.getUrunAdi(),
                    beden,
                    renk,
                    satisRaporu.getFiyat(),
                    satisRaporu.getAdet()
            });
            stokTablosunuDoldur();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Fiyat ve adet alanlarına doğru değerler giriniz!", "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }

        // Alanları temizle
        urun_adiField.setText("");
        bedenField.setText("");
        renkField.setText("");
        fiyatField.setText("");
        adetField.setText("");
    }


    private void stokTablosunuDoldur() {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM stok_takip.stok_raporu");
             ResultSet rs = ps.executeQuery()) {

            stokTableModel.setRowCount(0); // Mevcut verileri temizle
            while (rs.next()) {
                stokTableModel.addRow(new Object[]{
                        rs.getString("kategori"),
                        rs.getString("urun_adi"),
                        rs.getString("beden"),
                        rs.getString("renk"),
                        rs.getDouble("fiyat"),
                        rs.getInt("adet")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void satisTablosunuDoldur() {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM stok_takip.satis_raporu");
             ResultSet rs = ps.executeQuery()) {

            satisTableModel.setRowCount(0); // Mevcut verileri temizle
            while (rs.next()) {
                satisTableModel.addRow(new Object[]{
                        rs.getString("kategori"),
                        rs.getString("urun_adi"),
                        rs.getString("beden"),
                        rs.getString("renk"),
                        rs.getDouble("fiyat"),
                        rs.getInt("adet")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SatısFormu().setVisible(true));
    }
}

