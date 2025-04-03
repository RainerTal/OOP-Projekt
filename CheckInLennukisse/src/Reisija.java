abstract class Reisija {
    private String nimi;
    private String email;
    private String passiNumber;
    private String istekoht;
    // 1 = Esimene klass, 2 = Äriklass, 3 = Turistiklass
    private int reisiklass;
    private int vahetusiJärel;

    public Reisija(String nimi, String email, String passiNumber, int reisiklass, String istekoht, int vahetusiJärel) {
        this.nimi = nimi;
        this.email = email;
        this.passiNumber = passiNumber;
        this.reisiklass = reisiklass;
        this.istekoht = istekoht;
        this.vahetusiJärel = vahetusiJärel;
    }

    public String getNimi() {
        return nimi;
    }

    public int getVahetusiJärel() { return vahetusiJärel; }

    public void decrementVahetusiJärel() {
        this.vahetusiJärel-- ;
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
        String reisiklassiNimi = switch (reisiklass) {
            case 1 -> "Esimese klass";
            case 2 -> "Äriklass";
            default -> "Turistiklass";
        };

        return "Reisija: " + nimi + " (Email: " + email + ", Passinumber: " + passiNumber +
                ", Istekoht: " + istekoht + ", Reisiklass: " + reisiklassiNimi + ")";
    }
}
