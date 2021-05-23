package spodlivoi.database.enums.converter;

import spodlivoi.database.enums.Copypaste;

import javax.persistence.AttributeConverter;

public class CopypasteConverter implements AttributeConverter<Copypaste, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Copypaste attribute) {
        return attribute.ordinal();
    }

    @Override
    public Copypaste convertToEntityAttribute(Integer dbData) {
        return Copypaste.values()[dbData - 1];
    }
}
