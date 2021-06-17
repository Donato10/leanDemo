package org.lean.transform.mapper;

public interface InboundMapper<ENTITY, DTO> {
    ENTITY toEntity(DTO dto);
}
