package mx.onecard.onecardapp;

/**
 * Created by OneCardAdmon on 29/06/2015.
 * Esta clase agrupara los elementos de los renglones de la ListView
 * de la activity Navigation Drawer
 */
public class RowItemDrawer {
    private String name;
    private int iconId;

    public RowItemDrawer(String name, int iconId) {
        this.name = name;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
