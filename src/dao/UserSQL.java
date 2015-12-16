package dao;

import util.Main;
import entity.Permission;
import entity.RegisterUser;
import entity.User;
import servise.MessageMain;
import util.HTML.HTML;
import util.SQL;
import action.edit;
import util.Tool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/6/3.
 */
public class UserSQL {
    /*
    * users(username,password,nick,gender,Email,motto,registertime,type,solved,submissions,Mark)
    * permission(id,name)
    * userper(username,perid)
    * */
    public int register(User u){
        SQL sql1=new SQL("select * from users where username=?",u.getUsername());
        try {
            ResultSet s=sql1.query();
            s.next();
            if(s.isLast()) return -1;//已存在
        }catch (SQLException e){
            return 0;
        }finally {
            sql1.close();
        }
        new SQL("insert into users values(?,md5(?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                , u.getUsername()
                , u.getPassword()
                , HTML.HTMLtoString(u.getNick())
                , u.getGender()
                , HTML.HTMLtoString(u.getSchool())
                , HTML.HTMLtoString(u.getEmail())
                , HTML.HTMLtoString(u.getMotto())
                , u.getRegistertime()
                , u.getType()
                , HTML.HTMLtoString(u.getMark())
                , -100000
                , 0,0,"","","","","","",0,0,0).update();
        MessageMain.addMessageWelcome(u);
        return 1;
    }
    public int getRank(String user){
        SQL sql=new SQL("select rank+1 from v_user where username=?", user);
        ResultSet rs=sql.query();
        try {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql.close();
        return -1;
    }
    public Permission getPermission(String username){
        if(username==null) return null;
        SQL sql=new SQL("select perid from userper where username=?",username);
        ResultSet rs=sql.query();
        Permission p=new Permission(rs);
        sql.close();
        return p;
    }
    public List<List<String>> getPermissionTable(){
        //user,per,admin
        SQL sql=new SQL("SELECT username,perid,(select name from permission where id=perid) as name FROM userper");
        ResultSet rs=sql.query();
        List<List<String>> table=new ArrayList<List<String>>();
        try {
            while(rs.next()){
                List<String> row=new ArrayList<String>();
                row.add(rs.getString("username"));
                row.add(rs.getString("name"));
                row.add(HTML.a("delper.action?user="+rs.getString("username")+"&perid="+rs.getString("perid"),"删除"));
                table.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql.close();
        return table;
    }
    public void addPer(String user,int per){
        new SQL("INSERT INTO userper values(?,?)",user,per).update();
    }
    public void delPer(String user,int per){
        new SQL("delete from userper where username=? and perid=?",user,per).update();
    }

    public User getUser(String username){
        return getUser(username,false);
    }
    public User getUserHaveRank(String username){
        return getUser(username,true);
    }
    private User getUser(String username,boolean rank){
        SQL sql;
        if(rank)sql=new SQL("select username,nick,gender,school,Email,motto,registertime,type,Mark,rating,rank+1 as rank,ratingnum,acb,name,faculty,major,cla,no,phone,inTeamLv,inTeamStatus from v_user where username=? ",username);
        else sql=new SQL("SELECT username,nick,gender,school,Email,motto,registertime,type,Mark,rating,-1 as rank,ratingnum,acb,name,faculty,major,cla,no,phone,inTeamLv,inTeamStatus from users where username=?",username);
        return sql.queryBean(User.class);
    }
    public List<User> getUsers(int from,int num,String serach,String order,boolean desc){
        if(order==null||order.equals("")){
            order="rank";
        }
        SQL sql;
        if(serach==null||serach.equals("")){
            sql=new SQL("select username,nick,gender,school,Email,motto,registertime,type,Mark,rating,rank+1 as rank,ratingnum,acb,acnum,name,faculty,major,cla,no,phone,inTeamLv,inTeamStatus " +
                    " from v_user "+
                    " ORDER BY "+order+(desc?" desc ":" ")+
                    " LIMIT "+from+","+num);
        }else{
            sql=new SQL("select username,nick,gender,school,Email,motto,registertime,type,Mark,rating,rank+1 as rank,ratingnum,acb,acnum,name,faculty,major,cla,no,phone,inTeamLv,inTeamStatus  from v_user where  (username like ? or nick like ?)  " +
                    " ORDER BY "+order+(desc?" desc ":" ")+
                    " LIMIT "+from+","+num,"%"+serach+"%","%"+serach+"%");
        }
        return sql.queryBeanList(User.class);
    }
    public int getUsersNum(String search){
        return new SQL("select count(*) from users where (username like ? or nick like ?)","%"+search+"%","%"+search+"%").queryNum();
    }
    public List<User> getRichTop10(){
        return new SQL("select username,nick,gender,school,Email,motto,registertime,type,Mark,rating,rank+1 as rank,ratingnum,acb,name,faculty,major,cla,no,phone,inTeamLv,inTeamStatus from v_user order by acb desc,rating desc " +
                "LIMIT 0,10").queryBeanList(User.class);
    }
    public List<List<String>> getUsers(int cid,int from,int num,String serach,boolean is3){
        List<List<String>> list=new ArrayList<List<String>>();
        SQL sql=new SQL("select * from v_contestuser where cid=? and (username like ? or nick like ?) ORDER BY time desc" +
                " LIMIT "+from+","+num,cid,"%"+serach+"%","%"+serach+"%");
        try {
            ResultSet rs=sql.query();
            while(rs.next()){
                List<String> s=new ArrayList<String>();
                String nick=rs.getString("nick");
                if(nick==null){
                    s.add(rs.getString("username"));
                    if(is3){
                        s.add("");
                        s.add("");
                        s.add("");
                        s.add("");
                        s.add("");
                        s.add("");
                    }
                    s.add(HTML.textb("未注册","red"));
                    s.add("");
                }else{
                    //HTML.a("UserInfo.jsp?user=" + rs.getString("username"), rs.getString("username"));
                    s.add(rs.getString("username"));
                    if(is3){
                        s.add(rs.getString("name"));
                        s.add(rs.getInt("gender")+"");
                        s.add(rs.getString("faculty"));
                        s.add(rs.getString("major"));
                        s.add(rs.getString("cla"));
                        s.add(rs.getString("no"));
                    }
                    s.add(rs.getString("nick"));
                    s.add(User.ratingToHTML(User.getShowRating(rs.getInt("ratingnum"), rs.getInt("rating"))));
                }
                if(rs.getInt("statu")==3){
                    s.add(
                            HTML.a("javascript:retry_show('" + rs.getString("username") +
                                    "','" +
                                    rs.getString("info") +
                                    "','" +
                                    cid +
                                    "'," +
                                    (Main.loginUser()!=null && Main.loginUser().getUsername().equals(rs.getString("username"))) +
                                    ")"
                                    , RegisterUser.statuToHTML(rs.getInt("statu"))));
                }
                else s.add(RegisterUser.statuToHTML(rs.getInt("statu")));
                Timestamp t=rs.getTimestamp("time");
                if(t==null){
                    s.add("");
                }else{
                    s.add(t.toString().substring(0, 19));
                }
                s.add(rs.getString("info"));
                list.add(s);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }finally {
            sql.close();
        }
    }
    public List<User> getRegisterUsers(int cid){
        SQL sql=new SQL("select * from v_contestuser where cid=?",cid);
        List<User> list=new ArrayList<User>();
        ResultSet rs=sql.query();
        try {
            while(rs.next()){
                User u=new User();
                u.setNick(rs.getString("nick"));
                u.setUsername(rs.getString("username"));
                u.setName(rs.getString("name"));
                u.setGender(rs.getInt("gender"));
                u.setFaculty(rs.getString("faculty"));
                u.setMajor(rs.getString("major"));
                u.setCla(rs.getString("cla"));
                u.setNo(rs.getString("no"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
        }
        return list;
    }
    public static int getUsersNum(int cid,String serach){
        SQL sql=new SQL("select count(*) from v_contestuser where cid=? and (username like ? or nick like ?)",cid,"%"+serach+"%","%"+serach+"%");
        ResultSet rs=sql.query();
        try {
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
        }
        return 0;
    }
    public static int getUsersNum(int cid,int st){
        SQL sql=new SQL("select count(*) from contestuser where cid=? and statu=?",cid,st);
        ResultSet rs=sql.query();
        try {
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
        }
        return 0;
    }

    public String login(String user,String pass){
        SQL sql=new SQL("select username,password from users where username=? and password=md5(?)", user, pass);
        try {
            ResultSet r=sql.query();
            if(r.next()){
                return "LoginSuccess";
            }else{
                SQL sql2=new SQL("select * from users where username=?", user);
                ResultSet rs=sql2.query();
                if(rs.next()){
                    sql2.close();
                    return "WrongPassword";
                }else{
                    sql2.close();
                    return "NoSuchUser";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
        }
        return "SystemError";
    }
    public boolean update(User u){
        Tool.log("Edit:"+u.getUsername());
        String sql="UPDATE users set ";
        if(u.getPassword()!=null) sql+="password = md5('"+u.getPassword()+"'),";
        sql+=" nick = '"+u.getNick()+"'";
        sql+=",name = '"+u.getName()+"'";
        sql+=",gender = "+u.getGender();
        sql+=",school = '"+u.getSchool()+"'";
        sql+=",faculty = '"+u.getFaculty()+"'";
        sql+=",major = '"+u.getMajor()+"'";
        sql+=",cla = '"+u.getCla()+"'";
        sql+=",no = '"+u.getNo() +"'";
        sql+=",phone = '"+ u.getPhone()+"'";
        sql+=",Email = '"+u.getEmail()+"'";
        sql+=",motto = '"+u.getMotto()+"'";
        sql+=",inTeamLv = "+u.getInTeamLv();
        sql+=",inTeamStatus = "+u.getInTeamStatus();
        sql+=" WHERE username=?";
        Tool.log(sql);
        return (new SQL(sql, u.getUsername()).update()==1);
    }

    public boolean haveViewCode(String user,int pid){
        SQL sql=new SQL("SELECT * FROM t_viewcode WHERE username=? AND pid=?", user, pid);
        ResultSet rs=sql.query();
        try {
            return rs.next();
        } catch (SQLException e) {
            return false;
        } finally {
            sql.close();
        }
    }
    public Set<Integer> canViewCode(String user){
        SQL sql=new SQL("SELECT pid FROM t_viewcode WHERE username=?",user);
        ResultSet rs=sql.query();
        Set<Integer> list=new HashSet<Integer>();
        try {
            while(rs.next()){
                list.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
        }
        return list;
    }
    public int addViewCode(String user,int pid){
        return new SQL("INSERT INTO t_viewcode VALUES(?,?)",user,pid).update();
    }

    public int awardACB(String user,int num,String text){
        if(num<0){
            num=-num;
            int ret=subACB(user,num);
            if(ret>0){
                MessageMain.subMessageAwardACB(user,num,text);
                Tool.log("管理员扣去"+user+" "+num+"ACB"+" 备注："+text);
            }
            return ret;
        }
        addACB(user,num);
        int x=MessageMain.addMessageAwardACB(user,num,text);
        Tool.log("管理员给"+user+" "+num+"ACB"+" 备注："+text);
        return x;
    }
    public int addACB(String user,int num){
        return new SQL("UPDATE users SET acb=acb+? WHERE username=?",num,user).update();
    }
    public int subACB(String user,int num){
        return new SQL("UPDATE users SET acb=acb-? WHERE username=? AND acb>=?",num,user,num).update();
    }

}
