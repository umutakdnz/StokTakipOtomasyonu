package rapor;//package rapor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kullanici.*;

public class MudurFormu extends JFrame {

    // Bileşenler
    private JTextField txtRol, txtKullaniciAdi, txtEposta;
    private JPasswordField txtParola;
    private JCheckBox chkSatis, chkStok;
    private JButton btnKaydet, btnGuncelle, btnSil, btnSatisRaporu, btnStokRaporu, btnCikis;
    private JTable raporTablosu;
    private DefaultTableModel tableModel;

    public MudurFormu() {
        // Form Ayarları
        setTitle("Müdür Paneli");
        setSize(800, 600); // Orta büyüklükte boyut
        setLocationRelativeTo(null); // Formu ekran ortasına al
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Çıkış ayarı
        setLayout(null);

        // Kullanıcı Bilgi Alanları
        JLabel lblKullaniciAdi = new JLabel("Kullanıcı Adı:");
        lblKullaniciAdi.setBounds(20, 20, 100, 25);
        add(lblKullaniciAdi);

        txtKullaniciAdi = new JTextField();
        txtKullaniciAdi.setBounds(130, 20, 150, 25);
        add(txtKullaniciAdi);

        JLabel lblParola = new JLabel("Parola:");
        lblParola.setBounds(20, 60, 100, 25);
        add(lblParola);

        txtParola = new JPasswordField();
        txtParola.setBounds(130, 60, 150, 25);
        add(txtParola);

        JLabel lblEposta = new JLabel("E-posta:");
        lblEposta.setBounds(20, 100, 100, 25);
        add(lblEposta);

        txtEposta = new JTextField();
        txtEposta.setBounds(130, 100, 150, 25);
        add(txtEposta);

        // Checkboxlar: Tek Seçim Kontrolü
        chkSatis = new JCheckBox("Satış Elemanı");
        chkSatis.setBounds(20, 140, 120, 25);
        chkSatis.addActionListener(e -> chkStok.setSelected(false));
        add(chkSatis);

        chkStok = new JCheckBox("Depocu");
        chkStok.setBounds(150, 140, 120, 25);
        chkStok.addActionListener(e -> chkSatis.setSelected(false));
        add(chkStok);

        // Butonlar
        btnKaydet = new JButton("Kullanıcı Ekle");
        btnKaydet.setBounds(20, 180, 120, 30);
        add(btnKaydet);

        btnGuncelle = new JButton("Güncelle");
        btnGuncelle.setBounds(150, 180, 120, 30);
        add(btnGuncelle);

        btnSil = new JButton("Kullanıcı Sil");
        btnSil.setBounds(280, 180, 120, 30);
        add(btnSil);

        btnSatisRaporu = new JButton("Satış Raporu");
        btnSatisRaporu.setBounds(20, 220, 120, 30);
        add(btnSatisRaporu);

        btnStokRaporu = new JButton("Stok Raporu");
        btnStokRaporu.setBounds(150, 220, 120, 30);
        add(btnStokRaporu);

        btnCikis = new JButton("Çıkış");
        btnCikis.setBounds(280, 220, 120, 30);
        add(btnCikis);

        // Çıkış butonuna olay ekle
        btnCikis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KullaniciFormu kullaniciFormu = new KullaniciFormu(); // Kullanıcı formunu aç
                kullaniciFormu.setVisible(true);
                dispose(); // Müdür formunu kapat
            }
        });

        // Tablo
        tableModel = new DefaultTableModel();
        raporTablosu = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(raporTablosu);
        scrollPane.setBounds(20, 260, 740, 280);
        add(scrollPane);

        // Kullanıcı Ekleme İşlevi
        btnKaydet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rol = chkSatis.isSelected() ? "Satış Elemanı" : chkStok.isSelected() ? "Depocu" : "";
                String kullaniciAdi = txtKullaniciAdi.getText();
                String parola = new String(txtParola.getPassword());
                String eposta = txtEposta.getText();

                if (!rol.isEmpty() && !kullaniciAdi.isEmpty() && !parola.isEmpty() && !eposta.isEmpty()) {
                    try {
                        if (rol.equals("Depocu")) {
                            // Depocu nesnesi  ve veritabanına ekle
                            Depocu depocu1 = new Depocu();
                            depocu1.setKullaniciAdi(kullaniciAdi);
                            depocu1.setParola(parola);
                            depocu1.setEposta(eposta);
                            depocu1.setRol(rol);

                            kullaniciEkle(depocu1);
                        } else if (rol.equals("Satış Elemanı")) {
                            // Satış Elemanı nesnesi ve veritabanına ekle
                            SatisElemani satisElemani1 = new SatisElemani();
                            satisElemani1.setKullaniciAdi(kullaniciAdi);
                            satisElemani1.setParola(parola);
                            satisElemani1.setEposta(eposta);
                            satisElemani1.setRol(rol);

                            kullaniciEkle(satisElemani1);
                        }

                        // Kaydetme işlemi sonrası formu temizle
                        txtKullaniciAdi.setText("");
                        txtParola.setText("");
                        txtEposta.setText("");
                        chkSatis.setSelected(false);
                        chkStok.setSelected(false);

                        JOptionPane.showMessageDialog(null, "Kullanıcı başarıyla kaydedildi!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Kullanıcı kaydedilirken bir hata oluştu: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Tüm alanları doldurunuz!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Rapor Butonları İşlevleri
        btnSatisRaporu.addActionListener(e -> tabloyuDoldur("Satis"));
        btnStokRaporu.addActionListener(e -> tabloyuDoldur("Stok"));

        btnGuncelle.addActionListener(e -> kullaniciGuncelle());
        btnSil.addActionListener(e -> kullaniciSil());
    }

    // Kullanıcı Ekleme Metodu (Veritabanı Bağlantısı Örneği)
    //Parametre olarak Depocu d

    public void kullaniciEkle(Object kullanici) throws SQLException {
        String query = "";
        if (kullanici instanceof Depocu) {
            Depocu depocu = (Depocu) kullanici;
            query = "INSERT INTO kullanici (kullanici_adi, parola, eposta, rol) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.connect(); PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, depocu.getKullaniciAdi());
                ps.setString(2, depocu.getParola());
                ps.setString(3, depocu.getEposta());
                ps.setString(4, depocu.getRol());
                ps.executeUpdate();
            }
        } else if (kullanici instanceof SatisElemani) {
            SatisElemani satisElemani = (SatisElemani) kullanici;
            query = "INSERT INTO kullanici (kullanici_adi, parola, eposta, rol) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.connect(); PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, satisElemani.getKullaniciAdi());
                ps.setString(2, satisElemani.getParola());
                ps.setString(3, satisElemani.getEposta());
                ps.setString(4, satisElemani.getRol());
                ps.executeUpdate();
            }
        }
    }

    // Kullanıcı Güncelleme Metodu
    private void kullaniciGuncelle() {
        String kullaniciAdi = txtKullaniciAdi.getText();
        String parola = new String(txtParola.getPassword());
        String eposta = txtEposta.getText();

        if (!kullaniciAdi.isEmpty() && !parola.isEmpty() && !eposta.isEmpty()) {
            String query = "UPDATE kullanici SET parola = ?, eposta = ? WHERE kullanici_adi = ?";
            try (Connection connection = DatabaseConnection.connect();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, parola);
                preparedStatement.setString(2, eposta);
                preparedStatement.setString(3, kullaniciAdi);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Kullanıcı başarıyla güncellendi!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Veritabanı Hatası: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Tüm alanları doldurunuz!", "Uyarı", JOptionPane.WARNING_MESSAGE);
        }

        // Kaydetme işlemi sonrası formu temizle
        txtKullaniciAdi.setText("");
        txtParola.setText("");
        txtEposta.setText("");
        chkSatis.setSelected(false);
        chkStok.setSelected(false);
    }

    // Kullanıcı Silme Metodu
    private void kullaniciSil() {
        String kullaniciAdi = txtKullaniciAdi.getText();

        if (!kullaniciAdi.isEmpty()) {
            String query = "DELETE FROM kullanici WHERE kullanici_adi = ?";
            try (Connection connection = DatabaseConnection.connect();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, kullaniciAdi);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Kullanıcı başarıyla silindi!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Veritabanı Hatası: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Kullanıcı Adını giriniz!", "Uyarı", JOptionPane.WARNING_MESSAGE);
        }

        // Kaydetme işlemi sonrası formu temizle
        txtKullaniciAdi.setText("");
        txtParola.setText("");
        txtEposta.setText("");
        chkSatis.setSelected(false);
        chkStok.setSelected(false);
    }

    // Raporları veritabanından alıp tabloyu doldurur
    private void tabloyuDoldur(String raporTuru) {
        String query = "";
        if (raporTuru.equals("Satis")) {
            query = "SELECT kategori, urun_adi, fiyat, adet, (fiyat * adet) AS toplamucret FROM satis_raporu";
        } else if (raporTuru.equals("Stok")) {
            query = "SELECT kategori, urun_adi, fiyat, adet, (fiyat * adet) AS toplamucret FROM stok_raporu";
        } else {
            JOptionPane.showMessageDialog(this, "Hatalı Rapor Türü: " + raporTuru, "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Eğer query hala null ise çalışmayı durdur
        if (query == null || query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "SQL Sorgusu boş. Lütfen sistem yöneticisine danışın.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Tabloyu temizle
            tableModel.setRowCount(0);
            // Tablo başlıkları
            tableModel.setColumnIdentifiers(new Object[]{"Kategori", "Ürün Adı", "Fiyat", "Adet", "Toplam Ücret"});

            // Veritabanından gelen veriyi tabloya ekle
            while (resultSet.next()) {
                String kategori = resultSet.getString("kategori");
                String urunadi = resultSet.getString("urun_adi");
                double fiyat = resultSet.getDouble("fiyat");
                int adet = resultSet.getInt("adet");
                double toplamUcret = resultSet.getDouble("toplamucret");

                tableModel.addRow(new Object[]{kategori, urunadi, fiyat, adet, toplamUcret});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Veritabanı Hatası: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MudurFormu form = new MudurFormu();
            form.setVisible(true);
        });
    }
}