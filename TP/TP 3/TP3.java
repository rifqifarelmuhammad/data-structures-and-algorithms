// Nama: Rifqi Farel Muhammad
// NPM: 2106650310
// Kelas: SDA D
// Mendapatkan inspirasi (ide) dari Bonaventura Galang

//Mengimport module yang dibutuhkan
import java.io.*;
import java.util.*;

public class TP3 {
    private static FastIO io = new FastIO();
	private static ArrayList<ArrayList<Edge>> adjacencyList;
	private static int banyakPos;
	private static int[] lokasiKurcaci;

    public static void main(String[] args) {
        banyakPos = io.nextInt();
		int banyakTerowongan = io.nextInt();

		//Inisialisasi adjacency list
		adjacencyList = new ArrayList<ArrayList<Edge>>(banyakPos);
		for (int i = 0; i < banyakPos; i++){
			adjacencyList.add(new ArrayList<Edge>());
		}

		//Memasukkan edge ke adjacency list
		for (int i = 0; i < banyakTerowongan; i++){
			int from = io.nextInt(), to = io.nextInt(), waktu = io.nextInt(), ukuran = io.nextInt();
			adjacencyList.get(from-1).add(new Edge(from, to, ukuran, waktu));
			adjacencyList.get(to-1).add(new Edge(to, from, ukuran, waktu));
		}

		//Menyimpan lokasi kurcaci
		int banyakKurcaci = io.nextInt();
		lokasiKurcaci = new int[banyakKurcaci];
		for (int i = 0; i < banyakKurcaci; i++){
			lokasiKurcaci[i] = io.nextInt();
		}

		//Query
		int jumlahQuery = io.nextInt();
		for (int i = 0; i < jumlahQuery; i++){
			String query = io.next();
			if (query.equals("KABUR")){
				int F = io.nextInt();
				int E = io.nextInt();
				long[] output = dijkstrasKabur(F);
				io.println(output[E-1]);
			}else if (query.equals("SIMULASI")){
				//List pos peristirahatan
				int banyak = io.nextInt();
				int[] tempPos = new int[banyak];
				for (int j = 0; j < banyak; j++){
					tempPos[j] = io.nextInt();
				}

				long[] output = dijkstrasSimulasi(tempPos);

				//Mencari jumlah detik terkecil agar semua kurcaci dapat keluar dari sistem terowongan
				long max = output[lokasiKurcaci[0] - 1];
				if (lokasiKurcaci.length > 1){
					for (int j = 1; j < lokasiKurcaci.length; j++){
						if (max < output[lokasiKurcaci[j] - 1]){
							max = output[lokasiKurcaci[j] - 1];
						}
					}
				}

				io.println(max);
			}else{
				int v1 = io.nextInt();
				int v2 = io.nextInt();
				int v3 = io.nextInt();

				//Melakukan djikstra v1 ke v2
				long[][] firstDjikstra = dijkstrasSuper(v1);
				long valueFirst = firstDjikstra[v2 - 1][0];
				if (firstDjikstra[v2 - 1][1] < valueFirst){
					valueFirst = firstDjikstra[v2 - 1][1];
				}

				//Melakukan djikstra v2 ke v3
				long[][] secondDjikstra = dijkstrasSuper(v2);
				long valueSecond = secondDjikstra[v3 - 1][0];
				if (secondDjikstra[v3 - 1][1] < valueSecond){
					valueSecond = secondDjikstra[v3 - 1][1];
				}

				//Mencari jumlah detik terkecil
				long output = firstDjikstra[v2 - 1][0] + secondDjikstra[v3 - 1][1];
				if ((firstDjikstra[v2 - 1][1] + secondDjikstra[v3 - 1][0]) < output){
					output = firstDjikstra[v2 - 1][1] + secondDjikstra[v3 - 1][0];
				}

				io.println(output);
			}
		}

		io.close();
    }

	//Mencari ukuran terowongan terbesar dari setiap path yang ada menggunakan prinsip djikstra
	public static long[] dijkstrasKabur(int source){
		long[] output = new long[banyakPos];
		ArrayList<Boolean> hijau = new ArrayList<Boolean>(banyakPos);
		Heap abuAbu = new Heap(banyakPos);

		abuAbu.addMax(new AbuAbu(source, Integer.MAX_VALUE));
		
		for (int i = 0; i < banyakPos; i++){
			hijau.add(false);
		}

		while (!abuAbu.heap.isEmpty()){
			AbuAbu temp = abuAbu.removeMax();
			
			if (hijau.get(temp.pos - 1)){
				continue;
			}else{
				long newDistance = -1;
				
				for (int i = 0; i < adjacencyList.get(temp.pos - 1).size(); i++){
					Edge edge = adjacencyList.get(temp.pos - 1).get(i);
					newDistance = Math.min(temp.weight, edge.ukuran);
					
					if (newDistance > output[edge.to - 1]){
						output[edge.to - 1] = newDistance;
						abuAbu.addMax(new AbuAbu(edge.to, newDistance));
					}
				}
			}
			hijau.set(temp.pos-1, true);
		}
		output[source-1] = 0;

		return output;
	}

	//Mencari waktu terkecil dari setiap path yang ada menggunakan prinsip djikstra
	public static long[] dijkstrasSimulasi(int[] source){
		long[] output = new long[banyakPos];
		ArrayList<Boolean> hijau = new ArrayList<Boolean>(banyakPos);
		Heap abuAbu = new Heap(banyakPos);

		for (int i = 0; i < banyakPos; i++){
			output[i] = Integer.MAX_VALUE;
			hijau.add(false);
		}

		for (int i = 0; i < source.length; i++){
			abuAbu.addMin(new AbuAbu(source[i], 0));
			output[source[i] - 1] = 0;
		}

		while (!abuAbu.heap.isEmpty()){
			AbuAbu temp = abuAbu.removeMin();
			
			if (hijau.get(temp.pos - 1)){
				continue;
			}else{
				long currentDistance = -1;
				long newDistance = -1;
				
				for (int i = 0; i < adjacencyList.get(temp.pos - 1).size(); i++){
					Edge edge = adjacencyList.get(temp.pos - 1).get(i);
					currentDistance = edge.waktu;
					newDistance = output[temp.pos - 1] + currentDistance;
					
					if (newDistance < output[edge.to - 1]){
						output[edge.to - 1] = newDistance;
						abuAbu.addMin(new AbuAbu(edge.to, newDistance));
					}
				}
			}
			hijau.set(temp.pos-1, true);
		}

		return output;
	}

	//Mencari waktu terkecil dari setiap path yang ada menggunakan prinsip djikstra, tetapi menambahkan kemungkinan penggunaan power
	public static long[][] dijkstrasSuper(int source){
		long[][] output = new long[banyakPos][2];
		ArrayList<ArrayList<Boolean>> hijau = new ArrayList<ArrayList<Boolean>>();
		Heap abuAbu = new Heap(banyakPos);

		for (int i = 0; i < banyakPos; i++){
			hijau.add(new ArrayList<Boolean>());
			for (int j = 0; j < 2; j++){
				output[i][j] = Integer.MAX_VALUE;
				hijau.get(i).add(false);
			}
		}

		abuAbu.addMin(new AbuAbu(source, Integer.MAX_VALUE, false));
		output[source - 1][0] = 0;
		output[source - 1][1] = 0;

		while (!abuAbu.heap.isEmpty()){
			// System.out.println(Arrays.deepToString(output));
			AbuAbu temp = abuAbu.removeMin();
			int tempState;

			if (temp.power){
				tempState = 1;
			}else{
				tempState = 0;
			}
			
			if (hijau.get(temp.pos - 1).get(tempState)){
				continue;
			}else{
				long currentDistance = -1;
				long newDistance = -1;
				
				for (int i = 0; i < adjacencyList.get(temp.pos - 1).size(); i++){
					Edge edge = adjacencyList.get(temp.pos - 1).get(i);

					if (!temp.power){
						newDistance = output[temp.pos - 1][0];

						if (newDistance < output[edge.to - 1][1]){
							output[edge.to - 1][1] = newDistance;
							abuAbu.addMin(new AbuAbu(edge.to, newDistance, true));
						}
					}

					currentDistance = edge.waktu;
					newDistance = output[temp.pos - 1][tempState] + currentDistance;
					
					if (newDistance < output[edge.to - 1][tempState]){
						output[edge.to - 1][tempState] = newDistance;
						abuAbu.addMin(new AbuAbu(edge.to, newDistance, temp.power));
					}
				}
			}
			hijau.get(temp.pos - 1).set(tempState, true);
		}

		return output;
	}
}

//Class Edge
class Edge{
	int from;
    int to;
    long ukuran;
	long waktu;

    public Edge(int from, int to, long ukuran, long waktu){
        this.from = from;
        this.to = to;
        this.ukuran = ukuran;
		this.waktu = waktu;
    }
}

//Class untuk disimpan ke dalam binary heap
class AbuAbu implements Comparable<AbuAbu>{
	int pos;
	long weight;
	boolean power;

	public AbuAbu(int pos, long weight){
		this.pos = pos;
		this.weight = weight;
	}

	public AbuAbu(int pos, long weight, boolean power){
		this.pos = pos;
		this.weight = weight;
		this.power = power;
	}

	//Memprioritaskan weight yang lebih besar (bisa ukuran, bisa waktu)
	@Override
    public int compareTo(AbuAbu o){
        if (this.weight > o.weight){
            return 1;
        }else{
            return -1;
        }
    }
}

class Heap{
	public ArrayList<AbuAbu> heap;

	public Heap(int size){
		heap = new ArrayList<AbuAbu>(size);
	}

	public void addMax(AbuAbu abuAbu){  //max heap
		heap.add(abuAbu);

		if (heap.size() != 1){
			percolateUpMax(heap.size() - 1);  //Percolate up max heap
		}
	}

	public void addMin(AbuAbu abuAbu){  //min heap
		heap.add(abuAbu);

		if (heap.size() != 1){
			percolateUpMin(heap.size() - 1);  //Percolate up min heap
		}
	}

	public AbuAbu removeMax(){  //Mengambil nilai teratas di max heap
		AbuAbu output = heap.get(0);
		heap.set(0, heap.get(heap.size() - 1));  //Mengambil nilai terakhir di heap untuk dijadikan root
		heap.remove(heap.size() - 1);  //Menghapus nilai terakhir di heap (node yang sudah dipindahkan)
		if (heap.size() > 1){
			percolateDownMax(0);  //Percolate down max heap
		}
		return output;
	}

	public AbuAbu removeMin(){  //Mengambil nilai teratas di min heap
		AbuAbu output = heap.get(0);
		heap.set(0, heap.get(heap.size() - 1));  //Mengambil nilai terakhir di heap untuk dijadikan root
		heap.remove(heap.size() - 1);  //Menghapus nilai terakhir di heap (node yang sudah dipindahkan)
		if (heap.size() > 1){
			percolateDownMin(0);  //Percolate down min heap
		}
		return output;
	}

	public void percolateUpMax(int leaf){
		int parent = parentOf(leaf);
		AbuAbu value = heap.get(leaf);

		//Membandingkan leaf (node yang baru dimasukkan) dengan parentnya, jika lebih besar maka ditukar 
		while (leaf > 0 && value.compareTo(heap.get(parent)) > 0){
			heap.set(leaf, heap.get(parent));
			leaf = parent;
			parent = parentOf(leaf);
		}

		heap.set(leaf, value);
	}

	public void percolateUpMin(int leaf){
		int parent = parentOf(leaf);
		AbuAbu value = heap.get(leaf);

		//Membandingkan leaf (node yang baru dimasukkan) dengan parentnya, jika lebih kecil maka ditukar 
		while (leaf > 0 && value.compareTo(heap.get(parent)) < 0){
			heap.set(leaf, heap.get(parent));
			leaf = parent;
			parent = parentOf(leaf);
		}

		heap.set(leaf, value);
	}

	public void percolateDownMax(int indeks){
		int heapSize = heap.size();
		AbuAbu temp = heap.get(indeks);

		//Membandingkan parent dengan anak-anaknya, jika parent < anaknya maka dipindahkan
		while (indeks < heapSize){
			int childPos = leftChildOf(indeks);
			if (childPos < heapSize){
				//Mencari anak terbesar (anak kiri atau anak kanan)
				if ((rightChildOf(indeks) < heapSize) && (heap.get(childPos + 1).compareTo(heap.get(childPos)) > 0)){
					childPos++;
				}

				if (heap.get(childPos).compareTo(temp) > 0){
					heap.set(indeks, heap.get(childPos));
					indeks = childPos;
				}else{
					heap.set(indeks, temp);
					break;
				}
			}else{
				heap.set(indeks, temp);
				break;
			}
		}
	}

	public void percolateDownMin(int indeks){
		int heapSize = heap.size();
		AbuAbu temp = heap.get(indeks);

		//Membandingkan parent dengan anak-anaknya, jika parent > anaknya maka dipindahkan
		while (indeks < heapSize){
			int childPos = leftChildOf(indeks);
			if (childPos < heapSize){
				//Mencari anak terkecil (anak kiri atau anak kanan)
				if ((rightChildOf(indeks) < heapSize) && (heap.get(childPos + 1).compareTo(heap.get(childPos)) < 0)){
					childPos++;
				}

				if (heap.get(childPos).compareTo(temp) < 0){
					heap.set(indeks, heap.get(childPos));
					indeks = childPos;
				}else{
					heap.set(indeks, temp);
					break;
				}
			}else{
				heap.set(indeks, temp);
				break;
			}
		}
	}

	public int parentOf(int indeks){  //Mencari parent dari suatu node
        return (indeks-1)/2;
    }

	public int leftChildOf(int indeks){  //Mencari anak kiri dari suatu node
		return 2 * indeks + 1;
	}

	public int rightChildOf(int indeks){  //Mencari anak kanan dari suatu node
        return 2 * (indeks + 1);
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