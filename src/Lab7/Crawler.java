package Lab7;

import java.net.*;
import java.util.LinkedList;
import java.util.*;

public class Crawler {

    private int maxDepth; //Макс глубина
    private LinkedList<URLDepthPair> toVisit; // Страницы для посещения
    private HashMap<String, URLDepthPair> visited; // Посещенные сайты
    private static final String protocol = "http:"; //Протокол

    public static void main(String args[])
    {
        Crawler crawler = new Crawler("https://mail.yandex.ru/", 2);
        crawler.run();
    }

    public Crawler(String address, int maxDepth){
        visited = new HashMap<>();
        toVisit = new LinkedList<>();
        //Кладем изначальный адресс
        toVisit.add(new URLDepthPair(address, 0));
        this.maxDepth = maxDepth;
    }

    public void run(){
        while (toVisit.size() > 0) {
            URLDepthPair current_address = toVisit.pop(); //Последняя пара в списке на посещение
            if (visited.containsKey(current_address.getUrl())) continue; // Проверка на посещение ранее
            visited.put(current_address.getUrl(), current_address);//Перекладываем в посещенные
            System.out.println(current_address);
            if (current_address.getDepth() < maxDepth) // Проверка макс глубины
                search(current_address); // Если не макс, поиск на текущей ссылки
        }
    }

    private void search(URLDepthPair current_address){
        try {
            URL url = new URL(current_address.getUrl()); // URL Кладем адресс
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();//Открываем подлючение
            connection.setRequestMethod("GET");//Получаем данные
            Scanner scanner = new Scanner(connection.getInputStream());//Сканер считывает поток данных

            //Ищем любую ссылку на сайте
            while (scanner.findWithinHorizon("<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1", 0) != null){
                String new_url = scanner.match().group(2); //Записываем URL
                newURLPair(new_url, current_address); // Создаем новую пару URL
            }
            //Отлов ошибок
        } catch (Exception e) {
            System.err.println("Exception: " + e.getLocalizedMessage());
        }
    }

    private void newURLPair(String new_url, URLDepthPair current_address){
        //Если ссылка с двух слешей добавляем протокол
        if (new_url.startsWith("//"))
            new_url = protocol + new_url;
        //Строка без протокола и слешей
        else if (!new_url.startsWith(protocol)) return;
        //Новая пара сайт глубина
        URLDepthPair new_pair = new URLDepthPair(new_url, current_address.getDepth() + 1);
        //Добавляем пару на посещение
        toVisit.add(new_pair);
    }
}
