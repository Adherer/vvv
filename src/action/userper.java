package action;

import Main.Main;

/**
 * Created by Syiml on 2015/8/19 0019.
 */
public class userper {

    public String getUser() {
        return user;
    }
    public String getPerid() {
        return perid;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setPerid(String perid) {
        this.perid = perid;
    }

    public String user;
    public String perid;

    public String add(){
        if(Main.loginUserPermission().getPermissionAdmin())
            Main.users.addPer(user,Integer.parseInt(perid));
        return "success";
    }
    public String del(){
        if(Main.loginUserPermission().getPermissionAdmin())
            Main.users.delPer(user,Integer.parseInt(perid));
        return "success";
    }
}
