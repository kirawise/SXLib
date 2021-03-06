package com.will.sxlib.searchBook;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.will.sxlib.R;
import com.will.sxlib.base.BaseActivity;
import com.will.sxlib.util.NetworkHelper;

/**
 * Created by Will on 2016/5/25.
 */
public class BookListActivity extends BaseActivity {
    private BookListAdapter bookListAdapter;
    private MaterialSearchView searchView;
    private String keyword;
    @Override
    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.avtivity_book_list);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.book_list_recycler_view);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.book_list_tool_bar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.book_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        searchView = (MaterialSearchView) findViewById(R.id.book_list_search_view);
        searchView.setLayoutTransition(new LayoutTransition());
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyword = query;
                bookListAdapter.refreshWithAnotherKeyword(query);
                searchView.closeSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        toolbar.setTitle("搜索中···");
        toolbar.setLayoutTransition(new LayoutTransition());
        keyword = getIntent().getStringExtra("query");
        int searchWay = getIntent().getIntExtra("mode",0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bookListAdapter = new BookListAdapter(keyword,searchWay);

        recyclerView.setAdapter(bookListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem search = menu.findItem(R.id.book_list_menu_search);
        searchView.setMenuItem(search);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.book_list_menu_sort_by_date_asc:
                bookListAdapter.refreshWithAnotherSort(NetworkHelper.SORT_BY_DATE_ASC);
                return true;
            case R.id.book_list_menu_sort_by_date_desc:
                bookListAdapter.refreshWithAnotherSort(NetworkHelper.SORT_BY_DATE_DESC);
                return true;
            case R.id.book_list_menu_sort_by_matching:
                bookListAdapter.refreshWithAnotherSort(NetworkHelper.SORT_BY_MATCHING);
                return true;
            default:
                return false;
        }
    }

}
