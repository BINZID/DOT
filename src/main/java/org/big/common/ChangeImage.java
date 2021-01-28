package org.big.common;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;

/**
 * @Author: WangTianshan(王天山)
 * @Description:
 * @Created Date: 2018/1/31 9:44
 * @Modified By:
 * @Last Modified Date:
 */
public class ChangeImage {
    public static void changeImageSize(String oldPath,String newPath, int maxSize) {
        try {
            Thumbnails.of(oldPath).size(maxSize, maxSize).toFile(newPath);
        } catch (IOException e) {
            try {
                Thumbnails.of(oldPath).scale(0.5f).toFile(newPath);
            } catch (IOException e1) {
            }
        }
    }
}
