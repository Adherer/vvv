package util.CodeCompare.cplusplus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.Main;
import entity.Contest;
import entity.statu;
import util.HTML.HTML;
import util.HTML.TableHTML;

/**
 * Created by Syiml on 2015/7/29 0029.
 */
public class ContestCodeCompare {
    Contest c;
    List<Integer> status;
    List<ContestCodeCompareRecord> list;
    public ContestCodeCompare(int cid){
        this.c=Main.contests.getContest(cid);
    }
    private void setCompareOne(int rid1,int rid2,statu s1,statu s2,ContestCodeCompareRecord cccr){
        cccr.rid1=rid1;
        cccr.rid2=rid2;
        cccr.user1=s1.getUser();
        cccr.user2=s2.getUser();
        cccr.pid=s1.getPid();
        cccr.f=Main.codeCmp(Main.getCode(rid1), Main.getCode(rid2));
    }
    public void compareAll(double minf){//将该rid和其他所有rid小于它的代码计算重复率
        List<Integer> plist=c.getProblemList();
        list=new ArrayList<ContestCodeCompareRecord>();
        for(int pid:plist){
            status=Main.contests.getAcRidFromCidPid(c.getCid(),pid);
            ContestCodeCompareRecord cccr=new ContestCodeCompareRecord();
            for(int i=0;i<status.size();i++){
                statu s1=Main.status.getStatu(status.get(i));
                for(int j=i+1;j<status.size();j++){
                    statu s2=Main.status.getStatu(status.get(j));
                    if(s1.getPid()==s2.getPid()&&!s1.getUser().equals(s2.getUser())){
                        setCompareOne(status.get(i),status.get(j),s1,s2,cccr);
                        if(cccr.f>=minf){
                            list.add(cccr);
                            cccr=new ContestCodeCompareRecord();
                        }
                    }
                }
            }
        }
    }
    public String HTML(double minf){
        //输出重复率大于minf的记录
        compareAll(minf);
        Collections.sort(list);
        TableHTML table=new TableHTML();
        table.setClass("table");
        table.addColname("rid1","user1","rid2","user2","pid", "重复率","查看");
        for(int i=0;i<list.size();i++){
            ContestCodeCompareRecord cccr=list.get(i);
            if(cccr.f<minf) break;
            table.addRow(cccr.rid1+"",cccr.user1,cccr.rid2+"",cccr.user2, Main.contests.getContest(c.getCid()).getProblemId(cccr.pid)+"",
                    String.format("%.2f",list.get(i).f*100)+"%",
                    HTML.aNew("code2.jsp?rid=" + cccr.rid1 + "&rid2=" + cccr.rid2, "对比"));
        }
        list.clear();
        status.clear();
        return HTML.panelnobody("代码检测", table.HTML());
    }
}
