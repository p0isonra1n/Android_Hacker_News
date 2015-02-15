package com.isaacparker.hackernews;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends Activity {

    RecyclerView storyList;
    int currentItem = 0;

    final int MAXITEMS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storyList = (RecyclerView) findViewById(R.id.cardList);
        storyList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        storyList.setLayoutManager(llm);

        final ArrayList<Story> Stories = new ArrayList<Story>();

        storyList.addOnItemTouchListener(new RecyclerItemClickListener(this, storyList, new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Stories.get(position).url));
                startActivity(browserIntent);
            }

            @Override
            public void onItemLongClick(View view, int position)
            {
                Toast.makeText(MainActivity.this, "Long Click!", Toast.LENGTH_SHORT).show();
            }
        }));


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            final JSONArray jsonObject = new JSONArray((String)response);
                            //Load only 10 results
                            for(int i = 0; i < MAXITEMS; i++){
                                //Log.i("HackerNews", String.valueOf(jsonObject.get(i)));
                                currentItem = i;
                                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                                String url ="https://hacker-news.firebaseio.com/v0/item/" + jsonObject.getLong(i) + ".json?print=pretty";
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener() {
                                            @Override
                                            public void onResponse(Object response) {
                                                try {
                                                    JSONObject jsonClass = new JSONObject((String)response);
                                                    //Toast.makeText(MainActivity.this, (String)response, Toast.LENGTH_LONG).show();
                                                    Story story = new Story();
                                                    story.by = jsonClass.getString("by");
                                                    story.id = jsonClass.getLong("id");
                                                    if(jsonClass.has("kids")){
                                                        JSONArray jsonKids = jsonClass.getJSONArray("kids");
                                                        ArrayList<Integer> kids = new ArrayList<Integer>();
                                                        for(int j = 0; j < jsonKids.length(); j++){
                                                            kids.add(jsonKids.getInt(j));
                                                        }
                                                        story.kids = kids;
                                                    }
                                                    story.score = jsonClass.getInt("score");
                                                    story.time = jsonClass.getLong("time");
                                                    story.title = jsonClass.getString("title");
                                                    story.type = jsonClass.getString("type");
                                                    story.url = jsonClass.getString("url");
                                                    Stories.add(story);

                                                    displayStories(Stories);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                queue.add(stringRequest);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void displayStories(ArrayList<Story> Stories){
        for(int i = 0; i < Stories.size(); i++){
            StoryAdapter sa = new StoryAdapter(this, Stories);
            storyList.setAdapter(sa);
            sa.notifyDataSetChanged();
            //Toast.makeText(MainActivity.this, String.valueOf(i) + " - " + Stories.get(i).title, Toast.LENGTH_SHORT).show();
        }
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
}
