package restaurant.votingsystem.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Configuration
@EnableCaching
public class ConfigCache {

    @Bean
    public CaffeineCache cacheRestaurants() {
        return new CaffeineCache("restaurants",
                Caffeine.newBuilder()
                        .expireAfterAccess(1, TimeUnit.DAYS)
                        .maximumSize(100)
                        .build());
    }

    @Bean
    public CaffeineCache cacheMenuItems() {
        return new CaffeineCache("menuItems",
                Caffeine.newBuilder()
                        .expireAfterAccess(1, TimeUnit.DAYS)
                        .maximumSize(1000)
                        .build());
    }

    @Bean
    public CaffeineCache cacheDishes() {
        return new CaffeineCache("dishes",
                Caffeine.newBuilder()
                        .expireAfterAccess(1, TimeUnit.DAYS)
                        .maximumSize(500)
                        .build());
    }
}
