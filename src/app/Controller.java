//=============================================================================
//PROGRAMMER1: Bikhyat Adhikari
// PANTHER ID1: 6522969
// CLASS: Your class: CAP5507
//
//PROGRAMMER2: Your name
// PANTHER ID2: Your panther ID
// CLASS: Your class: example CAP5706
//
// SEMESTER: Spring 2026
// CLASSTIME: Online
//
// Project: Game Theory Coding Assignment
// DUE: Sunday, April 19, 2025
//
// CERTIFICATION: I certify that this work is my own and that
// none of it is the work of any other person.
//=============================================================================

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app;

import java.util.Scanner;
import java.util.Random;
import java.util.*;

/**
 *
 * @author badhi008
 */
public class Controller {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String mode;
        int rows, cols;
        // Take user inputs
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter (R)andom or (M)anual payoffs entries");
        mode = sc.nextLine();
        System.out.print("Enter the number of rows: ");
        rows = sc.nextInt();
        System.out.print("Enter the number of cols: ");
        cols = sc.nextInt();

        // Define matrices for payoffs
        String[][] u1 = new String[rows][cols];
        String[][] u2 = new String[rows][cols];
        sc.nextLine();
        
        if (mode.toUpperCase().equals("M")) {
            System.out.println("Manual Entries");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    System.out.printf("Enter payoff for (A%d, B%d)= ", i + 1, j + 1);
                    String line = sc.nextLine();
                    String[] inp = line.split("\\s*,\\s*");
                    u1[i][j] = inp[0];
                    u2[i][j] = inp[1];
                }
                System.out.println("-".repeat(50));
            }
            sc.close();
            Controller.displayNormalForm(u1, u2);
            Controller.displayNashEq(u1, u2);
        } else if (mode.toUpperCase().equals("R")) {
            sc.close();
            Random rand = new Random();

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // Player1's payoff
                    u1[i][j] = Integer.toString(rand.nextInt(199) - 99);
                    // Player2's payoff
                    u2[i][j] = Integer.toString(rand.nextInt(199) - 99);
                }
            }

            Controller.displayStrategiesAndPayoffs(u1, u2);
            Controller.displayNormalForm(u1, u2);
            Controller.displayNashEq(u1, u2);
            Controller.displayPayoffRandomBeliefs(u1, u2);
        } else {
            System.out.printf("INVALID SELECTION: ,%s\n", mode);
        }
    }

    public static void displayStrategiesAndPayoffs(String[][] u1, String u2[][]) {
        Controller.printStrategiesAndPayoffs(u1, 1);
        Controller.printStrategiesAndPayoffs(u2, 2);
    }

    public static void displayNormalForm(String[][] u1, String[][] u2) {
        System.out.println("=".repeat(50));
        System.out.println("Display Normal Form");
        System.out.println("=".repeat(50));
//        Controller.printFormattedMatrix(u1, u2);
        Controller.printFormatMatrix(u1, u2);

    }

    public static void displayNashEq(String[][] u1, String[][] u2) {
        String[][] u1Marked = new String[u1.length][u1[0].length];
        String[][] u2Marked = new String[u1.length][u1[0].length];
        // Player 1
        for (int j = 0; j < u1[0].length; j++) {
            int currMax = Integer.MIN_VALUE;
            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < u1.length; i++) {
                int payoff = Integer.parseInt(u1[i][j]);
                if (payoff > currMax) {
                    indices.clear();
                    currMax = payoff;
                    indices.add(i);
                } else if (payoff == currMax) {
                    indices.add(i);
                }
                u1Marked[i][j] = u1[i][j];
            }
            for (int index : indices) {
                u1Marked[index][j] = "H";
            }
        }

        // Player 2
        for (int i = 0; i < u2.length; i++) {
            int currMax = Integer.MIN_VALUE;
            List<Integer> indices = new ArrayList<>();
            for (int j = 0; j < u2[0].length; j++) {
                int payoff = Integer.parseInt(u2[i][j]);
                if (payoff > currMax) {
                    indices.clear();
                    currMax = payoff;
                    indices.add(j);
                } else if (payoff == currMax) {
                    indices.add(j);
                }
                u2Marked[i][j] = u2[i][j];
            }
            for (int index : indices) {
                u2Marked[i][index] = "H";
            }
        }

        // Check for Nash Equilibriums
        String nashEq = "";
        for (int i = 0; i < u1.length; i++) {
            for (int j = 0; j < u1[0].length; j++) {
                if (u1Marked[i][j].equals("H") && u2Marked[i][j].equals("H")) {
                    nashEq += "(A" + (i + 1) + ", B" + (j + 1) + ") ";
                }
            }
        }

        System.out.println("=".repeat(50));
        System.out.println("Nash Equilibrium Locations");
        System.out.println("=".repeat(50));
        Controller.printFormatMatrix(u1Marked, u2Marked);
        if (nashEq.equals("")) {
            System.out.println("Nash Pure Equilibrium(s): None\n");
            if (u1.length == 2 && u1[0].length == 2) {
                Controller.displayIndiffProbabilities(u1, u2);
            }
        } else {
            System.out.printf("Nash Pure Equilibrium(s): %s\n\n", nashEq);
            if (u1.length == 2 && u1[0].length == 2) {
                System.out.println("-".repeat(50) + "\nPlayer 1 & 2 Indifferent Mix Probabilities\n" + "-".repeat(50));
                System.out.println("Normal Form has Pure Strategy Equilibrium");
            }
        }
        System.out.println();

    }

    public static void displayPayoffRandomBeliefs(String[][] u1, String[][] u2) {
        // Player2's belief about player1
        float[] b1 = Controller.generateKProbabilities(u1.length);
        // Player1's beleif about player 2
        float[] b2 = Controller.generateKProbabilities(u1[0].length);

        String header = "-".repeat(50) + "\nPlayer %d Expected Payoffs with Player %d Mixing\n" + "-".repeat(50) + "\n";
        System.out.printf(String.format(header, 1, 2));

        String rowFormat = "U(%s,%s) = %.2f";

        String beleif = "(";
        for (float b : b2) {
            beleif += String.format("%.2f", b);
            beleif += ", ";
        }
        beleif = beleif.substring(0, beleif.length() - 2) + ")";
        float[] p1payoffs = new float[u1.length];
        for (int i = 0; i < u1.length; i++) {
            float total = 0;
            for (int j = 0; j < u1[0].length; j++) {
                total += Integer.parseInt(u1[i][j]) * b2[j];
            }
            p1payoffs[i] = total;
            System.out.print(String.format(rowFormat, "A" + (i + 1), beleif, total));
            System.out.println();
        }
        System.out.println();
        String footer = "-".repeat(50) + "\nPlayer %d Best Response with Player %d Mixing\n" + "-".repeat(50) + "\n";

        System.out.printf(String.format(footer, 1, 2));
//        System.out.println();

        float p1BestPayOff = Float.NEGATIVE_INFINITY;
        List<Integer> p1BestResponseIndices = new ArrayList<>();
        for (int i = 0; i < p1payoffs.length; i++) {
            if (p1payoffs[i] > p1BestPayOff) {
                p1BestResponseIndices.clear();
                p1BestResponseIndices.add(i);
                p1BestPayOff = p1payoffs[i];
            } else if (p1BestPayOff == p1payoffs[i]) {
                p1BestResponseIndices.add(i);
            }
        }

        String p1BestResponses = "{";
        for (int i : p1BestResponseIndices) {
            p1BestResponses += "A" + (i + 1) + ", ";
        }
        p1BestResponses = p1BestResponses.substring(0, p1BestResponses.length() - 2) + "}\n";
        System.out.printf("BR%s = %s", beleif, p1BestResponses);
        System.out.println();
        System.out.printf(String.format(header, 2, 1));
        String beleifs = beleif;
        beleif = "(";
        for (float b : b1) {
            beleif += String.format("%.2f", b);
            beleif += ", ";
        }
        beleif = beleif.substring(0, beleif.length() - 2) + ")";
        float[] p2payoffs = new float[u2[0].length];
        for (int j = 0; j < u2[0].length; j++) {
            float total = 0;
            for (int i = 0; i < u2.length; i++) {
                total += Integer.parseInt(u2[i][j]) * b1[i];
            }
            p2payoffs[j] = total;
            System.out.print(String.format(rowFormat, beleif, "B" + (j + 1), total));
            System.out.println();
        }
        System.out.println();
        System.out.printf(String.format(footer, 2, 1));
//        System.out.println();

        float p2BestPayOff = Float.NEGATIVE_INFINITY;
        List<Integer> p2BestResponseIndices = new ArrayList<>();
        for (int i = 0; i < p2payoffs.length; i++) {
            if (p2payoffs[i] > p2BestPayOff) {
                p2BestResponseIndices.clear();
                p2BestResponseIndices.add(i);
                p2BestPayOff = p2payoffs[i];
            } else if (p2BestPayOff == p2payoffs[i]) {
                p2BestResponseIndices.add(i);
            }
        }

        String p2BestResponses = "{";
        for (int i : p2BestResponseIndices) {
            p2BestResponses += "B" + (i + 1) + ", ";
        }
        p2BestResponses = p2BestResponses.substring(0, p2BestResponses.length() - 2) + "}\n";
        System.out.printf("BR%s = %s", beleif, p2BestResponses);
        System.out.println();
        beleifs = beleif + ", " + beleifs;
        String beleifFormat = "Player %d -> U(" + beleifs + ")= %.2f";

        System.out.println("-".repeat(50) + "\nPlayer 1 & 2 Expected payoffs with both Players Mixing\n" + "-".repeat(50));

        System.out.printf(String.format(beleifFormat, 1, Controller.calculateExpectedPayOff(u1, b1, b2)));
        System.out.println();
        System.out.printf(String.format(beleifFormat, 2, Controller.calculateExpectedPayOff(u2, b1, b2)));
        System.out.println();

    }

    public static void displayIndiffProbabilities(String[][] u1, String[][] u2) {
        System.out.println("-".repeat(50) + "\nPlayer 1 & 2 Indifferent Mix Probabilities\n" + "-".repeat(50));
        String rowFormat = "Player %d probability of strategies (%s%d) = %.2f\n";
        float[][] u = Controller.stringToNumericArray(u1);
        float p = (u[1][1] - u[1][0]) / (u[0][0] - u[0][1] - u[1][0] + u[1][1]);
        System.out.printf(String.format(rowFormat, 1, "A", 1, p));
        System.out.printf(String.format(rowFormat, 1, "A", 2, 1 - p));
        p = (u[1][1] - u[0][1]) / (u[0][0] - u[0][1] - u[1][0] + u[1][1]);
        System.out.printf(String.format(rowFormat, 2, "B", 1, p));
        System.out.printf(String.format(rowFormat, 2, "B", 2, 1 - p));
    }

    private static float[][] stringToNumericArray(String[][] u) {
        float[][] res = new float[u.length][u[0].length];
        for (int i = 0; i < u.length; i++) {
            for (int j = 0; j < u[0].length; j++) {
                res[i][j] = Float.parseFloat(u[i][j]);
            }
        }

        return res;
    }

    private static float calculateExpectedPayOff(String[][] u, float[] p1, float[] p2) {
        float expectedPayOff = 0;
        for (int i = 0; i < u.length; i++) {
            for (int j = 0; j < u[0].length; j++) {
                expectedPayOff += p1[i] * p2[j] * Integer.parseInt(u[i][j]);
            }
        }
        return expectedPayOff;
    }

    private static void printBestResponses(String[][] u, float[] probs) {

    }

    private static float[] generateKProbabilities(int k) {
        float[] probs = new float[k];
        Random rand = new Random();
        float total = 0;
        for (int i = 0; i < k; i++) {
            probs[i] = rand.nextFloat();
            total += probs[i];
        }
        if (total == 0.0f) {
            return Controller.generateKProbabilities(k);
        }
        for (int i = 0; i < k; i++) {
            probs[i] /= total;
        }
        return probs;
    }

    private static void printStrategiesAndPayoffs(String[][] u, int n) {
        System.out.println("-".repeat(50));
        System.out.printf("Player: Player%d's strategies\n", n);
        System.out.println("-".repeat(50));
        String strategies = "{";
        if (n == 1) {
            for (int i = 1; i <= u.length; i++) {
                strategies = strategies + "A" + i + ", ";
            }
        } else {
            for (int i = 1; i <= u[0].length; i++) {
                strategies = strategies + "B" + i + ", ";
            }
        }
        strategies = strategies.substring(0, strategies.length() - 2);
        System.out.println(strategies + "}\n");
        System.out.println("-".repeat(50));
        System.out.printf("Player: Player%d's payoffs\n", n);
        System.out.println("-".repeat(50));

        for (String[] u11 : u) {
            String row = "";
            for (String cell : u11) {
                row += String.format("%4s,", cell);
            }
            row = row.substring(0, row.length() - 1);
            System.out.println(row);
        }
        System.out.println();
    }

    private static void printFormatMatrix(String[][] u1, String[][] u2) {
        int rows = u1.length;
        int cols = u2[0].length;

        // Print Player 2 headers
        System.out.print(" ".repeat(3) + "| ");
        for (int j = 0; j < cols; j++) {
            String header = "B" + (j + 1);
            System.out.print(Controller.getPaddedString(header, 10, " ".charAt(0)) + "|");
        }
        System.out.print("\n" + "-".repeat(cols * 11 + 5) + "\n");

        // Print each row
        for (int i = 0; i < rows; i++) {
            System.out.printf("%-5s", "A" + (i + 1) + " |");
            for (int j = 0; j < cols; j++) {
                String cell = String.format("(%s,%s)", u1[i][j], u2[i][j]);
                System.out.print(Controller.getPaddedString(cell, 10, " ".charAt(0)) + "|");
            }
            System.out.print("\n" + "-".repeat(cols * 11 + 5) + "\n");
        }
        System.out.println();
    }

    // String formatting code, to center align a String for a given length.
    // The code is modified version of https://stackoverflow.com/questions/34467630/how-to-center-a-string-by-formatting-in-java
    private static String getPaddedString(String str, int totalLength, char paddingChar) {
        if (str == null) {
            str = "";
        }

        int strLength = str.length();
        if (strLength >= totalLength) {
            return str; // string longer than total length, return as is
        }

        int totalPadding = totalLength - strLength;
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < leftPadding; i++) {
            sb.append(paddingChar);
        }
        sb.append(str);
        for (int i = 0; i < rightPadding; i++) {
            sb.append(paddingChar);
        }

        return sb.toString();
    }
}
