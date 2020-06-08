package restaurant.votingsystem.web;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DishRepository repository;

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        Dish d= repository.getOne(id);
        return d;
    }

//    @GetMapping("/")
//    public List<Dish> get() {
//        return repository.getAllByDescription("Суп");
//    }

    @GetMapping("/")
    public List<Dish> getAll() {
        long t=repository.count();
        return repository.findAll();
    }

    @PostMapping
    public Dish save(){
        return null;
    }
}
