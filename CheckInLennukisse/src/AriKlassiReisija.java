public class AriKlassiReisija extends Reisija{
    public AriKlassiReisija(String nimi, String email, String passiNumber, String istekoht) {
        super(nimi, email, passiNumber, 2, istekoht, Integer.MAX_VALUE);
    }

    @Override
    public boolean saabValidaIstekohta() {
        return true;
    }
}
