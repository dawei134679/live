package com.qiniu.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @Date       : 2017-03-10
 * @Comments   : 导入导出Excel工具类
 * @Version    : 1.0.0
 */

public class ExcelUitl  {
	/*
     * 通用的Excel文件导入导出方法
     *   title:首行标题
     *   sheets:sheet的tab标签页说明
     *   headers:表头：List存放表头
     *   datas:数据行：list存放实体数据，map存放具体每一行数据，和headers对应。
     *   rs:HttpServletResponse响应作用域，如果不为null，会直接将文件流输出到客户端，下载文件
     */
    public static void ExpExs(String title,String sheets,List<String> headers,List datas,HttpServletResponse rs,int type){
        try { 
            if(sheets== null || "".equals(sheets)){ sheets = "sheet"; }
              
            HSSFWorkbook workbook = new HSSFWorkbook(); 
            HSSFSheet sheet = workbook.createSheet(sheets); //+workbook.getNumberOfSheets()
             
            HSSFRow row;
            HSSFCell cell;
              
            // 设置这些样式
            HSSFFont font = workbook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);//字体
            font.setFontHeightInPoints((short) 16);//字号 
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
            //font.setColor(HSSFColor.BLUE.index);//颜色
              
            HSSFCellStyle cellStyle= workbook.createCellStyle(); //设置单元格样式
            cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER );
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setFont(font);
              
            //产生表格标题行       
            row = sheet.createRow(0);
            row.setHeightInPoints(20);
            for (int i = 0; i < headers.size(); i++) { 
                HSSFRichTextString text = new HSSFRichTextString(headers.get(i).toString());  
                cell = row.createCell(i);
                cell.setCellValue(text); 
                cell.setCellStyle(cellStyle);
            }  
              
              
            cellStyle= workbook.createCellStyle(); 
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setDataFormat((short)0x31);//设置显示格式，避免点击后变成科学计数法了
            //遍历集合数据，产生数据行  
            List<Object> list = null;
            for (int i=0; i <datas.size(); i++) { 
            	Map resultMap = (Map) datas.get(i);
            	//创建表格行
                row=sheet.createRow((i+1));
                row.setHeightInPoints(20);
                Iterator<Map.Entry<String, Object>> it = resultMap.entrySet().iterator();
            	list = new ArrayList<Object>();
                while(it.hasNext()) {
                	Map.Entry<String, Object> entry = it.next();
                	System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                	list.add(entry.getValue());
                }
                for(int j=0;j<list.size();j++) {
                	//创建表格列
                    cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    if(list.get(j) != null) {
                        cell.setCellValue(new HSSFRichTextString(list.get(j).toString())); 
                    }else{
                        cell.setCellValue(new HSSFRichTextString(""));     
                   }
               }
            }   
              
            for (int i = 0; i < headers.size(); i++) { 
                sheet.autoSizeColumn((short)i);
            }
             
            rs.reset();
            rs.setContentType("application/vnd.ms-excel;charset=utf-8"); //自动识别
            String fileName = "";
            switch(type){
            	case 1:
                    fileName = new String(("新增留存数据").getBytes(), "ISO8859-1");
                    break;
            	case 2:
            		fileName = new String(("登录留存数据").getBytes(), "ISO8859-1");
                    break;
            	case 3:
            		fileName = new String(("付费留存数据").getBytes(), "ISO8859-1");
                    break;
            }
            rs.setHeader("Content-Disposition","attachment;filename="+fileName+".xls");
            //文件流输出到rs里
            workbook.write(rs.getOutputStream());
            rs.getOutputStream().flush();
            rs.getOutputStream().close();
        } catch (Exception e) {  
            System.out.println("#Error ["+e.getMessage()+"] ");
        }
    }   
}
