package ru.kir.server.database.daoimpl;

import org.json.JSONObject;
import ru.kir.server.database.DBConnection;
import ru.kir.server.database.dao.ReceivingRecordDao;

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

import static ru.kir.commons.CommonConstants.*;

/**
 * Created by Kirill Zhitelev on 28.10.2015.
 */
@Path("/records")
public class ReceivingRecordDaoImpl implements ReceivingRecordDao {
    private Connection connection = DBConnection.getDbConnection().connectionToBase();
    private static final String getByDate = "select * from diary where currentdate = ?";
    private static final String getByTheme = "select * from diary where theme = ?";
    private static final String getAll = "select * from diary";

    @Override
    @GET
    @Path("/date")
    @Produces("application/json")
    public String getTextByDate(@QueryParam("date") String date) {
        return getRecords(date, getByDate);
    }

    @GET
    @Override
    @Path("/theme")
    @Produces("application/json")
    public String getTextByTheme(@QueryParam("theme")String theme) {
        return getRecords(theme, getByTheme);
    }

    @Override
    @GET
    @Path("/all")
    @Produces("application/json")
    public String getAll() {
        return getRecords(null, getAll);
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
        responseObj.put("records", records);
    }
}
