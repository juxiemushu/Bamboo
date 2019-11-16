package com.wei.generator.common.utils;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBUtils {

    private static final String COLUMN_NAME = "COLUMN_NAME";

    @Autowired
    private static DruidDataSource dataSource;

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static List<String> showAllTables() throws SQLException {
        List<String> tables = new ArrayList<>();

        Connection connection = DBUtils.getConnection();
        DatabaseMetaData dbmd = connection.getMetaData();
        String database = connection.getCatalog();
        ResultSet rs = dbmd.getTables(database, null, null, new String[] { "TABLE" });
        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        return tables;
    }

    public static ResultSet getTableColumnInfo(String table, DatabaseMetaData dbmd) throws SQLException {
        return dbmd.getColumns(null, null, table, null);
    }


    public static List<String> getNotNullColumn(String table, DatabaseMetaData dbmd) throws SQLException {
        List<String> result = new ArrayList<>();
        ResultSet rs = dbmd.getColumns(null, null, table, null);
        while (rs.next()) {
            if ("NO".equalsIgnoreCase(rs.getString("IS_NULLABLE"))) {
                result.add(rs.getString(COLUMN_NAME));
            }
        }
        return result;
    }

    public static String getPrimaryKey(String table, DatabaseMetaData dbmd) throws SQLException {
        String columnPK = null;
        ResultSet rs = dbmd.getPrimaryKeys("oauth", null, table);
        while (rs.next()) {
            columnPK = rs.getString(COLUMN_NAME);
        }
        return columnPK;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection!=null){
            connection.close();
        }
    }
    public static void closeResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet!=null){
            resultSet.close();
        }
    }
    public static void closeSqlSession(SqlSession sqlSession) throws SQLException {
        if (sqlSession!=null){
            sqlSession.close();
        }
    }

    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

}
