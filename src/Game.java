import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacUtils;
import java.security.SecureRandom;
import java.util.*;

public class Game {
    public static void main (String[] args) {
        if (args_check(args)) {
            HashMap<String, String[]> wins_dict = win_dict(args);
            String key = get_key().toUpperCase();
            String pc_choise = get_pc_choise(args);
            System.out.println("HMAC:\n" + HmacUtils.hmacSha256Hex(key, pc_choise).toUpperCase());
            int person_choise = get_person_move(args);
            if (person_choise >0 && person_choise <= args.length) {
                System.out.println("Your move: " + args[person_choise-1]);
                System.out.println("Computer move: " + pc_choise);
                if (Arrays.asList(wins_dict.get(args[person_choise-1])).contains(pc_choise)) {
                    System.out.println("You lose!");
                } else if (pc_choise.equals(args[person_choise - 1])) {
                    System.out.println("Draw!");
                } else {
                    System.out.println("You win!");
                }
                System.out.println("HMAC key: " + key);
            }
        }
    }

    private static int get_person_move (String[] args) {
        int variable;
        do {
            System.out.println("Availeble moves:");
            for (int i = 1; i <= args.length; i++) {
                System.out.println(i + " - " + args[i - 1]);
            }
            System.out.println("0 - exit");
            variable = get_input();
        } while (variable < 0 || variable > args.length);
        return variable;
    }

    private static int get_input() {
        System.out.print("Enter your move: ");
        int input;
        try{
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextInt();
        } catch (InputMismatchException e) {
            input = -1;
        }
        return input;
    }

    private static String get_pc_choise (String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        int index = secureRandom.nextInt(args.length-1);
        return args[index];
    }

    private static String get_key () {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key_bates = new byte[16];
        secureRandom.nextBytes(key_bates);
        return Hex.encodeHexString(key_bates);
    }

    private static boolean check_differense(String[] args) {
        Arrays.sort(args);
        if (args.length == 0) {return false;}
        for (int i = 0; i+1 < args.length; i++){
            if (args[i].equals(args[i+1])) {return false;}
        }
        return true;
    }

    private static boolean args_check (String[] args){
        boolean[] bool_errors= {args.length != 0, args.length >= 3, args.length % 2 == 1, check_differense(args)};
        String[] errors_description = {"Parameters are not set.", "Count of accepted parameters must be equal 3 or more.", "Count of accepted parameters must be odd.", "Accepted parameters must be different."};
        if(bool_errors[0] && bool_errors[1] && bool_errors[2] && bool_errors[3]) {
            return true;
        } else {
            System.out.println("Error!");
            for (int i = 1, j = 0; j < bool_errors.length; j++) {
                if (!bool_errors[j]) {
                    System.out.println(i + ". " + errors_description[j]);
                    i++;
                }
            }
            System.out.println("Example: java -jar Game.java A B C D E");
            return false;
        }
    }

    private static HashMap<String, String[]> win_dict (String[] args){
        HashMap<String, String[]> wins = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String value;
            String[] win_values = new String[args.length/2];
            value = args[i];
            for (int x = 1; x <= args.length/2 ; x++) {
                if (i + x < args.length) {
                    win_values[x-1] = args[i+x];
                } else {
                    win_values[x-1] = args[i+x-args.length];
                }
            }
            wins.put(value, win_values);
        }
        return wins;
    }
}