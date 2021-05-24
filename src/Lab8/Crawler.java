package Lab8;


public class Crawler {

    private String URL;
    private static int maxDepth;
    public static int threadsWaiting = 0; //Кол-во ожидающих потоков
    public static int URLCount = 0;
    private static final String protocol = "https:";

    public static void main(String args[])
    {
        Crawler crawler = new Crawler("https://yandex.ru/", 2);
        crawler.run();
        //Завершаем программу, когда один из потоков вызывает printURLCount
        Runtime.getRuntime().addShutdownHook(new Thread(Crawler::printURLCount));
    }

    public Crawler(String address, int maxDepth)
    {
        this.URL = address;
        this.maxDepth = maxDepth;
    }

    public void run()
    {
        CrawlerTask task = new CrawlerTask(new URLPair(URL, 0));
        task.start(); //Запускаем поток
    }

    public static int getMaxDepth()
    {
        return maxDepth;
    }

    private static void printURLCount()
    {
        System.out.println("Final URL count: " + URLCount);
    }
}
