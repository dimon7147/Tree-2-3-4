import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/***
 * Тесты под все требуемые к реализации задачи, а именно:
 * 1. Построение дерева из текстового файла
 * 2. Итератор для обхода дерева в отсортированном виде
 * 3. Возврат набора строк, которые больше заданной
 * 4. Возврат набора строк, которые меньше заданной
 * 5. Возврат набора строк, которые равны заданной
 * 6. Возврат набора строк, которые в диапозоне между заданными
 * 7. Возврат последней строки (Возвращается наибольший элемент)
 * 8. Возврат первой строки (Возвращается наименьший элемент)
 * Для работы тестов должен быть создан файл test.txt с элементами от 1 до 9 включительно, каждый с новой строки
 * */
class Tree234Test {
    /***
     * Задание 1: Разработать дерево. Дерево строится из текстового файла(набор строк, разделенных ‘\n’).
     * Задание 2: Разработать итератор по всем в отсортированном виде
     */
    @Test
    public void buildTree() {
        Tree234<String> tree = new Tree234<>();
        try {
            Scanner scanner = new Scanner(new File("test.txt"));
            while (scanner.hasNextLine()) {
                tree.insert(scanner.nextLine());
            }
            Iterator<String> iterator = new TreeIterator<>(tree).iterator();
            int i = 1;
            while (iterator.hasNext()) {
                Assertions.assertEquals(i + "", iterator.next());
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * Задача 3: Вернуть набор строк, которые больше заданной
     */
    @Test
    public void BiggerThat() {
        Tree234<String> tree = new Tree234<>();
        try {
            Scanner scanner = new Scanner(new File("test.txt"));
            while (scanner.hasNextLine()) {
                tree.insert(scanner.nextLine());
            }
            ArrayList<String> list;
            list = tree.getListOfElementsBiggerThat("2");
            Assertions.assertTrue(list.contains("3"));
            Assertions.assertTrue(list.contains("4"));
            Assertions.assertTrue(list.contains("5"));
            Assertions.assertTrue(list.contains("6"));
            Assertions.assertTrue(list.contains("7"));
            Assertions.assertTrue(list.contains("8"));
            Assertions.assertTrue(list.contains("9"));

            Assertions.assertFalse(list.contains("1"));
            Assertions.assertFalse(list.contains("2"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * Задача 4: Вернуть набор строк, которые меньше заданной
     */
    @Test
    public void SmallerThat() {
        Tree234<String> tree = new Tree234<>();
        try {
            Scanner scanner = new Scanner(new File("test.txt"));
            while (scanner.hasNextLine()) {
                tree.insert(scanner.nextLine());
            }
            ArrayList<String> list;
            list = tree.getListOfElementsSmallerThat("7");
            Assertions.assertTrue(list.contains("6"));
            Assertions.assertTrue(list.contains("5"));
            Assertions.assertTrue(list.contains("4"));
            Assertions.assertTrue(list.contains("3"));
            Assertions.assertTrue(list.contains("2"));
            Assertions.assertTrue(list.contains("1"));

            Assertions.assertFalse(list.contains("8"));
            Assertions.assertFalse(list.contains("9"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * Задача 5: Вернуть набор строк, которые равны заданной
     */
    @Test
    public void find() {
        Tree234<String> tree = new Tree234<>();
        try {
            Scanner scanner = new Scanner(new File("test.txt"));
            while (scanner.hasNextLine()) {
                tree.insert(scanner.nextLine());
            }
            Assertions.assertEquals(-1, tree.find("-15"));
            Assertions.assertNotEquals(-1, tree.find("4"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * Задача 6: Вернуть набор строк, которые в диапозоне между заданными
     */
    @Test
    public void inRange() {
        Tree234<String> tree = new Tree234<>();
        try {
            Scanner scanner = new Scanner(new File("test.txt"));
            while (scanner.hasNextLine()) {
                tree.insert(scanner.nextLine());
            }
            ArrayList<String> list;
            list = tree.getListOfElementsInRange("3", "8");
            Assertions.assertTrue(list.contains("3"));
            Assertions.assertTrue(list.contains("4"));
            Assertions.assertTrue(list.contains("5"));
            Assertions.assertTrue(list.contains("6"));
            Assertions.assertTrue(list.contains("7"));
            Assertions.assertTrue(list.contains("8"));

            Assertions.assertFalse(list.contains("2"));
            Assertions.assertFalse(list.contains("1"));
            Assertions.assertFalse(list.contains("9"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * Задача 7: Вернуть первую строку
     */
    @Test
    public void first() {
        Tree234<String> tree = new Tree234<>();
        try {
            Scanner scanner = new Scanner(new File("test.txt"));
            while (scanner.hasNextLine()) {
                tree.insert(scanner.nextLine());
            }
            Assertions.assertEquals("1", tree.getSmallest());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * Задача 8: Вернуть последнюю строку
     */
    @Test
    public void last() {
        Tree234<String> tree = new Tree234<>();
        try {
            Scanner scanner = new Scanner(new File("test.txt"));
            while (scanner.hasNextLine()) {
                tree.insert(scanner.nextLine());
            }
            Assertions.assertEquals("9", tree.getLargest());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}