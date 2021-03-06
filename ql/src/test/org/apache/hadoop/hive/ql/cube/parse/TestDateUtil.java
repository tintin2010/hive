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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import static org.apache.hadoop.hive.ql.cube.parse.DateUtil.resolveDate;

/**
 * Unit tests for cube DateUtil class
 * TestDateUtil.
 *
 */
public class TestDateUtil {
  public static final String[] testpairs = {
    "2013-Jan-01", "2013-Jan-31",
    "2013-Jan-01", "2013-May-31",
    "2013-Jan-01", "2013-Dec-31",
    "2013-Feb-01", "2013-Apr-25",
    "2012-Feb-01", "2013-Feb-01",
    "2011-Feb-01", "2013-Feb-01",
    "2013-Jan-02", "2013-Feb-02",
    "2013-Jan-02", "2013-Mar-02",
  };

  public static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MMM-dd");

  private Date pairs[];

  @Before
  public void setUp() {
    pairs = new Date[testpairs.length];
    for (int i = 0; i < testpairs.length; i++) {
      try {
        pairs[i] = DATE_FMT.parse(testpairs[i]);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
  }

  @Test
  public void testMonthsBetween() throws Exception {
    int i = 0;
    Assert.assertEquals("2013-Jan-01 to 2013-Jan-31",
        1, DateUtil.getMonthsBetween(pairs[i],
        DateUtils.round(pairs[i+1], Calendar.MONTH)));

    i+=2;
    Assert.assertEquals("2013-Jan-01 to 2013-May-31",
        5, DateUtil.getMonthsBetween(pairs[i],
        DateUtils.round(pairs[i+1], Calendar.MONTH)));

    i+=2;
    Assert.assertEquals("2013-Jan-01 to 2013-Dec-31",
        12,
        DateUtil.getMonthsBetween(pairs[i], DateUtils.round(pairs[i+1], Calendar.MONTH)));

    i+=2;
    Assert.assertEquals("2013-Feb-01 to 2013-Apr-25",
        2, DateUtil.getMonthsBetween(pairs[i], pairs[i+1]));

    i+=2;
    Assert.assertEquals("2012-Feb-01 to 2013-Feb-01",
        12, DateUtil.getMonthsBetween(pairs[i], pairs[i+1]));

    i+=2;
    Assert.assertEquals("2011-Feb-01 to 2013-Feb-01",
        24, DateUtil.getMonthsBetween(pairs[i], pairs[i+1]));

    i+=2;
    Assert.assertEquals("2013-Jan-02 to 2013-Feb-02",
        0,
        DateUtil.getMonthsBetween(pairs[i], pairs[i+1]));

    i+=2;
    Assert.assertEquals( "2013-Jan-02 to 2013-Mar-02",
        1, DateUtil.getMonthsBetween(pairs[i], pairs[i+1]));
  }

  @Test
  public void testQuartersBetween() throws Exception {
    int i = 0;
    Assert.assertEquals("2013-Jan-01 to 2013-Jan-31",
        0, DateUtil.getQuartersBetween(pairs[i], pairs[i+1]));

    i+=2;
    Assert.assertEquals("2013-Jan-01 to 2013-May-31",
        1, DateUtil.getQuartersBetween(pairs[i], pairs[i+1]));

    i+=2;
    Assert.assertEquals("2013-Jan-01 to 2013-Dec-31",
        4, DateUtil.getQuartersBetween(pairs[i], DateUtils.round(pairs[i+1], Calendar.MONTH)));

    i+=2;
    Assert.assertEquals("2013-Feb-01 to 2013-Apr-25",
        0, DateUtil.getQuartersBetween(pairs[i], pairs[i+1]));

    i+=2;
    Assert.assertEquals("2012-Feb-01 to 2013-Feb-01",
        3, DateUtil.getQuartersBetween(pairs[i], pairs[i+1]));

    i+=2;
    Assert.assertEquals("2011-Feb-01 to 2013-Feb-01",
        7, DateUtil.getQuartersBetween(pairs[i], pairs[i+1]));
  }


  @Test
  public void testYearsBetween() throws Exception {
    int i = 0;
    Assert.assertEquals( "" + pairs[i] + "->" + pairs[i+1],
        0, DateUtil.getYearsBetween(pairs[i], pairs[i+1]));

    i+=2;
    Assert.assertEquals( "" + pairs[i] + "->" + pairs[i+1],
        0, DateUtil.getYearsBetween(pairs[i], pairs[i+1]));

    i+=2;
    Assert.assertEquals( "" + pairs[i] + "->" + pairs[i+1],
        1, DateUtil.getYearsBetween(pairs[i],
        DateUtils.round(pairs[i+1], Calendar.MONTH)));

    i+=2;
    Assert.assertEquals( "" + pairs[i] + "->" + pairs[i+1],
        0, DateUtil.getYearsBetween(pairs[i], pairs[i+1]));

    i+=2;
    Assert.assertEquals( "" + pairs[i] + "->" + pairs[i+1],
        0, DateUtil.getYearsBetween(pairs[i], pairs[i+1]));

    i+=2;
    Assert.assertEquals( "" + pairs[i] + "->" + pairs[i+1],
        1, DateUtil.getYearsBetween(pairs[i], pairs[i+1]));
  }

  @Test
  public void testWeeksBetween() throws Exception {
    int weeks = DateUtil.getWeeksBetween(DATE_FMT.parse("2013-May-26"),
        DATE_FMT.parse("2013-Jun-2"));
    Assert.assertEquals("2013-May-26 to 2013-Jun-2", 1, weeks);

    weeks = DateUtil.getWeeksBetween(DATE_FMT.parse("2013-May-27"),
        DATE_FMT.parse("2013-Jun-1"));
    Assert.assertEquals("2013-May-27 to 2013-Jun-1", 0, weeks);

    weeks = DateUtil.getWeeksBetween(DATE_FMT.parse("2013-May-25"),
        DATE_FMT.parse("2013-Jun-2"));
    Assert.assertEquals("2013-May-25 to 2013-Jun-1", 1, weeks);

    weeks = DateUtil.getWeeksBetween(DATE_FMT.parse("2013-May-26"),
        DATE_FMT.parse("2013-Jun-9"));
    Assert.assertEquals("2013-May-26 to 2013-Jun-8", 2, weeks);


    weeks = DateUtil.getWeeksBetween(DATE_FMT.parse("2013-May-26"),
        DATE_FMT.parse("2013-Jun-10"));
    Assert.assertEquals("2013-May-26 to 2013-Jun-10", 2, weeks);
  }

  @Test
  public void testNowWithGranularity() throws Exception {
    String dateFmt = "yyyy/MM/dd-HH.mm.ss.SSS";
    // Tuesday Sept 23, 2014, 12.02.05.500 pm
    String testDateStr = "2014/09/23-12.02.05.500";
    final SimpleDateFormat sdf = new SimpleDateFormat(dateFmt);
    final Date testDate = sdf.parse(testDateStr);

    System.out.print("@@ testDateStr=" + testDateStr + " parsed date=" + testDate);

    // Tests without a diff, just resolve now with different granularity
    Assert.assertEquals(testDateStr, sdf.format(resolveDate("now", testDate)));
    Assert.assertEquals("2014/01/01-00.00.00.000", sdf.format(resolveDate("now.year", testDate)));
    Assert.assertEquals("2014/09/01-00.00.00.000", sdf.format(resolveDate("now.month", testDate)));
    // Start of week resolves to Sunday
    Assert.assertEquals("2014/09/21-00.00.00.000", sdf.format(resolveDate("now.week", testDate)));
    Assert.assertEquals("2014/09/23-00.00.00.000", sdf.format(resolveDate("now.day", testDate)));
    Assert.assertEquals("2014/09/23-12.00.00.000", sdf.format(resolveDate("now.hour", testDate)));
    Assert.assertEquals("2014/09/23-12.02.00.000", sdf.format(resolveDate("now.minute", testDate)));
    Assert.assertEquals("2014/09/23-12.02.05.000", sdf.format(resolveDate("now.second", testDate)));

    // Tests with a diff
    Assert.assertEquals("2014/09/22-00.00.00.000", sdf.format(resolveDate("now.day -1day", testDate)));
    Assert.assertEquals("2014/09/23-10.00.00.000", sdf.format(resolveDate("now.hour -2hour", testDate)));
    Assert.assertEquals("2014/09/24-12.00.00.000", sdf.format(resolveDate("now.hour +24hour", testDate)));
    Assert.assertEquals("2015/01/01-00.00.00.000", sdf.format(resolveDate("now.year +1year", testDate)));
    Assert.assertEquals("2014/02/01-00.00.00.000", sdf.format(resolveDate("now.year +1month", testDate)));
  }

}
