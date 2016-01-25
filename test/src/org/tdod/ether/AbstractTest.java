package org.tdod.ether;

import junit.framework.Assert;

import org.tdod.ether.util.TestUtil;

public class AbstractTest {

   public void baseSetUp() {
      try {
         TestUtil.initializeWorld();
      } catch (Exception e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }

   }

}
