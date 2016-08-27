package util;

import WebSocket.MatchWebSocket;
import dao.*;
import entity.Status;
import util.CodeCompare.cplusplus.CPlusPlusCompare;
import util.GlobalVariables.GlobalVariables;
import entity.Permission;
import entity.User;
import entity.Problem;
import util.HTML.problemHTML;
import action.addLocalProblem;
import action.addproblem1;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2015/5/21.
 * 实现业务逻辑和一些简单的杂项功能
 */
public class Main {
    public static final JSONObject GV=GlobalVariables.read();
    public static DBConnectionPool conns;
    public static ProblemSQL problems = new ProblemSQL();
    public static statusSQL status = new statusSQL();
    public static UserSQL users = new UserSQL();
    public static LogDao logs = new LogDao();

    public static Submitter submitter=new SubmitterImp();

    //常量获取
    final public static int problemShowNum=GV.getInt("problemShowNum");//每页显示的题目数量
    final public static int statuShowNum=GV.getInt("statuShowNum");//statu每页显示数量
    final public static int contestShowNum=GV.getInt("contestShowNum");//contest每页显示数量
    final public static int userShowNum=GV.getInt("userShowNum");//user每页显示数量
    final public static int discussShowNum=GV.getInt("discussShowNum");//discuss的显示数量
    final public static int autoConnectionTimeMinute=GV.getInt("autoConnectionTimeMinute");
    public static boolean isDebug=GV.getBoolean("debug");
    public static String version=GV.getString("version");
    public static Map<Integer,Set<MatchWebSocket>> sockets=new HashMap<Integer, Set<MatchWebSocket>>();

    public static void Init(){
        try {
            Class.forName(Main.GV.get("sqlclass").toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        conns = new DBConnectionPool();
        status.init();
    }
    public static String addProblem(addproblem1 action){
        Problem p=new Problem(action.getOjid(),action.getOjspid(),action.getTitle(),action.getAuthor(),action.getIsSpj()!=null);
        problems.addProblem(action.getPid(), p);
        return "success";
    }
    public static String addLocalProblem(addLocalProblem action){
        Problem p=new Problem(action.title);
        p.Author=action.getAuthor();
        p.spj = action.isSpj;
        int newpid=problems.addProblem(-1,p);
        problemHTML ph=new problemHTML();
        ph.setInt64("%I64d");
        ph.setTimeLimit(action.getTime() + "MS");
        ph.setMenoryLimit(action.getMemory() + "MB");
        problems.saveProblemHTML(newpid, ph);
        FILE.createDirectory(newpid);
        return "success";
    }
    public static String editLocalProblem(addLocalProblem action){
        int pid=Integer.parseInt(action.getPid());
        Problem p=Main.problems.getProblem(pid);
        p.Title=action.getTitle();
        p.Author=action.getAuthor();
        p.spj = action.isSpj;
        Main.problems.editProblem(pid,p);
        problemHTML ph=Main.problems.getProblemHTML(pid);
        ph.setTimeLimit(action.getTime()+"MS");
        ph.setMenoryLimit(action.getMemory()+"MB");
        problems.delProblemDis(pid);
        problems.saveProblemHTML(pid,ph);
        return "success";
    }
    public static String setProblemVisiable(int pid){
        return problems.setProblemVisiable(pid);
    }

    public static Permission getPermission(String user){
        return users.getPermission(user);
    }
    public static String getCode(int rid){
        return status.getStatu(rid).getCode();
    }

    public static String getRealPath(String s){
        return getSession().getServletContext().getRealPath(s);
    }

    public static String uploadFile(File upload,String path) throws IOException {
        InputStream is=new FileInputStream(upload);
        OutputStream os=new FileOutputStream(path);
        byte buffer[] = new byte[1024];
        int count=0;
        while((count=is.read(buffer))>0){
            os.write(buffer,0,count);
        }
        os.close();
        is.close();
        return "success";
    }
    public static HttpSession getSession(){
        return ServletActionContext.getRequest().getSession();
    }
    public static PrintWriter getOut(){
        try {
            return ServletActionContext.getResponse().getWriter();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void saveURL(){
        String url=getRequest().getRequestURI();
        if(getRequest().getQueryString()!=null){
            url+="?"+getRequest().getQueryString();
        }
        getSession().setAttribute("url",url);
    }
    public static void saveURL(String s){
        getSession().setAttribute("url",s);
    }
    public static HttpServletRequest getRequest(){
        return ServletActionContext.getRequest();
    }
    public static Permission loginUserPermission(){
        User u=loginUser();
        if(u==null) return new Permission();
        else return getPermission(u.getUsername());
    }
    public static User loginUser(){
        try{
            return (User)getSession().getAttribute("user");
        }catch (Exception e){
            return null;
        }
    }


    public static boolean canViewCode(Status s,User user) {
        return user != null && (user.getUsername().equals(s.getUser()) || Main.users.haveViewCode(user.getUsername(), s.getPid()) || user.getPermission().getViewCode());
    }
    public static String getIP(){
        return getRequest().getRemoteAddr();
    }

    public static double codeCmp(String code1,String code2){
        CPlusPlusCompare cmp = new CPlusPlusCompare();
        return cmp.getSimilarity(code1,code2);
    }
}
