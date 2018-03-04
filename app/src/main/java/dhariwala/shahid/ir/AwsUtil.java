package dhariwala.shahid.ir;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by shahiddhariwala on 01/03/18.
 */

public class AwsUtil {

    private static AmazonRekognition rekogClient = null;
    private static final String poolId = "us-east-1:556299b0-8278-43d8-90d6-9f584fbf36ab";
    private static final Regions region = Regions.US_EAST_1;

    public static void init(Context context) {
        if (rekogClient == null) {
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    context,
                    poolId,
                    region
            );
            rekogClient = new AmazonRekognitionClient(credentialsProvider);
        }
    }


    public static AmazonRekognition getAWSRekognition() {
        return rekogClient;
    }

    public static Image getImageFromPath(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        ByteBuffer imageBytes = ByteBuffer.wrap(stream.toByteArray());
        Image image = new Image().withBytes(imageBytes);
        return image;
    }

    public static DetectLabelsRequest getDetectLabelRequest(String path) {
        Image image = getImageFromPath(path);
        DetectLabelsRequest request = new DetectLabelsRequest();
        request.setImage(image);
        return request;
    }

    public static List<Label> getLabels(String path) {
        DetectLabelsRequest request = getDetectLabelRequest(path);
        DetectLabelsResult response = getAWSRekognition().detectLabels(request);
        List<Label> labels = response.getLabels();
        return labels;
    }

    public static DetectFacesRequest getDetectFacesRequest(String path) {
        DetectFacesRequest faceRequest = new DetectFacesRequest().withImage(getImageFromPath(path));
        return faceRequest;
    }

    public static List<FaceDetail> getFaces(String path) {
        DetectFacesRequest request = getDetectFacesRequest(path);
        DetectFacesResult response = getAWSRekognition().detectFaces(request);
        List<FaceDetail> faceDetails = response.getFaceDetails();
        return faceDetails;
    }

    public static List<CompareFacesMatch> compareFace(String face1, String face2){

        try{
            Image image_ref = getImageFromPath(face1);
            Image image_to_check = getImageFromPath(face2);

            List<CompareFacesMatch> matches = getAWSRekognition().compareFaces(new CompareFacesRequest(image_ref, image_to_check)).getFaceMatches();

            return matches;
        }catch (Exception e){
            return null;
        }

    }


}



