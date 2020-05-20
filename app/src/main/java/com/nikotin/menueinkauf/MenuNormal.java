package com.nikotin.menueinkauf;

//DV: This is the Class which is used to map the JSON Response to an Object
//in our case we will use the Menu Object from the Menu JSON Object of the Backend
//e.g. https://ffhs-innt-my-menu.eu-gb.mybluemix.net/v1/menu/random
public class MenuNormal {
    private Integer menuId;
    private String name;
    private String kueche;
    private String art;
    private String bildUrl;
    private Integer anzPersonen;
    private String zutaten;
    private String zubereitung;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKueche() {
        return kueche;
    }

    public void setKueche(String kueche) {
        this.kueche = kueche;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getBildUrl() {
        return bildUrl;
    }

    public void setBildUrl(String bildUrl) {
        this.bildUrl = bildUrl;
    }

    public Integer getAnzPersonen() {
        return anzPersonen;
    }

    public void setAnzPersonen(Integer anzPersonen) {
        this.anzPersonen = anzPersonen;
    }

    public String getZutaten() {
        return zutaten;
    }

    public void setZutaten(String zutaten) {
        this.zutaten = zutaten;
    }

    public String getZubereitung() {
        return zubereitung;
    }

    public void setZubereitung(String zubereitung) {
        this.zubereitung = zubereitung;
    }
}

