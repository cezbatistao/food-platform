package com.food.restaurant.templates

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.food.restaurant.gateway.database.model.CategoryModel
import com.food.restaurant.gateway.database.model.MenuItemModel
import com.food.restaurant.gateway.database.model.RestaurantModel

class RestaurantModelTemplate: TemplateLoader {

    override fun load() {
        Fixture.of(RestaurantModel::class.java)
                .addTemplate("pizza hut", object : Rule() {
                    init {
                        add("id", 1L)
                        add("uuid", "cbb9c2bd-abde-48a3-891a-6229fc9b7c2f")
                        add("name", "Pizza Hut")
                        add("category", one(CategoryModel::class.java, "category pizza"))
                        add("logo", "https://pbs.twimg.com/profile_images/1333417326704791553/Mm0bj3oN.jpg")
                        add("description", "Pizza Hut é uma cadeia de restaurantes e franquias especializada em pizzas e massas. Com sede na cidade de Plano, no Texas, a Pizza Hut é a maior cadeia de pizzarias do mundo, com quase 15 mil restaurantes e quiosques em mais de 130 países. Possui 95 restaurantes no Brasil e 91 em Portugal.")
                        add("address", "Av. Nome da avenida, 123")
                        add("itens", has(3).of(MenuItemModel::class.java,
                                "pizza hut pepperoni", "pizza hut meat", "pizza hut supreme"))
                    }
                })
                .addTemplate("braz pizzaria", object : Rule() {
                    init {
                        add("id", 2L)
                        add("uuid", "23e0211c-19ea-47ee-b98e-77e023e1a95f")
                        add("name", "Bráz Pizzaria")
                        add("category", one(CategoryModel::class.java, "category pizza"))
                        add("logo", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwwg9Nscz6dkUtIpPUB0u1tAxfZmJcmcfGTA&usqp=CAU")
                        add("description", "Pizzaria reúne ingredientes clássicos e sofisticados e muito burburinho e aconchego frente aos fornos à lenha.")
                        add("address", "Av. Nome da outra avenida, 789")
                        add("itens", has(2).of(MenuItemModel::class.java,
                                "pizza braz napolitan", "pizza braz pepperoni"))
                    }
                })
                .addTemplate("dominos pizza", object : Rule() {
                    init {
                        add("id", 3L)
                        add("uuid", "36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c")
                        add("name", "Domino's Pizza")
                        add("category", one(CategoryModel::class.java, "category pizza"))
                        add("logo", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQd7Ow6kgZoZk4h4oybgOkRJf6LZ_NLpBhfRA&usqp=CAU")
                        add("description", "Domino's Pizza é uma empresa de alimentação baseada em pizzas. Atualmente, é a maior rede de entregas de pizzas do mundo, com 13.000 lojas em 83 países")
                        add("address", "Rua Nome da rua, 654")
                        add("itens", has(2).of(MenuItemModel::class.java,
                                "pizza dominos pepperoni", "pizza dominos mozzarella"))
                    }
                })
    }
}
