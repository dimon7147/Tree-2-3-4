import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[]args){
        Tree234<Integer> tree = new Tree234<>();
        Scanner scanner;
        try {
            scanner = new Scanner(new File("test.txt"));
            while (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                tree.insert(num);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(tree);
    }
}