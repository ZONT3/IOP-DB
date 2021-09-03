package ru.zont.iopdb.data;

import android.os.Build;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TDoll extends DataEntity {
    private static ArrayList<TDoll> allList;

    public static ArrayList<TDoll> fetchAllList() {
        if (allList != null) return allList;
        try {
            final ResultSet r = Database.inst().getStatement().executeQuery("SELECT * FROM tdolls");
            final ArrayList<TDoll> res = new ArrayList<>();
            while (r.next()) {
                final TDoll tDoll = new TDoll();
                final ResultSetMetaData meta = r.getMetaData();
                for (int i = 1; i <= meta.getColumnCount(); i++)
                    tDoll.setValue(meta.getColumnName(i), r.getObject(i));
                res.add(tDoll);
            }
            allList = res;
            return res;
        } catch (SQLException t) {
            throw new RuntimeException(t);
        }
    }

    public static TDoll getByID(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return fetchAllList()
                    .parallelStream()
                    .filter(tDoll -> tDoll.getID() == id)
                    .findAny()
                    .orElse(null);
        else {
            for (TDoll tDoll: fetchAllList())
                if (tDoll.getID() == id)
                    return tDoll;
            return null;
        }
    }

    public int[] getTileset() {
        final String tileset = getValue("pattern");
        if (tileset == null) return null;
        final Matcher m = Pattern.compile("^\\[([\\d, ]+)]$").matcher(tileset);
        if (!m.find()) return null;

        final String[] split = m.group(1).split(", *");
        final int[] res = new int[split.length];
        for (int i = 0; i < res.length; i++)
            res[i] = Integer.parseInt(split[i]);
        return res;
    }
}
