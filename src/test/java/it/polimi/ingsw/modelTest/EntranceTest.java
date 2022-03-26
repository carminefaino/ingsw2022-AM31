package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.Entrance;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EntranceTest {
    private Entrance entrance = new Entrance();

    @Test
    public void addAndGetTest(){
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.BLUE));
        entrance.addStudent(StudsAndProfsColor.BLUE);
        entrance.addStudent(StudsAndProfsColor.BLUE);
        entrance.addStudent(StudsAndProfsColor.BLUE);
        entrance.addStudent(StudsAndProfsColor.BLUE);
        entrance.addStudent(StudsAndProfsColor.YELLOW);
        entrance.addStudent(StudsAndProfsColor.GREEN);
        entrance.addStudent(StudsAndProfsColor.PINK);
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(4, entrance.getStudentsByColor(StudsAndProfsColor.BLUE));
    }


    @Test
    public void removeAndGetTest(){
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.BLUE));
        entrance.addStudent(StudsAndProfsColor.BLUE);
        entrance.addStudent(StudsAndProfsColor.BLUE);
        entrance.addStudent(StudsAndProfsColor.BLUE);
        entrance.addStudent(StudsAndProfsColor.BLUE);
        entrance.addStudent(StudsAndProfsColor.YELLOW);
        entrance.addStudent(StudsAndProfsColor.GREEN);
        entrance.addStudent(StudsAndProfsColor.PINK);
        entrance.removeStudent(StudsAndProfsColor.BLUE);
        entrance.removeStudent(StudsAndProfsColor.BLUE);
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(2, entrance.getStudentsByColor(StudsAndProfsColor.BLUE));

    }

}
