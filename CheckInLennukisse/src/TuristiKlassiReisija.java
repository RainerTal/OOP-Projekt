public class TuristiKlassiReisija extends Reisija{
    public TuristiKlassiReisija(String nimi, String email, String passiNumber) {
        super(nimi, email, passiNumber, 3);
    }

    @Override
    public boolean saabValidaIstekohta() {
        return false;
    }
}
