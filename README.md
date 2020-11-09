Restaurant voting project 
===============================
Design and implement a REST API using Hibernate/Spring/Spring-Boot **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

##### Each restaurant provides new menu each day.

----------
### cURL commands for unregister user:

##### register new User
`curl -s -i -X POST -d '{"name":"New User","email":"new@mail.ru","password":"password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/profile/register`

### cURL commands for user:
 
##### get All Menus Restaurants
`curl -s http://localhost:8080/restaurants/menus --user user@yandex.ru:password`
 
##### get Menu Restaurant 100014
`curl -s http://localhost:8080/restaurants/100014/menus --user user@yandex.ru:password`

##### get Users who voted for Restaurant 100014
`curl -s http://localhost:8080/restaurants/100014/votes --user user@yandex.ru:password`

##### create new Vote for Restaurant 100014
`curl -s -X POST -d '{}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurants/100014/votes --user gosha@gmail.com:gosha`
 
### cURL commands for admin:

##### get All Restaurants
`curl -s http://localhost:8080/restaurants --user admin@gmail.com:admin`
 
##### create new Restaurant
`curl -s -X POST -d '{"name":"Created restaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurants --user admin@gmail.com:admin`
 
##### update Restaurant 100014
`curl -s -X PUT -d '{"name":"Edited restaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurants/100014 --user admin@gmail.com:admin`
 
##### delete Restaurant 100014
`curl -s -X DELETE http://localhost:8080/restaurants/100014 --user admin@gmail.com:admin`
 
##### create today's new MenuItem for Restaurant 100014
`curl -s -X POST -d '{"dishId":100003, "price":1000}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurants/100014/menus --user admin@gmail.com:admin`
  
##### get All Users
`curl -s http://localhost:8080/admin/users --user admin@gmail.com:admin`

##### get Users 100000
`curl -s http://localhost:8080/admin/users/100000 --user admin@gmail.com:admin`

##### get votes Users 100000
`curl -s http://localhost:8080/admin/users/100000/votes --user admin@gmail.com:admin`

##### create new Dish
`curl -s -X POST -d '{"description":"Created dish"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/dishes --user admin@gmail.com:admin`

##### get Dish that contains in description "coffee"
`curl -s http://localhost:8080/dishes?description=coffee --user admin@gmail.com:admin`

##### get history Dish 100002
`curl -s http://localhost:8080/dishes/100002/history --user admin@gmail.com:admin`

