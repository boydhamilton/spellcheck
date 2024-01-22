import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class spellcheck{

    

    static int levenshteinRecursive(String given, String check, int m, int n){
    /*  three edit moves are:
            - removal
            - addition
            - substitution
        each of these yields an edit distance of 1
    */
        if(m==0){
            return n;
        }else if(n==0){
            return m;
        }
        char[] givencarr = given.toCharArray();
        char[] checkcarr = check.toCharArray();
        
        if(givencarr[m-1] == checkcarr[n-1]){
            return levenshteinRecursive(given, check, m-1, n-1);
        }
        
        return 1 + Math.min(
        levenshteinRecursive(given, check, m, n-1),Math.min(
        levenshteinRecursive(given, check, m-1, n),
        levenshteinRecursive(given, check, m-1, n-1)));
    }

    public static String[] autocorrect(String phrase){
        String fileName = "words_pos.csv";
        List<List<String>> rows = new ArrayList<>();

        try{
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                rows.add(Arrays.asList(values));
            }
            br.close();
        }catch(IOException exception){
            exception.printStackTrace();
        }

        String[] words = new String[rows.size()];
        for(int i=0; i<rows.size(); i++){
            List<String> row = rows.get(i);
            words[i] = row.get(1);
        }

        List<String> possibilities = new ArrayList<>();
        if (!Arrays.asList(words).contains(phrase)){
            int cbDistance = 2; // current best
            for(String word : words){
                int d = levenshteinRecursive(phrase, word, phrase.length(), word.length());
                if(d<cbDistance){
                    possibilities.clear();
                    cbDistance = d;
                    possibilities.add(word);
                }else if(d==cbDistance){
                    possibilities.add(word);
                }
            }
        }else{
            return new String[] {"Correct"};
        }
        return possibilities.toArray(new String[0]);
    }

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        String phrase = scanner.nextLine();

        String[] corrections = autocorrect(phrase);
        for(String maybe : corrections){
            System.out.println(maybe);
        }
        
        scanner.close();
    }
}