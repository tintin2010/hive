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



import java.util.Date;

import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.SemanticException;

public class TimeRange {
  private String partitionColumn;
  private Date toDate;
  private Date fromDate;
  private ASTNode astNode;

  public static class TimeRangeBuilder {
    private final TimeRange range;

    public TimeRangeBuilder() {
      this.range = new TimeRange();
    }

    public TimeRangeBuilder partitionColumn(String col) {
      range.partitionColumn = col;
      return this;
    }

    public TimeRangeBuilder toDate(Date to) {
      range.toDate = to;
      return this;
    }

    public TimeRangeBuilder fromDate(Date from) {
      range.fromDate = from;
      return this;
    }

    public TimeRangeBuilder astNode(ASTNode node) {
      range.astNode = node;
      return this;
    }

    public TimeRange build() {
      return range;
    }
  }

  public static TimeRangeBuilder getBuilder() {
    return new TimeRangeBuilder();
  }

  private TimeRange() {

  }

  public String getPartitionColumn() {
    return partitionColumn;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public void validate() throws SemanticException {
    if (partitionColumn == null || fromDate == null || toDate == null)   {
      throw new SemanticException("Invalid time range");
    }

    if (fromDate.after(toDate)) {
      throw new SemanticException("From date: " + fromDate
        + " is after to date:" + toDate);
    }
  }

  @Override
  public String toString() {
    return partitionColumn + " [" + fromDate + ":" + toDate + "]";
  }
}
