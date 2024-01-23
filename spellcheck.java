import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class spellcheck{

    

    static int levenshteinRecursive(String given, String check, int m, int n){ // time complexity: O(3^(m+n))
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
        levenshteinRecursive(given, check, m, n-1),Math.min( // insertion
        levenshteinRecursive(given, check, m-1, n), // removal
        levenshteinRecursive(given, check, m-1, n-1))); // substitution
    }
    static int levenshteinMatrix(String given, String check){
        int m = given.length();
        int n = check.length();
        int[][] matrix = new int[m+1][n+1]; // each row is a character in respective string, +1 is to account for whitespace at position string[-1]

        for(int i=0; i<=m; i++){
            matrix[i][0] = i;
        }
        for(int i=0; i<=n; i++){
            matrix[0][i] = i;
        }
        char[] givencarr = given.toCharArray();
        char[] checkcarr = check.toCharArray();
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (givencarr[i - 1] == checkcarr[j-1]) {
                    matrix[i][j] = matrix[i - 1][j - 1];
                } else {
                    matrix[i][j] = 1 + Math.min(
                            matrix[i][j - 1],Math.min(
                            matrix[i - 1][j],
                            matrix[i - 1][j - 1]));
                }
            }
        }
        return matrix[m][n];
    }


    public static String[] suggestions(String phrase){
        String fileName = "data/words_pos.csv";
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
            int range = 2; // current best
            for(String word : words){
                int d = levenshteinMatrix(phrase, word);
                if(d<range){
                    possibilities.clear();
                    range = d;
                    possibilities.add(word);
                }else if(d==range){
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
        String[] phrase = scanner.nextLine().split(" ");
        scanner.close();

        for(String s : phrase){
            String[] corrections = suggestions(s.toLowerCase());
            if(corrections[0]!="Correct"){
                System.out.println("\n ("+s+") Did you mean...");
                for(String maybe : corrections){
                    System.out.print(maybe+" ");
                }
                System.out.println("");
            }
            
        }
        
    }
}