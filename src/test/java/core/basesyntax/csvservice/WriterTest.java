package core.basesyntax.csvservice;

import core.basesyntax.FruitStorageStrategy;
import core.basesyntax.Storage;
import core.basesyntax.Transaction;
import core.basesyntax.operation.Buy;
import core.basesyntax.operation.Return;
import core.basesyntax.operation.Supply;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WriterTest {

    @BeforeClass
    public static void beforeClass() {
        FruitStorageStrategy.fruitStorageStrategy.put("s", new Supply());
        FruitStorageStrategy.fruitStorageStrategy.put("b", new Buy());
        FruitStorageStrategy.fruitStorageStrategy.put("r", new Return());
        List<Transaction> transactions = Reader.read("src/main/resources/easyIncome.csv");
        FruitStorageStrategy.storageOperation(transactions);
    }

    @Test
    public void normalWrite() {
        Writer.write("src/main/resources/result1.csv");
        try {
            Set<String> expected = new HashSet<>(Files.readAllLines(Path.of("src/main/resources/expectedEasyIncomeResult.csv")));
            Set<String> actual = new HashSet<>(Files.readAllLines(Path.of("src/main/resources/resultOfEasyIncome.csv")));
            expected.removeAll(actual);
            Assert.assertTrue(expected.isEmpty());
        } catch (IOException ignored) {
        }
    }

    @Test
    public void emptyStorageTest() {
        Storage.storage = new HashMap<>();
        Writer.write("src/main/resources/onlyHeader.csv");
        try {
            Set<String> expected = new HashSet<>(Files.readAllLines(Path.of("src/main/resources/onlyHeader.csv")));
            Assert.assertEquals(1, expected.size());
        } catch (IOException ignored) {
        }
    }
}
