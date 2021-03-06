package vit.vithandbook.fragment;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import vit.vithandbook.adapter.CardListAdapter;
import vit.vithandbook.R;
import vit.vithandbook.adapter.onItemClickListener;


public class SubSectionFragment extends BackHandlerFragment {

    public  String mainCategory ;
    ArrayList<String> Subtopics ;
    CardListAdapter rvAdapter ;
    ProgressBar load ;
    android.support.v7.widget.RecyclerView recyclerView;

    public static SubSectionFragment newInstance(String mainCategory)
    {
        SubSectionFragment frag = new SubSectionFragment() ;
        frag.mainCategory = mainCategory ;
        return frag ;
    }
    public SubSectionFragment()
    {

    }

    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container , Bundle SavedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_sub_section,container,false);
        load = (ProgressBar)view.findViewById(R.id.ssprogressbar);
        recyclerView =(android.support.v7.widget.RecyclerView) view.findViewById(R.id.subSectionList);
        new AsyncTask<Void,Void,Void>()
        {

            @Override
            protected void onPreExecute()
            {
                load.setVisibility(View.VISIBLE);
            }
            @Override
            protected Void doInBackground(Void... params) {
                fetchSubSectionData();
                // sample data remove to test with actual data
                Subtopics.add("Hostel Fees");
                Subtopics.add("Mess Refund and other stuff");
                Subtopics.add("9 pointer advantages");
                Subtopics.add("Hostel Fees");
                Subtopics.add("Hostel Fees");
                Subtopics.add("Identity card");
                return null;
            }
            @Override
            protected void onPostExecute(Void res)
            {
              load.setVisibility(View.GONE);
              rvAdapter = new CardListAdapter(getActivity(),Subtopics);
                rvAdapter.setOnItemClickListener(new onItemClickListener() {
                    @Override
                    public void onItemClick(String data) {
                        rvItemClick(data);
                    }
                });
                recyclerView.setAdapter(rvAdapter);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
            }
        }.execute();
        return view ;
    }
    @Override
    public boolean onBackPressed()
    {
        return false ;
    }

    public void rvItemClick(String data)
    {
        Fragment hideFragment = getActivity().getFragmentManager().findFragmentByTag("subSectionFragment");
        Fragment articleFragment = ArticleListFragment.newInstance(data);
       getActivity().getFragmentManager().beginTransaction().setCustomAnimations(R.transition.fade_in, R.transition.fade_out, R.transition.fade_in, R.transition.fade_out)
       .hide(hideFragment).add(R.id.mainNavigator,articleFragment,"articleListFragment").addToBackStack(null).commit();
    }

    public void fetchSubSectionData()
    {
        SQLiteDatabase db = getActivity().openOrCreateDatabase("Handbook", Context.MODE_PRIVATE,null);
        Cursor cursor = db.rawQuery("SELECT `sub_category` from `articles` Where main_category = ?",new String[]{mainCategory});
        Subtopics = new ArrayList<String>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            Subtopics.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
    }
}

