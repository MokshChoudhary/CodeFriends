package com.futurework.codefriends.Service;

public class Service {

    public String removeExtraFromString(final String string){

        StringBuilder s = new StringBuilder();
        for(int i=0;i<string.length();i++){
            if(string.charAt(i)!='.'&&string.charAt(i)!='@'&&string.charAt(i)!='_'&&string.charAt(i)!='-'){
                s.append(string.charAt(i));
            }
        }

        return s.toString();
    }

}
