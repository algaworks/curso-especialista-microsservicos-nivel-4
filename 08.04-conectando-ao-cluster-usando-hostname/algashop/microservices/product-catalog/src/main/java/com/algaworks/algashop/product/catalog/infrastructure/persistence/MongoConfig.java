package com.algaworks.algashop.product.catalog.infrastructure.persistence;

import org.bson.UuidRepresentation;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.mongodb.autoconfigure.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClientSettingsBuilderCustomizer uuidCustomizer() {
        return builder -> builder.uuidRepresentation(UuidRepresentation.STANDARD);
    }

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
                List.of(new OffsetDateTimeReadConverter(), new OffsetDateTimeWriteConverter())
        );
    }

    public static class OffsetDateTimeReadConverter implements Converter<Date, OffsetDateTime> {
        @Override
        public @Nullable OffsetDateTime convert(Date source) {
            return source.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }
    }

    public static class OffsetDateTimeWriteConverter implements Converter<OffsetDateTime, Date> {
        @Override
        public @Nullable Date convert(OffsetDateTime source) {
            return Date.from(source.toInstant());
        }
    }

}
