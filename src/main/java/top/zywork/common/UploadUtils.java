package top.zywork.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
     * @param compress 是否开启图片压缩
     * @return
     */
    public static String upload(MultipartFile file, String allowedExts, int maxSize, String uploadParentDir, String uploadDir, boolean compress) {
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        if (fileSize > maxSize) {
            return "超过最大文件限制，最大大小：" + maxSize + "MB";
        }
        if (!FileUtils.checkExt(fileName, allowedExts)) {
            return "文件类型错误，后缀只能是：" + allowedExts;
        }
        String saveDir = FileUtils.mkdirs(uploadParentDir, uploadDir);
        try {
            file.transferTo(new File(saveDir, FileUtils.newFileName(fileName)));
        } catch (IOException e) {
            logger.error("save upload file error: {}", e.getMessage());
            return "文件上传错误，稍候再试";
        }
        return null;
    }

    /**
     * 多文件上传，如果是图片，可指定是否对图片进行压缩
     * @param files 多个文件
     * @param allowedExts 允许的后缀
     * @param maxSize 最大文件大小，单位byte
     * @param uploadParentDir 上传父目录
     * @param uploadDir 上传子目录，可以带有下级目录
     * @param compress 是否开启图片压缩
     * @return
     */
    public static String upload(MultipartFile[] files, String allowedExts, int maxSize, String uploadParentDir, String uploadDir, boolean compress) {
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
            String saveDir = FileUtils.mkdirs(uploadParentDir, uploadDir);
            try {
                file.transferTo(new File(saveDir, FileUtils.newFileName(fileName)));
            } catch (IOException e) {
                logger.error("save upload file error: {}", e.getMessage());
                return "文件上传错误，稍候再试";
            }
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
     * @param compress 是否开启图片压缩
     * @return
     */
    public static String uploadImg(MultipartFile file, String allowedExts, int maxSize, String uploadParentDir, String uploadDir, boolean compress) {
        return upload(file, allowedExts, maxSize, uploadParentDir, uploadDir, compress);
    }

    /**
     * 多图片上传
     * @param files 多个图片文件
     * @param allowedExts 允许的后缀
     * @param maxSize 最大文件大小，单位byte
     * @param uploadParentDir 上传父目录
     * @param uploadDir 上传子目录，可以带有下级目录
     * @param compress 是否开启图片压缩
     * @return
     */
    public static String uploadImgs(MultipartFile[] files, String allowedExts, int maxSize, String uploadParentDir, String uploadDir, boolean compress) {
        return upload(files, allowedExts, maxSize, uploadParentDir, uploadDir, compress);
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
    public static String uploadFile(MultipartFile file, String allowedExts, int maxSize, String uploadParentDir, String uploadDir) {
        return upload(file, allowedExts, maxSize, uploadParentDir, uploadDir, false);
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
    public static String uploadFiles(MultipartFile[] files, String allowedExts, int maxSize, String uploadParentDir, String uploadDir) {
        return upload(files, allowedExts, maxSize, uploadParentDir, uploadDir, false);
    }

}
