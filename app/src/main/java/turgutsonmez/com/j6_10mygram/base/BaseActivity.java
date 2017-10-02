package turgutsonmez.com.j6_10mygram.base;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import turgutsonmez.com.j6_10mygram.R;

/**
 * Created by TurgutSonmez on 15.09.2017.
 */

public class BaseActivity extends AppCompatActivity {
  public ProgressDialog mProgressDialog;
  public ProgressBar mProgressBar;
  public FirebaseAuth mAuth;
  public FirebaseUser user;
  public FirebaseDatabase database;
  public DatabaseReference myRef;
  public StorageReference mStorageRef;


  public void showProgressDialog(String title, String message) {
    if (mProgressDialog == null) {
      mProgressDialog = new ProgressDialog(this);
      mProgressDialog.setTitle(title);
      mProgressDialog.setMessage(message);
      mProgressDialog.setIndeterminate(true);
    }
    mProgressDialog.show();
  }

  public void showProgressDialog(String message) {
    if (mProgressDialog == null) {
      mProgressDialog = new ProgressDialog(this);
      mProgressDialog.setTitle("Lütfen Bekleyin");
      mProgressDialog.setMessage(message);
      mProgressDialog.setIndeterminate(true);
    }
    mProgressDialog.show();
  }

  public void hideProgressDialog() {
    if (mProgressDialog != null && mProgressDialog.isShowing()) {
      mProgressDialog.dismiss();
    }
  }
}
