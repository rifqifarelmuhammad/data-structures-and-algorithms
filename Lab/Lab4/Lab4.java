import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Lab4 {
    private static InputReader in;
    static PrintWriter out;
    private static Gedung firstGedung;
    private static Karakter denji;
    private static Karakter iblis;
    private static int jumlahPertemuan;
    
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int jumlahGedung = in.nextInt();
        for (int i = 0; i < jumlahGedung; i++) {
            String namaGedung = in.next();
            int jumlahLantai = in.nextInt();

            Gedung tempGedung = new Gedung(namaGedung, jumlahLantai);

            if (firstGedung == null){
                firstGedung = tempGedung;
            }else{
                Gedung temp = firstGedung;

                while (temp.next != firstGedung){
                    temp = temp.next;
                }

                temp.next = tempGedung;
            }

            tempGedung.next = firstGedung;
        }

        String gedungDenji = in.next();
        int lantaiDenji = in.nextInt();

        String gedungIblis = in.next();
        int lantaiIblis = in.nextInt();

        Gedung tempGedung = firstGedung;
        // int indeksGedungDenji = 0;
        // int indeksGedungIblis = 0;
        for (int j = 0; j < jumlahGedung; j++){
            if (tempGedung.namaGedung.equals(gedungDenji)){
                denji = new Karakter(tempGedung, lantaiDenji);
                // indeksGedungDenji = 0;
                denji.arah = 0;

            }
            
            if(tempGedung.namaGedung.equals(gedungIblis)){
                iblis = new Karakter(tempGedung, lantaiIblis);
                // indeksGedungIblis = 1;
                iblis.arah = 1;
            }

            tempGedung = tempGedung.next;
        }

        // if (indeksGedungDenji > indeksGedungIblis){
        //     if ((indeksGedungDenji - indeksGedungIblis) % 2 == 1){
        //         denji.arah = 0;
        //         iblis.arah = 1;
        //     }else{
        //         denji.arah = 0;
        //         iblis.arah = 1;
        //     }
        // }


        int Q = in.nextInt();

        for (int i = 0; i < Q; i++) {

            String command = in.next();

            if (command.equals("GERAK")) {
                gerak();
            } else if (command.equals("HANCUR")) {
                hancur();
            } else if (command.equals("TAMBAH")) {
                tambah();
            } else if (command.equals("PINDAH")) {
                pindah();
            }
        }

        out.close();
    }

    // TODO: Implemen perintah GERAK
    static void gerak() {

        if (denji.arah == 0){
            if (denji.currentLantai != denji.currentGedung.jumlahLantai){
                denji.currentLantai++;
            }else{
                denji.currentGedung = denji.currentGedung.next;
                denji.currentLantai = denji.currentGedung.jumlahLantai;
                denji.arah = 1;
            }

            if (denji.currentGedung.namaGedung.equals(iblis.currentGedung.namaGedung)){
                if (denji.currentLantai == iblis.currentLantai){
                    jumlahPertemuan++;
                }
            }
        }else{
            if (denji.currentLantai != 1){
                denji.currentLantai--;
            }else{
                denji.currentGedung = denji.currentGedung.next;
                denji.currentLantai = 1;
                denji.arah = 0;
            }

            if (denji.currentGedung.namaGedung.equals(iblis.currentGedung.namaGedung)){
                if (denji.currentLantai == iblis.currentLantai){
                    jumlahPertemuan++;
                }
            }
        }

        for (int i = 0; i < 2; i++){
            if (iblis.arah == 0){
                if (iblis.currentLantai != iblis.currentGedung.jumlahLantai){
                    iblis.currentLantai++;
                }else{
                    iblis.currentGedung = iblis.currentGedung.next;
                    iblis.currentLantai = iblis.currentGedung.jumlahLantai;
                    iblis.arah = 1;
                }
            }else{
                if (iblis.currentLantai != 1){
                    iblis.currentLantai--;
                }else{
                    iblis.currentGedung = iblis.currentGedung.next;
                    iblis.currentLantai = 1;
                    iblis.arah = 0;
                }
            }
        }

        if (denji.currentGedung.namaGedung.equals(iblis.currentGedung.namaGedung)){
            if (denji.currentLantai == iblis.currentLantai){
                jumlahPertemuan++;
            }
        }

        out.print(denji.currentGedung.namaGedung + " " + denji.currentLantai + " ");
        out.print(iblis.currentGedung.namaGedung + " " + iblis.currentLantai + " ");
        out.println(jumlahPertemuan);
    }

    // TODO: Implemen perintah HANCUR
    static void hancur() {
        // boolean flag = false;
        if (denji.currentLantai == 1){
            out.println(denji.currentGedung.namaGedung + " " + (-1));
        }else if (denji.currentGedung.namaGedung.equals(iblis.currentGedung.namaGedung)){
            if (denji.currentLantai - 1 == iblis.currentLantai){
                out.println(denji.currentGedung.namaGedung + " " + (-1));
            }else{
                out.println(denji.currentGedung.namaGedung + " " + (denji.currentLantai - 1));
                denji.currentGedung.jumlahLantai--;
                if (iblis.currentLantai >= denji.currentLantai){
                    iblis.currentLantai--;
                }
                denji.currentLantai--;
            }
        }else{
            out.println(denji.currentGedung.namaGedung + " " + (denji.currentLantai - 1));
            denji.currentGedung.jumlahLantai--;
            denji.currentLantai--;
        }

    }

    // TODO: Implemen perintah TAMBAH
    static void tambah() {
        iblis.currentGedung.jumlahLantai = iblis.currentGedung.jumlahLantai + 1;

        if (iblis.currentLantai == 1){
            out.println(iblis.currentGedung.namaGedung + " " + 1);
        }else{
            out.println(iblis.currentGedung.namaGedung + " " + iblis.currentLantai);
        }

        iblis.currentLantai++;

        if (denji.currentGedung.equals(iblis.currentGedung)){
            if (denji.currentLantai >= (iblis.currentLantai-1)){
                denji.currentLantai++;
            }
        }
    }

    // TODO: Implemen perintah PINDAH
    static void pindah() {
        denji.currentGedung = denji.currentGedung.next;
        if (denji.arah == 0){
            denji.currentLantai = 1;
        }else{
            denji.currentLantai = denji.currentGedung.jumlahLantai;
        }

        if (denji.currentGedung.namaGedung.equals(iblis.currentGedung.namaGedung)){
            if (denji.currentLantai == iblis.currentLantai){
                jumlahPertemuan++;
            }
        }

        out.println(denji.currentGedung.namaGedung + " " + denji.currentLantai);
    }

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
class Gedung {
    public String namaGedung;
    public int jumlahLantai;
    public Gedung next;

    public Gedung(String namaGedung, int jumlahLantai) {
        this.namaGedung = namaGedung;
        this.jumlahLantai = jumlahLantai;
    }
}

// TODO: Lengkapi Class Karakter
class Karakter {
    public Gedung currentGedung;
    public int arah; //0 = naik, 1 = turun
    public int currentLantai;

    public Karakter(Gedung currentGedung, int currentLantai) {
        this.currentGedung = currentGedung;
        this.currentLantai = currentLantai;
    }
}