package mx.onecard.lists.item;

/**
 * Created by OneCardAdmon on 29/06/2015.
 * Esta clase agrupara los elementos de los renglones de la ListView
 * de la activity Navigation Drawer
 */
public class NavMenu {
    private String name;
    private int iconId;

    public NavMenu(String name, int iconId) {
        this.name = name;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public int getIconId() {
        return iconId;
    }

}
