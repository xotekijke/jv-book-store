package com.example.jvbookstore.mapper;

import com.example.jvbookstore.config.MapperConfig;
import com.example.jvbookstore.dto.category.CategoryDto;
import com.example.jvbookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "deleted", ignore = true)
    Category toEntity(CategoryDto categoryDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateCategoryFromDto(CategoryDto categoryDto, @MappingTarget Category category);
}
