package org.web.servlet.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.web.framework.action.support.InitOperations;
import org.web.servlet.FileUploadAdvice;
import org.web.util.FileFormatHelper;
import org.web.util.IPTimeStamp;

import tool.mastery.core.CollectionUtil;
import tool.mastery.core.IOUtil;
import tool.mastery.log.Logger;

class DefaultFileUploadAdvice implements FileUploadAdvice {

	public static final Logger LOG = Logger.getLogger(DefaultFileUploadAdvice.class);
	
	/**
	 * @Fields items : 保存全部的上传内容
	 */
	private List<FileItem> items = null;

	/**
	 * @Fields params : 保存所有的参数
	 */
	private Map<String, List<String>> params = new HashMap<String, List<String>>();

	/**
	 * @Fields files : 保存所有文件
	 */
	private Map<String, FileItem> files = new HashMap<String, FileItem>();

	/**
	 * @Fields maxSize : 默认最大文件上传大小
	 */
	private int maxSize = MAX_SIZE;
	
	/**
	 * <p>
	 * Title:FileUploadTools
	 * </p>
	 * <p>
	 * Description: 构造函数
	 * </p>
	 * 
	 * @param request
	 *            request请求
	 * @param maxSize
	 *            最大上传大小
	 * @param tempDir
	 *            存放临时文件的目录
	 * @throws FileUploadException
	 * @throws IOException
	 * @throws Exception
	 */
	public DefaultFileUploadAdvice(HttpServletRequest request, int maxSize,
			String tempDir) throws IOException {
		// 处理磁盘工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 如果此文件不为空，则设置为临时文件存储区域
		if (tempDir != null) {
			tempDir = this.getFullPath(tempDir);
			File tempFile = new File(tempDir);
			if (!tempFile.exists()) { // 判断此文件夹是否存在
				tempFile.mkdirs(); // 创建一个新的文件夹
			}
			factory.setRepository(tempFile);
		}
		// 创建处理工具
		ServletFileUpload upload = new ServletFileUpload(factory);
		if (maxSize > 0) {
			this.maxSize = maxSize;
		}
		// 文件名中文处理
		upload.setHeaderEncoding("utf-8");
		// 设置最大上传文件大小
		upload.setFileSizeMax(this.maxSize);
		try {
			this.items = upload.parseRequest(request); // 接收全部内容
		} catch (FileUploadException e) {
			throw new IOException(e);// 向上抛出异常
		}
		this.init();
	}

	/**
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * 
	 * @Title: init
	 * 
	 * @Description: TODO 初始化参数，区分普通参数和上传文件
	 * 
	 * @param @throws UnknownHostException
	 * @param @throws UnsupportedEncodingException
	 * @return void 返回类型
	 * 
	 * @throws
	 */
	private void init() throws IOException {
		// 随机的IP数字名
		IPTimeStamp its = new IPTimeStamp(InetAddress.getLocalHost()
				.getHostAddress());
		// 依次取出每一个上传项
		for (FileItem item : this.items) {
			if (item.isFormField()) { // 判断是否是普通的文本参数
				// 取得表单的名称
				String fieldName = item.getFieldName();
				// 取得表单的内容
				String fieldValue = new String(item.getString().getBytes(
						"ISO-8859-1"), "utf-8");
				// 保存内容
				List<String> temp = null;
				if (this.params.containsKey(fieldName)) { // 判断内容是否已经存放
					temp = this.params.get(fieldName); // 如果存在则取出
				} else { // 如果不存在
					temp = new ArrayList<String>(); // 重新开辟List数组
				}
				// 向List集合中设置内容
				temp.add(fieldValue);
				// 向Map中增加内容
				this.params.put(fieldName, temp);
			} else {
				if (!item.getName().equalsIgnoreCase("")) {
					String fileName = its.getIPTimeRand() + "."
							+ item.getName().split("\\.")[1];
					this.files.put(fileName, item); // 保存全部上传文件
				}
			}
		}
	}

	@Override
	public Map<String, String> upload(String savePath , boolean isModifyName) throws IOException {
		// TODO Auto-generated method stub
		Map<String, String> names = this.saveAll(savePath , isModifyName);
		if (names.size() == 0) {
			return null;
		}
		return names;
	}

	@Override
	public Map<String, FileItem> getAllFiles() {
		// TODO Auto-generated method stub
		return this.files;
	}

	@Override
	public int getMaxSize() {
		// TODO Auto-generated method stub
		return this.maxSize;
	}

	@Override
	public Map<String, List<String>> getReqeustParams() {
		// TODO Auto-generated method stub
		return this.params;
	}

	/**
	 * 
	 * @Title: saveAll
	 * 
	 * @Description: TODO 保存所有文件,并返回文件名称，所有异常跑抛出
	 * 
	 * @param @param saveDir 保存文件的文件夹名
	 * @param @return
	 * @param @throws IOException
	 * 
	 * @return List<String> 返回类型
	 * 
	 * @throws
	 */
	@SuppressWarnings("unused")
	private Map<String, String> saveAll(String saveDir , boolean isModifyName) throws IOException {
		/*File file = new File(getFullPath(saveDir));
		if (!file.exists()) {
			file.mkdirs();
		}*/
		Map<String, String> names = new HashMap<String, String>();
		if (this.files.size() > 0) {
			// 取得全部的key
			Set<String> keys = this.files.keySet();
			File saveFile = null; // 定义保存的文件
			// 定义文件的输入流，用于读取源文件
			InputStream ips = null;
			// 定义文件的输出流，用于保存文件
			OutputStream ops = null;
			// 循环所有的key取出每一个上传文件
			for (String key : keys) {
				// 依次取出每一个文件
				FileItem item = this.files.get(key);
				String fileName = null;
				if(isModifyName) {
					fileName = new IPTimeStamp(InetAddress.getLocalHost()
							.getHostAddress()).getIPTimeRand()
							+ "."
							+ item.getName().split("\\.")[1];
				}else {
					fileName = item.getName();
				}
				String formatName = FileFormatHelper.getFormat(item.getName().split("\\.")[1]);
				File file = new File(getFullPath(saveDir + "/" + formatName));
				if (!file.exists()) {
					file.mkdirs();
				}
				// 重新拼凑出新的路径
				saveFile = new File(file.getAbsolutePath() + "/" + fileName);
				// 保存生成后的文件
				names.put(item.getFieldName(), saveFile.getAbsolutePath());
				// 将此加入到参数列表当中
				this.params.put(
						item.getFieldName(),
						CollectionUtil.convertObjectToList(saveFile.getAbsolutePath()));
				try {
					ips = item.getInputStream();
					ops = new FileOutputStream(saveFile); // 定义输出流保存文件
					int temp = 0; // 接收每一个字节
					byte[] data = new byte[512]; // 开辟空间分块保存
					while ((temp = ips.read(data, 0, 512)) != -1) {
						ops.write(data);
					}
				} catch (IOException e) {
					throw e;
				} finally {
					// 关闭输入输出流
					IOUtil.close(ips);
					IOUtil.close(ops);
				}
			}
		}
		return names; // 返回生成后的文件名称
	}

	private String getFullPath(String path) {
		return InitOperations.ServicesPath + path;
	}
}
