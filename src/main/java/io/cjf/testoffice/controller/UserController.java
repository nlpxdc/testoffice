package io.cjf.testoffice.controller;

import io.cjf.testoffice.dao.UserMapper;
import io.cjf.testoffice.po.User;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getAll")
    public List<User> getAll(){
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
                if (value instanceof Long){
                    cell.setCellValue((Long) value);
                }
                if (value instanceof Integer){
                    cell.setCellValue((Integer) value);
                }
                if (value instanceof Short){
                    cell.setCellValue((Short) value);
                }
                if (value instanceof Byte){
                    cell.setCellValue((Byte) value);
                }
                if (value instanceof Double){
                    cell.setCellValue((Double) value);
                }
                if (value instanceof Float){
                    cell.setCellValue((Float) value);
                }
                if (value instanceof Boolean) {
                    cell.setCellValue((Boolean) value);
                }
                if (value instanceof Date) {
                    Date date = (Date) value;
                    cell.setCellValue(date.toString());
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        byte[] data = baos.toByteArray();
        baos.close();

        String filename = UUID.randomUUID().toString() + ".xls";
        response.setHeader("Content-Disposition", "attachment; filename="+filename);

        return data;
    }
}
