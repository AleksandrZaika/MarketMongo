public class Input {

    private static String command = "";
    private static String product = "";
    private static String price = "";
    private static String market = "";

    public static String checkInput(String text) {

        String formattedText = "";
        String command = commandName(text);

        try {
            switch (command) {
                case ("ДОБАВИТЬ_МАГАЗИН"):
                    MarketAndProduct.addMarket(marketName(text));
                    break;

                case ("ДОБАВИТЬ_ТОВАР"):
                    MarketAndProduct.addProduct(productName(text), priceProduct(text));
                    break;

                case ("ВЫСТАВИТЬ_ТОВАР"):
                    MarketAndProduct.addProductToMarket(productName(text), marketName(text));
                    break;

                case ("СТАТИСТИКА_ТОВАРОВ"):
                    MarketAndProduct.viewStatistic();
                    break;

                case ("ВЫХОД"):
                    Main.mongoClient.close();
                    break;

                default:
                    System.out.println("Неверный формат команды");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return formattedText;
    }

    public static String commandName(String text) {
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch >= 'а' && ch <= 'я') {
                command = text.substring(0, i - 1);
                break;
            } else {
                command = text;
            }
        }
        return command;
    }

    public static String marketName(String text) {
        int firstIndex = command.length() + product.length();
        int lastIndex = text.length();
        market = text.substring(firstIndex, lastIndex);
        return market;
    }

    public static String productName(String text) {
        product = "";
        int firstIndex = command.length() + 1;
        for (int i = firstIndex; i < text.length(); i++) {
            char ch = text.charAt(i);
            if ((ch >= '0' && ch <= '9') || (ch >= 'А' && ch <= 'Я')) {
                product = text.substring(firstIndex - 1, i);
                break;
            }
        }
        return product;
    }

    public static Integer priceProduct(String text) {
        int firstIndex = command.length() + product.length();
        int lastIndex = text.length();
        price = text.substring(firstIndex, lastIndex);
        return Integer.parseInt(price);
    }
}


