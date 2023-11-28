// Nama: Rifqi Farel Muhammad
// NPM: 2106650310
// Kelas: SDA D
// Mendapatkan inspirasi (ide) dari Bonaventura Galang

//Mengimport module yang dibutuhkan
import java.io.*;
import java.util.*;

public class TP2 {
    private static Mesin firstMesin;  //Pointer first pada linked list
    private static Mesin lastMesin;  //Pointer last pada linked list
    private static Mesin budi;  //Pointer current pada linked list
    private static int banyakMesin;
    private static FastIO io = new FastIO();  //Untuk input dan output lebih cepat

    public static void main(String[] args) {
        banyakMesin = io.nextInt();
        
        // Inisialisasi mesin menggunakan linked list
        for (int i = 0; i < banyakMesin; i++){
            Mesin mesin = new Mesin(i+1);

            if (firstMesin == null){
                firstMesin = mesin;
                firstMesin.next = lastMesin;
                firstMesin.prev = lastMesin;

                lastMesin = mesin;
                lastMesin.next = firstMesin;
                lastMesin.prev = firstMesin;

                budi = mesin;
            }else{
                lastMesin.next = mesin;
                mesin.prev = lastMesin;
                mesin.next = firstMesin;
                lastMesin = lastMesin.next;

                lastMesin.next = firstMesin;
                firstMesin.prev = lastMesin;
            }

            //Menginisialisasi score tiap mesin menggunakan AVL Tree
            int banyakScore = io.nextInt();
            for (int j = 0; j < banyakScore; j++){
                int score = io.nextInt();
                mesin.tree.root = mesin.tree.insertNode(mesin.tree.root, score);
            }
        }

        //Input dan pengolahan query
        int banyakQuery = io.nextInt();
        for (int i = 0; i < banyakQuery; i++){
            String query = io.next();

            if (query.equals("MAIN")){
                int Y = io.nextInt();
                io.println(MAIN(Y));
            }else if (query.equals("GERAK")){
                String arah = io.next();
                io.println(GERAK(arah));
            }else if (query.equals("HAPUS")){
                int X = io.nextInt();
                io.println(HAPUS(X));
            }else if (query.equals("LIHAT")){
                int L = io.nextInt();
                int H = io.nextInt();
                io.println(LIHAT(L, H));
            }else{
                io.println(EVALUASI());
            }
        }

        io.close();
    }

    //Query MAIN
    public static int MAIN(int Y){
        budi.tree.root = budi.tree.insertNode(budi.tree.root, Y);  //Menginsert node score baru ke AVL Tree
        return budi.tree.posisi(budi.tree.root, Y);  //Mencari urutan posisi dari node yang baru dimasukkan ke AVL Tree
    }

    //Query GERAK
    public static int GERAK(String arah){
        if (arah.equals("KIRI")){  //Memindahkan posisi current ke prev nya
            budi = budi.prev;
        }else{  //Memindahkan posisi current ke next nya
            budi = budi.next;
        }
        return budi.nomorIdentitas;
    }

    //Query HAPUS
    public static long HAPUS(int X){
        long output;  //Jumlah score yang dihapus
        int size;

        //Size root dari tree mesin di depan budi
        if (budi.tree.root == null){
            size = 0;
        }else{
            size = budi.tree.root.size;
        }
        
        //Jika X >= size, maka seluruh score dalam mesin dihapus dan mesin dipindahkan ke paling kanan (last)
        if (X >= size){
            if (budi.tree.root == null){
                output = 0;
            }else{
                output = budi.tree.root.sum;
            }

            budi.tree = new AVLTree();

            if (firstMesin != lastMesin){  //Jika mesin > 1, budi bergerak ke mesin sebelahnya
                if (budi == firstMesin){  //Jika budi berada di depan mesin terkiri, semua pointer tinggal dipindahkan ke next nya
                    budi = budi.next;
                    lastMesin = lastMesin.next;
                    firstMesin = firstMesin.next;
                }else if (budi == lastMesin){  //Jika budi berada di depan mesin terkanan, hanya budi yang berpindah ke next nya
                    budi = budi.next;
                }else{
                    //Jika budi berada di depan mesin yang berada di tengah-tengah, pindahkan mesin ke paling kanan & budi pindah ke mesin sebelahnya (sebelum dipindahkan)
                    Mesin temp = budi;

                    budi = budi.next;
                    temp.next.prev = temp.prev;
                    temp.prev.next = temp.next;
                    
                    lastMesin.next = temp;
                    firstMesin.prev = temp;
                    temp.next = firstMesin;
                    temp.prev = lastMesin;
                    lastMesin = lastMesin.next;
                }
            }
        }else{  //Jika X < size, penghapusan terjadi
            long before = budi.tree.root.sum;  //Jumlah score sebelum penghapusan
            
            while (X > 0 && budi.tree.root != null){
                int beforeDelete = budi.tree.root.size;  //Size tree sebelum penghapusan

                budi.tree.root = findHapus(budi.tree.root, X);  //Menghapus node paling kanan

                //size tree sesudah penghapusan
                int afterDelete;
                if (budi.tree.root != null){
                    afterDelete = budi.tree.root.size;;
                }else{
                    afterDelete = 0;
                }

                X = X - (beforeDelete - afterDelete);  //Mengupdate nilai X
            }

            long after = budi.tree.root.sum;  //Jumlah score setelah penghapusan

            output = before - after;  //Score yang dihapus
        }

        return output;
    }

    //Mencari node terbesar (terkanan) dan melakukan penghapusan sesuai prosedur soal
    public static Node findHapus(Node node, int X){
        Node output;
        if (node.right != null){  //Selama anak kanan dari suatu subtree masih ada, rekursifkan ke kanan
            node.right = findHapus(node.right, X);
            output = node;
        }else{
            if (node.sama > X){  //Jika jumlah score yang sama pada suatu node > X, cukup hapus jumlah score yang sama saja (bukan nodenya)
                node.sama -= X;
                output = node;
            }else{  //Jika jumlah score yang sama pada suatu node <= X, hapus nodenya
                output = budi.tree.deleteNode(node, node.score);
            }
        }

        if (output == null){  //Langsung return node jika null
            return output;
        }

        //Balancing dan melakukan update size & sum pada node
        output.height = Math.max(budi.tree.getHeight(output.left), budi.tree.getHeight(output.right)) + 1;
        output.update();
        output.updateSum();
        if (budi.tree.getBalance(output) > 1){
            if (budi.tree.getBalance(output.left) >= 0){
                return budi.tree.rightRotate(output);
            }else {
                output.left = budi.tree.leftRotate(output.left);
                return budi.tree.rightRotate(output);
            }
        }else if (budi.tree.getBalance(output) < -1){
            if (budi.tree.getBalance(output.right) <= 0){
                return budi.tree.leftRotate(output);
            }else {
                output.right = budi.tree.rightRotate(output.right);
                return budi.tree.leftRotate(output);
            }
        }
        return output;
    }

    //Query LIHAT
    public static int LIHAT(int L, int H){
        int lowerCount = lower(budi.tree.root, L);  //Jumlah node yang berada di bawah node lower
        int upperCount = upper(budi.tree.root, H);  //Jumlah node yang berada di bawah node upper (inklusif)

        return upperCount - lowerCount;
    }

    //Mencari jumlah node yang berada di bawah node dengan nilai yang paling mendekati L (batas bawah)
    public static int lower(Node node, int L){
        if (node == null){
            return 0;
        }

        if (L == node.score){  //Jika L == node.score -> ambil anak kirinya
            if (node.left != null){
                return node.left.size;
            }else{
                return 0;
            }
        }else if (L < node.score){  //Jika L < node.score -> rekursifin ke anak kirinya
            return lower(node.left, L);
        }else{  //Jika L > node.score -> rekurifin ke anak kanannya tapi ditambahin jumlah anak kirinya dan score yang sama pada root subtree tersebut
            if (node.left != null){
                return lower(node.right, L) + node.left.size + node.sama;
            }else{
                return lower(node.right, L) + node.sama;
            }
        }
    }

    //Mencari jumlah node yang berada di bawah node dengan nilai yang paling mendekati H (batas atas) -> inklusif
    public static int upper(Node node, int H){
        if (node == null){
            return 0;
        }

        if (H == node.score){  //Jika H == node.score -> ambil anak kiri dan jumlah node yang sama pada root subtreenya
            if (node.left != null){
                return node.left.size + node.sama;
            }else{
                return node.sama;
            }
        }else if (H > node.score){  //Jika H > node.score -> rekurifin ke anak kanannya tapi ditambahin jumlah anak kirinya dan score yang sama pada root subtree tersebut
            if (node.left != null){
                return upper(node.right, H) + node.sama + node.left.size;
            }else{
                return upper(node.right, H) + node.sama;
            }
        }else{  //Jika H < node.score -> Rekursifin ke anak kirinya
            return upper(node.left, H);
        }
    }

    //Melakukan evaluasi dengan cara sorting menggunakan merge sort
    public static int EVALUASI(){
        if (lastMesin == firstMesin){  //Jika hanya ada 1 mesin
            return 1;
        }else{  //Jika > 1 mesin
            //Memasukkan semua mesin ke dalam array
            Mesin mesinBudi = budi;
            int nowMesin = 0;
            int counter = 1;
    
            Mesin[] listMesin = new Mesin[banyakMesin];
            listMesin[0] = firstMesin;
            Mesin temp = firstMesin.next;
            while (temp != firstMesin){
                listMesin[counter] = temp;
                temp = temp.next;
                counter++;
            }
            
            //Melakukan sorting menggunakan merge sort
            sort(listMesin, 0, banyakMesin - 1);
    
            //Menyusun kembali mesin dari array dalam bentuk linked list
            for (int i = 0; i < banyakMesin; i++){
                if (i == 0){
                    firstMesin = listMesin[0];
                }else if (i == banyakMesin - 1){
                    lastMesin = listMesin[banyakMesin -1];
                    lastMesin.next = firstMesin;
                    firstMesin.prev = lastMesin;
                }else{
                    listMesin[i-1].next = listMesin[i];
                    listMesin[i].prev = listMesin[i-1];
                    listMesin[i+1].prev = listMesin[i];
                    listMesin[i].next = listMesin[i+1];
                }

                if (listMesin[i] == mesinBudi){  //Mencari posisi mesin yang sebelumnya berada di depan budi
                    nowMesin = i;
                }
            }
    
            return nowMesin + 1;
        }
    }

    //Referensi: https://www.geeksforgeeks.org/merge-sort/
    //Memisahkan array dan menggabungkannya sekaligus melakukan sorting
    public static void sort(Mesin[] listMesin, int awal, int akhir){
        if (awal < akhir){
            //Memisahkan array
            int middle = (awal + (akhir - 1))/2;
            sort(listMesin, awal, middle);
            sort(listMesin, middle + 1, akhir);

            //Menggabungkan array & Sorting
            merge(listMesin, awal, middle, akhir);
        }
    }

    public static void merge(Mesin[] listMesin, int awal, int middle, int akhir){
        //Menggabungkan array & Sorting
        int length1 = middle - awal + 1;
        int length2 = akhir - middle;
        
        Mesin left[] = new Mesin[length1];
        Mesin right[] = new Mesin[length2];

        for (int i = 0; i < length1; ++i){
            left[i] = listMesin[awal + i];
        }
        for (int j = 0; j < length2; ++j){
            right[j] = listMesin[middle + 1 + j];
        }

        int ii = 0;
        int jj = 0;
        int k = awal;

        while(ii < length1 && jj < length2){
            int leftSize;
            int rightSize;

            if (left[ii].tree.root == null){
                leftSize= 0;
            }else{
                leftSize = left[ii].tree.root.size;
            }

            if (right[jj].tree.root == null){
                rightSize = 0;
            }else{
                rightSize = right[jj].tree.root.size;
            }

            if (leftSize > rightSize){
                listMesin[k] = left[ii];
                ii++;
            }else if (leftSize == rightSize){
                if (left[ii].nomorIdentitas < right[jj].nomorIdentitas){
                    listMesin[k] = left[ii];
                    ii++;
                }else{
                    listMesin[k] = right[jj];
                    jj++;
                }
            }else{
                listMesin[k] = right[jj];
                jj++;
            }
            k++;
        }

        while (ii < length1){
            listMesin[k] = left[ii];
            ii++;
            k++;
        }

        while (jj < length2){
            listMesin[k] = right[jj];
            jj++;
            k++;
        }
    }
}

//Class Mesin sebagai Node dalam linked list
class Mesin{
    //Atribute pada node mesin
    public int nomorIdentitas;
    public AVLTree tree;  //Implementasi score menggunakan AVL Tree
    public Mesin next;
    public Mesin prev;

    //Constructor
    public Mesin(int nomorIdentitas){
        this.nomorIdentitas = nomorIdentitas;
        tree = new AVLTree();
    }

    //Memasukkan score ke mesin
    void insertScore(int score){
        tree.root = tree.insertNode(tree.root, score);
    }
}

//Class Node sebagai node dalam AVL Tree
class Node {
    //Atribute height untuk balancing
    //Atribute size sebagai jumlah node dalam suatu subtree
    //Atribute sama sebagai jumlah orang yang memiliki score yang sama
    //Atribute sum sebagai jumlah score yang dimiliki pada suatu subtree
    int score, height, size, sama;
    long sum;
    Node left, right;

    //Constructor
    Node(int score) {
        this.score = score;
        this.height = 1;
        this.size = 1;
        this.sama = 1;
        this.sum = (long) score;
    }

    //Update size node
    void update(){
        int leftTemp;
        int rightTemp;

        if (this.left == null){
            leftTemp = 0;
        }else{
            leftTemp = this.left.size;
        }

        if (this.right == null){
            rightTemp = 0;
        }else{
            rightTemp = this.right.size;
        }

        this.size = this.sama + leftTemp + rightTemp;
    }

    //Update sum node
    void updateSum(){
        long leftTemp;
        long rightTemp;

        if (this.left == null){
            leftTemp = 0;
        }else{
            leftTemp = this.left.sum;
        }

        if (this.right == null){
            rightTemp = 0;
        }else{
            rightTemp = this.right.sum;
        }

        this.sum = ((long) this.sama * this.score) + leftTemp + rightTemp;
    }
}

//AVL Tree untuk implement score pada setiap mesin
//Source: Lab 5 SDA
class AVLTree {
    Node root;  //root tree

    //Right rotate untuk balancing
    Node rightRotate(Node node) {
        //Merotate node
        Node temp = node.left;
        Node temp2 = temp.right;

        temp.right = node;
        node.left = temp2;

        //Melakukan balancing, update size, dan update sum pada node yang dirotate
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        temp.height = Math.max(getHeight(temp.left), getHeight(temp.right)) + 1;

        node.update();
        temp.update();

        node.updateSum();
        temp.updateSum();

        return temp;
    }

    //left rotate untuk balancing
    Node leftRotate(Node node) {
        //Merotate node
        Node temp = node.right;
        Node temp2 = temp.left;

        temp.left = node;
        node.right = temp2;

        //Melakukan balancing, update size, dan update sum pada node yang dirotate
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        temp.height = Math.max(getHeight(temp.left), getHeight(temp.right)) + 1;
        
        node.update();
        temp.update();

        node.updateSum();
        temp.updateSum();

        return temp;
    }

    //Menginsert node ke dalam AVL Tree
    Node insertNode(Node node, int score) {
        //Jika node null, membuat node baru
        if (node == null){
            node = new Node(score);
            return node;
        }

        if (score < node.score){  //Jika score < score root subtree, rekurifkan ke subtree anak kiri
            node.left = insertNode(node.left, score);
        }else if (score > node.score){ //Jika score > score root subtree, rekurifkan ke subtree anak kanan
            node.right = insertNode(node.right, score);
        }else{  //Jika score = score root subtree, tambahkan atribute sama pada node tersebut
            node.sama++;
        }

        //Balancing dan melakukan update size & sum pada node
        int maxHeight = Math.max(getHeight(node.left), getHeight(node.right));
        node.height = maxHeight + 1;
        node.update();
        node.updateSum();

        if (getBalance(node) > 1){
            if (score < node.left.score){
                return rightRotate(node);
            }else if (score > node.left.score){
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }else if (getBalance(node) < -1){
            if (score > node.right.score){
                return leftRotate(node);
            }else if (score < node.right.score){
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }

        return node;
    }

    Node deleteNode(Node node, int score) {
        if (score < node.score){  //Jika score < score root subtree, rekurifkan ke subtree anak kiri
            node.left = deleteNode(node.left, score);
        }else if (score > node.score){ //Jika score > score root subtree, rekurifkan ke subtree anak kanan
            node.right = deleteNode(node.right, score);
        }else if (node.left != null && node.right != null){ //Penghapusan terjadi dan root subtree memilik 2 anak
            //Menggunakan prinsip predecessors inorder
            Node temp = findMax(node.left);
            node.score = temp.score;
            node.left = deleteNode(node.left, temp.score);
        }else{  //Penghapusan terjadi, tetapi root subtree memiliki 1 anak atau tidak memiliki anak
            node = (node.left != null) ? node.left : node.right;
        }

        //Tidak melakukan balancing ketika node == null
        if (node == null){
            return node;
        }

        //Balancing dan melakukan update size & sum pada node
        int maxHeight = Math.max(getHeight(node.left), getHeight(node.right));
        node.height = maxHeight + 1;
        node.update();
        node.updateSum();

        if (getBalance(node) > 1){
            if (getBalance(node.left) >= 0){
                return rightRotate(node);
            }else {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }else if (getBalance(node) < -1){
            if (getBalance(node.right) <= 0){
                return leftRotate(node);
            }else {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }

        return node;
    }

    //Mencari node dengan nilai score terbesar
    Node findMax(Node node){
        while (node.right != null){
            node = node.right;
        }

        return node;
    }

    // Utility function to get height of node
    int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Utility function to get balance factor of node
    int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    //Mencari urutan posisi suatu node berdasarkan scorenya
    int posisi(Node node, int score){
        if (node == null){
            return 0;
        }
        
        if (score == node.score){  //Jika score == score root subtree, ambil size anak kanan (jika ada) + 1 (node itu sendiri)
            if (node.right != null){
                return node.right.size + 1;
            }else{
                return 1;
            }
        }else if (score < node.score){
            //Jika score < score root subtree, rekursifkan ke anak kiri dan tambahkan jumlah score sama pada root tersebut beserta size anak kanannya (jika ada)
            if (node.right != null){
                return node.right.size + node.sama + posisi(node.left, score);
            }else{
                return node.sama + posisi(node.left, score);
            }
        }else{  //Jika score > score root subtree, langsug rekursifkan ke anak kanan
            return posisi(node.right, score);
        }
    }
}

//Source: https://usaco.guide/general/fast-io?lang=java#even-faster-methods
class FastIO extends PrintWriter {
	private InputStream stream;
	private byte[] buf = new byte[1 << 16];
	private int curChar;
	private int numChars;

	// standard input
	public FastIO() { this(System.in, System.out); }

	public FastIO(InputStream i, OutputStream o) {
		super(o);
		stream = i;
	}

	// file input
	public FastIO(String i, String o) throws IOException {
		super(new FileWriter(o));
		stream = new FileInputStream(i);
	}

	// throws InputMismatchException() if previously detected end of file
	private int nextByte() {
		if (numChars == -1) {
			throw new InputMismatchException();
		}
		if (curChar >= numChars) {
			curChar = 0;
			try {
				numChars = stream.read(buf);
			} catch (IOException e) {
				throw new InputMismatchException();
			}
			if (numChars == -1) {
				return -1;  // end of file
			}
		}
		return buf[curChar++];
	}

	// to read in entire lines, replace c <= ' '
	// with a function that checks whether c is a line break
	public String next() {
		int c;
		do {
			c = nextByte();
		} while (c <= ' ');

		StringBuilder res = new StringBuilder();
		do {
			res.appendCodePoint(c);
			c = nextByte();
		} while (c > ' ');
		return res.toString();
	}

	public int nextInt() {  // nextLong() would be implemented similarly
		int c;
		do {
			c = nextByte();
		} while (c <= ' ');

		int sgn = 1;
		if (c == '-') {
			sgn = -1;
			c = nextByte();
		}

		int res = 0;
		do {
			if (c < '0' || c > '9') {
				throw new InputMismatchException();
			}
			res = 10 * res + c - '0';
			c = nextByte();
		} while (c > ' ');
		return res * sgn;
	}

	public double nextDouble() { return Double.parseDouble(next()); }
}