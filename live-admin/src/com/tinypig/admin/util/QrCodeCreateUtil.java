package com.tinypig.admin.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码生成和读的工具类
 *
 */
public class QrCodeCreateUtil {

	/**
	 * 生成包含字符串信息的二维码图片
	 * @param pathdir 输出路径
	 * @param file 输出文件名称（a.png）
	 * @param content 二维码携带信息
	 * @param qrCodeSize 二维码图片大小
	 * @param imageFormat 二维码的格式
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public static boolean createQrCode(String pathdir,String file,String content, int qrCodeSize, String imageFormat) throws Exception {
		int width = qrCodeSize; // 图像宽度
		int height = qrCodeSize; // 图像高度
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
		Path path = FileSystems.getDefault().getPath(pathdir, file);
		MatrixToImageWriter.writeToPath(bitMatrix, imageFormat, path);// 输出图像
		return true;
	}

	/**
	 * 读二维码并输出携带的信息
	 */
	public static String readQrCode(InputStream inputStream) throws IOException {
		// 从输入流中获取字符串信息
		BufferedImage image = ImageIO.read(inputStream);
		// 将图像转换为二进制位图源
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		Result result = null;
		try {
			result = reader.decode(bitmap);
		} catch (ReaderException e) {
			e.printStackTrace();
		}
		return result.getText();
	}

	/**
	 * 测试代码
	 * @throws WriterException 
	 */
	public static void main(String[] args) throws Exception {
		boolean result = createQrCode("d://","qrcode.jpg","http://192.168.20.251:8086/TinyPigWebServer/auth/qrregist?code=dVn6AnfuP5/RXVX2T3YT0XRMv8bgOYHAjgfXp5h+L5c=",900, "JPEG");
		System.out.println(result);
		// String text = readQrCode(new FileInputStream(new File("d:\\qrcode.jpg")));
		// System.out.println(text);
	}

}