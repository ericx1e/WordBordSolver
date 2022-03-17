import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class merger { //finds the max score and moves across different tolerances
    static final int BOARD_SIZE = 4;
    static final int MAX_TOL = 3;
    public static void main(String[] args) throws FileNotFoundException {
        Scanner[] scanners = new Scanner[MAX_TOL];
        Scanner[] moveScanners = new Scanner[MAX_TOL];
        for(int i = 0; i < MAX_TOL; i++) {
            scanners[i] = new Scanner(new File("solutions" + BOARD_SIZE + "-tol" + (i+1) + ".txt"));
            moveScanners[i] = new Scanner(new File("moves" + BOARD_SIZE + "-tol" + (i+1) + ".txt"));
        }

        PrintWriter out = new PrintWriter("solutions" + BOARD_SIZE + ".txt");
        PrintWriter out1 = new PrintWriter("moves" + BOARD_SIZE + ".txt");

        while(scanners[0].hasNext()) {
            int maxI = 0;
            String maxLine = scanners[0].nextLine();
            int maxScore = Integer.parseInt(maxLine.split(" ")[1]);
            for(int i = 1; i < MAX_TOL; i++) {
                String line = scanners[i].nextLine();
                int score = Integer.parseInt(line.split(" ")[1]);
                if(score > maxScore) {
                    maxI = i;
                    maxLine = line;
                    maxScore = score;
                }
            }

            System.out.println(maxLine);
            out.println(maxLine);

            for(int i = 0; i < MAX_TOL; i++) {
                String line = moveScanners[i].nextLine();
                if(i == maxI) {
                    out1.println(line);
                }
            }
        }

        for(int i = 0; i < MAX_TOL; i++) {
            scanners[i].close();
            moveScanners[i].close();
        }
        out.close();
    }
}
