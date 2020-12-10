package com.example.demoapplication.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import com.example.demoapplication.R;
import com.example.demoapplication.app.App;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import java.util.Objects;

public class GoogleAuthManager {

    private final Fragment fragment;
    private OnAuthenticationCallbackListener listener;
    private final FirebaseAuth firebaseAuth;
    private static final String TAG = "Google Sign In";
    private ActivityResultLauncher<Intent> signInRequestLauncher;
    private final ProgressDialog progressDialog;

    public GoogleAuthManager(Fragment fragment,FirebaseAuth firebaseAuth,ProgressDialog progressDialog) {
        this.fragment = fragment;
        this.firebaseAuth = firebaseAuth;
        this.progressDialog = progressDialog;
    }

    public void setSignInRequestLauncher() {
        this.signInRequestLauncher=getSignInRequestLauncher();
    }

    public void setAuthenticationListener(OnAuthenticationCallbackListener listener) {
        this.listener=listener;
    }

    public void launchSignIn() {
        Intent signInIntent = App.getInstance().getGoogleSignInClient().getSignInIntent();
        signInRequestLauncher.launch(signInIntent);
    }

    private ActivityResultLauncher<Intent> getSignInRequestLauncher() {
        return fragment.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    if (account != null) {
                        firebaseAuthWithGoogle(account.getIdToken());
                    }
                    else {
                        if(listener!=null) {
                            listener.onAuthenticationFail(Objects.requireNonNull(Objects.requireNonNull(task.getException()).toString()));
                        }
                    }
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    if(listener!=null) {
                        listener.onAuthenticationFail(Objects.requireNonNull(e.toString()));
                    }
                }
            }
            else {
                if(listener!=null) {
                    listener.onAuthenticationFail(fragment.getString(R.string.str_default_err_message));
                }
            }
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        progressDialog.setMessage(fragment.getString(R.string.str_sign_in));
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            if(listener!=null) {
                                listener.onAuthenticationSuccess(user);
                            }
                        }
                        else {
                            if(listener!=null) {
                                listener.onAuthenticationFail(Objects.requireNonNull(task.getException()).toString());
                            }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        if(listener!=null) {
                            listener.onAuthenticationFail(Objects.requireNonNull(task.getException()).toString());
                        }
                    }
                });
    }

    public interface OnAuthenticationCallbackListener {
        void onAuthenticationSuccess(FirebaseUser firebaseUser);
        void onAuthenticationFail(String msg);
    }

}
