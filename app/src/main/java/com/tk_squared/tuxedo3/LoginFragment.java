package com.tk_squared.tuxedo3;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.CallbackManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class LoginFragment extends Fragment {
    private LoginButton loginButton;
    private Button skipButton;
    public Callbacks callbacks;

    public interface Callbacks{
        void onSkip();
    }
    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final TuxedoActivity tuxedoActivity = (TuxedoActivity)getActivity();
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        view.setBackgroundColor(Color.BLACK);
        skipButton = (Button) view.findViewById(R.id.skip_button);
        if(skipButton == null) skipButton = new Button(getActivity());

        callbacks = tuxedoActivity;

        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callbacks = (Callbacks) getActivity();
                callbacks.onSkip();
            }
        });
       /*
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        // If using in a fragment
        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        }

*/
        return view;
    }
}
