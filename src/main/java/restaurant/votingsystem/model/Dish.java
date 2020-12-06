package restaurant.votingsystem.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = "description",
        name = "dishes_unique_description_idx")})
public class Dish extends AbstractBaseEntity {
    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(max = 100)
    private String description;

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

}
