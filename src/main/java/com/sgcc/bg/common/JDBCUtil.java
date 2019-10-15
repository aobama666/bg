package com.sgcc.bg.common;



import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtil {

    private static Connection connection = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet resultSet = null;
    private static JDBCUtil per = null;
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = ConfigUtils.getConfig("jdbc_syncForZH_url");
    private static final String USERNAME = ConfigUtils.getConfig("jdbc_syncForZH_username");
    private static final String PASSWORD = ConfigUtils.getConfig("jdbc_syncForZh_password");

    private JDBCUtil(){}
    public static JDBCUtil getInstace(){
        if(null == per){
            per = new JDBCUtil();
            per.registeredDriver();
        }
        return per;
    }

    private void registeredDriver() {
        //注册驱动
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //获取数据库的链接
    public  Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 查询需要的结果
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> executeQuery(String sql, List<Object> params) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        int index = 1;
        pstmt = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(index++, params.get(i));
            }
        }
        resultSet = pstmt.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols_len = metaData.getColumnCount();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < cols_len; i++) {
                String columnName = metaData.getColumnName(i + 1);
                Object cols_value = resultSet.getObject(columnName);
                if (cols_value == null) {
                    cols_value = "";
                }
                map.put(columnName, cols_value);
            }
            list.add(map);
        }
        return list;
    }

    public void closeConnection() {
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(pstmt != null){
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
