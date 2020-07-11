package restaurant.votingsystem.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.repository.MenuItemRepository;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = MenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuItemController {
    static final String REST_URL="/menus";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    MenuItemRepository menuItemRepository;

    @GetMapping
    public List<MenuItem> getAllToDay() {
        log.info("Get all menus items from restaurants");
        return menuItemRepository.getAllMenus(LocalDate.now());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItem> createWithLocation(@RequestBody MenuItem menuItem) {
        log.info("New menuItem was added");
        menuItemRepository.save(menuItem);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(menuItem.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(menuItem);
    }
}