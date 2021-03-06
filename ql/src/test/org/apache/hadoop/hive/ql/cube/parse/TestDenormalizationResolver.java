package org.apache.hadoop.hive.ql.cube.parse;
/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

import static org.apache.hadoop.hive.ql.cube.parse.CubeTestSetup.getDbName;
import static org.apache.hadoop.hive.ql.cube.parse.CubeTestSetup.getExpectedQuery;
import static org.apache.hadoop.hive.ql.cube.parse.CubeTestSetup.getWhereForDailyAndHourly2days;
import static org.apache.hadoop.hive.ql.cube.parse.CubeTestSetup.getWhereForDailyAndHourly2daysWithTimeDim;
import static org.apache.hadoop.hive.ql.cube.parse.CubeTestSetup.now;
import static org.apache.hadoop.hive.ql.cube.parse.CubeTestSetup.twoDaysRange;
import static org.apache.hadoop.hive.ql.cube.parse.CubeTestSetup.twodaysBack;
import junit.framework.Assert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.parse.ParseException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.session.SessionState;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDenormalizationResolver {

  private Configuration conf;
  private CubeQueryRewriter driver;
  private final String cubeName = CubeTestSetup.TEST_CUBE_NAME;

  static CubeTestSetup setup;
  static HiveConf hconf = new HiveConf(TestDenormalizationResolver.class);
  static String dbName;
  @BeforeClass
  public static void setup() throws Exception {
    SessionState.start(hconf);
    setup = new CubeTestSetup();
    String dbName = TestDenormalizationResolver.class.getSimpleName();
    setup.createSources(hconf, dbName);
  }

  @AfterClass
  public static void tearDown() throws Exception {
    setup.dropSources(hconf);
  }

  @Before
  public void setupDriver() throws Exception {
    conf = new Configuration();
    conf.set(CubeQueryConfUtil.DRIVER_SUPPORTED_STORAGES, "C1,C2");
    conf.setBoolean(CubeQueryConfUtil.DISABLE_AUTO_JOINS, false);
    conf.setBoolean(CubeQueryConfUtil.ENABLE_SELECT_TO_GROUPBY, true);
    conf.setBoolean(CubeQueryConfUtil.ENABLE_GROUP_BY_TO_SELECT, true);
    conf.setBoolean(CubeQueryConfUtil.DISABLE_AGGREGATE_RESOLVER, false);
    driver = new CubeQueryRewriter(new HiveConf(conf, HiveConf.class));
  }

  private CubeQueryContext rewrittenQuery;
  private String rewrite(CubeQueryRewriter driver, String query)
      throws SemanticException, ParseException {
    rewrittenQuery = driver.rewrite(query);
    return rewrittenQuery.toHQL();
  }

  @Test
  public void testDenormsAsDirectFields() throws SemanticException, ParseException {
    // denorm fields directly available
    String twoDaysITRange = "time_range_in(it, '" +
        CubeTestSetup.getDateUptoHours(
            twodaysBack) + "','" + CubeTestSetup.getDateUptoHours(now) + "')";
    String hqlQuery = rewrite(driver, "select dim2big1, max(msr3)," +
        " msr2 from testCube" +
        " where " + twoDaysITRange);
    String expecteddim2big1 = getExpectedQuery(cubeName,
        "select testcube.dim2big1, max(testcube.msr3), sum(testcube.msr2) FROM ",
        null, " group by testcube.dim2big1",
        getWhereForDailyAndHourly2daysWithTimeDim(cubeName, "it", "C2_summary4"));
    TestCubeRewriter.compareQueries(expecteddim2big1, hqlQuery);
    hqlQuery = rewrite(driver, "select dim2big2, max(msr3)," +
        " msr2 from testCube" +
        " where " + twoDaysITRange);
    String expecteddim2big2 = getExpectedQuery(cubeName,
        "select testcube.dim2big2, max(testcube.msr3), sum(testcube.msr2) FROM ",
        null, " group by testcube.dim2big2",
        getWhereForDailyAndHourly2daysWithTimeDim(cubeName, "it", "C2_summary4"));
    TestCubeRewriter.compareQueries(expecteddim2big2, hqlQuery);
    Throwable th = null;
    try {
      hqlQuery = rewrite(driver, "select dim2bignew, max(msr3)," +
          " msr2 from testCube" +
          " where " + twoDaysITRange);
      Assert.fail();
    } catch (SemanticException e) {
      e.printStackTrace();
      th = e;
    }
    Assert.assertNotNull(th);

    driver = new CubeQueryRewriter(new HiveConf(conf, HiveConf.class));
    hqlQuery = rewrite(driver, "select testdim3.name, dim2big1, max(msr3)," +
        " msr2 from testCube" +
        " where " + twoDaysITRange);
    System.out.println("HQL query:" + hqlQuery);
    String expected = getExpectedQuery(cubeName,
        "select testdim3.name, testcube.dim2big1, max(testcube.msr3), sum(testcube.msr2) FROM ",
        " JOIN " + getDbName() + "c2_testdim2tbl3 testdim2 " +
        "on testcube.dim2big1 = testdim2.bigid1" +
        " join TestDenormalizationResolver.c2_testdim3tbl testdim3 on " +
        "testdim2.testdim3id = testdim3.id", null,
        " group by testdim3.name, (testcube.dim2big1)", null,
        getWhereForDailyAndHourly2daysWithTimeDim(cubeName, "it", "C2_summary4"));
    TestCubeRewriter.compareQueries(expected, hqlQuery);

    conf.set(CubeQueryConfUtil.DRIVER_SUPPORTED_STORAGES, "C2");
    driver = new CubeQueryRewriter(new HiveConf(conf, HiveConf.class));
    hqlQuery = rewrite(driver, "select dim2big1, max(msr3)," +
        " msr2 from testCube" +
        " where " + twoDaysITRange);
    TestCubeRewriter.compareQueries(expecteddim2big1, hqlQuery);
    hqlQuery = rewrite(driver, "select dim2big2, max(msr3)," +
        " msr2 from testCube" +
        " where " + twoDaysITRange);
    TestCubeRewriter.compareQueries(expecteddim2big2, hqlQuery);
  }

  @Test
  public void testDenormsWithJoins() throws SemanticException, ParseException {
    // all following queries use joins to get denorm fields
    conf.set(CubeQueryConfUtil.DRIVER_SUPPORTED_STORAGES, "C1");
    driver = new CubeQueryRewriter(new HiveConf(conf, HiveConf.class));
    String hqlQuery = rewrite(driver, "select dim2big1, max(msr3)," +
        " msr2 from testCube" +
        " where " + twoDaysRange);
    System.out.println("HQL query:" + hqlQuery);
    String expected = getExpectedQuery(cubeName,
        "select testdim2.bigid1, max(testcube.msr3), sum(testcube.msr2) FROM ",
        " JOIN " + getDbName() + "c1_testdim2tbl2 testdim2 ON testcube.dim2 = " +
            " testdim2.id and (testdim2.dt = 'latest') ", null,
            "group by (testdim2.bigid1)", null,
            getWhereForDailyAndHourly2days(cubeName, "c1_summary2"));
    TestCubeRewriter.compareQueries(expected, hqlQuery);

    driver = new CubeQueryRewriter(new HiveConf(conf, HiveConf.class));
    hqlQuery = rewrite(driver, "select testdim2.name, dim2big1, max(msr3)," +
        " msr2 from testCube" +
        " where " + twoDaysRange);
    System.out.println("HQL query:" + hqlQuery);
    expected = getExpectedQuery(cubeName,
        "select testdim2.name, testdim2.bigid1, max(testcube.msr3), sum(testcube.msr2) FROM ",
        " JOIN " + getDbName() + "c1_testdim2tbl2 testdim2 ON testcube.dim2 = " +
            " testdim2.id and (testdim2.dt = 'latest') ", null,
            "group by testdim2.name, testdim2.bigid1", null,
            getWhereForDailyAndHourly2days(cubeName, "c1_summary2"));
    TestCubeRewriter.compareQueries(expected, hqlQuery);

    driver = new CubeQueryRewriter(new HiveConf(conf, HiveConf.class));
    hqlQuery = rewrite(driver, "select testdim2.name, dim2big1, max(msr3)," +
        " msr2 from testCube left outer join testdim2" +
        " where " + twoDaysRange);
    System.out.println("HQL query:" + hqlQuery);
    expected = getExpectedQuery(cubeName,
        "select testdim2.name, testdim2.bigid1, max(testcube.msr3), sum(testcube.msr2) FROM ",
        " left outer JOIN " + getDbName() + "c1_testdim2tbl2 testdim2 ON testcube.dim2 = " +
            " testdim2.id and (testdim2.dt = 'latest') ", null,
            "group by testdim2.name, testdim2.bigid1", null,
            getWhereForDailyAndHourly2days(cubeName, "c1_summary2"));
    TestCubeRewriter.compareQueries(expected, hqlQuery);

    driver = new CubeQueryRewriter(new HiveConf(conf, HiveConf.class));
    hqlQuery = rewrite(driver, "select testdim3.name, dim2big1, max(msr3)," +
        " msr2 from testCube" +
        " where " + twoDaysRange);
    System.out.println("HQL query:" + hqlQuery);
    expected = getExpectedQuery(cubeName,
        "select testdim3.name, testdim2.bigid1, max(testcube.msr3), sum(testcube.msr2) FROM ",
        " JOIN " + getDbName() + "c1_testdim2tbl3 testdim2 " +
        "on testcube.dim2 = testdim2.id AND (testdim2.dt = 'latest')" +
        " join TestDenormalizationResolver.c1_testdim3tbl testdim3 on " +
        "testdim2.testdim3id = testdim3.id AND (testdim3.dt = 'latest')", null,
        " group by testdim3.name, (testdim2.bigid1)", null,
            getWhereForDailyAndHourly2days(cubeName, "c1_summary2"));
    TestCubeRewriter.compareQueries(expected, hqlQuery);

    driver = new CubeQueryRewriter(new HiveConf(conf, HiveConf.class));
    Throwable th = null;
    try {
      hqlQuery = rewrite(driver, "select dim2big2, max(msr3)," +
          " msr2 from testCube" +
          " where " + twoDaysRange);
      Assert.fail();
    } catch (SemanticException e) {
      th = e;
    }
    Assert.assertNotNull(th); 
  }

  @Test
  public void testDimensionQuery() throws Exception {
    driver = new CubeQueryRewriter(new HiveConf(conf, HiveConf.class));
    String hqlQuery = rewrite(driver, "select citydim.name, citydim.statename from" +
        " citydim");

    String joinExpr =
        " join " + getDbName() + "c1_statetable statedim on" +
            " citydim.stateid = statedim.id and (statedim.dt = 'latest')";
    String expected = getExpectedQuery("citydim",
        "SELECT citydim.name, statedim.name FROM ", joinExpr,
        null, null, "c1_citytable", true);
    TestCubeRewriter.compareQueries(expected, hqlQuery);
  }
}
