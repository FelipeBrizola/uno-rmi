import java.util.Scanner;

public class Helper {

    public static String intColorToStrColor(int color) {

        switch (color) {
        case 0:
            return "BLUE";
        case 2:
            return "GREEN";
        case 3:
            return "RED";
        case 1:
            return "YELLOW";

        default:
            return "";
        }
    }

    public static boolean isInt(String number) {
        try {
            Integer.parseInt(number);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String playWithJoker(Scanner scanner) {
        System.out.println("Selecione o Id da proxima cor ativa: ");
        System.out.println("Id: 0  | BLUE");
        System.out.println("Id: 1  | YELLOW");
        System.out.println("Id: 2  | GREEN");
        System.out.println("Id: 3  | RED");
        return scanner.nextLine();
    }
}