package edu.umich.seedforandroid.patient;

public class NavigationDrawerItem  {

    private int icon;
    private String title;

    public NavigationDrawerItem(int icon, String title)  {

        super();
        this.icon = icon;
        this.title = title;
    }

    public int getIcon()  {

        return icon;
    }

    public String getTitle()  {

        return title;
    }
}
