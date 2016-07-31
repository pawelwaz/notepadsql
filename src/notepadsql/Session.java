/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepadsql;

/**
 *
 * @author pawel
 */
public class Session {
    private static String userName;
    private static Integer userId;
    private static String host;
    
    public String getUserName() {
        return userName;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setUserName(String name) {
        userName = name;
    }
    
    public void setUserId(Integer id) {
        userId = id;
    }
    
    public void setHost(String newHost) {
        host = newHost;
    }
}
