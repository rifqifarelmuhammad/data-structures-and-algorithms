// Nama: Rifqi Farel Muhammad
// NPM: 2106650310
// Kelas: SDA D

//Mengimport module yang dibutuhkan
import java.io.*;
import java.util.StringTokenizer;

public class Lab0 {
    //Atribute class with private static modifier
    private static InputReader in;
    private static PrintWriter out;

    //"multiplyMod" merupakan function perkalian dari N bilangan dan dimodulo dengan 1000000007
    static int multiplyMod(int N, int Mod, int[] x) {
        long total = 1;  //Menggunakan type data long agar bisa memuat angka yang besar ketika perkalian

        for(int i = 0; i < N; i++){
            //Mengalikan variable total dengan bilangan berikutnya pada array x, kemudian dimodulokan
            total = (total * x[i]) % Mod;
        }
        
        return (int) total;  //Mereturn output
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of N
        int N = in.nextInt();

        // Read value of x
        int[] x = new int[N];
        for (int i = 0; i < N; ++i) {
            x[i] = in.nextInt();
        }

        // TODO: implement method multiplyMod(int, int, int[]) to get the answer
        int ans = multiplyMod(N, (int) (1e9+7), x);
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