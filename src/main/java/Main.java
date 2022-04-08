import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;

public class Main {

    public static MongoCollection<Document> marketCollection;
    public static MongoCollection<Document> productCollection;
    public static MongoClient mongoClient;
    public static MongoDatabase database;

    public static void main(String[] args) {

        mongoClient = new MongoClient("127.0.0.1", 27017);
        database = mongoClient.getDatabase("marketmongo");

        // Создаем коллекции
        marketCollection = database.getCollection("market");
        productCollection = database.getCollection("product");

        // Удалим из нее все документы
        marketCollection.drop();
        productCollection.drop();



        Scanner scanner = new Scanner(System.in);
        MarketAndProduct.help();
        while (true) {
            System.out.print("Введите команду: ");
            String input = scanner.nextLine();

            Input.checkInput(input);
        }
    }
}