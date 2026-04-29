import java.util.*;
import java.sql.*;

public class Timetable {
    List<TimeSlot> list = new ArrayList<>();

    public void loadFromDB() {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM timetable WHERE user_id=?"
            );
            ps.setInt(1, LoginUI.userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Node room = new Node(rs.getString("room"),0,0);
                list.add(new TimeSlot(
                        rs.getInt("start_time"),
                        rs.getInt("end_time"),
                        room,
                        rs.getString("subject")
                ));
            }
        } catch(Exception e){ e.printStackTrace(); }
    }

    public void add(int sh,int sm,int eh,int em,Node room,String sub) {
        int start = sh*60+sm;
        int end = eh*60+em;

        list.add(new TimeSlot(start,end,room,sub));

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO timetable(user_id,subject,start_time,end_time,room) VALUES(?,?,?,?,?)"
            );

            ps.setInt(1, LoginUI.userId);
            ps.setString(2, sub);
            ps.setInt(3, start);
            ps.setInt(4, end);
            ps.setString(5, room.name);

            ps.executeUpdate();
        } catch(Exception e){ e.printStackTrace(); }
    }

    public TimeSlot getCurrent(int now) {
        for(TimeSlot t:list)
            if(now>=t.start && now<t.end) return t;
        return null;
    }

    public TimeSlot getNext(int now) {
        TimeSlot next=null;
        int min=Integer.MAX_VALUE;

        for(TimeSlot t:list){
            if(t.start>now && t.start-now<min){
                min=t.start-now;
                next=t;
            }
        }
        return next;
    }
}
