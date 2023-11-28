// Nama: Rifqi Farel Muhammad
// NPM: 2106650310
// Kelas: SDA D
// Mendapatkan inspirasi dari Dhafin Raditya Juliawan & Fadhlan Hasyim & Bonaventura Galang

//Mengimport module yang dibutuhkan
import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class TP1 {
    //Atribute class
    private static InputReader in;
    private static PrintWriter out;
    private static MenuRestoran[] listMenu;
    private static Koki[] listKoki;
    private static TreeSet<Koki> listKokiTerurutA = new TreeSet<Koki>();
    private static TreeSet<Koki> listKokiTerurutG = new TreeSet<Koki>();
    private static TreeSet<Koki> listKokiTerurutS = new TreeSet<Koki>();
    private static Pelanggan[] listPelanggan;
    private static Deque<Order> urutanOrder = new ArrayDeque<Order>();
    private static int N;
    private static int P;
    private static int Y;
    private static int M;
    private static int[] negatifCounter;
    private static long tempDP[][][][];

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        //Inisialisasi Array Menu Restoran & harganya
        M = in.nextInt();
        listMenu = new MenuRestoran[M];
        tempDP = new long[M][3][3][3];
        
        //Menu Restoran
        for (int i = 0; i < M; i++){
            int harga = in.nextInt();
            char jenisMakanan = in.nextChar();
            listMenu[i] = new MenuRestoran((i+1), jenisMakanan, harga);
        }

        //Inisialisasi Array Spesialisasi Koki
        int V = in.nextInt();
        listKoki = new Koki[V];

        //Spesialisasi Koki
        for (int i = 0; i < V; i++){
            char spesialisasi = in.nextChar();
            Koki koki = new Koki((i+1), spesialisasi);
            listKoki[i] = koki;
            
            if (spesialisasi == 'A'){
                listKokiTerurutA.add(koki);
            }else if (spesialisasi == 'G'){
                listKokiTerurutG.add(koki);
            }else{
                listKokiTerurutS.add(koki);
            }
        }

        //Input banyaknya pelanggan secara keseluruhan
        P = in.nextInt();
        listPelanggan = new Pelanggan[P];

        //Input banyaknya kursi yang tersedia pada restoran
        N = in.nextInt();

        //Input jumlah hari restoran beroperasi
        Y = in.nextInt();

        for (int i = 0; i < Y; i++){
            int jumlahPelanggan = in.nextInt();
            negatifCounter = new int[jumlahPelanggan];  //Untuk prefix sum
            char temp; 
            int jumlahPelangganAman = 0;  //Ruang tunggu
            
            for (int j = 0; j < jumlahPelanggan; j++){
                int id = in.nextInt();

                if ((listPelanggan[id - 1]) == null){  //Kalau baru dimasukin, bikin objek pelanggan
                    listPelanggan[id-1] = new Pelanggan(id);
                }

                temp = in.nextChar();
                int uangPelanggan = in.nextInt();
                listPelanggan[id-1].setUang(uangPelanggan); //Ngeset uang

                if (temp != '?'){ 
                    listPelanggan[id-1].setStatusKesehatan(temp);  //Ngeset status kesehatan (bukan ?)

                    //Prefix sum
                    if (temp == '-'){
                        if (j != 0){
                            negatifCounter[j] = negatifCounter[j-1] + 1;
                        }else{
                            negatifCounter[j] = 1;
                        }
                    }else{
                        if (j != 0){
                            negatifCounter[j] = negatifCounter[j-1];
                        }else{
                            negatifCounter[j] = 0;
                        }
                    }
                }else{  
                    //Advance scanning make prefix sum
                    int advanceScanning = in.nextInt();
                    int countNegatif = 0;

                    if (j - advanceScanning == 0){
                        countNegatif = negatifCounter[j-1];
                    }else{
                        countNegatif = negatifCounter[j-1] - negatifCounter[j - advanceScanning - 1];
                    }

                    int countPositif = advanceScanning - countNegatif;
                    if (countNegatif >= countPositif){
                        listPelanggan[id-1].setStatusKesehatan('-');
                        negatifCounter[j] = negatifCounter[j-1] + 1;
                    }else{
                        listPelanggan[id-1].setStatusKesehatan('+');
                        negatifCounter[j] = negatifCounter[j-1] + 0;
                    }
                }

                //State awal
                if (listPelanggan[id-1].getBlacklist()){
                    out.print(3 + " ");
                }else{
                    if (listPelanggan[id-1].getStatusKesehatan() == '+'){
                        out.print(0 + " ");
                    }else{
                        jumlahPelangganAman++;
    
                        if (jumlahPelangganAman <= N){
                            out.print(1 + " ");
                        }else{
                            out.print(2 + " ");
                        }
                    }
                }
            }
            out.println();

            int jumlahPelayanan = in.nextInt();
            
            for (int j = 0; j < jumlahPelayanan; j++){
                char pelayanan = in.nextChar();
                if (pelayanan == 'P'){  //Query P
                    int id_pelanggan = in.nextInt();
                    int index_makanan = in.nextInt();
                    out.println(P(id_pelanggan, index_makanan));
                }else if (pelayanan == 'L'){ //Query L
                    out.println(L());
                }else if (pelayanan == 'B'){ //Query B
                    int id_pelanggan = in.nextInt();
                    out.println(B(id_pelanggan));
                }else if (pelayanan == 'C'){ //Query C
                    int Q = in.nextInt();
                    C(Q);
                }else{ //Query D
                    long cost_A = (long) in.nextInt();
                    long cost_G = (long) in.nextInt();
                    long cost_S = (long) in.nextInt();
                    out.println(D(cost_A, cost_G, cost_S));
                }
            }   
        }

        out.close();
    }

    public static int P(int id_pelanggan, int index_makanan){
        //Ngambil jenis makanan yang dipilih untuk ditambahkan ke tagihan pelanggan + menentukan koki yang melayani
        char jenisMakanan = listMenu[index_makanan-1].getJenisMakanan();
        listPelanggan[id_pelanggan-1].nambahPesanan(listMenu[index_makanan-1].getHarga());  //Nambah total biaya pelanggan
        Koki koki;

        //Nentuin koki yang ambil pesanan (dari tree set)
        if (jenisMakanan == 'A'){
            koki = listKokiTerurutA.first();
        }else if (jenisMakanan == 'G'){
            koki = listKokiTerurutG.first();
        }else{
            koki = listKokiTerurutS.first();
        }
        
        urutanOrder.addLast(new Order(id_pelanggan, koki));  //Menambahkan pesanan baru ke urutan paling kanan (akhir)

        return koki.getId();
    }

    public static int L(){
        //Mengambil orderan selanjutnya sesuai urutan
        Order order = urutanOrder.removeFirst();
        Koki koki = order.getKoki();
        int id_pelanggan = order.getId_Pelanggan();
        
        //Nambah jumlah pelayanan koki terus diset ulang di tree set nya
        if (koki.getSpesialisasi() == 'A'){
            listKokiTerurutA.remove(koki);
            koki.melayani();
            listKokiTerurutA.add(koki);
        }else if (koki.getSpesialisasi() == 'G'){
            listKokiTerurutG.remove(koki);
            koki.melayani();
            listKokiTerurutG.add(koki);
        }else{
            listKokiTerurutS.remove(koki);
            koki.melayani();
            listKokiTerurutS.add(koki);
        }
        
        return id_pelanggan;
    }

    public static int B(int id_pelanggan){
        //Jika uang pelanggan mencukupi
        if (listPelanggan[id_pelanggan-1].getUang() >= listPelanggan[id_pelanggan-1].getTotalHargaPesanan()){
            listPelanggan[id_pelanggan-1].newHari();
            return 1;
        }else{  //Jika uang pelanggan tidak mencukupi, langsung diblacklist untuk hari selanjutnya
            listPelanggan[id_pelanggan-1].blacklist();    
            listPelanggan[id_pelanggan-1].newHari();    
            return 0;
        }
    }

    public static void C(int Q){
        //Sorting menggunakan Arrays.sort
        Arrays.sort(listKoki, new Comparator<Koki>() {
            public int compare(Koki koki1, Koki koki2){
                //Sorting berdasarkan jumlah pelayanan
                if (koki1.getJumlahPelayanan() < koki2.getJumlahPelayanan()){
                    return koki1.getJumlahPelayanan() - koki2.getJumlahPelayanan();
                }else if (koki1.getJumlahPelayanan() > koki2.getJumlahPelayanan()){
                    return koki1.getJumlahPelayanan() - koki2.getJumlahPelayanan();
                }else{
                    //Sorting berdasarkan spesialisasi -> S G A
                    if (koki1.getSpesialisasi() < koki2.getSpesialisasi()){
                        return koki2.getSpesialisasi() - koki1.getSpesialisasi();
                    }else if (koki1.getSpesialisasi() > koki2.getSpesialisasi()){
                        return koki2.getSpesialisasi() - koki1.getSpesialisasi();
                    }else{
                        //Sorting berdasarkan Id
                        if (koki1.getId() < koki2.getId()){
                            return koki1.getId() - koki2.getId();
                        }else{
                            return koki1.getId() - koki2.getId();
                        }
                    }
                }
            }
            
        });

        //Mencetak output
        for (int k = 0; k < Q; k++){
            if (k != (Q-1)){
                out.print(listKoki[k].getId() + " ");
            }else{
                out.println(listKoki[k].getId());
            }
        }
    }

    public static long D(Long cost_A, long cost_G, long cost_S){
        //Menginisialisasi seluruh nilai index array dengan nilai yang besar (agar pencarian harga minimal dapat optimal)
        for (int i = 0; i < listMenu.length; i++){
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 3; k++){
                    for (int l = 0; l < 3; l++){
                        tempDP[i][j][k][l] = (long) 5e9+1;
                    }
                }
            }
        }

        //Before = Indeks terakhir huruf tersebut muncul
        int beforeS = -1;
        int beforeG = -1;
        int beforeA = -1;

        /*
            State 0 -> Tidak buat paket sama sekali, jadi tinggal nambahin harga makanan dari indeks ke 0 - i

            State 1 -> Buat paket. Ada 2 kemungkinan:
            1). Bikin paket baru (Jadi ada 2 huruf yang sama -> Pakai before)
            2). Lanjutin paket sebelumnya (ada huruf yang sama lagi -> Pakai before juga)

            State 2 -> Berhenti bikin paket. Ada 3 kemungkinan:
            1). Indeks ke i adalah elemen terakhir, jadi tinggal return nilainya aja (pakai state sebelumnya / state 1)
            2). Elemen sekarang g jadi paket, tapi sebelumnya ada paket -> Ambil harga paket sebelumnya + harga menu indeks ke i
            3). Elemen sekarang ga jadi paket, tapi sebelumnya juga g ada paket -> Basicnya sama kayak state 0, hanya sedikit berbeda
        */

        for (int i = 0; i < listMenu.length; i++){
            if (i == 0){  //Base case (listMenu[0] hanya 1 makanan, jadi langsung ambil harga makanan dia)
                tempDP[i][0][0][0] = listMenu[i].getHarga();

                //Mengubah nilai before (sesuai dengan jenis makanan yang muncul)
                if (listMenu[i].getJenisMakanan() == 'S'){
                    beforeS = i;
                }else if (listMenu[i].getJenisMakanan() == 'G'){
                    beforeG = i;
                }else{
                    beforeA = i;
                }
                continue;
            }else{  //Dynamic Programming case
                //[i][j][k][l] = [Menu][S][G][A]
                for (int j = 0; j < 3; j++){
                    for (int k = 0; k < 3; k++){
                        for (int l = 0; l < 3; l++){
                            //Tidak boleh ada kemuncul state 1 sebanyak >= 2 agar tidak terjadi overlap
                            if (j == 0 && k == 1 && l == 1){
                                continue;
                            }else if (j == 1 && k == 0 && l == 1){
                                continue;
                            }else if (j == 1 && k == 1 && l == 0){
                                continue;
                            }else if (j == 1 && k == 1 && l == 1){
                                continue;
                            }else if (j == 2 && k == 1 && l == 1){
                                continue;
                            }else if (j == 1 && k == 2 && l == 1){
                                continue;
                            }else if (j == 1 && k == 1 && l == 2){
                                continue;
                            }else{
                                //Fokus ke index S
                                if (listMenu[i].getJenisMakanan() == 'S'){
                                    if (j == 0){  // State 0
                                        if(i == 0){
                                            tempDP[i][j][k][l] = listMenu[i].getHarga();
                                        }else{
                                            tempDP[i][j][k][l] = tempDP[i-1][j][k][l] + listMenu[i].getHarga();
                                        }
                                    }else if (j == 1){ //State 1
                                        if (beforeS >= 0){
                                            if (beforeS == 0){
                                                tempDP[i][j][k][l] = cost_S * (i+1);
                                            }else{
                                                Long temp1 = tempDP[beforeS][j][k][l] + (cost_S * (i - beforeS));
                                                long temp2 = tempDP[beforeS-1][j-1][k][l] + (cost_S * (i - beforeS + 1));
                                                
                                                if (temp1 < temp2){
                                                    tempDP[i][j][k][l] = temp1;
                                                }else{
                                                    tempDP[i][j][k][l] = temp2;
                                                }
                                            }         
                                        }
                                    }else if (j == 2){ //State 2
                                        if (i == 0){
                                            tempDP[i][j][k][l] = tempDP[i][j-1][k][l];
                                        }else{
                                            long temp1 = tempDP[i][j-1][k][l];
                                            long temp2 = tempDP[i-1][j-1][k][l] + listMenu[i].getHarga();
                                            long temp3 = tempDP[i-1][j][k][l] + listMenu[i].getHarga();
                                            
                                            if (temp1 < temp2 && temp1 < temp3){
                                                tempDP[i][j][k][l] = temp1;
                                            }else if (temp2 < temp1 && temp2 < temp3){
                                                tempDP[i][j][k][l] = temp2;
                                            }else{
                                                tempDP[i][j][k][l] = temp3;
                                            }
                                        }
                                    }
                                }else if (listMenu[i].getJenisMakanan() == 'G'){  //Fokus ke index G
                                    if (k == 0){  //State 0
                                        if(i == 0){
                                            tempDP[i][j][k][l] = listMenu[i].getHarga();
                                        }else{
                                            tempDP[i][j][k][l] = tempDP[i-1][j][k][l] + listMenu[i].getHarga();
                                        }
                                    }else if (k == 1){  //State 1
                                        if (beforeG >= 0){
                                            if (beforeG == 0){
                                                tempDP[i][j][k][l] = cost_G * (i+1);
                                            }else{
                                                long temp1 = tempDP[beforeG][j][k][l] + (cost_G * (i - beforeG));
                                                long temp2 = tempDP[beforeG-1][j][k-1][l] + (cost_G * (i - beforeG + 1));
                                                
                                                if (temp1 < temp2){
                                                    tempDP[i][j][k][l] = temp1;
                                                }else{
                                                    tempDP[i][j][k][l] = temp2;
                                                }
                                            }         
                                        }
                                    }else if (k == 2){  //State 2
                                        if (i == 0){
                                            tempDP[i][j][k][l] = tempDP[i][j][k-1][l];
                                        }else{
                                            long temp1 = tempDP[i][j][k-1][l];
                                            long temp2 = tempDP[i-1][j][k-1][l] + listMenu[i].getHarga();
                                            long temp3 = tempDP[i-1][j][k][l] + listMenu[i].getHarga();
                                            
                                            if (temp1 < temp2 && temp1 < temp3){
                                                tempDP[i][j][k][l] = temp1;
                                            }else if (temp2 < temp1 && temp2 < temp3){
                                                tempDP[i][j][k][l] = temp2;
                                            }else{
                                                tempDP[i][j][k][l] = temp3;
                                            }
                                        }
                                    }
                                }else if (listMenu[i].getJenisMakanan() == 'A'){  //Fokus ke index A
                                    if (l == 0){  //State 0
                                        if(i == 0){
                                            tempDP[i][j][k][l] = listMenu[i].getHarga();
                                        }else{
                                            tempDP[i][j][k][l] = tempDP[i-1][j][k][l] + listMenu[i].getHarga();
                                        }
                                    }else if (l == 1){  //State 1
                                        if (beforeA >= 0){
                                            if (beforeA == 0){
                                                tempDP[i][j][k][l] = cost_A * (i+1);
                                            }else{
                                                long temp1 = tempDP[beforeA][j][k][l] + (cost_A * (i - beforeA));
                                                long temp2 = tempDP[beforeA-1][j][k][l-1] + (cost_A * (i - beforeA + 1));
                                                
                                                if (temp1 < temp2){
                                                    tempDP[i][j][k][l] = temp1;
                                                }else{
                                                    tempDP[i][j][k][l] = temp2;
                                                }
                                            }         
                                        }
                                    }else if (l == 2){  //State 2
                                        if (i == 0){
                                            tempDP[i][j][k][l] = tempDP[i][j][k][l-1];
                                        }else{
                                            long temp1 = tempDP[i][j][k][l-1];
                                            long temp2 = tempDP[i-1][j][k][l-1] + listMenu[i].getHarga();
                                            long temp3 = tempDP[i-1][j][k][l] + listMenu[i].getHarga();
                                            
                                            if (temp1 < temp2 && temp1 < temp3){
                                                tempDP[i][j][k][l] = temp1;
                                            }else if (temp2 < temp1 && temp2 < temp3){
                                                tempDP[i][j][k][l] = temp2;
                                            }else{
                                                tempDP[i][j][k][l] = temp3;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //Mengupdate nilai before
                if (listMenu[i].getJenisMakanan() == 'S'){
                    beforeS = i;
                }else if (listMenu[i].getJenisMakanan() == 'G'){
                    beforeG = i;
                }else{
                    beforeA = i;
                }
            }
        }
        
        //Mencari harga minimum (akses index terakhir -> Menu paling akhir)
        long min = Long.MAX_VALUE;
        for (int i = 0; i < 27; i++){
            int A = i % 3;
            int G = (i / 3) % 3;
            int S = (i / 9) % 3;
            min = Math.min(min, tempDP[M-1][S][G][A]);
        }

        return min;
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
            String input = next();
            if (input.equals("A")){
                return 'A';
            }else if(input.equals("G")){
                return 'G';
            }else if(input.equals("S")){
                return 'S';
            }else if(input.equals("+")){
                return '+';
            }else if(input.equals("-")){
                return '-';
            }else if(input.equals("?")){
                return '?';
            }else if(input.equals("P")){
                return 'P';
            }else if(input.equals("L")){
                return 'L';
            }else if(input.equals("B")){
                return 'B';
            }else if(input.equals("C")){
                return 'C';
            }else{
                return 'D';
            }
        }
    }

    static class Koki implements Comparable<Koki>{
        private int id;
        private int jumlahPelayanan;
        private char spesialisasi;

        Koki(int id, char spesialisasi){
            this.id = id;
            this.spesialisasi = spesialisasi;
        }

        public void melayani(){
            this.jumlahPelayanan++;
        }
        
        public int getId(){
            return this.id;
        }

        public int getJumlahPelayanan(){
            return this.jumlahPelayanan;
        }

        public char getSpesialisasi(){
            return this.spesialisasi;
        }

        public int compareTo(Koki koki) {
            if (Integer.compare(this.getJumlahPelayanan(), koki.getJumlahPelayanan()) != 0) {
                return Integer.compare(this.getJumlahPelayanan(), koki.getJumlahPelayanan());
            } else if (Integer.compare((int) koki.getSpesialisasi(), (int) this.getSpesialisasi()) != 0) {
                return Integer.compare((int) koki.getSpesialisasi(), (int) this.getSpesialisasi());
            } else {
                return Integer.compare(this.getId(), koki.getId());
            }
        }
    }

    static class MenuRestoran{
        private int id;
        private char jenisMakanan;
        private int harga;

        MenuRestoran(int id, char jenisMakanan, int harga){
            this.id = id;
            this.jenisMakanan = jenisMakanan;
            this.harga = harga;
        }

        public int getId(){
            return this.id;
        }

        public char getJenisMakanan(){
            return this.jenisMakanan;
        }

        public int getHarga(){
            return this.harga;
        }
    }
    
    static class Pelanggan{
        private int id;
        private boolean blacklist;
        private int totalHargaPesanan;
        private char statusKesehatan;
        private int uang;

        public Pelanggan(int id){
            this.id = id;
        }

        public void nambahPesanan(int harga){
            totalHargaPesanan += harga;
        }

        public void setStatusKesehatan(char statusKesehatan){
            this.statusKesehatan = statusKesehatan;
        }

        public void setUang(int uang){
            this.uang = uang;
        }

        public void newHari(){
            totalHargaPesanan = 0;
        }

        public void blacklist(){
            this.blacklist = true;
        }

        public int getTotalHargaPesanan(){
            return this.totalHargaPesanan;
        }

        public int getId(){
            return this.id;
        }

        public boolean getBlacklist(){
            return this.blacklist;
        }

        public char getStatusKesehatan(){
            return statusKesehatan;
        }

        public int getUang(){
            return this.uang;
        }
    }

    static class Order{
        private int id_pelanggan;
        private Koki koki;

        public Order(int id_pelanggan, Koki koki){
            this.id_pelanggan = id_pelanggan;
            this.koki = koki;
        }

        public int getId_Pelanggan(){
            return this.id_pelanggan;
        }

        public Koki getKoki(){
            return koki;
        }
    }
}