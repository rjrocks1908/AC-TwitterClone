package com.haxon.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        Toast.makeText(TwitterUsers.this,"Hello " + ParseUser.getCurrentUser().getUsername(),Toast.LENGTH_LONG).show();

        listView = findViewById(R.id.listview);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(TwitterUsers.this,android.R.layout.simple_list_item_checked,arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(TwitterUsers.this);

        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if (users.size() > 0 && e == null) {
                        for (ParseUser user : users) {
                            arrayList.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                        for (String twitterUser : arrayList){
                            if (ParseUser.getCurrentUser().getList("fanOf") != null){
                                if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)){
                                    listView.setItemChecked(arrayList.indexOf(twitterUser),true);
                                }
                            }
                        }
                    } else {
                        Toast.makeText(TwitterUsers.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutUser:
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Logging out " + ParseUser.getCurrentUser().getUsername());
                progressDialog.show();
                ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(TwitterUsers.this, "Logged out", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(TwitterUsers.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(TwitterUsers.this, e.getMessage() + "\nUnknown Error", Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                    }
            });
                break;
            case R.id.sendTweetItem:
                Intent intent = new Intent(TwitterUsers.this, SendTweetActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if (checkedTextView.isChecked()){
            Toast.makeText(TwitterUsers.this,arrayList.get(position) + " is now followed!",Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().add("fanOf",arrayList.get(position));
        }else{
            Toast.makeText(TwitterUsers.this,arrayList.get(position) + " is not followed!",Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(arrayList.get(position));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentUserFanOfList);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Toast.makeText(TwitterUsers.this,"Saved",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
