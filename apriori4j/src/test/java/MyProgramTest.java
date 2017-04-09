import apriori4j.AprioriAlgorithm;
import apriori4j.ItemSet;
import apriori4j.Transaction;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Md. Mainul Hasan Patwary.
 * @email mhasan_mitul@yahoo.com
 * @skype mhasan_mitul
 * @Date 08-Apr-17
 */
public class MyProgramTest {
    public static List<Transaction> getGroceryTransactions() throws IOException {
        String fullFilePath = "D:\\workspace_j2ee\\apriori4j\\apriori4j\\src\\test\\resources\\grocery.csv";
        return getTransactions(fullFilePath);
    }

    public static List<Transaction> getMushroomTransactions() throws IOException {
        String fullFilePath = "D:\\workspace_j2ee\\apriori4j\\apriori4j\\src\\test\\resources\\mushroom.csv";
        return getTransactions(fullFilePath);
    }

    public static List<Transaction> getChessTransactions() throws IOException {
        String fullFilePath = "D:\\workspace_j2ee\\apriori4j\\apriori4j\\src\\test\\resources\\chess.csv";
        return getTransactions(fullFilePath);
    }

    private static List<Transaction> getTransactions(String fullFilePath) throws IOException {
        List<String> lines = FileUtils.readLines(new File(fullFilePath));
        Set<Transaction> transactions = new LinkedHashSet<>();
        for (String line : lines) {
            Set<String> items = new HashSet<String>();
            for (String item : line.split(",")) {
                items.add(item);
            }

            transactions.add(new Transaction(items));
        }
        return new LinkedList<>(transactions);
    }

    @Test
    public void testGroceryTransaction() throws Exception{

        List<Transaction> transactions = getGroceryTransactions();
        decendingOrderSortOfTransaction(transactions);

        List<Transaction> copyOfTransactions = getGroceryTransactions();

        Set<ItemSet> itemSet= AprioriAlgorithm.toOneElementItemSets(transactions);

        Map<Transaction, Set<Transaction>> antichain = findAntichain(transactions, copyOfTransactions);

        int d = findD(antichain);

        System.out.println("Size of Unique transaction: "+transactions.size());

        System.out.println("Number of Unique Items: "+itemSet.size());

        System.out.println("D-->"+d);

        System.out.println("N Calculated By d-bound -->"+evalueateNBasedOnDBound(d,0.1,0.1));

        System.out.println("N Calculated By Toivonen -->"+evalueateNToivonen(0.1,0.1));
    }

    @Test
    public void testMushroomTransaction() throws Exception{
        List<Transaction> transactions = getMushroomTransactions();

        decendingOrderSortOfTransaction(transactions);

        List<Transaction> copyOfTransactions = getMushroomTransactions();

        Set<ItemSet> itemSet= AprioriAlgorithm.toOneElementItemSets(transactions);

        Map<Transaction, Set<Transaction>> antichain = findAntichain(transactions, copyOfTransactions);

        int d = findD(antichain);

        System.out.println("Size of Unique transaction: "+transactions.size());

        System.out.println("Number of Unique Items: "+itemSet.size());

        System.out.println("D-->"+d);

        System.out.println("N Calculated By d-bound -->"+evalueateNBasedOnDBound(d,0.1,0.1));

        System.out.println("N Calculated By Toivonen -->"+evalueateNToivonen(0.1,0.1));
    }

    @Test
    public void testChessTransaction() throws Exception{
        List<Transaction> transactions = getChessTransactions();

        decendingOrderSortOfTransaction(transactions);

        List<Transaction> copyOfTransactions = getChessTransactions();

        Set<ItemSet> itemSet= AprioriAlgorithm.toOneElementItemSets(transactions);

        Map<Transaction, Set<Transaction>> antichain = findAntichain(transactions, copyOfTransactions);

        int d = findD(antichain);

        System.out.println("Size of Unique transaction: "+transactions.size());

        System.out.println("Number of Unique Items: "+itemSet.size());

        System.out.println("D-->"+d);

        System.out.println("N Calculated By d-bound -->"+evalueateNBasedOnDBound(d,0.1,0.1));

        System.out.println("N Calculated By Toivonen -->"+evalueateNToivonen(0.1,0.1));
    }

    private void decendingOrderSortOfTransaction(List<Transaction> transactions) {
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o2.getItems().size() - o1.getItems().size();
            }
        });
    }

    private Map<Transaction,Set<Transaction>> findAntichain(List<Transaction> transactions, List<Transaction> copyOfTransactions) {
        Map<Transaction, Set<Transaction>> antichain = new LinkedHashMap<>();
        for(Transaction transaction: transactions){
            Set<Transaction> disjointTransaction = new HashSet<>();
            for (Transaction transactionOfCopyOfTransaction: copyOfTransactions){
                if(!transaction.getItems().containsAll(transactionOfCopyOfTransaction.getItems())){
                    disjointTransaction.add(transactionOfCopyOfTransaction);
                }
            }
            antichain.put(transaction,disjointTransaction);
        }
        return antichain;
    }

    private int findD(Map<Transaction, Set<Transaction>> antichain) {
        int d = 0;
        for(Map.Entry<Transaction,Set<Transaction>> antichainEntry: antichain.entrySet()){
            int disjointElementAtLeastSameNumberOrGraterThanItem = 0;
            for(Transaction disjointTransaction: antichainEntry.getValue()){
                if(disjointTransaction.getItems().size() >= antichainEntry.getKey().getItems().size()){
                    disjointElementAtLeastSameNumberOrGraterThanItem++;
                }
            }
            System.out.println(antichainEntry.getKey()+"-->"+disjointElementAtLeastSameNumberOrGraterThanItem);

            if(antichainEntry.getKey().getItems().size() >= d && disjointElementAtLeastSameNumberOrGraterThanItem >= d){
                d = antichainEntry.getKey().getItems().size();
            }
        }
        return d;
    }

    public int evalueateNBasedOnDBound(int d,double epsilon,double delta){
        int n = (int) Math.ceil((4 / (epsilon * epsilon)) * (d + Math.log(1/delta)));
        return n;
    }

    public int evalueateNToivonen(double epsilon,double delta){
        int n = (int) Math.ceil((1 / (2 * epsilon * epsilon)) * Math.log(2/delta));
        return n;
    }
}
