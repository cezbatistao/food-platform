package com.food.review.gateway

import com.food.review.domain.exception.EntityNotFoundException
import com.food.review.domain.ReviewPageable
import com.food.review.gateway.mongo.entity.OrderItemMongo
import com.food.review.gateway.mongo.entity.RestaurantMongo
import com.food.review.gateway.mongo.entity.ReviewMongo
import com.food.review.lang.MongoRepositoryComponentTest
import com.food.review.lang.fixture.ReviewFixture
import java.util.UUID
import java.util.stream.Collectors
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowableOfType
import org.instancio.Instancio
import org.instancio.Select.field
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ReviewGatewayComponentTest: MongoRepositoryComponentTest() {

    @Autowired
    lateinit var reviewGateway: ReviewGateway

    val modelReviewMongo = Instancio.of(ReviewMongo::class.java)
        .set(field("id"), null)
        .generate(field("uuid")) { gen -> gen.text().uuid() }
        .generate(field("orderUuid")) { gen -> gen.text().uuid() }
        .generate(field(RestaurantMongo::class.java, "uuid")) { gen -> gen.text().uuid() }
        .generate(field(OrderItemMongo::class.java, "uuid")) { gen -> gen.text().uuid() }
        .generate(field(OrderItemMongo::class.java, "menuItemUuid")) { gen -> gen.text().uuid() }
        .generate(field("items")) { gen -> gen.collection<OrderItemMongo>().minSize(1).maxSize(3) }
        .set(field("text"), "")
        .set(field("createdAt"), null)
        .set(field("updatedAt"), null)
        .toModel()

    @Test
    fun WhenSave_thenReviewSavedAtDatabase() {
        //given
        val reviewToSave = ReviewFixture.getValidCreated()

        //when
        val reviewSaved = this.reviewGateway.save(reviewToSave)

        //then
        val reviewFoundByIdOptional = this.reviewRepository.findById(reviewSaved.id!!)
        assertThat(reviewFoundByIdOptional).isPresent

        //and
        val reviewFoundById = reviewFoundByIdOptional.get()
        assertThat(reviewFoundById.id).isNotNull
        assertThat(reviewFoundById.uuid).isEqualTo(reviewToSave.uuid.toString())
        assertThat(reviewFoundById.status).isEqualTo(reviewToSave.status)
        assertThat(reviewFoundById.uuid).isEqualTo(reviewToSave.uuid.toString())
        assertThat(reviewFoundById.userUuid).isEqualTo(reviewToSave.userUuid.toString())
        assertThat(reviewFoundById.restaurant!!.uuid).isEqualTo(reviewToSave.restaurant.uuid.toString())
        assertThat(reviewFoundById.restaurant!!.name).isEqualTo(reviewToSave.restaurant.name)
        assertThat(reviewFoundById.text).isNullOrEmpty()
        assertThat(reviewFoundById.createdAt).isNotNull
        assertThat(reviewFoundById.updatedAt).isNotNull

        //and
        assertThat(reviewFoundById.items).hasSize(reviewToSave.items.size)
        reviewFoundById.items!!.forEach { orderItemFound->
            val orderItemToSave = reviewToSave.items.find { it.uuid.toString() == orderItemFound.uuid }
            assertThat(orderItemToSave).isNotNull
            assertThat(orderItemFound.menuItemUuid).isEqualTo(orderItemToSave!!.menuItemUuid.toString())
            assertThat(orderItemFound.name).isEqualTo(orderItemToSave.name)
        }
    }

    @Test
    fun WhenFindByUserUuidWithUserUuidAndPageagle_thenResultOnePage() {
        //given
        val userUuid = UUID.fromString("dbb9c2bd-abde-48a3-891a-6229fc9b7c21")

        val reviewsMongo = Instancio.of(modelReviewMongo)
            .set(field("userUuid"), userUuid.toString())
            .stream()
            .limit(3)
            .collect(Collectors.toList())

        val reviewFromAnotherUserMongo = Instancio.of(modelReviewMongo)
            .generate(field("userUuid")) { gen -> gen.text().uuid() }
            .create()
        reviewsMongo.add(reviewFromAnotherUserMongo)

        this.reviewRepository.saveAll(reviewsMongo)

        val reviewPageable = ReviewPageable(userUuid, null, null, 0, 2)

        //when
        val reviewPage = this.reviewGateway.findByUserUuid(reviewPageable)

        //then
        assertThat(reviewPage).isNotNull
        assertThat(reviewPage.totalElements).isEqualTo(3)
        assertThat(reviewPage.totalPages).isEqualTo(2)
        assertThat(reviewPage.size).isEqualTo(2)
        assertThat(reviewPage.number).isEqualTo(0)
        assertThat(reviewPage.content).hasSize(2)

        //and
        reviewPage.content.forEach {review ->
            val reviewMongoFound = reviewsMongo.find { reviewMongo -> reviewMongo.uuid == review.uuid.toString() }
            assertThat(reviewMongoFound).isNotNull

            assertThat(review.id).isEqualTo(reviewMongoFound!!.id)
            assertThat(review.status).isEqualTo(reviewMongoFound.status)
            assertThat(review.restaurant.uuid.toString()).isEqualTo(reviewMongoFound.restaurant!!.uuid)
            assertThat(review.restaurant.name).isEqualTo(reviewMongoFound.restaurant!!.name)
            assertThat(review.orderUuid.toString()).isEqualTo(reviewMongoFound.orderUuid)
            assertThat(review.userUuid.toString()).isEqualTo(reviewMongoFound.userUuid)
            assertThat(review.text).isEqualTo(reviewMongoFound.text)
            assertThat(review.createdAt).isEqualToIgnoringNanos(reviewMongoFound.createdAt)
            assertThat(review.updatedAt).isEqualToIgnoringNanos(reviewMongoFound.updatedAt)

            assertThat(review.items).hasSize(reviewMongoFound.items!!.size)

            review.items.forEach {orderItem ->
                val orderItemMongo = reviewMongoFound.items!!.find { orderItemMongo -> orderItemMongo.uuid == orderItem.uuid.toString() }

                assertThat(orderItem.menuItemUuid.toString()).isEqualTo(orderItemMongo!!.menuItemUuid)
                assertThat(orderItem.name).isEqualTo(orderItemMongo.name)
            }
        }
    }

    @Test
    fun WhenFindByUserUuidWithUserUuidAndRestaurantUuidAndStatus_thenResultOnePageWithOneRegister() {
        //given
        val userUuid = UUID.fromString("dbb9c2bd-abde-48a3-891a-6229fc9b7c21")

        val reviewsMongo = Instancio.of(modelReviewMongo)
            .set(field("userUuid"), userUuid.toString())
            .stream()
            .limit(3)
            .collect(Collectors.toList())

        val reviewFromAnotherUserMongo = Instancio.of(modelReviewMongo)
            .generate(field("userUuid")) { gen -> gen.text().uuid() }
            .create()
        reviewsMongo.add(reviewFromAnotherUserMongo)

        this.reviewRepository.saveAll(reviewsMongo)

        val reviewPageable = ReviewPageable(
            UUID.fromString(reviewFromAnotherUserMongo.userUuid),
            UUID.fromString(reviewFromAnotherUserMongo.restaurant!!.uuid),
            reviewFromAnotherUserMongo.status,
            0,
            10)

        //when
        val reviewPage = this.reviewGateway.findByUserUuid(reviewPageable)

        //then
        assertThat(reviewPage).isNotNull
        assertThat(reviewPage.totalElements).isEqualTo(1)
        assertThat(reviewPage.totalPages).isEqualTo(1)
        assertThat(reviewPage.size).isEqualTo(10)
        assertThat(reviewPage.number).isEqualTo(0)
        assertThat(reviewPage.content).hasSize(1)

        //and
        val review = reviewPage.content[0]

        assertThat(review.id).isEqualTo(reviewFromAnotherUserMongo!!.id)
        assertThat(review.status).isEqualTo(reviewFromAnotherUserMongo.status)
        assertThat(review.restaurant.uuid.toString()).isEqualTo(reviewFromAnotherUserMongo.restaurant!!.uuid)
        assertThat(review.restaurant.name).isEqualTo(reviewFromAnotherUserMongo.restaurant!!.name)
        assertThat(review.orderUuid.toString()).isEqualTo(reviewFromAnotherUserMongo.orderUuid)
        assertThat(review.userUuid.toString()).isEqualTo(reviewFromAnotherUserMongo.userUuid)
        assertThat(review.text).isEqualTo(reviewFromAnotherUserMongo.text)
        assertThat(review.createdAt).isEqualToIgnoringNanos(reviewFromAnotherUserMongo.createdAt)
        assertThat(review.updatedAt).isEqualToIgnoringNanos(reviewFromAnotherUserMongo.updatedAt)

        assertThat(review.items).hasSize(reviewFromAnotherUserMongo.items!!.size)

        review.items.forEach {orderItem ->
            val orderItemMongo = reviewFromAnotherUserMongo.items!!.find { orderItemMongo -> orderItemMongo.uuid == orderItem.uuid.toString() }

            assertThat(orderItem.menuItemUuid.toString()).isEqualTo(orderItemMongo!!.menuItemUuid)
            assertThat(orderItem.name).isEqualTo(orderItemMongo.name)
        }
    }

    @Test
    fun WhenFindByUserUuidWithUnregisteredUserUuidAndPageagle_thenResultEmpty() {
        //given
        val userUuid = UUID.fromString("dbb9c2bd-abde-48a3-891a-6229fc9b7c21")

        val reviewsMongo = Instancio.of(modelReviewMongo)
            .set(field("userUuid"), userUuid.toString())
            .stream()
            .limit(3)
            .collect(Collectors.toList())

        val reviewFromAnotherUserMongo = Instancio.of(modelReviewMongo)
            .generate(field("userUuid")) { gen -> gen.text().uuid() }
            .create()
        reviewsMongo.add(reviewFromAnotherUserMongo)

        this.reviewRepository.saveAll(reviewsMongo)

        val reviewPageable = ReviewPageable(UUID.randomUUID(), null, null, 0, 2)

        //when
        val reviewPage = this.reviewGateway.findByUserUuid(reviewPageable)

        //then
        assertThat(reviewPage).isNotNull
        assertThat(reviewPage.totalElements).isEqualTo(0)
        assertThat(reviewPage.totalPages).isEqualTo(0)
        assertThat(reviewPage.size).isEqualTo(2)
        assertThat(reviewPage.number).isEqualTo(0)
        assertThat(reviewPage.content).hasSize(0)
    }

    @Test
    fun WhenFindByUserUuidAndUuid_thenReturnResult() {
        //given
        val userUuid = UUID.fromString("dbb9c2bd-abde-48a3-891a-6229fc9b7c21")

        val reviewsMongo = Instancio.of(modelReviewMongo)
            .set(field("userUuid"), userUuid.toString())
            .stream()
            .limit(3)
            .collect(Collectors.toList())

        val reviewFromAnotherUserMongo = Instancio.of(modelReviewMongo)
            .generate(field("userUuid")) { gen -> gen.text().uuid() }
            .create()
        reviewsMongo.add(reviewFromAnotherUserMongo)

        this.reviewRepository.saveAll(reviewsMongo)

        //when
        val review = this.reviewGateway.findByUserUuidAndUuid(
            UUID.fromString(reviewFromAnotherUserMongo.userUuid),
            UUID.fromString(reviewFromAnotherUserMongo.uuid))

        //then
        assertThat(review.id).isEqualTo(reviewFromAnotherUserMongo!!.id)
        assertThat(review.status).isEqualTo(reviewFromAnotherUserMongo.status)
        assertThat(review.restaurant.uuid.toString()).isEqualTo(reviewFromAnotherUserMongo.restaurant!!.uuid)
        assertThat(review.restaurant.name).isEqualTo(reviewFromAnotherUserMongo.restaurant!!.name)
        assertThat(review.orderUuid.toString()).isEqualTo(reviewFromAnotherUserMongo.orderUuid)
        assertThat(review.userUuid.toString()).isEqualTo(reviewFromAnotherUserMongo.userUuid)
        assertThat(review.text).isEqualTo(reviewFromAnotherUserMongo.text)
        assertThat(review.createdAt).isEqualToIgnoringNanos(reviewFromAnotherUserMongo.createdAt)
        assertThat(review.updatedAt).isEqualToIgnoringNanos(reviewFromAnotherUserMongo.updatedAt)

        assertThat(review.items).hasSize(reviewFromAnotherUserMongo.items!!.size)

        review.items.forEach {orderItem ->
            val orderItemMongo = reviewFromAnotherUserMongo.items!!.find { orderItemMongo -> orderItemMongo.uuid == orderItem.uuid.toString() }

            assertThat(orderItem.menuItemUuid.toString()).isEqualTo(orderItemMongo!!.menuItemUuid)
            assertThat(orderItem.name).isEqualTo(orderItemMongo.name)
        }
    }

    @Test
    fun WhenFindByUserUuidAndUuidWithUnregisteredData_thenReturnException() {
        //given
        val userUuid = UUID.fromString("dbb9c2bd-abde-48a3-891a-6229fc9b7c21")

        val reviewsMongo = Instancio.of(modelReviewMongo)
            .set(field("userUuid"), userUuid.toString())
            .stream()
            .limit(3)
            .collect(Collectors.toList())

        val reviewFromAnotherUserMongo = Instancio.of(modelReviewMongo)
            .generate(field("userUuid")) { gen -> gen.text().uuid() }
            .create()
        reviewsMongo.add(reviewFromAnotherUserMongo)

        this.reviewRepository.saveAll(reviewsMongo)

        //when
        val thrown = catchThrowableOfType(
            { this.reviewGateway.findByUserUuidAndUuid(UUID.randomUUID(), UUID.randomUUID()) },
            EntityNotFoundException::class.java
        )

        //then
        assertThat(thrown).isInstanceOf(EntityNotFoundException::class.java)
            .hasMessageContaining("Review doesn't exists")
        assertThat(thrown.code).isEqualTo("0001")
        assertThat(thrown.error).isEqualTo("entityNotFoundException")
    }
}
