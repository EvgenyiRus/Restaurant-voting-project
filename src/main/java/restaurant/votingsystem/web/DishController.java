package restaurant.votingsystem.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.repository.DishRepository;
import restaurant.votingsystem.repository.MenuItemRepository;
import restaurant.votingsystem.util.DishHistoryUtil;
import restaurant.votingsystem.util.exception.NotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "The Dishes API", description = "Work with dishes")
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@CacheConfig(cacheNames={"dishes"})
public class DishController {
    public static final String REST_URL = "/dishes";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @GetMapping
    @Cacheable
    public List<Dish> getAll() {
        log.info("Get all dishes");
        return dishRepository.getAll();
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    public Dish get(@PathVariable int id) {
        log.info("Get dish with id={} ", id);
        return dishRepository.findById(id).orElseThrow(
                () -> new NotFoundException(new String[]{"dish", String.valueOf(id)}, NotFoundException.NOT_FOUND_EXCEPTION));
    }

    @GetMapping("/{id}/history")
    public List<MenuItem> getHistory(@PathVariable int id) {
        log.info("Get dish history with id='{}'", id);
        return DishHistoryUtil.getHistory(menuItemRepository.getHistoryDish(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("Dish with id={} was deleted", id);
        dishRepository.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id) {
        log.info("Dish with id='{}' was updated", id);
        dish.setId(id);
        dishRepository.save(dish);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Dish> create(@Valid @RequestBody Dish dish) {
        log.info("New dish '{}' was added", dish.getDescription());
        dishRepository.save(dish);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(dish.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(dish);
    }
}
