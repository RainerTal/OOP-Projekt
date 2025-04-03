import java.util.List;
import java.util.Scanner;

public class Peaklass {

    /**
     * Peaprogramm, mis käivitab lennukisse check-in'imise süsteemi.
     * Meetod:
     * 1. Küsib kasutajalt lennunumbri ja lennuki parameetrid (ridade ja kohtade arvu)
     * 2. Loob istumisjaotuse vastavalt sisestatud parameetritele
     * 3. Genereerib pardale juhuslikud kaasreisijad
     * 4. Lisab uue reisija (kasutaja) pardale
     * 5. Võimaldab kasutajal muuta istekohta, vaadata istumisjaotust või lõpetada check-in
     * 6. Lõpetamisel väljastab kasutajale pardakaardi
     *
     * Kasutusel on käsurealt jooksvalt sisseloetavad parameetrid
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Sisesta lennunumber: ");
        String lennuNumber = scanner.nextLine();

        int ridadeArv = 0;
        boolean valiidneReaSisend = false;
        while (!valiidneReaSisend) {
            System.out.print("Sisesta lennuki ridade arv (min 10, max 99): ");
            if (scanner.hasNextInt()) {
                ridadeArv = scanner.nextInt();
                valiidneReaSisend = true;
            } else {
                System.out.println("Palun sisesta arv vahemikus 10-99");
                scanner.next();
            }
        }

        int kohtadeArv = 0;
        boolean valiidneKohaSisend = false;
        while (!valiidneKohaSisend) {
            System.out.print("Sisesta kohtade arv reas (min 2, max 8): ");
            if (scanner.hasNextInt()) {
                kohtadeArv = scanner.nextInt();
                valiidneKohaSisend = true;
            } else {
                System.out.println("Palun sisesta arv vahemikus 10-99");
                scanner.next();
            }
        }

        scanner.nextLine();
        IstumisJaotus istumisJaotus = new IstumisJaotus(ridadeArv, kohtadeArv);
        CheckInSysteem checkInSysteem = new CheckInSysteem(lennuNumber, istumisJaotus);

        System.out.println("Kaasreisjate loomine");
        checkInSysteem.genereeriReisijad(istumisJaotus.getMaxMahutavusAlla80());
        List<Reisija> reisijad = checkInSysteem.getReisijadListis();

        for (Reisija reisija : reisijad) {
            System.out.println("Lisati " + reisija);
        }

        Reisija reisija = checkInSysteem.lisaReisija();
        istumisJaotus.lisaKasutajaReisija(reisija);

        boolean jatkaTööd = true;
        while (jatkaTööd) {
            System.out.println("\nVali tegevus:");
            System.out.println("1 - Muuda istekohta");
            System.out.println("2 - Kuva istumisjaotus");
            System.out.println("3 - Lõpeta tegevus");
            int valik = scanner.nextInt();
            scanner.nextLine();

            switch (valik) {
                case 1 -> {
                    if (reisija.getVahetusiJärel() <= 0) {
                        System.out.println("Te ei saa enam istekohta muuta, kuna kõik vahetused on otsas.");
                        break;
                    }

                    System.out.println("Istekoha muutmine:");
                    System.out.println("Siin on teile istumisplaan, kus kõik vabad kohad on märgitud 'o'-ga, teie istekoht on tähistatud '❎'-ga.");
                    CheckInSysteem.oota(2000);
                    System.out.println("\n" + istumisJaotus + "\n");
                    System.out.print("Sisestage uus soovitud VABA istekoht: ");
                    String uusIsteKoht = scanner.nextLine();
                    checkInSysteem.vahetaIstekohta(reisija, uusIsteKoht);
                }
                case 2 -> System.out.println(istumisJaotus);
                case 3 -> {
                    jatkaTööd = false;
                    System.out.println("Lennu " + lennuNumber + " check-in lõpetatud.");
                    System.out.println("Vormistan boarding pass-i...");
                    PardaKaart pardaKaart = new PardaKaart(reisija.getNimi(), lennuNumber, reisija.getIstekoht(), reisija.getReisiklass());
                    pardaKaart.prindiPardaKaart();
                        }
                default -> System.out.println("Tundmatu valik, palun proovi uuesti.");
                    }
                }
    }
}
