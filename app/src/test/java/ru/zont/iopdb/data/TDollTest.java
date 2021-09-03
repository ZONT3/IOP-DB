package ru.zont.iopdb.data;

import junit.framework.TestCase;

import java.util.ArrayList;

public class TDollTest extends TestCase {
    public void testFetch() {
        final ArrayList<TDoll> list = TDoll.fetchAllList();
        assertNotNull("List null", list);
        assertFalse("List empty", list.isEmpty());
    }
}