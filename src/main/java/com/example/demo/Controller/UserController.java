package com.example.demo.Controller;

import com.example.demo.Entitys.UserInfo;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Util.CommonResult;
import com.example.demo.Util.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Controller
@RestController
public class UserController {
    @Autowired UserMapper userMapper;
    //@Autowired JavaMailSender javaMailSender;
    @Autowired
    TokenHelper th;
    @PostMapping("/login")
    public CommonResult login(@RequestBody UserInfo userInfo){
        UserInfo checkUser = userMapper.findUserByNameAndPassWord(userInfo.getUsername());
        if(checkUser==null)
            return CommonResult.failed("用户不存在！");
        if(checkUser.getPassword() != null && checkUser.getPassword().equals(userInfo.getPassword()) && checkUser.getState())
        {
            String token = th.createToken();
            th.putToken(userInfo.getUsername(),token);

            return CommonResult.success(token,"登录成功");
        }
        else if(checkUser.getState())
            return CommonResult.failed("该用户尚未通过审核，请联系管理员。");
        else {
            return CommonResult.failed("登录失败，请检查账户信息");
        }
    }

    @GetMapping("/agree")
    public String agree(String username){
        UserInfo checkUser = userMapper.findUserByNameAndPassWord(username);
        if(checkUser==null)
            return "该用户不存在";
        //checkUser.setState(true);
        userMapper.setStateByUsername(true,checkUser.getUsername());
        return "审核成功";
    }

    @PostMapping("/register")
    public CommonResult register(@RequestBody UserInfo userInfo){
        UserInfo checkUser = userMapper.findUserByNameAndPassWord(userInfo.getUsername());
        //System.out.println(checkUser.getUsername());
        if(checkUser!=null){
            return CommonResult.failed("注册失败，该用户名已被注册");
        }
        int r = userMapper.addUser(userInfo);
        if(r!=0)
        {
            Properties prop = new Properties();//创建Java配置对象
            prop.setProperty("mail.transport.protocol", "smtp");//传输协议
            prop.setProperty("mail.smtp.host", "smtp.xxx.com");//邮箱服务器地址
            prop.setProperty("mail.smtp.auth","true");//是否需要身份验证
            prop.setProperty("mail.debug", "true");//是否显示日志信息
            prop.setProperty("mail.smtp.port", "25");//发送邮件端口号

            Session session = Session.getDefaultInstance(prop);//使用配置对象获取会话对象

            MimeMessage msg = new MimeMessage(session);//获取消息对象

            try {
                msg.setSentDate(new java.util.Date());//设置发件日期
                msg.setFrom(new InternetAddress("754667437@qq.com", "xxx"));//设置发件地址
                msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("wliyan_2021@tju.edu.cn"));//设置接收地址
                msg.addHeader("X-Company", "Produced By Micron Studio");//自定义头信息
                msg.setSubject("基因编辑新用户注册");//邮件主题 ( 即邮件标题 )
                msg.setContent("<html>A new user registered your system:"+userInfo.getUsername()+"\tEmail:"+userInfo.getEmailAdd()+"\tagree: http://136.243.59.69:8999/agree?username="+userInfo.getUsername()+"</html>", "text/html");//邮件内容以及 Content-Type

                Transport trans = session.getTransport("smtp");//获取传输对象
                trans.connect("smtp.qq.com", "754667437@qq.com", "vogyprfnxvdjbdgj");//使用账户密码连接邮件服务器
                trans.sendMessage(msg, msg.getAllRecipients());//发送消息
                System.out.println("审核邮件发送成功");
                //userInfo.setState(false);
                //userMapper.addUser(userInfo);
                trans.close();
            }catch (Exception e){

                System.out.println("邮件发送失败");
                e.printStackTrace();
            }
            return CommonResult.success("注册成功，请等待管理员审核");
        }
        else{
            return CommonResult.failed("注册失败");
        }
    }

}
