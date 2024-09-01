package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.*;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ParameterMapperTest {

    static Stream<Arguments> mappedTypes() {
        return Stream.of(mapType(null, null), mapType("test", "test"), mapType(1, 1), mapType(1L, 1L),
                mapType(1.0, 1.0), mapType(1.0f, 1.0f), mapType(true, true), mapType(false, false),
                mapType(LocalDate.of(2024, 9, 1), "2024-09-01"),
                mapType(Instant.parse("2007-12-03T10:15:30.00Z"), "2007-12-03 10:15:30.000"),
                mapType(LocalDateTime.parse("2007-12-03T10:15:30"), "2007-12-03 10:15:30.000"));
    }

    static Arguments mapType(final Object input, final Object expected) {
        return Arguments.of(input, expected);
    }

    @ParameterizedTest
    @MethodSource("mappedTypes")
    void map(final Object input, final Object expected) {
        final Object actual = ParameterMapper.create().map(input);
        assertThat(actual).isEqualTo(expected);
    }
}
