package com.test.thread;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception{

        List<String> nonGlobalMessages = new ArrayList<>();
        for(int i=1;i<=5;i++){
            nonGlobalMessages.add("PF-Custo-"+i);
        }
//        for(int i=1;i<=10;i++){
//            nonGlobalMessages.add("PF-Custo-"+i);
//        }
//        for(int i=1;i<=10;i++){
//            nonGlobalMessages.add("PF-Custo-"+i);
//        }

        new Thread(()->{
            for(String s:nonGlobalMessages){
                try {
                    TaskDelegator.submitTask(s, "NON-GLOBAL");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


        List<String> globalMessages = new ArrayList<>();
        for(int i=1;i<=5;i++){
            globalMessages.add("PF-"+i);
        }
//        for(int i=1;i<=10;i++){
//            globalMessages.add("PF-"+i);
//        }
//        for(int i=1;i<=10;i++){
//            globalMessages.add("PF-"+i);
//        }
        new Thread(()->{
            for(String s:globalMessages){
                try {
                TaskDelegator.submitTask(s, "GLOBAL");
            }catch (Exception e){
                e.printStackTrace();
            }
            }
        }).start();
    }
}
