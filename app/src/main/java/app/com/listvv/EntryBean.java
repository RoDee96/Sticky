package app.com.listvv;

/**
 * Created by Rohit on 6/13/2017.
 */

public class EntryBean {
    private int id;
    private String text;
    private String date;
    private String time;
//    private int image;

    public int getId() {
        return id;
    }

    public EntryBean(int id, String text, String time, String date) {
        this.text = text;
        //this.image = image;
        this.time = time;
        this.date = date;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

//    public int getImage() {
//        return image;
//    }
}
