package com.phuonghuynh;

import com.google.common.io.Files;
import com.phuonghuynh.config.CoreConfiguration;
import com.phuonghuynh.ws.TinWebService;
import com.phuonghuynh.ws.model.TinName;
import com.phuonghuynh.ws.model.TinServiceUser;
import com.phuonghuynh.ws.model.ValidateTinNameResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Created by phuonghqh on 5/3/15.
 */
public class TinCheckTest {

  @Test
  public void testTinCheck() throws IOException {
    TinWebService ws = new CoreConfiguration().pvsService();
    TinServiceUser wsUser = TinServiceUser.TinServiceUserBuilder.tinServiceUser().withUserLogin("phuonghqh@gmail.com").withUserPassword("waiting2212").build();
    List<Integer> resultOKs = Arrays.asList(1, 6, 7, 8);
    File tinResultFile = new File("src/test/resources/tinCheckResult.csv");
    tinResultFile.delete();
    Files.append("Payee,Tax ID (SSN),Response Code,Result\n", tinResultFile, StandardCharsets.UTF_8);

    CSVParser parser = CSVParser.parse(new File("src/test/resources/TIN Payee and Address Validations.csv"),
      StandardCharsets.UTF_8, CSVFormat.EXCEL);
    for (CSVRecord csvRecord : parser) {
      TinName tinName = TinName.TinNameBuilder.tinName().withTin(csvRecord.get(1).replaceAll("-", "")).withName(csvRecord.get(0)).build();
      ValidateTinNameResult result = ws.validateTinName(tinName, wsUser);
      Files.append(String.format("%s,%s,%s,%s\n", csvRecord.get(0).indexOf(",") >= 0 ? String.format("\"%s\"", csvRecord.get(0)) : csvRecord.get(0), csvRecord.get(1),
          result.getTinNameCode(), resultOKs.contains(result.getTinNameCode())),
        tinResultFile, StandardCharsets.UTF_8);
    }
  }
}
