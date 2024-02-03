package com.example.demo.Util;


import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TokenHelper {

    private static Map<String, String>  map = new HashMap<String, String>();;
    final private static int toklen = 6;

    public TokenHelper() {
        map.put("123abc", "test");
    }

    //一个用户登录需要生成一个登录识别码token

    public String createToken() {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i = 0 ; i < toklen ; i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public void putToken(String username, String token) {
        if(map.size()>500){
            map.clear();
            map.put("123abc", "test");
        }
        map.put(token, username);
        System.out.println(map);
    }

    public void deleteToken(String token) {
        map.remove(token);
        System.out.println(map);
    }

    public boolean searchToken(String token) {
        if(map.containsKey(token)) {
            return true;
        } else{
            return false;
        }
    }


    public String getName(String token) {
        return map.get(token);
    }


    public boolean isNull(String token) {
        if(token.equals(null)) {
            return true;
        } else{
            return false;
        }
    }




}
