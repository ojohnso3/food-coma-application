package edu.brown.cs.student.kdtree;

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

  private static final double D = 1e-6;

  @Before
  public void setUp() {
    List<Double> l00 = new ArrayList<>();
    List<Double> l01 = new ArrayList<>();
    List<Double> l10 = new ArrayList<>();
    List<Double> l11 = new ArrayList<>();
    List<Double> ln11= new ArrayList<>();

    l00.add(0.);
    l00.add(0.);

    l01.add(0.);
    l01.add(1.);

    l10.add(1.);
    l10.add(0.);

    l11.add(1.);
    l11.add(1.);

    ln11.add(-1.);
    ln11.add(-1.);

    n00 = new BaseKDNode("0", l00);
    n01 = new BaseKDNode("1", l01);
    n10 = new BaseKDNode("2", l10);
    n11 = new BaseKDNode("3", l11);
    neg11 = new BaseKDNode("4", ln11);

    try {
      kdt = new KDTree<>(2);
    } catch (KDTreeException ignored) {
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
    n00 = null;
    n01 = null;
    n10 = null;
    n11 = null;
    neg11 = null;
    kdt = null;
  }

  @Test
  public void initStateTest() {
    setUp();
    assertEquals(kdt.getDim(), 2);
    assertNull(kdt.getRoot());
    tearDown();
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
    tearDown();
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
    tearDown();
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
    tearDown();
  }

  @Test
  public void nearestSearchTest() throws KDTreeException {
    setUp();
    List<BaseKDNode> ls = new ArrayList<>();
    ls.add(n00);
    ls.add(n01);
    ls.add(n10);
    ls.add(n11);
    ls.add(neg11);

    kdt.initializeTree(ls);

    assertTrue(kdt.nearestSearch(n00, 0).isEmpty());
    try {
      assertTrue(kdt.nearestSearch(null, -123).isEmpty());
    } catch (KDTreeException e) {
      assertEquals("ERROR: must search for a non-negative amount of neighbors", e.getMessage());
    }
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
    tearDown();
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

    assertEquals(kdt.radiusSearch(new BaseKDNode("-1", List.of(0., 0.)), 0).get(0), n00);
    try {
      assertTrue(kdt.radiusSearch(null, -123).isEmpty());
    } catch (KDTreeException e) {
      assertEquals("ERROR: must search in a non-negative radius", e.getMessage());
    }
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
    tearDown();
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
    tearDown();
  }

  @Test
  public void normalize1DTest() {
    //1D EXAMPLE
    List<Double> l0 = new ArrayList<>();
    List<Double> l1 = new ArrayList<>();
    List<Double> l2 = new ArrayList<>();
    List<Double> l_5 = new ArrayList<>();
    List<Double> l5 = new ArrayList<>();
    List<Double> l10 = new ArrayList<>();

    l0.add(0.);
    l1.add(1.);
    l2.add(2.);
    l_5.add(.5);
    l5.add(5.);
    l10.add(10.);

    BaseKDNode n0 = new BaseKDNode("0", l0);
    BaseKDNode n1 = new BaseKDNode("1", l1);
    BaseKDNode n2 = new BaseKDNode("2", l2);
    BaseKDNode n_5 = new BaseKDNode("3", l_5);
    BaseKDNode n5 = new BaseKDNode("4", l5);
    BaseKDNode nten = new BaseKDNode("5", l10);

    try {
      KDTree<BaseKDNode> tree = new KDTree<>(1);
      List<BaseKDNode> ls = new ArrayList<>();
      ls.add(n0);
      ls.add(n1);
      ls.add(n2);
      ls.add(n_5);
      ls.add(n5);
      ls.add(nten);
      tree.normalizeAxes(ls);
    } catch (KDTreeException e) {
      System.err.println(e.getMessage());
    }

    double min = 0.;
    double max = 10.;
    assertEquals(norm(0., min, max), n0.getCoords().get(0), D);
    assertEquals(norm(1., min, max), n1.getCoords().get(0), D);
    assertEquals(norm(2., min, max), n2.getCoords().get(0), D);
    assertEquals(norm(.5, min, max), n_5.getCoords().get(0), D);
    assertEquals(norm(5., min, max), n5.getCoords().get(0), D);
    assertEquals(norm(10., min, max), nten.getCoords().get(0), D);
  }

  @Test
  public void normalize2DTest() {
    // 2D EXAMPLE
    setUp();
    List<BaseKDNode> ls2 = new ArrayList<>();
    ls2.add(n00);
    ls2.add(n01);
    ls2.add(n10);
    ls2.add(n11);
    ls2.add(neg11);
    kdt.normalizeAxes(ls2);

    double min = -1.;
    double max = 1.;
    assertEquals(norm(0., min, max), n00.getCoords().get(0), D);
    assertEquals(norm(0., min, max), n00.getCoords().get(1), D);

    assertEquals(norm(0., min, max), n01.getCoords().get(0), D);
    assertEquals(norm(1., min, max), n01.getCoords().get(1), D);

    assertEquals(norm(1., min, max), n10.getCoords().get(0), D);
    assertEquals(norm(0., min, max), n10.getCoords().get(1), D);

    assertEquals(norm(1., min, max), n11.getCoords().get(0), D);
    assertEquals(norm(1., min, max), n11.getCoords().get(1), D);

    assertEquals(norm(-1., min, max), neg11.getCoords().get(0), D);
    assertEquals(norm(-1., min, max), neg11.getCoords().get(1), D);
    tearDown();
  }

  private double norm(double in, double min, double max) {
    return (in - min) / (max - min);
  }
}
