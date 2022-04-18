package main.huffman;

import java.util.*;
import java.io.ByteArrayOutputStream; // Optional

/**
 * Huffman instances provide reusable Huffman Encoding Maps for
 * compressing and decompressing text corpi with comparable
 * distributions of characters.
 */
public class Huffman {
    
    // -----------------------------------------------
    // Construction
    // -----------------------------------------------

    private HuffNode trieRoot;
    // TreeMap chosen here just to make debugging easier
    private TreeMap<Character, String> encodingMap;
    // Character that represents the end of a compressed transmission
    private static final char ETB_CHAR = 23;
    
    /**
     * Creates the Huffman Trie and Encoding Map using the character
     * distributions in the given text corpus
     * @param corpus A String representing a message / document corpus
     *        with distributions over characters that are implicitly used
     *        throughout the methods that follow. Note: this corpus ONLY
     *        establishes the Encoding Map; later compressed corpi may
     *        differ.
     */
    public Huffman (String corpus) {
    	Map<Character, Integer> storedMap = new HashMap<>();
    	PriorityQueue<HuffNode> pq = new PriorityQueue<>();
    	storedMap = distributionFreq(corpus);
    	for(Map.Entry<Character, Integer> entry: storedMap.entrySet()) {
    		HuffNode leafNode = new HuffNode(entry.getKey(), entry.getValue());
    		pq.add(leafNode);
    	}
    	while(pq.size() > 1) {
    		HuffNode childOne = pq.poll();
    		HuffNode childTwo = pq.poll();
    		HuffNode parent = new HuffNode((childOne.character < childTwo.character)? childOne.character : childTwo.character, childOne.count + childTwo.count);
    		parent.left = childOne;
    		parent.right = childTwo;
    		pq.add(parent);
    	}
    	trieRoot = pq.poll();
    	String emptyString = "";
    	encodingMap = new TreeMap<Character, String>();
    	encoder(trieRoot, emptyString);
    }
    
    /**
     * Recursive method that fills the global variable encodingMap with a bitcode that corresponds to the
     * characters in the corpus that we are trying to compress
     * @param encodedMap The edited version of the encoding map initially passed in, creates a compressed
     * 		  version of the character's initial bitcode
     * @param currentNode The current node being operated on in the trie, updates with each recursion
     * @param currentCode The current bitcode of the character in the map, updates with each recursion
     */
    private void encoder(HuffNode currentNode, String currentCode) {
    	if(currentNode.isLeaf()) {
    		encodingMap.put(currentNode.character, currentCode);
    		return;
    	}
    	else {
    		encoder(currentNode.left, currentCode + "0");
    		encoder(currentNode.right, currentCode + "1");
    	}
    }

    /**
     * A method that allows us to determine the frequency of each character in the corpus we
     * are compressing, including the ETB character that we will use in our compressed bytecode
     * @param corpus The corpus that we are operating on and using to determine the frequency at
     * 		  which characters appear
     * @return A map that correlates each character in the corpus to a certain frequency
     */
    private Map<Character, Integer> distributionFreq(String corpus) {
        Map<Character, Integer> charMap = new HashMap<>();
        charMap.put(ETB_CHAR, 1);
    	for(int i = 0; i < corpus.length(); i++) {
    		if(charMap.containsKey(corpus.charAt(i))) {
    			charMap.put(corpus.charAt(i), charMap.get(corpus.charAt(i)) + 1);
    		}
    		else {
    			charMap.put(corpus.charAt(i), 1);
    		}
    	}
    	return charMap;
    }
    
    // -----------------------------------------------
    // Compression
    // -----------------------------------------------
    
    /**
     * Compresses the given String message / text corpus into its Huffman coded
     * bitstring, as represented by an array of bytes. Uses the encodingMap
     * field generated during construction for this purpose.
     * @param message String representing the corpus to compress.
     * @return {@code byte[]} representing the compressed corpus with the
     *         Huffman coded bytecode. Formatted as:
     *         (1) the bitstring containing the message itself, (2) possible
     *         0-padding on the final byte.
     */
    public byte[] compress (String message) {
    	ByteArrayOutputStream compMessage = new ByteArrayOutputStream();
    	String totalMessage = "";
    	for(int i = 0; i < message.length(); i++) {
    		char temp = message.charAt(i);
    		String writtenBitcode = encodingMap.get(temp);
    		totalMessage += writtenBitcode;
    	}
    	totalMessage += encodingMap.get(ETB_CHAR);
    	for(int i = 0; i < totalMessage.length(); i = i + 8) {
    		if(i + 8 > totalMessage.length()) {
    			String endMessage = totalMessage.substring(i, totalMessage.length());
    			byte endByte = Byte.parseByte(endMessage, 2);
    			int endInt = (int)endByte;
    			endInt = endInt << (8 - endMessage.length());
    			compMessage.write((byte)endInt);
    		}
    		else {
    			String bulkMessage = totalMessage.substring(i, i + 8);
    			int bulkInt = Integer.parseInt(bulkMessage, 2);
    			compMessage.write((byte)bulkInt);
    		}
    	}
    	return compMessage.toByteArray();
    }
    
    
    // -----------------------------------------------
    // Decompression
    // -----------------------------------------------
    
    /**
     * Decompresses the given compressed array of bytes into their original,
     * String representation. Uses the trieRoot field (the Huffman Trie) that
     * generated the compressed message during decoding.
     * @param compressedMsg {@code byte[]} representing the compressed corpus with the
     *        Huffman coded bytecode. Formatted as:
     *        (1) the bitstring containing the message itself, (2) possible
     *        0-padding on the final byte.
     * @return Decompressed String representation of the compressed bytecode message.
     */
    public String decompress (byte[] compressedMsg) {
    	String decompString = "";
    	String byteMsg = "";
    	HuffNode decompRoot = trieRoot;
    	for(int i = 0; i < compressedMsg.length; i++) {
        	byteMsg = String.format("%8s", Integer.toBinaryString(compressedMsg[i] & 0xFF)).replace(' ', '0');
        	for(char num : byteMsg.toCharArray()) {
        		if(decompRoot.isLeaf()) {
        			if(decompRoot.character == ETB_CHAR) {
        				return decompString;
        			}
        			else {
        				decompString += decompRoot.character;
        				decompRoot = trieRoot;
        			}
        		}
        		if(num == '0') {
            			decompRoot = decompRoot.left;
        		}
        		else {
            		decompRoot = decompRoot.right;
        		}
        	}
    	}
    	return decompString;
    }
    
    
    // -----------------------------------------------
    // Huffman Trie
    // -----------------------------------------------
    
    /**
     * Huffman Trie Node class used in construction of the Huffman Trie.
     * Each node is a binary (having at most a left and right child), contains
     * a character field that it represents, and a count field that holds the 
     * number of times the node's character (or those in its subtrees) appear 
     * in the corpus.
     */
    private static class HuffNode implements Comparable<HuffNode> {
        
        HuffNode left, right;
        char character;
        int count;
        
        HuffNode(char character, int count) {
            this.count = count;
            this.character = character;
        }
        
        public boolean isLeaf() {
            return this.left == null && this.right == null;
        }
        
        public int compareTo(HuffNode other) {
        	if(this.count == other.count) {
        		return this.character - other.character;
        	}
        	return this.count - other.count;
        }
        
    }

}
