package restaurant.votingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import restaurant.votingsystem.model.Dish;
import restaurant.votingsystem.model.MenuItem;
import restaurant.votingsystem.model.Restaurant;
import restaurant.votingsystem.repository.DishRepository;
import restaurant.votingsystem.repository.MenuItemRepository;
import restaurant.votingsystem.repository.RestaurantRepository;
import restaurant.votingsystem.to.RestaurantTo;
import restaurant.votingsystem.util.RestaurantUtil;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

@Service
public class RestaurantService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<MenuItem> getRestaurantsWithMenuByDay(LocalDate date){
        log.info("Get menus all restaurants");
        return menuItemRepository.getAllMenus(date);
    }
//    public List<Dish> getDishes(Integer id) {
//        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
//        Root<Element> elementRoot = criteriaQuery.from(Element.class);
//        criteriaQuery.select(criteriaBuilder.max(elementRoot.get("number")))
//                .where(criteriaBuilder.equal(elementRoot.get("sectionid"), id));
//        Long result = em.createQuery(criteriaQuery).getSingleResult();
//        return (result == null ? 0 : result) + 1;
//    }

//    public Dish get(int id) {
//        log.info("get dish {} ", id);
//        return repository.findById(id);
//    }
//
//    public Dish save(Dish dish) {
//        log.info("{} was added", dish);
//        return repository.save(dish);
//    }
//
////    @Cacheable("dishes")
//    public List<Dish> getAll(String description) {
//        log.info("get all dishes that the description contains {}",description);
//        return repository.getAllByDescription(description);
//    }
//
//    public int delete(int id) {
//        log.info("dish with id={} was deleted", id);
//        return repository.delete(id);
//    }
}
