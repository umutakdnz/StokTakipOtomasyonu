package rapor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kullanici.*;

public class KullaniciFormu extends JFrame {

    // Bileşenler
    private JTextField txtKullaniciAdi;
    private JPasswordField txtParola;
    private JComboBox<String> cmbRol;

    public KullaniciFormu() {
        // Form başlığı
        setTitle("Kullanıcı Giriş Paneli");

        // Form boyutu
        setSize(400, 300);

        // Formu ekrana ortala
        setLocationRelativeTo(null);

        // Form kapatma davranışı
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout ayarı
        setLayout(new GridLayout(5, 2, 10, 10)); // 5 satır, 2 sütun, boşluklarla

        // Bileşenler
        JLabel lblRol = new JLabel("Rol:");
        cmbRol = new JComboBox<>(new String[]{"Rol Seçin", "Müdür", "Satış Elemanı", "Depocu"}); // ComboBox ekledik

        JLabel lblKullaniciAdi = new JLabel("Kullanıcı Adı:");
        txtKullaniciAdi = new JTextField();

        JLabel lblParola = new JLabel("Parola:");
        txtParola = new JPasswordField();

        JButton btnGiris = new JButton("Giriş");

        // Bileşenleri form üzerine ekle
        add(lblRol);
        add(cmbRol);

        add(lblKullaniciAdi);
        add(txtKullaniciAdi);

        add(lblParola);
        add(txtParola);

        add(new JLabel()); // Boş bir hücre
        add(btnGiris);

        // Butona olay ekle
        btnGiris.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rol = (String) cmbRol.getSelectedItem();
                String kullaniciAdi = txtKullaniciAdi.getText();
                String parola = new String(txtParola.getPassword());

                // Boş alan kontrolü
                if (kullaniciAdi.isEmpty() || parola.isEmpty() || rol == null) {
                    JOptionPane.showMessageDialog(null, "Lütfen tüm alanları doldurunuz!", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Veritabanı doğrulaması
                if (kullaniciDogrula(rol, kullaniciAdi, parola)) {
                    // Eğer rol "Müdür" ise MudurFormu açılır
                    if (rol.equals("Müdür")) {
                        MudurFormu mudurFormu = new MudurFormu();  // MudurFormu nesnesi oluşturulur
                        mudurFormu.setVisible(true);  // MudurFormu görünür yapılır
                    } else if (rol.equals("Depocu")){
                        DepocuFormu depocuFormu = new DepocuFormu();
                        depocuFormu.setVisible(true);
                    } else if (rol.equals("Satış Elemanı")){
                        SatısFormu satısFormu = new SatısFormu();
                        satısFormu.setVisible(true);
                    } else {
                        // Diğer roller için giriş başarılı mesajı
                        JOptionPane.showMessageDialog(null, "Giriş başarılı!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                    }
                    dispose();  // Kullanıcı formunu kapatır
                } else {
                    JOptionPane.showMessageDialog(null, "Rol, kullanıcı adı veya parola yanlış!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Kullanıcı doğrulama metodu
    private boolean kullaniciDogrula(String rol, String kullaniciAdi, String parola) {
        String query = "SELECT * FROM kullanici WHERE rol = ? AND kullanici_adi = ? AND parola = ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, rol);
            preparedStatement.setString(2, kullaniciAdi);
            preparedStatement.setString(3, parola);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Eğer sonuç varsa giriş başarılıdır
            return resultSet.next();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void main(String[] args) {
        // Formu oluştur ve görünür yap
        SwingUtilities.invokeLater(() -> {
            KullaniciFormu form = new KullaniciFormu();
            form.setVisible(true);
        });
    }
}
