package com.futurework.codefriends.templates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.futurework.codefriends.R;

public class InfoHolder extends AppCompatActivity {

    private final ImageView popupMenu = findViewById(R.id.pop_menu);
    private final ImageView Image = findViewById(R.id.image);
    private final TextView name = findViewById(R.id.name);
    private final TextView status = findViewById(R.id.status);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_holder);
    }

    private void popupMenu() {
        PopupMenu p = new PopupMenu(this, popupMenu);
        p.getMenuInflater().inflate(R.menu.info_holder_menu, p .getMenu());
        p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(),item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        p.show();
    }
}