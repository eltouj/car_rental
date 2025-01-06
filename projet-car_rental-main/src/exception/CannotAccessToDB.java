package exception;

import java.sql.SQLException;

public class CannotAccessToDB extends RuntimeException {
    public CannotAccessToDB(String s, SQLException e) {
        super(s, e);
    }
}
