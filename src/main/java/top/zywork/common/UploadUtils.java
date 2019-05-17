package top.zywork.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.zywork.constant.FileConstants;
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
 * 可指定按大小或按比例压缩图片，如原图文件名为abc.png，则按200*200大小压缩后的
 * 文件名为：abc_200x200.png，按0.5比例压缩后的文件名为:abc_5.png
 * <p>
 * 创建于2018-12-29<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Slf4j
public class UploadUtils {

    /**
     * 检测上传文件是否符合格式及大小要求
     *
     * @param file
     * @param allowedExts 允许的后缀
     * @param maxSize     最大文件大小，单位byte
     * @return
     */
    public static ResponseStatusVO checkFile(MultipartFile file, String allowedExts, long maxSize) {
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        if (fileSize > maxSize) {
            return ResponseStatusVO.dataError("超过最大文件限制，最大大小：" + maxSize / 1024 / 1024 + "MB", null);
        }
        if (!FileUtils.checkExt(fileName, allowedExts)) {
            return ResponseStatusVO.dataError("文件类型错误，后缀只能是：" + allowedExts, null);
        }
        return ResponseStatusVO.ok("文件检测通过", null);
    }

    /**
     * 检测上传的多个文件是否符合格式及大小要求
     *
     * @param files
     * @param allowedExts 允许的后缀
     * @param maxSize     最大文件大小，单位byte
     * @return
     */
    public static ResponseStatusVO checkFiles(MultipartFile[] files, String allowedExts, long maxSize) {
        StringBuilder errorInfo = new StringBuilder();
        for (int i = 0, len = files.length; i < len; i++) {
            MultipartFile file = files[i];
            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            if (fileSize > maxSize) {
                errorInfo.append("第").append(i + 1).append("个文件超过最大文件限制，最大大小：").append(maxSize / 1024 / 1024).append("MB\n");
            }
            if (!FileUtils.checkExt(fileName, allowedExts)) {
                errorInfo.append("第").append(i + 1).append("个文件类型错误，后缀只能是：").append(allowedExts).append("\n");
            }
        }
        if (StringUtils.isEmpty(errorInfo.toString())) {
            return ResponseStatusVO.ok("所有文件检测通过", null);
        }
        return ResponseStatusVO.dataError(errorInfo.toString(), null);
    }

    /**
     * 单文件上传，如果是图片，可指定是否对图片进行压缩
     *
     * @param file            单个文件
     * @param uploadParentDir 上传父目录
     * @param uploadDir       上传子目录，可以带有下级目录
     * @param compressSizes   指定图片压缩大小，二维数组的形式指定多个压缩大小，如{{200, 200}, {500, 500}}。第一个数字为宽度，第二个数字为高度
     * @param compressScales  指定图片压缩比例，如{0.8, 0.5}表示分别需要按0.8和0.5的比例进行压缩
     * @return
     */
    public static ResponseStatusVO upload(MultipartFile file, String uploadParentDir, String uploadDir, int[][] compressSizes, float[] compressScales) {
        return save(file, file.getOriginalFilename(), uploadParentDir, uploadDir, compressSizes, compressScales);
    }

    /**
     * 多文件上传，如果是图片，可指定是否对图片进行压缩，返回的ResponseStatusVO的data是所有文件上传后的文件名列表
     *
     * @param files           多个文件
     * @param uploadParentDir 上传父目录
     * @param uploadDir       上传子目录，可以带有下级目录
     * @param compressSizes   指定图片压缩大小，二维数组的形式指定多个压缩大小，如{{200, 200}, {500, 500}}。第一个数字为宽度，第二个数字为高度
     * @param compressScales  指定图片压缩比例，如{0.8, 0.5}表示分别需要按0.8和0.5的比例进行压缩
     * @return
     */
    public static ResponseStatusVO upload(MultipartFile[] files, String uploadParentDir, String uploadDir, int[][] compressSizes, float[] compressScales) {
        int errorCount = 0;
        List<Object> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            ResponseStatusVO statusVO = save(file, file.getOriginalFilename(), uploadParentDir, uploadDir, compressSizes, compressScales);
            fileNames.add(statusVO.getData());
            if (statusVO.getCode().intValue() == ResponseStatusEnum.DATA_ERROR.getCode()) {
                errorCount++;
            }
        }
        return ResponseStatusVO.ok(errorCount == 0 ? "所有文件上传成功"
                : errorCount == files.length ? "所有文件上传失败" : errorCount + "个文件上传失败，请检查后再次上传", fileNames);
    }

    /**
     * 保存文件
     *
     * @param file
     * @param fileName
     * @param uploadParentDir
     * @param uploadDir
     * @param compressSizes
     * @param compressScales
     * @return
     */
    private static ResponseStatusVO save(MultipartFile file, String fileName, String uploadParentDir, String uploadDir, int[][] compressSizes, float[] compressScales) {
        String saveDir = FileUtils.mkdirs(uploadParentDir, uploadDir);
        String newFileName = FileUtils.newFileNameWithoutExt(fileName);
        String fullExt = FileUtils.getFullExt(fileName);
        File newFile = new File(saveDir, newFileName + fullExt);
        try {
            file.transferTo(newFile);
            if (compressSizes != null) {
                // 需要按指定大小压缩
                for (int[] size : compressSizes) {
                    String uploadFileName = uploadFileName(newFileName, fullExt, size);
                    String newFilePath = saveDir + FileConstants.SEPARATOR + uploadFileName;
                    if (MIMETypeEnum.GIF.getExt().equalsIgnoreCase(fullExt)) {
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
                    String uploadFileName = uploadFileName(newFileName, fullExt, scale);
                    String newFilePath = saveDir + FileConstants.SEPARATOR + uploadFileName;
                    if (MIMETypeEnum.GIF.getExt().equalsIgnoreCase(fullExt)) {
                        // 压缩gif图片
                        ImageCompressUtils.compressGif(newFile.getAbsolutePath(), scale, new FileOutputStream(new File(newFilePath)));
                    } else {
                        BufferedImage bufferedImage = ImageCompressUtils.compress(newFile.getAbsolutePath(), scale);
                        ImageUtils.saveImage(bufferedImage, newFilePath, MIMETypeEnum.findByValue(FileUtils.getExt(fileName)));
                    }
                }
            }
            return ResponseStatusVO.ok("文件上传成功", newFileName + fullExt);
        } catch (IOException e) {
            log.error("save upload file error: {}", e.getMessage());
            return ResponseStatusVO.dataError("文件上传错误，稍候再试", null);
        }
    }

    /**
     * 单图片上传
     *
     * @param file            单个图片文件
     * @param uploadParentDir 上传父目录
     * @param uploadDir       上传子目录，可以带有下级目录
     * @param compressSizes   指定图片压缩大小，二维数组的形式指定多个压缩大小，如{{200, 200}, {500, 500}}。第一个数字为宽度，第二个数字为高度
     * @return
     */
    public static ResponseStatusVO uploadImg(MultipartFile file, String uploadParentDir, String uploadDir, int[][] compressSizes) {
        return upload(file, uploadParentDir, uploadDir, compressSizes, null);
    }

    /**
     * 单图片上传
     *
     * @param file            单个图片文件
     * @param uploadParentDir 上传父目录
     * @param uploadDir       上传子目录，可以带有下级目录
     * @param compressScales  指定图片压缩比例，如{0.8, 0.5}表示分别需要按0.8和0.5的比例进行压缩
     * @return
     */
    public static ResponseStatusVO uploadImg(MultipartFile file, String uploadParentDir, String uploadDir, float[] compressScales) {
        return upload(file, uploadParentDir, uploadDir, null, compressScales);
    }

    /**
     * 多图片上传
     *
     * @param files           多个图片文件
     * @param uploadParentDir 上传父目录
     * @param uploadDir       上传子目录，可以带有下级目录
     * @param compressSizes   指定图片压缩大小，二维数组的形式指定多个压缩大小，如{{200, 200}, {500, 500}}。第一个数字为宽度，第二个数字为高度
     * @return
     */
    public static ResponseStatusVO uploadImgs(MultipartFile[] files, String uploadParentDir, String uploadDir, int[][] compressSizes) {
        return upload(files, uploadParentDir, uploadDir, compressSizes, null);
    }

    /**
     * 多图片上传
     *
     * @param files           多个图片文件
     * @param uploadParentDir 上传父目录
     * @param uploadDir       上传子目录，可以带有下级目录
     * @param compressScales  指定图片压缩比例，如{0.8, 0.5}表示分别需要按0.8和0.5的比例进行压缩
     * @return
     */
    public static ResponseStatusVO uploadImgs(MultipartFile[] files, String uploadParentDir, String uploadDir, float[] compressScales) {
        return upload(files, uploadParentDir, uploadDir, null, compressScales);
    }

    /**
     * 单文件上传
     *
     * @param file            单个文件
     * @param uploadParentDir 上传父目录
     * @param uploadDir       上传子目录，可以带有下级目录
     * @return
     */
    public static ResponseStatusVO uploadFile(MultipartFile file, String uploadParentDir, String uploadDir) {
        return upload(file, uploadParentDir, uploadDir, null, null);
    }

    /**
     * 多文件上传
     *
     * @param files           多个文件
     * @param uploadParentDir 上传父目录
     * @param uploadDir       上传子目录，可以带有下级目录
     * @return
     */
    public static ResponseStatusVO uploadFiles(MultipartFile[] files, String uploadParentDir, String uploadDir) {
        return upload(files, uploadParentDir, uploadDir, null, null);
    }

    private static String uploadFileName(String fileNameWithoutExt, String fullExt, int[] size) {
        return fileNameWithoutExt + "_" + size[0] + "x" + size[1] + fullExt;
    }

    private static String uploadFileName(String fileNameWithoutExt, String fullExt, float scale) {
        return fileNameWithoutExt + "_" + (int) (scale * 10) + fullExt;
    }

    /**
     * 上传选项类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UploadOptions {
        private String uploadParentDir;
        private String uploadDir;
        private Integer[][] compressSizes;
        private Float[] compressScales;
        private String dbSaveUrl;

        public void generateCompressSizes(String compressSizesStr) {
            String[] strArray = compressSizesStr.split(",");
            this.compressSizes = new Integer[strArray.length][2];
            for (int i = 0, len = strArray.length; i < len; i++) {
                String[] sizeArray = strArray[i].split("x");
                this.compressSizes[i][0] = Integer.valueOf(sizeArray[0]);
                this.compressSizes[i][1] = Integer.valueOf(sizeArray[1]);
            }
        }

        public void generateCompressScales(String compressScalesStr) {
            String[] strArray = compressScalesStr.split(",");
            this.compressScales = new Float[strArray.length];
            for (int i = 0, len = strArray.length; i < len; i++) {
                this.compressScales[i] = Float.valueOf(strArray[i]);
            }
        }
    }

}
