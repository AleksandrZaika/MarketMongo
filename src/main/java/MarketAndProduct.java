
import com.mongodb.client.model.UpdateOptions;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

public class MarketAndProduct {

    //примеры команд
    public static void help() {
        System.out.println("-------------------------------");
        System.out.println("Примеры команд: ");
        System.out.println("ДОБАВИТЬ_МАГАЗИНДевяточка");
        System.out.println("ДОБАВИТЬ_ТОВАРВафли54");
        System.out.println("ВЫСТАВИТЬ_ТОВАРВафлиДевяточка");
        System.out.println("СТАТИСТИКА_ТОВАРОВ");
        System.out.println("ВЫХОД");
        System.out.println("-------------------------------");
    }

    //добавление магазина в БД
    public static void addMarket(String marketName) {
        if (Main.marketCollection.find(new BsonDocument().append("name", new BsonString(marketName))).first() == null){
            Main.marketCollection.insertOne(Document.parse("{name: \"" + marketName + "\", products: []}"));
            System.out.println("Магазин: " +  marketName + " добавлен в базу.");
        } else {
            System.out.println("Магазин: " +  marketName + " уже есть в базе.");
        }
    }

    //добавление товара в БД
    public static void addProduct(String productName, Integer priceProduct) {
        Main.productCollection.updateOne(BsonDocument.parse("{name: {$eq: \"" + productName + "\"}}"),
                BsonDocument.parse("{$set: {price: " + priceProduct + "}}"), new UpdateOptions().upsert(true));
        System.out.println("Товар: " +  productName + " с ценой " + priceProduct + "руб., добавлен в базу.");
    }

    //выставить товар (добавление товара в список магазина)
    public static void addProductToMarket(String productName, String marketName) {
        Document product = Main.productCollection
                .find(new BsonDocument().append("name", new BsonString(productName)))
                .first();
        Document market = Main.marketCollection
                .find(new BsonDocument().append("name", new BsonString(marketName)))
                .first();
        if (product == null) {
            System.out.println("Товар: " + productName + " не найден в базе.");
        } else if (market == null) {
            System.out.println("Магазин: " + marketName + " не найден в базе.");
        } else {
            Main.marketCollection.updateOne(BsonDocument.parse("{name: {$eq: \"" + marketName + "\"}}"),
                    BsonDocument.parse("{$addToSet: {products: " + product.toJson() + "}}"));
            System.out.println("Товар: " + productName + " выставлен на продажу в магазине " + marketName);
        }
    }

    //статистика товаров
    public static void viewStatistic() {
        Statistic.printStatistics(Statistic.getStatistics());
    }
}
