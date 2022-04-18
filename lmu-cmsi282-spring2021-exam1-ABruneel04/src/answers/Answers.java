package answers;

import java.util.*;

/**
 * [!] Consult the Exam PDF in the /doc subfolder for information on how to complete
 *     the questions on the exam!
 */
public class Answers {
    
    // ======================================================================
    // Section One - Conceptual Potpourri
    // ======================================================================
    
    // Question 1.1.
    // ----------------------------------------------------------------------
    
    /**
     * Answer the question on your exam PDF by returning the Boolean associated
     * with your choice (i.e., true or false)
     *
     * @return A Boolean containing your answer
     */
    public static Boolean question1_1_1 () {
        return false;
    }
    
    /**
     * Answer the question on your exam PDF by returning the Boolean associated
     * with your choice (i.e., true or false)
     *
     * @return A Boolean containing your answer
     */
    public static Boolean question1_1_2 () {
        true;
    }
    
    /**
     * Answer the question on your exam PDF by returning the Boolean associated
     * with your choice (i.e., true or false)
     *
     * @return A Boolean containing your answer
     */
    public static Boolean question1_1_3 () {
        return true;
    }
    
    /**
     * Answer the question on your exam PDF by returning the Boolean associated
     * with your choice (i.e., true or false)
     *
     * @return A Boolean containing your answer
     */
    public static Boolean question1_1_4 () {
        return false;
    }
    
    
    // Question 1.2.
    // ----------------------------------------------------------------------
    
    /**
     * Answer the question on your exam PDF by returning the Boolean associated
     * with your choice (i.e., true or false)
     *
     * @return A Boolean containing your answer
     */
    public static Boolean question1_2_1 () {
        return true;
    }
    
    /**
     * Answer the question on your exam PDF by returning the Boolean associated
     * with your choice (i.e., true or false)
     *
     * @return A Boolean containing your answer
     */
    public static Boolean question1_2_2 () {
        return true;
    }
    
    /**
     * Answer the question on your exam PDF by returning the Boolean associated
     * with your choice (i.e., true or false)
     *
     * @return A Boolean containing your answer
     */
    public static Boolean question1_2_3 () {
        return false;
    }
    
    /**
     * Answer the question on your exam PDF by returning the Boolean associated
     * with your choice (i.e., true or false)
     *
     * @return A Boolean containing your answer
     */
    public static Boolean question1_2_4 () {
        return true;
    }
    
    
    // Question 1.3.
    // ----------------------------------------------------------------------
    
    /**
     * Answer the question on your exam PDF by returning the Boolean associated
     * with your choice (i.e., true or false)
     *
     * @return A Boolean containing your answer
     */
    public static Boolean question1_3_1 () {
        return true;
    }
    
    /**
     * Answer the question on your exam PDF by returning the Boolean associated
     * with your choice (i.e., true or false)
     *
     * @return A Boolean containing your answer
     */
    public static Boolean question1_3_2 () {
        return false;
    }
    
    /**
     * Answer the question on your exam PDF by returning the Boolean associated
     * with your choice (i.e., true or false)
     *
     * @return A Boolean containing your answer
     */
    public static Boolean question1_3_3 () {
        return false;
    }
    
    /**
     * Answer the question on your exam PDF by returning the Boolean associated
     * with your choice (i.e., true or false)
     *
     * @return A Boolean containing your answer
     */
    public static Boolean question1_3_4 () {
        return false;
    }
    
    
    // ======================================================================
    // Section Two - Searchpocalypse
    // ======================================================================
    
    /**
     * Answer the question on your exam PDF by returning the array of Strings
     * containing the sequence of states expanded by the given search strategy
     *
     * @return An array of Strings containing the search strategy's sequence
     * of expanded states, e.g., ["I", "T", "G2"]
     */
    public static String[] question2_1_1 () {
        return new String[] {"I", "T", "W", "G1", };
    }
    
    /**
     * Answer the question on your exam PDF by returning the array of Strings
     * containing the sequence of states expanded by the given search strategy
     *
     * @return An array of Strings containing the search strategy's sequence
     * of expanded states, e.g., ["I", "T", "G2"]
     */
    public static String[] question2_1_2 () {
        return new String[] {"I", "T", "U", "V", "W", "G2"};
    }
    
    /**
     * Answer the question on your exam PDF by returning the array of Strings
     * containing the sequence of states expanded by the given search strategy
     *
     * @return An array of Strings containing the search strategy's sequence
     * of expanded states, e.g., ["I", "T", "G2"]
     */
    public static String[] question2_1_3 () {
        return new String[] {"I", "U", "X", "G4"};
    }
    
    /**
     * Answer the question on your exam PDF by returning the array of Strings
     * containing the sequence of states expanded by the given search strategy
     *
     * @return An array of Strings containing the search strategy's sequence
     * of expanded states, e.g., ["I", "T", "G2"]
     */
    public static String[] question2_1_4 () {
        return new String[] {"I", "V", "Y", "G5"};
    }
    
    /**
     * Answer the question on your exam PDF by returning the array of Strings
     * containing the sequence of states expanded by the given search strategy
     *
     * @return An array of Strings containing the search strategy's sequence
     * of expanded states, e.g., ["I", "T", "G2"]
     */
    public static String[] question2_1_5 () {
        return new String[] {"I", "U", "X", "G4"};
    }
    
    
    // ======================================================================
    // Section Three - Minimax
    // ======================================================================
    
    /**
     * Answer the question on your exam PDF by returning a map of each labeled
     * non-terminal node to its minimax score.
     *   - If the node would be pruned by alpha-beta pruning, leave its mapped
     *     value as null (NOT the String "null", the value null)
     *
     * @return A map of non-terminals to their minimax scores
     */
    public static Map<String, Integer> question3_1 () {
        Map<String, Integer> solution = new HashMap<>();
        solution.put("N0", 5);
        solution.put("N1", 5);
        solution.put("N2", 4);
        solution.put("N3", 3);
        solution.put("N4", 5);
        solution.put("N5", 4);
        solution.put("N6", null);
        solution.put("N7", null);
        return solution;
    }
    
    /**
     * Answer the question on your exam PDF by returning a map of each labeled
     * edge to a Boolean denoting whether or not it would represent a recursive
     * call that was made. In other words, each labeled edge should be mapped
     * to true if it is NOT pruned, or false if it is.
     *
     * @return A map of each edge to whether or not it would be pruned.
     */
    public static Map<String, Boolean> question3_2 () {
        Map<String, Boolean> solution = new HashMap<>();
        solution.put("E0", true);
        solution.put("E1", true);
        solution.put("E2", true);
        solution.put("E3", true);
        solution.put("E4", true);
        solution.put("E5", true);
        solution.put("E6", false);
        solution.put("E7", false);
        solution.put("E8", false);
        solution.put("E9", false);
        solution.put("E10", true);
        solution.put("E11", true);
        solution.put("E12", true);
        solution.put("E13", false);
        solution.put("E14", false);
        solution.put("E15", false);
        solution.put("E16", false);
        solution.put("E17", false);
        return solution;
    }
    
    /**
     * Answer the question on your exam PDF by returning the String corresponding to
     * the labeled edge / action that the player at the root would choose.
     * Options: "E0", "E1", "E2"
     *
     * @return Which edge the agent at the root would choose
     */
    public static String question3_3 () {
        return "E0";
    }
    
    
    // ======================================================================
    // Section Four - Dynamic Programming
    // ======================================================================
    
    /**
     * Answer the question on your exam PDF by returning the completed dynamic
     * programming memoization table for this problem. Any cell that would not
     * be completed by the given form of dynamic programming should be left null.
     *
     * @return The completed memoization table.
     */
    public static Integer[][] question4_1 () {
        return new Integer[][] {
        //   0     1     2     3     4     5     6     7     8     9
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, // D = {1}
            {0, 1, 2, 3, 1, 2, 3, 4, 2, 3}, // D = {1, 4}
            {0, 1, 2, 3, 1, 2, 1, 2, 2, 3}  // D = {1, 4, 6}
        };
    }
    
    /**
     * Answer the question on your exam PDF by returning the number of each coins
     * in the optimal solution to problem 4.1.
     *
     * @return Map of coin denomination to count of those present in the optimal
     * solution. For example, if there were 3 coins from the 4 cent denomination
     * in the optimal solution, we would put(4, 3)
     */
    public static Map<Integer, Integer> question4_2 () {
        Map<Integer, Integer> solution = new HashMap<>();
        solution.put(1, 1); // Number of 1 cent coins
        solution.put(4, 2); // Number of 4 cent coins
        solution.put(6, 0); // Number of 6 cent coins
        return solution;
    }
    
    /**
     * Answer the question on your exam PDF by returning the completed dynamic
     * programming memoization table for this problem. Any cell that would not
     * be completed by the given form of dynamic programming should be left null.
     *
     * @return The completed memoization table.
     */
    public static Integer[][] question4_3 () {
        return new Integer[][] {
        //   ""    G     R     E     A     T
            {0,    0,    0,    0,    0,    0}, // ""
            {0, null, null, null, null, null}, // C
            {0, null, "R", "R", null, null}, // R
            {0, null, null, null, "RA", null}, // A
            {0, null, null, null, null, "RAT"}, // T
            {0, null, null, null, null, "RAT"}  // E
        };
    }
    
    /**
     * Answer the question on your exam PDF by returning the String of the LCS
     * solution to problem 4.3.
     *
     * @return Case-sensitive String (i.e., all-caps) of the LCS solution to
     * problem 4.3.
     */
    public static String question4_4 () {
        return "RAT";
    }
    
}
