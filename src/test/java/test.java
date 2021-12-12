import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class test {

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
        assertEquals(0, game.getValue());
    }

    @Test
    void testX() {
        Tile game = new Tile(0, 0, 0);
        game.setX(0);
        assertEquals(0, game.getX());
    }

    @Test
    void testY() {
        Tile game = new Tile(0, 0, 0);
        game.setY(0);
        assertEquals(0, game.getY());
    }

    @Test
    void testAdd(){
        int a = 2;
        int b = 3;
        int expResult = 5;
        int result = Game.MyMath.add(a,b);
        assertEquals(expResult, result);
    }

}
