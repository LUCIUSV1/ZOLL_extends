package org.lucius.utils;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

//import com.dao.BaseDao;
//import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

public class ControlFile {

	// 根据str,font的样式以及输出文件目录
	public static void createImage(String str, Font font, File outFile,
								   Integer width, Integer height) throws Exception {
		// 创建图片
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();
		g.setClip(0, 0, width, height);
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);// 先用黑色填充整张图片,也就是背景
		g.setColor(Color.black);// 在换成黑色
		g.setFont(font);// 设置画笔字体
		/** 用于获得垂直居中y */
		Rectangle clip = g.getClipBounds();
		FontMetrics fm = g.getFontMetrics(font);
		int ascent = fm.getAscent();
		int descent = fm.getDescent();
		int y = (clip.height - (ascent + descent)) / 2 + ascent;
		for (int i = 0; i < 6; i++) {// 256 340 0 680
			g.drawString(str, i * 680, y);// 画出字符串
		}
		g.dispose();
		ImageIO.write(image, "png", outFile);// 输出png图片
	}
	//解析PDF内容

	public static String getFileListame(String strPath) {
		File file = new File(strPath);
		File[] fs = file.listFiles();
		Arrays.sort(fs, new CompratorByLastModified());
		for (int i = 0; i < fs.length; i++) {
			return fs[i].getName();
		}
		return "";
	}
	public static String read(String path, String encoding) throws IOException {
		String content = "";
		File file = new File(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), encoding));
		String line = null;
		while ((line = reader.readLine()) != null) {
			content += line + "\n";
		}
		reader.close();
		return content;
	}

	public static String readFileContent(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		StringBuffer sbf = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempStr;
			while ((tempStr = reader.readLine()) != null) {
				sbf.append(tempStr);
			}
			reader.close();
			return sbf.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return sbf.toString();
	}

	public static String readFile(String path) throws Exception {
		File file =new File(path);
		InputStream in=new FileInputStream(file);//实例化FileInputStream
		int fileLength=(int)file.length();
		byte b[]=new byte[fileLength];
		in.close();
		return new String(b);
	}

	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
//			BaseDao.log.error("ɾ���ļ�ʧ��:" + fileName + "�����ڣ�");
			return false;
		} else {
			if (file.isFile())
				return deleteFile(fileName);
			else
				return deleteDirectory(fileName);
		}
	}

	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// ����ļ�·������Ӧ���ļ����ڣ�������һ���ļ�����ֱ��ɾ��
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
//				BaseDao.log.error("ɾ�������ļ�" + fileName + "ʧ�ܣ�");
				return false;
			}
		} else {
//			BaseDao.log.error("ɾ�������ļ�ʧ�ܣ�" + fileName + "�����ڣ�");
			return false;
		}
	}

	public static boolean deleteDirectory(String dir) {
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		File dirFile = new File(dir);
		if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
			return false;
		}
		boolean flag = true;
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				flag = ControlFile.deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
			else if (files[i].isDirectory()) {
				flag =ControlFile.deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			return false;
		}
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	//判断文件夹内是否有文件
	public static boolean getDir(String paString) {

		File file = new File(paString);
		File[] listFiles = file.listFiles();
		if (listFiles.length > 0) {

			return true;
		} else {

			return false;
		}
	}

}
