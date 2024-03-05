package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MageRepositoryTest {
    MageRepository mageRepository;

    @BeforeEach
    void initialization() {
        mageRepository = new MageRepository();
        mageRepository.initialize(new Mage[]{new Mage("m1", 100)});
    }

    @Test
    void Should_ThrowIllegalArgumentException_When_DeletingNotExistingObject(){
        assertThrows(IllegalArgumentException.class, () -> {
            mageRepository.delete("NOTEXISTINGNAME");
        });
    }

    @Test
    void Should_ReturnEmptyOptional_When_ObjectIsNotPresent() {
        Optional<Mage> result = mageRepository.find("NOTEXISTINGNAME");
        assertThat(result).isEmpty();
    }

    //?
    @Test
    void Should_ReturnOptionalWithValue_When_ObjectIsPresent() {
        Optional<Mage> result = mageRepository.find("m1");
        assertThat(result.get().getName()).isEqualTo("m1");
        assertThat(result.get().getLevel()).isEqualTo(100);
    }

    @Test
    void Should_ThrowIllegalArgumentException_When_SavingObjectWithAlreadyExistingKey() {
        assertThrows(IllegalArgumentException.class, () -> mageRepository.save(new Mage("m1", 10)));
    }

    //

    @Test
    void Should_Save_When_SavingObjectWithUniqueKey(){
        Mage mage = new Mage("m2", 10);
        mageRepository.save(mage);
        Optional<Mage> result = mageRepository.find("m2");
        assertEquals(mage, result.get());
    }

}