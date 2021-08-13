package Coding.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayListTest {

    private ArrayList<String> list;

    @Before
    public void before() {
        list = new ArrayList<>();
    }

    @Test
    public void testSizeAfterInitWithDefaultConstruct() {
        String[] arr = getPrivateArrField(list);

        if (arr != null) {
            Assert.assertEquals(10, arr.length);
        }

        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testSizeAfterInitCollectionAsParamConstruct() {
        list = new ArrayList<>(List.of("Test", "Test1", "Test2"));

        Assert.assertEquals(10, getPrivateArrField(list).length);
        Assert.assertEquals(3, list.size());
    }

    @Test(expected = InvalidParameterException.class)
    public void testSizeAfterInitCollectionWithNullAsParamConstruct() {
        list = new ArrayList<>(null);
    }

    @Test
    public void testSizeAfterInitCollectionAsParamConstructTriggeringResizing() {
        list = new ArrayList<>(List.of("Test", "Test1", "Test2", "Test3", "Test4", "Test5", "Test6", "Test7", "Test8", "Test9", "Test10", "Test11" ));

        Assert.assertEquals(20, getPrivateArrField(list).length);
        Assert.assertEquals(12, list.size());
    }

    @Test
    public void testSizeAfterInitWithSizeParamConstruct() {
        list = new ArrayList<>(100);

        Assert.assertEquals(0, list.size());

        String[] arr = getPrivateArrField(list);

        if (arr != null) {
            Assert.assertEquals(100, arr.length);
        }
    }

    @Test(expected = InvalidParameterException.class)
    public void testSizeAfterInitWithNegativeSizeParamConstruct() {
        list = new ArrayList<>(-1);
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(list.isEmpty());

        list.add("1");

        Assert.assertFalse(list.isEmpty());
    }

    @Test
    public void testGetElement() {
        // initial size of 10, so everything populated with null 0 - 9
        Assert.assertNull(list.get(0));
        Assert.assertNull(list.get(9));

        list.add("1");

        Assert.assertEquals("1", list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetElementOutOfPositiveRange() {
        list.get(11);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetElementOutOfNegativeRange() {
        list.get(-1);
    }

    @Test
    public void testAddFunctionality() {
        Assert.assertEquals(0, list.size());

        String[] arr = getPrivateArrField(list);
        if (arr != null) {
            Assert.assertEquals(10, arr.length);
        }

        list.add("1");
        list.add("2");
        list.add("3");

        Assert.assertEquals(3, list.size());

        list.add("4");

        Assert.assertEquals(4, list.size());

        arr = getPrivateArrField(list);
        if (arr != null) {
            Assert.assertEquals(10, arr.length);
        }

        // Trigger resizing

        list.add("5");
        list.add("6");
        list.add("7");

        Assert.assertEquals(7, list.size());

        arr = getPrivateArrField(list);

        if (arr != null) {
            Assert.assertEquals(10, arr.length);
        }

        list.add("8"); // resizing triggered

        Assert.assertEquals(8, list.size());

        arr = getPrivateArrField(list);

        if (arr != null) {
            Assert.assertEquals(20, arr.length); // Must be increased with a factor of 2
        }

    }

    @Test
    public void testAddAllWithoutResizing() {
        Assert.assertEquals(0, list.size());

        list.addAll(List.of("1", "2", "3"));

        Assert.assertEquals(3, list.size());

        list.addAll(List.of("3", "4", "5"));

        Assert.assertEquals(6, list.size());
        Assert.assertEquals("1", list.get(0));
        Assert.assertEquals("2", list.get(1));
        Assert.assertEquals("3", list.get(2));
        Assert.assertEquals("3", list.get(3));
        Assert.assertEquals("4", list.get(4));
        Assert.assertEquals("5", list.get(5));
        Assert.assertNull(list.get(6));
    }

    @Test
    public void testAddAllWithEmptyCollection() {
        Assert.assertEquals(0, list.size());

        list.addAll(Collections.emptyList());

        Assert.assertEquals(0, list.size());
        Assert.assertEquals(10, getPrivateArrField(list).length);
    }

    @Test
    public void testAddAllWithResizing() {
        Assert.assertEquals(0, list.size());
        Assert.assertEquals(10, getPrivateArrField(list).length);

        list.addAll(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9"));

        Assert.assertEquals(9, list.size());
        Assert.assertEquals(20, getPrivateArrField(list).length);

        list.addAll(List.of("10", "11"));

        Assert.assertEquals(11, list.size());
        Assert.assertEquals(20, getPrivateArrField(list).length);

        list.addAll(List.of("12", "13", "14", "15", "16", "17", "18", "19", "20", "21"));

        Assert.assertEquals(21, list.size());
        Assert.assertEquals(40, getPrivateArrField(list).length);
    }

    @Test
    public void testAddAllWithResizingFromBigCollection() {
        Assert.assertEquals(0, list.size());
        Assert.assertEquals(10, getPrivateArrField(list).length);

        List<String> bigList = Arrays.asList(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        );

        list.addAll(bigList);

        Assert.assertEquals(140, list.size());
        Assert.assertEquals(320, getPrivateArrField(list).length);
    }

    @Test
    public void testElementRemoval() {
        list.add("1");
        list.add("2");
        list.add("3");

        Assert.assertEquals(3, list.size());
        Assert.assertEquals("3", list.get(2));

        list.remove(2);

        Assert.assertNull(list.get(2));
        Assert.assertEquals(2, list.size());

        Assert.assertEquals("1", list.get(0));

        list.remove(0);

        // elements shifted to the left
        Assert.assertEquals("2", list.get(0));
        Assert.assertEquals(1, list.size());

        list.remove(0);

        Assert.assertTrue(list.isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddInPositivePositionOutOfRange() {
        list.add("Test", 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddInNegativePositionOutOfRange() {
        list.add("Test", -1);
    }

    @Test
    public void testAddInPosition() {
        list.add("Test", 0);

        Assert.assertEquals("Test", list.get(0));

        list.add("Test1", 1);

        Assert.assertEquals("Test1", list.get(1));
        Assert.assertEquals("Test", list.get(0));

        list.add("Test2", 1);

        Assert.assertEquals("Test2", list.get(1));
        Assert.assertEquals("Test1", list.get(2));
        Assert.assertEquals("Test", list.get(0));

        list.add("Test3", 3);;

        Assert.assertEquals("Test2", list.get(1));
        Assert.assertEquals("Test1", list.get(2));
        Assert.assertEquals("Test", list.get(0));
        Assert.assertEquals("Test3", list.get(3));

        list.add("Test5", 0);

        Assert.assertEquals("Test5", list.get(0));
        Assert.assertEquals("Test1", list.get(3));
        Assert.assertEquals("Test", list.get(1));
        Assert.assertEquals("Test3", list.get(4));
    }

    @Test
    public void testClear() {
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");

        Assert.assertFalse(list.isEmpty());

        list.clear();

        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testIterateThroughElements() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));

        list.add("1");
        list.add("2");
        list.add("3");

        for (String element : list) {
            System.out.print(element);
            Assert.assertEquals(element, outContent.toString());
            outContent.reset();
        }

        System.setOut(System.out);
    }

    private String[] getPrivateArrField(Object object) {
        Field arrField;
        try {
            arrField = object.getClass().getDeclaredField("arr");
            arrField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            return null;
        }

        try {
            Object[] arr = (Object[]) arrField.get(object);
            return Arrays.copyOf((Object[]) arrField.get(object), arr.length, String[].class);
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
