package restaurant.votingsystem.to;

import com.sun.istack.NotNull;

import java.time.LocalDate;

public class VoteTo extends BaseTo {

    private LocalDate date = LocalDate.now();
    @NotNull
    private int userId;
    @NotNull
    private int restaurantId;

    public VoteTo() {
    }

    public VoteTo(Integer id, LocalDate date, int userId, int restaurantId) {
        super(id);
        this.date = date;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }
}
