//Mendapatkan inspirasi dari Dhafin Raditya Juliawan

import java.io.*;
import java.util.StringTokenizer;

public class Lab3 {
    private static InputReader in;
    private static PrintWriter out;

    public static char[] A;
    public static int N;
    public static int[][] value;
    public static int [] counterR;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Inisialisasi Array Input
        N = in.nextInt();
        A = new char[N];
        value = new int[N][N];
        counterR = new int[N];
        int temp = 0;

        // Membaca File Input
        for (int i = 0; i < N; i++) {
            A[i] = in.nextChar();
            if (A[i] == 'R'){
                temp += 1;
            }
            counterR[i] = temp;
        }

        // Run Solusi
        int solution = getMaxRedVotes(0, N - 1);
        out.print(solution);

        // Tutup OutputStream
        out.close();
    }

    public static int getMaxRedVotes(int start, int end){
        for (int i = 0; i < value.length; i++){
            for (int j = 0; j < value[i].length; j++){
                value[i][j] = -1;
            }
        }

        return getMaxRedVotesHelper(start, end);
    }

    public static int getMaxRedVotesHelper(int start, int end) {
        // TODO : Implementasikan solusi rekursif untuk mendapatkan skor vote maksimal
        // untuk RED pada subarray A[start ... end] (inklusif)

        int RCount = counterR[end];
        int BCount = N - counterR[end];
        int jumlah = 0;
        int maks = 0;

        if (value[start][end] != -1){
            // System.out.println("hai");
            // System.out.println(value[start][end]);
            // System.out.println();
            return value[start][end];
        }
        
        if (start == end){
            if (A[start] == 'R'){
                return 1;
            }else{
                return 0;
            }
        }
        
        // if (((end+1) - start) > 2){

        //     for (int i = start; i <= end; i++){
        //         if (A[i] == 'R'){
        //             RCount++;
        //         }else{
        //             BCount++;
        //         }
    
        //         if (RCount > A.length/2){
        //            return (end+1) - start;
        //         }
        //     }
        // }

        if (RCount > BCount){
            System.out.println("masuk");
            return (end+1) - start;
        }
        
        for (int i = start; i < end; i++){
            jumlah = getMaxRedVotesHelper(start, i) + getMaxRedVotesHelper((i+1), end);

            if (jumlah > maks){
                maks = jumlah;
            }
        }

        // System.out.println(Arrays.deepToString(value));
        // System.out.print(value[0][0] + " ");

        value[start][end] = maks;

        return maks;
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public char nextChar() {
            return next().equals("R") ? 'R' : 'B';
        }
    }
}