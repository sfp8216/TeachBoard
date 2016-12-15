package com.rit.sfp.teachboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by steve on 12/11/2016.
 */

public class TeachboardListActivity extends Activity {
    ListView teachBoardList;
    DatabaseHelper myDb;
    ArrayList<String> boardId = new ArrayList<>();
    ArrayList<String> boardNames = new ArrayList<>();
    ArrayList<Bitmap> boardPreviews = new ArrayList<>();
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachboard_list);

        myDb = new DatabaseHelper(this);
        username = getIntent().getStringExtra("Username");

        Cursor boardNameRes = myDb.getAllTeachboards();
        while (boardNameRes.moveToNext()) {
            boardId.add(boardNameRes.getString(0));
            boardNames.add(boardNameRes.getString(1));
        }
        Cursor boardDataRes = myDb.getAllTeachboards();
        while (boardDataRes.moveToNext()) {
            if (!boardDataRes.isNull(4)) {
                Bitmap bm = BitmapFactory.decodeByteArray(boardDataRes.getBlob(4), 0, boardDataRes.getBlob(4).length);
                boardPreviews.add(bm);
            } else {
                boardPreviews.add(null);
            }
        }
        teachBoardList = (ListView) findViewById(R.id.teachboardList);
        if (boardNames.size() != 0) {
            teachBoardList.setAdapter(new TeachboardAdapter(this, boardNames, boardPreviews));
            teachBoardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String username = getIntent().getStringExtra("Username");
                    Intent joinBoardIntent = new Intent(view.getContext(), WhiteboardActivity.class);
                    joinBoardIntent.putExtra("Username", username);
                    joinBoardIntent.putExtra("BoardId", boardId.get(position));
                    startActivityForResult(joinBoardIntent, 0);
                }
            });

        } else {
            //If there are no boardNames
            teachBoardList.setVisibility(View.GONE);
            findViewById(R.id.nothingToShowTV).setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        TeachboardAdapter adapter = (TeachboardAdapter) teachBoardList.getAdapter();
        if(adapter != null)
            adapter.notifyDataSetChanged();
        boardId.clear();
        boardNames.clear();
        boardPreviews.clear();
        Cursor boardNameRes = myDb.getAllTeachboards();
        while (boardNameRes.moveToNext()) {
            boardId.add(boardNameRes.getString(0));
            boardNames.add(boardNameRes.getString(1));
        }
        Cursor boardDataRes = myDb.getAllTeachboards();
        while (boardDataRes.moveToNext()) {
            if (!boardDataRes.isNull(4)) {
                Bitmap bm = BitmapFactory.decodeByteArray(boardDataRes.getBlob(4), 0, boardDataRes.getBlob(4).length);
                boardPreviews.add(bm);
            } else {
                boardPreviews.add(null);
            }
        }
        teachBoardList = (ListView) findViewById(R.id.teachboardList);
        if (boardNames.size() != 0) {
            teachBoardList.setAdapter(new TeachboardAdapter(this, boardNames, boardPreviews));
            teachBoardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String username = getIntent().getStringExtra("Username");
                    Intent joinBoardIntent = new Intent(view.getContext(), WhiteboardActivity.class);
                    joinBoardIntent.putExtra("Username", username);
                    joinBoardIntent.putExtra("BoardId", boardId.get(position));
                    startActivityForResult(joinBoardIntent, 0);
                }
            });

        } else {
            //If there are no boardNames
            teachBoardList.setVisibility(View.GONE);
            findViewById(R.id.nothingToShowTV).setVisibility(View.VISIBLE);
        }

    }
}


class TeachboardAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    ArrayList<String> result;
    Context context;
    ArrayList<Bitmap> imageId = new ArrayList<Bitmap>();

    public TeachboardAdapter(TeachboardListActivity teachboardListActivity, ArrayList<String> boardnames, ArrayList<Bitmap> images) {
        result = boardnames;
        context = teachboardListActivity;
        imageId = images;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.activity_listboard, null);
        holder.tv = (TextView) rowView.findViewById(R.id.board_name);
        holder.img = (ImageView) rowView.findViewById(R.id.board_preview);
        Resources res = holder.img.getResources();
        if (imageId.size() == 0 || imageId.get(0) == null) {
            holder.img.setBackgroundColor(Color.WHITE);
        } else {
            holder.img.setBackground(new BitmapDrawable(imageId.get(position)));
        }
        holder.tv.setText(result.get(position));
        return rowView;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }
}
