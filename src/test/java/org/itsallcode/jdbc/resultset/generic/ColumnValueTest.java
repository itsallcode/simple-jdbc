package org.itsallcode.jdbc.resultset.generic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class ColumnValueTest {
    @Test
    void testToString() {
        ToStringVerifier.forClass(ColumnValue.class).verify();
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ColumnValue.class).verify();
    }

    @Test
    void getString() {
        assertThat(new ColumnValue(null, "value").getString()).isEqualTo("value");
    }

    @Test
    void getValue() {
        assertThat(new ColumnValue(null, 1).getValue(Integer.class)).isEqualTo(1);
    }

    @Test
    void getValueWrongType() {
        final ColumnValue value = new ColumnValue(null, 1);
        assertThatThrownBy(() -> value.getValue(String.class))
                .hasMessage("Cannot cast java.lang.Integer to java.lang.String");
    }
}
