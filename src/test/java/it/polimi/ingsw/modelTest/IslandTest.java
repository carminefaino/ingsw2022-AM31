package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.StudsAndProfsColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Check if the methods of Island class work
 */
class IslandTest {
    Island island = new Island(2);

    /**
     * Check that the students are properly added to an island
     */
    @Test
    void addStudent() {
        assertEquals(0, island.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(0, island.getStudentsByColor(StudsAndProfsColor.GREEN));
        island.addStudent(StudsAndProfsColor.RED);
        island.addStudent(StudsAndProfsColor.GREEN);
        island.addStudent(StudsAndProfsColor.GREEN);
        assertEquals(1, island.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(2, island.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, island.getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(0, island.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(0, island.getStudentsByColor(StudsAndProfsColor.YELLOW));

    }
}