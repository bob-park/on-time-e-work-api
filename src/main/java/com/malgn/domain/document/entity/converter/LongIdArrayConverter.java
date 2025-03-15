package com.malgn.domain.document.entity.converter;

import java.util.List;

import jakarta.persistence.Converter;

import com.malgn.common.entity.converter.JsonConverter;

@Converter
public class LongIdArrayConverter extends JsonConverter<List<Long>> {


}
