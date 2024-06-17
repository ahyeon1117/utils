package com.hyeon.backend.utils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.io.File;

public class SystemUtils {

  public static final String getOSName() {
    return System.getProperty("os.name").toLowerCase();
  }

  public interface CLibrary extends Library {
    CLibrary INSTANCE = Native.load(
      Platform.isWindows() ? "kernel32" : "c",
      CLibrary.class
    );

    void GetSystemInfo(SystemInfo info);
    void GlobalMemoryStatusEx(MemoryStatusEx status);
  }

  // 시스템 정보 구조체 정의
  @Structure.FieldOrder(
    {
      "dwOemId",
      "dwPageSize",
      "lpMinimumApplicationAddress",
      "lpMaximumApplicationAddress",
      "dwActiveProcessorMask",
      "dwNumberOfProcessors",
      "dwProcessorType",
      "dwAllocationGranularity",
      "dwProcessorLevel",
      "dwProcessorRevision",
    }
  )
  public static class SystemInfo extends Structure {

    public int dwOemId;
    public int dwPageSize;
    public Pointer lpMinimumApplicationAddress;
    public Pointer lpMaximumApplicationAddress;
    public Pointer dwActiveProcessorMask;
    public int dwNumberOfProcessors;
    public int dwProcessorType;
    public int dwAllocationGranularity;
    public short dwProcessorLevel;
    public short dwProcessorRevision;
  }

  // 메모리 상태 구조체 정의
  @Structure.FieldOrder(
    {
      "dwLength",
      "dwMemoryLoad",
      "ullTotalPhys",
      "ullAvailPhys",
      "ullTotalPageFile",
      "ullAvailPageFile",
      "ullTotalVirtual",
      "ullAvailVirtual",
      "ullAvailExtendedVirtual",
    }
  )
  public static class MemoryStatusEx extends Structure {

    public int dwLength = size();
    public int dwMemoryLoad;
    public long ullTotalPhys;
    public long ullAvailPhys;
    public long ullTotalPageFile;
    public long ullAvailPageFile;
    public long ullTotalVirtual;
    public long ullAvailVirtual;
    public long ullAvailExtendedVirtual;
  }

  public String getSysInfo() {
    String result = "";
    // 메모리 정보 가져오기
    MemoryStatusEx memoryStatus = new MemoryStatusEx();
    CLibrary.INSTANCE.GlobalMemoryStatusEx(memoryStatus);

    String totalMemory =
      "Total Physical Memory: " +
      memoryStatus.ullTotalPhys /
      (1024 * 1024) +
      " MB";
    String availableMemory =
      "Available Physical Memory: " +
      memoryStatus.ullAvailPhys /
      (1024 * 1024) +
      " MB";
    result += String.format("%s \n%s \n", totalMemory, availableMemory);

    File[] roots = File.listRoots();
    for (File root : roots) {
      // 디스크 정보 가져오기
      File diskPartition = new File("C:"); // 예: C 드라이브

      // 총 디스크 용량
      long totalDiskSpace = diskPartition.getTotalSpace();

      // 사용 중인 디스크 용량
      long usableDiskSpace = diskPartition.getUsableSpace();

      String totalDisk =
        "Total Disk Space: " + totalDiskSpace / (1024 * 1024 * 1024) + " GB";
      String usableDisk =
        "Usable Disk Space: " + usableDiskSpace / (1024 * 1024 * 1024) + " GB";
      String freeDisk =
        "Free Disk Space: " +
        (totalDiskSpace - usableDiskSpace) /
        (1024 * 1024 * 1024) +
        " GB";
      result +=
        String.format(
          "%s : { \n %s \n %s \n %s \n}\n",
          root.getAbsolutePath(),
          totalDisk,
          freeDisk,
          usableDisk
        );
    }
    return result;
  }
}
