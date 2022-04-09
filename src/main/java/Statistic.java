import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;

public class Statistic {

    public static AggregateIterable<Document> getStatistics() {
        return Main.products.aggregate(Arrays.asList(
                Aggregates.lookup("shops", "name", "products", "statistic"),
                Aggregates.unwind("$statistic"),
                Aggregates.group("$statistic.name",
                        Accumulators.sum("productscount", 1),
                        Accumulators.avg("avgprice", "$price"),
                        Accumulators.max("maxprice", "$price"),
                        Accumulators.min("minprice", "$price")
                )
        ));
    }

    public static void printStatistics(AggregateIterable<Document> aggregates) {
        for (Document document : aggregates) {
            String shopName = document.getString("_id");

            System.out.println("Магазин " + shopName);
            System.out.println("\tКоличество товаров: " + document.get("productscount"));
            System.out.println("\tСредняя цена товара: " + document.get("avgprice"));
            System.out.println("\tСамый дорогой товар: " + document.get("maxprice"));
            System.out.println("\tСамый недорогой товар: " + document.get("minprice"));
            System.out.println("\tКоличество товаров, дешевле 100 рублей: " + cheaperThanHundredRubles(shopName) + "\n");
        }
    }

    static long cheaperThanHundredRubles(String shopName) {
        Document shop = MarketAndProduct.getShop(shopName);
        ArrayList<String> product = (ArrayList<String>) shop.get("products");
        return product.stream()
                .filter(i -> (int) MarketAndProduct.getProduct(i).get("price") < 100)
                .count();
    }
}
