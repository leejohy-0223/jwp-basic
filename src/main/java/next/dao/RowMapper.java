package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper {
    Object MapRow(ResultSet rs) throws SQLException;
}
