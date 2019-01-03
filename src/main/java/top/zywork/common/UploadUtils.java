package top.zywork.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import top.zywork.enums.MIMETypeEnum;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件上传工具类，上传到本地服务器<br/>
 *
 * 创建于2018-12-29<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class UploadUtils {

    private static final Logger logger = LoggerFactory.getLogger(UploadUtils.class);

    /**
     * 单文件上传，如果是图片，可指定是否对图片进行压缩
     * @param file 单个文件
     * @param allowedExts 允许的后缀
     * @param maxSize 最大文件大小，单位byte
     * @param uploadParentDir 上传父目录
     * @param uploadDir 上传子目录，可以带有下级目录
     * @param compressSizes 指定图片压缩大小，二维数组的形式指定多个压缩大小，如{{200, 200}, {500, 500}}。第一个数字为宽度，第二个数字为高度
     * @param compressScales 指定图片压缩比例，如{0.8, 0.5}表示分别需要按0.8和0.5的比例进行压缩
     * @return
     */
    public static String upload(MultipartFile file, String allowedExts, long maxSize, String uploadParentDir, String uploadDir, int[][] compressSizes, float[] compressScales) {
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        if (fileSize > maxSize) {
            return "超过最大文件限制，最大大小：" + maxSize + "MB";
        }
        if (!FileUtils.checkExt(fileName, allowedExts)) {
            return "文件类型错误，后缀只能是：" + allowedExts;
        }
        return save(file, fileName, uploadParentDir, uploadDir, compressSizes, compressScales);
    }

    /**
     * 多文件上传，如果是图片，可指定是否对图片进行压缩
     * @param files 多个文件
     * @param allowedExts 允许的后缀
     * @param maxSize 最大文件大小，单位byte
     * @param uploadParentDir 上传父目录
     * @param uploadDir 上传子目录，可以带有下级目录
     * @param compressSizes 指定图片压缩大小，二维数组的形式指定多个压缩大小，如{{200, 200}, {500, 500}}。第一个数字为宽度，第二个数字为高度
     * @param compressScales 指定图片压缩比例，如{0.8, 0.5}表示分别需要按0.8和0.5的比例进行压缩
     * @return
     */
    public static String upload(MultipartFile[] files, String allowedExts, long maxSize, String uploadParentDir, String uploadDir, int[][] compressSizes, float[] compressScales) {
        for (int i = 0, len = files.length; i < len; i++) {
            MultipartFile file = files[i];
            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            if (fileSize > maxSize) {
                return "第" + (i + 1) + "个文件超过最大文件限制，最大大小：" + maxSize + "MB";
            }
            if (!FileUtils.checkExt(fileName, allowedExts)) {
                return "第" + (i + 1) + "个文件类型错误，后缀只能是：" + allowedExts;
            }
            return save(file, fileName, uploadParentDir, uploadDir, compressSizes, compressScales);
        }
        return null;
    }

    /**
     * 保存文件
     * @param file
     * @param fileName
     * @param uploadParentDir
     * @param uploadDir
     * @param compressSizes
     * @return
     */
    private static String save(MultipartFile file, String fileName, String uploadParentDir, String uploadDir, int[][] compressSizes, float[] compressScales) {
        String saveDir = FileUtils.mkdirs(uploadParentDir, uploadDir);
        try {
            String newFileName = FileUtils.newFileNameWithoutExt(fileName);
            String fullExt = FileUtils.getFullExt(fileName);
            File newFile = new File(saveDir, newFileName + fullExt);
            file.transferTo(newFile);
            if (compressSizes != null) {
                // 需要按指定大小压缩
                for (int[] size : compressSizes) {
                    String newFilePath = saveDir + File.separator + newFileName + "_" + size[0] + "x" + size[1] + fullExt;
                    if (FileUtils.checkExt(fileName, ".gif")) {
                        // 压缩gif图片
                        ImageCompressUtils.compressGif(newFile.getAbsolutePath(), size[0], size[1], new FileOutputStream(new File(newFilePath)));
                    } else {
                        BufferedImage bufferedImage = ImageCompressUtils.compress(newFile.getAbsolutePath(), size[0], size[1]);
                        ImageUtils.saveImage(bufferedImage, newFilePath, MIMETypeEnum.findByValue(FileUtils.getExt(fileName)));
                    }
                }
            } else if (compressScales != null) {
                // 需要按指定比例压缩
                for (float scale : compressScales) {
                    String newFilePath = saveDir + File.separator + newFileName + "_" + (int) (scale * 10) + fullExt;
                    if (FileUtils.checkExt(fileName, ".gif")) {
                        // 压缩gif图片
                        ImageCompressUtils.compressGif(newFile.getAbsolutePath(), scale, new FileOutputStream(new File(newFilePath)));
                    } else {
                        BufferedImage bufferedImage = ImageCompressUtils.compress(newFile.getAbsolutePath(), scale);
                        ImageUtils.saveImage(bufferedImage, newFilePath, MIMETypeEnum.findByValue(FileUtils.getExt(fileName)));
                    }
                }
            }
        } catch (IOException e) {
            logger.error("save upload file error: {}", e.getMessage());
            return "文件上传错误，稍候再试";
        }
        return null;
    }

    /**
     * 单图片上传
     * @param file 单个图片文件
     * @param allowedExts 允许的后缀
     * @param maxSize 最大文件大小，单位byte
     * @param uploadParentDir 上传父目录
     * @param uploadDir 上传子目录，可以带有下级目录
     * @param compressSizes 指定图片压缩大小，二维数组的形式指定多个压缩大小，如{{200, 200}, {500, 500}}。第一个数字为宽度，第二个数字为高度
     * @return
     */
    public static String uploadImg(MultipartFile file, String allowedExts, long maxSize, String uploadParentDir, String uploadDir, int[][] compressSizes) {
        return upload(file, allowedExts, maxSize, uploadParentDir, uploadDir, compressSizes, null);
    }

    /**
     * 单图片上传
     * @param file 单个图片文件
     * @param allowedExts 允许的后缀
     * @param maxSize 最大文件大小，单位byte
     * @param uploadParentDir 上传父目录
     * @param uploadDir 上传子目录，可以带有下级目录
     * @param compressScales 指定图片压缩比例，如{0.8, 0.5}表示分别需要按0.8和0.5的比例进行压缩
     * @return
     */
    public static String uploadImg(MultipartFile file, String allowedExts, long maxSize, String uploadParentDir, String uploadDir, float[] compressScales) {
        return upload(file, allowedExts, maxSize, uploadParentDir, uploadDir, null, compressScales);
    }

    /**
     * 多图片上传
     * @param files 多个图片文件
     * @param allowedExts 允许的后缀
     * @param maxSize 最大文件大小，单位byte
     * @param uploadParentDir 上传父目录
     * @param uploadDir 上传子目录，可以带有下级目录
     * @param compressSizes 指定图片压缩大小，二维数组的形式指定多个压缩大小，如{{200, 200}, {500, 500}}。第一个数字为宽度，第二个数字为高度
     * @return
     */
    public static String uploadImgs(MultipartFile[] files, String allowedExts, int maxSize, String uploadParentDir, String uploadDir, int[][] compressSizes) {
        return upload(files, allowedExts, maxSize, uploadParentDir, uploadDir, compressSizes, null);
    }

    /**
     * 多图片上传
     * @param files 多个图片文件
     * @param allowedExts 允许的后缀
     * @param maxSize 最大文件大小，单位byte
     * @param uploadParentDir 上传父目录
     * @param uploadDir 上传子目录，可以带有下级目录
     * @param compressScales 指定图片压缩比例，如{0.8, 0.5}表示分别需要按0.8和0.5的比例进行压缩
     * @return
     */
    public static String uploadImgs(MultipartFile[] files, String allowedExts, long maxSize, String uploadParentDir, String uploadDir, float[] compressScales) {
        return upload(files, allowedExts, maxSize, uploadParentDir, uploadDir, null, compressScales);
    }

    /**
     * 单文件上传
     * @param file 单个文件
     * @param allowedExts 允许的后缀
     * @param maxSize 最大文件大小，单位byte
     * @param uploadParentDir 上传父目录
     * @param uploadDir 上传子目录，可以带有下级目录
     * @return
     */
    public static String uploadFile(MultipartFile file, String allowedExts, long maxSize, String uploadParentDir, String uploadDir) {
        return upload(file, allowedExts, maxSize, uploadParentDir, uploadDir, null, null);
    }

    /**
     * 多文件上传
     * @param files 多个文件
     * @param allowedExts 允许的后缀
     * @param maxSize 最大文件大小，单位byte
     * @param uploadParentDir 上传父目录
     * @param uploadDir 上传子目录，可以带有下级目录
     * @return
     */
    public static String uploadFiles(MultipartFile[] files, String allowedExts, long maxSize, String uploadParentDir, String uploadDir) {
        return upload(files, allowedExts, maxSize, uploadParentDir, uploadDir, null, null);
    }

}
