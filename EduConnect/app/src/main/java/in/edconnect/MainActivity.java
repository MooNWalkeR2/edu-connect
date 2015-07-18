package in.edconnect;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

import in.edconnect.JsonRequest.NewCustomJsonRequest;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText username , password;
    private Button loginbutton;
    private TextView wrongcomb;
    private boolean status;
    int studentornot=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginbutton = (Button) findViewById(R.id.submitbutton_activitymain);
        username = (EditText) findViewById(R.id.usernameinput_activitymain);
        password = (EditText) findViewById(R.id.passwordinput_activitymain);
        wrongcomb = (TextView) findViewById(R.id.wrongcomb_activitymain);


        loginbutton.setOnClickListener(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        //Check for the internet connection

        //Check if the inputs are not empty
        if(username.getText().toString().equals("")){
            wrongcomb.setText("Please Enter Username!");
            wrongcomb.setVisibility(View.VISIBLE);
            return;
        }
        if(password.getText().toString().equals("")){
            wrongcomb.setText("Please Enter Password!");
            wrongcomb.setVisibility(View.VISIBLE);
            return;
        }

        //Send username and password to web services and return the status here
        if(authenticateThisUser(username.getText().toString(),password.getText().toString())){
            //If status is true go to new activity otherwise make that textview visible


            if(studentornot==1) {
                startActivity(new Intent(MainActivity.this, HomePageActivity.class));
            }else{
                startActivity(new Intent(MainActivity.this,HomePageActivityTeacher.class));
            }
            finish();
        }else{
            wrongcomb.setText("Wrong Username and Password Combination!");
            wrongcomb.setVisibility(View.VISIBLE);
            return;
        }





    }

    private boolean authenticateThisUser(String username , String password){
        status = false;

        String url = "";      //Add your web service url here
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String,String> params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);                          //Customize according to your web services
        NewCustomJsonRequest newCustomJsonRequest = new NewCustomJsonRequest(Request.Method.GET, url, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                                                                  //Handle your response here and change the status boolean
                                                                    //Change the value of studentornot
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                                                                    //Handle your error here
            }
        });
        //requestQueue.add(newCustomJsonRequest);                   // Send request to web services

        tempCheckUsers(username,password);
        return status;
    }

    private void tempCheckUsers(String username , String password){
        if(username.equals("preet") && password.equals("preet")){
            status = true;
            studentornot = 1;
        }else if(username.equals("rathin") && password.equals("rathin")){
            status = true;
            studentornot = 1;
        }else if(username.equals("subbarao") && password.equals("subbarao")){
            status = true;
            studentornot = 0;
        }
    }

}
