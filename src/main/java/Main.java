import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;

public class Main {

    public static MongoCollection<Document> shops;
    public static MongoCollection<Document> products;
    public static MongoClient mongoClient;
    public static MongoDatabase database;

    public static void main(String[] args) {

        mongoClient = new MongoClient("127.0.0.1", 27017);
        database = mongoClient.getDatabase("marketMongo");

        // Создаем коллекции
        shops = database.getCollection("shops");
        products = database.getCollection("product");

        // Удалим из нее все документы
        shops.drop();
        products.drop();

        Scanner scanner = new Scanner(System.in);
        MarketAndProduct.help();
        while (true) {
            System.out.print("Введите команду: ");
            String input = scanner.nextLine();

            Input.checkInput(input);
        }


    }
}