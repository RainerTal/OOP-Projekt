public class TuristiKlassiReisija extends Reisija{
    public TuristiKlassiReisija(String nimi, String email, String passiNumber, String istekoht) {
        super(nimi, email, passiNumber, 3, istekoht, 3);
    }

    @Override
    public boolean saabValidaIstekohta() {
        return false;
    }
}
