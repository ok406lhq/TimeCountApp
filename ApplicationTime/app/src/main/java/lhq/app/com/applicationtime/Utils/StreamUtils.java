package lhq.app.com.applicationtime.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class StreamUtils {
    public static String ReadFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len = 0;
        byte[] bys = new byte[1024];
        while ((len = is.read(bys)) != -1) {
            out.write(bys, 0, len);
        }
        String result = out.toString();
        out.close();
        is.close();
        return result;
    }
}
