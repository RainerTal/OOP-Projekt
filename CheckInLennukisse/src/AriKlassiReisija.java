public class AriKlassiReisija extends Reisija{
    public AriKlassiReisija(String nimi, String email, String passiNumber) {
        super(nimi, email, passiNumber, 2);
    }

    @Override
    public boolean saabValidaIstekohta() {
        return false;
    }
}
