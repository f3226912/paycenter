package cn.gdeng.paycenter.util.web.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * 序列化与反序列工具类
 * @author xiaojun
 *
 */
public class SerializeUtil {
	/**
	 * 序列化
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				closeOutputSource(oos,baos);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 反序列化
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		if(bytes == null){
			return null;
		}
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				colseInputSource(ois,bais);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 关闭输出资源
	 * @throws IOException 
	 */
	private static void closeOutputSource(ObjectOutputStream oos,ByteArrayOutputStream baos) throws IOException{
		if (oos!=null) {
			oos.close();
		}
		if (baos!=null) {
			baos.close();
		}
	}
	/**
	 * 关闭输入资源
	 * @throws IOException
	 */
	private static void colseInputSource(ObjectInputStream ois,ByteArrayInputStream bais) throws IOException{
		if (ois!=null) {
			ois.close();
		}
		if (bais!=null) {
			bais.close();
		}
	}
}