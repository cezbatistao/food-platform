package com.food.restaurant.templates

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.food.restaurant.domain.Category

class CategoryTemplate: TemplateLoader {

    override fun load() {
        Fixture.of(Category::class.java)
                .addTemplate("category pizza", object : Rule() {
                    init {
                        add("id", 1L)
                        add("code", "pizza")
                        add("description", "Pizzaria")
                    }
                })
                .addTemplate("category hamburguer", object : Rule() {
                    init {
                        add("id", 2L)
                        add("code", "hamburguer")
                        add("description", "Hambúrguer")
                    }
                })
                .addTemplate("category vegetariana", object : Rule() {
                    init {
                        add("id", 3L)
                        add("code", "vegetariana")
                        add("description", "Vegetariana")
                    }
                })
    }
}
