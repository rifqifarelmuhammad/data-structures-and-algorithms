// Nama: Rifqi Farel Muhammad
// NPM: 2106650310
// Kelas: SDA D

//Mengimport module yang dibutuhkan
import java.io.*;
import java.util.StringTokenizer;

public class Lab1 {
    //Atribute class with private static modifier
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        int[] sofita = {0,0,0,0,0,0};  //Counter huruf SOFITA

        //Melakukan pensortiran huruf menggunakan for loop dan branching
        for (int i = 0; i < N; i++){
            if (x[i] == 'S'){
                sofita[0]++;
            }else if (x[i] == 'O'){
                if (sofita[0] > sofita[1]){
                    sofita[1]++;
                }
            }else if (x[i] == 'F'){
                if (sofita[1] > sofita[2]){
                    sofita[2]++;
                }
            }else if (x[i] == 'I'){
                if (sofita[2] > sofita[3]){
                    sofita[3]++;
                }
            }else if (x[i] == 'T'){
                if (sofita[3] > sofita[4]){
                    sofita[4]++;
                }
            }else if (x[i] == 'A'){
                if (sofita[4] > sofita[5]){
                    sofita[5]++;
                }
            }
        }

        return (N - (sofita[5] * 6)); //Mereturn hasil
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of N
        int N = in.nextInt();

        // Read value of x
        char[] x = new char[N];
        for (int i = 0; i < N; ++i) {
            x[i] = in.next().charAt(0);
        }

        int ans = getTotalDeletedLetters(N, x);
        out.println(ans);

        // don't forget to close/flush the output
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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

    }
}