package org.example.weblab3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PointResultTest {

    @Test
    public void testPointInFirstQuarter() {
        PointResult point = new PointResult();
        point.setX(1.0);
        point.setY(1.0);
        point.setR(2.0);
        
        assertTrue(point.checkHit(), "Точка (1,1) должна попадать в область при R=2");
    }
    
    @Test
    public void testPointInSecondQuarter() {
        PointResult point = new PointResult();
        point.setX(-1.0);
        point.setY(1.0);
        point.setR(2.0);
        
        assertFalse(point.checkHit(), "Точка (-1,1) не должна попадать в область при R=2");
    }
    
    @Test
    public void testPointInThirdQuarter() {
        PointResult point = new PointResult();
        point.setX(-1.0);
        point.setY(-1.0);
        point.setR(2.0);
        
        assertTrue(point.checkHit(), "Точка (-1,-1) должна попадать в область при R=2");
    }
    
    @Test
    public void testPointInFourthQuarter() {
        PointResult point = new PointResult();
        point.setX(1.0);
        point.setY(-1.0);
        point.setR(2.0);
        
        assertFalse(point.checkHit(), "Точка (1,-1) не должна попадать в область при R=2");
    }
    
    @Test
    public void testPointOnBorder() {
        PointResult point = new PointResult();
        point.setX(0.0);
        point.setY(0.0);
        point.setR(2.0);
        
        assertTrue(point.checkHit(), "Точка (0,0) должна попадать в область при R=2");
    }
} 