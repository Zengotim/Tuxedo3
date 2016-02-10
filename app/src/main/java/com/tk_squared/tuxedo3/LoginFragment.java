package com.tk_squared.tuxedo3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class LoginFragment extends Fragment {
    private LoginButton loginButton;
    private Button skipButton;
    public Callbacks callbacks;
   // private CallbackManager callbackManager;


    public interface Callbacks{
        void onLoginFinish();
    }
    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final TuxedoActivity tuxedoActivity = (TuxedoActivity)getActivity();


        View view = inflater.inflate(R.layout.fragment_login, container, false);
       // view.setBackgroundColor(Color.BLACK);
       // tuxedoActivity.setContentView(view);
        skipButton = (Button) view.findViewById(R.id.skip_button);
        if(skipButton == null) skipButton = new Button(getActivity());



        loginButton = (LoginButton)view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);

        callbacks = tuxedoActivity;

        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callbacks = (Callbacks) getActivity();
                callbacks.onLoginFinish();
            }
        });

        loginButton.registerCallback(tuxedoActivity.getCallbackManager(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("LOGIN", "Facebook logged in");
                callbacks.onLoginFinish();

            }

            @Override
            public void onCancel() {
                Log.i("FB Cancel", "Canceled login");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i("FacebookErr: ", e.toString());
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final TuxedoActivity tuxedoActivity = (TuxedoActivity)getActivity();
        tuxedoActivity.getCallbackManager().onActivityResult(requestCode,
                resultCode, data);
    }
}
