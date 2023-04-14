package com.cn.db.dbdoc.generator.impl;

import com.cn.db.dbdoc.entity.TableFieldInfo;
import com.cn.db.dbdoc.entity.TableInfo;
import com.cn.db.dbdoc.entity.TableKeyInfo;
import com.cn.db.dbdoc.enums.GenerateType;
import com.cn.db.dbdoc.generator.GeneratorService;
import com.cn.db.dbdoc.servjce.impl.MySqlDbInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;

/**
 * @author linht
 */
@Slf4j
@Service
public class ExcelGeneratorServiceImpl implements GeneratorService {
    @Autowired
    private MySqlDbInfoServiceImpl dbInfoService;

    private String indexSheetName = "目录";

    @Override
    public GenerateType type() {
        return GenerateType.excel;
    }

    @Override
    public void generateDbDoc(Long id, OutputStream outputStream) {
        String databaseName = dbInfoService.databaseName(id);
        List<TableInfo> tableInfos = dbInfoService.tableInfoList(id);
        try (HSSFWorkbook workbook = new HSSFWorkbook()
        ) {
            this.createIndexSheet(workbook, databaseName, tableInfos);
            for (int i = 0; i < tableInfos.size(); i++) {
                TableInfo tableInfo = tableInfos.get(i);
                createTableSheet(workbook, tableInfo);
            }
            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void createTableSheet(HSSFWorkbook workbook, TableInfo tableInfo) {
        HSSFSheet tableSheet = workbook.createSheet(tableInfo.getTableName());

        Integer rowIndex = 1;
        //创建sheet的表前缀
        rowIndex = this.createTableIndex(workbook, tableInfo, rowIndex, tableSheet);
        // 创建field表格
        rowIndex = createFieldTable(tableInfo, tableSheet, rowIndex, workbook);
        rowIndex = rowIndex + 3;
        // 创建key表格
        createKeyTable(tableInfo, tableSheet, rowIndex, workbook);
    }

    private Integer createTableIndex(HSSFWorkbook workbook, TableInfo tableInfo, Integer rowIndex, HSSFSheet tableSheet) {
        HSSFCellStyle simpleCellStyle = initSimpleCellStyle(workbook);
        HSSFCellStyle linkCellStyle = initLinkCellStyle(workbook);
        // 创建表格sheet,sheet名称长度最大32个字符
        tableSheet.setDefaultColumnWidth(45);
        HSSFRow tableSheetIndexRow = tableSheet.createRow(rowIndex++);
        HSSFCell tableSheetIndexRowFirstCell = createCell(0, tableSheetIndexRow, "返回目录", linkCellStyle);
        Hyperlink toIndexLink = workbook.getCreationHelper().createHyperlink(HyperlinkType.DOCUMENT);
        toIndexLink.setAddress(String.format("'%s'!A1", indexSheetName));
        tableSheetIndexRowFirstCell.setHyperlink(toIndexLink);

        // 创建表名
        HSSFRow tableSheetTableNameRow = tableSheet.createRow(rowIndex++);
        createCell(0, tableSheetTableNameRow, "表名：" + tableInfo.getTableName(), simpleCellStyle);

        // 创建第三行，注释
        HSSFRow tableSheetTableCommentRow = tableSheet.createRow(rowIndex++);
        createCell(0, tableSheetTableCommentRow, "注释：" + tableInfo.getTableRemark(), simpleCellStyle);
        return rowIndex;
    }

    /**
     * 创建索引
     *
     * @param tableInfo
     * @param tableSheet
     * @param rowIndex
     * @return
     */
    private Integer createKeyTable(TableInfo tableInfo, HSSFSheet tableSheet, Integer rowIndex, HSSFWorkbook workbook) {
        HSSFCellStyle simpleCellStyle = initSimpleCellStyle(workbook);
        //索引标题
        createCell(0, tableSheet.createRow(rowIndex++), "索引信息", simpleCellStyle);
        //索引头部
        HSSFRow indexSheetHeadRow = tableSheet.createRow(rowIndex++);
        createCell(0, indexSheetHeadRow, "名称", simpleCellStyle);
        createCell(1, indexSheetHeadRow, "栏位", simpleCellStyle);
        createCell(2, indexSheetHeadRow, "索引类型", simpleCellStyle);
        createCell(3, indexSheetHeadRow, "索引方式", simpleCellStyle);
        createCell(4, indexSheetHeadRow, "索引备注", simpleCellStyle);
        //创建索引内容
        for (int j = 0; j < tableInfo.getKeys().size(); j++) {
            TableKeyInfo tableKeyInfo = tableInfo.getKeys().get(j);
            HSSFRow keyRow = tableSheet.createRow(rowIndex++);
            createCell(0, keyRow, tableKeyInfo.getName(), simpleCellStyle);
            createCell(1, keyRow, String.join(",", tableKeyInfo.getColumns()), simpleCellStyle);
            createCell(2, keyRow, tableKeyInfo.getUnique() ? "Unique" : "Normal", simpleCellStyle);
            createCell(3, keyRow, tableKeyInfo.getIndexType(), simpleCellStyle);
            createCell(4, keyRow, tableKeyInfo.getIndexComment(), simpleCellStyle);
        }
        return rowIndex;
    }

    /**
     * 创建字段
     *
     * @param tableInfo
     * @param tableSheet
     * @param rowIndex
     * @param workbook
     * @return
     */
    private Integer createFieldTable(TableInfo tableInfo, HSSFSheet tableSheet, Integer rowIndex, HSSFWorkbook workbook) {
        HSSFCellStyle titleCellStyle = initTitleCellStyle(workbook);
        HSSFCellStyle simpleCellStyle = initSimpleCellStyle(workbook);
        // 创建表格头部
        HSSFRow tableSheetHeadRow = tableSheet.createRow(rowIndex++);

        createCell(0, tableSheetHeadRow, "字段", titleCellStyle);
        createCell(1, tableSheetHeadRow, "注释", titleCellStyle);
        createCell(2, tableSheetHeadRow, "类型", titleCellStyle);
        createCell(3, tableSheetHeadRow, "键", titleCellStyle);
        createCell(4, tableSheetHeadRow, "能否为空", titleCellStyle);
        createCell(5, tableSheetHeadRow, "默认值", titleCellStyle);

        // 创建表格内容
        for (int j = 0; j < tableInfo.getFields().size(); j++) {

            TableFieldInfo tableFieldInfo = tableInfo.getFields().get(j);
            HSSFRow fieldRow = tableSheet.createRow(rowIndex++);
            createCell(0, fieldRow, tableFieldInfo.getField(), simpleCellStyle);
            createCell(1, fieldRow, tableFieldInfo.getRemark(), simpleCellStyle);
            createCell(2, fieldRow, tableFieldInfo.getType(), simpleCellStyle);
            createCell(3, fieldRow, tableFieldInfo.getKey(), simpleCellStyle);
            createCell(4, fieldRow, tableFieldInfo.getNullAble(), simpleCellStyle);
            createCell(5, fieldRow, tableFieldInfo.getDefaultValue(), simpleCellStyle);
        }
        return rowIndex;
    }

    /**
     * 创建index索引
     *
     * @param workbook
     * @param databaseName
     * @param tableInfos
     */
    private void createIndexSheet(HSSFWorkbook workbook, String databaseName, List<TableInfo> tableInfos) {
        CreationHelper createHelper = workbook.getCreationHelper();
        HSSFCellStyle simpleCellStyle = initSimpleCellStyle(workbook);
        HSSFCellStyle linkCellStyle = initLinkCellStyle(workbook);
        // 创建目录sheet
        HSSFSheet indexSheet = workbook.createSheet(indexSheetName);
        indexSheet.setActive(true);
        indexSheet.setDefaultColumnWidth(50);
        // 创建数据库名称row
        HSSFRow dbNameRow = indexSheet.createRow(0);
        createCell(0, dbNameRow, "数据库名称:" + databaseName, simpleCellStyle);

        HSSFRow tablesRow = indexSheet.createRow(2);
        createCell(0, tablesRow, "表名称", simpleCellStyle);
        createCell(1, tablesRow, "注释", simpleCellStyle);
        // 创建返回目录sheet的超链接
        Hyperlink toIndexLink = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
        toIndexLink.setAddress(String.format("'%s'!A1", indexSheetName));

        // 创建表目录
        for (int i = 0; i < tableInfos.size(); i++) {
            TableInfo tableInfo = tableInfos.get(i);
            // 目录sheet创建一个cell超链接到表格Sheet
            HSSFRow indexRow = indexSheet.createRow(i + 3);
            HSSFCell indexRowCell = createCell(0, indexRow, tableInfo.getTableName(), linkCellStyle);
            Hyperlink toTableLink = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
            toTableLink.setAddress(String.format("'%s'!A1", tableInfo.getTableName()));
            indexRowCell.setHyperlink(toTableLink);
            createCell(1, indexRow, tableInfo.getTableRemark(), linkCellStyle);
        }
    }

    /**
     * 创建标题title
     *
     * @param workbook
     * @return
     */
    private HSSFCellStyle initTitleCellStyle(HSSFWorkbook workbook) {

        HSSFCellStyle titleCellStyle = workbook.createCellStyle();
        HSSFFont titleFont = workbook.createFont();
        titleFont.setFontName("微软雅黑");
        titleFont.setBold(false);
        titleFont.setFontHeightInPoints((short) 10);
        titleCellStyle.setFont(titleFont);
        titleCellStyle.setBorderLeft(BorderStyle.THIN);
        titleCellStyle.setBorderRight(BorderStyle.THIN);
        titleCellStyle.setBorderTop(BorderStyle.THIN);
        titleCellStyle.setBorderBottom(BorderStyle.THIN);
        titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleCellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.CORAL.getIndex());
        return titleCellStyle;
    }

    /**
     * 创建一般样式
     *
     * @param workbook
     * @return
     */
    private HSSFCellStyle initSimpleCellStyle(HSSFWorkbook workbook) {

        HSSFCellStyle simpleCellStyle = workbook.createCellStyle();
        simpleCellStyle.setBorderLeft(BorderStyle.THIN);
        simpleCellStyle.setBorderRight(BorderStyle.THIN);
        simpleCellStyle.setBorderTop(BorderStyle.THIN);
        simpleCellStyle.setBorderBottom(BorderStyle.THIN);
        HSSFFont simpleFont = workbook.createFont();
        simpleFont.setFontName("微软雅黑");
        simpleFont.setFontHeightInPoints((short) 10);
        simpleCellStyle.setFont(simpleFont);
        return simpleCellStyle;
    }

    /**
     * 创建超链接样式
     *
     * @param workbook
     */
    private HSSFCellStyle initLinkCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle linkCellStyle = workbook.createCellStyle();
        linkCellStyle.setBorderLeft(BorderStyle.THIN);
        linkCellStyle.setBorderRight(BorderStyle.THIN);
        linkCellStyle.setBorderTop(BorderStyle.THIN);
        linkCellStyle.setBorderBottom(BorderStyle.THIN);
        HSSFFont linkFont = workbook.createFont();
        linkFont.setFontName("YAHEI");
        linkFont.setFontHeightInPoints((short) 10);
        linkFont.setItalic(true);
        linkCellStyle.setFont(linkFont);
        return linkCellStyle;
    }

    public HSSFCell createCell(int indexColumn, HSSFRow row, String value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(indexColumn, CellType.STRING);
        cell.setCellValue(value);
        cell.setCellStyle(style);
        return cell;
    }
}
