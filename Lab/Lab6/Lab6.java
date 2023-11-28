//Referensi: https://stackoverflow.com/questions/15319561/how-to-implement-a-median-heap
//Mendapatkan ide dari Bonaventura Galang untuk query UBAH

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO: Lengkapi class ini
public class Lab6 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);
        Heap heap = new Heap();
        int id = 0;

        int N = in.nextInt();
        id += N;

        // TODO
        for (int i = 1; i <= N; i++) {
            int harga = in.nextInt();
            Saham saham = new Saham(i, harga);
            heap.add(saham);
        }
        int Q = in.nextInt();
        
        // TODO
        for (int i = 0; i < Q; i++) {
            String q = in.next();

            if (q.equals("TAMBAH")) {
                int harga = in.nextInt();
                Saham saham = new Saham(++id, harga);
                heap.add(saham);
            } else if (q.equals("UBAH")) {
                int nomorSeri = in.nextInt();
                int harga = in.nextInt();
                
                int indeks;
                
                if (heap.listSahamDecending.get(nomorSeri - 1) != -1){
                    indeks = heap.listSahamDecending.get(nomorSeri - 1);
                    Saham saham = heap.heapSahamDecending.get(indeks);
                    
                    if (indeks == (heap.heapSahamDecending.size() - 1)){
                        heap.listSahamDecending.set(heap.heapSahamDecending.get(heap.heapSahamDecending.size() - 1).id - 1, indeks);
                        heap.heapSahamDecending.remove(heap.heapSahamDecending.size() - 1);
                        heap.listSahamDecending.set(saham.id - 1, -1);
                    }else{
                        heap.heapSahamDecending.set(indeks, heap.heapSahamDecending.get(heap.heapSahamDecending.size() - 1));
                        heap.listSahamDecending.set(heap.heapSahamDecending.get(heap.heapSahamDecending.size() - 1).id - 1, indeks);
                        heap.heapSahamDecending.remove(heap.heapSahamDecending.size() - 1);
                        heap.listSahamDecending.set(saham.id - 1, -1);

                        if (heap.heapSahamDecending.get(heap.parentOf(indeks)).harga <= heap.heapSahamDecending.get(indeks).harga){
                            if (heap.heapSahamDecending.get(heap.parentOf(indeks)).harga == heap.heapSahamDecending.get(indeks).harga){
                                if (heap.heapSahamDecending.get(heap.parentOf(indeks)).id < heap.heapSahamDecending.get(indeks).id){
                                    heap.percolateUpDecending(indeks);
                                }else{
                                    heap.percolateDownDecending(indeks);
                                }
                            }else{
                                heap.percolateUpDecending(indeks);
                            }
                        }else{
                            heap.percolateDownDecending(indeks);
                        }
                    }
                    
                    saham.harga = harga;
                    heap.add(saham);
                }else{
                    indeks = heap.listSahamAscending.get(nomorSeri - 1);
                    Saham saham = heap.heapSahamAscending.get(indeks);
                    
                    if (indeks == (heap.heapSahamAscending.size() - 1)){
                        heap.listSahamAscending.set(heap.heapSahamAscending.get(heap.heapSahamAscending.size() - 1).id - 1, indeks);
                        heap.heapSahamAscending.remove(heap.heapSahamAscending.size() - 1);
                        heap.listSahamAscending.set(saham.id - 1, -1);
                    }else{
                        heap.heapSahamAscending.set(indeks, heap.heapSahamAscending.get(heap.heapSahamAscending.size() - 1));
                        heap.listSahamAscending.set(heap.heapSahamAscending.get(heap.heapSahamAscending.size() - 1).id - 1, indeks);
                        heap.heapSahamAscending.remove(heap.heapSahamAscending.size() - 1);
                        heap.listSahamAscending.set(saham.id - 1, -1);
                        
                        if (heap.heapSahamAscending.get(heap.parentOf(indeks)).harga >= heap.heapSahamAscending.get(indeks).harga){
                            if (heap.heapSahamAscending.get(heap.parentOf(indeks)).harga == heap.heapSahamAscending.get(indeks).harga){
                                if (heap.heapSahamAscending.get(heap.parentOf(indeks)).id < heap.heapSahamAscending.get(indeks).id){
                                    heap.percolateDownAscending(indeks);
                                }else{
                                    heap.percolateUpAscending(indeks);
                                }
                            }else{
                                heap.percolateUpAscending(indeks);
                            }
                        }else{
                            heap.percolateDownAscending(indeks);
                        }
                    }
                    
                    saham.harga = harga;
                    heap.add(saham);
                }
                System.out.println("yeay");
                System.out.println(heap.heapSahamDecending);
                System.out.println(heap.heapSahamAscending);
                System.out.println(heap.listSahamDecending);
                System.out.println(heap.listSahamAscending);
                System.out.println();
            }

            int totalSize = heap.heapSahamAscending.size() + heap.heapSahamDecending.size();
        
            if (totalSize % 2 == 0){
                out.println(heap.heapSahamAscending.get(0).id);
            }else{
                int indeks = (totalSize/2) + 1;
                if (heap.heapSahamDecending.size() == indeks){
                    out.println(heap.heapSahamDecending.get(0).id);
                }else{
                    out.println(heap.heapSahamAscending.get(0).id);
                }
            }
        }
        // out.println();
        // out.println(heap.heapSahamDecending);
        // out.println();
        // out.println(heap.heapSahamAscending);
        out.flush();
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

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}

class Saham {
    public int id;
    public int harga;

    public Saham(int id, int harga) {
        this.id = id;
        this.harga = harga;
    }

    @Override
    public String toString(){
        return "harga: " + this.harga + " id: " + this.id + " | ";
    }
}

// TODO: Lengkapi class ini
class Heap {
    public ArrayList<Saham> heapSahamAscending; //Heap Kanan
    public ArrayList<Saham> heapSahamDecending; //Heap Kiri
    public ArrayList<Integer> listSahamAscending;
    public ArrayList<Integer> listSahamDecending;
    // public int counter;

    public Heap() {
        heapSahamAscending = new ArrayList<Saham>();
        heapSahamDecending = new ArrayList<Saham>();
        listSahamAscending = new ArrayList<Integer>();
        listSahamDecending = new ArrayList<Integer>();
    }
    
    public void add(Saham saham){
        if (heapSahamDecending.size() == 0){
            // counter++;
            heapSahamDecending.add(saham);
            // listSaham.add(counter++);
            listSahamDecending.add(heapSahamDecending.size() - 1);
            listSahamAscending.add(-1);
        }else{
            if (saham.harga < heapSahamDecending.get(0).harga){
                heapSahamDecending.add(saham);
                if (listSahamDecending.size() >= saham.id){
                    listSahamDecending.set(saham.id - 1, heapSahamDecending.size() - 1);
                    listSahamAscending.set(saham.id - 1, -1);
                }else{
                    listSahamDecending.add(heapSahamDecending.size() - 1);
                    listSahamAscending.add(-1);
                }
                percolateUpDecending(heapSahamDecending.size() - 1);
            }else if (saham.harga == heapSahamDecending.get(0).harga){
                if (saham.id < heapSahamDecending.get(0).id){
                    heapSahamDecending.add(saham);
                    if (listSahamDecending.size() > saham.id){
                        listSahamDecending.set(saham.id - 1, heapSahamDecending.size() - 1);
                        listSahamAscending.set(saham.id - 1, -1);
                    }else{
                        listSahamDecending.add(heapSahamDecending.size() - 1);
                        listSahamAscending.add(-1);
                    }
                    percolateUpDecending(heapSahamDecending.size() - 1);
                }else{
                    heapSahamAscending.add(saham);
                    if (listSahamAscending.size() > saham.id){
                        listSahamAscending.set(saham.id - 1, heapSahamAscending.size() - 1);
                        listSahamDecending.set(saham.id - 1, -1);
                    }else{
                        listSahamAscending.add(heapSahamAscending.size() - 1);
                        listSahamDecending.add(-1);
                    }
                    percolateUpAscending(heapSahamAscending.size() - 1);
                }
            }else{
                heapSahamAscending.add(saham);
                if (listSahamAscending.size() > saham.id){
                    listSahamAscending.set(saham.id - 1, heapSahamAscending.size() - 1);
                    listSahamDecending.set(saham.id - 1, -1);
                }else{
                    listSahamAscending.add(heapSahamAscending.size() - 1);
                    listSahamDecending.add(-1);
                }

                percolateUpAscending(heapSahamAscending.size() - 1);
            }
            balancing();
        }
    }

    private void balancing(){
        if (Math.abs(heapSahamDecending.size() - heapSahamAscending.size()) > 1){
            if (heapSahamDecending.size() > heapSahamAscending.size()){
                Saham temp = heapSahamDecending.get(0);
                heapSahamDecending.set(0, heapSahamDecending.get(heapSahamDecending.size()-1));
                // listSaham.set(heapSahamDecending.get(heapSahamDecending.size()-1).id - 1, 0);
                listSahamDecending.set(heapSahamDecending.get(heapSahamDecending.size()-1).id - 1, 0);
                heapSahamDecending.remove(heapSahamDecending.size() -1);
                listSahamDecending.set(temp.id - 1, -1);
                // listSahamDecending
                
                if (heapSahamDecending.size() > 1){
                    percolateDownDecending(0);
                }
                heapSahamAscending.add(temp);
                listSahamAscending.set(temp.id - 1, heapSahamAscending.size()-1);
                percolateUpAscending(heapSahamAscending.size()-1);
            }else{
                Saham temp = heapSahamAscending.get(0);
                heapSahamAscending.set(0, heapSahamAscending.get(heapSahamAscending.size()-1));
                listSahamAscending.set(heapSahamAscending.get(heapSahamAscending.size()-1).id - 1, 0);
                heapSahamAscending.remove(heapSahamAscending.size() -1);
                listSahamAscending.set(temp.id - 1, -1);
                
                if (heapSahamAscending.size() > 1){
                    percolateDownAscending(0);
                }
                
                heapSahamDecending.add(temp);
                listSahamDecending.set(temp.id - 1, heapSahamDecending.size() - 1);
                percolateUpDecending(heapSahamDecending.size()-1);
            }
        }
    }

    public void percolateUpAscending(int leaf){
        int parent = parentOf(leaf);
        Saham value = heapSahamAscending.get(leaf);
        
        while (leaf > 0 && (value.harga <= heapSahamAscending.get(parent).harga)){
            if (value.harga == heapSahamAscending.get(parent).harga){
                if (value.id < heapSahamAscending.get(parent).id){
                    heapSahamAscending.set(leaf, heapSahamAscending.get(parent));
                    // listSaham.set(heapSahamAscending.get(parent).id - 1, leaf);
                    listSahamAscending.set(heapSahamAscending.get(parent).id - 1, leaf);
                    leaf = parent;
                    parent = parentOf(leaf);
                }else{
                    break;
                }
            }else{
                heapSahamAscending.set(leaf, heapSahamAscending.get(parent));
                listSahamAscending.set(heapSahamAscending.get(parent).id - 1, leaf);
                leaf = parent;
                parent = parentOf(leaf);
            }
        }
        heapSahamAscending.set(leaf, value);
        // listSaham.set(value.id - 1, leaf);
        listSahamAscending.set(value.id - 1, leaf);
    }
    
    public void percolateUpDecending(int leaf){
        int parent = parentOf(leaf);
        Saham value = heapSahamDecending.get(leaf);
        
        while (leaf > 0 && (value.harga >= heapSahamDecending.get(parent).harga)){
            
            if (value.harga == heapSahamDecending.get(parent).harga){
                if (value.id > heapSahamDecending.get(parent).id){
                    heapSahamDecending.set(leaf, heapSahamDecending.get(parent));
                    listSahamDecending.set(heapSahamDecending.get(parent).id - 1, leaf);
                    leaf = parent;
                    parent = parentOf(leaf);
                }else{
                    break;
                }
            }else{
                heapSahamDecending.set(leaf, heapSahamDecending.get(parent));
                listSahamDecending.set(heapSahamDecending.get(parent).id - 1, leaf);
                leaf = parent;
                parent = parentOf(leaf);
            }
        }
        heapSahamDecending.set(leaf, value);
        listSahamDecending.set(value.id - 1, leaf);
    }
    
    public int parentOf(int indeks){
        return (indeks-1)/2;
    }
        
    public void percolateDownAscending(int indeks){
        int heapSize = heapSahamAscending.size();
        Saham temp = heapSahamAscending.get(indeks);
        
        while (indeks < heapSize){
            int childPos = leafChildOf(indeks);
            if (childPos < heapSize){
                if ((rightChildOf(indeks) < heapSize) && (heapSahamAscending.get(childPos + 1).harga <= heapSahamAscending.get(childPos).harga)){
                    if (heapSahamAscending.get(childPos + 1).harga == heapSahamAscending.get(childPos).harga){
                        if (heapSahamAscending.get(childPos + 1).id < heapSahamAscending.get(childPos).id){
                            childPos++;
                        }
                    }else{
                        childPos++;
                    }
                }
                
                if (heapSahamAscending.get(childPos).harga <= temp.harga){
                    if (heapSahamAscending.get(childPos).harga == temp.harga){
                        if (heapSahamAscending.get(childPos).id < temp.id){
                            heapSahamAscending.set(indeks, heapSahamAscending.get(childPos));
                            listSahamAscending.set(heapSahamAscending.get(childPos).id - 1, indeks);
                            indeks = childPos;
                        }else{
                            heapSahamAscending.set(indeks, temp);
                            listSahamAscending.set(temp.id - 1, indeks);
                            break;
                        }
                    }else{
                        listSahamAscending.set(heapSahamAscending.get(childPos).id - 1, indeks);
                        heapSahamAscending.set(indeks, heapSahamAscending.get(childPos));
                        indeks = childPos;
                    }
                }else{
                    listSahamAscending.set(temp.id - 1, indeks);
                    heapSahamAscending.set(indeks, temp);
                    break;
                }
            }else{
                listSahamAscending.set(temp.id - 1, indeks);
                heapSahamAscending.set(indeks, temp);
                break;
            }
        }
    }
    
    public void percolateDownDecending(int indeks){
        int heapSize = heapSahamDecending.size();
        Saham temp = heapSahamDecending.get(indeks);
        
        while (indeks < heapSize){
            int childPos = leafChildOf(indeks);
            if (childPos < heapSize){
                if ((rightChildOf(indeks) < heapSize) && (heapSahamDecending.get(childPos + 1).harga >= heapSahamDecending.get(childPos).harga)){
                    if (heapSahamDecending.get(childPos + 1).harga == heapSahamDecending.get(childPos).harga){
                        if (heapSahamDecending.get(childPos + 1).id > heapSahamDecending.get(childPos).id){
                            childPos++;
                        }
                    }else{
                        childPos++;
                    }
                }

                if (heapSahamDecending.get(childPos).harga >= temp.harga){
                    if (heapSahamDecending.get(childPos).harga == temp.harga){
                        if (heapSahamDecending.get(childPos).id > temp.id){
                            // listSaham.set(heapSahamDecending.get(childPos).id - 1, indeks);
                            listSahamDecending.set(heapSahamDecending.get(childPos).id - 1, indeks);
                            heapSahamDecending.set(indeks, heapSahamDecending.get(childPos));
                            indeks = childPos;
                        }else{
                            // listSaham.set(temp.id - 1, indeks);
                            listSahamDecending.set(temp.id - 1, indeks);
                            heapSahamDecending.set(indeks, temp);
                            break;
                        }
                    }else{
                        // listSaham.set(heapSahamDecending.get(childPos).id - 1, indeks);
                        listSahamDecending.set(heapSahamDecending.get(childPos).id - 1, indeks);
                        heapSahamDecending.set(indeks, heapSahamDecending.get(childPos));
                        indeks = childPos;
                    }
                }else{
                    listSahamDecending.set(temp.id - 1, indeks);
                    heapSahamDecending.set(indeks, temp);
                    break;
                }
            }else{
                listSahamDecending.set(temp.id - 1, indeks);
                heapSahamDecending.set(indeks, temp);
                break;
            }
        }
    }

    private int leafChildOf(int indeks){
        return 2 * indeks + 1;
    }

    private int rightChildOf(int indeks){
        return 2 * (indeks + 1);
    }
}
