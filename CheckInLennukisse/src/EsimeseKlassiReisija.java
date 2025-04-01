public class EsimeseKlassiReisija extends Reisija{
    public EsimeseKlassiReisija(String nimi, String email, String passiNumber) {
        super(nimi, email, passiNumber, 1);
    }

    @Override
    public boolean saabValidaIstekohta() {
        return true;
    }
}
