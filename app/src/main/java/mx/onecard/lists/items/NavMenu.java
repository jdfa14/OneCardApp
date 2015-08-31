package mx.onecard.lists.items;

import mx.onecard.onecardapp.R;

/**
 * Created by OneCardAdmon on 29/06/2015.
 * Esta clase agrupara los elementos de los renglones de la ListView
 * de la activity Navigation Drawer
 */
public class NavMenu {
    public String name;
    public int iconId;
    public TYPE type;

    public enum TYPE{
        ACTION,
        TRANSITION,
        DELIMITER
    }

    public NavMenu(String name, int iconId, TYPE type) {
        this.name = name;
        this.iconId = iconId;
        this.type = type;
    }
}
