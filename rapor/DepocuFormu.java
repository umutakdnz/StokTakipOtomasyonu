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

public class DepocuFormu extends JFrame {
    private JComboBox<String> kategoriComboBox;
    private JTextField urunAdiField, bedenField, renkField, fiyatField, adetField;
    private JButton ekleButton, guncelleButton, silButton, cikisButton;
    private JTable stokTablosu;
    private DefaultTableModel tableModel;

    public DepocuFormu() {
        setTitle("Depocu Paneli");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel solPanel = new JPanel();
        solPanel.setLayout(new GridLayout(8, 2, 5, 5));

        solPanel.add(new JLabel("Kategori:"));
        kategoriComboBox = new JComboBox<>(new String[]{"Kategori Seçin", "Erkek Giyim", "Kadın Giyim", "Çocuk Giyim"});
        solPanel.add(kategoriComboBox);

        solPanel.add(new JLabel("Ürün Adı:"));
        urunAdiField = new JTextField();
        solPanel.add(urunAdiField);

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

        guncelleButton = new JButton("Güncelle");
        solPanel.add(guncelleButton);

        ekleButton = new JButton("Ürünü Ekle");
        solPanel.add(ekleButton);

        cikisButton = new JButton("Çıkış");
        solPanel.add(cikisButton);

        silButton = new JButton("Sil");
        solPanel.add(silButton);

        add(solPanel, BorderLayout.WEST);

        JPanel sagPanel = new JPanel(null);
        tableModel = new DefaultTableModel(new String[]{"Kategori", "Ürün Adı", "Beden", "Renk", "Fiyat", "Adet"}, 0);
        stokTablosu = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(stokTablosu);
        scrollPane.setBounds(10, 50, 500, 400);
        sagPanel.add(scrollPane);

        add(sagPanel, BorderLayout.CENTER);

        ekleButton.addActionListener(e -> ekleButonuIslemleri());

        silButton.addActionListener(e -> silButonuIslemleri());

        guncelleButton.addActionListener(e -> guncelleButonuIslemleri());

        cikisButton.addActionListener(e -> {
            dispose();
            new KullaniciFormu().setVisible(true);
        });

        tabloyuGuncelle();
    }

    private void ekleButonuIslemleri() {
        // Kullanıcıdan alınan veriler
        String kategori = (String) kategoriComboBox.getSelectedItem();
        String urunAdi = urunAdiField.getText().trim();
        String beden = bedenField.getText().trim();
        String renk = renkField.getText().trim();
        String fiyat = fiyatField.getText().trim();
        String adet = adetField.getText().trim();

        // Giriş kontrolü
        if (kategori.isEmpty() || urunAdi.isEmpty() || beden.isEmpty() || renk.isEmpty() || fiyat.isEmpty() || adet.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm bilgileri doldurun!", "Eksik Bilgi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Urun nesnesi oluştur ve değerlerini ata
            Urun urun = new Urun();
            urun.setKategori(kategori);
            urun.setUrunAdi(urunAdi);
            urun.setBeden(beden);
            urun.setRenk(renk);
            urun.setFiyat(Double.parseDouble(fiyat));
            urun.setAdet(Integer.parseInt(adet));

            // Veritabanına ekle
            boolean eklemeBasarili = urunEkle(urun);
            if (eklemeBasarili) {
                JOptionPane.showMessageDialog(this, "Ürün başarıyla eklendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                tabloyuGuncelle(); // Tabloyu güncelle
            } else {
                JOptionPane.showMessageDialog(this, "Ürün eklenemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lütfen fiyat ve adet alanlarına doğru değerler girin!", "Hata", JOptionPane.ERROR_MESSAGE);
        }

        urunAdiField.setText("");
        bedenField.setText("");
        renkField.setText("");
        fiyatField.setText("");
        adetField.setText("");
    }

    private boolean urunEkle(Urun urun) {
        String query = "INSERT INTO stok_takip.stok_raporu (kategori, urun_adi, beden, renk, fiyat, adet) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, urun.getKategori());
            preparedStatement.setString(2, urun.getUrunAdi());
            preparedStatement.setString(3, urun.getBeden());
            preparedStatement.setString(4, urun.getRenk());
            preparedStatement.setDouble(5, urun.getFiyat());
            preparedStatement.setInt(6, urun.getAdet());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Veritabanı Hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }



    private void silButonuIslemleri() {
        int selectedRow = stokTablosu.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek istediğiniz ürünü seçin!", "Hata", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String urunAdi = (String) tableModel.getValueAt(selectedRow, 1);

        try (Connection connection = DatabaseConnection.connect()) {
            String query = "DELETE FROM stok_takip.stok_raporu WHERE urun_adi = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, urunAdi);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Ürün başarıyla silindi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                tabloyuGuncelle();
            } else {
                JOptionPane.showMessageDialog(this, "Ürün silinemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Veritabanı Hatası: " + ex.getMessage());
        }
    }

    private void guncelleButonuIslemleri() {
        int selectedRow = stokTablosu.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen güncellemek istediğiniz ürünü seçin!", "Hata", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String eskiUrunAdi = (String) tableModel.getValueAt(selectedRow, 1); // Seçili satırdan eski ürün adını al
        String kategori = (String) kategoriComboBox.getSelectedItem();
        String urunAdi = urunAdiField.getText().trim();
        String beden = bedenField.getText().trim();
        String renk = renkField.getText().trim();
        String fiyat = fiyatField.getText().trim();
        String adet = adetField.getText().trim();

        // Giriş kontrolü
        if (urunAdi.isEmpty() || beden.isEmpty() || renk.isEmpty() || fiyat.isEmpty() || adet.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm bilgileri doldurun!", "Eksik Bilgi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Urun nesnesi oluştur ve değerleri ata
            Urun urun = new Urun();
            urun.setKategori(kategori);
            urun.setUrunAdi(urunAdi);
            urun.setBeden(beden);
            urun.setRenk(renk);
            urun.setFiyat(Double.parseDouble(fiyat));
            urun.setAdet(Integer.parseInt(adet));

            // Veritabanında güncelle
            boolean guncellemeBasarili = urunGuncelle(urun, eskiUrunAdi);
            if (guncellemeBasarili) {
                JOptionPane.showMessageDialog(this, "Ürün başarıyla güncellendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                tabloyuGuncelle(); // Tabloyu güncelle
            } else {
                JOptionPane.showMessageDialog(this, "Ürün güncellenemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lütfen fiyat ve adet alanlarına doğru değerler girin!", "Hata", JOptionPane.ERROR_MESSAGE);
        }

        urunAdiField.setText("");
        bedenField.setText("");
        renkField.setText("");
        fiyatField.setText("");
        adetField.setText("");

    }

    private boolean urunGuncelle(Urun urun, String eskiUrunAdi) {
        String query = "UPDATE stok_takip.stok_raporu SET kategori = ?, urun_adi = ?, beden = ?, renk = ?, fiyat = ?, adet = ? WHERE urun_adi = ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, urun.getKategori());
            preparedStatement.setString(2, urun.getUrunAdi());
            preparedStatement.setString(3, urun.getBeden());
            preparedStatement.setString(4, urun.getRenk());
            preparedStatement.setDouble(5, urun.getFiyat());
            preparedStatement.setInt(6, urun.getAdet());
            preparedStatement.setString(7, eskiUrunAdi);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Veritabanı Hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }


    private void tabloyuGuncelle() {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM stok_takip.stok_raporu");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            tableModel.setRowCount(0);
            while (resultSet.next()) {
                String kategori = resultSet.getString("kategori");
                String urunAdi = resultSet.getString("urun_adi");
                String beden = resultSet.getString("beden");
                String renk = resultSet.getString("renk");
                double fiyat = resultSet.getDouble("fiyat");
                int adet = resultSet.getInt("adet");

                tableModel.addRow(new Object[]{kategori, urunAdi, beden, renk, fiyat, adet});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Veritabanı Hatası: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DepocuFormu form = new DepocuFormu();
            form.setVisible(true);
        });
    }
}

