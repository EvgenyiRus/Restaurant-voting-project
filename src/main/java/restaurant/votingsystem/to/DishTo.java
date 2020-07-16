package restaurant.votingsystem.to;

public class DishTo extends BaseTo {
    private String description;

    public DishTo(Integer id, String description) {
        super(id);
        this.description = description;
    }

    public DishTo(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
