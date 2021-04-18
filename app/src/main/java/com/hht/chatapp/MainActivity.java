package com.hht.chatapp;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.hht.chatapp.Apdapter.ViewPagerApdapter;
import com.hht.chatapp.Fragment.ChatsFragment;
import com.hht.chatapp.Fragment.UsersFragment;
import com.hht.chatapp.Model.Users;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    FirebaseUser firebaseUser;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(firebaseUser.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                //Toast.makeText(MainActivity.this, "Users Login "+users.getUsername(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ViewPagerApdapter viewPagerApdapter = new ViewPagerApdapter(getSupportFragmentManager());
        viewPagerApdapter.addFragment(new ChatsFragment(), "Chats");
        viewPagerApdapter.addFragment(new UsersFragment(),"Users ");
        viewPager.setAdapter(viewPagerApdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.LoginOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
                return true;
        }
        return  false;
    }

    private void initWidgets(){
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }
}