package com.hyeon.backend.utils;

import com.hyeon.backend.enums.Result;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CmdRunner {

  /** 매개변수로 주어진 명령어를 로컬에서 실행합니다. */
  public Result runCmd(String cmd) {
    try {
      log.debug("명령어를 실행합니다. 한 줄의 명령어를 실행합니다.");
      log.debug("실행할 명령어: " + cmd);
      Process process;
      process = Runtime.getRuntime().exec(cmd);
      InputStreamReader inputStreamReader = new InputStreamReader(
        process.getInputStream()
      );
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      StringBuilder stringBuilder = new StringBuilder();
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
      }

      process.waitFor();
      int resultCode = process.exitValue();
      log.debug("명령어 실행 종료 값(0은 정상 종료를 뜻합니다): " + resultCode);
      log.info("command output: " + stringBuilder.toString());

      process.destroy();
      bufferedReader.close();
      inputStreamReader.close();
      if (0 == resultCode) {
        log.info("명령어 작성이 성공했습니다.");
        return Result.SUCCESS;
      } else {
        log.info("명령어 작성이 실패했습니다.");
        return Result.FAILURE;
      }
    } catch (IOException ioe) {
      log.error(
        "명령어 결과를 받는 과정에서 예외가 발생했습니다. 상세정보: " +
        ioe.toString()
      );
      return Result.ERROR;
    } catch (IllegalThreadStateException itse) {
      log.error(
        "명령어 프로세스를 종료하는 과정에서 예외가 발생했습니다. 상세정보: " +
        itse.toString()
      );
      itse.printStackTrace();
      return Result.ERROR;
    } catch (InterruptedException ie) {
      log.error(
        "명령어 수행 중 인터럽트를 받았습니다. 상세정보: " + ie.toString()
      );
      return Result.ERROR;
    } catch (Exception e) {
      log.error("알 수 없는 에러가 발생했습니다. 상세정보: " + e.toString());
      return Result.ERROR;
    }
  }

  public Result runCmd(String[] cmds) {
    try {
      log.debug("명령어를 실행합니다. 여러 명령어를 실행합니다.");
      String cmdToExec = Arrays.stream(cmds).collect(Collectors.joining(";"));
      log.debug("실행할 명령어: " + cmdToExec);
      Process process;
      process = Runtime.getRuntime().exec(cmdToExec);
      InputStreamReader inputStreamReader = new InputStreamReader(
        process.getInputStream()
      );
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      StringBuilder stringBuilder = new StringBuilder();
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
      }

      process.waitFor();
      int resultCode = process.exitValue();
      log.debug("명령어 실행 종료 값(0은 정상 종료를 뜻합니다): " + resultCode);
      process.destroy();
      log.info("커맨드 출력: " + stringBuilder.toString());

      bufferedReader.close();
      inputStreamReader.close();
      if (0 == resultCode) {
        log.info("명령어 작성이 성공했습니다.");
        return Result.SUCCESS;
      } else {
        log.info("명령어 작성이 실패했습니다.");
        return Result.FAILURE;
      }
    } catch (IOException ioe) {
      log.error(
        "명령어 결과를 받는 과정에서 예외가 발생했습니다. 상세정보: " +
        ioe.toString()
      );
      return Result.ERROR;
    } catch (IllegalThreadStateException itse) {
      log.error(
        "명령어 프로세스를 종료하는 과정에서 예외가 발생했습니다. 상세정보: " +
        itse.toString()
      );
      return Result.ERROR;
    } catch (Exception e) {
      log.error("알 수 없는 에러가 발생했습니다. 상세정보: " + e.toString());
      return Result.ERROR;
    }
  }

  public String getCmdOutput(String cmd) throws Exception {
    log.debug("명령어를 실행합니다. 한 줄의 명령어를 실행합니다.");
    log.debug("실행할 명령어: " + cmd);
    Process process;
    process = Runtime.getRuntime().exec(cmd);
    InputStreamReader inputStreamReader = new InputStreamReader(
      process.getInputStream()
    );
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    StringBuilder stringBuilder = new StringBuilder();
    String line;

    while ((line = bufferedReader.readLine()) != null) {
      stringBuilder.append(line);
    }

    process.waitFor();
    int resultCode = process.exitValue();

    process.destroy();
    bufferedReader.close();
    inputStreamReader.close();
    if (0 != resultCode) {
      log.info("명령어 작성이 실패했습니다.");
      throw new Exception("Command excecution has failed.");
    }

    return stringBuilder.toString();
  }

  public String getCmdOutput(String[] cmds) {
    String result = null;
    try {
      Process process = Runtime.getRuntime().exec(cmds);
      InputStreamReader inputStreamReader = new InputStreamReader(
        process.getInputStream()
      );
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      StringBuilder stringBuilder = new StringBuilder();
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
      }

      process.waitFor();
      int resultCode = process.exitValue();
      process.destroy();

      bufferedReader.close();
      inputStreamReader.close();
      if (0 == resultCode) {
        log.info("명령어 작성이 성공했습니다.");
        result = stringBuilder.toString();
      } else {
        log.info("명령어 작성이 실패했습니다.");
      }
    } catch (IOException ioe) {
      log.error(
        "명령어 결과를 받는 과정에서 예외가 발생했습니다. 상세정보: " +
        ioe.toString()
      );
    } catch (IllegalThreadStateException itse) {
      log.error(
        "명령어 프로세스를 종료하는 과정에서 예외가 발생했습니다. 상세정보: " +
        itse.toString()
      );
    } catch (Exception e) {
      log.error("알 수 없는 에러가 발생했습니다. 상세정보: " + e.toString());
    }
    return result;
  }

  /**
   * runCmd로 실행할 수 없는 경우 임시 셸스크립트파일을 작성하고 실행합니다. 메소드가 종료되기 전에 임시 셸스크립트 파일을 삭제합니다.
   */
  public Result writeAndExecuteShFile(String[] lines, String type) {
    try {
      Path shFilePath = Paths.get(
        "/tmp/" + System.currentTimeMillis() + type + ".sh"
      );
      log.debug("셸스크립트를 생성합니다.");
      log.debug("스크립트 경로: " + shFilePath.toString());
      File shFile = shFilePath.toFile();
      FileWriter fileWriter = new FileWriter(shFile);

      fileWriter.write("#!/bin/bash");
      fileWriter.write("\n");

      int lineLength = lines.length;
      for (int i = 0; i < lineLength; i++) {
        fileWriter.write(lines[i]);
        if (i < lineLength - 1) {
          fileWriter.write("\n");
        }
      }
      fileWriter.close();

      log.debug("===== 스크립트 내용 =====");
      try (BufferedReader br = new BufferedReader(new FileReader(shFile))) {
        String line;
        while ((line = br.readLine()) != null) {
          System.out.println(line);
        }
      }

      // 셸스크립트를 실행합니다.
      String cmd = "sh " + shFile.getAbsolutePath();
      Result runShFileResult = runCmd(cmd);
      shFile.delete();
      return runShFileResult;
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
      log.error(
        "셸 스크립트 파일을 찾을 수 없습니다. 상세정보: " + fnfe.toString()
      );
      return Result.ERROR;
    } catch (IOException ioe) {
      log.error(
        "셸 스크립트 작성 중 에러가 발생했습니다. 상세정보: " + ioe.toString()
      );
      return Result.ERROR;
    } catch (Exception e) {
      log.error("알 수 없는 에러가 발생했습니다. 상세정보: " + e.toString());
      return Result.ERROR;
    }
  }

  public Result checkUser(String userName) {
    try {
      String whoami = getCmdOutput("whoami");
      if (!userName.equals(whoami)) {
        log.error(
          "이 application은 " +
          userName +
          " 유저로 실행해야 정상적으로 작동합니다. application을 종료합니다."
        );
        return Result.FAILURE;
      }
      return Result.SUCCESS;
    } catch (IOException ioe) {
      log.error(
        "명령어 결과를 받는 과정에서 예외가 발생했습니다. 상세정보: " +
        ioe.toString()
      );
      return Result.ERROR;
    } catch (IllegalThreadStateException itse) {
      log.error(
        "명령어 프로세스를 종료하는 과정에서 예외가 발생했습니다. 상세정보: " +
        itse.toString()
      );
      return Result.ERROR;
    } catch (Exception e) {
      log.error("알 수 없는 에러가 발생했습니다. 상세정보: " + e.toString());
      return Result.ERROR;
    }
  }
}
