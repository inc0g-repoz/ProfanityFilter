package com.github.inc0grepoz.profilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * An easy to use profanities filter that uses regular expressions
 * to indicate swearings in text messages.
 * 
 * <p>Requires JRE 1.8 or higher.
 * 
 * @author inc0g-repoz
 * @see Pattern
 */
public class ProfanityFilter {

    private final List<Pattern> patterns = new ArrayList<>();

    /**
     * Creates a clear instance of this class without any profanities.
     */
    public ProfanityFilter() {}

    /**
     * Creates a new instance of this class from profanities
     * included in the collection passed to the constructor.
     * 
     * @param words a collection with profanities regular expressions
     */
    public ProfanityFilter(Collection<String> words) {
        addWords(words);
    }

    /**
     * Creates a new instance of this class from profanities
     * included in the file passed to the constructor.
     * 
     * @param file a file with a profanities regular expressions list
     * @throws IOException if unable to read the provided file
     */
    public ProfanityFilter(File file) throws IOException {
        addWords(file);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        patterns.forEach(elt -> joiner.add(elt.pattern()));
        return "[" + joiner.toString() + "]";
    }

    /**
     * Adds a regular expression to be filtered from the argument.
     * Ignores it if empty or has a <code>'#'</code> in the beginning.
     * 
     * @param word a regular expression
     */
    public void addWord(String word) {
        if (!word.isEmpty() && word.charAt(0) != '#') {
            patterns.add(Pattern.compile(word));
        }
    }

    /**
     * Adds regular expressions to be filtered from an array in the argument.
     * Ignores them if empty or has a <code>'#'</code> in the beginning.
     * 
     * @param words a array with profanities regular expressions
     */
    public void addWords(String... words) {
        for (int i = 0; i < words.length; i++) {
            addWord(words[i]);
        }
    }

    /**
     * Adds regular expressions to be filtered from a collection in the argument.
     * 
     * @param words a collection with profanities regular expressions
     */
    public void addWords(Collection<String> words) {
        words.forEach(this::addWord);
    }

    /**
     * Adds profanities included in the file in the argument.
     * 
     * @param file a file with a profanities regular expressions list
     * @throws IOException if unable to read the provided file
     */
    public void addWords(File file) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file));

        for (String word; (word = in.readLine()) != null;) {
            addWord(word);
        }

        in.close();
    }

    /**
     * Tests a string passed in the argument. Returns <code>true</code>
     * if it has profanities and <code>false</code> otherwise.
     * 
     * @param string a string to be tested
     * @return <code>true</code> if any profanities matches found
     */
    public boolean findMatches(String string) {
        return patterns.stream().anyMatch(elt -> elt.matcher(string).find());
    }

    /**
     * Tests a string passed in the argument. Returns a transformed string
     * with all cussings replaced by the symbol provided in another argument.
     * 
     * @param string a string to be tested and transformed
     * @param symbol a symbol to use as replacement
     * @return a transformed string that has swearings replaced
     */
    public String replaceMatches(String string, char symbol) {
        String[] words = string.split(" ");

        for (Pattern elt: patterns)
        for (int i = 0; i < words.length; i++) {
            if (!elt.matcher(words[i]).find()) {
                continue;
            }

            words[i] = (new String(new char[words[i].length()]))
                    .replace('\0', symbol);
        }

        return String.join(" ", words);
    }

    /**
     * Tests a string passed in the argument. Returns a transformed string
     * with all cussings replaced by the word provided in another argument.
     * 
     * @param string a string to be tested and transformed
     * @param replacement a string to use as replacement
     * @return a transformed string that has swearings replaced
     */
    public String replaceMatches(String string, String replacement) {
        String[] split = string.split(" ");

        for (Pattern elt: patterns)
        for (int i = 0; i < split.length; i++) {
            if (elt.matcher(split[i]).find()) {
                split[i] = replacement;
            }
        }

        return String.join(" ", split);
    }

}
