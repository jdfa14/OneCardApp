package mx.onecard.lists.items;

import mx.onecard.onecardapp.R;

/**
 * Created by OneCardAdmon on 11/08/2015.
 * Clase que contendra la informacion de la notificacion
 */
public class NotificationItem {
    public static final int NOTICE = 0;
    public static final int WARNING = 1;
    public static final int NEW = 2;

    private int id;
    private String tittle;
    private String content;
    private int imgResourceId;
    private int colorId;
    private int type;

    public NotificationItem(int type, int id, String tittle, String content) {
        this.id = id;
        this.tittle = tittle;
        this.type = type;
        this.content = content;
        setType(type);
    }

    private void setType(int type) {
        switch (type) {
            case NOTICE:
                imgResourceId = R.drawable.ic_message_black_24dp;
                colorId = R.color.list_message;
                break;
            case WARNING:
                imgResourceId = R.drawable.ic_warning_white_24dp;
                colorId = R.color.list_warning;
                break;
            case NEW:
                imgResourceId = R.drawable.ic_new_releases_black_24dp;
                colorId = R.color.list_news;
                break;
        }
    }

    public String getTittle() {
        return tittle;
    }

    public String getContent() {
        return content;
    }

    public int getImgResourceId() {
        return imgResourceId;
    }


    public int getColorId() {
        return colorId;
    }

    public int getType() {
        return type;
    }
}
