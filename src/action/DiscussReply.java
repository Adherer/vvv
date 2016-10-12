package action;


import dao.Discuss.DiscussSQL;
import entity.User;
import servise.DiscussMain;
import util.Main;
import servise.MessageMain;
import util.MainResult;
import util.Tool;

/**
 * Created by Syiml on 2015/7/5 0005.
 */
public class DiscussReply extends BaseAction{
    public String text;
    public int id;//discuss id
    public int rid;//reply id
    public int rrid;//replyReply id;

    public String dr(){
        try{
            User u=Main.loginUser();
            Tool.log(u.getUsername()+"回复了id为"+id+"的帖子");
            MessageMain.discussAt(id,text,true);
            return DiscussSQL.reply(u,id,text);
        }catch(NumberFormatException e){
            return "error";
        }
    }
    public String hideshow(){
        return DiscussSQL.hideshow(id,rid);
    }
    public String adminReply(){
        Tool.log("管理员"+Main.loginUser().getUsername()+"回复了id为"+id+"的回复");
        return DiscussSQL.adminReply(id,rid,text);
    }
    public String replyReply(){
        MainResult mr = DiscussMain.replyReply(this);
        setPrompt(mr.getPrompt());
        if(mr == MainResult.SUCCESS) return SUCCESS;
        return ERROR;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getRrid() {
        return rrid;
    }

    public void setRrid(int rrid) {
        this.rrid = rrid;
    }
}
