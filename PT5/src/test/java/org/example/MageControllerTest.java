package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class MageControllerTest {
    private MageController mageController;
    private MageRepository mageRepository;

    @BeforeEach
    void initialization() {
        mageRepository = mock(MageRepository.class);
        mageController = new MageController(mageRepository);
        mageRepository.initialize(new Mage[]{new Mage("m1", 100)});
    }

    @Test
    void testDeleteSuccess() {
        assertThat(mageController.delete("m1")).isEqualTo("done");
    }

    @Test
    void testDeleteFail() {
        doThrow(IllegalArgumentException.class).when(mageRepository).delete(anyString());
        assertThat(mageController.delete("NOTEXISTINGNAME")).isEqualTo("not found");
    }

    @Test
    void testFindFail(){
        Mage mage = new Mage("m3", 10);
        when(mageRepository.find("m3")).thenReturn(Optional.empty());
        assertThat(mageController.find("m3")).isEqualTo("not found");
    }

    @Test
    void testFindSuccess(){
        Mage mage = new Mage("m3", 10);
        when(mageRepository.find(mage.getName())).thenReturn(Optional.of(mage));
        assertThat(mageController.find("m3")).isEqualTo(mage.toString());

    }


    @Test
    void testSaveSuccess(){
        assertThat(mageController.save("m3", "10")).isEqualTo("done");
    }

    @Test
    void testSaveFail(){
        Mage mage = new Mage("m3", 10);
        doThrow(IllegalArgumentException.class).when(mageRepository).save(any());
        assertThat(mageController.save(mage.getName(), String.valueOf(mage.getLevel()))).isEqualTo("bad request");

    }

}

