public class PardaKaart {
    private String reisijaNimi;
    private String lennunumber;
    private String istekoht;
    private String checkInID;
    private int reisiKlass;

    public PardaKaart(String reisijaNimi, String lennunumber, String istekoht, int reisiKlass) {
        this.reisijaNimi = reisijaNimi;
        this.lennunumber = lennunumber;
        this.istekoht = istekoht;
        this.reisiKlass = reisiKlass;
        this.checkInID = genereeriCheckInID();
    }

    private String genereeriCheckInID() {
        return "PK" + (int)(10000000 + Math.random() * 90000000);
    }

    public void prindiPardaKaart() {
        System.out.println("----------- Pardakaart -----------");
        if (reisiKlass == 1) System.out.println("Klass: Esimene");
        else if (reisiKlass == 2) System.out.println("Klass: Äri");
        else if (reisiKlass == 3) System.out.println("Klass: Turist");
        System.out.println("Reisija: " + reisijaNimi);
        System.out.println("Lend: " + lennunumber);
        System.out.println("Istekoht: " + istekoht);
        System.out.println("Check-ini kinnitus: " + checkInID);
        System.out.println("----------------------------------");
    }
}
