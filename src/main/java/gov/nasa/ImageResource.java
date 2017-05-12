package gov.nasa;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;

@Path("/image")
public class ImageResource {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response createImage(@QueryParam("Image") Image image, @QueryParam("datetime") String datetime){
        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Image getImageByID(@QueryParam("ImgID") String ImgID){
        return null;
    }

}
