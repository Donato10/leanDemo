package org.lean.transform.mapper;

public interface OutboundMapper<DTO, ENTITY> {
    DTO toDto(ENTITY entity);
}
