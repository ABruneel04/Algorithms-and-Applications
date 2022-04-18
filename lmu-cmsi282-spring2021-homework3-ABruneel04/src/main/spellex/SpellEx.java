package main.spellex;

import java.util.*;

public class SpellEx {
    
    // Note: Not quite as space-conscious as a Bloom Filter,
    // nor a Trie, but since those aren't in the JCF, this map 
    // will get the job done for simplicity of the assignment
    private Map<String, Integer> dict;
    
    // For your convenience, you might need this array of the
    // alphabet's letters for a method
    private static final char[] LETTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    /**
     * Constructs a new SpellEx spelling corrector from a given
     * "dictionary" of words mapped to their frequencies found
     * in some corpus (with the higher counts being the more
     * prevalent, and thus, the more likely to be suggested)
     * @param words The map of words to their frequencies
     */
    public SpellEx(Map<String, Integer> words) {
        dict = new HashMap<>(words);
    }
    
    /**
     * Returns the edit distance between the two input Strings
     * s0 and s1 based on the minimal number of insertions, deletions,
     * replacements, and transpositions required to transform s0
     * into s1
     * @param s0 A "start" String
     * @param s1 A "destination" String
     * @return The minimal edit distance between s0 and s1
     */    
    public static int editDistance (String s0, String s1) {
        int[][] editTable = new int[s0.length() + 1][s1.length() + 1];
        for(int i = 0; i < s0.length() + 1; i++) {
            for(int j = 0; j < s1.length() + 1; j++) {
        		if(i == 0) {
        			editTable[i][j] = j;
        		}
        		else if(j == 0) {
        			editTable[i][j] = i;
        		}
        		else {
        			String testStringZero = s0.substring(0, i);
        			String testStringOne  = s1.substring(0, j);    			
        			if(testStringZero.equals(testStringOne)) {
        				editTable[i][j] = 0;
        			}
        			else {
        				editTable[i][j] = minEditValue(editTable, testStringZero, testStringOne, i, j);
        			}
        		}
            }
        }
        return editTable[s0.length()][s1.length()];
    }
    
    /**
     * Returns the minimal edit distance through applying insertion, deletion
     * replacement, and transposition to the passed in substrings
     * @param editTable The table containing all edit distance values that have currently been found
     * @param testStringZero The row substring being looked at
     * @param testStringOne The column substring being looked at
     * @param i The row index currently being assessed
     * @param j The column index currently being assessed
     * @return The minimal edit distance from insertion, deletion, replacement, and transposition cases
     */    
    private static int minEditValue (int[][] editTable, String testStringZero, String testStringOne, int i, int j) {
        ArrayList<Integer> editValues = new ArrayList<>();
        if(i >= 1) {
        	editValues.add(editTable[i][j - 1] + 1);
        }
        if(j >= 1) {
        	editValues.add(editTable[i - 1][j] + 1);
        }
        if(i >= 1 && j >= 1) {
            if(!(testStringZero.charAt(i - 1) == (testStringOne.charAt(j - 1)))) {
            	editValues.add(editTable[i - 1][j - 1] + 1);
            }
            else {
            	editValues.add(editTable[i - 1][j - 1]);
            }
        }
        if(i >= 2 && j >= 2) {
            if(testStringZero.charAt(i - 1) == testStringOne.charAt(j - 2) && testStringZero.charAt(i - 2) == testStringOne.charAt(j - 1)) {
            	editValues.add(editTable[i - 2][j - 2] + 1);
            }
        }
        int minValue = Collections.min(editValues);
        return minValue;
    }
    
    /**
     * Returns the n closest words in the dictionary to the given word,
     * where "closest" is defined by:
     * <ul>
     *   <li>Minimal edit distance (with ties broken by:)</li>
     *   <li>Largest count / frequency in the dictionary (with ties broken by:)</li>
     *   <li>Ascending alphabetic order</li>
     * </ul>
     * @param word The word we are comparing against the closest in the dictionary
     * @param n The number of least-distant suggestions desired
     * @return A set of up to n suggestions closest to the given word
     */
    public Set<String> getNLeastDistant (String word, int n) {
        Set<String> leastDistantStrs = new HashSet<String>();
        PriorityQueue<SpellExTieBreaker> pq = new PriorityQueue<>();
        for(Map.Entry<String, Integer> entry : dict.entrySet()) {
        	SpellExTieBreaker tb = new SpellExTieBreaker(entry.getKey(), editDistance(word, entry.getKey()), entry.getValue());
        	pq.add(tb);
        }
        for(int i = 0; i < n; i++) {
        	leastDistantStrs.add(pq.poll().givenWord);
        }
        return leastDistantStrs;
    }
    
    /**
     * Returns the set of n most frequent words in the dictionary to occur with
     * edit distance distMax or less compared to the given word. Ties in
     * max frequency are broken with ascending alphabetic order.
     * @param word The word to compare to those in the dictionary
     * @param n The number of suggested words to return
     * @param distMax The maximum edit distance (inclusive) that suggested / returned 
     * words from the dictionary can stray from the given word
     * @return The set of n suggested words from the dictionary with edit distance
     * distMax or less that have the highest frequency.
     */
    public Set<String> getNBestUnderDistance (String word, int n, int distMax) {
        Set<String> tempSetOne = new HashSet<String>();
        Set<String> tempSetTwo = new HashSet<String>();
        Set<String> underDistStrs = new HashSet<String>();
        PriorityQueue<SpellExTieBreaker> pq = new PriorityQueue<>();
        tempSetOne.add(word);
        for(int i = 0; i < distMax; i++) {
    		for(String possibleWord: tempSetOne) {    			
    			tempSetTwo.addAll(getAllPossibleWords(possibleWord));
    		}
    		tempSetOne.addAll(tempSetTwo);
        }
        for(String containedWord: tempSetOne) {
        	if(dict.containsKey(containedWord)) {
                SpellExTieBreaker tb = new SpellExTieBreaker(containedWord, 0, dict.get(containedWord));
                pq.add(tb);
        	}
        }        
        for(int j = 0; j < n; j++) {
        	if(!(pq.isEmpty())) {
            	underDistStrs.add(pq.poll().givenWord);
        	}
        }
        return underDistStrs;
    }
    
    /**
     * Returns all possible words within one edit distance of a passed in word
     * parameter. This set is found by manually attempting all insertion, deletion,
     * replacement, and transposition combinations
     * @param word The word that has insertion, deletion, replacement, and
     * transposition actions taken on it
     * @return The set of strings that are all possible words
     * reachable within one edit distance
     */
    private Set<String> getAllPossibleWords(String word) {
    	Set<String> possibleWords = new HashSet<String>();
        //insertion        
        for(int i = 0; i < word.length() + 1; i++) {
        	for(int j = 0; j < LETTERS.length; j++) {
        		String addedWord = word.substring(0, i) + LETTERS[j] + word.substring(i, word.length());
        		possibleWords.add(addedWord);
        	}
        }
        //deletion
        for(int i = 0; i < word.length(); i++) {
        	String addedWord = word.substring(0, i) + word.substring(i + 1, word.length());
        	possibleWords.add(addedWord);
        }
        //replacement
        for(int i = 0; i < word.length(); i++) {
        	for(int j = 0; j < LETTERS.length; j++) {
        		String addedWord = word.substring(0, i) + LETTERS[j] + word.substring(i + 1, word.length());
            	possibleWords.add(addedWord);
        	}
        }
        //transposition        
        for(int i = 0; i < word.length(); i++) {
        	for(int j = word.length() - 1; j >= 0; j--) {
        		char[] passedInWord = word.toCharArray();
        		char temp = passedInWord[j];
        		passedInWord[j] = passedInWord[i];
            	passedInWord[i] = temp;
            	String addedWord = new String(passedInWord);
            	possibleWords.add(addedWord);
        	}        	
        }       
        return possibleWords;
    }
    
    /**
     * SpellEx private nested class that creates an object able to
     * store information needed to break ties between the edit distance
     * of words
     */
    private class SpellExTieBreaker implements Comparable<SpellExTieBreaker> {
    	
    	//Private Fields
    	// -------------
    	private String givenWord;
    	private int editDist;
    	private int frequency;
    	
    	/**
    	 * Constructs a SpellExTieBreaker that we can use in our getN methods
    	 * 
    	 * @param givenword The word tied to the edit distance and frequency
    	 * @param editDist The amount of edits needed to transform the string to another
    	 * @param frequency The amount of time the word appears in our given corpus
    	 */
    	private SpellExTieBreaker(String givenWord, int editDist, int frequency) {
    		this.givenWord = givenWord;
    		this.editDist = editDist;
    		this.frequency = frequency;
    	}
    	
    	/**
    	 * @param SpellExTieBreaker tb that is being compared to another
    	 * SpellExTieBreaker object already in the priority queue
    	 * @return an integer value that tells the priority queue how to
    	 * rank the SpellExTieBreaker objects in the queue
    	 */
    	public int compareTo(SpellExTieBreaker tb) {
    		if(this.editDist == tb.editDist && this.frequency == tb.frequency) {
    			return this.givenWord.compareTo(tb.givenWord);
    		}
    		else if(this.editDist == tb.editDist) {
    			return tb.frequency - this.frequency;
    		}
    		else {
    			return this.editDist - tb.editDist;
    		}
    	}
    }    	   
}

