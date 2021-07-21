package next.dao;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public void update(String query, PreparedStatementSetter pss) throws DataAccessException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pss.values(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void update(String query, Object... parameters) throws DataAccessException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            for(int i=0; i<parameters.length; i++) {
                pstmt.setObject(i + 1, parameters[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws DataAccessException {
        ResultSet rs = null;
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pss.values(pstmt);
            rs = pstmt.executeQuery();

            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rowMapper.MapRow(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public <T> T queryForObject(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws SQLException {
        List<T> result = query(sql, pss, rowMapper);
        if (result.isEmpty()) {
            return null;
        }
        // get(0)하면 result에서 맨 처음 Object가 나오게 된다.
        return result.get(0);
    }


}
