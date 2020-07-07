package restaurant.votingsystem.to;

public class DishTo extends BaseTo {
    private String description;
    private double price;

    public DishTo(Integer id, String description, double price) {
        super(id);
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
