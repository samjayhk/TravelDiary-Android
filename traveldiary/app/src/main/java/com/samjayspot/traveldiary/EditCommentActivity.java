package com.samjayspot.traveldiary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditCommentActivity extends SwipeBackActivity implements View.OnClickListener {

    int pid;

    int currentCid;
    String currentComment;

    ImageView btnClose;
    TextView btnSubmit, btnUpload, txtTitle;
    EditText edtContent;

    ProgressBar progressBar;
    RelativeLayout commentLayout;
    SharedPreferences sharedPreferences;

    private void postImageRequest(String url, File file) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(180, TimeUnit.SECONDS).readTimeout(180, TimeUnit.SECONDS).build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("filename", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Snackbar snackbar = Snackbar.make(commentLayout, e.toString(), Snackbar.LENGTH_SHORT);
                View rootSnackbar = snackbar.getView();
                rootSnackbar.setBackgroundColor(Color.RED);
                snackbar.show();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            progressBar.setVisibility(View.INVISIBLE);
                            if (Boolean.parseBoolean(json.getString("result"))) {
                                edtContent.setText(edtContent.getText().toString() + "\n<img src=\"http://localhost:3001/uploads/" + json.getString("filename") + "\">");
                                Snackbar snackbar = Snackbar.make(commentLayout, json.getString("message"), Snackbar.LENGTH_SHORT);
                                View rootSnackbar = snackbar.getView();
                                rootSnackbar.setBackgroundColor(getResources().getColor(R.color.main_blue));
                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar.make(commentLayout, json.getString("message"), Snackbar.LENGTH_SHORT);
                                View rootSnackbar = snackbar.getView();
                                rootSnackbar.setBackgroundColor(Color.RED);
                                snackbar.show();
                            }
                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void putRequest(String url, String comment) {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("comment", comment)
                .build();
        Request request = new Request.Builder()
                .addHeader("x-token", getSession().get(0).toString())
                .url(url)
                .put(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("bugs", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());

                    if (Boolean.parseBoolean(json.getString("result"))) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                            }
                        });
                    } else {
                        Snackbar snackbar = Snackbar.make(commentLayout, json.getString("message"), Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(Color.RED);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    Snackbar snackbar = Snackbar.make(commentLayout, e.toString(), Snackbar.LENGTH_SHORT);
                    View rootSnackbar = snackbar.getView();
                    rootSnackbar.setBackgroundColor(Color.RED);
                    snackbar.show();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        commentLayout = (RelativeLayout) findViewById(R.id.commentLayout);
        btnClose = (ImageView) findViewById(R.id.btnCommentClose);
        btnSubmit = (TextView) findViewById(R.id.btnCommentComment);
        txtTitle = (TextView) findViewById(R.id.txtCommentTitle);
        edtContent = (EditText) findViewById(R.id.edtContent);
        btnUpload = (TextView) findViewById(R.id.btnCommentUpload);

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentCid = extras.getInt("cid");
            currentComment = extras.getString("comment");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtTitle.setText(extras.getString("title"));
                    edtContent.setText(currentComment);
                    btnSubmit.setText("Put");
                }
            });
        }

        btnClose.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

    }

    public ArrayList getSession() {
        sharedPreferences = getSharedPreferences("traveldiary", Context.MODE_PRIVATE);
        ArrayList session = new ArrayList();

        if (!sharedPreferences.getString("token", "").equals("")) {
            session.add(0, sharedPreferences.getString("token", ""));
            session.add(1, sharedPreferences.getString("username", ""));
            session.add(2, sharedPreferences.getString("reg", ""));
            session.add(3, sharedPreferences.getString("last", ""));
            session.add(4, sharedPreferences.getString("email", ""));
            return session;
        } else {
            Log.d("response", "No Session Record.");
            return null;
        }
    }

    public String nativeToUnicode(String content) {
        String characters = content;
        String ascii = "";
        for (int i = 0; i < characters.length(); i++) {
            int code = Character.codePointAt(String.valueOf(characters.charAt(i)), 0);
            if (code > 127) {
                String charAscii = Integer.toString(code, 16);
                charAscii = new String("0000").substring(charAscii.length(), 4) + charAscii;
                ascii += "\\u" + charAscii;
            } else {
                ascii += characters.charAt(i);
            }
        }
        return ascii;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCommentClose:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                break;
            case R.id.btnCommentComment:
                String content = Html.toHtml(edtContent.getText(), Html.FROM_HTML_MODE_LEGACY);
                content = this.nativeToUnicode(content.replace("&lt;", "<").replace("&gt;", ">"));
                putRequest(APIsManagement.getUpdateComment(currentCid), content);
                break;
            case R.id.btnCommentUpload:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 0);
                break;
            default:
                break;
        }
    }

    public static Bitmap getSmallBitmap(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);

        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    private File persistImage(Bitmap bitmap, String path) {
        File imageFile = new File(path);

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }

        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    progressBar = (ProgressBar) findViewById(R.id.indeterminateProgressBar);
                    progressBar.setVisibility(View.VISIBLE);
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] projection = {MediaStore.MediaColumns.DATA,
                            MediaStore.MediaColumns.MIME_TYPE};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    postImageRequest(APIsManagement.getUpload(), persistImage(getSmallBitmap(picturePath), picturePath));
                }
                break;
            default:
                break;
        }
    }
}
