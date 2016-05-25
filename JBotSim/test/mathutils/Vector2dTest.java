/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mathutils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gus
 */
public class Vector2dTest {
    
    public Vector2dTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of length method, of class Vector2d.
     */
    @Test
    public void testLength() {
        System.out.println("length");
        Vector2d instance = new Vector2d();
        double expResult = 0.0;
        double result = instance.length();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of lengthSquared method, of class Vector2d.
     */
    @Test
    public void testLengthSquared() {
        System.out.println("lengthSquared");
        Vector2d instance = new Vector2d();
        double expResult = 0.0;
        double result = instance.lengthSquared();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rotate method, of class Vector2d.
     */
    @Test
    public void testRotate() {
        System.out.println("rotate");
        double angle = 0.0;
        Vector2d instance = new Vector2d();
        instance.rotate(angle);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dVec2Perpendicular method, of class Vector2d.
     */
    @Test
    public void testDVec2Perpendicular() {
        System.out.println("dVec2Perpendicular");
        Vector2d instance = new Vector2d();
        instance.dVec2Perpendicular();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sub method, of class Vector2d.
     */
    @Test
    public void testSub() {
        System.out.println("sub");
        Vector2d B = null;
        Vector2d instance = new Vector2d();
        instance.sub(B);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of subFrom method, of class Vector2d.
     */
    @Test
    public void testSubFrom() {
        System.out.println("subFrom");
        Vector2d B = null;
        Vector2d instance = new Vector2d();
        instance.subFrom(B);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class Vector2d.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        Vector2d v1 = null;
        Vector2d instance = new Vector2d();
        instance.add(v1);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dot method, of class Vector2d.
     */
    @Test
    public void testDot() {
        System.out.println("dot");
        Vector2d v1 = null;
        Vector2d instance = new Vector2d();
        double expResult = 0.0;
        double result = instance.dot(v1);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAngle method, of class Vector2d.
     */
    @Test
    public void testGetAngle() {
        System.out.println("getAngle");
        Vector2d instance = new Vector2d();
        double expResult = 0.0;
        double result = instance.getAngle();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSignedAngle method, of class Vector2d.
     */
    @Test
    public void testGetSignedAngle() {
        System.out.println("getSignedAngle");
        
        Vector2d a;
        Vector2d b;
        double expResult;
        double result; 
        
        //
        a = new Vector2d(-1, 1);
        b = new Vector2d(-1,-1);
        expResult = Math.PI/2;
        result = a.getSignedAngle(b);
        assertEquals(expResult, result, 0.0001);
        
        //
        a = new Vector2d(1, 0);
        b = new Vector2d(0,1);
        expResult = Math.PI/2;
        result = a.getSignedAngle(b);
        assertEquals(expResult, result, 0.0001);
        
        
        //
        a = new Vector2d(1, 0);
        b = new Vector2d(0,-1);
        expResult = -Math.PI/2;
        result = a.getSignedAngle(b);
        assertEquals(expResult, result, 0.0001);
        
        
        //
        a = new Vector2d(1, 0);
        b = new Vector2d(-1,0);
        expResult = Math.PI;
        result = a.getSignedAngle(b);
        assertEquals(expResult, result, 0.0001);
        
        
        //
        a = new Vector2d(1, 0);
        b = new Vector2d(1,1);
        expResult = Math.PI/4;
        result = a.getSignedAngle(b);
        assertEquals(expResult, result, 0.0001);
        
        
        //
        a = new Vector2d(1, 0);
        b = new Vector2d(1,-1);
        expResult = -Math.PI/4;
        result = a.getSignedAngle(b);
        assertEquals(expResult, result, 0.0001);
        
        
        //
        a = new Vector2d(0, 1);
        b = new Vector2d(-1,1);
        expResult = Math.PI/4;
        result = a.getSignedAngle(b);
        assertEquals(expResult, result, 0.0001);
        
        
        //
        a = new Vector2d(0, -1);
        b = new Vector2d(-1,-1);
        expResult = -Math.PI/4;
        result = a.getSignedAngle(b);
        assertEquals(expResult, result, 0.0001);
        
        
        
        
        System.out.println("   Success!");
        
        
    }

    /**
     * Test of angle method, of class Vector2d.
     */
    @Test
    public void testAngle() {
        System.out.println("angle");
        Vector2d v1 = null;
        Vector2d instance = new Vector2d();
        double expResult = 0.0;
        double result = instance.angle(v1);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of negate method, of class Vector2d.
     */
    @Test
    public void testNegate() {
        System.out.println("negate");
        Vector2d instance = new Vector2d();
        instance.negate();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLength method, of class Vector2d.
     */
    @Test
    public void testSetLength() {
        System.out.println("setLength");
        double newLength = 0.0;
        Vector2d instance = new Vector2d();
        instance.setLength(newLength);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of distanceTo method, of class Vector2d.
     */
    @Test
    public void testDistanceTo() {
        System.out.println("distanceTo");
        Vector2d position = null;
        Vector2d instance = new Vector2d();
        double expResult = 0.0;
        double result = instance.distanceTo(position);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Vector2d.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Vector2d instance = new Vector2d();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
