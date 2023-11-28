//Berkolaborasi dengan Bonaventura Galang

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

public class Lab5 {

    private static InputReader in;
    static PrintWriter out;
    static AVLTree tree = new AVLTree();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int numOfInitialPlayers = in.nextInt();
        for (int i = 0; i < numOfInitialPlayers; i++) {
            // TODO: process inputs
            String key = in.next();
            String value = in.next();
            tree.root = tree.insertNode(tree.root, Integer.parseInt(value), key);
        }

        int numOfQueries = in.nextInt();
        for (int i = 0; i < numOfQueries; i++) {
            String cmd = in.next();
            if (cmd.equals("MASUK")) {
                String key = in.next();
                String value = in.next();
                handleQueryMasuk(key, Integer.parseInt(value));
            } else {
                String K = in.next();
                String B = in.next();
                handleQueryDuo(Integer.parseInt(K), Integer.parseInt(B));
            }
        }

        // tree.root = tree.insertNode(tree.root, 10, "A");
        // tree.root = tree.insertNode(tree.root, 85, "B");
        // tree.root = tree.insertNode(tree.root, 15, "C");
        // tree.root = tree.insertNode(tree.root, 70, "D");
        // tree.root = tree.insertNode(tree.root, 20, "E");
        // tree.root = tree.insertNode(tree.root, 60, "F");
        // tree.root = tree.insertNode(tree.root, 30, "G");
        // tree.root = tree.insertNode(tree.root, 50, "H");
        // tree.root = tree.insertNode(tree.root, 65, "I");
        // tree.root = tree.insertNode(tree.root, 80, "J");
        // tree.root = tree.insertNode(tree.root, 90, "K");
        // tree.root = tree.insertNode(tree.root, 40, "L");
        // tree.root = tree.insertNode(tree.root, 5, "M");
        // tree.root = tree.insertNode(tree.root, 55,"N");
        // tree.printTree(tree.root, "", true);

        out.close();
    }

    static void handleQueryMasuk(String nama, int value) {
        // TODO
        tree.root = tree.insertNode(tree.root, value, nama);
        out.println(tree.countLess(tree.root, value));
    }

    static void handleQueryDuo(int K, int B) {
        // TODO
        Node lower = tree.lowerBound(tree.root, K);
        Node upper = tree.upperBound(tree.root, B);
        // System.out.println("lower: " + lower.key);
        // System.out.println("upper: " + upper.key);

        if (lower == null || upper == null){
            out.println(-1 + " " + -1);
        } else if (lower.key > upper.key){
            out.println(-1 + " " + -1);
        }else if (lower == upper){
            if (lower.namaSama.size() > 1){
                String playerNameLower = lower.namaSama.removeLast();
                String playerNameUpper =  upper.namaSama.removeLast();

                if (lower.namaSama.size() == 0){
                    tree.root = tree.deleteNode(tree.root, lower.key);
                }

                if (playerNameLower.compareTo(playerNameUpper) > 0){
                    out.println(playerNameUpper + " " + playerNameLower);
                }else{
                    out.println(playerNameLower + " " + playerNameUpper);
                }
            }else{
                out.println(-1 + " " + -1);
            }
        }else{
            String playerNameLower = lower.namaSama.removeLast();
            if (lower.namaSama.size() == 0){
                tree.root = tree.deleteNode(tree.root, lower.key);
            }

            String playerNameUpper = upper.namaSama.removeLast();
            if (upper.namaSama.size() == 0){
                tree.root = tree.deleteNode(tree.root, upper.key);
            }

            if (playerNameLower.compareTo(playerNameUpper) > 0){
                out.println(playerNameUpper + " " + playerNameLower);
            }else{
                out.println(playerNameLower + " " + playerNameUpper);
            }
        }
    }

    // taken from https://codeforces.com/submissions/Petr
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


// TODO: modify as needed
class Node {
    int key, height,size;
    Node left, right;
    Deque<String> namaSama = new ArrayDeque<String>();

    Node(int key) {
        this.key = key;
        this.height = 1;
        this.size = 1;
    }

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

        this.size = this.namaSama.size() + leftTemp + rightTemp;
    }
}


class AVLTree {

    Node root;

    Node rightRotate(Node node) {
        // TODO: implement right rotate
        Node temp = node.left;
        Node temp2 = temp.right;

        temp.right = node;
        node.left = temp2;

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        temp.height = Math.max(getHeight(temp.left), getHeight(temp.right)) + 1;

        node.update();
        temp.update();

        return temp;
    }

    Node leftRotate(Node node) {
        // TODO: implement left rotate
        Node temp = node.right;
        Node temp2 = temp.left;

        temp.left = node;
        node.right = temp2;

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        temp.height = Math.max(getHeight(temp.left), getHeight(temp.right)) + 1;
        
        node.update();
        temp.update();

        return temp;
    }

    Node insertNode(Node node, int key, String playerName) {
        // TODO: implement insert node
        if (node == null){
            node = new Node(key);
            node.namaSama.addLast(playerName);
            return node;
        }

        if (key < node.key){
            node.left = insertNode(node.left, key, playerName);
        }else if (key > node.key){
            node.right = insertNode(node.right, key, playerName);
        }else{
            node.namaSama.addLast(playerName);
        }

        int maxHeight = Math.max(getHeight(node.left), getHeight(node.right));
        node.height = maxHeight + 1;
        node.update();

        if (getBalance(node) > 1){
            if (key < node.left.key){
                return rightRotate(node);
            }else if (key > node.left.key){
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }else if (getBalance(node) < -1){
            if (key > node.right.key){
                return leftRotate(node);
            }else if (key < node.right.key){
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }

        return node;
    }

    Node deleteNode(Node node, int key) {
        // TODO: implement delete node
        if (key < node.key){
            node.left = deleteNode(node.left, key);
        }else if (key > node.key){
            node.right = deleteNode(node.right, key);
        }else if (node.left != null && node.right != null){
            Node temp = findMax(node.left);
            node.key = temp.key;
            node.namaSama = temp.namaSama;
            node.left = deleteNode(node.left, temp.key);
        }else{
            node = (node.left != null) ? node.left : node.right;
        }

        if (node == null){
            return node;
        }

        int maxHeight = Math.max(getHeight(node.left), getHeight(node.right));
        node.height = maxHeight + 1;
        node.update();

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

    Node findMax(Node node){
        while (node.right != null){
            node = node.right;
        }
        // System.out.println(node.key + " dvs " + node.namaSama);

        return node;
    }

    Node removeMax(Node node){
        if (node.right != null){
            node.right = removeMax(node.right);
            return node;
        }else{
            return node.left;
        }
    }

    Node lowerBound(Node node, int value) {
        // TODO: return node with the lowest key that is >= value
        if (node == null){
            return null;
        }
        
        if (value == node.key){
            return node;
        }else if (value < node.key){
            Node temp = lowerBound(node.left, value);
            if (temp != null){
                return temp;
            }else{
                return node;
            }
        }else{
            return lowerBound(node.right, value);
        }
    }

    Node upperBound(Node node, int value) {
        // TODO: return node with the greatest key that is <= value
        if (node == null){
            return null;
        }
        
        if (value == node.key){
            return node;
        }else if (value > node.key){
            Node temp = upperBound(node.right, value);
            if (temp != null){
                return temp;
            }else{
                return node;
            }
        }else{
            return upperBound(node.left, value);
        }
    }

    int countLess(Node node, int value){
        if (node == null){
            return 0;
        }else if (value == node.key){
            if (node.left != null){
                return node.left.size;
            }else{
                return 0;
            }
        }else if (node.key > value){
            return countLess(node.left, value);
        }else{
            int jumlah = countNodes(node.left) + node.namaSama.size();
            jumlah += countLess(node.right, value);
            return jumlah;
        }
    }

    int countNodes(Node node){
        if (node == null){
            return 0;
        }
        node.update();
        return node.size;
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

    void printTree(Node currPtr, String indent, boolean last) {
        if (currPtr != null) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "   ";
            } else {
                System.out.print("L----");
                indent += "|  ";
            }
            System.out.println(currPtr.key + " " + currPtr.namaSama);
            printTree(currPtr.left, indent, false);
            printTree(currPtr.right, indent, true);
        }
    }
}