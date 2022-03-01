import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class main {
    static final int BOARD_SIZE = 4;
    static final int TOTAL_MOVES = 20;
    static HashSet<String> words;
    public static void main(String[] args) throws FileNotFoundException {
        words = new HashSet<>();
        Scanner wordsIn = new Scanner(new File("words" + BOARD_SIZE + ".txt"));

        while (wordsIn.hasNext()) {
            words.add(wordsIn.next());
        }
        wordsIn.close();

        Scanner boardsIn = new Scanner(new File("boards" + BOARD_SIZE + ".txt"));

        while (boardsIn.hasNext()) {
            char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
            for (int i = 0; i < BOARD_SIZE; i++) {
                board[i] = boardsIn.next().toCharArray();
            }

            //Generates every possible move
//            for(int i = 0; i < BOARD_SIZE * BOARD_SIZE * 2; i++) {
//                Move nextMove = new Move((i < BOARD_SIZE * BOARD_SIZE) ? "r" : "c", (i%(BOARD_SIZE*BOARD_SIZE))/BOARD_SIZE, i%BOARD_SIZE);
//            }

            ArrayList<char[][]> boardsToCheck = new ArrayList<>();
            boardsToCheck.add(board);

            for(int j = 0; j < TOTAL_MOVES; j++) {
                ArrayList<char[][]> nextBoardsToCheck = new ArrayList<>();
                for(char[][] b : boardsToCheck) {
                    for(int i = 0; i < BOARD_SIZE * BOARD_SIZE * 2; i++) {
                        Move nextMove = new Move((i < BOARD_SIZE * BOARD_SIZE) ? "r" : "c", (i%(BOARD_SIZE*BOARD_SIZE))/BOARD_SIZE, i%BOARD_SIZE);
                        if(nextMove.n == 0) {
                            continue;
                        }
                        char[][] nextBoard = move(b, nextMove);

                        if(scoreBoard(nextBoard) >= 200) {

                        }
                        System.out.println(scoreBoard(nextBoard));

//                        for(int r = 0; r < BOARD_SIZE; r++) {
//                            System.out.println(nextBoard[r]);
//                        }
                    }
                }
            }
        }

        boardsIn.close();
    }

    static int scoreBoard(char[][] board) {
        int result = 0;
        for(int r = 0; r < BOARD_SIZE; r++) {
            String s = String.valueOf(board[r]);
            String rev = new StringBuilder(s).reverse().toString();
            //add score and don't double count palindromes
            result+= (words.contains(s) ? 1 : 0) + ((words.contains(rev) ? 1 : 0) - (words.contains(s) && words.contains(rev) && s.equals(rev) ? 1 : 0));
        }

        for(int c = 0; c < BOARD_SIZE; c++) {
            StringBuilder sb = new StringBuilder();
            for (int r = 0; r < BOARD_SIZE; r++) {
                sb.append(board[r][c]);
            }
            String s = sb.toString();
            String rev = sb.reverse().toString();
            result+= (words.contains(s) ? 1 : 0) + ((words.contains(rev) ? 1 : 0) - (words.contains(s) && words.contains(rev) && s.equals(rev) ? 1 : 0));
        }

        return result * 100; //100 points per word
    }


    static char[][] move(char[][] board, Move move) {
        char[][] result = new char[BOARD_SIZE][];
        for(int i = 0; i < BOARD_SIZE; i++) {
            result[i] = board[i].clone();
        }

        if(move.dir.equals("r")) {
            for(int i = 0; i < BOARD_SIZE; i++) {
                result[move.i][(i + move.n + BOARD_SIZE) % BOARD_SIZE] = board[move.i][i];
            }
        } else {
            for(int i = 0; i < BOARD_SIZE; i++) {
                result[(i + move.n + BOARD_SIZE) % BOARD_SIZE][move.i] = board[i][move.i];
            }
        }

        return result;
    }


    static class Move {
        public String dir;
        public int i, n;

        public Move(String dir, int i, int n) {
            this.dir = dir;
            this.i = i;
            this.n = n;
        }

        @Override
        public String toString() {
            return "Move{" +
                    "dir='" + dir + '\'' +
                    ", i=" + i +
                    ", n=" + n +
                    '}';
        }
    }
}
