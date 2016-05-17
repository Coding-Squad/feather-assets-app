package com.reminisense.fa.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
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
import com.reminisense.fa.assets.UserInfo.User;
import com.reminisense.fa.utils.FeaqEndpoint;
import com.reminisense.fa.utils.ServerRequest;
import com.reminisense.fa.utils.ServerResponse;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity{

    @Bind(R.id.cancel) AppCompatButton cancel;
    private EditText cId,oId,name,desc,takeOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerassets);

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

    private void registerProcess(String name, String email,String password){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://128.199.83.107:8080/FeatherAssets/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FeaqEndpoint requestInterface = retrofit.create(FeaqEndpoint.class);

        User user = new User();
        user.companyId(cId);
        user.ownerId(oId);
        user.name(name);
        ServerRequest request = new ServerRequest();
        request.setUser(user);
        Call<ServerResponse> response = FeaqEndpoint.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
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

    private void goToMenu(){
        setContentView(R.layout.activity_menu);
    }
}

