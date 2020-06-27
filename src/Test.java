import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {

	public static void main(String[] args) throws UnknownHostException, UnsupportedEncodingException {
		String str = "1:::127.0.0.1:10075";
		String[] arr = str.split(":::");
//		byte[] arr = new byte[] {-1};
//		System.out.println(new String(arr,0,arr.length,"utf-8"));
		if("�".equals("�"))
			System.out.println(true);
		if(arr[0] == "1")
			System.out.println(true);
		System.out.println(arr[1]);
//		System.out.println(InetAddress.getByName("127.0.0.1"));
	}

}
