//Mendapatkan ide dari Bonaventura Galang

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class Lab7 {
    private static InputReader in;
    private static PrintWriter out;
    private static ArrayList<ArrayList<Edge>> adjacencyList;
    private static int[] bentengDiserang;
    private static long[] minimal;
    private static int N;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        N = in.nextInt();
        int M = in.nextInt();

        adjacencyList = new ArrayList<ArrayList<Edge>>(N);
        bentengDiserang = new int[M];
        minimal = new long[N];
        for (int i = 1; i <= N; i++) {
            adjacencyList.add(new ArrayList<Edge>());
        }

        for (int i = 0; i < M; i++) {
            int F = in.nextInt();
            bentengDiserang[i] = F;
        }

        int E = in.nextInt();
        for (int i = 0; i < E; i++) {
            int A = in.nextInt(), B = in.nextInt(), W = in.nextInt();
            adjacencyList.get(B-1).add(new Edge(B, A, W));
        }

        // out.println(adjacencyList);
        // out.println();
        // out.println(Arrays.toString(bentengDiserang));

        for (int i = 0; i < bentengDiserang.length; i++){
            long[] temp = dijkstras(bentengDiserang[i]);
            System.out.println(Arrays.toString(temp));
            
            if (i != 0){
                for (int j = 0; j < temp.length; j++){
                    if (temp[j] < minimal[j]){
                        minimal[j] = temp[j];
                    }
                }
            }else{
                for (int j = 0; j < temp.length; j++){
                    minimal[j] = temp[j];
                }
            }
        }

        // out.println(minimal);
        // System.out.println(Arrays.toString(minimal));

        int Q = in.nextInt();
        while (Q-- > 0) {
            int S = in.nextInt(), K = in.nextInt();
            // TODO: Implementasi query
            long banyakMusuh = minimal[S-1];

            if (banyakMusuh < K){
                out.println("YES");
            }else{
                out.println("NO");
            }
        }

        out.close();
    }

    //Referensi: https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-in-java-using-priorityqueue/
    public static long[] dijkstras(int source){
        long[] output = new long[N];
        ArrayList<Integer> hijau = new ArrayList<Integer>();
        PriorityQueue<AbuAbu> abuAbu = new PriorityQueue<AbuAbu>();

        for (int i = 0; i < N; i++){
            output[i] = Integer.MAX_VALUE;
        }

        // abuAbu.add(source);
        abuAbu.add(new AbuAbu(source, 0));
        output[source-1] = 0;

        while (hijau.size() != N){
            if (abuAbu.isEmpty()){
                break;
            }

            while (!abuAbu.isEmpty()){
                AbuAbu temp = abuAbu.remove();
                
                if (hijau.contains(temp.indeks)){
                    continue;
                }else{
                    long currentDistance = -1;
                    long newDistance = -1;
                    for (int i = 0; i < adjacencyList.get(temp.indeks - 1).size(); i++){
                        Edge edge = adjacencyList.get(temp.indeks - 1).get(i);
                        currentDistance = edge.weight;
                        newDistance = output[temp.indeks - 1] + currentDistance;

                        if (newDistance < output[edge.to - 1]){
                            output[edge.to - 1] = newDistance;
                            abuAbu.add(new AbuAbu(edge.to, newDistance));
                        }
        
                    }
                }
                
                hijau.add(temp.indeks);
            }
        }

        return output;
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

    }
}

class Edge{
    public int from;
    public int to;
    public int weight;

    public Edge(int from, int to, int weight){
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String toString(){
        return "vertex: " + from + " to: " + to;
    }
}

class AbuAbu implements Comparable<AbuAbu>{
    int indeks;
    long totalWeight;

    public AbuAbu(int indeks, long totalWeight){
        this.indeks = indeks;
        this.totalWeight = totalWeight;
    }

    @Override
    public int compareTo(AbuAbu o){
        if (this.totalWeight > o.totalWeight){
            return 1;
        }else{
            return -1;
        }
    }
}