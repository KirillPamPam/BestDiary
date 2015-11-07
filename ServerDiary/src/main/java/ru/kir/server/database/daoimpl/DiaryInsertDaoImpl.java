package ru.kir.server.database.daoimpl;

import ru.kir.server.database.DBConnection;
import ru.kir.server.database.dao.DiaryInsertDao;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Kirill Zhitelev on 01.11.2015.
 */
@Path("/send")
public class DiaryInsertDaoImpl implements DiaryInsertDao {
    private Connection connection = DBConnection.getDbConnection().connectionToBase();
    private static final String insertRecord = "insert into diary (theme, text, currentdate) values (?,?,?)";

    @Override
    @POST
    public void insertRecord(@QueryParam("theme")String theme, @QueryParam("text")String text, @QueryParam("date")String currentDate) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertRecord)) {
            preparedStatement.setString(1, theme);
            preparedStatement.setString(2, text);
            preparedStatement.setString(3, currentDate);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
