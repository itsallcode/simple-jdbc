package org.itsallcode.jdbc.metadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.DatabaseMetaData;

import org.itsallcode.jdbc.metadata.ColumnMetaData.*;
import org.junit.jupiter.api.Test;

class ColumnMetaDataTest {

    @Test
    void nullability() {
        assertThat(Nullability.valueOf(DatabaseMetaData.columnNoNulls)).isEqualTo(Nullability.NO_NULLS);
    }

    @Test
    void nullabilityInvalid() {
        assertThatThrownBy(() -> Nullability.valueOf(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown value 3 for nullability");
    }

    @Test
    void isoNullability() {
        assertThat(ISONullability.valueOfNullability("NO")).isEqualTo(ISONullability.NULLABLE);
    }

    @Test
    void isoNullabilityInvalid() {
        assertThatThrownBy(() -> ISONullability.valueOfNullability("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown value 'unknown' for ISO nullability");
    }

    @Test
    void autoIncrement() {
        assertThat(AutoIncrement.valueOfAutoIncrement("NO")).isEqualTo(AutoIncrement.NO_AUTO_INCREMENT);
    }

    @Test
    void autoIncrementInvalid() {
        assertThatThrownBy(() -> AutoIncrement.valueOfAutoIncrement("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown value 'unknown' for auto increment");
    }

    @Test
    void generated() {
        assertThat(Generated.valueOfGenerated("NO")).isEqualTo(Generated.NOT_GENERATED);
    }

    @Test
    void generatedUnknown() {
        assertThatThrownBy(() -> Generated.valueOfGenerated("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown value 'unknown' for column generated");
    }
}
