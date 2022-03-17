import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
        //tolerance allows boards without the maximum score for a round of moves to pass
        //higher tolerance sometimes results in lower total score

        for(int tolerance = 2; tolerance <= 2; tolerance++) {
            Scanner boardsIn = new Scanner(new File("boards" + BOARD_SIZE + ".txt"));

            int boardI = 0;

            PrintWriter out = new PrintWriter("solutions" + BOARD_SIZE + "-tol" + tolerance + ".txt");
            PrintWriter out1 = new PrintWriter("moves" + BOARD_SIZE + "-tol" + tolerance + ".txt");

            double startTime = System.currentTimeMillis();
            while (boardsIn.hasNext()) {
                char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
                for (int i = 0; i < BOARD_SIZE; i++) {
                    board[i] = boardsIn.next().toCharArray();
                }

                //Generates every possible move
//            for(int i = 0; i < BOARD_SIZE * BOARD_SIZE * 2; i++) {
//                Move nextMove = new Move((i < BOARD_SIZE * BOARD_SIZE) ? "r" : "c", (i%(BOARD_SIZE*BOARD_SIZE))/BOARD_SIZE, i%BOARD_SIZE);
//            }

                ArrayList<Sim> boardsToCheck = new ArrayList<>();
                boardsToCheck.add(new Sim(board, tolerance));

                int maxScore = 0;
                for (int j = 0; j < TOTAL_MOVES; j++) {
                    ArrayList<Sim> nextBoardsToCheck = new ArrayList<>();
                    //Parallel array
//                ArrayList<Integer> nextBoardsScores = new ArrayList<>();
                    for (Sim b : boardsToCheck) {
                        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE * 2; i++) {
                            Move nextMove = new Move((i < BOARD_SIZE * BOARD_SIZE) ? "r" : "c", (i % (BOARD_SIZE * BOARD_SIZE)) / BOARD_SIZE, i % BOARD_SIZE);
                            if (nextMove.n == 0) {
                                continue;
                            }
                            Sim nextBoard = b.moveAndScore(nextMove);

                            int score = nextBoard.totalScore;

                            if (score > maxScore) {
                                maxScore = score;
                            }
                            nextBoardsToCheck.add(nextBoard);

                            //prints out the board
//                        for(int r = 0; r < BOARD_SIZE; r++) {
//                            System.out.println(nextBoard[r]);
//                        }
                        }
                    }
                    for (int i = 0; i < nextBoardsToCheck.size(); i++) {
                        Sim e = nextBoardsToCheck.get(i);
                        if (e.totalScore < maxScore) {
                            e.penaltiesLeft--;
                        }
                        if (e.penaltiesLeft <= 0) {
                            nextBoardsToCheck.remove(i);
                            i--;
                        }
                    }
                    boardsToCheck = new ArrayList<>(nextBoardsToCheck);
//                for (Sim sim : nextBoardsToCheck) {
//                    if (sim.totalScore == maxScore) { //only take boards that yielded the max score
//                        boardsToCheck.add(sim);
//                    }
//                }
                }

                Sim maxSim = boardsToCheck.get(0);

                for (Sim b : boardsToCheck) {
                    if (b.totalScore > maxSim.totalScore) {
                        maxSim = b;
                    }
                }

                StringBuilder result = new StringBuilder(boardI + " " + maxSim.totalScore + " ");
                StringBuilder result1 = new StringBuilder();


                for (Move move : maxSim.moves) {
                    result.append(move);
                    result.append(" ");
                    result1.append(move.dir.equals("r") ? 0 : 1).append(" ");
                    result1.append(move.i).append(" ");
                    result1.append(move.n).append(" ");
                }


                System.out.println(result);
                out.println(result);
                out1.println(result1);
                boardI++;
            }

            out.close();
            out1.close();

            boardsIn.close();

            double endTime = System.currentTimeMillis();
            System.out.println("Program run time: " + (endTime - startTime) / 1000 + " seconds");
        }
    }

    static HashSet<String> scoreBoard(char[][] board) {
        HashSet<String> result = new HashSet<>();
        for(int r = 0; r < BOARD_SIZE; r++) {
            String s = String.valueOf(board[r]);
            String rev = new StringBuilder(s).reverse().toString();
            if(words.contains(s)) {
                result.add(s);
            }
            if(words.contains(rev)) {
                result.add(rev);
            }
        }

        for(int c = 0; c < BOARD_SIZE; c++) {
            StringBuilder sb = new StringBuilder();
            for (int r = 0; r < BOARD_SIZE; r++) {
                sb.append(board[r][c]);
            }
            String s = sb.toString();
            String rev = sb.reverse().toString();
            if(words.contains(s)) {
                result.add(s);
            }
            if(words.contains(rev)) {
                result.add(rev);
            }
        }
        return result;
    }

    static class Sim {
        char[][] board;
        ArrayList<Move> moves;
        HashSet<String> wordsFound; //easy way to prevent duplicates
        int totalScore;
        int penaltiesLeft;

        public Sim(char[][] board, int penaltiesLeft) {
            this.board = board;
            moves = new ArrayList<>();
            wordsFound = new HashSet<>();
            this.penaltiesLeft = penaltiesLeft;
        }

        public Sim(char[][] board, ArrayList<Move> moves, HashSet<String> wordsFound, int penaltiesLeft) {
            this.board = board;
            this.moves = moves;
            this.wordsFound = wordsFound;
            this.totalScore = wordsFound.size();
            this.penaltiesLeft = penaltiesLeft;
        }

        public Sim moveAndScore(Move move) {
            char[][] result = new char[BOARD_SIZE][];
            for (int i = 0; i < BOARD_SIZE; i++) {
                result[i] = board[i].clone();
            }

            if (move.dir.equals("r")) {
                for (int i = 0; i < BOARD_SIZE; i++) {
                    result[move.i][(i + move.n + BOARD_SIZE) % BOARD_SIZE] = board[move.i][i];
                }
            } else {
                for (int i = 0; i < BOARD_SIZE; i++) {
                    result[(i + move.n + BOARD_SIZE) % BOARD_SIZE][move.i] = board[i][move.i];
                }
            }

            ArrayList<Move> nextMoves = new ArrayList<>(moves);
            nextMoves.add(move);

            HashSet<String> nextWordsFound = new HashSet<>(wordsFound);
            nextWordsFound.addAll(scoreBoard(result));

            return new Sim(result, nextMoves, nextWordsFound, penaltiesLeft);
        }

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
            return "Move(" +
                    "dir='" + dir + '\'' +
                    ", i=" + i +
                    ", n=" + n +
                    ')';
        }
    }
}
