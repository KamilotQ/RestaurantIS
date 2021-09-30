package sample.Menu;

public class Menu {
    private int idmenu;
    private String dishname;
    private String description;
    private Double price;
    private String quantity;

    public Menu(int idmenu, String dishname, String description, Double price, String quantity) {
        this.idmenu = idmenu;
        this.dishname = dishname;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public int getIdmenu() {
        return idmenu;
    }

    public void setIdmenu(int idmenu) {
        this.idmenu = idmenu;
    }

    public String getDishname() {
        return dishname;
    }

    public void setDishname(String dishname) {
        this.dishname = dishname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
