import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class test {

    @Test
    public void testTile() {
        Tile game = new Tile(0, 0, 0);
        game.setCanCombine(false);
        assertEquals(false, game.isCanCombine());
    }

    @Test
    public void testValue() {
        Tile game = new Tile(0, 0, 0);
        game.setValue(0);
        assertEquals(0, game.getValue());
    }

    @Test
    public void testX() {
        Tile game = new Tile(0, 0, 0);
        game.setX(0);
        assertEquals(0, game.getX());
    }

    @Test
    public  void testY() {
        Tile game = new Tile(0, 0, 0);
        game.setY(0);
        assertEquals(0, game.getY());
    }

    @Test
    public void testAdd(){
        int a = 2;
        int b = 3;
        int expResult = 5;
        int result = Utils.add(a,b);
        assertEquals(expResult, result);
    }

}
