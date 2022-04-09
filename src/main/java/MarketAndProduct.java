import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import java.util.ArrayList;

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
    public static void addMarket(String shopName) {
        Document shop = new Document("name", shopName);
        shop.append("products", new ArrayList<String>());
        if (getShop(shopName) == null) {
            Main.shops.insertOne(shop);
            System.out.println("Магазин: " + shopName + " добавлен в базу.");
        } else {
            System.out.println("Магазин: " + shopName + " уже есть в базе.");
        }
    }

    //добавление товара в БД
    public static void addProduct(String productName, Integer priceProduct) {
        try {
            Document product = new Document("name", productName);
            product.append("price", priceProduct);
            if (priceProduct != 0) {
                if (getProduct(productName) == null) {
                    Main.products.insertOne(product);
                    System.out.println("Товар: " + productName + priceProduct + "р., добавлен в базу.");
                } else {
                    System.out.println("Товар: " + productName + " уже есть в базе.");
                }
            }
        } catch (Exception e) {
            System.out.println("Неверный формат команды");
        }
    }

    //выставить товар (добавление товара в список магазина)
    public static void addProductToMarket(String productName, String shopName) {
        try {
            Document product = Main.products
                    .find(new BsonDocument().append("name", new BsonString(productName)))
                    .first();
            Document market = Main.shops
                    .find(new BsonDocument().append("name", new BsonString(shopName)))
                    .first();
            if (product == null) {
                System.out.println("Товар: " + productName + " не найден в базе.");
            } else if (market == null) {
                System.out.println("Магазин: " + shopName + " не найден в базе.");
            } else {
                Main.shops.updateOne(getShop(shopName), new Document("$addToSet",
                        new Document("products", getProduct(productName).get("name"))));
                System.out.println("Товар: " + productName + " выставлен в магазине " + shopName);
            }
        } catch (Exception e) {
            System.out.println("Неверный формат команды");
        }
    }

    //статистика товаров
    public static void viewStatistic() {
        Statistic.printStatistics(Statistic.getStatistics());
    }

    public static Document getShop(String name) {
        return Main.shops.find(new Document("name", name)).first();
    }

    public static Document getProduct(String name) {
        return Main.products.find(new Document("name", name)).first();
    }
}
