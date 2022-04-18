package answers;

import java.util.*;

/**
 * [!] Consult the Exam PDF in the /doc subfolder for information on how to complete
 *     the questions on the exam!
 */
public class Answers {
    
    // ======================================================================
    // Part One - Edit Distance
    // ======================================================================
    
    /**
     * Answer the question on your exam PDF by returning the Edit Distance 
     * memoization table for the Levenshtein recurrence.
     *
     * @return A 2D array of Integers containing your answer
     */
    public static Integer[][] question1_1 () {
        return new Integer[][]{
          // -  A  B  D  C  E  F
            {0, 1, 2, 3, 4, 5, 6}, // -
            {1, 0, 1, 2, 3, 4, 5}, // A
            {2, 1, 1, 2, 3, 4, 5}, // A
            {3, 2, 2, 2, 3, 4, 5}, // B
            {4, 3, 3, 3, 2, 3, 4}, // C
            {5, 4, 4, 4, 4, 3, 4}, // D
            {6, 5, 5, 5, 5, 4, 3}, // F
            {7, 5, 4, 4, 4, 4, 4}  // E
        };
    }
    
    /**
     * Answer the question on your exam PDF by indicating the letter of your
     * answer choice (use capital letters).
     * 
     * @return The capital letter of your answer choice.
     */
    public static String question1_2 () {
        return "B";
    }
    
    /**
     * Answer the question on your exam PDF by indicating the Boolean of your
     * answer choice.
     * 
     * @return The Boolean of your answer choice.
     */
    public static Boolean question1_3 () {
        return false;
    }
    
    
    // ======================================================================
    // Part Two - Bloom Filters
    // ======================================================================
    
    /**
     * Answer the question on your exam PDF by indicating the state of the Bloom
     * filter after inserting the given items with the given hash functions.
     * 
     * @return The state of the Bloom Filter after insertion
     */
    public static Byte[] question2_1 () {
        return new Byte[] {
         // 0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15
            1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0
        };
    }
    
    /**
     * Answer the question on your exam PDF by indicating the letter of your
     * answer choice (use capital letters).
     * 
     * @return The capital letter of your answer choice.
     */
    public static String question2_2_1 () {
        return "A";
    }
    
    /**
     * Answer the question on your exam PDF by indicating the letter of your
     * answer choice (use capital letters).
     * 
     * @return The capital letter of your answer choice.
     */
    public static String question2_2_2 () {
        return "C";
    }
    
    /**
     * Answer the question on your exam PDF by indicating the letter of your
     * answer choice (use capital letters).
     * 
     * @return The capital letter of your answer choice.
     */
    public static String question2_2_3 () {
        return "B";
    }
    
    /**
     * Answer the question on your exam PDF by indicating the state of the Bloom
     * filter after the operation described.
     * 
     * @return The state of the Bloom Filter after the operation described
     */
    public static Byte[] question2_3 () {
        return new Byte[] {
         // 0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15
            0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0
        };
    }
    
    /**
     * Answer the question on your exam PDF by indicating the letter of your
     * answer choice (use capital letters).
     * 
     * @return The capital letter of your answer choice.
     */
    public static String question2_4 () {
        return "B";
    }
    
    /**
     * Answer the question on your exam PDF by indicating the letter of your
     * answer choice (use capital letters).
     * 
     * @return The capital letter of your answer choice.
     */
    public static String question2_5 () {
        return "B";
    }
    
    /**
     * Answer the question on your exam PDF by indicating the letter of your
     * answer choice (use capital letters).
     * 
     * @return The capital letter of your answer choice.
     */
    public static String question2_6 () {
        return "A";
    }
    
    
    // ======================================================================
    // Part Three - Compression
    // ======================================================================
    
    /**
     * Returns the encoding map of Characters to their corresponding bitstring
     * in the compressed encoding scheme created from the Huffman Trie that would
     * be constructed in the manner specified on the exam.
     * 
     * @return The compressed encoding map of characters mapped to their bitstrings
     */
    public static Map<Character, String> question3_1 () {
        Map<Character, String> result = new TreeMap<>();
        result.put('D', "1101");
        result.put('B', "1100");
        result.put('C', "111");
        result.put('E', "00");
        result.put('A', "01");
        result.put('F', "10");
        return result;
    }
    
    /**
     * Decode the bitstring message using the Huffman Trie you created to
     * answer question 3.1 and return the decoded message *IN ALL CAPITAL
     * LETTERS*
     * 
     * @return The decoded message IN ALL CAPITAL LETTERS 
     */
    public static String question3_2 () {
        return "DECAF";
    }
    
    
    // ======================================================================
    // Part Four - CSP Potpourri
    // ======================================================================
    
    /**
     * Answer the question on your exam PDF by indicating the letter of your
     * answer choice (use capital letters).
     * 
     * @return The next variable that would be attempted by MRV.
     */
    public static String question4_1 () {
        return "B";
    }
    
    /**
     * Answer the question on your exam PDF by indicating the value of your
     * answer choice.
     * 
     * @return The next value that would be attempted by LCV.
     */
    public static Integer question4_2 () {
        return 0;
    }
    
    /**
     * Provide the next partial assignment that backtracking would attempt
     * on the problem given in the exam by returning a map of variables
     * mapped to their currently assigned values. For any variable that is
     * unassigned, leave its value as null.
     * 
     * @return The partial assignment of variables mapped to values.
     */
    public static Map<String, Integer> question4_3 () {
        Map<String, Integer> result = new TreeMap<>();
        result.put("A", 0);
        result.put("B", 2);
        result.put("C", 0);
        result.put("D", 0);
        result.put("E", 2);
        result.put("F", 3);
        result.put("G", 3);
        return result;
    }
    
    /**
     * Answer the question on your exam PDF by indicating the set of
     * variables that would be amenable to reassignment.
     * 
     * @return The set of variables amenable to reassignment.
     */
    public static Set<String> question4_4 () {
        return new TreeSet<String>(
            // Place answer variables in here, e.g., Arrays.asList("A", "B") 
            Arrays.asList("D", "G", "F")
        );
    }
    
    /**
     * Answer the question on your exam PDF by indicating the value of your
     * answer choice.
     * 
     * @return The next value that would be attempted by Min-Conflict.
     */
    public static Integer question4_5 () {
        return 3;
    }
    
    /**
     * Provide the solution to the event scheduling CSP after performing
     * cutset conditioning and solving in the manner specified on the exam.
     * 
     * @return The complete assignment of values to variables.
     */
    public static Map<String, Integer> question4_6 () {
        Map<String, Integer> result = new TreeMap<>();
        result.put("A", 3);
        result.put("B", 1);
        result.put("C", 2);
        result.put("D", 3);
        result.put("E", 1);
        result.put("F", 1);
        result.put("G", 0);
        return result;
    }
    
    
    // ======================================================================
    // Part Five - Consistency
    // ======================================================================
    
    /**
     * Returns the trimmed domains of each variable in the CSP after running
     * AC-3. The result is a Map of variables mapped to their domains (sets of
     * Integer values).
     * 
     * @return The map of variables mapped to their domains
     */
    public static Map<String, Set<Integer>> question5_1 () {
        Map<String, Set<Integer>> result = new TreeMap<>();
        // Trim the domains of each variable by removing values from their mapped set:
        result.put("W", new TreeSet<Integer>(Arrays.asList(2, 3)));
        result.put("X", new TreeSet<Integer>(Arrays.asList(1, 2)));
        result.put("Y", new TreeSet<Integer>(Arrays.asList(0, 1)));
        result.put("Z", new TreeSet<Integer>(Arrays.asList(0, 1)));
        return result;
    }

    /**
     * Returns the trimmed domains of each variable in the CSP after enforcing
     * 3-consistency on the given ternary arc. The result is a Map of variables 
     * mapped to their domains (sets of Integer values).
     * 
     * @return The map of variables mapped to their domains
     */
    public static Map<String, Set<Integer>> question5_2 () {
        Map<String, Set<Integer>> result = new TreeMap<>();
        // Trim the domains of each variable by removing values from their mapped set:
        result.put("X", new TreeSet<Integer>(Arrays.asList(3, 4, 6)));
        result.put("Y", new TreeSet<Integer>(Arrays.asList(2, 3)));
        result.put("Z", new TreeSet<Integer>(Arrays.asList(1, 3, 4)));
        return result;
    }
    
    
    // ======================================================================
    // Part Six - Genetic Algorithms
    // ======================================================================
    
    /**
     * Answer the question on your exam PDF by indicating the letter of your
     * answer choice (use capital letters).
     * 
     * @return The capital letter of your answer choice.
     */
    public static String question6_1 () {
        return "A";
    }
    
    /**
     * Answer the question on your exam PDF by indicating the Boolean of your
     * answer choice.
     * 
     * @return The Boolean of your answer choice.
     */
    public static Boolean question6_2 () {
        return false;
    }
    
    
}
