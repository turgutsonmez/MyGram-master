package turgutsonmez.com.j6_10mygram;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;

import java.util.HashMap;

import turgutsonmez.com.j6_10mygram.base.BaseActivity;
import turgutsonmez.com.j6_10mygram.model.Gonderi;

public class PostActivity extends BaseActivity {
    private ImageView imgView;
    private Button btnResim;
    private ImagePicker imagePicker;
    private UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initComp();
    }

    private void initComp() {
        imgView = (ImageView) findViewById(R.id.post_imgView);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshImagePicker();
                imagePicker.choosePicture(true);
            }
        });
        btnResim = (Button) findViewById(R.id.post_btnResim);
        btnResim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog("Dosya yükleniyor");
                Uri file = Uri.fromFile(imagePicker.getImageFile());
                mStorageRef = FirebaseStorage.getInstance().getReference().child("posts/" + file.getLastPathSegment());
                uploadTask = mStorageRef.putFile(file);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        database = FirebaseDatabase.getInstance();
                        Gonderi gonderi = new Gonderi();
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        gonderi.setGonderenId(user.getUid());
                        gonderi.setYol(downloadUrl.toString());
                        HashMap<String, Object> timestampNow = new HashMap<>();
                        timestampNow.put("timestamp", ServerValue.TIMESTAMP);
                        gonderi.setEklenmeZamani(timestampNow);
                        myRef = database.getReference().child("posts").child(gonderi.getId());
                        myRef.setValue(gonderi);
                        hideProgressDialog();
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

    private void refreshImagePicker() {
        imagePicker = new ImagePicker(this, null, new OnImagePickedListener() {
            @Override
            public void onImagePicked(Uri imageUri) {
                imgView.setImageURI(imageUri);
            }
        });
        imagePicker.setWithImageCrop(16, 10);
    }
}
