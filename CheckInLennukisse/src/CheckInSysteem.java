import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CheckInSysteem {
    private String lennunumber;
    private List<Reisija> reisijad = new ArrayList<>();
    private Scanner scanner;

    public CheckInSysteem(String lennunumber) {
        this.lennunumber = lennunumber;
        this.scanner = new Scanner(System.in);
    }

    public void lisaReisija() {
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

        Reisija reisija = switch (reisiklass) {
            case 1 -> new EsimeseKlassiReisija(nimi, email, passinumber);
            case 2 -> new AriKlassiReisija(nimi, email, passinumber);
            default -> new TuristiKlassiReisija(nimi, email, passinumber);
        };

        reisijad.add(reisija);
    }

    public static void main(String[] args) {
        CheckInSysteem checkInSysteem = new CheckInSysteem("9233");
        checkInSysteem.lisaReisija();
    }

}
