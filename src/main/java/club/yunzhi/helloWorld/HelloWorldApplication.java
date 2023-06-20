package club.yunzhi.helloWorld;

//import com.dingtalk.api.DefaultDingTalkClient;
//import com.dingtalk.api.DingTalkClient;
//import com.dingtalk.api.request.OapiRobotSendRequest;
//import com.dingtalk.api.response.OapiRobotSendResponse;
//import com.taobao.api.ApiException;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
@RestController
@Configuration  //标记配置类
@EnableScheduling   //开启定时任务
public class HelloWorldApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);
    }

    public Date lastTime;
    public boolean flag = false;

    @RequestMapping("helloWorld")
    public void helloWorld() throws ApiException {
        // 记录本次接受时间
        this.lastTime = new Date();
        if (this.flag) {
            powerRestoration();
            this.flag = false;
        }
    }

    // 定时任务. 1分钟 * 3 = 3
    // 添加定时任务
    @Scheduled(cron = "0 0/3 * * * ?")
    private void myTasks() throws ParseException, ApiException {
        Date now = new Date();
        if (this.lastTime != null) {
            if (now.getTime() - this.lastTime.getTime() > 3 * 60 * 1000) {
                this.flag = true;
                outage();
            }
        }
    }


    public void outage () throws ApiException {

        //创建钉钉请求客户端
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=78602734ce542105a3bd0367d773dfba1ad10904cff8878e3ae5b57acf0c0d81");
        //创建机器人请求
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        //指定机器人发送消息类型 为text
        request.setMsgtype("text");
        //创建发送文本内容信息
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        //设置文本内容
        text.setContent("市电断电 ！\n" +
                "请修复 \n" +
                "联系：13900000000");
        request.setText(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        //通过手机号指定艾特的用户
//        at.setAtMobiles(Arrays.asList("1888888*****"));
        // isAtAll是否艾特全部，true 为艾特全部
        at.setIsAtAll(true);
        //指定艾特的用户人信息
//        at.setAtUserIds(Arrays.asList("109929","32099"));
        request.setAt(at);
        OapiRobotSendResponse response = client.execute(request);
//        log.info("钉钉消息返回结果为：{}",response.isSuccess());
    }

    public void powerRestoration () throws ApiException {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=78602734ce542105a3bd0367d773dfba1ad10904cff8878e3ae5b57acf0c0d81");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        SimpleDateFormat recoverTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    //24小时制
        text.setContent("市电已恢复 ！\n" +
                "恢复时间: \n" + recoverTime.format(this.lastTime)
                );
        request.setText(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setIsAtAll(true);
        request.setAt(at);
        OapiRobotSendResponse response = client.execute(request);
    }

//    public Date lastTime;
//    public Date nowTime;
//
//    public String lastTimeStr = "";
//    public String nowTimeStr = "";
//
//
//
//    @RequestMapping("helloWorld")
//    public String helloWorld() {
//        // 记录本次接受时间
//        Date date = new Date();
//        SimpleDateFormat arduino = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    //24小时制
//        this.lastTimeStr = arduino.format(date);
//        System.out.println(arduino.format(date));   //24小时制
//        return this.lastTimeStr;
//    }
//
//    // 定时任务. 1分钟 * 3 = 3
//    //添加定时任务
//    @Scheduled(cron = "0 0/3 * * * ?")
//    private void myTasks() throws ParseException {
//        long Distance = 1000 * 60 * 3;
//        Date date = new Date();
//        SimpleDateFormat nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        this.nowTimeStr = nowTime.format(date);
//        this.lastTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.lastTimeStr);
//        this.nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.nowTimeStr);
//        if (this.lastTime != null) {
//            if (this.nowTime.getTime() - this.lastTime.getTime() > Distance) {
//                System.out.println("超出3分钟" + LocalDateTime.now());
//                System.out.println("断电，发送消息");
//            }
//            System.out.println("当前时间" + LocalDateTime.now());
//        }
//    }
}

