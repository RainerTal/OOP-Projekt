public class EsimeseKlassiReisija extends Reisija{
    public EsimeseKlassiReisija(String nimi, String email, String passiNumber, String istekoht) {
        super(nimi, email, passiNumber, 1, istekoht, Integer.MAX_VALUE);
    }

    @Override
    public boolean saabValidaIstekohta() {
        return true;
    }
}
