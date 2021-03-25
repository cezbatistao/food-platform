package com.food.review.lang

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.generator.FieldReflectionArbitraryGenerator
import com.navercorp.fixturemonkey.validator.ArbitraryValidator
import spock.lang.Shared
import spock.lang.Specification

class UnitSpec extends Specification {

    @Shared
    FixtureMonkey fixture

    def setupSpec() {
        this.fixture = FixtureMonkey.builder()
                .validator(new ArbitraryValidator() {
                    @Override
                    void validate(Object arbitrary) {
                    }
                })
                .build();
    }
}
