package action;

import util.Main;

/**
 * Created by Syiml on 2015/6/15 0015.
 */
public class rejudge {
    int rid;
    int pid;
    int status;
    public int getRid(){return rid;}
    public void setRid(int rid){this.rid=rid;}

    public int getPid() {
        return pid;
    }

    public int getStatus() {
        return status;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String rej(){
        if(!Main.loginUserPermission().getReJudge()) return "error";
        Main.submitter.reJudge(rid);
        return "success";
    }
    public String rejudge(){
        Main.submitter.reJudge(pid,rid,status);
        return "success";
    }
}
