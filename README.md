# MarketMongo

Что нужно сделать

Напишите программу для управления товарами в магазинах: создайте в MongoDB коллекции для хранения товаров и магазинов. У объекта магазин предусмотрите поле для списка товаров.


Программа должна запускаться и принимать на вход следующие команды:

Команда добавления магазина. Сначала укажите название команды, затем название магазина. В одно слово, без пробелов.

Команда добавления товара. Сначала укажите название команды, затем название товара, в одно слово, без пробелов. Затем укажите целое число — цену товара в рублях.

Команда добавления товара в магазин.Сначала укажите название команды, затем название товара и магазина. Для простоты будем считать, что в магазине продаются «Вафли». Не будем хранить или брать в расчёт количество коробок вафель в магазине.

Команда получения информации о товарах во всех магазинах. Команда должна выводить для каждого магазина:
общее количество наименований товаров,
среднюю цену товаров,
самый дорогой и самый дешевый товар,
количество товаров дешевле 100 рублей.
Постарайтесь вести расчёты на стороне базы данных (например, aggregate), а не в вашей программе.
