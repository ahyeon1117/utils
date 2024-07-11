package com.hyeon.backend.utils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.io.File;

public class SystemUtils {

  private String terminal;
  private String executeCommand;
  private String memoryGetCommand;

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

  public String getSysInfo() throws IOException, InterruptedException {
    String result = "";
    // 메모리 정보 가져오기
    result += getMemInfo();

    File[] roots = File.listRoots();
    for (File root : roots) {
      // 총 디스크 용량
      long totalDiskSpace = root.getTotalSpace();

      // 사용 중인 디스크 용량
      long usableDiskSpace = root.getUsableSpace();

      String totalDisk =
        "Total Disk Space: " + totalDiskSpace / (1024 * 1024 * 1024) + " GB";
      String usableDisk =
        "Used Disk Space: " + usableDiskSpace / (1024 * 1024 * 1024) + " GB";
      String freeDisk =
        "Free Disk Space: " +
        (totalDiskSpace - usableDiskSpace) /
        (1024 * 1024 * 1024) +
        " GB";
      result +=
        String.format(
          "\n------HDD------ \n%s : { \n %s \n %s \n %s \n}\n------HDD------",
          root.getAbsolutePath(),
          totalDisk,
          freeDisk,
          usableDisk
        );
    }
    return result;
  }

  public String getMemInfo() throws IOException, InterruptedException {
    String os = System.getProperty("os.name").toLowerCase();

    if (os.contains("win")) {
      terminal = "cmd";
      executeCommand = "/c";
      memoryGetCommand =
        "for /f \"tokens=1,2\" %a in ('wmic OS get FreePhysicalMemory^,TotalVisibleMemorySize ^| findstr /r \"[0-9]\"') do @echo %a %b";
    } else if (os.contains("linux")) {
      terminal = "sh";
      executeCommand = "-c";
      memoryGetCommand =
        "export TERM=xterm && top -n1 -b | grep Mem | head -n 1 | awk '{print $6 + $10, $4}'";
    }

    ProcessBuilder processBuilder = new ProcessBuilder(
      terminal,
      executeCommand,
      memoryGetCommand
    );
    processBuilder.redirectErrorStream(true); // 에러 스트림을 표준 출력 스트림으로 리디렉션, 이 설정을 해주어야 에러 발생시 로그로 출력
    Process process = processBuilder.start();
    process.waitFor();
    String memUsageString = new String(
      readAllBytes(process.getInputStream()),
      StandardCharsets.UTF_8
    );
    List<String> memUsageList = Arrays.asList(memUsageString.split(" "));
    if (memUsageList.size() >= 2) {
      Double totalMemorySize = Double.parseDouble(memUsageList.get(1));
      Double freeMemorySize = Double.parseDouble(memUsageList.get(0));
      Double memUsage = Double.parseDouble(
        String.format(
          "%.3f",
          (totalMemorySize - freeMemorySize) / totalMemorySize * 100
        )
      );
      if (os.contains("win")) {
        return String.format(
          "\n------MEM------\ntotalMemorySize: %f KB\nfreeMemorySize: %f KB\nmemUsage : %f %%\n------MEM------",
          totalMemorySize,
          freeMemorySize,
          memUsage
        );
      } else if (os.contains("linux")) {
        return String.format(
          "\n------MEM------\ntotalMemorySize: %f KB\nfreeMemorySize: %f KB\nmemUsage : %f %%\n------MEM------",
          totalMemorySize,
          freeMemorySize,
          memUsage
        );
      }
    }
    return "";
  }

  public static byte[] readAllBytes(InputStream inputStream)
    throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    byte[] data = new byte[1024];
    int bytesRead;
    while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, bytesRead);
    }
    return buffer.toByteArray();
  }
}
