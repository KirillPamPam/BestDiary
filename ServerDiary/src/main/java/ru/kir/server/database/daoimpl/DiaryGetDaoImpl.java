package ru.kir.server.database.daoimpl;

import org.json.JSONObject;
import ru.kir.server.database.DBConnection;
import ru.kir.server.database.dao.DiaryGetDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirill Zhitelev on 28.10.2015.
 */
@Path("/records")
public class DiaryGetDaoImpl implements DiaryGetDao {
    private Connection connection = DBConnection.getDbConnection().connectionToBase();
    private static final String getByDate = "select * from diary where currentdate = ?";
    private static final String getByTheme = "select * from diary where theme = ?";

    @Override
    @GET
    @Path("/date")
    @Produces("application/json")
    public String getTextByDate(@QueryParam("date") String date) {
        List<Object> records = new ArrayList<>();
        ResultSet resultSet = null;
        JSONObject responseObg = new JSONObject();
        try (PreparedStatement preparedStatement = connection.prepareStatement(getByDate)) {
            preparedStatement.setString(1, date);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                JSONObject recordObj = new JSONObject();
                recordObj.put("Id", resultSet.getString(1));
                recordObj.put("Theme", resultSet.getString(2));
                recordObj.put("Text", resultSet.getString(3));
                recordObj.put("Date", resultSet.getString(4));

                records.add(recordObj);
            }
            responseObg.put("records", records);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return responseObg.toString();
    }

    @GET
    @Override
    @Path("/theme")
    @Produces("application/json")
    public String getTextByTheme(@QueryParam("theme")String theme) {
        ResultSet resultSet = null;
        List<Object> records = new ArrayList<>();

        JSONObject responseObg = new JSONObject();

        try (PreparedStatement preparedStatement = connection.prepareStatement(getByTheme)) {
            preparedStatement.setString(1, theme);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                JSONObject recordObj = new JSONObject();
                recordObj.put("Id", resultSet.getString(1));
                recordObj.put("Theme", resultSet.getString(2));
                recordObj.put("Text", resultSet.getString(3));
                recordObj.put("Date", resultSet.getString(4));

                records.add(recordObj);
            }
            responseObg.put("records", records);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return responseObg.toString();
    }
}
