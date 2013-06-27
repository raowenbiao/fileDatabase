package com.raowenbiao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.kuanter.util.PathUtil;

/**
 * 简单文件数据库
 * 
 * @author Rao Wenbiao
 * 
 */
public class TxtDB {
	private String basePath = new PathUtil().getWebRoot() + "db/";
	private String dbPath = "";
	/**邮箱验证数据*/
	public static final String mailVerifyDB = "mailVerify.db";

	public TxtDB() {
		dbPath = basePath + "default.db";

	}

	public TxtDB(String fileName) {
		if (fileName == null || fileName.equals("")) {
			dbPath = basePath + "default.db";
		} else {
			if (fileName.contains("."))
				dbPath = basePath + fileName;
			else {
				dbPath = basePath + fileName + ".db";
			}
		}

	}
	/**
	 * 
	 *@Description:获取唯一字符串
	 *@author Rao Wenbiao
	 * 2013-6-24 上午9:14:09
	 */
	public static String getRandomKey() {
		return "" + System.currentTimeMillis() + new Random().nextInt(99999);
	}

	/**
	 * 
	 * @Description:通过key查询数据
	 * @author Rao Wenbiao 2013-6-22 下午4:02:40 TxtDB Map<String,String>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> find(String key) {
		return find(key, Map.class);
	}
	/**
	 * 
	 *@Description:通过key查询数据
	 *@author Rao Wenbiao
	 * 2013-6-24 上午9:14:44
	 *TxtDB
	 *T
	 */
	public <T> T find(String key, Class<T> clazz) {
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(dbPath);
			props.load(in);
			return JSON.parseObject(props.getProperty(key), clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 *@Description:保存数据(add)自动生成key
	 *@author Rao Wenbiao
	 * 2013-6-24 上午9:15:00
	 *TxtDB
	 *String
	 */
	public String save(Object data) {
		String key = getRandomKey();
		save(key, data);
		return key;
	}

	/**
	 * 
	 *@Description:保存数据（addOrUpdata）
	 *@author Rao Wenbiao
	 * 2013-6-24 上午9:15:52
	 *TxtDB
	 *void
	 */
	public void save(String key, Object data) {
		Properties prop = new Properties();
		InputStream in = null;
		OutputStream out = null;
		try {
			File file = new File(dbPath);
			if (!file.exists()){
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
				

			in = new FileInputStream(file);
			prop.load(in);
			out = new FileOutputStream(dbPath);
			prop.setProperty(key, JSON.toJSONString(data));
			prop.store(out, "save:" + key);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 *@Description:删除数据
	 *@author Rao Wenbiao
	 * 2013-6-24 上午9:17:14
	 *TxtDB
	 *void
	 */
	public void delete(String key) {
		Properties prop = new Properties();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(dbPath);
			// 从输入流中读取属性列表（键和元素对）
			prop.load(in);
			out = new FileOutputStream(dbPath);
			prop.remove(key);
			prop.store(out, "delete" + key);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 *@Description:删除数据
	 *@author Rao Wenbiao
	 * 2013-6-24 上午9:17:14
	 *TxtDB
	 *void
	 */
	public void clear() {
		Properties prop = new Properties();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(dbPath);
			// 从输入流中读取属性列表（键和元素对）
			prop.load(in);
			out = new FileOutputStream(dbPath);
			prop.clear();
			prop.store(out, "clear");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
