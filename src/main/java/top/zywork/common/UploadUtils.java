package top.zywork.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import top.zywork.enums.MIMETypeEnum;
import top.zywork.enums.ResponseStatusEnum;
import top.zywork.vo.ResponseStatusVO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public static ResponseStatusVO upload(MultipartFile file, String allowedExts, long maxSize, String uploadParentDir, String uploadDir, int[][] compressSizes, float[] compressScales) {
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        if (fileSize > maxSize) {
            return ResponseStatusVO.dataError("超过最大文件限制，最大大小：" + maxSize + "MB", null);
        }
        if (!FileUtils.checkExt(fileName, allowedExts)) {
            return ResponseStatusVO.dataError("文件类型错误，后缀只能是：" + allowedExts, null);
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
    public static ResponseStatusVO upload(MultipartFile[] files, String allowedExts, long maxSize, String uploadParentDir, String uploadDir, int[][] compressSizes, float[] compressScales) {
        for (int i = 0, len = files.length; i < len; i++) {
            MultipartFile file = files[i];
            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            if (fileSize > maxSize) {
                return ResponseStatusVO.dataError("第" + (i + 1) + "个文件超过最大文件限制，最大大小：" + maxSize + "MB", null);
            }
            if (!FileUtils.checkExt(fileName, allowedExts)) {
                return ResponseStatusVO.dataError("第" + (i + 1) + "个文件类型错误，后缀只能是：" + allowedExts, null);
            }
        }
        int errorCount = 0;
        for (MultipartFile file : files) {
            ResponseStatusVO statusVO = save(file, file.getOriginalFilename(), uploadParentDir, uploadDir, compressSizes, compressScales);
            if (statusVO.getCode().intValue() == ResponseStatusEnum.DATA_ERROR.getCode()) {
                errorCount++;
            }
        }
        return ResponseStatusVO.ok(errorCount == 0 ? "所有文件上传成功"
                : errorCount == files.length ? "所有文件上传失败" : "部分文件上传上传失败，请检查后再次上传", null);
    }

    /**
     * 保存文件
     * @param file
     * @param fileName
     * @param uploadParentDir
     * @param uploadDir
     * @param compressSizes
     * @param compressScales
     * @return
     */
    private static ResponseStatusVO save(MultipartFile file, String fileName, String uploadParentDir, String uploadDir, int[][] compressSizes, float[] compressScales) {
        List<String> uploadFileNames = new ArrayList<>();
        String saveDir = FileUtils.mkdirs(uploadParentDir, uploadDir);
        try {
            String newFileName = FileUtils.newFileNameWithoutExt(fileName);
            String fullExt = FileUtils.getFullExt(fileName);
            File newFile = new File(saveDir, newFileName + fullExt);
            file.transferTo(newFile);
            uploadFileNames.add(newFileName + fullExt);
            if (compressSizes != null) {
                // 需要按指定大小压缩
                for (int[] size : compressSizes) {
                    String uploadFileName = uploadFileName(newFileName, fullExt, size);
                    String newFilePath = saveDir + File.separator + uploadFileName;
                    if (MIMETypeEnum.GIF.getExt().equalsIgnoreCase(fullExt)) {
                        // 压缩gif图片
                        ImageCompressUtils.compressGif(newFile.getAbsolutePath(), size[0], size[1], new FileOutputStream(new File(newFilePath)));
                    } else {
                        BufferedImage bufferedImage = ImageCompressUtils.compress(newFile.getAbsolutePath(), size[0], size[1]);
                        ImageUtils.saveImage(bufferedImage, newFilePath, MIMETypeEnum.findByValue(FileUtils.getExt(fileName)));
                    }
                    uploadFileNames.add(uploadFileName);
                }
            } else if (compressScales != null) {
                // 需要按指定比例压缩
                for (float scale : compressScales) {
                    String uploadFileName = uploadFileName(newFileName, fullExt, scale);
                    String newFilePath = saveDir + File.separator + uploadFileName;
                    if (MIMETypeEnum.GIF.getExt().equalsIgnoreCase(fullExt)) {
                        // 压缩gif图片
                        ImageCompressUtils.compressGif(newFile.getAbsolutePath(), scale, new FileOutputStream(new File(newFilePath)));
                    } else {
                        BufferedImage bufferedImage = ImageCompressUtils.compress(newFile.getAbsolutePath(), scale);
                        ImageUtils.saveImage(bufferedImage, newFilePath, MIMETypeEnum.findByValue(FileUtils.getExt(fileName)));
                    }
                    uploadFileNames.add(uploadFileName);
                }
            }
            return ResponseStatusVO.ok("文件上传成功", uploadFileNames);
        } catch (IOException e) {
            logger.error("save upload file error: {}", e.getMessage());
            return ResponseStatusVO.dataError("文件上传错误，稍候再试", null);
        }
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
    public static ResponseStatusVO uploadImg(MultipartFile file, String allowedExts, long maxSize, String uploadParentDir, String uploadDir, int[][] compressSizes) {
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
    public static ResponseStatusVO uploadImg(MultipartFile file, String allowedExts, long maxSize, String uploadParentDir, String uploadDir, float[] compressScales) {
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
    public static ResponseStatusVO uploadImgs(MultipartFile[] files, String allowedExts, int maxSize, String uploadParentDir, String uploadDir, int[][] compressSizes) {
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
    public static ResponseStatusVO uploadImgs(MultipartFile[] files, String allowedExts, long maxSize, String uploadParentDir, String uploadDir, float[] compressScales) {
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
    public static ResponseStatusVO uploadFile(MultipartFile file, String allowedExts, long maxSize, String uploadParentDir, String uploadDir) {
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
    public static ResponseStatusVO uploadFiles(MultipartFile[] files, String allowedExts, long maxSize, String uploadParentDir, String uploadDir) {
        return upload(files, allowedExts, maxSize, uploadParentDir, uploadDir, null, null);
    }

    private static String uploadFileName(String fileNameWithoutExt, String fullExt, int[] size) {
        return fileNameWithoutExt + "_" + size[0] + "x" + size[1] + fullExt;
    }

    private static String uploadFileName(String fileNameWithoutExt, String fullExt, float scale) {
        return fileNameWithoutExt + "_" + (int) (scale * 10) + fullExt;
    }

}
