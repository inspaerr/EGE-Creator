package ru.blackmirrror.egetrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ru.blackmirrror.egetrainer.Fragments.AccountFragment;
import ru.blackmirrror.egetrainer.Fragments.AchievementsFragment;
import ru.blackmirrror.egetrainer.Fragments.FavoriteFragment;
import ru.blackmirrror.egetrainer.Fragments.SearchFragment;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    //начало работы меню

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_head, menu);
        return true;
    }

    //слушатель нажатий меню, переходы на фрагменты

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.item_search:
                fragment = new SearchFragment();
                break;
            case R.id.item_achievements:
                fragment = new AchievementsFragment();
                break;
            case R.id.item_favorite:
                fragment = new FavoriteFragment();
                break;
            case R.id.item_account:
                fragment = new AccountFragment();
                break;
            default:
                super.onOptionsItemSelected(item);
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_field, fragment);
        ft.commit();
        return true;
    }

}