package org.lean.service.impl;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lean.model.Position;
import org.lean.persistence.PositionRepository;
import org.lean.service.PositionService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PositionServiceImplTest {

    @Mock
    PositionRepository positionRepository;
    PositionService positionService;

    @BeforeEach
    void setup() {
        positionService = new PositionServiceImpl(positionRepository);
    }

    @Test
    void test_findPosition_existingName() {
        //GIVEN: An existing position
        var existingName = "some-name";
        var existingPosition = Position.builder()
                                       .name(existingName)
                                       .build();
        when(positionRepository.findByName(existingName))
            .thenReturn(Optional.of(existingPosition));

        //WHEN: trying to find it using its name
        var resultingPosition = positionService.findPosition(existingName);

        //THEN: the existing position is requested
        assertEquals(existingPosition, resultingPosition);
    }

    @Test
    void test_findPosition_nonExistingName() {
        //GIVEN: An existing position
        var nonExistingName = "some-non-existing-name";
        when(positionRepository.findByName(nonExistingName))
            .thenReturn(Optional.empty());

        //WHEN: trying to find it using its name
        positionService.findPosition(nonExistingName);

        //THEN: a new position is saved
        verify(positionRepository).save(Position.builder().name(nonExistingName).build());
    }
}