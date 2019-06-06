# yandex-kassa-sdk-java
Реализация API на JAVA для работы с сервисом Yandex Kassa - https://kassa.yandex.ru

**Пример использования:**  

    YandexKassaClient kassaClient = new YandexKassaClient.Builder()
                .secretKey(YANDEX_API_SECRET_KEY)
                .shopId(YANDEX_SHOP_ID)
                .build();

    BigDecimal amount = new BigDecimal("100");
    Currency currency = Currency.RUB;
    String paymentToken = "test_payment_token";
    Payment payment = null;
    try {
        payment = kassaClient.createPayment(amount, currency, paymentToken);
    } catch (YandexKassaException e) {
        e.printStackTrace();
    }
    
YANDEX_SHOP_ID и YANDEX_API_SECRET_KEY можно получить в кабинете клиента Yandex.Kassa после регистрации.
    
**Реализовано:**
1. Создание платежа с помощью платёжного токена из мобильного SDK от Yandex.
2. Получение платежа по идентификатору.

Работа над реализацией только началась. Проект наполняется кодом по мере производственной необходимости.
