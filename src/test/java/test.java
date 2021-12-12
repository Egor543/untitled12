import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class test {

    @Test
    void testTile() {
        Tile game = new Tile(0, 0, 0);
        game.setCanCombine(false);
        assertEquals(game.isCanCombine(), false);
    }

    @Test
    void testValue() {
        Tile game = new Tile(0, 0, 0);
        game.setValue(0);
        assertEquals(game.getValue(), 0);
    }

    @Test
    void testX() {
        Tile game = new Tile(0, 0, 0);
        game.setX(0);
        assertEquals(game.getX(), 0);
    }

    @Test
    void testY() {
        Tile game = new Tile(0, 0, 0);
        game.setY(0);
        assertEquals(game.getY(), 0);
    }

}
