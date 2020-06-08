package restaurant.votingsystem.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = "description",
        name = "dishes_unique_description_idx")})
public class Dish extends AbstractBaseEntity{

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(max = 100)
    private String description;

    @Column(name = "price", nullable = false,columnDefinition = "Integer default 0")
    @NotNull
    @Range(min = 0, max = 99999)
    private double price;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dish")
    private List<MenuItem> menuItems;

    public Dish() {
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

    @Override
    public String toString() {
        return "Dish{" +
                "description='" + description + '\'' +
                ", price=" + price +
                "" +
                '}';
    }
}
