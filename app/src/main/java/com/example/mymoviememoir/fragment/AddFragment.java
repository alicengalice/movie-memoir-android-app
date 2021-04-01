package com.example.mymoviememoir.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.R;

public class AddFragment extends Fragment {
    private EditText etMessage;
    private Button button;
    public AddFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.add_fragment, container, false);
        etMessage = view.findViewById(R.id.et_message);
        button = view.findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString().trim();
                if (!message.isEmpty() ) {
                    SharedPreferences sharedPref= getActivity().
                            getSharedPreferences("Message", Context.MODE_PRIVATE);
                    SharedPreferences.Editor spEditor = sharedPref.edit();
                    spEditor.putString("message", message);
                    spEditor.apply();
                }
            }
        });
        return view;
    }
}