import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CheckInSysteem {
    private String lennunumber;
    private List<Reisija> reisijad = new ArrayList<>();
    private Scanner scanner;
    private IstumisJaotus istumisJaotus;

    // Esimese klassi kohad on alati esimesed 2 rida
    // Äriklassi kohad on alati read 3-7
    // Kõik peale seda on turistiklassi kohad
    private int esimesesKlassisKohti;
    private int ariKlassisKohti;

    public CheckInSysteem(String lennunumber, IstumisJaotus istumisJaotus) {
        this.lennunumber = lennunumber;
        this.istumisJaotus = istumisJaotus;
        this.scanner = new Scanner(System.in);

        // Esimese klassi kohad on alati esimesed 2 rida
        // Äriklassi kohad on alati read 3-7
        this.esimesesKlassisKohti = 2 * istumisJaotus.getIstumisMaatriks()[0].length;
        this.ariKlassisKohti = 5 * istumisJaotus.getIstumisMaatriks()[0].length;
    }

    public Reisija[] getReisijad() {
        return reisijad.toArray(new Reisija[0]);
    }

    public List<Reisija> getReisijadListis() {
        return reisijad;
    }

    /**
     * Lisab süsteemi uue reisija, küsides kasutajalt vajalikud andmed.
     * Võimaldab kasutajal sisestada reisija andmed ning vastavalt reisiklassile
     * määratakse istekoht kas kasutaja valikul või suvaliselt.
     *
     * @return Lisatud reisija
     */
    public Reisija lisaReisija() {
        System.out.println("----------- Uus reisija -----------");

        System.out.println("Sisesta reisija nimi: ");
        String nimi = scanner.nextLine();

        String email;

        while (true) {
            System.out.println("Sisesta reisija email: ");
            email = scanner.nextLine();

            if (!email.contains("@")) {
                System.out.println("See pole valiidne email!");
            } else break;
        }

        System.out.println("Sisesta passinumber: ");
        String passinumber = scanner.nextLine();

        int reisiklass;

        while (true) {
            System.out.println("Vali reisiklass: (1 - Esimene klass | 2 - Äriklass | 3 - Turistiklass): ");
            reisiklass = Integer.parseInt(scanner.nextLine());

            if (!(reisiklass == 1 || reisiklass == 2 || reisiklass == 3)) {
                System.out.println("See pole valiidne reisiklass, palun sisestada number 1, 2 või 3.");
            } else break;
        }

        if (reisiklass == 3){
            System.out.println("Teile, kui turistiklassi reisijale, on määratud suvaline istekoht, mida saate veel kolm korda muuta.");
        }

        Reisija reisija = switch (reisiklass) {
            case 1 -> new EsimeseKlassiReisija(nimi, email, passinumber, küsiSoovitudIstekohta(istumisJaotus, reisiklass));
            case 2 -> new AriKlassiReisija(nimi, email, passinumber, küsiSoovitudIstekohta(istumisJaotus, reisiklass));
            default -> new TuristiKlassiReisija(nimi, email, passinumber, suvalineIstekoht(istumisJaotus, reisiklass));
        };

        reisijad.add(reisija);
        istumisJaotus.määraIstekohtVõetuks(reisija.getIstekoht(), reisija);
        istumisJaotus.lisaKasutajaReisija(reisija);
        return reisija;
    }

    /**
     * Vahetab reisija istekoha uue vastu, kui see on võimalik.
     * Kontrollib, kas reisijal on vahetusi järel, kas uus istekoht on vaba
     * ning kas see vastab reisija reisiklassi piirangutele.
     *
     * @param reisija Reisija, kelle istekohta soovitakse vahetada
     * @param uusIstekoht Soovitud uus istekoht formaadis "ridaTäht" (nt "1A")
     */
    public void vahetaIstekohta(Reisija reisija, String uusIstekoht) {
        if (reisija.getVahetusiJärel() <= 0) {
            System.out.println("Teil ei ole rohkem istekoha vahetusi järel.");
            return;
        }

        int[] isteKohtArvud = istumisJaotus.isteKohaStringArvudeks(uusIstekoht);
        if (isteKohtArvud[0] == -1 || isteKohtArvud[1] == -1) {
            System.out.println("Sisestatud istekoht " + uusIstekoht + " ei vasta formaadile, palun proovige uuesti!");
            oota(2000);
            return;
        }

        int rida = isteKohtArvud[1];
        int reisiklass = reisija.getReisiklass();
        boolean onLubatudRida = false;

        if (reisiklass == 1 && rida >= 1 && rida <= 2) {
            onLubatudRida = true;
        } else if (reisiklass == 2 && rida >= 3 && rida <= 7) {
            onLubatudRida = true;
        } else if (reisiklass == 3 && rida >= 8) {
            onLubatudRida = true;
        }

        if (!onLubatudRida) {
            String reisiklassiNimi = switch (reisiklass) {
                case 1 -> "esimese klassi";
                case 2 -> "äriklassi";
                default -> "turistiklassi";
            };

            String klassiRead = switch (reisiklass) {
                case 1 -> "1-2";
                case 2 -> "3-7";
                default -> "8-" + istumisJaotus.getIstumisMaatriks().length;
            };

            System.out.println("Valitud istekoht " + uusIstekoht + " ei vasta " + reisiklassiNimi +
                    " reisijale. Palun valige istekoht vahemikus " + klassiRead + " rida.");
            oota(2000);
            return;
        }

        if (istumisJaotus.kasVabaIstekoht(uusIstekoht)) {
            istumisJaotus.vabastaIstekoht(reisija.getIstekoht());
            reisija.setIstekoht(uusIstekoht);
            istumisJaotus.määraIstekohtVõetuks(uusIstekoht, reisija);
            istumisJaotus.lisaKasutajaReisija(reisija);
            reisija.decrementVahetusiJärel();
            System.out.println("Teie istekoht on nüüd muudetud: " + reisija.getIstekoht());
        } else {
            System.out.println("Valitud istekoht " + uusIstekoht + " ei ole vaba, jääte oma praegusele kohale.");
        }
    }

    /**
     * Leiab ja tagastab suvalise vaba istekoha vastavalt reisiklassile.
     * Esimese klassi kohad on ridades 1-2, äriklassi kohad ridades 3-7
     * ja turistiklassi kohad alates reast 8.
     *
     * @param istumisJaotus Istumisjaotus, mille põhjal vabu kohti kontrollitakse
     * @param reisiklass Reisiklass, millele vastavat istekohta otsitakse
     * @return Vaba istekoht
     */
    public String suvalineIstekoht(IstumisJaotus istumisJaotus, int reisiklass) {
        int isteKohtadeArvReas = istumisJaotus.getIstumisMaatriks()[0].length;
        int maxKatseid = 200; //parandab ühe koha genereerimise errori
        int katseid = 0;
        String istekoht = null;

        if (reisiklass == 1) {
            while (istekoht == null && katseid < maxKatseid) {
                String kandidaat = genereeriSuvalineIsteKoht(1, 2, isteKohtadeArvReas);
                if(istumisJaotus.kasVabaIstekoht(kandidaat)) {
                    istekoht = kandidaat;
                }
                katseid++;
            }
        } else if (reisiklass == 2) {
            while (istekoht == null && katseid < maxKatseid) {
                String kandidaat = genereeriSuvalineIsteKoht(3, 7, isteKohtadeArvReas);
                if (istumisJaotus.kasVabaIstekoht(kandidaat)) {
                    istekoht = kandidaat;
                }
                katseid++;
            }
        } else {
            while (istekoht == null && katseid < maxKatseid) {
                String kandidaat = genereeriSuvalineIsteKoht(8, istumisJaotus.getIstumisMaatriks().length, isteKohtadeArvReas);
                if (istumisJaotus.kasVabaIstekoht(kandidaat)) {
                    istekoht = kandidaat;
                }
                katseid++;
            }
        }

        return istekoht;
    }

    /**
     * Genereerib suvalise istekoha määratud ridade vahemikus.
     *
     * @param minRida Minimaalne rida (kaasa arvatud)
     * @param maxRida Maksimaalne rida (kaasa arvatud)
     * @param kohtadeArvReas Kohtade arv igas reas
     * @return Istekoht formaadis "ridaTäht" (nt "1A")
     */
    private String genereeriSuvalineIsteKoht(int minRida, int maxRida, int kohtadeArvReas) {
        int suvalineRida = (int)(Math.random() * (maxRida - minRida + 1) + minRida);
        int suvalineKoht = (int)(Math.random() * kohtadeArvReas);

        String koht = switch (suvalineKoht) {
            case 0 -> "A";
            case 1 -> "B";
            case 2 -> "C";
            case 3 -> "D";
            case 4 -> "E";
            case 5 -> "F";
            case 6 -> "G";
            default -> "H";
        };

        return suvalineRida + koht;
    }

    /**
     * Võimaldab kasutajal valida istekoha või määrab suvalise.
     * Kui kasutaja soovib ise valida, kuvatakse istumisplaan ja kontrollitakse,
     * kas valitud koht on vaba ning vastab reisiklassi piirangutele.
     *
     * @param istumisJaotus Istumisjaotus, mille põhjal vabu kohti kontrollitakse
     * @param reisiklass Reisiklass, millele vastavat istekohta otsitakse
     * @return Valitud või määratud istekoht
     */
    public String küsiSoovitudIstekohta(IstumisJaotus istumisJaotus, int reisiklass){

        System.out.println("Kas soovite ise istekohta valida? : (1 - Soovin ise valida | 2 - Määrake mulle suvaline isetkoht) ");
        boolean valik = false;
        try {
            int vastus = Integer.parseInt(scanner.nextLine());
            valik = (vastus == 1);
        } catch (NumberFormatException e) {
            System.out.println("Vigane sisend. Määran teile suvalise istekoha.");
        }

        String istekoht = null;
        if (valik) {
            boolean onVaba = false;
            String reisiklassiNimi = switch (reisiklass) {
                case 1 -> "esimene klassi";
                case 2 -> "äriklassi";
                default -> "turistiklassi";
            };
            String klassiRead = switch (reisiklass) {
                case 1 -> "1-2";
                case 2 -> "3-7";
                default -> "8-" + istumisJaotus.getIstumisMaatriks().length;
            };

            while (istekoht == null && !onVaba) {
                System.out.println("Siin on teile istumisplaan, kus kõik vabad kohad on märgitud 'o'-ga");
                oota(1000); //et kasutaja jõuaks lugeda
                System.out.println("\n" + istumisJaotus + "\n");
                System.out.println("Sisestage oma soovitud VABA istekoht formaadis arvTäht nt: 1A või 6C või 13D");
                System.out.println("Kuna olete " + reisiklassiNimi + " reisija, siis palun valige istekoht vahemikus " + klassiRead +" rida");

                String sisend = scanner.nextLine();

                int[] isteKohtArvud = istumisJaotus.isteKohaStringArvudeks(sisend); //proovi kas sobib
                if (isteKohtArvud[0] == -1 || isteKohtArvud[1] == -1) {
                    System.out.println("Sisestatud istekoht " + sisend + " ei vasta formaadile, palun proovige uuesti!");
                    oota(2000);
                    continue;
                }

                int rida = isteKohtArvud[1];
                boolean onLubatudRida = false;

                if (reisiklass == 1 && rida >= 1 && rida <= 2) {
                    onLubatudRida = true;
                } else if (reisiklass == 2 && rida >= 3 && rida <= 7) {
                    onLubatudRida = true;
                } else if (reisiklass == 3 && rida >= 8) {
                    onLubatudRida = true;
                }

                if (!onLubatudRida) {
                    System.out.println("Valitud istekoht " + sisend + " ei vasta " + reisiklassiNimi +
                            " reisijale. Palun valige istekoht vahemikus " + klassiRead + " rida.");
                    oota(2000);
                    continue;
                }

                onVaba = istumisJaotus.kasVabaIstekoht(sisend);

                if (onVaba) {
                    istekoht = sisend;
                    System.out.println("Teie soovitud istekoht " + sisend + " on nüüd teile määratud!");
                }
                else {
                    System.out.println("Istekoht " + sisend + " ei ole vaba. Palun valige teine istekoht.");
                    oota(2000);
                }
            }
        }
        else {
            istekoht = suvalineIstekoht(istumisJaotus, reisiklass);
        }
        return istekoht;
    }

    /**
     * Genereerib pardale juhuslikud reisijad, täites lennuki kuni 80% ulatuses.
     * Reisijad jaotatakse erinevate reisiklasside vahel ning neile määratakse istekohad.
     * Esimeses klassis ja äriklassis on kohtade arv piiratud, kuid kasutajale jäetakse alati ruumi.
     *
     * @param maxMahutuvusAlla80Protsendi Maksimaalne genereeritavate reisijate arv
     */
    public void genereeriReisijad(int maxMahutuvusAlla80Protsendi) {
        String[] nimed = new String[] {
                "Andres Tamm", "Tiina Kask", "Mart Sepp", "Kristiina Rebane", "Jaan Mägi",
                "Liis Saar", "Rein Kukk", "Kati Lepp", "Toomas Kaasik", "Kadri Kuusk",
                "Margus Kivi", "Anna Ilves", "Tõnu Lepik", "Mari Oja", "Siim Kallas",
                "Piret Pärn", "Karl Vaher", "Laura Koppel", "Peeter Raud", "Tiiu Männik",
                "Priit Järv", "Katrin Mänd", "Madis Põder", "Eva Teder", "Taavi Lõhmus",
                "Helena Paju", "Jaak Luik", "Liina Soo", "Urmas Kask", "Kersti Kütt",
                "Tanel Karu", "Kristi Pilv", "Indrek Mölder", "Malle Roos", "Tarmo Ots",
                "Maria Laur", "Ants Sild", "Tiina Pärna", "Ago Jõgi", "Anneli Aas",
                "Heiki Kull", "Kaire Salu", "Raivo Karu", "Maarja Kivi", "Meelis Ojamaa",
                "Piia Rohtla", "Aivar Põld", "Eve Kivi", "Märt Kruus", "Liisi Nurm",
                "Lauri Jõesaar", "Sirje Leht", "Ivo Sarapuu", "Kaie Laane", "Sander Kask",
                "Triin Kuusk", "Mati Koppel", "Marika Kaur", "Kristjan Tamm", "Annika Luht",
                "Tõnis Vaher", "Kadri Pärtel", "Rain Oja", "Piret Lill", "Kaido Uibo",
                "Kerli Palu", "Oliver Rand", "Pille Lipp", "Mihkel Orav", "Tiia Toom",
                "Alar Karu", "Merle Kuus", "Magnus Luik", "Liina Laas", "Alari Rebane",
                "Katrin Kalda", "Marko Mänd", "Signe Rattas", "Raul Kruuse", "Reet Kivi",
                "Erki Kaasik", "Kairi Tamme", "Kalev Laur", "Tiina Roos", "Siim Aru",
                "Liisa Mets", "Olev Meri", "Kati Koppel", "Tõnu Pärn", "Maie Pärna",
                "Andrus Kask", "Katariina Luik", "Erik Saar", "Marta Ots", "Aare Teder",
                "Sirli Uus", "Marek Laine", "Hanna Jõgi", "Jüri Rand", "Külli Aas",
                "Hannes Lõhmus", "Triin Toom", "Mart Liiv", "Egle Pukk", "Tauno Kalk",
                "Silvia Põder", "Paul Kurg", "Merle Arula", "Rauno Raudsepp", "Ly Õun",
                "Kaspar Sepp", "Elina Mänd", "Sten Ilves", "Eha Lepik", "Villu Ojamaa",
                "Ene Ivask", "Silver Tali", "Marit Laur", "Aivar Soo", "Elve Pärtel",
                "Jaanus Kõiv", "Kersti Kaasa", "Sven Kaur", "Kaja Kull", "Tarvo Tamm",
                "Leida Mägi", "Ain Oja", "Mailis Roos", "Janar Sild", "Krista Org",
                "Jarek Pruul", "Maret Vaher", "Vahur Kukk", "Kristel Saarma", "Taavi Tali",
                "Helen Lepp", "Toivo Sarapuu", "Liisi Kaljuste", "Kaur Lember", "Tuuli Laht",
                "Veiko Käo", "Piia Pärna", "Riho Lõhmus", "Leelo Rand", "Alar Mäeots",
                "Heli Ots", "Tõnu Altmäe", "Ülle Toom", "Innar Teder", "Piret Kuusk",
                "Enno Veski", "Karin Pärn", "Enn Lepp", "Eve Liiber", "Kalmer Lepik",
                "Maire Kukk", "Rando Aas", "Marion Valk", "Kaido Peterson", "Ingrid Kask",
                "Alvar Kallas", "Milvi Karu", "Indrek Roos", "Terje Luik", "Gert Tamm",
                "Maie Laur", "Janno Mölder", "Tiia Hein", "Tarmo Aru", "Margit Teder",
                "Jürgen Raudsepp", "Anneli Kaasik", "Margo Sepp", "Evelin Rohtla", "Joel Lepik",
                "Irja Oja", "Imre Kangur", "Astrid Lõhmus", "Veljo Rebane", "Aire Kull",
                "Sulev Luik", "Reelika Rand", "Arno Järv", "Annika Kull", "Peep Ots",
                "Maris Pärn", "Arvo Laine", "Liisi Kask", "Kaido Paju", "Kelli Lepp"
        };

        String[] emailid = new String[]{
                "andres.tamm@gmail.com", "tiina.kask@gmail.com", "mart.sepp@gmail.com", "kristiina.rebane@gmail.com", "jaan.magi@gmail.com",
                "liis.saar@gmail.com", "rein.kukk@gmail.com", "kati.lepp@gmail.com", "toomas.kaasik@gmail.com", "kadri.kuusk@gmail.com",
                "margus.kivi@gmail.com", "anna.ilves@gmail.com", "tonu.lepik@gmail.com", "mari.oja@gmail.com", "siim.kallas@gmail.com",
                "piret.parn@gmail.com", "karl.vaher@gmail.com", "laura.koppel@gmail.com", "peeter.raud@gmail.com", "tiiu.mannik@gmail.com",
                "priit.jarv@gmail.com", "katrin.mand@gmail.com", "madis.poder@gmail.com", "eva.teder@gmail.com", "taavi.lohmus@gmail.com",
                "helena.paju@gmail.com", "jaak.luik@gmail.com", "liina.soo@gmail.com", "urmas.kask@gmail.com", "kersti.kutt@gmail.com",
                "tanel.karu@gmail.com", "kristi.pilv@gmail.com", "indrek.molder@gmail.com", "malle.roos@gmail.com", "tarmo.ots@gmail.com",
                "maria.laur@gmail.com", "ants.sild@gmail.com", "tiina.parna@gmail.com", "ago.jogi@gmail.com", "anneli.aas@gmail.com",
                "heiki.kull@gmail.com", "kaire.salu@gmail.com", "raivo.karu@gmail.com", "maarja.kivi@gmail.com", "meelis.ojamaa@gmail.com",
                "piia.rohtla@gmail.com", "aivar.pold@gmail.com", "eve.kivi@gmail.com", "mart.kruus@gmail.com", "liisi.nurm@gmail.com",
                "lauri.joesaar@gmail.com", "sirje.leht@gmail.com", "ivo.sarapuu@gmail.com", "kaie.laane@gmail.com", "sander.kask@gmail.com",
                "triin.kuusk@gmail.com", "mati.koppel@gmail.com", "marika.kaur@gmail.com", "kristjan.tamm@gmail.com", "annika.luht@gmail.com",
                "tonis.vaher@gmail.com", "kadri.partel@gmail.com", "rain.oja@gmail.com", "piret.lill@gmail.com", "kaido.uibo@gmail.com",
                "kerli.palu@gmail.com", "oliver.rand@gmail.com", "pille.lipp@gmail.com", "mihkel.orav@gmail.com", "tiia.toom@gmail.com",
                "alar.karu@gmail.com", "merle.kuus@gmail.com", "magnus.luik@gmail.com", "liina.laas@gmail.com", "alari.rebane@gmail.com",
                "katrin.kalda@gmail.com", "marko.mand@gmail.com", "signe.rattas@gmail.com", "raul.kruuse@gmail.com", "reet.kivi@gmail.com",
                "erki.kaasik@gmail.com", "kairi.tamme@gmail.com", "kalev.laur@gmail.com", "tiina.roos@gmail.com", "siim.aru@gmail.com",
                "liisa.mets@gmail.com", "olev.meri@gmail.com", "kati.koppel@gmail.com", "tonu.parn@gmail.com", "maie.parna@gmail.com",
                "andrus.kask@gmail.com", "katariina.luik@gmail.com", "erik.saar@gmail.com", "marta.ots@gmail.com", "aare.teder@gmail.com",
                "sirli.uus@gmail.com", "marek.laine@gmail.com", "hanna.jogi@gmail.com", "juri.rand@gmail.com", "kulli.aas@gmail.com",
                "hannes.lohmus@gmail.com", "triin.toom@gmail.com", "mart.liiv@gmail.com", "egle.pukk@gmail.com", "tauno.kalk@gmail.com",
                "silvia.poder@gmail.com", "paul.kurg@gmail.com", "merle.arula@gmail.com", "rauno.raudsepp@gmail.com", "ly.oun@gmail.com",
                "kaspar.sepp@gmail.com", "elina.mand@gmail.com", "sten.ilves@gmail.com", "eha.lepik@gmail.com", "villu.ojamaa@gmail.com",
                "ene.ivask@gmail.com", "silver.tali@gmail.com", "marit.laur@gmail.com", "aivar.soo@gmail.com", "elve.partel@gmail.com",
                "jaanus.koiv@gmail.com", "kersti.kaasa@gmail.com", "sven.kaur@gmail.com", "kaja.kull@gmail.com", "tarvo.tamm@gmail.com",
                "leida.magi@gmail.com", "ain.oja@gmail.com", "mailis.roos@gmail.com", "janar.sild@gmail.com", "krista.org@gmail.com",
                "jarek.pruul@gmail.com", "maret.vaher@gmail.com", "vahur.kukk@gmail.com", "kristel.saarma@gmail.com", "taavi.tali@gmail.com",
                "helen.lepp@gmail.com", "toivo.sarapuu@gmail.com", "liisi.kaljuste@gmail.com", "kaur.lember@gmail.com", "tuuli.laht@gmail.com",
                "veiko.kao@gmail.com", "piia.parna@gmail.com", "riho.lohmus@gmail.com", "leelo.rand@gmail.com", "alar.maeots@gmail.com",
                "heli.ots@gmail.com", "tonu.altmae@gmail.com", "ulle.toom@gmail.com", "innar.teder@gmail.com", "piret.kuusk@gmail.com",
                "enno.veski@gmail.com", "karin.parn@gmail.com", "enn.lepp@gmail.com", "eve.liiber@gmail.com", "kalmer.lepik@gmail.com",
                "maire.kukk@gmail.com", "rando.aas@gmail.com", "marion.valk@gmail.com", "kaido.peterson@gmail.com", "ingrid.kask@gmail.com",
                "alvar.kallas@gmail.com", "milvi.karu@gmail.com", "indrek.roos@gmail.com", "terje.luik@gmail.com", "gert.tamm@gmail.com",
                "maie.laur@gmail.com", "janno.molder@gmail.com", "tiia.hein@gmail.com", "tarmo.aru@gmail.com", "margit.teder@gmail.com",
                "jurgen.raudsepp@gmail.com", "anneli.kaasik@gmail.com", "margo.sepp@gmail.com", "evelin.rohtla@gmail.com", "joel.lepik@gmail.com",
                "irja.oja@gmail.com", "imre.kangur@gmail.com", "astrid.lohmus@gmail.com", "veljo.rebane@gmail.com", "aire.kull@gmail.com",
                "sulev.luik@gmail.com", "reelika.rand@gmail.com", "arno.jarv@gmail.com", "annika.kull@gmail.com", "peep.ots@gmail.com",
                "maris.parn@gmail.com", "arvo.laine@gmail.com", "liisi.kask@gmail.com", "kaido.paju@gmail.com", "kelli.lepp@gmail.com"
        };

        int esimesesKlassisKohtiLoendur = 2; //et kasutaja saaks alati valida esimeses klassis koha ja seda vahetada
        int ariKlassisKohtiLoendur = 2;

        for (int i = 0; i < maxMahutuvusAlla80Protsendi; i++) {
            int suvalineIsik = (int) (Math.random() * nimed.length);
            String nimi = nimed[suvalineIsik];
            String email = emailid[suvalineIsik];
            String passinumber = "KF" + (int) (Math.random() * 9000000 + 1000000);

            int juhuArv1kuni100 = (int) (Math.random() * 100 + 1);
            int loplikReisiklass = getLoplikReisiklass(juhuArv1kuni100, esimesesKlassisKohtiLoendur, ariKlassisKohtiLoendur);
            String istekoht = suvalineIstekoht(istumisJaotus, loplikReisiklass);

            Reisija reisija = switch (loplikReisiklass) {
                case 1 -> {
                    esimesesKlassisKohtiLoendur++;
                    yield new EsimeseKlassiReisija(nimi, email, passinumber, istekoht);
                }
                case 2 -> {
                    ariKlassisKohtiLoendur++;
                    yield new AriKlassiReisija(nimi, email, passinumber, istekoht);
                }
                default -> new TuristiKlassiReisija(nimi, email, passinumber, istekoht);
            };

            if (istekoht != null) {
                reisijad.add(reisija);
                istumisJaotus.määraIstekohtVõetuks(istekoht, reisija);
            }
        }

    }

    private int getLoplikReisiklass(int juhuArv1kuni100, int esimesesKlassisKohtiLoendur, int ariKlassisKohtiLoendur) {
        int reisiklass;

        if (juhuArv1kuni100 < 10) {
            reisiklass = 1;
        } else if (juhuArv1kuni100 < 30) {
            reisiklass = 2;
        } else reisiklass = 3;

        int loplikReisiklass = reisiklass;

        if (loplikReisiklass == 1 && esimesesKlassisKohtiLoendur >= esimesesKlassisKohti) {
            loplikReisiklass = 2;
        }

        if (loplikReisiklass == 2 && ariKlassisKohtiLoendur >= ariKlassisKohti) {
            loplikReisiklass = 3;
        }
        return loplikReisiklass;
    }

    //funktsioon, mis lihtsustab kasutajal käsurealt info lugemist, oodates kindla aja
    public static void oota(int millisekundit) {
        try {
            Thread.sleep(millisekundit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
