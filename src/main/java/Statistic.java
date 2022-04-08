import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Projections.include;
import static java.util.Arrays.asList;

public class Statistic {

    public static AggregateIterable<Document> getStatistics() {
        return Main.productCollection.aggregate(Arrays.asList(
                Aggregates.lookup("market", "name", "products", "statistic"),
                Aggregates.unwind("$statistic"),
                Aggregates.group("$statistic.name",
                        Accumulators.sum("productsCount", 1),
                        Accumulators.avg("avgPrice", "$price"),
                        Accumulators.max("maxPrice", "$price"),
                        Accumulators.min("minPrice", "$price")
                )
        ));
    }

    public static void printStatistics(AggregateIterable<Document> aggregates) {
        for (Document document : aggregates
        ) {
            String marketName = document.getString("_id");

            System.out.println("Магазин " + marketName);
            System.out.println("\tКоличество товаров: " + document.get("productsCount"));
            System.out.println("\tСредняя цена товара: " + document.get("avgPrice"));
            System.out.println("\tСамый дорогой товар: " + document.get("maxPrice"));
            System.out.println("\tСамый недорогой товар: " + document.get("minPrice"));
            System.out.println("\tКоличество товаров, дешевле 100 рублей: " + countGoodsLtHundred(marketName) + "\n");

        }
    }

    static long countGoodsLtHundred(String shopName) {
        Document shop = getShop(shopName);
        ArrayList<String> product = (ArrayList<String>) shop.get("product");
        return product.stream().filter(i -> (int) getItem(i).get("price") < 100).count();
    }

    private static Document getShop(String name) {
        return Main.marketCollection.find(new Document("name", name)).first();
    }

    private static Document getItem(String name) {
        return Main.productCollection.find(new Document("name", name)).first();
    }




//    private static AggregateIterable<Document> getStatistics() {
//        return Main.productCollection.aggregate(Arrays.asList(
//                Aggregates.lookup("shops", "name", "goods", "shops_list"),
//                Aggregates.unwind("$shops_list"),
//                Aggregates.group("$shops_list.name",
//                        Accumulators.sum("goodscount", 1),
//                        Accumulators.avg("avgprice", "$price"),
//                        Accumulators.max("maxprice", "$price"),
//                        Accumulators.min("minprice", "$price")
//                )
//        ));


//        return Main.database.getCollection("marketmongo").aggregate(
//                asList(lookup("product", "products", "name", "product"),
//                        project(include("name", "product")),
//                        unwind("$product"),
//                        sort(Sorts.ascending("product.price")),
//                        group("$name",
//                                sum("totalProduct", 1L),
//                                avg("avgPrice", "product.price"),
//                                first("cheap", "product.name"),
//                                first("cheapPrice", "product.price"),
//                                last("expensive", "product.name"),
//                                last("expensivePrice", "product.price"),
//                                sum("less100", Document.parse(
//                                        "{ \"$cond\": [ { \"$lt\": [ \"product.price\", 100 ] }, 1, 0 ] }")))));
//    }


//    public static void printStatistics() {
//        AggregateIterable<Document> aggregateIterable =
//                Main.marketCollection.aggregate(List.of(
//                        Aggregates.lookup("product", "products", "name", "statistics"),
//                                    unwind("$statistics"),
//                                    group("$name",
//                                        avg("avgPrice", "$statistics.price"),
//                                        min("minPrice", "$statistics.name"),
//                                        max("maxPrice", "$statistics.name"),
//                                        sum("count", 1),
//                                        sum("countLessThanHundred", new Document("$cond",
//                                                Arrays.asList(new Document("$lt",
//                                                    Arrays.asList("$statistics.price", 100)), 1, 0)))
//                        )));
//        for (Document doc : aggregateIterable) {
//            System.out.println("Market: " + doc.get("_id") +
//                    "\n Product count: " + doc.get("count") +
//                    "\n Product count less than hundred: " + doc.get("countLessThanHundred") +
//                    "\n Average price product: " + doc.get("avgPrice") +
//                    "\n Min price product: " + doc.get("minPrice") +
//                    "\n Max price product: " + doc.get("maxPrice"));
//        }
//

//        getStatistics().forEach(response ->
//                System.out.println("Название магазина: " + response.get("_id") + "\r\n" +
//                                "Общее количество товаров в магазине: " + response.getLong("totalProduct") + "\r\n" +
//                                "Средняя стоимость товаров в магазине: " + Math.round(response.getDouble("avgPrice")) + "\r\n" +
//                                "Самый дешевый товар " + response.getString("cheap") + " стоит " + response.getInteger("cheapPrice") + "\r\n" +
//                                "Самый дорогой товар " + response.getString("expensive") + " стоит " + response.getInteger("expensivePrice") + "\r\n" +
//                                "Общее количество товара дешевле 100: " + response.getInteger("less100") + "\r\n"));
//    }
}
