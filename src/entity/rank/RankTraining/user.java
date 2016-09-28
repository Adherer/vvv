package entity.rank.RankTraining;

import entity.Result;
import entity.rank.RankBaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Syiml on 2015/8/11 0011.
 */
public class user extends RankBaseUser{
    int pnum;
    List<Integer> num;//0未提交 正数表示第i次提交首次通过 负数表示错误次数

    public user(){}
    public void init(String username,int valid,int pnum){
        super.init(username,valid,pnum);
        num=new ArrayList<Integer>();
        for(int i=0;i<pnum;i++){
            num.add(0);
        }
    }
    public void addres(int pid,Result s,int len){
        if(pid<0) return;
        if(num.get(pid)<=0) {
            if (s == Result.AC) {
                num.set(pid, -num.get(pid) + 1);
            } else {
                num.set(pid, num.get(pid) - 1);//错误一次
            }
        }
    }

    public int getAcNum(){
        int ret=0;
        for(Integer i:num){
            if(i>=1) ret++;
        }
        return  ret;
    }
    public int getWrongNum(){
        int ret=0;
        for(Integer i:num){
            if(i>=1) ret+=i-1;
        }
        return ret;
    }

    @Override
    public int compareTo(RankBaseUser o) {
        user u = (user)o;
        int ac=getAcNum();
        int uac=u.getAcNum();
        if(ac==uac){
            return getWrongNum()-u.getWrongNum();
        }else{
            return uac-ac;
        }
    }

    @Override
    protected boolean noRank() {
        return getAcNum() == 0;
    }
}
