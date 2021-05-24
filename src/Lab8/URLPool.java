package Lab8;

import java.util.LinkedList;
import java.util.*;

public class URLPool
{
    private HashMap<String, URLPair> visited;
    private LinkedList<URLPair> toVisit;

    public URLPool()
    {
        visited = new HashMap<>();
        toVisit = new LinkedList<>();
    }

    //Сихронизированно для всех объектов класса
    public synchronized URLPair getLink()
    {
        boolean isWaiting = false;
        if (toVisit.size() == 0) //Если есть сайт на посещение
        {
            try
            {
                Crawler.threadsWaiting++; //Увеличиваем кол-во ожидающих потоков
                isWaiting = true; //Поток ожидает
                this.wait(); //Появилась ссылка на посещение
            }
            catch (Exception e)
            {
                return null;
            }
        }
        if (isWaiting) Crawler.threadsWaiting--; //Уменьшаем кол-во ожидающих потоков
        URLPair urlPair = toVisit.pop(); // Поток берет ссылку на посещение
        visited.put(urlPair.getURL(), urlPair); // Добавляем ссылку в посещенные
        return urlPair;
    }

    public synchronized void addLink(URLPair urlPair)
    {
        if (!visited.containsKey(urlPair.getURL())) // Если нет в списке посещенных то добавляем
        {
            toVisit.add(urlPair);
            this.notify(); // Сообщаем что есть ссылка на посещение
        }
    }
}

