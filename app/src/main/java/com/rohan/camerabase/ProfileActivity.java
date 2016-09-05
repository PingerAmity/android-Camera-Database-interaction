package com.rohan.camerabase;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView imageView;
    private Uri selectedImageUri;
    private String picturePath;
    DataBaseAdapter dataBaseAdapter;
    Button b1,b2;
    ImageView iv;
    String tempDir="";
    static final int CAMERA_REQUEST = 2;
    static final int GALLERY_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter = dataBaseAdapter.open();

        recyclerView = (RecyclerView) findViewById(R.id.AppRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new RVAdapter(receiveHeadexrText(),receiveFooterText());
        recyclerView.setAdapter(adapter);

        imageView = (ImageView) findViewById(R.id.ProfilePicture);
        fetchImage(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog browseDialog = new Dialog(ProfileActivity.this);
                browseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                browseDialog.setContentView(R.layout.show_dialog);
                browseDialog.show();

                Button btnGallery = (Button) browseDialog.findViewById(R.id.btnGallery);
                Button btnTakePic = (Button) browseDialog.findViewById(R.id.btnCamera);
                btnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        browseDialog.dismiss();
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, GALLERY_REQUEST);
                    }
                });
                btnTakePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        browseDialog.dismiss();
                        String uniqueId = String.valueOf(new Date().getTime());
                        String current = "CAM_" + uniqueId + ".png";
                        File mycam = new File(tempDir, current);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        selectedImageUri = Uri.fromFile(mycam);
                        picturePath = tempDir + current;

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
                        startActivityForResult(intent, CAMERA_REQUEST);
                    }
                });
            }
        });
    }

    private void fetchImage(ImageView iview) {
        Intent in = getIntent();
        Bundle b = in.getExtras();
        String username = b.getString("Username");
        Bitmap bitmap = dataBaseAdapter.receiveImage(username);
        if(bitmap!=null)
            iview.setImageBitmap(bitmap);
    }

    private String[] receiveHeadexrText() {
        String[] header = dataBaseAdapter.getAllNames();
        return header;
    }

    private String[] receiveFooterText() {
        String[] footer = dataBaseAdapter.getAllEmails();
        return footer;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

//            Drawable d = Drawable.createFromPath(picturePath);
//            iv.setImageDrawable(d);
            Bitmap bitmapImage = null;
            try {
                bitmapImage = decodeBitmap(selectedImage );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            iv = (ImageView) findViewById(R.id.ProfilePicture);
            iv.setImageBitmap(bitmapImage);
            Intent in = getIntent();
            Bundle b = in.getExtras();
            String username = b.getString("Username");
            dataBaseAdapter.updateImage(newImage(bitmapImage),username);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK ) {
//            Drawable d = Drawable.createFromPath(picturePath);
//            iv.setImageDrawable(d);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            iv = (ImageView) findViewById(R.id.ProfilePicture);
            iv.setImageBitmap(photo);
            Intent in = getIntent();
            Bundle b = in.getExtras();
            String username = b.getString("Username");
            dataBaseAdapter.updateImage(newImage(photo),username);
        }
    }

    public byte[] newImage(Bitmap bitmap){
        byte[] image = DbBitmapUtility.getBytes(bitmap);
        return image;
    }

    public  Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }
}
