package io.cjf.testoffice.controller;

import com.alibaba.fastjson.JSON;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.cjf.testoffice.dao.UserMapper;
import io.cjf.testoffice.enumeration.Gender;
import io.cjf.testoffice.po.User;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zeroturnaround.zip.ByteSource;
import org.zeroturnaround.zip.ZipEntrySource;
import org.zeroturnaround.zip.ZipUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.*;

@RestController
@RequestMapping("/user")
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Configuration configuration;

    @GetMapping("/getAll")
    public List<User> getAll() {
        List<User> users = userMapper.selectAll();
        return users;
    }

    @GetMapping(value = "/exportXls", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] exportXls(HttpServletResponse response) throws IOException, IllegalAccessException {
        List<User> users = userMapper.selectAll();

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("users");

        Field[] declaredFields;
        Row row;

        row = sheet.createRow(0);
        declaredFields = User.class.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            String name = field.getName();
            Cell cell = row.createCell(i);
            cell.setCellValue(name);
        }

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            row = sheet.createRow(i + 1);
            declaredFields = user.getClass().getDeclaredFields();
            for (int j = 0; j < declaredFields.length; j++) {
                Field field = declaredFields[j];
                Cell cell = row.createCell(j);

                field.setAccessible(true);
                Object value = field.get(user);
                if (value instanceof String) {
                    cell.setCellValue((String) value);
                }
                if (value instanceof Long) {
                    cell.setCellValue((Long) value);
                }
                if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                }
                if (value instanceof Short) {
                    cell.setCellValue((Short) value);
                }
                if (value instanceof Byte) {
                    cell.setCellValue((Byte) value);
                }
                if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                }
                if (value instanceof Float) {
                    cell.setCellValue((Float) value);
                }
                if (value instanceof Boolean) {
                    cell.setCellValue((Boolean) value);
                }
                if (value instanceof Date) {
                    Date date = (Date) value;
                    cell.setCellValue(date.toString());
                }

                String name = field.getName();
                if (name.equals("gender")) {
                    byte numericCellValue = (byte) cell.getNumericCellValue();
                    cell.setCellValue(String.valueOf(Gender.values()[numericCellValue]));
                }
                if (name.equals("enabled")) {
                    boolean enabled = cell.getBooleanCellValue();
                    cell.setCellValue(enabled ? "启用" : "禁用");
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        byte[] data = baos.toByteArray();
        baos.close();

        String filename = UUID.randomUUID().toString() + ".xls";
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        return data;
    }

    @GetMapping(value = "/exportXlsx", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] exportXlsx(HttpServletResponse response) throws IOException, IllegalAccessException {
        List<User> users = userMapper.selectAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("users");

        Field[] declaredFields;
        Row row;

        row = sheet.createRow(0);
        declaredFields = User.class.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            String name = field.getName();
            Cell cell = row.createCell(i);
            cell.setCellValue(name);
        }

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            row = sheet.createRow(i + 1);
            declaredFields = user.getClass().getDeclaredFields();
            for (int j = 0; j < declaredFields.length; j++) {
                Field field = declaredFields[j];
                Cell cell = row.createCell(j);

                field.setAccessible(true);
                Object value = field.get(user);
                if (value instanceof String) {
                    cell.setCellValue((String) value);
                }
                if (value instanceof Long) {
                    cell.setCellValue((Long) value);
                }
                if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                }
                if (value instanceof Short) {
                    cell.setCellValue((Short) value);
                }
                if (value instanceof Byte) {
                    cell.setCellValue((Byte) value);
                }
                if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                }
                if (value instanceof Float) {
                    cell.setCellValue((Float) value);
                }
                if (value instanceof Boolean) {
                    cell.setCellValue((Boolean) value);
                }
                if (value instanceof Date) {
                    Date date = (Date) value;
                    cell.setCellValue(date.toString());
                }

                String name = field.getName();
                if (name.equals("gender")) {
                    byte numericCellValue = (byte) cell.getNumericCellValue();
                    cell.setCellValue(String.valueOf(Gender.values()[numericCellValue]));
                }
                if (name.equals("enabled")) {
                    boolean enabled = cell.getBooleanCellValue();
                    cell.setCellValue(enabled ? "启用" : "禁用");
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        byte[] data = baos.toByteArray();
        baos.close();

        String filename = UUID.randomUUID().toString() + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        return data;
    }

    @GetMapping(value = "/exportStreamXlsx", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] exportStreamXlsx(HttpServletResponse response) throws IOException, IllegalAccessException {
        List<User> users = userMapper.selectAll();

        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        Field[] declaredFields;
        Row row;

        row = sheet.createRow(0);
        declaredFields = User.class.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            String name = field.getName();
            Cell cell = row.createCell(i);
            cell.setCellValue(name);
        }

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            row = sheet.createRow(i + 1);
            declaredFields = user.getClass().getDeclaredFields();
            for (int j = 0; j < declaredFields.length; j++) {
                Field field = declaredFields[j];
                Cell cell = row.createCell(j);

                field.setAccessible(true);
                Object value = field.get(user);
                if (value instanceof String) {
                    cell.setCellValue((String) value);
                }
                if (value instanceof Long) {
                    cell.setCellValue((Long) value);
                }
                if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                }
                if (value instanceof Short) {
                    cell.setCellValue((Short) value);
                }
                if (value instanceof Byte) {
                    cell.setCellValue((Byte) value);
                }
                if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                }
                if (value instanceof Float) {
                    cell.setCellValue((Float) value);
                }
                if (value instanceof Boolean) {
                    cell.setCellValue((Boolean) value);
                }
                if (value instanceof Date) {
                    Date date = (Date) value;
                    cell.setCellValue(date.toString());
                }

                String name = field.getName();
                if (name.equals("gender")) {
                    byte numericCellValue = (byte) cell.getNumericCellValue();
                    cell.setCellValue(String.valueOf(Gender.values()[numericCellValue]));
                }
                if (name.equals("enabled")) {
                    boolean enabled = cell.getBooleanCellValue();
                    cell.setCellValue(enabled ? "启用" : "禁用");
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        byte[] data = baos.toByteArray();
        baos.close();

        String filename = UUID.randomUUID().toString() + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        return data;
    }

    @GetMapping(value = "/exportXmlXlsx", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] exportXmlXlsx(HttpServletResponse response) throws IOException, TemplateException {

        Template template = configuration.getTemplate("users.ftl");

        List<User> users = userMapper.selectAll();
        Map root = new HashMap<>();
        root.put("users", users);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer out = new OutputStreamWriter(baos);
        template.process(root, out);
        out.close();

        byte[] data = baos.toByteArray();
        baos.close();

        String filename = UUID.randomUUID().toString() + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        return data;
    }

    @GetMapping("/getById")
    public User getById(@RequestParam Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        return user;
    }

    @GetMapping(value = "/getJsonFileById", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getJsonFileById(@RequestParam Long userId, HttpServletResponse response) {
        User user = userMapper.selectByPrimaryKey(userId);
        String dataStr = JSON.toJSONString(user);

        String filename = UUID.randomUUID().toString() + ".json";
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        return dataStr.getBytes();
    }

    @GetMapping(value = "/getZipFileById", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getZipFileById(@RequestParam Long userId, HttpServletResponse response) throws IOException {
        User user = userMapper.selectByPrimaryKey(userId);
        String dataStr = JSON.toJSONString(user);

        String uuid = UUID.randomUUID().toString();

        ZipEntrySource[] sources = {
                new ByteSource(uuid + ".json", dataStr.getBytes())
        };
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipUtil.pack(sources, baos);
        byte[] data = baos.toByteArray();
        baos.close();

        String filename = uuid + ".zip";
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        return data;
    }

    @GetMapping("/getMultiZipByIds")
    public byte[] getMultiZipByIds(@RequestParam List<Long> userIds, HttpServletResponse response) throws IOException {
        List<User> users = userMapper.selectByIds(userIds);
        List<ZipEntrySource> sources = new ArrayList<>();

        for (User user : users) {
            String userJsonStr = JSON.toJSONString(user);
            ByteSource byteSource = new ByteSource(user.getUserId() + ".json", userJsonStr.getBytes());
            sources.add(byteSource);
        }

        ZipEntrySource[] byteSources = sources.toArray(new ZipEntrySource[users.size()]);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipUtil.pack(byteSources, baos);
        byte[] data = baos.toByteArray();
        baos.close();

        String uuid = UUID.randomUUID().toString();
        String filename = uuid + ".zip";
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        return data;
    }
}
