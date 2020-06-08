package restaurant.votingsystem.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.repository.DishRepository;
import restaurant.votingsystem.service.DishService;

import java.util.List;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {
    static final String REST_URL = "/dishes";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DishRepository repository;

    @GetMapping("/{id:\\d+}")
    public Dish get(@PathVariable int id) {
        log.info("get dish {} ", id);
        return repository.findById(id);
    }

    @GetMapping
    public List<Dish> getAll(@RequestParam(value = "description", required = false) String description) {
        log.info("get all dishes that the description contains '{}'",description);
        if (description!=null) {
            return repository.getAllByDescription(description.toLowerCase());
        }
        return repository.getAllByDescription("");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Dish save(@RequestBody Dish dish){
        log.info("'{}' was added", dish);
        return repository.save(dish);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("dish with id={} was deleted", id);
        repository.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Dish dish, @PathVariable int id) {
        log.info("Dish '{}' was updated", dish.getDescription());
        dish.setId(id);
        repository.save(dish);
    }
}
