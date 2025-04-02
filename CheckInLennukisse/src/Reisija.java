abstract class Reisija {
    private String nimi;
    private String email;
    private String passiNumber;
    private String istekoht;
    // 1 = Esimene klass, 2 = Äriklass, 3 = Turistiklass
    private int reisiklass;

    public Reisija(String nimi, String email, String passiNumber, int reisiklass, String istekoht) {
        this.nimi = nimi;
        this.email = email;
        this.passiNumber = passiNumber;
        this.reisiklass = reisiklass;
        this.istekoht = istekoht;
    }

    public String getNimi() {
        return nimi;
    }

    public String getEmail() {
        return email;
    }

    public String getPassiNumber() {
        return passiNumber;
    }

    public String getIstekoht() {
        return istekoht;
    }

    public int getReisiklass() {
        return reisiklass;
    }

    public void setIstekoht(String istekoht) {
        this.istekoht = istekoht;
    }

    public abstract boolean saabValidaIstekohta();

    @Override
    public String toString() {
        return String.format(
                "Reisija: %s (Email: %s, Passinumber: %s, Istekoht: %s, Reisiklass: %s)",
                nimi, email, passiNumber, istekoht, reisiklass
        );
    }
}
