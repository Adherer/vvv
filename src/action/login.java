package action;

import Main.Main;

/**
 * Created by Administrator on 2015/6/4.
 */
public class login {
    public String user;
    public String pass;
    public String login(){
        String ret= Main.users.login(user,pass);
        if(ret.equals("LoginSuccess")) {
            Main.log("Login:"+user+" IP:"+Main.getIP());
            Main.getSession().setAttribute("user", Main.users.getUser(user));
            Main.getOut().println("{\"ret\":\"LoginSuccess\"}");
            //return "success";
        }else {
            Main.getOut().println("{\"ret\":\""+ret+"\"}");
        }
        //else return "error";
        return "none";
    }
}
