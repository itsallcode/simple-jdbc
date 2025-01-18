package org.itsallcode.jdbc.metadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.itsallcode.jdbc.metadata.ColumnMetaData.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ColumnMetaDataTest {

    @ParameterizedTest
    @CsvSource({ "0,NO_NULLS", "1,NULLABLE", "2,NULLABLE_UNKNOWN" })
    void nullability(final int value, final Nullability expected) {
        assertThat(Nullability.valueOf(value)).isEqualTo(expected);
    }

    @Test
    void nullabilityInvalid() {
        assertThatThrownBy(() -> Nullability.valueOf(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown value 3 for nullability");
    }

    @ParameterizedTest
    @CsvSource({ "YES,NO_NULLS", "NO,NULLABLE", "'',NULLABLE_UNKNOWN" })
    void isoNullability(final String value, final ISONullability expected) {
        assertThat(ISONullability.valueOfNullability(value)).isEqualTo(expected);
    }

    @Test
    void isoNullabilityInvalid() {
        assertThatThrownBy(() -> ISONullability.valueOfNullability("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown value 'unknown' for ISO nullability");
    }

    @ParameterizedTest
    @CsvSource({ "YES,AUTO_INCREMENT", "NO,NO_AUTO_INCREMENT", "'',UNKNOWN" })
    void autoIncrement(final String value, final AutoIncrement expected) {
        assertThat(AutoIncrement.valueOfAutoIncrement(value)).isEqualTo(expected);
    }

    @Test
    void autoIncrementInvalid() {
        assertThatThrownBy(() -> AutoIncrement.valueOfAutoIncrement("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown value 'unknown' for auto increment");
    }

    @ParameterizedTest
    @CsvSource({ "YES,GENERATED", "NO,NOT_GENERATED", "'',UNKNOWN" })
    void generated(final String value, final Generated expected) {
        assertThat(Generated.valueOfGenerated(value)).isEqualTo(expected);
    }

    @Test
    void generatedUnknown() {
        assertThatThrownBy(() -> Generated.valueOfGenerated("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown value 'unknown' for column generated");
    }
}
