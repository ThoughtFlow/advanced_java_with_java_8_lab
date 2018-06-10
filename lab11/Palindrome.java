package lab11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Algorithm to find a palindrome:
 * - Strip off spaces in between words
 * - Convert all characters to same case (using lowercase in these examples)
 * - Filter out any word that is 2 characters or less
 * - Find the half-way point in the word and compare each character on the lower half to their counterpart on the upper half 
 */
public class Palindrome
{
    public static List<String> findPalindromeImperative(List<String> list)
    {
        List<String> palindromes = new ArrayList<>();
        for (String nextWord : list)
        {
            nextWord = nextWord.toLowerCase();
            nextWord = nextWord.replace(" ", "");
            nextWord = nextWord.replace(",", "");
            nextWord = nextWord.replace("'", "");
            boolean isPalindrome = true;
            int halfWay = nextWord.length() / 2 - 1;
            for (int index = 0; index < halfWay; ++index)
            {
                if (nextWord.charAt(index) != nextWord.charAt(nextWord.length() - index - 1))
                {
                    isPalindrome = false;
                    break;
                }
            }

            if (isPalindrome)
            {
                palindromes.add(nextWord);
            }
        }

        return palindromes;
    }

    public static List<String> findPalindrome(List<String> list)
    {
        return list.stream().
            map(s -> {s = s.replace(" ", ""); return s;}).
            map(s -> {s = s.replace(",", ""); return s;}).
            map(s -> {s = s.replace("'", ""); return s;}).
            map(s -> s.toLowerCase()).
            filter(s -> s.length() > 2).
            filter(s -> {
                int halfWay = s.length() / 2 - 1;
                return IntStream.rangeClosed(0, halfWay).allMatch(i -> s.charAt(i) == s.charAt(s.length() - i - 1));
            }).
            collect(Collectors.toList());
    }
    
    public static List<String> findPalindromeKeepOriginal(List<String> list)
    {
        return list.stream().
            map(Holder::new).
            map(h -> {h.strippedWord = h.strippedWord.replace(" ", ""); return h;}).
            map(h -> {h.strippedWord = h.strippedWord.replace(",", ""); return h;}).
            map(h -> {h.strippedWord = h.strippedWord.replace("'", ""); return h;}).
            map(h -> {h.strippedWord = h.strippedWord.toLowerCase(); return h;}).
            filter(h -> h.strippedWord.length() > 2).
            filter(h -> {
                int halfWay = h.strippedWord.length() / 2 - 1;
                return IntStream.rangeClosed(0, halfWay).allMatch(i -> h.strippedWord.charAt(i) == h.strippedWord.charAt(h.strippedWord.length() - i - 1));
            }).
            map(h -> h.original).
            collect(Collectors.toList());
    }
    
    public static List<String> findPalindromeKeepOriginalConcise(List<String> list)
    {
    	BiFunction<Holder, String, Holder> replacer = (h, s) -> {h.strippedWord = h.strippedWord.replace(s, ""); return h;};
    	
        return list.stream().
            map(Holder::new).
            map(h -> replacer.apply(h, " ")).
            map(h -> replacer.apply(h, ",")).
            map(h -> replacer.apply(h, "'")).
            map(h -> {h.strippedWord = h.strippedWord.toLowerCase(); return h;}).
            filter(h -> h.strippedWord.length() > 2).
            filter(h -> {
                int halfWay = h.strippedWord.length() / 2 - 1;
                return IntStream.rangeClosed(0, halfWay).allMatch(i -> h.strippedWord.charAt(i) == h.strippedWord.charAt(h.strippedWord.length() - i - 1));
            }).
            map(h -> h.original).
            collect(Collectors.toList());
    }

    public static void main(String... args) throws IOException
    {
        List<String> palindromes = Arrays.asList(
        "A man a plan a cat a ham a yak a yam a hat a canal Panama",
        "A Toyota Race fast safe car A Toyota",
        "As I pee sir, I see Pisa",
        "Aibohphobia",
        "ABBA",
        "A Toyota's a Toyota",
        "This is not a palindrome");
        System.out.println("These are palindromes: " );
        findPalindromeKeepOriginal(palindromes).forEach(System.out::println);
        
        // Variations
//        findPalindrome(palindromes).forEach(System.out::println);
        //findPalindromeKeepOriginalConcise(palindromes).forEach(System.out::println);
        //findPalindromeImperative(palindromes).forEach(System.out::println);
    }

    private static class Holder
    {
        public final String original;
        public String strippedWord;

        public Holder(String original)
        {
            this.original = original;
            this.strippedWord = original;
        }
    }
}