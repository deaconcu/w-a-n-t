import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by deacon on 2017/9/9.
 */
public class Test {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("^user_id=(\\d+)&session_token=(\\w+)$");
        Matcher m = pattern.matcher("user_id=1001&session_token=123456");
        m.find();
        System.out.println(m.group(0));
        System.out.println(m.group(1));
        System.out.println(m.group(2));
    }
}
