package edu.brown.cs.student.foodCOMA.kdtree;

import edu.brown.cs.student.kdtree.BaseKDNode;
import edu.brown.cs.student.kdtree.KDTree;
import edu.brown.cs.student.kdtree.KDTreeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * tests for KDTree.
 */
public class KDTreeTest {
  private BaseKDNode n00;
  private BaseKDNode n01;
  private BaseKDNode n10;
  private BaseKDNode n11;
  private BaseKDNode neg11;

  private KDTree<BaseKDNode> kdt;

  private static final double D = 1e-3;

  @Before
  public void setUp() {
    n00 = new BaseKDNode("0", List.of(0., 0.));
    n01 = new BaseKDNode("1", List.of(0., 1.));
    n10 = new BaseKDNode("2", List.of(1., 0.));
    n11 = new BaseKDNode("3", List.of(1., 1.));
    neg11 = new BaseKDNode("4", List.of(-1., -1.));

    try {
      kdt = new KDTree<>(2);
    } catch (KDTreeException e) {
    }

    try {
      KDTree<BaseKDNode> bkdt = new KDTree<>(0);
      KDTree<BaseKDNode> b3kdt = new KDTree<>(-3);
    } catch (KDTreeException e) {
      assertEquals("dim must be a positive integer", e.getMessage());
    }
  }

  @After
  public void tearDown() {
    ;
  }

  @Test
  public void initStateTest() {
    setUp();
    assertEquals(kdt.getDim(), 2);
    assertNull(kdt.getRoot());
  }

  @Test
  public void initializeTreeTest() {
    setUp();

    List<BaseKDNode> ls = new ArrayList<>();
    ls.add(n00);
    ls.add(n01);
    ls.add(n10);
    ls.add(n11);
    ls.add(neg11);

    kdt.initializeTree(ls);

    assertEquals(kdt.getDim(), 2);
    assertEquals((Object) kdt.getRoot().getCoords().get(0), 0.);

    assertTrue((kdt.getRoot().getLeftChild().getCoords().get(1) <= kdt.getRoot().getCoords().get(1)));
    assertTrue((kdt.getRoot().getRightChild().getCoords().get(1) >= kdt.getRoot().getCoords().get(1)));

    assertNotNull(kdt.toString());
  }

  @Test
  public void distanceTest() {
    setUp();
    List<BaseKDNode> ls = new ArrayList<>();
    ls.add(n00);
    ls.add(n01);
    ls.add(n10);
    ls.add(n11);
    ls.add(neg11);

    kdt.initializeTree(ls);

    assertEquals(kdt.distance(n00, n00), 0., D);
    assertEquals(kdt.distance(n00, n01), 1., D);
    assertEquals(kdt.distance(n00, n10), 1., D);

    assertEquals(kdt.distance(n00, n11), Math.sqrt(2), D);
    assertEquals(kdt.distance(n00, neg11), Math.sqrt(2), D);

    assertEquals(kdt.distance(n01, n10), Math.sqrt(2), D);
    assertEquals(kdt.distance(n10, n01), Math.sqrt(2), D);

    assertEquals(kdt.distance(n11, neg11), 2.82842712475, D);
    assertEquals(kdt.distance(neg11, n11), 2.82842712475, D);
  }

  @Test
  public void insertTest() {
    setUp();
    kdt.insert(n00);
    assertEquals(kdt.getRoot(), n00);

    kdt.insert(n10);
    assertEquals(kdt.getRoot().getRightChild(), n10);
    kdt.insert(neg11);
    assertEquals(kdt.getRoot().getLeftChild(), neg11);

    kdt.insert(n11);
    assertEquals(kdt.getRoot().getRightChild().getRightChild(), n11);
  }

  @Test
  public void nearestSearchTest() throws KDTreeException {
    setUp();
    List<BaseKDNode> ls = new ArrayList<BaseKDNode>();
    ls.add(n00);
    ls.add(n01);
    ls.add(n10);
    ls.add(n11);
    ls.add(neg11);

    kdt.initializeTree(ls);

//    assertTrue(kdt.nearestSearch(n00, 0).isEmpty());
//    assertTrue(kdt.nearestSearch(null, -123).isEmpty());
    // search by target doesn't self choose
    assertEquals(kdt.nearestSearch(neg11, 1).get(0), n00);
    assertEquals(kdt.nearestSearch(new BaseKDNode("-1", List.of(-1., -1.)), 1).get(0), neg11);
    // get all nodes for k >= size
    assertTrue(kdt.nearestSearch(new BaseKDNode("-1", List.of(-1., -1.)), 5).containsAll(ls));
    assertTrue(ls.containsAll(kdt.nearestSearch(new BaseKDNode("-1", List.of(-1., -1.)), 5)));

    assertTrue(kdt.nearestSearch(new BaseKDNode("-1", List.of(-1., -1.)), 9).containsAll(ls));
    assertTrue(ls.containsAll(kdt.nearestSearch(new BaseKDNode("-1", List.of(-1., -1.)), 9)));
    // doesn't include self
    ls.remove(n00);
    assertTrue(kdt.nearestSearch(n00, 5).containsAll(ls));
    assertTrue(ls.containsAll(kdt.nearestSearch(n00, 5)));
  }

  @Test
  public void radiusSearchTest() throws KDTreeException {
    setUp();
    List<BaseKDNode> ls = new ArrayList<BaseKDNode>();
    ls.add(n00);
    ls.add(n01);
    ls.add(n10);
    ls.add(n11);
    ls.add(neg11);

    kdt.initializeTree(ls);

    assertTrue(kdt.radiusSearch(n00, 0).isEmpty());
//    assertTrue(kdt.radiusSearch(null, -123).isEmpty());
    // search by target doesn't self choose
    assertEquals(kdt.radiusSearch(neg11, 1.5).get(0), n00);
    assertEquals(kdt.radiusSearch(new BaseKDNode("-1", List.of(-1., -1.)), 1).get(0), neg11);
    // get all nodes for k >= size
    assertTrue(kdt.radiusSearch(new BaseKDNode("-1", List.of(-1., -1.)), 5).containsAll(ls));
    assertTrue(ls.containsAll(kdt.radiusSearch(new BaseKDNode("-1", List.of(-1., -1.)), 5)));

    assertTrue(kdt.radiusSearch(new BaseKDNode("-1", List.of(-1., -1.)), 9).containsAll(ls));
    assertTrue(ls.containsAll(kdt.radiusSearch(new BaseKDNode("-1", List.of(-1., -1.)), 9)));
    // doesn't include self
    ls.remove(n00);
    assertTrue(kdt.radiusSearch(n00, 5).containsAll(ls));
    assertTrue(ls.containsAll(kdt.radiusSearch(n00, 5)));
  }

  @Test
  public void boxSearchTest() throws KDTreeException {
    setUp();
    List<BaseKDNode> ls = new ArrayList<BaseKDNode>();
    ls.add(n00);
    ls.add(n01);
    ls.add(n10);
    ls.add(n11);
    ls.add(neg11);

    kdt.initializeTree(ls);

    List<List<Double>> bounds = List.of(List.of(0., 0.), List.of(0., 0.));
    assertTrue(kdt.boxSearch(bounds).get(0).equals(n00));
    // get all nodes for k >= size
    assertTrue(kdt.boxSearch(List.of(List.of(-2., 2.), List.of(-2., 2.))).containsAll(ls));
    assertTrue(ls.containsAll(kdt.boxSearch(List.of(List.of(-2., 2.), List.of(-2., 2.)))));
  }
}
