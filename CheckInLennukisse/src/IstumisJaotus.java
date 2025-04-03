import java.util.ArrayList;
import java.util.List;

public class IstumisJaotus {
    private Reisija[][] istumisMaatriks;
    private int maxMahutavus;
    private List<Reisija> kasutajaReisijad;


    public IstumisJaotus(int ridadeArv, int kohtiReas) {
        ridadeArv = Math.min(Math.max(10, ridadeArv), 99); // 10-99 rida
        kohtiReas = Math.min(Math.max(2, kohtiReas), 8); // 2-8 kohta reas ehk tähed A-H

        this.maxMahutavus = ridadeArv * kohtiReas;
        this.istumisMaatriks = new Reisija[ridadeArv][kohtiReas];
        this.kasutajaReisijad = new ArrayList<>();
    }

    public void lisaKasutajaReisija(Reisija reisija) {
        if (reisija != null && !kasutajaReisijad.contains(reisija)) {
            kasutajaReisijad.add(reisija);
        }
    }

    public boolean kasVabaIstekoht(String istekoht) {
        int[] istekohtArr = isteKohaStringArvudeks(istekoht);
        int koht = istekohtArr[0];
        int rida = istekohtArr[1];

        return this.istumisMaatriks[rida - 1][koht] == null;
    }

    public void määraIstekohtVõetuks(String istekoht, Reisija reisija) {
        int[] kohtJaRida = this.isteKohaStringArvudeks(istekoht);
        Reisija[][] reisijad = this.getIstumisMaatriks();
        reisijad[kohtJaRida[1] - 1][kohtJaRida[0]] = reisija;
        this.setIstumisMaatriks(reisijad);
    }

    public void vabastaIstekoht(String istekoht) {
        int[] kohtJaRida = this.isteKohaStringArvudeks(istekoht);
        Reisija[][] reisijad = this.getIstumisMaatriks();
        reisijad[kohtJaRida[1] - 1][kohtJaRida[0]] = null;
        this.setIstumisMaatriks(reisijad);
    }

    /**
     * Teisendab istekoha sõne formaadis "ridaTäht" (nt "1A") massiiviks, kus
     * esimene element on koht (tähe indeks) ja teine element on rida.
     *
     * @param iste Istekoha sõne (nt "1A", "12B")
     * @return Massiiv kujul [koht, rida] või [-1, -1] kui sisend on vigane
     */
    public int[] isteKohaStringArvudeks(String iste) {

        if (iste == null || iste.isEmpty()) {
            System.out.println("Vigane sisend: istekoht ei saa olla tühi.");
            return new int[]{-1, -1};
        }

        if (iste.length() < 2 || !Character.isLetter(iste.charAt(iste.length() - 1))) {
            System.out.println("Sisestatud istekoht " + iste + " ei vasta formaadile (nr + täht).");
            return new int[]{-1, -1};
        }

        char kohaTaht = iste.charAt(iste.length() - 1);
        int rida;

        try {
            rida = Integer.parseInt(iste.substring(0, iste.length() - 1));
            if (rida < 1 || rida > istumisMaatriks.length) {
                System.out.println("Rida " + rida + " on väljaspool vahemikku 1-" + istumisMaatriks.length);
                return new int[]{-1, -1};
            }
        } catch (NumberFormatException e) {
            System.out.println("Sisestatud reanumber ei ole korrektne: " + iste);
            return new int[]{-1, -1};
        }

        int koht = switch (Character.toUpperCase(kohaTaht)) {
            case 'A' -> 0;
            case 'B' -> 1;
            case 'C' -> 2;
            case 'D' -> 3;
            case 'E' -> 4;
            case 'F' -> 5;
            case 'G' -> 6;
            case 'H' -> 7;
            default -> {
                System.out.println("Kohatäht " + kohaTaht + " pole lubatud (A-" +
                        (char)('A' + istumisMaatriks[0].length - 1) + ")");
                yield -1;
            }
        };

        if (koht < 0 || koht >= istumisMaatriks[0].length) {
            System.out.println("Koht " + kohaTaht + " on väljaspool lubatud vahemikku.");
            return new int[]{-1, -1};
        }

        return new int[]{koht, rida};
    }

    /**
     * Kuvab lennuki istumisjaotuse visuaalselt käsureale.
     * Väljundis on:
     * - read nummerdatud (1,2,3...)
     * - kohad tähistatud tähtedega (A,B,C...)
     * - vabad kohad märgitud tähisega "o "
     * - hõivatud kohad märgitud tähisega "x "
     * - kasutaja valitud kohad märgitud tähisega "❎"
     * - vahekäik on märgitud vertikaalselt tähisega "¦"
     *
     * @return Istumisplaani tekstiline formaat koos ridade, kohtade ja sümbolitega
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int rows = istumisMaatriks.length;
        int cols = istumisMaatriks[0].length;

        int borderWidth;
        switch (cols) {
            case 2 -> borderWidth = 9;
            case 3 -> borderWidth = 11;
            case 4 -> borderWidth = 13;
            case 5 -> borderWidth = 15;
            case 6 -> borderWidth = 17;
            case 7 -> borderWidth = 19;
            case 8 -> borderWidth = 21;
            default -> borderWidth = 0;
        }

        sb.append("   ┌").append("-".repeat(borderWidth)).append("┐\n");

        for (int i = 0; i < rows; i++) {

            if (i < 9) sb.append(" ");
            sb.append(i + 1).append(" | ");

            for (int j = 0; j < cols; j++) {

                if (j == cols / 2) {
                    sb.append(" ¦  ");
                }

                boolean onKasutajaReisija = false;
                if (istumisMaatriks[i][j] != null) {
                    for (Reisija kasutaja : kasutajaReisijad) {
                        if (istumisMaatriks[i][j] == kasutaja) {
                            onKasutajaReisija = true;
                            break;
                        }
                    }
                }

                if (onKasutajaReisija) {
                    sb.append("❎");
                } else {
                    sb.append(istumisMaatriks[i][j] != null ? "x " : "o ");
                }
            }

            sb.append("|\n");

            if (i < rows - 1) {
                sb.append("   |");

                if (cols % 2 == 1) {
                    int separatorLength;
                    switch (cols) {
                        case 3 -> separatorLength = 2;
                        case 5 -> separatorLength = 3;
                        case 7 -> separatorLength = 4;
                        default -> separatorLength = 1;
                    }

                    sb.append("-".repeat((cols / 2) + separatorLength))
                            .append(" ¦ ")
                            .append("-".repeat(cols + 2))
                            .append("|\n");
                } else {
                    sb.append("-".repeat(cols + 1))
                            .append(" ¦ ")
                            .append("-".repeat(cols + 1))
                            .append("|\n");
                }
            }
        }

        sb.append("   └").append("-".repeat(borderWidth)).append("┘\n");

        sb.append("     ");

        for (int j = 0; j < cols; j++) {

            if (j == cols / 2) {
                sb.append("    ");
            }

            char taht = (char)('A' + j);
            sb.append(taht).append(" ");
        }
        sb.append("\n");

        return sb.toString();
    }

    public Reisija[][] getIstumisMaatriks() {
        return istumisMaatriks;
    }

    public int getMaxMahutavus() {
        return maxMahutavus;
    }

    public int getMaxMahutavusAlla80(){
        int suvalineMahutavusAlla80 = (int) (Math.random() * this.maxMahutavus * 0.8 + 5);
        return suvalineMahutavusAlla80;
    }

    public void setIstumisMaatriks(Reisija[][] istumisMaatriks) {
        this.istumisMaatriks = istumisMaatriks;
    }
}