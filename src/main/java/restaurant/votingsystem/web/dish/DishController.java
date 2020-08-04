package restaurant.votingsystem.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.repository.DishRepository;
import restaurant.votingsystem.repository.MenuItemRepository;
import restaurant.votingsystem.util.DishHistoryUtil;

import javax.annotation.security.RolesAllowed;
import java.net.URI;
import java.util.List;

import static org.jsoup.internal.StringUtil.isBlank;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {
    static final String REST_URL = "/dishes";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        log.info("Get dish with id={} ", id);
        return dishRepository.findById(id).orElseThrow();
    }

    @GetMapping
    public List<Dish> getAll(@RequestParam(value = "description", required = false) String description) {
        log.info("Get all dishes or that the description contains '{}'",description);
        if (!isBlank(description)) {
            return dishRepository.getAllByDescription(description.toLowerCase());
        }
        return dishRepository.getAllByDescription("");
    }

    @GetMapping("/{id}/history")
    public List<DishHistory> getHistory(@PathVariable int id) {
        log.info("Get history dish with id='{}'",id);
        return DishHistoryUtil.getHistoryDish(menuItemRepository.getHistoryDish(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Dish with id={} was deleted", id);
        dishRepository.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Dish dish, @PathVariable int id) {
        log.info("Dish with id='{}' was updated", id);
        dish.setId(id);
        dishRepository.save(dish);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@RequestBody Dish dish) {
        log.info("New dish '{}' was added", dish.getDescription());
        dishRepository.save(dish);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(dish.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(dish);
    }
}
