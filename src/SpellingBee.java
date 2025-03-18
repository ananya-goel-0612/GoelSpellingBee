import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        generateWords("", letters);
    }

    public void generateWords (String word, String letters) {
        if (letters.isEmpty()) {
            words.add(word);
            return;
        }


    }

    public void sort() {
        words = mergeSort(words, 0, words.size()-1);
    }

    // Merges the two arraylists together and returns the sorted arraylist of strings
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> sorted = new ArrayList<String>();
        int index1 = 0;
        int index2 = 0;

        while (index1 < arr1.size() && index2 < arr2.size()) {
            if (arr1.get(index1).compareTo(arr2.get(index2)) <= 0 ) {
                sorted.add(arr1.get(index1++));
            }
            else {
                sorted.add(arr2.get(index2++));
            }
        }

        while (index1 < arr1.size()) {
            sorted.add(arr1.get(index1++));
        }
        while (index2 < arr2.size()) {
            sorted.add(arr2.get(index2++));
        }

        return sorted;
    }

    public ArrayList<String> mergeSort(ArrayList<String> arr, int low, int high) {
        if (high == low) {
            ArrayList<String> newArr = new ArrayList<String>();
            newArr.add(arr.get(low));
            return newArr;
        }

        int med = (high + low) / 2;

        ArrayList<String> arr1 = mergeSort(arr, low, med);
        ArrayList<String> arr2 = mergeSort(arr, med + 1, high);

        return merge(arr1, arr2);
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    public void checkWords() {
        // Binary search to see if it is in the dictionary
        for (int i = 0; i < words.size(); i++) {
            // If it is not in the dictionary, remove it from words.
            if (!checkWord(words.get(i), 0, DICTIONARY_SIZE - 1)) {
                words.remove(i);
                i--;
            }
        }
    }

    public boolean checkWord(String word, int start, int end) {
        // The middle of the array that you're looking it is the value you're comparing
        // to the target
        int mid = start + (end - start) / 2;

        // Return true if the word is found in the dictionary
        if (DICTIONARY[mid].equals(word)) {
            return true;
        }

        // If it's an uneven number of elements in the array and all of them have been checked,
        // return false
        if (start == end) {
            return false;
        }

        // Look at the first half of the array if the target is less than the midpoint
        if (word.compareTo(DICTIONARY[mid]) < 0) {
            return checkWord(word, start, mid);
        }

        // Look at the second half of the array if the target is greater than the midpoint
        return checkWord(word, mid + 1, end);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
