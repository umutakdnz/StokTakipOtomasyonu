package kullanici;

public abstract class Kullanici {
    private String rol;
    private String kullaniciAdi;
    private String parola;
    private String eposta;

    public Kullanici(String rol, String kullaniciAdi, String parola, String eposta) {
        this.rol = rol;
        this.kullaniciAdi = kullaniciAdi;
        this.parola = parola;
        this.eposta = eposta;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public String getKullaniciAdi() {
        return kullaniciAdi;
    }
    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }
    public String getParola() {
        return parola;
    }
    public void setParola(String parola) {
        this.parola = parola;
    }
    public String getEposta() {
        return eposta;
    }
    public void setEposta(String eposta) {
        this.eposta = eposta;
    }
}

