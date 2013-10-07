package org.apache.hadoop.hive.ql.cube.metadata;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.metadata.Table;

public final class CubeDimensionTable extends AbstractCubeTable {
  private final Map<String, List<TableReference>> dimensionReferences;
  private final Map<String, UpdatePeriod> snapshotDumpPeriods;

  public CubeDimensionTable(String dimName, List<FieldSchema> columns,
      double weight, Map<String, UpdatePeriod> snapshotDumpPeriods) {
    this(dimName, columns, weight, snapshotDumpPeriods,
        new HashMap<String, List<TableReference>>(), new HashMap<String, String>());
  }

  public CubeDimensionTable(String dimName, List<FieldSchema> columns,
      double weight, Set<String> storages) {
    this(dimName, columns, weight, getSnapshotDumpPeriods(storages),
        new HashMap<String, List<TableReference>>(), new HashMap<String, String>());
  }

  public CubeDimensionTable(String dimName, List<FieldSchema> columns,
      double weight, Map<String, UpdatePeriod> snapshotDumpPeriods,
      Map<String, List<TableReference>> dimensionReferences) {
    this(dimName, columns, weight, snapshotDumpPeriods, dimensionReferences,
        new HashMap<String, String>());
  }

  public CubeDimensionTable(String dimName, List<FieldSchema> columns,
      double weight, Set<String> storages,
      Map<String, List<TableReference>> dimensionReferences) {
    this(dimName, columns, weight, getSnapshotDumpPeriods(storages),
        dimensionReferences, new HashMap<String, String>());
  }

  public CubeDimensionTable(String dimName, List<FieldSchema> columns,
      double weight, Set<String> storages,
      Map<String, List<TableReference>> dimensionReferences,
      Map<String, String> properties) {
    this(dimName, columns, weight, getSnapshotDumpPeriods(storages),
        dimensionReferences, properties);
  }

  public CubeDimensionTable(String dimName, List<FieldSchema> columns,
      double weight,
      Map<String, UpdatePeriod> snapshotDumpPeriods,
      Map<String, List<TableReference>> dimensionReferences,
      Map<String, String> properties) {
    super(dimName, columns, properties, weight);
    this.dimensionReferences = dimensionReferences;
    this.snapshotDumpPeriods = snapshotDumpPeriods;
    addProperties();
  }

  private static Map<String, UpdatePeriod> getSnapshotDumpPeriods(
      Set<String> storages) {
    Map<String, UpdatePeriod> snapshotDumpPeriods =
        new HashMap<String, UpdatePeriod>();
    for (String storage : storages) {
      snapshotDumpPeriods.put(storage, null);
    }
    return snapshotDumpPeriods;
  }

  public CubeDimensionTable(Table tbl) {
    super(tbl);
    this.dimensionReferences = getDimensionReferences(getProperties());
    this.snapshotDumpPeriods = getDumpPeriods(getName(), getProperties());
  }

  @Override
  public CubeTableType getTableType() {
    return CubeTableType.DIMENSION;
  }

  @Override
  protected void addProperties() {
    super.addProperties();
    setDimensionReferenceProperties(getProperties(), dimensionReferences);
    setSnapshotPeriods(getName(), getProperties(), snapshotDumpPeriods);
  }

  public Map<String, List<TableReference>> getDimensionReferences() {
    return dimensionReferences;
  }

  public Map<String, UpdatePeriod> getSnapshotDumpPeriods() {
    return snapshotDumpPeriods;
  }

  private static void setSnapshotPeriods(String name, Map<String, String> props,
                                        Map<String, UpdatePeriod> snapshotDumpPeriods) {
    if (snapshotDumpPeriods != null) {
      props.put(MetastoreUtil.getDimensionStorageListKey(name),
          MetastoreUtil.getStr(snapshotDumpPeriods.keySet()));
      for (Map.Entry<String, UpdatePeriod> entry : snapshotDumpPeriods.entrySet())
      {
        if (entry.getValue() != null) {
          props.put(MetastoreUtil.getDimensionDumpPeriodKey(name, entry.getKey()),
              entry.getValue().name());
        }
      }
    }
  }

  private static void setDimensionReferenceProperties(Map<String, String> props,
                                                     Map<String, List<TableReference>> dimensionReferences) {
    if (dimensionReferences != null) {
      for (Map.Entry<String, List<TableReference>> entry : dimensionReferences.entrySet()) {
        props.put(MetastoreUtil.getDimensionSrcReferenceKey(entry.getKey()),
            MetastoreUtil.getDimensionDestReference(entry.getValue()));
      }
    }
  }

  public static Map<String, List<TableReference>> getDimensionReferences(
      Map<String, String> params) {
    Map<String, List<TableReference>> dimensionReferences =
        new HashMap<String, List<TableReference>>();
    for (String param : params.keySet()) {
      if (param.startsWith(MetastoreConstants.DIM_KEY_PFX)) {
        String key = param.replace(MetastoreConstants.DIM_KEY_PFX, "");
        String toks[] = key.split("\\.+");
        String dimName = toks[0];
        String value = params.get(MetastoreUtil.getDimensionSrcReferenceKey(dimName));

        if (value != null) {
          String refDims[] = StringUtils.split(value, ",");
          List<TableReference> references = new ArrayList<TableReference>(refDims.length);
          for (String refDim : refDims) {
            references.add(new TableReference(refDim));
          }
          dimensionReferences.put(dimName, references);
        }
      }
    }
    return dimensionReferences;
  }

  public static Map<String, UpdatePeriod> getDumpPeriods(String name,
      Map<String, String> params) {
    String storagesStr = params.get(MetastoreUtil.getDimensionStorageListKey(
        name));
    if (storagesStr != null) {
      Map<String, UpdatePeriod> dumpPeriods = new HashMap<String, UpdatePeriod>();
      String[] storages = storagesStr.split(",");
      for (String storage : storages) {
        String dumpPeriod = params.get(MetastoreUtil.getDimensionDumpPeriodKey(
            name, storage));
        if (dumpPeriod != null) {
          dumpPeriods.put(storage, UpdatePeriod.valueOf(dumpPeriod));
        } else {
          dumpPeriods.put(storage, null);
        }
      }
      return dumpPeriods;
    }
    return null;
  }

  @Override
  public boolean equals(Object obj) {
    if (!super.equals(obj)) {
      return false;
    }
    CubeDimensionTable other = (CubeDimensionTable) obj;

    if (this.getDimensionReferences() == null) {
      if (other.getDimensionReferences() != null) {
        return false;
      }
    } else {
      if (!this.getDimensionReferences().equals(
          other.getDimensionReferences())) {
        return false;
      }
    }
    if (this.getSnapshotDumpPeriods() == null) {
      if (other.getSnapshotDumpPeriods() != null) {
        return false;
      }
    } else {
      if (!this.getSnapshotDumpPeriods().equals(
          other.getSnapshotDumpPeriods())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Set<String> getStorages() {
    return snapshotDumpPeriods.keySet();
  }

  public boolean hasStorageSnapshots(String storage) {
    return (snapshotDumpPeriods.get(storage) != null);
  }

  public void addDimensionReference(String referenceName, TableReference reference) throws HiveException {
    List<TableReference> refs = dimensionReferences.get(referenceName);
    if (refs == null) {
      Iterator<TableReference> itr = refs.iterator();
      while (itr.hasNext()) {
        TableReference existing = itr.next();
        if (existing.equals(reference)) {
          itr.remove();
        }
      }
    } else {
      refs = new ArrayList<TableReference>(1);
      dimensionReferences.put(referenceName, refs);
    }
    refs.add(reference);
    addProperties();
  }

  public void addSnapshotDumpPeriod(String storage, UpdatePeriod period) throws HiveException {
    if (storage == null) {
      throw new NullPointerException("Cannot add null storage for " + getName());
    }

    if (snapshotDumpPeriods.containsKey(storage)) {
      LOG.info("Updating dump period for " + storage + " from " + snapshotDumpPeriods.get(storage)
      + " to " + period);
    }

    snapshotDumpPeriods.put(storage, period);
    addProperties();
  }
}
