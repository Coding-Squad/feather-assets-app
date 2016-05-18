package com.reminisense.fa.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.reminisense.fa.R;
import com.reminisense.fa.models.User;
import com.reminisense.fa.models.UserInfo;
import com.reminisense.fa.utils.FeaqEndpoint;
import com.reminisense.fa.utils.RestClient;
import com.reminisense.fa.BuildConfig;
import com.reminisense.fa.utils.ServerRequest;
import com.reminisense.fa.utils.ServerResponse;


import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity{

    private static final String Submit = "Submitting... ";

    @Bind(R.id.companyId) EditText cId;
    @Bind(R.id.ownerId) EditText oId;
    @Bind(R.id.name) EditText aName;
    @Bind(R.id.description) EditText desc;
    @Bind(R.id.takeoutInfo) EditText tOutInfo;
    @Bind(R.id.submit) Button submitButton;

    private FeaqEndpoint apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerassets);
        ButterKnife.bind(this);

        apiService = new RestClient().getApiService();

        submitButton.setOnClickListener(new SubmitClickListener());
    }

    private class SubmitClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            String cIdInput = cId.getText().toString();
            String oIdInput = oId.getText().toString();
            String aNameInput = aName.getText().toString();
            String descInput = desc.getText().toString();
            String tOutInfoInput = tOutInfo.getText().toString();

            SubmitData(cIdInput,oIdInput,aNameInput,descInput,tOutInfoInput);
            setFieldsEnabled(false);
        }
    }

    private void SubmitData(String cId, String oId, String aName, String desc, String tOutInfo){
        User user = new User(cId , oId, aName, desc, tOutInfo);
        user.getCompanyId();
        user.getOwnerId();
        user.getName();
        user.getDescription();
        user.getTakeOutInfo();

        Call<UserInfo> call = apiService.register(user);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(response.code() == 200){
                    UserInfo userInfo = response.body();
                    if(userInfo.getSuccess() == 1){
                        Toast.makeText(RegisterActivity.this, "Submit successful",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent();
                        intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.MenuActivity");
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error submitting, please try again", Toast.LENGTH_LONG).show();
                        setFieldsEnabled(true);
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Error connecting to server, please try again", Toast.LENGTH_LONG).show();
                    setFieldsEnabled(true);
                }

            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error connecting to server, please try again", Toast.LENGTH_LONG).show();
                t.printStackTrace();
                setFieldsEnabled(true);
            }
        });
    }

    private void setFieldsEnabled(boolean enabled) {
        if ( enabled ) {
            cId.setEnabled(true);
            oId.setEnabled(true);
            submitButton.setEnabled(true);
            desc.setEnabled(true);
            aName.setEnabled(true);
            tOutInfo.setEnabled(true);
        } else {
            cId.setEnabled(true);
            oId.setEnabled(true);
            submitButton.setEnabled(true);
            desc.setEnabled(true);
            aName.setEnabled(true);
            tOutInfo.setEnabled(true);
        }
    }

}
/*
    @Bind(R.id.cancel) AppCompatButton cancel;
    private EditText cId,oId,name,desc,takeOut;
    private FeaqEndpoint apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerassets);

        apiService = new RestClient().getApiService();

            final RadioGroup takeOut = (RadioGroup)findViewById(R.id.TKOgroup);
            Button bt = (Button)findViewById(R.id.submit);

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int selectedId = takeOut.getCheckedRadioButtonId();
                    RadioButton confirmation = (RadioButton)findViewById(selectedId);
                }
            });

    }

    private void initViews(View view){

        cId = (EditText)view.findViewById(R.id.companyId);
        oId = (EditText)view.findViewById(R.id.ownerId);
        name = (EditText)view.findViewById(R.id.name);
        desc = (EditText)view.findViewById(R.id.description);
        takeOut = (EditText)view.findViewById(R.id.takeoutInfo);
    }


    public void onClick(View v) {

        switch (v.getId()){
            case R.id.cancel:
                goToMenu();
                break;

            case R.id.submit:

                String companyId = cId.getText().toString();
                String ownerId = oId.getText().toString();
                String Aname = name.getText().toString();
                String description = desc.getText().toString();
                String takeOutInfo = takeOut.getText().toString();

                if(!companyId.isEmpty() && !ownerId.isEmpty() && !Aname.isEmpty()) {

                    registerProcess(companyId,ownerId,Aname);

                } else {

                    System.out.print("Fields are empty !");
                }
                break;

        }

    }

    private void registerProcess(String name, String cId,String oId){

        User user = new User();
        user.getCompanyId(R.id.companyId);
        user.getOwnerId(R.id.ownerId);
        user.getName(name);
        ServerRequest request = new ServerRequest();
        request.setUser(user);

        Call<UserInfo> call = apiService.register(User);

        call.enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                System.out.print("failed");

            }
        });
    }

    private void goToMenu(Throwable t)
    {
        Toast.makeText()
        setContentView(R.layout.activity_menu);
    }*/



