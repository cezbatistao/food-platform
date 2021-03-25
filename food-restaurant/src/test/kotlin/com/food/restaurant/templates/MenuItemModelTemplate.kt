package com.food.restaurant.templates

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.food.restaurant.gateway.database.model.MenuItemModel
import java.math.BigDecimal

class MenuItemModelTemplate: TemplateLoader {

    override fun load() {
        Fixture.of(MenuItemModel::class.java)
                .addTemplate("pizza hut pepperoni", object : Rule() {
                    init {
                        add("id", 1L)
                        add("uuid", "743b55f8-9543-11eb-a8b3-0242ac130003")
                        add("name", "Pepperoni")
                        add("description", "Muitas fatias de pepperoni (salame especial condimentado com páprica) servidas sobre mussarela e de molho de tomate.")
                        add("value", BigDecimal("33.99"))
                    }
                })
                .addTemplate("pizza hut meat", object : Rule() {
                    init {
                        add("id", 2L)
                        add("uuid", "773712b0-9543-11eb-a8b3-0242ac130003")
                        add("name", "Meat")
                        add("description", "Mussarela Pizza Hut, pepperoni, presunto, carnes bovinas e suínas cobertas por bacon.")
                        add("value", BigDecimal("34.99"))
                    }
                })
                .addTemplate("pizza hut supreme", object : Rule() {
                    init {
                        add("id", 3L)
                        add("uuid", "7d35de8a-9543-11eb-a8b3-0242ac130003")
                        add("name", "Supreme")
                        add("description", "Combinação de molho de tomate, pepperoni, cebola, pimentão, champignon, seleção de carnes bovina e suína e mussarela.")
                        add("value", BigDecimal("35.99"))
                    }
                })
                .addTemplate("pizza braz napolitan", object : Rule() {
                    init {
                        add("id", 4L)
                        add("uuid", "8dc970ae-9543-11eb-a8b3-0242ac130003")
                        add("name", "NAPOLITANA")
                        add("description", "Molho de tomate, alho cru fatiado, parmesão e folhas frescas de manjericão")
                        add("value", BigDecimal("43.99"))
                    }
                })
                .addTemplate("pizza braz pepperoni", object : Rule() {
                    init {
                        add("id", 5L)
                        add("uuid", "81af3f42-9543-11eb-a8b3-0242ac130003")
                        add("name", "CALABRESA")
                        add("description", "Calabresa artesanal em fatias, cebola e azeitonas pretas")
                        add("value", BigDecimal("41.99"))
                    }
                })
                .addTemplate("pizza dominos pepperoni", object : Rule() {
                    init {
                        add("id", 6L)
                        add("uuid", "843bfe62-9543-11eb-a8b3-0242ac130003")
                        add("name", "Pepperoni")
                        add("description", "Mussarela, oregano e pepperoni.")
                        add("value", BigDecimal("33.99"))
                    }
                })
                .addTemplate("pizza dominos mozzarella", object : Rule() {
                    init {
                        add("id", 7L)
                        add("uuid", "88e3812e-9543-11eb-a8b3-0242ac130003")
                        add("name", "Mussarela")
                        add("description", "Queijo mussarela e oregano.")
                        add("value", BigDecimal("31.99"))
                    }
                })
    }
}
