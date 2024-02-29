import java.io.*;
import java.util.*;




// boyd hamilton 2024


public class spellcheck{

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

    static void parseData(List<List<String>> rows, List<String> rank) throws IOException{ // TODO: thread
        String wordsFileName = "data/words_pos.csv";
        String unigramFileName = "data/unigram_freq.csv";
        BufferedReader br = new BufferedReader(new FileReader(wordsFileName));
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            rows.add(Arrays.asList(values));
        }
        br.close();
        br = new BufferedReader(new FileReader(unigramFileName));
        while ((line = br.readLine()) != null) {
            String v = line.split(",")[0];
            rank.add(v);
        }
        br.close();
    }


    public static String[] levenshteinSuggestions(String phrase,List<List<String>> rows){
        
        String[] words = new String[rows.size()];
        for(int i=0; i<rows.size(); i++){
            List<String> row = rows.get(i);
            words[i] = row.get(1);
        }

        List<String> possibilities = new ArrayList<>();
        if (!Arrays.asList(words).contains(phrase)){
            int range = 2; // current best
            for(String word : words){ // could stream
                int d = levenshteinMatrix(phrase, word); // ngl instead of ints i could be returning the values as bytes. edit distance is not 32 bits
                if(d<range){
                    possibilities.clear();
                    range = d;
                    possibilities.add(word);
                }else if(d==range){
                    possibilities.add(word);
                }
            }
        }else{
            return new String[] {"c"}; // correctly spelled (in our dictionary)
        }

        // if no possibilities, we failed to find a word in acceptable edit distance + it was not in our dictionary
        return (possibilities.size()>1) ? possibilities.toArray(new String[0]) : new String[] {"f"}; 
    }

    // with the unigrams we're just sorting the suggestions we got from levenschtein on popularity
    static String[] unigramSorting(String[] guesses, List<String> rank){
        List<String> l_guesses = Arrays.asList(guesses);

        Comparator<String> best = new Comparator<String>() {
            @Override
            public int compare(String a, String b){
                boolean air = rank.contains(a); // a/b in rank
                boolean bir = rank.contains(b);

                if (air && bir){
                    return Float.compare(rank.indexOf(a), rank.indexOf(b));

                // if in freq list at all, instantly more valid than if not
                }else if(air){
                    return -1; // a comes before b
                }else if(bir){
                    return 1; // b comes before a
                }else{
                    return 0; // equal, sort others around it
                }
            }
        };
        Collections.sort(l_guesses,best);

        return l_guesses.toArray(new String[0]);
    }

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] phrase = br.readLine().split(" ");
        br.close();

        // parse csvs
        List<List<String>> rows = new ArrayList<>();
        List<String> rank = new ArrayList<>();
        try{ // TODO: thread
            parseData(rows, rank);
        }catch(IOException e){
            e.printStackTrace();
        }

        // out
        for(String s : phrase){
            String[] guesses = levenshteinSuggestions(s.toLowerCase(), rows);
            String[] corrections = 
            (guesses[0].equals("c") || guesses[0].equals("f")) // special cases (correct, failed)
            ? guesses
            : unigramSorting(guesses, rank) ;
           
            if(corrections[0]!="c" && corrections[0]!="f"){ 
                System.out.println("\n ("+s+") Did you mean...");
                for(String maybe : corrections){
                    System.out.print(maybe+" ");
                }
                System.out.println("");
            }
        }

        // approximation of memory used to run the program in the jvm
        long mem =Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory();
        mem=(long)((double)mem/Math.pow(10.0,6.0));
        System.out.println("\n~"+mem+" megabytes");
    }
}