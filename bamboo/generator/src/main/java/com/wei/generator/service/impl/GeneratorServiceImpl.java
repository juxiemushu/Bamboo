package com.wei.generator.service.impl;

import com.wei.generator.common.enumeration.FileType;
import com.wei.generator.common.utils.DBUtils;
import com.wei.generator.common.utils.FileUtils;
import com.wei.generator.model.ColumnModel;
import com.wei.generator.model.TableModel;
import com.wei.generator.model.request.GeneratorRequestModel;
import com.wei.generator.service.IGeneratorService;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class GeneratorServiceImpl implements IGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    @Autowired
    DataSource dataSource;

    @Override
    public int generatorFile(GeneratorRequestModel model) {
        int result = 0;

        //根据表名获取具体的表格信息
        String tableName = model.getTargetName();
        TableModel tableModel = this.getTableInfo(tableName);

        try {
            result = this.createFile(tableModel, model);
        } catch (IOException | TemplateException e) {
            result = -1;
            logger.error(e.getMessage());
        }

        return result;
    }

    // 获取table信息
    private TableModel getTableInfo(String tableName) {
        TableModel tableModel = new TableModel();
        List<ColumnModel> columns = tableModel.getColumns();

        List<String> multiColumns = null;
        List<String> NotNullColumns = null;

        tableModel.setName(tableName);

        Connection conn = null;
        DatabaseMetaData dbmd = null;
        try {
            conn = dataSource.getConnection();
            dbmd = conn.getMetaData();

            String dbType = dbmd.getSchemaTerm();
            String databaseType = dbmd.getDatabaseProductName();
            String catalog = dbmd.getCatalogTerm();
            ResultSet rs = dbmd.getCatalogs();

            // 获取主键字段
            String columnPk = DBUtils.getPrimaryKey(tableName, dbmd);
            // 获取不为空的字段
            NotNullColumns = DBUtils.getNotNullColumn(tableName, dbmd);
            // 获取表列信息
            ResultSet rs1 = DBUtils.getTableColumnInfo(tableName, dbmd);

            while (rs1.next()) {
                String columnName = rs1.getString("COLUMN_NAME");
                if ("OBJECT_VERSION_NUMBER".equalsIgnoreCase(columnName) || "REQUEST_ID".equalsIgnoreCase(columnName)
                        || "PROGRAM_ID".equalsIgnoreCase(columnName) || "CREATED_BY".equalsIgnoreCase(columnName)
                        || "CREATION_DATE".equalsIgnoreCase(columnName) || "LAST_UPDATED_BY".equalsIgnoreCase(columnName)
                        || "LAST_UPDATE_DATE".equalsIgnoreCase(columnName) || "LAST_UPDATE_LOGIN".equalsIgnoreCase(columnName)
                        || columnName.toUpperCase().startsWith("ATTRIBUTE")) {
                    continue;
                }
                columns.add(setColumnInfo(rs1, columnPk, NotNullColumns, multiColumns));
            }
            // 是否是多语言表
            rs1.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return tableModel;
    }

    private ColumnModel setColumnInfo(ResultSet rs1, String columnPk, List<String> NotNullColumns, List<String> multiColumns) throws SQLException {
        ColumnModel column = new ColumnModel();
        String columnName = rs1.getString("COLUMN_NAME");
        column.setName(columnName);
        String typeName = rs1.getString("TYPE_NAME");
        column.setJdbcType(typeName);
        if (StringUtils.isNotEmpty(rs1.getString("REMARKS"))) {
            column.setRemarks(rs1.getString("REMARKS"));
        }
        // 判断是否为主键
        if (columnName.equalsIgnoreCase(columnPk)) {
            column.setId(true);
        }
        // 判断是否为null字段
        for (String n : NotNullColumns) {
            if (columnName.equalsIgnoreCase(n) && !columnName.equalsIgnoreCase(columnPk)) {
                if ("BIGINT".equalsIgnoreCase(typeName)) {
                    column.setNotNull(true);
                } else if ("VARCHAR".equalsIgnoreCase(typeName)) {
                    column.setNotEmpty(true);
                }
            }
        }

        column.setColumnLength(rs1.getString("COLUMN_SIZE"));
        return column;
    }

    private int createFile(TableModel table, GeneratorRequestModel info) throws IOException, TemplateException {

        int result = FileUtils.isFileExist(info);
        if (result == 0) {
            if (!"NotOperation".equalsIgnoreCase(info.getDtoStatus())) {
                FileUtils.createDtoFile(table, info);
            }
            if (!"NotOperation".equalsIgnoreCase(info.getControllerStatus())) {
                FileUtils.createFtlInfoByType(FileType.Controller, table, info);
            }
            if (!"NotOperation".equalsIgnoreCase(info.getMapperStatus())) {
                FileUtils.createFtlInfoByType(FileType.Mapper, table, info);
            }
            if (!"NotOperation".equalsIgnoreCase(info.getImplStatus())) {
                FileUtils.createFtlInfoByType(FileType.Impl, table, info);
            }
            if (!"NotOperation".equalsIgnoreCase(info.getServiceStatus())) {
                FileUtils.createFtlInfoByType(FileType.Service, table, info);
            }
            if (!"NotOperation".equalsIgnoreCase(info.getMapperXmlStatus())) {
                FileUtils.createFtlInfoByType(FileType.MapperXml, table, info);
            }
            if (!"NotOperation".equalsIgnoreCase(info.getHtmlStatus())) {
                FileUtils.createFtlInfoByType(FileType.Html, table, info);
            }
        }
        return result;
    }
}
