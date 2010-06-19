package com.google.luna.client.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.luna.client.LunaTestCase;
import com.google.luna.client.test.TestProgress.TestMonitor;
import com.google.luna.client.test.data.FakeTestCase;
import com.google.luna.client.test.data.FakeTestPackage;
import com.google.luna.client.utils.FakeCookieJar;
import com.google.luna.client.utils.Thunk;

public class ProgressTest extends LunaTestCase {

  private TestMonitor forceNext(TestProgress progress) {
    assertTrue(progress.hasNext());
    final TestMonitor[] result = {null};
    progress.getNext().eagerOnValue(new Thunk<TestMonitor>() {
      @Override
      public void onValue(TestMonitor test) {
        assertNotNull(test);
        result[0] = test;
      }
    });
    assertNotNull(result[0]);
    return result[0];
  }

  private static final int kTestCount = 1000;
  private FakeCookieJar jar;
  private TestProgress progress;
  private FakeTestPackage pack;
  private List<FakeTestCase> tests;

  @Override
  protected void gwtSetUp() throws Exception {
    super.gwtSetUp();
    jar = new FakeCookieJar();
    tests = new ArrayList<FakeTestCase>();
    for (int i = 0; i < kTestCount; i++)
      tests.add(new FakeTestCase(i, false, "", ""));
    pack = new FakeTestPackage(tests);
    progress = new TestProgress(pack, jar.factory());
  }

  @Test
  public void testSimpleProgress() {
    for (int i = 0; i < kTestCount; i++) {
      final int serial = i;
      assertTrue(progress.hasNext());
      TestMonitor next = forceNext(progress);
      assertEquals(serial, next.getCase().getSerial());
    }
    assertFalse(progress.hasNext());
  }

  @Test
  public void testContinueAfterInterruption() {
    for (int i = 0; i < kTestCount / 2; i++) {
      TestMonitor result = forceNext(progress);
      assertEquals(i, result.getCase().getSerial());
      if (i % 5 == 0) result.testFailed();
      else result.testPassed();
    }

    TestProgress newProgress = new TestProgress(pack, jar.factory());
    for (int i = kTestCount / 2; i < kTestCount; i++) {
      TestMonitor result = forceNext(newProgress);
      assertEquals(i, result.getCase().getSerial());
      if (i % 5 == 0) result.testFailed();
      else result.testPassed();
    }
  }

  @Test
  public void testFail() {
    for (int i = 0; i < kTestCount; i++) {
      TestMonitor monitor = forceNext(progress);
      monitor.testFailed();
      assertEquals(i + 1, progress.getFailureCount());
      assertEquals(0, progress.getSuccessCount());
    }
  }

}
