package ru.kir.server.database;

import org.json.JSONObject;

import javax.ws.rs.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.kir.commons.CommonConstants.*;

/**
 * Created by Kirill Zhitelev on 28.10.2015.
 */
@Path("/records")
public class ServerRecordService {
    private Connection connection = DBConnection.getDbConnection().connectToBase();
    private static final String getByDate = "select * from diary where currentdate = ?";
    private static final String getByTheme = "select * from diary where theme = ?";
    private static final String getAll = "select * from diary";
    private static final String insertRecord = "insert into diary (theme, text, currentdate) values (?,?,?)";

    @GET
    @Path("/date")
    @Produces("application/json")
    public String getTextByDate(@QueryParam("date") String date) {
        return getRecords(date, getByDate);
    }

    @GET
    @Path("/theme")
    @Produces("application/json")
    public String getTextByTheme(@QueryParam("theme")String theme) {
        return getRecords(theme, getByTheme);
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    public String getAll() {
        return getRecords(null, getAll);
    }

    @POST
    @Path("/transfer")
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

    private String getRecords(String byWhat, String query) {
        ResultSet resultSet = null;
        List<Object> records = new ArrayList<>();

        JSONObject responseObj = new JSONObject();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if(byWhat != null) {
                preparedStatement.setString(1, byWhat);
            }
            resultSet = preparedStatement.executeQuery();

            fromRecordToJson(responseObj, resultSet, records);

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

        return responseObj.toString();
    }

    private static void fromRecordToJson(JSONObject responseObj, ResultSet resultSet, List<Object> records)
            throws SQLException {

        while (resultSet.next()) {
            JSONObject recordObj = new JSONObject();
            recordObj.put(ID, resultSet.getString(1));
            recordObj.put(THEME, resultSet.getString(2));
            recordObj.put(TEXT, resultSet.getString(3));
            recordObj.put(DATE, resultSet.getString(4));

            records.add(recordObj);
        }
        responseObj.put(RECORDS, records);
    }
}
