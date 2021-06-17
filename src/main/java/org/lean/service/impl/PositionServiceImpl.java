package org.lean.service.impl;

import org.lean.model.Position;
import org.lean.persistence.PositionRepository;
import org.lean.service.PositionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Position findPosition(String positionName) {
        return positionRepository.findByName(positionName)
                                 .orElseGet(() -> positionRepository.save(Position.builder().name(positionName).build()));
    }
}
