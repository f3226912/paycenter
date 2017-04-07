package cn.gdeng.paycenter.util.web.api;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 随机唯一ID产生器
 * 
 * @author panmin
 */
public class RandomIdGenerator {

	private static Random random;

	private static String table;

	static {
		random = new Random();
		table = "0123456789";
	}

	/**
	 * 获取唯一随机数
	 */
	public synchronized static String randomId(long id) {
		String ret = null;
		String num = String.format("%05d", id);
		int key = random.nextInt(10), seed = random.nextInt(100);
		CaesarUtil caesar = new CaesarUtil(table, seed);
		num = caesar.encode(key, num);
		ret = num + String.format("%01d", key) + String.format("%02d", seed);
		return ret;
	}
	
	@SuppressWarnings("static-access")
    public static void main(String[] args) {
		RandomIdGenerator r = new RandomIdGenerator();
		Map<String, String> map = new HashMap<String, String>();
		int size = 999999;
		for (int i = 0; i < size; i += 1) {
			String orderNumber = r.randomId(i);
			System.out.println(i + "  " + orderNumber);
			if (map.containsKey(orderNumber)) {
				System.out.println("发现重复数" + orderNumber);
				return;
			} else {
				map.put(orderNumber, orderNumber);
			}
			if (i == size - 1) {
				System.out.println("finish");
			}
		}
	}
	
	public static class CaesarUtil {
		
		private String table;
		
		private int seedA = 1103515245;
		
		private int seedB = 12345;

		public CaesarUtil(String table, int seed) {
			this.table = chaos(table, seed, table.length());
		}

		public CaesarUtil(String table) {
			this(table, 11);
		}

		public CaesarUtil() {
			this(11);
		}

		public CaesarUtil(int seed) {
			this("ABCDEFGHIJKLMNOPQRSTUVWXYZ", seed);
		}

		public char dict(int i, boolean reverse) {
			int s = table.length(), index = reverse ? s - i : i;
			return table.charAt(index);
		}

		public int dict(char c, boolean reverse) {
			int s = table.length(), index = table.indexOf(c);
			return reverse ? s - index : index;
		}

		public int seed(int seed) {
			long temp = seed;
			return (int) ((temp * seedA + seedB) & 0x7fffffffL);
		}

		public String chaos(String data, int seed, int cnt) {
			StringBuffer buf = new StringBuffer(data);
			char tmp;
			int a, b, r = data.length();
			for (int i = 0; i < cnt; i += 1) {
				seed = seed(seed);
				a = seed % r;
				seed = seed(seed);
				b = seed % r;
				tmp = buf.charAt(a);
				buf.setCharAt(a, buf.charAt(b));
				buf.setCharAt(b, tmp);
			}
			return buf.toString();
		}

		public String crypto(boolean reverse, int key, String text) {
			String ret = null;
			StringBuilder buf = new StringBuilder();
			int m, s = table.length(), e = text.length();

			for (int i = 0; i < e; i += 1) {
				m = dict(text.charAt(i), reverse);
				if (m < 0)
					break;
				m = m + key + i;
				buf.append(dict(m % s, reverse));
			}
			if (buf.length() == e)
				ret = buf.toString();
			return ret;
		}

		public String encode(int key, String text) {
			return crypto(false, key, text);

		}

		public String decode(int key, String text) {
			return crypto(true, key, text);
		}

		public static void main(String[] args) {
			CaesarUtil caesar = new CaesarUtil();
			System.out.println(caesar.table);
			String data = caesar.encode(35, "APPLE");
			
			System.out.println(data);
			String s2 = caesar.decode(35, data);
			System.out.println(s2);
			System.out.println("ABC".charAt(1));
		}
	}
}
