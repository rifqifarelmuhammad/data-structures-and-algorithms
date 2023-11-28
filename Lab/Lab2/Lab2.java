// Nama: Rifqi Farel Muhammad
// NPM: 2106650310
// Kelas: SDA D

//Mengimport module yang dibutuhkan
import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;


public class Lab2 {
    //Atribute class with private static modifier
    private static InputReader in;
    private static PrintWriter out;
    private static Deque<Deque<Integer>> conveyorBelt = new ArrayDeque<Deque<Integer>>();

    static int geserKanan() {
        if (conveyorBelt.peekLast() == null){  //Mengcek apakah toples terakhir kosong atau tidak
            return -1;
        }else if (conveyorBelt.size() == 1 && conveyorBelt.getFirst().isEmpty()){  //Mengecek apakah toples pertama kosong (jika jumlah toples = 1)
            return -1;
        }

        conveyorBelt.addFirst(conveyorBelt.removeLast());  //Geser kanan

        if (!conveyorBelt.getFirst().isEmpty()){  //Mengecek apakah suatu toples kosong atau tidak
            return conveyorBelt.getFirst().peek();  //Kue teratas pada toples
        }else{
            return -1;
        }
    }

    static int beliRasa(int rasa) {
        //Variable tambahan untuk program
        int temp = 0;
        boolean exist = false;

        if (conveyorBelt.peekFirst() == null){  //Mengcek apakah toples pertama kosong atau tidak
            return -1;
        }else if (conveyorBelt.size() == 1 && conveyorBelt.getFirst().isEmpty()){  //Mengecek apakah toples pertama kosong (jika jumlah toples = 1)
            return -1;
        }

        for (int i = 0; i < conveyorBelt.size(); i++){
            if (!conveyorBelt.getFirst().isEmpty()){
                if (conveyorBelt.getFirst().peek() == rasa){
                    exist = true;
                    if (i == 0){
                        break;
                    }else {
                        temp = i;
                    }
                }
            }
            
            geserKanan();   
        }

        if (exist){  //Jika kue yang diingkan terdapat pada bagian teratas toples
            int posisi = conveyorBelt.size() - temp;

            if (posisi < (conveyorBelt.size()/2)){
                for (int i = 0; i < posisi; i++){
                    conveyorBelt.addLast(conveyorBelt.removeFirst());
                }
            }else{
                for (int i = 0; i < (conveyorBelt.size() - posisi); i++){
                    geserKanan();
                }
            }

            conveyorBelt.getFirst().pop();
            if (temp == 0){
                return 0;
            }

            return posisi;
        }else{
            return -1;
        }
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);
        
        int N = in.nextInt();
        int X = in.nextInt();
        int C = in.nextInt();

        for (int i = 0; i < N; ++i) {
            Deque<Integer> kueDalamToples = new ArrayDeque<Integer>();

            for (int j = 0; j < X; j++) {

                int rasaKeJ = in.nextInt();
                kueDalamToples.addFirst(rasaKeJ);
            }

            conveyorBelt.addFirst(kueDalamToples);
        }

        for (int i = 0; i < C; i++) {
            String perintah = in.next();
            if (perintah.equals("GESER_KANAN")) {
                out.println(geserKanan());
            } else if (perintah.equals("BELI_RASA")) {
                int namaRasa = in.nextInt();
                out.println(beliRasa(namaRasa));
            }
        }
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