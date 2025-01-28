package rapor;

import javax.swing.*;
import kullanici.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KullaniciFormu form = new KullaniciFormu();
            form.setVisible(true);
        });
    }
}