package com.samjayspot.traveldiary;

/**
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * @samjayhk
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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.List;
import java.util.Locale;
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
    TextView btnSubmit, btnUpload, btnCamera;
    EditText edtTitle, edtContent;
    ProgressBar progressBar;
    Button btnGPS;
    Uri outPutfileUri;

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
                    final JSONObject json = new JSONObject(response.body().string());
                    progressBar.setVisibility(View.INVISIBLE);
                    if (Boolean.parseBoolean(json.getString("result"))) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    edtContent.setText(edtContent.getText().toString() + "\n<img src=\"" + APIsManagement.defaultServer + "uploads/" + json.getString("filename") + "\">");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

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

    public String getGPSLocation() {
        String country_name = null;
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(getApplicationContext(), new Locale("EN"));
        for (String provider : lm.getAllProviders()) {
            @SuppressWarnings("ResourceType") Location location = lm.getLastKnownLocation(provider);
            if (location != null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        country_name = addresses.get(0).getCountryName();
                        Log.d("Current Country : ", country_name);
                        return country_name;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
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
        btnGPS = (Button) findViewById(R.id.btnGPS);
        btnCamera = (TextView) findViewById(R.id.btnCamera);

        btnClose.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnGPS.setOnClickListener(this);
        btnCamera.setOnClickListener(this);

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
                if (edtTitle.getText().toString() != "" && edtContent.getText().toString() != "") {
                    String title = this.nativeToUnicode(edtTitle.getText().toString());
                    String content = Html.toHtml(edtContent.getText(), Html.FROM_HTML_MODE_LEGACY);
                    content = this.nativeToUnicode(content.replace("&lt;", "<").replace("&gt;", ">"));

                    postRequest(APIsManagement.getWriteThread(), String.valueOf(currentTid), title, content);
                } else {
                    Snackbar snackbar = Snackbar.make(writeLayout, "Please fill in Title & Content!", Snackbar.LENGTH_SHORT);
                    View rootSnackbar = snackbar.getView();
                    rootSnackbar.setBackgroundColor(Color.RED);
                    snackbar.show();
                }
                break;
            case R.id.btnUpload:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 0);
                break;
            case R.id.btnCamera:
                Intent captureIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                final String authority = "com.samjayspot.traveldiary.fileprovider";
                boolean b = Build.VERSION.SDK_INT >= 24;
                File file = new File(Environment.getExternalStorageDirectory(), "capture_by_" + getSession().get(1).toString() + ".jpg");
                try {
                    outPutfileUri = b
                            ? FileProvider.getUriForFile(WriteActivity.this, authority, file)
                            : Uri.fromFile(file);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
                    startActivityForResult(captureIntent, 1);
                } catch (NullPointerException e) {
                    Log.d("bugs", e.toString());
                }
                break;
            case R.id.btnGPS:
                String locationResults = getGPSLocation();
                if (locationResults != null) {
                    if (tags.contains(locationResults)) {
                        for (int i = 0; i < tags.size(); i++) {
                            if (tags.get(i).contains(locationResults)) {
                                spinner.setSelectedIndex(i);
                                currentTid = i;
                            }
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(writeLayout, "location not found!", Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(Color.RED);
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(writeLayout, "location not found!", Snackbar.LENGTH_SHORT);
                    View rootSnackbar = snackbar.getView();
                    rootSnackbar.setBackgroundColor(Color.RED);
                    snackbar.show();
                }
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
            case 1:
                if (resultCode == RESULT_OK) {
                    progressBar = (ProgressBar) findViewById(R.id.indeterminateProgressBar);
                    progressBar.setVisibility(View.VISIBLE);

                    File file = new File(outPutfileUri.getPath());

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), outPutfileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String rePath = file.toString().replace("/my_image", "");
                    postImageRequest(APIsManagement.getUpload(), persistImage(getSmallBitmap(rePath), rePath));
                }
                break;
            default:
                break;
        }
    }
}
