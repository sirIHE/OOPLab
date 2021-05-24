package Lab7;

public class URLDepthPair {

    private String address;
    private int depth;

    public int getDepth(){
        return depth;
    }

    public String getUrl(){
        return address;
    }

    //Конструктор (пара адресс||глубина)
    public URLDepthPair(String address, int depth){
        this.address = address;
        this.depth = depth;
    }

    //Распечатка
    @Override
    public String toString(){
        return address + " | depth: " + depth;
    }
}