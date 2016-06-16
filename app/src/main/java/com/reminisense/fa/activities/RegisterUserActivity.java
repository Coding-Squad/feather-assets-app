package com.reminisense.fa.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.reminisense.fa.BuildConfig;
import com.reminisense.fa.R;
import com.reminisense.fa.models.RestResult;
import com.reminisense.fa.models.User;
import com.reminisense.fa.utils.FeatherAssetsWebService;
import com.reminisense.fa.utils.RestClient;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Marjo on 5/23/2016.
 */
public class RegisterUserActivity extends AppCompatActivity {

    private static final String Submit = "Submitting.... ";
    @Bind(R.id.fnText)
    EditText fnText;
    @Bind(R.id.lnText)
    EditText lnText;
    @Bind(R.id.userImage)
    ImageView userImage;
    @Bind(R.id.posText)
    EditText posText;
    @Bind(R.id.descText)
    EditText descText;
    @Bind(R.id.emailText)
    EditText emailText;
    @Bind(R.id.regUserOpenCamera)
    AppCompatImageButton regUserOpenCamera;
    @Bind(R.id.btnSbmt)
    AppCompatButton btnSbmt;
    Uri captureImageUri;

    //directory name for captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Feather Assets";

    private static final int TAKE_PIC = 4;

    private FeatherAssetsWebService apiService;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_registeruser);
        ButterKnife.bind(this);

        //setTitle("Register User");

        apiService = new RestClient().getApiService();

        btnSbmt.setOnClickListener(new SubmitClickListener());
        regUserOpenCamera.setOnClickListener(new OpenCameraListener());
    }

    private class OpenCameraListener implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureImageUri = getOutputMediaFileUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri);
            //start intent
            startActivityForResult(intent, TAKE_PIC);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == TAKE_PIC) {
            if (resultCode == RESULT_OK) {
                previewCapturedImage();
            }
        }
    }

    /*
    * Display image from a path to ImageView
    */
    private void previewCapturedImage() {
        try {
            userImage.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(captureImageUri.getPath(),
                    options);

            userImage.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private class SubmitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            User user = new User();

            user.setFirstName(fnText.getText().toString());
            user.setLastName(lnText.getText().toString());
            user.setPosition(posText.getText().toString());
            user.setDescription(descText.getText().toString());
            user.setEmail(emailText.getText().toString());
            /*
                FIXME: image URL for user
                user.setUserImageUrls(captureImageUri.getPath());
             */

            Log.d(RegisterUserActivity.class.toString(), user.toString());
            submitData(user);
            setFieldsEnabled(false);
        }

        private void submitData(User user) {
            Call<RestResult> call = apiService.registerUser(user);
            call.enqueue(new Callback<RestResult>() {
                @Override
                public void onResponse(Call<RestResult> call, Response<RestResult> response) {
                    if (response.code() == 200) {
                        RestResult restResult = response.body();
                        if ("OK".equals(restResult.getResult())) {
                            Toast.makeText(RegisterUserActivity.this, "Submit Successful", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent();
                            intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.RegisterActivity");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterUserActivity.this, restResult.getMessage(), Toast.LENGTH_LONG).show();
                            setFieldsEnabled(true);
                        }
                    } else {
                        Toast.makeText(RegisterUserActivity.this, "Error connecting to server, please try again", Toast.LENGTH_LONG).show();
                        setFieldsEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<RestResult> call, Throwable t) {
                    Toast.makeText(RegisterUserActivity.this, "Error connecting to server, please try again", Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                    setFieldsEnabled(true);
                }
            });
        }

    }

    public void setFieldsEnabled(boolean enabled) {
        if (enabled) {
            fnText.setEnabled(true);
            lnText.setEnabled(true);
            posText.setEnabled(true);
            descText.setEnabled(true);
            emailText.setEnabled(true);
        } else {
            fnText.setEnabled(false);
            lnText.setEnabled(false);
            posText.setEnabled(false);
            descText.setEnabled(false);
            emailText.setEnabled(false);
        }
    }

    /**
     * Creating file uri to store image
     */
    public Uri getOutputMediaFileUri() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed to create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return Uri.fromFile(mediaFile);
    }

}
