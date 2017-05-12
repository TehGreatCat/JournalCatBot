package gov.nasa.data;

import java.awt.*;

public interface ImageRepository {

    Image addImage(String imgSrc, String datetime);

    Image getImageByID(String id);

    void deleteImage(String id);

    class ImageNotFoundException extends Exception{}
}
