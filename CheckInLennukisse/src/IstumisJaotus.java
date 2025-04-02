public class IstumisJaotus {
    private Reisija[][] istumisMaatriks;
    private int maxMahutavus;

    public IstumisJaotus(int ridadeArv, int kohtiReas) {
        ridadeArv = Math.min(Math.max(10, ridadeArv), 99); // 10-99 rida
        kohtiReas = Math.min(Math.max(2, kohtiReas), 8); // 2-8 kohta reas ehk tähed A-H

        this.maxMahutavus = ridadeArv * kohtiReas;
        this.istumisMaatriks = new Reisija[ridadeArv][kohtiReas];
    }

    public IstumisJaotus(int ridadeArv, int kohtiReas, Reisija[] etteAntudReisjad) {
        ridadeArv = Math.min(Math.max(10, ridadeArv), 99); // 10-99 rida
        kohtiReas = Math.min(Math.max(2, kohtiReas), 8); // 2-8 kohta reas ehk tähed A-H

        this.maxMahutavus = ridadeArv * kohtiReas;
        this.istumisMaatriks = new Reisija[ridadeArv][kohtiReas];

        for (Reisija reisija : etteAntudReisjad) {
            määraIstekoht(reisija, reisija.getIstekoht());
        }
    }

    public Reisija[][] getIstumisMaatriks() {
        return istumisMaatriks;
    }

    public int getMaxMahutavus() {
        return maxMahutavus;
    }

    public void setIstumisMaatriks(Reisija[][] istumisMaatriks) {
        this.istumisMaatriks = istumisMaatriks;
    }

    public void määraIstekoht(Reisija reisija, String uusIstekoht) {
        String praeguneIstekoht = reisija.getIstekoht();

        if (praeguneIstekoht != null) {
            int[] istekohtArr = isteKohaStringArvudeks(praeguneIstekoht);
            int koht = istekohtArr[0];
            int rida = istekohtArr[1];

            this.istumisMaatriks[rida-1][koht] = null; //kustuta praegune istekoht ära
        }

        while (kasVabaIstekoht(uusIstekoht)) {
            //küsi uut istekohta ja värki kuni pole võetud
        }

        reisija.setIstekoht(uusIstekoht);
    }

    public boolean kasVabaIstekoht(String istekoht) {
        int[] istekohtArr = isteKohaStringArvudeks(istekoht);
        int koht = istekohtArr[0];
        int rida = istekohtArr[1];

        return this.istumisMaatriks[rida - 1][koht] == null;
    }

    public int[] isteKohaStringArvudeks(String iste) {
        char kohaTaht = iste.charAt(iste.length() - 1);
        int rida = Integer.parseInt(iste.substring(0,iste.length() - 1));

        int koht = switch (kohaTaht) {
            case 'A' -> 0;
            case 'B' -> 1;
            case 'C' -> 2;
            case 'D' -> 3;
            case 'E' -> 4;
            case 'F' -> 5;
            case 'G' -> 6;
            case 'H' -> 7;
            default -> throw new IllegalStateException("Ootamatu istmekoht: " + iste);
        };

        return new int[]{koht, rida};
    }

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
                sb.append(istumisMaatriks[i][j] != null ? "x " : "o ");
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

    public int getMaxMahutavusAlla80(){
        int suvalineMahutavusAlla80 = (int) (Math.random() * this.maxMahutavus * 0.8 + 5);
        return suvalineMahutavusAlla80;
    }
}