package marc.com.maildemo.test;

import com.sun.mail.imap.IMAPMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;

/**
 * Created by wangcd on 2018/2/22.
 */

public class IMAPReceiveMailTest {
    public static void main(String[] args) throws Exception {
        // 准备连接服务器的会话信息
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", "imap.163.com");
        props.setProperty("mail.imap.port", "143");

        // 创建Session实例对象
        Session session = Session.getInstance(props);

        // 创建IMAP协议的Store对象
        Store store = session.getStore("imap");

        // 连接邮件服务器
        store.connect("broderick_wang@163.com", "Wg3213299");

        // 获得收件箱
        Folder folder = store.getFolder("INBOX");
        // 以读写模式打开收件箱
        folder.open(Folder.READ_WRITE);

        // 获得收件箱的邮件列表
        Message[] messages = folder.getMessages();

        // 打印不同状态的邮件数量
        System.out.println("收件箱中共" + messages.length + "封邮件!");
        System.out.println("收件箱中共" + folder.getUnreadMessageCount() + "封未读邮件!");
        System.out.println("收件箱中共" + folder.getNewMessageCount() + "封新邮件!");
        System.out.println("收件箱中共" + folder.getDeletedMessageCount() + "封已删除邮件!");

        System.out.println("------------------------开始解析邮件----------------------------------");

        // 解析邮件
        for (Message message : messages) {
            IMAPMessage msg = (IMAPMessage) message;
            String subject = MimeUtility.decodeText(msg.getSubject());
            System.out.println("[" + subject + "]未读，是否需要阅读此邮件（yes/no）？");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String answer = reader.readLine();
            if ("yes".equalsIgnoreCase(answer)) {
//                POP3ReceiveMailTest.parseMessage(msg);  // 解析邮件
                // 第二个参数如果设置为true，则将修改反馈给服务器。false则不反馈给服务器
                msg.setFlag(Flags.Flag.SEEN, true);   //设置已读标志
            }
        }

        // 关闭资源
        folder.close(false);
        store.close();
    }
}
