package spodlivoi.database.enums.converter;

import spodlivoi.database.enums.Gender;

import javax.persistence.AttributeConverter;

public class GenderConverter implements AttributeConverter<Gender, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Gender attribute) {
        if (attribute == null) {
            return 0;
        }
        return attribute.ordinal();
    }

    @Override
    public Gender convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return Gender.MALE;
        }
        return Gender.values()[dbData];
    }
}
