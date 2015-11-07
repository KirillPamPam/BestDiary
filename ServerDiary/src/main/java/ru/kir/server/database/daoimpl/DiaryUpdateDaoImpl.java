package ru.kir.server.database.daoimpl;

import ru.kir.server.database.DBConnection;
import ru.kir.server.database.dao.DiaryUpdateDao;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.sql.Connection;

/**
 * Created by Kirill Zhitelev on 07.11.2015.
 */
@Path("/update")
public class DiaryUpdateDaoImpl implements DiaryUpdateDao {
    private Connection connection = DBConnection.getDbConnection().connectionToBase();
    private static final String updateRecord = "update diary set text=? where id=?";

    @PUT
    @Override
    public void updateRecord(@QueryParam("id") String id) {

    }
}
