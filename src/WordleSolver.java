import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class WordleSolver {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("words5.txt"));
        char[] charsLeft = new char[] {'d','j','z','x','v','b','m', 'p', 'a'};
        while(in.hasNext()) {
            String word = in.next();
            boolean valid = true;
            for (char c : word.toCharArray()) {
                boolean contains = false;
                for(char c2 : charsLeft) {
                    if(c == c2) {
                        contains = true;
                        break;
                    }
                }
                if(!contains) {
                    valid = false;
                    break;
                }
            }
            if(!valid) {
                continue;
            }

            if(word.charAt(2) == 'a' && word.contains("p")) {
                System.out.println(word);
            }
        }
    }
}
