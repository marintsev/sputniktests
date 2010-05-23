package com.google.luna.client;
/**
 * Various constants.
 */
public class Constants {

  /**
   * Size of test block to fetch from the server.  The larger blocks you
   * fetch the longer the requests take to process, increasing the risk
   * of timeout.
   */
  public static final int kTestBlockSize = 128;

  /**
   * Number of test blocks to fetch ahead.  Fetch enough that test running
   * is unlikely to have to wait but no more.
   */
  public static final int kTestBlockAheadCount = 2;

}
