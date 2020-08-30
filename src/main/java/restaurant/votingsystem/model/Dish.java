package restaurant.votingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = "description",
        name = "dishes_unique_description_idx")})
public class Dish extends AbstractBaseEntity {
    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(max = 100)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dish")
    @OrderBy("date DESC")
    private List<MenuItem> menuItems;

    public Dish() {
    }

    public Dish(Integer id, String description) {
        super(id);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "description='" + description + '\'' +
                ", menuItems=" + menuItems +
                '}';
    }
}
