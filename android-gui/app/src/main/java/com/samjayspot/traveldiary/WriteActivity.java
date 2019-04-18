package com.samjayspot.traveldiary;

/**
 * THIS PROJECT IS A UNIVERSITY PROJECT AND MADE BY SCHOOL FOR HIGHER AND PROFESSIONAL EDUCATION WITH COVENTRY UNIVERSITY (UK).
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * AUTHOR INFORMATION:
 * NAME: TSANG Long Fung (187107130)
 * CONTACT NUMBER: (+852) 6679 2339
 * CONTACT EMAIL: 187107130@stu.vtc.edu.hk
 */

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

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
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

public class WriteActivity extends SwipeBackActivity implements View.OnClickListener {

    ImageView btnClose;
    TextView btnSubmit, btnUpload;
    EditText edtTitle, edtContent;
    ProgressBar progressBar;

    RelativeLayout writeLayout;
    MaterialSpinner spinner;
    SharedPreferences sharedPreferences;

    int currentTid = 0;
    ArrayList<Integer> tids = new ArrayList<Integer>();
    ArrayList<String> tags = new ArrayList<String>();

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

    private void postImageRequest(String url, File file) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(180, TimeUnit.SECONDS).readTimeout(180, TimeUnit.SECONDS).build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("filename", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Snackbar snackbar = Snackbar.make(writeLayout, e.toString(), Snackbar.LENGTH_SHORT);
                View rootSnackbar = snackbar.getView();
                rootSnackbar.setBackgroundColor(Color.RED);
                snackbar.show();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    progressBar.setVisibility(View.INVISIBLE);
                    if (Boolean.parseBoolean(json.getString("result"))) {
                        edtContent.setText(edtContent.getText().toString() + "\n<img src=\"http://localhost:3001/uploads/" + json.getString("filename") + "\">");
                        Snackbar snackbar = Snackbar.make(writeLayout, json.getString("message"), Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(getResources().getColor(R.color.main_blue));
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(writeLayout, json.getString("message"), Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(Color.RED);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void postRequest(String url, String tid, String subject, String content) {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("tid", tid)
                .add("subject", subject)
                .add("content", content)
                .build();
        Request request = new Request.Builder()
                .addHeader("x-token", getSession().get(0).toString())
                .url(url)
                .post(formBody)
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
                        Intent intent = new Intent(WriteActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else {

                        Log.d("bugs", json.getString("message"));
                        Snackbar snackbar = Snackbar.make(writeLayout, json.getString("message"), Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(Color.RED);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    Snackbar snackbar = Snackbar.make(writeLayout, "Something went wrong", Snackbar.LENGTH_SHORT);
                    View rootSnackbar = snackbar.getView();
                    rootSnackbar.setBackgroundColor(Color.RED);
                    snackbar.show();
                }
            }
        });
    }

    public void getRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

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

                        JSONArray tagsList = json.getJSONArray("tags");

                        for (int i = 0; i < tagsList.length(); i++) {
                            JSONObject tag = tagsList.getJSONObject(i);
                            tids.add(tag.getInt("tid"));
                            tags.add(tag.getString("name"));
                        }
                        spinner.setItems(tags);
                    } else {
                        Log.d("bugs", json.getString("message"));
                    }

                } catch (JSONException e) {
                    Log.d("bugs", e.toString());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        writeLayout = (RelativeLayout) findViewById(R.id.writeLayout);
        spinner = (MaterialSpinner) findViewById(R.id.writeSpinner);
        btnClose = (ImageView) findViewById(R.id.btnClose);
        btnSubmit = (TextView) findViewById(R.id.btnSubmit);
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtContent = (EditText) findViewById(R.id.edtContent);
        btnUpload = (TextView) findViewById(R.id.btnUpload);

        btnClose.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        try {
            this.getRequest(APIsManagement.getTagsList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                currentTid = position;
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

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
            Log.d("response", String.valueOf(session));
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
            case R.id.btnClose:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                break;
            case R.id.btnSubmit:
                String title = this.nativeToUnicode(edtTitle.getText().toString());
                String content = Html.toHtml(edtContent.getText(), Html.FROM_HTML_MODE_LEGACY);
                content = this.nativeToUnicode(content.replace("&lt;", "<").replace("&gt;", ">"));

                postRequest(APIsManagement.getWriteThread(), String.valueOf(currentTid), title, content);

                break;
            case R.id.btnUpload:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 0);
                break;
            default:
                break;
        }
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
