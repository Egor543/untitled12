import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class test {

    @Test
    public void testAdd(){
        int a = 2;
        int b = 3;
        int expResult = 5;
        int result = Utils.add(a,b);
        assertEquals(expResult, result);
    }
}
