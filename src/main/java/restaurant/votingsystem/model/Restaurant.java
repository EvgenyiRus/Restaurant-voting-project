package restaurant.votingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name="restaurants")
public class Restaurant extends AbstractBaseEntity{

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private List<MenuItem> menuItems;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name) {
        super(id);
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", menuItems=" + menuItems +
                '}';
    }
}
