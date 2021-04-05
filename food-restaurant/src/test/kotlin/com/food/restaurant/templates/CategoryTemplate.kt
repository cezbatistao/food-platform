package com.food.restaurant.templates

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.food.restaurant.domain.Category
import java.util.*

class CategoryTemplate: TemplateLoader {

    override fun load() {
        Fixture.of(Category::class.java)
                .addTemplate("category pizza", object : Rule() {
                    init {
                        add("id", 1L)
                        add("uuid", UUID.fromString("0eda194c-827c-4254-ada8-214115310fc6"))
                        add("code", "pizza")
                        add("description", "Pizzaria")
                    }
                })
                .addTemplate("category hamburguer", object : Rule() {
                    init {
                        add("id", 2L)
                        add("uuid", UUID.fromString("cc0cd1b9-4217-498d-8da8-661de03b86e5"))
                        add("code", "hamburguer")
                        add("description", "Hambúrguer")
                    }
                })
                .addTemplate("category vegetariana", object : Rule() {
                    init {
                        add("id", 3L)
                        add("uuid", UUID.fromString("67e92973-f1ff-4cc3-b2c0-6485939ae442"))
                        add("code", "vegetariana")
                        add("description", "Vegetariana")
                    }
                })
    }
}
